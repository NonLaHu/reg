const body = document.body;
const themeToggle = document.getElementById('theme-toggle');
const THEME_KEY = 'theme';

// apply stored or system theme
function loadTheme() {
    const stored = localStorage.getItem(THEME_KEY);
    const prefersDark = window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches;
    const theme = stored || (prefersDark ? 'dark' : 'light');
    body.classList.toggle('dark', theme === 'dark');
    themeToggle.textContent = theme === 'dark' ? '☀️' : '🌙';
    themeToggle.setAttribute('aria-pressed', theme === 'dark');
}
loadTheme();
themeToggle && themeToggle.addEventListener('click', () => {
    const isDark = body.classList.toggle('dark');
    localStorage.setItem(THEME_KEY, isDark ? 'dark' : 'light');
    themeToggle.textContent = isDark ? '☀️' : '🌙';
    themeToggle.setAttribute('aria-pressed', isDark);
});


// confirm modal
const confirmModal = document.getElementById('confirm');
const confirmText = document.getElementById('confirm-text');
const confirmYes = document.getElementById('confirm-yes');
const confirmNo = document.getElementById('confirm-no');

function confirmDialog(message) {
return new Promise((resolve) => {
    confirmText.textContent = message;
    confirmModal.setAttribute('aria-hidden', 'false');

    function cleanup() {
    confirmModal.setAttribute('aria-hidden', 'true');
    confirmYes.removeEventListener('click', onYes);
    confirmNo.removeEventListener('click', onNo);
    }
    function onYes() { cleanup(); resolve(true); }
    function onNo() { cleanup(); resolve(false); }
    confirmYes.addEventListener('click', onYes);
    confirmNo.addEventListener('click', onNo);
});
}



// Save notes
const saveNotesBtn = document.getElementById('save-notes');
if (saveNotesBtn) {
saveNotesBtn.addEventListener('click', async () => {
    const notes = document.getElementById('admin-notes').value;
    const pathMatch = window.location.pathname.match(/\/admin\/students\/(\d+)/);
    if (!pathMatch) return alert('Student id not found in URL.');
    const id = pathMatch[1];
    try {
    await postJson(`/api/admin/students/${id}/notes`, { notes });
    alert('Notes saved.');
    } catch (err) {
    console.error(err);
    alert('Save failed.');
    }
});
}

// keyboard shortcut D toggles theme
document.addEventListener('keydown', (e) => {
if (e.key.toLowerCase() === 'd' && !['INPUT','TEXTAREA','SELECT'].includes(document.activeElement.tagName)) {
    themeToggle.click();
}
});

async function viewEnrollmentDetailR(id) {
  try {
    const token = localStorage.getItem('jwt');

    const response = await fetch(`/admin/enrollments/${id}`, {
      method: "GET",
      headers: {
        "Authorization": `Bearer ${token}`, 
        "Accept": "text/html"            
      }
    });

    if (!response.ok) {
      throw new Error(`HTTP error! Status: ${response.status}`);
    }

    const html = await response.text();
    sessionStorage.setItem("pageContentEnrollmentAdmin", html);
    window.location.href = "render_preview_enrollment";

  } catch (err) {
    showNotification('Failed to fetch enrollment detail: ' + err.message, 'error');
  }
}

const birthEl = document.getElementById('birth-date');
const ageEl = document.getElementById('age');

if (birthEl && ageEl) {
    const birthStr = birthEl.textContent.trim();
    if (birthStr) {
        const birthDate = new Date(birthStr);
        const today = new Date();

        let age = today.getFullYear() - birthDate.getFullYear();
        const monthDiff = today.getMonth() - birthDate.getMonth();
        const dayDiff = today.getDate() - birthDate.getDate();
        if (monthDiff < 0 || (monthDiff === 0 && dayDiff < 0)) {
            age--; // birthday hasn't happened yet this year    
        }
        console.log(age)
        ageEl.textContent = age;
    }
}

function ensureNotificationContainer() {
  let el = document.getElementById('adminNotification');
  if (!el) {
    el = document.createElement('div');
    el.id = 'adminNotification';
    Object.assign(el.style, {
      position: 'fixed',
      right: '18px',
      top: '78px',
      zIndex: 9999,
      minWidth: '260px',
      maxWidth: '420px',
      fontFamily: 'Inter, system-ui, Arial',
    });
    document.body.appendChild(el);
  }
  return el;
}

function showNotification(message, type = 'info', ttl = 4500) {
  const container = ensureNotificationContainer();
  const node = document.createElement('div');

  node.className = `notif notif-${type}`;
  node.setAttribute('role', 'status');
  node.setAttribute('tabindex', '0');             // keyboard focusable
  node.setAttribute('title', 'Click or press Enter to dismiss');
  node.textContent = message;

  Object.assign(node.style, {
    marginBottom: '8px',
    padding: '12px 14px',
    borderRadius: '8px',
    color: '#fff',
    boxShadow: '0 6px 18px rgba(16,24,40,0.08)',
    fontSize: '13px',
    background: type === 'success' ? '#10b981' : type === 'error' ? '#ef4444' : '#2563eb',
    cursor: 'pointer',
    transition: 'opacity 200ms ease, transform 200ms ease',
    opacity: '1'
  });

  let removed = false;
  let timeoutId;

  function removeNotification() {
    if (removed) return;
    removed = true;
    // fade out + slight lift
    node.style.opacity = '0';
    node.style.transform = 'translateY(-6px)';
    clearTimeout(timeoutId);
    setTimeout(() => {
      if (node.parentElement) container.removeChild(node);
    }, 220);
  }

  // Close on click
  node.addEventListener('click', (e) => {
    e.stopPropagation();
    removeNotification();
  });

  // Append then schedule auto-remove
  container.appendChild(node);
  timeoutId = setTimeout(removeNotification, ttl);
  return node;
}

const passwordContainer = document.getElementById('password-container');
const passwordInput = document.getElementById('new-password');
const copyBtn = document.getElementById('copy-password');

async function handlePasswordReset() {
  const ok = await confirmDialog(
    'Reset the student password to a system-generated value? This will notify the student if email is configured.'
  );
  if (!ok) return;

  const idStr = document.getElementById("studentId").textContent; 
  const id = parseInt(idStr, 10);
  const token = localStorage.getItem('jwt');

  try {
    const response = await fetch(`/api/admin/students/student-reset-pass/${id}`, {
      method: 'POST',
      headers: {
        'Authorization': `Bearer ${token}`,
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      }
    });

    if (!response.ok) throw new Error(`HTTP ${response.status}`);
    const result = await response.json();

    // Show password in container
    const passwordMatch = result.message.match(/New password: (.+)$/);
    if (passwordMatch) {
      showPasswordModal(passwordMatch[1]);
    }else{
        console.log(result.message);
    }

  } catch (err) {
    console.error(err);
    alert('Reset failed: ' + (err?.message || 'server error'));
  }
}

// Show password modal
function showPasswordModal(password) {
  const overlay = document.getElementById('password-overlay');
  const input = document.getElementById('new-password');
  input.value = password;
  overlay.classList.remove('hidden');
}

// Close modal
document.getElementById('close-password').addEventListener('click', () => {
  document.getElementById('password-overlay').classList.add('hidden');
});

// Copy password
document.getElementById('copy-password').addEventListener('click', () => {
  const input = document.getElementById('new-password');
  input.select();
  document.execCommand('copy');
  alert('Password copied to clipboard!');
});

// Attach to button
document.getElementById('reset-password')?.addEventListener('click', handlePasswordReset);

async function viewEnrollmentODetail(id) {
  try {
    const token = localStorage.getItem("jwt");

    const response = await fetch(`/admin/pre-enrollments_old/${id}`, {
      method: "GET",
      headers: {
        "Authorization": `Bearer ${token}`, 
        "Accept": "text/html"            
      }
    });

    if (!response.ok) {
      throw new Error(`HTTP error! Status: ${response.status}`);
    }

    // Get HTML as string
    const html = await response.text();

    // Store HTML temporarily in sessionStorage
    sessionStorage.setItem("pageContentOldStudent", html);

    // Redirect to render page
    window.location.href = "renderOldStudent";

  } catch (err) {
    showNotification('Failed to fetch enrollment detail: ' + err.message, 'error');
  }
}