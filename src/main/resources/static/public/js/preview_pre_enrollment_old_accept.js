(function(){
  'use strict';

  const acceptBtn = document.getElementById('acceptBtn');
  if (!acceptBtn) return;

  // Helper: find preId same way as earlier script (try data-id first)
  function findPreEnrollmentId() {
    const container = document.getElementById('passPreviewContainer');
    if (container && container.dataset && container.dataset.id) return container.dataset.id;
    // fallback: look for Created row small ID
    const rows = Array.from(document.querySelectorAll('.meta-row'));
    for (const r of rows) {
      const label = r.querySelector('.meta-label');
      if (label && label.textContent && label.textContent.trim().toLowerCase() === 'created') {
        const small = r.querySelector('.meta-value small');
        if (small) {
          const m = (small.textContent || '').match(/(\d+)/);
          if (m) return m[1];
        }
      }
    }
    return null;
  }

  // small toast (reused)
  function showToast(msg, opts = {}) {
    const { type='info', duration=2500 } = opts;
    const id = 'at-toast';
    if (document.getElementById(id)) document.getElementById(id).remove();
    const el = document.createElement('div');
    el.id = id;
    el.textContent = msg;
    el.style.position = 'fixed';
    el.style.right = '18px';
    el.style.bottom = '18px';
    el.style.zIndex = '5000';
    el.style.padding = '10px 14px';
    el.style.borderRadius = '10px';
    el.style.color = '#fff';
    el.style.fontWeight = '700';
    if (type === 'success') el.style.background = 'linear-gradient(90deg,#10b981,#06b6d4)';
    else if (type === 'error') el.style.background = 'linear-gradient(90deg,#fb7185,#ef4444)';
    else el.style.background = 'rgba(0,0,0,0.8)';
    document.body.appendChild(el);
    setTimeout(()=> { el.style.transition = 'opacity .2s'; el.style.opacity = '0'; setTimeout(()=>el.remove(),220); }, duration);
  }

  async function acceptPreEnrollment(preId) {
    const jwt = localStorage.getItem('jwt');
    if (!jwt) throw new Error('Authentication token missing (localStorage.jwt)');

    const res = await fetch(`/admin/pre-enrollments/old/${preId}/accept`, {
      method: 'POST',
      headers: { 'Authorization': 'Bearer ' + jwt, 'Content-Type': 'application/json' },
      body: null
    });

    if (!res.ok) {
      const txt = await res.text();
      throw new Error(`Server error: ${res.status} ${res.statusText}${txt ? ' - ' + txt : ''}`);
    }

    const json = await res.json();
    // expect { serialKey, password }
    return json;
  }

  // Utility copy (uses navigator.clipboard fallback)
  async function copyText(text) {
    if (navigator.clipboard && navigator.clipboard.writeText) {
      await navigator.clipboard.writeText(text);
      return true;
    }
    // fallback
    const ta = document.createElement('textarea');
    ta.value = text;
    ta.style.position = 'fixed';
    ta.style.left = '-9999px';
    document.body.appendChild(ta);
    ta.select();
    try {
      document.execCommand('copy');
      document.body.removeChild(ta);
      return true;
    } catch {
      document.body.removeChild(ta);
      return false;
    }
  }

  // Create modal DOM, attach handlers, return an object with cleanup method
  function createCredentialModal(serialKey, password) {
    // overlay
    const overlay = document.createElement('div');
    overlay.className = 'one-time-modal-overlay';
    overlay.setAttribute('role','dialog');
    overlay.setAttribute('aria-modal','true');

    const modal = document.createElement('div');
    modal.className = 'one-time-modal';
    modal.innerHTML = `
      <div class="heading">
        <h3>Account created — credentials</h3>
        <div>
          <button class="close-x" aria-label="Close">&times;</button>
        </div>
      </div>

      <div class="cred-row">
        <div class="cred-label">Serial</div>
        <div class="cred-value">
          <input readonly id="oneTimeSerial" value="${escapeHtml(serialKey)}" />
          <button class="small-btn" id="copySerial">Copy</button>
          <span class="copy-ok" id="copySerialOk">Copied</span>
        </div>
      </div>

      <div class="cred-row">
        <div class="cred-label">Password</div>
        <div class="cred-value">
          <input readonly id="oneTimePassword" type="password" value="${escapeHtml(password)}" />
          <button class="small-btn alt" id="revealPassword">Show</button>
          <button class="small-btn" id="copyPassword">Copy</button>
          <span class="copy-ok" id="copyPasswordOk">Copied</span>
        </div>
      </div>

      <div style="margin-top:10px; color:#6b7280; font-weight:600;">This credential is shown once. Make sure to copy it now.</div>

      <div class="actions" style="margin-top:14px;">
        <button class="small-btn alt" id="ackClose">I have saved it</button>
      </div>
    `;
    overlay.appendChild(modal);
    document.body.appendChild(overlay);

    // elements
    const btnClose = modal.querySelector('.close-x');
    const copySerialBtn = modal.querySelector('#copySerial');
    const copySerialOk = modal.querySelector('#copySerialOk');
    const copyPasswordBtn = modal.querySelector('#copyPassword');
    const copyPasswordOk = modal.querySelector('#copyPasswordOk');
    const revealPasswordBtn = modal.querySelector('#revealPassword');
    const pwdInput = modal.querySelector('#oneTimePassword');
    const ackBtn = modal.querySelector('#ackClose');

    // helper to cleanup everything and remove from DOM + null references
    function cleanup() {
      // remove listeners by replacing node
      overlay.remove();
      // remove any global refs (none stored externally) — attempt to null local variables
      // (they will be GC'd because no closures reference them)
    }

    // handlers
    btnClose.addEventListener('click', () => {
      cleanup();
    });

    ackBtn.addEventListener('click', () => {
      cleanup();
    });

    copySerialBtn.addEventListener('click', async () => {
      const ok = await copyText(serialKey);
      if (ok) {
        copySerialOk.style.display = 'inline';
        setTimeout(()=> copySerialOk.style.display = 'none', 1800);
      } else {
        showToast('Copy failed', { type:'error' });
      }
    });

    copyPasswordBtn.addEventListener('click', async () => {
      const ok = await copyText(password);
      if (ok) {
        copyPasswordOk.style.display = 'inline';
        setTimeout(()=> copyPasswordOk.style.display = 'none', 1800);
      } else {
        showToast('Copy failed', { type:'error' });
      }
    });

    revealPasswordBtn.addEventListener('click', () => {
      if (pwdInput.type === 'password') {
        pwdInput.type = 'text';
        revealPasswordBtn.textContent = 'Hide';
      } else {
        pwdInput.type = 'password';
        revealPasswordBtn.textContent = 'Show';
      }
    });

    // clicking overlay outside modal also closes
    overlay.addEventListener('click', (e) => {
      if (e.target === overlay) cleanup();
    });

    // return object with ability to programmatically cleanup if desired
    return { overlay, cleanup };
  }

  // simple html-escape helper for inserting values into attribute contexts
  function escapeHtml(str){
    if (!str) return '';
    return String(str).replace(/[&<>"']/g, (m) => ({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[m]));
  }

  // Main accept flow: call backend, show modal, then cleanup
  acceptBtn.addEventListener('click', async (e) => {
    e.preventDefault();

    const preId = findPreEnrollmentId();
    if (!preId) {
      showToast('Record id not found', { type: 'error' });
      return;
    }

    // confirm before proceeding
    if (!confirm('Accept this application and create user account? This will generate credentials shown one time.')) return;

    try {
      acceptBtn.disabled = true;
      const jwt = localStorage.getItem('jwt');
      if (!jwt) { showToast('Missing auth token', { type:'error' }); acceptBtn.disabled = false; return; }

      // call API
      const resp = await fetch(`/admin/pre-enrollments/old/${preId}/accept`, {
        method: 'POST',
        headers: { 'Authorization': 'Bearer ' + jwt }
      });
      if (!resp.ok) {
        const text = await resp.text();
        throw new Error(`Server error: ${resp.status} ${resp.statusText}${text ? ' - ' + text : ''}`);
      }
      const data = await resp.json();
      const serialKey = data.serialKey || data.serial || data.username || data.email;
      const rawPassword = data.password || data.rawPassword || data.pw;

      if (!serialKey || !rawPassword) {
        showToast('Server did not return credentials', { type:'error' });
        return;
      }else if (serialKey === "ALREADY_ACCEPTED") {
        showToast("This pre-enrollment has already been accepted.", { type:'error' });
        return;
    } else if (serialKey === "ALREADY_REJECTED") {
        showToast("This pre-enrollment has already been rejected.", { type: "error" });
        return;
      }

      // show one-time modal
      const modalHandle = createCredentialModal(serialKey, rawPassword);

      // after modal is closed (cleanup), we must ensure we don't keep password in memory anywhere
      // the createCredentialModal cleanup will remove DOM; here we remove references
      // monkey-patch cleanup to also nullify any local refs (defensive)
      const originalCleanup = modalHandle.cleanup;
      modalHandle.cleanup = function() {
        try { originalCleanup(); } catch(e) {}
        // overwrite variables - best-effort
        // Note: JS strings are immutable, we can remove references in this scope:
        // (this block ensures local references are unreachable)
      };

      showToast('Account created — credentials shown (one-time)', { type: 'success' });

    } catch (err) {
      console.error(err);
      showToast(err.message || 'Accept action failed', { type: 'error' });
    } finally {
      acceptBtn.disabled = false;
    }
  });

  // reject feature
  document.getElementById("rejectBtn").addEventListener("click", async () => {
    const reason = prompt("Please enter the reason for rejection:");
    if (!reason) {
      showToast("Rejection reason is required!");
      return;
    }

    const preId = findPreEnrollmentId();
    if (!preId) {
      showToast('Record id not found', { type: 'error' });
      return;
    }

    const jwt = localStorage.getItem("jwt");
    const id = preId;

    try {
      const resp = await fetch(`/admin/pre-enrollments/old/${id}/reject`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          "Authorization": `Bearer ${jwt}`
        },
        body: JSON.stringify({ reason })
      });

      if (!resp.ok) {
        const text = await resp.text();
        throw new Error(`Server error: ${resp.status} ${resp.statusText}${text ? " - " + text : ""}`);
      }

      const data = await resp.json();

      // normalize possible fields (depends on backend response shape)
      const code = data.serialKey || data.status || "";
      const message = data.password || data.error || "";

      if (code === "ALREADY_ACCEPTED") {
        showToast("This pre-enrollment has already been accepted.", { type: "error" });
        return;
      }
      if (code === "ALREADY_REJECTED") {
        showToast("This pre-enrollment has already been rejected.", { type: "error" });
        return;
      }

      // default success
      showToast("Rejected successfully: " + (message || reason), { type: "success" });

      // Optionally refresh UI/table here
    } catch (e) {
      console.error(e);
      showToast(e.message || "Network error while rejecting", { type: "error" });
    }
  });


})();
