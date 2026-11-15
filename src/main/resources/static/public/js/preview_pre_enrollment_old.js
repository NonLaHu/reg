(function () {
  'use strict';

  const container = document.getElementById('passPreviewContainer');
  if (!container) return;
  const passUrl = container.dataset.passUrl;
  console.log(passUrl);
  const filename = container.dataset.passFilename || 'file';
  const thumbWrap = document.getElementById('thumbWrap');
  const placeholder = document.getElementById('thumbPlaceholder');
  const spinner = document.getElementById('thumbSpinner');
  const thumbText = document.getElementById('thumbText');

  const fileMeta = document.getElementById('fileMeta');
  const fileNameEl = document.getElementById('fileName');
  const fileTypeEl = document.getElementById('fileType');
  const fileError = document.getElementById('fileError');

  const openBtn = document.getElementById('openBtn');
  const downloadBtn = document.getElementById('downloadBtn');

  const modal = document.getElementById('previewModal');
  const modalContent = document.getElementById('modalContent');
  const modalClose = document.getElementById('modalClose');

  const acceptBtn = document.getElementById('acceptBtn');
  const rejectBtn = document.getElementById('rejectBtn');

  const enrollmentId = (function () {
    const el = document.querySelector('.thisId');
    if (!el) return null;
    const match = el.textContent.trim().match(/\d+/); // match the first number
    return match ? Number(match[0]) : null;
  })();


  // get JWT from localStorage
  function getJwt() {
    return localStorage.getItem('jwt'); // adjust key name if different
  }

  function showSpinner(on) {
    spinner.style.display = on ? 'inline-block' : 'none';
    thumbText.style.display = on ? 'none' : '';
  }

  async function fetchProtectedAsset(url) {
    const token = getJwt();
    if (!token) throw new Error('Authentication token missing (localStorage key "jwt").');

    const res = await fetch(url, {
      method: 'GET',
      headers: {
        'Authorization': 'Bearer ' + token
      }
    });

    if (!res.ok) {
      const msg = `Failed to fetch file: ${res.status} ${res.statusText}`;
      throw new Error(msg);
    }

    const contentType = res.headers.get('Content-Type') || '';
    const blob = await res.blob();
    return { blob, contentType };
  }

  function clearThumb() {
    thumbWrap.querySelectorAll('img, object, iframe').forEach(el => el.remove());
    fileMeta.style.display = 'none';
    placeholder.style.display = '';
    fileError.style.display = 'none';
  }

  async function renderPreview() {
    clearThumb();
    if (!passUrl) {
      // no file present
      thumbText.textContent = 'No pass certificate uploaded';
      return;
    }

    showSpinner(true);
    try {
      const { blob, contentType } = await fetchProtectedAsset(passUrl);

      // show meta
      fileNameEl.textContent = filename;
      fileTypeEl.textContent = contentType || 'unknown';
      fileMeta.style.display = '';

      // if image -> render <img>
      if (contentType.startsWith('image/')) {
        const url = URL.createObjectURL(blob);
        const img = document.createElement('img');
        img.src = url;
        img.alt = filename;
        img.onload = () => {
          // image loaded, hide placeholder
          placeholder.style.display = 'none';
        };
        img.addEventListener('click', () => openModalAsImage(url, filename));
        thumbWrap.appendChild(img);

        // Download handler uses blob URL; we keep the blob in closure
        downloadBtn.onclick = (e) => downloadBlob(blob, filename);
        openBtn.onclick = () => openModalAsImage(url, filename);

      } else if (contentType === 'application/pdf' || filename.toLowerCase().endsWith('.pdf')) {
        // PDF: place a simple icon and allow open/download
        placeholder.style.display = 'none';
        const pdfIcon = document.createElement('div');
        pdfIcon.style.display = 'flex';
        pdfIcon.style.flexDirection = 'column';
        pdfIcon.style.alignItems = 'center';
        pdfIcon.style.justifyContent = 'center';
        pdfIcon.innerHTML = `
          <svg width="64" height="64" viewBox="0 0 24 24" fill="none" aria-hidden="true">
            <path d="M14 3H6a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V9z" stroke="#ef4444" stroke-width="1.2"/>
            <path d="M14 3v6h6" stroke="#ef4444" stroke-width="1.2"/>
          </svg>
        `;
        thumbWrap.appendChild(pdfIcon);

        openBtn.onclick = async () => {
          // create blob url and open in new tab
          const url = URL.createObjectURL(blob);
          window.open(url, '_blank');
        };
        downloadBtn.onclick = () => downloadBlob(blob, filename);
      } else {
        // unknown type, fallback icon + download
        placeholder.style.display = 'none';
        const icon = document.createElement('div');
        icon.innerHTML = `<div style="font-weight:700;color:var(--muted)">${filename}</div>`;
        thumbWrap.appendChild(icon);
        openBtn.onclick = () => { /* could open in new tab */ const url = URL.createObjectURL(blob); window.open(url, '_blank'); };
        downloadBtn.onclick = () => downloadBlob(blob, filename);
      }

    } catch (err) {
      fileError.style.display = '';
      fileError.textContent = err.message;
      placeholder.style.display = '';
      fileMeta.style.display = 'none';
    } finally {
      showSpinner(false);
    }
  }

  function downloadBlob(blob, filename) {
    const a = document.createElement('a');
    const url = URL.createObjectURL(blob);
    a.href = url;
    a.download = filename || 'file';
    document.body.appendChild(a);
    a.click();
    setTimeout(() => {
      URL.revokeObjectURL(url);
      a.remove();
    }, 2000);
  }

  // modal helpers
  function openModalAsImage(url, alt) {
    modalContent.innerHTML = '';
    const img = document.createElement('img');
    img.src = url;
    img.alt = alt || '';
    modalContent.appendChild(img);
    modal.classList.add('active');
    modal.setAttribute('aria-hidden', 'false');
  }
  modalClose.addEventListener('click', closeModal);
  modal.addEventListener('click', (e) => { if (e.target === modal) closeModal(); });
  function closeModal() {
    modal.classList.remove('active');
    modal.setAttribute('aria-hidden', 'true');
    modalContent.innerHTML = '';
  }
  renderPreview();


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


  async function saveField(path, value, entityId) {
    if (!enrollmentId) {
      alert('No enrollment id available');
      return { success: false, message: 'No enrollment id available' };
    }

    const token = getJwt();
    let res, json;

    try {
      res = await fetch(`/api/admin/pre-enrollments-old/${enrollmentId}/field`, {
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


})();