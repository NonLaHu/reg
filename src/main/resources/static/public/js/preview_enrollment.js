
const LANG_KEY = 'utycc_lang';
const THEME_KEY = 'theme';

// --- elements
const langToggle = document.getElementById('langToggle');
const themeToggle = document.getElementById('themeToggle');
const acceptBtn = document.getElementById('acceptBtn');
const rejectBtn = document.getElementById('rejectBtn');
const previewModal = document.getElementById('previewModal');
const previewImage = document.getElementById('previewImage');
const downloadPreview = document.getElementById('downloadPreview');
const closePreview = document.getElementById('closePreview');
const fileReplaceInput = document.getElementById('fileReplaceInput');

// enrollment id (from page) - pulled from header meta or data attribute if you set one.
const enrollmentId = (function () {
  const el = document.querySelector('header .meta strong');
  return el ? el.textContent.trim() : null;
})();

// --- auth token helper (same approach used earlier)
function getAuthToken() {
  const token = localStorage.getItem('jwt');
  if (token) return token;
  const cookies = document.cookie.split(';').map(c => c.trim());
  for (const c of cookies) {
    if (c.startsWith('Authorization=')) return c.substring('Authorization='.length);
  }
  return null;
}

// --- language
function setLanguage(lang) {
  document.querySelectorAll('.lang').forEach(el => {
    const en = el.dataset.en || '';
    const my = el.dataset.my || '';
    if (lang === 'my' && my) el.textContent = my;
    else el.textContent = en || my;
  });
  localStorage.setItem(LANG_KEY, lang);
}
(function initLanguage() {
  const saved = localStorage.getItem(LANG_KEY) || 'en';
  setLanguage(saved);
  langToggle.addEventListener('click', () => {
    const cur = localStorage.getItem(LANG_KEY) || 'en';
    setLanguage(cur === 'en' ? 'my' : 'en');
  });
})();

// --- theme
function setTheme(theme) {
  document.documentElement.setAttribute('data-theme', theme);
  localStorage.setItem(THEME_KEY, theme);
}
(function initTheme() {
  const saved = localStorage.getItem(THEME_KEY) || 'light';
  setTheme(saved);
  themeToggle.addEventListener('click', () => {
    const cur = localStorage.getItem(THEME_KEY) || 'light';
    setTheme(cur === 'light' ? 'dark' : 'light');
  });
})();

// --- show preview modal
function showPreview(src) {
  previewImage.src = src;
  downloadPreview.href = src;
  previewModal.style.display = 'flex';
}
function closeModal() {
  previewModal.style.display = 'none';
  previewImage.src = '';
}
document.querySelectorAll('.view-file').forEach(btn => {
  btn.addEventListener('click', e => {
    // Find the closest .file-tile parent
    const tile = btn.closest('.file-tile');
    if (!tile) return;

    // Get the <img> inside the tile
    const img = tile.querySelector('img.protected-img');
    if (!img) return alert('Image not found');

    // Use the current real-time src
    const src = img.src;

    showPreview(src);
  });
});

closePreview.addEventListener('click', closeModal);
previewModal.addEventListener('click', (e) => {
  if (e.target.id === 'modalBackdrop') closeModal();
});

// --- accept / reject
async function postAction(action) {
  if (!enrollmentId) return alert('No enrollment id available');
  const token = getAuthToken();
  try {
    const res = await fetch(`/api/admin/enrollments/${enrollmentId}/${action}`, {
      method: 'POST',
      headers: token ? { 'Authorization': token.startsWith('Bearer') ? token : `Bearer ${token}`, 'Accept': 'application/json' } : { 'Accept': 'application/json' }
    });
    const json = await res.json().catch(() => null);
    if (!res.ok) {
      alert((json && json.message) ? json.message : `Action failed: ${res.status}`);
      return;
    }
    alert((json && json.message) ? json.message : `${action} succeeded`);
    // optionally refresh page
    location.reload();
  } catch (err) { console.error(err); alert('Network error'); }
}

if (acceptBtn) acceptBtn.addEventListener('click', () => postAction('accept'));
if (rejectBtn) rejectBtn.addEventListener('click', () => {
  const reason = prompt('Rejection remark (optional):') || '';
  // append remark as query param (or change to body if backend expects JSON)
  postAction(`reject?remark=${encodeURIComponent(reason)}`);
});

// --- Inline editing
// Click on any .field-edit button, convert nearest .field-value to input/textarea, show save/cancel
function createEditor(currentText, type = 'text') {
  if (type === 'textarea') {
    const ta = document.createElement('textarea');
    ta.value = currentText === '—' ? '' : currentText;
    ta.rows = 3;
    return ta;
  } else if (type === 'date') {
    const ip = document.createElement('input');
    ip.type = 'date';
    ip.value = currentText === '—' ? '' : currentText;
    return ip;
  } else {
    const ip = document.createElement('input');
    ip.type = 'text';
    ip.value = currentText === '—' ? '' : currentText;
    return ip;
  }
}

async function saveField(path, value, entityId) {
  if (!enrollmentId) {
    alert('No enrollment id available');
    return { success: false, message: 'No enrollment id available' };
  }

  const token = getAuthToken();
  let res, json;

  try {
    res = await fetch(`/api/admin/enrollments/${enrollmentId}/field`, {
      method: 'PATCH',
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
        ...(token ? { 'Authorization': token.startsWith('Bearer') ? token : `Bearer ${token}` } : {})
      },
      body: JSON.stringify({ path, value, entityId })
    });

    try {
      json = await res.json();
    } catch {
      json = null;
    }
  } catch (err) {
    console.error('Network error:', err);
    alert(`Network error: ${err.message}`);
    return { success: false, message: `Network error: ${err.message}` };
  }

  if (!res.ok) {
    let message = 'Update failed';
    switch (res.status) {
      case 400: message = json?.message || 'Invalid request data'; break;
      case 401: message = 'Unauthorized. Please log in again.'; break;
      case 403: message = 'Forbidden. You do not have permission.'; break;
      case 404: message = 'Enrollment not found'; break;
      case 409: message = json?.message || 'Conflict. Duplicate or invalid data.'; break;
      case 500: message = 'Server error. Please try again later.'; break;
      default: message = json?.message || `Error ${res.status}`;
    }
    alert(message);
    return false;
  }

  const successMessage = json?.message || 'Updated successfully';
  alert(successMessage);
  return true;
}

document.querySelectorAll('.field-edit').forEach(btn => {
  btn.addEventListener('click', (e) => {
    const fieldPath = btn.getAttribute('data-field');
    if (!fieldPath) return;

    const parentId = btn.dataset.parentId ? Number(btn.dataset.parentId) : undefined;
    console.log(parentId)

    // find sibling .field-value in same container
    const container = btn.closest('.field') || btn.parentElement;
    const valueEl = container.querySelector('.field-value');
    if (!valueEl) return;

    // Prevent double edit
    if (container.querySelector('.editor')) return;

    const currentText = valueEl.textContent.trim();
    // attempt to detect basic type
    let type = 'text';
    if (/^\d{4}-\d{2}-\d{2}$/.test(currentText)) type = 'date';
    if (currentText.length > 60) type = 'textarea';

    const editor = createEditor(currentText, type);
    editor.classList.add('editor');
    valueEl.style.display = 'none';
    // create action buttons
    const actions = document.createElement('div');
    actions.className = 'editor-actions';
    const saveBtn = document.createElement('button');
    saveBtn.className = 'btn tiny';
    saveBtn.textContent = 'Save';
    const cancelBtn = document.createElement('button');
    cancelBtn.className = 'btn tiny';
    cancelBtn.textContent = 'Cancel';
    actions.appendChild(saveBtn);
    actions.appendChild(cancelBtn);
    container.appendChild(editor);
    container.appendChild(actions);
    editor.focus();

    cancelBtn.addEventListener('click', () => {
      editor.remove();
      actions.remove();
      valueEl.style.display = '';
    });

    saveBtn.addEventListener('click', async () => {
      const newVal = editor.value;
      const ok = await saveField(fieldPath, newVal, parentId);
      if (ok) {
        valueEl.textContent = newVal || '—';
        valueEl.classList.toggle('dim', !newVal);
        editor.remove();
        actions.remove();
        valueEl.style.display = '';
      }
    });
  });
});

// --- File editing (replace file)
let currentReplaceFileId = null;
document.querySelectorAll('.edit-file').forEach(btn => {
  btn.addEventListener('click', () => {
    const fileId = btn.getAttribute('data-fileid');
    if (!fileId) return alert('No file id');
    currentReplaceFileId = fileId;
    fileReplaceInput.click();
  });
});

fileReplaceInput.addEventListener('change', async (ev) => {
  const file = ev.target.files && ev.target.files[0];
  if (!file) return;
  if (!enrollmentId || !currentReplaceFileId) return alert('Missing context');

  // confirm
  if (!confirm('Replace file?')) { fileReplaceInput.value = ''; currentReplaceFileId = null; return; }

  const fd = new FormData();
  fd.append('file', file);

  const token = getAuthToken();
  try {
    const res = await fetch(`/api/admin/enrollments/${enrollmentId}/files/${currentReplaceFileId}`, {
      method: 'POST',
      headers: token ? { 'Authorization': token.startsWith('Bearer') ? token : `Bearer ${token}` } : {},
      body: fd
    });
    const json = await res.json().catch(() => null);
    if (!res.ok) {
      alert((json && json.message) ? json.message : `Replace failed: ${res.status}`);
      return;
    }
    alert((json && json.message) ? json.message : 'File replaced');
    location.reload();
  } catch (err) { console.error(err); alert('Network error'); }
});

// --- small UX: make file thumbnails clickable too
document.querySelectorAll('.file-tile .thumb img').forEach(img => {
  img.addEventListener('click', () => {
    const src = img.getAttribute('src');
    if (!src) return;
    showPreview(src);
  });
});

//IMG fetched dynamically
async function initProtectedImages(options = {}) {
  const selector = options.selector || 'img.protected-img';
  const token = getAuthToken(); // implement below

  if (!token && options.fallbackToDirect !== false) {
    document.querySelectorAll(selector).forEach(img => {
      const url = img.getAttribute('data-protected-src');
      if (url) img.src = url;
    });
    return;
  }

  // helper to fetch single image and set src
  async function fetchAndSet(img) {
    const url = img.getAttribute('data-protected-src');
    if (!url) return;

    // optional: show spinner class
    img.classList.add('loading');

    try {
      const res = await fetch(url, {
        method: 'GET',
        headers: token ? { 'Authorization': token.startsWith('Bearer') ? token : `Bearer ${token}` } : {}
      });

      if (!res.ok) {
        throw new Error(`Failed to fetch image ${url}: ${res.status}`);
      }

      const blob = await res.blob();
      const objectUrl = URL.createObjectURL(blob);
      img.src = objectUrl;
      // store for cleanup later if you want to revoke
      img.dataset.objectUrl = objectUrl;

      // remove loading marker
      img.classList.remove('loading');
      img.classList.remove('noimg');
      //   document.querySelectorAll('.file-tile').forEach(tile => {
      //     const img = tile.querySelector('img.protected-img');
      //     const downloadLink = tile.querySelector('a[download]');

      //     if (!img || !downloadLink) return;

      //     // Initially tie download href to img src
      //     downloadLink.href = img.src;
      // });

    } catch (err) {
      console.warn('protected image load error', err);
      img.classList.remove('loading');
      img.classList.add('noimg'); // css hides or dims it
    }
  }

  // If page has many images, lazy-load with IntersectionObserver
  const imgs = Array.from(document.querySelectorAll(selector));
  if ('IntersectionObserver' in window) {
    const io = new IntersectionObserver((entries, obs) => {
      entries.forEach(en => {
        if (en.isIntersecting) {
          const img = en.target;
          obs.unobserve(img);
          fetchAndSet(img);
        }
      });
    }, { rootMargin: '200px' });

    imgs.forEach(img => io.observe(img));
  } else {
    // fallback: immediately fetch all
    await Promise.all(imgs.map(img => fetchAndSet(img)));
  }
}

initProtectedImages({ selector: 'img.protected-img', fallbackToDirect: true });

//dorm accept and reject
const dormAcceptBtn = document.getElementById('dormAcceptBtn');
const dormRejectBtn = document.getElementById('dormRejectBtn');
const dormStatus = document.getElementById('dormStatus');

// Accept Dorm
if (dormAcceptBtn) {

  dormAcceptBtn.addEventListener('click', async () => {
    const dormId = dormAcceptBtn.dataset.dormId; // set th:data-dorm-id="${enrollment.dorm.id}" in button
    const jwt = localStorage.getItem("jwt");

    try {
      const response = await fetch(`/api/dorm/${dormId}/accept`, {
        method: 'POST',
        headers: { 'Authorization': `Bearer ${jwt}` }
      });

      if (!response.ok) throw new Error("Failed to accept");

      const data = await response.json();
      dormStatus.textContent = data.status.toLowerCase();
      dormStatus.className = 'status ' + data.status.toLowerCase();

      alert('Dorm accepted successfully');
    } catch (err) {
      alert('Error: ' + err.message);
    }
  });

  // Reject Dorm
  dormRejectBtn.addEventListener('click', async () => {
    const reason = "Still in Development";

    const dormId = dormRejectBtn.dataset.dormId; // set th:data-dorm-id="${enrollment.dorm.id}" in button
    const jwt = localStorage.getItem("jwt");

    try {
      const response = await fetch(`/api/dorm/${dormId}/reject`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${jwt}`
        },
        body: JSON.stringify({ reason })
      });

      if (!response.ok) throw new Error("Failed to reject");

      const data = await response.json();
      dormStatus.textContent = data.status.toLowerCase() + (data.reason ? ": " + data.reason : "");
      dormStatus.className = 'status ' + data.status.toLowerCase();

      alert('Dorm rejected successfully');
    } catch (err) {
      alert('Error: ' + err.message);
    }
  });

}
