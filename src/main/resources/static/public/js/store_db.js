/**
 * FormDB autosave/restore utility using IndexedDB.
 * - Saves by name (or id) and partitions by formId.
 * - Skips elements with class "dbIgnore" and input[type="file"].
 * - Handles checkboxes, radio groups, selects (multi), textareas, inputs.
 *
 * Usage:
 *   FormDB.init(); // automatically binds all forms on page (each form should ideally have an id)
 *   // Or use API:
 *   // await FormDB.open(); 
 *   // FormDB.saveForm(document.getElementById('myForm'));
 */

const FormDB = (function () {
  const DB_NAME = 'FormDB';
  const STORE_NAME = 'FormInputs';
  const DB_VERSION = 1;
  const SAVE_DEBOUNCE_MS = 600;

  let dbPromise = null;
  const boundForms = new WeakSet();
  const debounceMap = new WeakMap();

  /* ---------------------- IndexedDB wrapper ---------------------- */
  function openDB() {
    if (dbPromise) return dbPromise;
    dbPromise = new Promise((resolve, reject) => {
      const req = indexedDB.open(DB_NAME, DB_VERSION);

      req.onupgradeneeded = (evt) => {
        const db = evt.target.result;
        if (!db.objectStoreNames.contains(STORE_NAME)) {
          // keyPath is 'formId' so each form stores one object
          db.createObjectStore(STORE_NAME, { keyPath: 'formId' });
        }
      };

      req.onsuccess = (evt) => resolve(evt.target.result);
      req.onerror = (evt) => reject(evt.target.error);
    });
    return dbPromise;
  }

  async function putRecord(record) {
    const db = await openDB();
    return new Promise((resolve, reject) => {
      const tx = db.transaction(STORE_NAME, 'readwrite');
      const store = tx.objectStore(STORE_NAME);
      const r = store.put(record);
      r.onsuccess = () => resolve(r.result);
      r.onerror = (e) => reject(e.target.error);
    });
  }

  async function getRecord(formId) {
    const db = await openDB();
    return new Promise((resolve, reject) => {
      const tx = db.transaction(STORE_NAME, 'readonly');
      const store = tx.objectStore(STORE_NAME);
      const r = store.get(formId);
      r.onsuccess = () => resolve(r.result);
      r.onerror = (e) => reject(e.target.error);
    });
  }

  async function deleteRecord(formId) {
    const db = await openDB();
    return new Promise((resolve, reject) => {
      const tx = db.transaction(STORE_NAME, 'readwrite');
      const store = tx.objectStore(STORE_NAME);
      const r = store.delete(formId);
      r.onsuccess = () => resolve();
      r.onerror = (e) => reject(e.target.error);
    });
  }

  /* ---------------------- Helpers ---------------------- */
  function getFormId(form, fallbackIndex = 0) {
    if (!form) return null;
    if (form.id) return `form:${form.id}`;
    if (form.name) return `form-name:${form.name}`;
    // last fallback: include path and index to reduce collisions across pages
    return `form:path:${location.pathname}:index:${fallbackIndex}`;
  }

  function getSaveableControls(form) {
    // all inputs/selects/textarea in given form (excluding .dbIgnore and type=file and disabled)
    const controls = Array.from(form.querySelectorAll('input, textarea, select'));
    return controls.filter(el => {
      if (el.classList && el.classList.contains('dbIgnore')) return false;
      if (el.disabled) return false;
      if (el.tagName.toLowerCase() === 'input' && el.type === 'file') return false;
      // skip buttons
      if (el.tagName.toLowerCase() === 'input' && ['button', 'submit', 'reset'].includes(el.type)) return false;
      return true;
    });
  }

  function normalizeKeyForControl(control) {
    // prefer name, fallback id; if neither, null -> skip
    return control.name || control.id || null;
  }

  /* ---------------------- Save logic ---------------------- */
  async function saveForm(form) {
    if (!form) return;
    const fallbackIndex = Array.from(document.forms).indexOf(form);
    const formId = getFormId(form, fallbackIndex);
    const controls = getSaveableControls(form);

    // Build data object keyed by control name (or id)
    const data = { formId, _savedAt: Date.now(), _version: 1 };

    const handledNames = new Set();

    for (const control of controls) {
      const key = normalizeKeyForControl(control);
      if (!key || handledNames.has(key)) continue;

      // Handle radio group
      if (control.tagName.toLowerCase() === 'input' && control.type === 'radio') {
        handledNames.add(key);
        const radios = Array.from(form.querySelectorAll(`input[name="${key}"][type="radio"]`));
        const checked = radios.find(r => r.checked);
        data[key] = { type: 'radio', value: checked ? checked.value : null };
        continue;
      }

      // Handle checkboxes (could be group or single)
      if (control.tagName.toLowerCase() === 'input' && control.type === 'checkbox') {
        const checkboxes = Array.from(form.querySelectorAll(`input[name="${key}"][type="checkbox"]`));
        if (checkboxes.length > 1) {
          handledNames.add(key);
          const checkedValues = checkboxes.filter(c => c.checked).map(c => c.value);
          data[key] = { type: 'checkbox-group', value: checkedValues };
        } else {
          handledNames.add(key);
          data[key] = { type: 'checkbox', checked: control.checked, value: control.value };
        }
        continue;
      }

      // Handle select (multiple)
      if (control.tagName.toLowerCase() === 'select') {
        handledNames.add(key);
        const sel = control;
        if (sel.multiple) {
          const selected = Array.from(sel.options).filter(o => o.selected).map(o => o.value);
          data[key] = { type: 'select-multiple', value: selected };
        } else {
          data[key] = { type: 'select', value: sel.value };
        }
        continue;
      }

      // All other controls: input/text/textarea
      handledNames.add(key);
      data[key] = { type: control.type || control.tagName.toLowerCase(), value: control.value };
    }

    // Persist
    try {
      await putRecord(data);
      // console.debug('Form saved', formId);
    } catch (err) {
      console.error('FormDB save error:', err);
    }
  }

  /* ---------------------- Restore logic ---------------------- */
  async function restoreForm(form, callback) {
    if (!form) {
      if (typeof callback === 'function') callback();
      return;
    }
    const fallbackIndex = Array.from(document.forms).indexOf(form);
    const formId = getFormId(form, fallbackIndex);
    let record;
    try {
      record = await getRecord(formId);
    } catch (err) {
      console.error('FormDB restore error:', err);
      if (typeof callback === 'function') callback(err);
      return;
    }
    if (!record) {
      if (typeof callback === 'function') callback();
      return;
    }

    const controls = getSaveableControls(form);

    // We'll map by name/id for quick access
    const controlMap = new Map();
    controls.forEach(ctrl => {
      const key = normalizeKeyForControl(ctrl);
      if (key) {
        if (!controlMap.has(key)) controlMap.set(key, []);
        controlMap.get(key).push(ctrl);
      }
    });

    Object.keys(record).forEach(key => {
      if (key === 'formId' || key.startsWith('_')) return;
      const entry = record[key];
      const controlsList = controlMap.get(key);
      if (!controlsList || controlsList.length === 0) return;

      try {
        if (entry.type === 'radio') {
          // mark the radio with that value as checked
          const radios = controlsList; // all radios with this name
          radios.forEach(r => r.checked = (r.value === entry.value));
        } else if (entry.type === 'checkbox-group') {
          const values = entry.value || [];
          controlsList.forEach(cb => cb.checked = values.includes(cb.value));
        } else if (entry.type === 'checkbox') {
          const cb = controlsList[0];
          cb.checked = !!entry.checked;
          if (entry.value !== undefined && cb.value !== entry.value) {
            // keep existing input value but optionally restore value (rare)
            cb.value = entry.value;
          }
        } else if (entry.type === 'select-multiple') {
          const sel = controlsList[0];
          Array.from(sel.options).forEach(opt => {
            opt.selected = (entry.value || []).includes(opt.value);
          });
        } else if (entry.type === 'select') {
          controlsList[0].value = entry.value ?? '';
        } else {
          // input / textarea / other
          controlsList[0].value = entry.value ?? '';
        }
      } catch (err) {
        console.warn(`FormDB: failed to restore ${key}`, err);
      }
    });

    if (typeof callback === 'function') callback();
  }

  /* ---------------------- Binding / debounce ---------------------- */
  function debounceSave(form) {
    if (debounceMap.has(form)) {
      clearTimeout(debounceMap.get(form));
    }
    const t = setTimeout(() => saveForm(form), SAVE_DEBOUNCE_MS);
    debounceMap.set(form, t);
  }

  function bindForm(form) {
    if (!form || boundForms.has(form)) return;
    boundForms.add(form);

    // restore first then run any form-specific logic via a callback if needed
    restoreForm(form, () => {
      // custom logic can run here externally if needed
      // e.g., after restore you may re-run UI init code
      const ev = new CustomEvent('formdb:restored', { detail: { form } });
      form.dispatchEvent(ev);
    });

    // events to trigger save:
    // - input (typing)
    // - change (selects, checkbox, radio)
    // - blur (explicit)
    // - submit -> on success, we can delete saved draft if desired
    form.addEventListener('input', () => debounceSave(form));
    form.addEventListener('change', () => debounceSave(form));
    form.addEventListener('blur', (e) => {
      // blur events bubble only in modern browsers; to be safe, use capture false
      const target = e.target;
      if (target && !target.classList.contains('dbIgnore')) debounceSave(form);
    }, true);

    // on submit: optionally remove saved draft (comment/uncomment as required)
    form.addEventListener('submit', async (e) => {
      // If you want to delete saved draft after successful submission,
      // call FormDB.delete(form) once server confirms success.
      // For now, we can delete preemptively or leave it:
      // await deleteRecord(getFormId(form));
    });
  }

  /* ---------------------- Public API ---------------------- */
  return {
    open: openDB,
    init: async function () {
      await openDB();
      // bind all forms currently on the page
      const forms = Array.from(document.querySelectorAll('form'));
      forms.forEach((form) => bindForm(form));
      // expose a simple event for other code to hook into restore completion:
      console.info('FormDB initialized, forms bound:', forms.map(f => getFormId(f, Array.from(document.forms).indexOf(f))));
    },
    saveForm,
    restoreForm,
    deleteForm: async function (form) {
      if (!form) return;
      const fallbackIndex = Array.from(document.forms).indexOf(form);
      const formId = getFormId(form, fallbackIndex);
      await deleteRecord(formId);
    },
    clearAll: async function () {
      const db = await openDB();
      return new Promise((resolve, reject) => {
        const tx = db.transaction(STORE_NAME, 'readwrite');
        const store = tx.objectStore(STORE_NAME);
        const req = store.clear();
        req.onsuccess = () => resolve();
        req.onerror = (e) => reject(e.target.error);
      });
    },
    bindForm, // expose for manual binding if you create forms dynamically
  };
})();

const dbName = "FormDB";
const storeName = "FormInputs";
let db;

// --- Open DB ---
const request = indexedDB.open(dbName, 1);

request.onupgradeneeded = (event) => {
  db = event.target.result;
  if (!db.objectStoreNames.contains(storeName)) {
    db.createObjectStore(storeName, { keyPath: "id" });
  }
};

request.onsuccess = (event) => {
  db = event.target.result;
  console.log("IndexedDB ready");

  // Auto-restore on load
  restoreFormData();

  // Auto-bind saving
  bindAutoSave();
};

request.onerror = (event) => {
  console.error("DB error:", event.target.error);
};

function getSaveableFields(form = document.querySelector("form")) {
  const all = Array.from(form.querySelectorAll("input, textarea, select"));
  return all.filter(el =>
    !el.classList.contains("dbIgnore") &&
    el.type !== "file"
  );
}

// --- Save ---
function saveFormData() {
  if (!db) return;

  const form = document.querySelector("form");
  if (!form) return;

  const fields = getSaveableFields(form);
  const data = {};

  fields.forEach((field) => {
    const key = field.name || field.id;
    if (!key) return;

    if (field.type === "checkbox" || field.type === "radio") {
      data[key] = { type: field.type, checked: field.checked, value: field.value };
    } else {
      data[key] = { type: field.type || field.tagName.toLowerCase(), value: field.value };
    }
  });

  const tx = db.transaction([storeName], "readwrite");
  const store = tx.objectStore(storeName);
  store.put({ id: form.id || "defaultForm", data });

}

// --- Restore ---
function restoreFormData() {
  if (!db) return;

  const form = document.querySelector("form");
  if (!form) return;

  const tx = db.transaction([storeName], "readonly");
  const store = tx.objectStore(storeName);
  const req = store.get(form.id || "defaultForm");

  req.onsuccess = (event) => {
    const record = event.target.result;
    if (!record || !record.data) return;

    // console.log("Restoring:", record.data);
    const fields = getSaveableFields(form);

    fields.forEach(field => {
      const saved = record.data[field.name || field.id];
      if (!saved) return;
      if (saved.type === "checkbox" || saved.type === "radio") {
        field.checked = !!saved.checked;
      } else {
        try {

          field.value = saved.value ?? "";
        } catch (e) {
          console.log(e)
          console.log(field)
        }

      }
    });
    queueMicrotask(() => {
      form.dispatchEvent(new CustomEvent("formdb:restored", {
        bubbles: true,
        detail: { form }
      }));
    });

  };
  req.onerror = (event) => console.error("Restore failed:", event.target.error);
}

// --- Auto-bind ---
function bindAutoSave() {
  const fields = getSaveableFields();
  fields.forEach(field => {
    field.addEventListener("blur", saveFormData);
  });

  // fallback periodic save
  setInterval(saveFormData, 5000);
}

document.querySelector('form').addEventListener('formdb:restored', () => {
  if (typeof newStudentFormLogic === 'function') newStudentFormLogic();
});

//newStudentForm
function newStudentFormLogic() {
  const momDeceaseInput = document.getElementById('mom-decease-date');
  if (momDeceaseInput && typeof updateMomSignDisplay === 'function') updateMomSignDisplay();

  const dadDeceaseInput = document.getElementById('dad-decease-date');
  if (dadDeceaseInput && typeof updateDadSignDisplay === 'function') updateDadSignDisplay();
}

