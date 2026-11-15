/**zUKkfc351j
 * public/js/admin.js
 *
 * Client-side admin UI logic.
 * - Configure endpoints and file names at the top.
 * - Fetch data from backend and populate cards & tables.
 * - Remove static table children and replace with server data.
 *
 * Notes:
 *  - Expects JWT stored at localStorage[TOKEN_KEY].
 *  - Expects endpoints to return JSON.
 *  - If backend returns error JSON like { status: "error", message: "..." },
 *    the library will surface that message in notifications.
 */

/* =========================
   Configuration
   ========================= */
const TOKEN_KEY = 'jwt';                      // where token is stored (localStorage)
const LOGO_FILE = '/public/asset/Logo_of_UTYCC.png';
const AVATAR_FILE = '/public/asset/placeholder.png';
const THEME_KEY = 'theme';   // localStorage key, values: 'light' or 'dark'
const DEFAULT_THEME = 'light';    // fallback theme

// API base and endpoints (update to match your backend)
const API_BASE = '/private/admin';
const ENDPOINTS = {
  DASHBOARD: `${API_BASE}/dashboard`,             // GET -> { usersCount, preEnrollments, filesCount, load }
  USERS: `${API_BASE}/recent_users`,              // GET -> [ {id, name, email, role, joined} ]
  ENROLLMENTS: `${API_BASE}/pre_enrollments`,     // GET -> [ {id, fullName, phone, viber, status, submitted} ]
  SECION_USERS: `${API_BASE}/section_users`,      // GET -> [ {id, name, email, role, joined} ]
  FILES: `${API_BASE}/files`,                     // GET -> [ {id, name, size, uploadedAt} ]
  PROFILE: `/admin/users/me`
};

/* =========================
   Small runtime helpers
   ========================= */

function getToken() {
  return localStorage.getItem(TOKEN_KEY);
}

function redirectToLogin() {
  window.location.href = '/login';
}

function redirectToNotAuthorized() {
  window.location.href = '/not-authorized';
}

/**
 * Generic fetch wrapper that:
 *  - attaches Authorization header
 *  - attempts to parse JSON responses
 *  - surfaces backend JSON error messages
 */
async function fetchJson(url, options = {}) {
  const token = getToken();
  if (!token) {
    redirectToLogin();
    throw new Error('Not authenticated');
  }

  const headers = Object.assign({}, options.headers || {}, {
    'Authorization': 'Bearer ' + token
  });

  const res = await fetch(url, Object.assign({}, options, { headers }));

  // handle auth errors centrally
  if (res.status === 401) {
    showNotification('Authentication required. Redirecting to login.', 'error');
    redirectToLogin();
    throw new Error('Unauthorized');
  }
  if (res.status === 403) {
    showNotification('You are not authorized to view this resource.', 'error');
    redirectToNotAuthorized();
    throw new Error('Forbidden');
  }

  // parse body as JSON when possible
  const text = await res.text();
  const isJson = text && (text.trim().startsWith('{') || text.trim().startsWith('['));
  const data = isJson ? JSON.parse(text) : null;

  if (!res.ok) {
    const msg = (data && (data.message || data.error || data.status)) || `${res.status} ${res.statusText}`;
    throw new Error(msg);
  }

  return data;
}

/* =========================
   UI utilities
   ========================= */

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

/**
 * showNotification(message, type)
 * type: 'info' | 'success' | 'error'
 */
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

/* =========================
   Render helpers
   ========================= */

function clearTableBody(tbody) {
  // remove all children
  while (tbody.firstChild) tbody.removeChild(tbody.firstChild);
}

function renderDashboardCards(stats = {}) {
  document.getElementById('statUsers').textContent = stats.usersCount ?? '—';
  document.getElementById('statEnrollments').textContent = stats.preEnrollments ?? '—';
  document.getElementById('statFiles').textContent = stats.filesCount ?? '—';
  document.getElementById('statEnrollmentsR').textContent = stats.enrollments ?? '—';
  document.getElementById('statLoad').textContent = stats.load ?? '—';
}

function renderUsersTable(users = []) {
  // find users tbody by aria-labelledby anchor
  const tbody = document.querySelector('section[aria-labelledby="usersHeading"] .table tbody');
  if (!tbody) return;
  clearTableBody(tbody);

  if (!users.length) {
    const tr = document.createElement('tr');
    tr.innerHTML = `<td colspan="6" style="color:var(--muted);padding:18px;text-align:center">No users found</td>`;
    tbody.appendChild(tr);
    return;
  }

  users.forEach((u, idx) => {
    const tr = document.createElement('tr');

    const joined = u.joined ? u.joined.split('T')[0] : (u.joinedDate || '-');
    const role = (u.role || 'USER').toUpperCase();

    // Determine action type
    let actionAttr = "view-user";

    tr.innerHTML = `
      <td>${idx + 1}</td>
      <td>${escapeHtml(u.name || u.fullName || '-')}</td>
      <td>${escapeHtml(u.email || '-')}</td>
      <td>${escapeHtml(role)}</td>
      <td>${escapeHtml(joined)}</td>
      <td class="actions">
        <button class="btn btn-sm" data-action="${actionAttr}" data-id="${u.id}">View</button>
      </td>
    `;

    tbody.appendChild(tr);
  });
}

function renderEnrollmentsTable(enrollments = []) {
  const tbody = document.querySelector('section[aria-labelledby="enrollmentsHeading"] .table tbody');
  if (!tbody) return;
  clearTableBody(tbody);

  if (!enrollments.length) {
    const tr = document.createElement('tr');
    tr.innerHTML = `<td colspan="7" style="color:var(--muted);padding:18px;text-align:center">No pre-enrollments</td>`;
    tbody.appendChild(tr);
    return;
  }

  enrollments.forEach(e => {
    const tr = document.createElement('tr');
    const statusClass = (e.status || '').toLowerCase();
    const submitted = e.submitted ? e.submitted.split('T')[0] : '-';
    tr.innerHTML = `
      <td>${escapeHtml(String(e.id))}</td>
      <td>${escapeHtml(e.fullNameMm || '-')}</td>
      <td>${escapeHtml(e.phone || e.phoneNumber || '-')}</td>
      <td>${escapeHtml(e.viber || e.viberPhoneNumber || '-')}</td>
      <td><span class="status-badge ${statusClass}">${escapeHtml(capitalize(e.status || 'unknown'))}</span></td>
      <td>${escapeHtml(e.createdAt)}</td>
      <td class="actions">
        <button class="btn btn-sm" data-action="view-enrollment" data-id="${e.id}">View</button>
      </td>
    `;
    tbody.appendChild(tr);
  });
}

function renderFilesTable(files = []) {
  // If you later add a files table, hook here.
  // Example placeholder: update #statFiles already used
}

/* =========================
   Utilities
   ========================= */

function escapeHtml(s) {
  if (s == null) return '';
  return String(s)
    .replace(/&/g, '&amp;')
    .replace(/</g, '&lt;')
    .replace(/>/g, '&gt;')
    .replace(/"/g, '&quot;')
    .replace(/'/g, '&#39;');
}

function capitalize(s) {
  if (!s) return s;
  return s.charAt(0).toUpperCase() + s.slice(1);
}

/* =========================
   Data loaders
   ========================= */

async function loadDashboard() {
  try {
    const data = await fetchJson(ENDPOINTS.DASHBOARD);
    renderDashboardCards({
      usersCount: data.usersCount ?? data.users ?? 0,
      preEnrollments: data.preEnrollments ?? data.enrollments ?? 0,
      filesCount: data.filesCount ?? data.files ?? 0,
      enrollments: data.enrollments ?? data.enrollmentR ?? 0,
      load: data.load ?? data.systemLoad ?? '—'
    });
  } catch (err) {
    showNotification('Failed to load dashboard: ' + err.message, 'error');
  }
}

async function loadUsers() {
  try {
    const users = await fetchJson(ENDPOINTS.USERS);
    renderUsersTable(users);
  } catch (err) {
    showNotification('Failed to load users: ' + err.message, 'error');
  }
}

async function loadEnrollments() {
  try {
    const enrollments = await fetchJson(ENDPOINTS.ENROLLMENTS);
    renderEnrollmentsTable(enrollments);
  } catch (err) {
    showNotification('Failed to load pre-enrollments: ' + err.message, 'error');
  }
}

async function loadFiles() {
  try {
    const files = await fetchJson(ENDPOINTS.FILES);
    renderFilesTable(files);
  } catch (err) {
    showNotification('Failed to load files: ' + err.message, 'error');
  }
}

/* =========================
   Actions (table buttons)
   ========================= */

async function handleAction(event) {
  const btn = event.target.closest('button[data-action]');
  if (!btn) return;

  const action = btn.getAttribute('data-action');
  const id = btn.getAttribute('data-id');

  switch (action) {
    case 'view-user':
      showNotification(`Open user detail: ${id}`, 'info', 2500);
      viewUserDetail(id);
      break;

    case 'view-enrollment':
      showNotification(`Open pre-enrollment detail: ${id}`, 'info', 2500);
      viewEnrollmentDetail(id);
      break;

    case 'view-enrollmentR':
      showNotification(`Open enrollment detail: ${id}`, 'info', 2500);
      viewEnrollmentDetailR(id);
      break;

    case 'view-enrollment-old':
      showNotification(`Open enrollment detail: ${id}`, 'info', 2500);
      viewEnrollmentODetail(id);
      break;

    case 'view-announcement':
      showNotification(`Open Announcement detail: ${id}`, 'info', 2500);
      viewStudentDetailA(id);
      break;

    case 'view-student':
      showNotification(`Open enrollment detail: ${id}`, 'info', 2500);
      viewStudentDetail(id);
      break;

    default:
      console.warn('Unhandled action', action, id);
  }
}


/* =========================
   Navigation & boot
   ========================= */

let currentSectionId = 'dashboard';

function setActiveNav(key) {
  document.querySelectorAll('.nav-item').forEach(li => li.classList.remove('active'));
  const el = document.querySelector(`.nav-item a[data-nav="${key}"]`);
  if (el && el.parentElement) el.parentElement.classList.add('active');
}

/* show/hide sections */
function showSection(sectionId) {
  if (currentSectionId === sectionId) return;

  document.querySelectorAll('.admin-section').forEach(sec => {
    sec.style.display = 'none';
  });

  const target = document.getElementById(sectionId);
  if (target) target.style.display = 'block';

  currentSectionId = sectionId;
  setActiveNav(sectionId);
}

async function loadSection(key) {
  showSection(key);

  switch (key) {
    case 'dashboard':
      await loadDashboard();
      await loadEnrollments();
      await loadUsers();
      break;
    case 'users':
      await loadSectionUsers();
      break;
    case 'pre-enrollments':
      await loadSectionEnrollments();
      break;
    case 'enrollments':
      await loadSectionEnrollmentsR();
      break;
    case 'pre-enrollmentsOld':
      await loadSectionEnrollmentsO();
      break;
    case 'students':
      await loadStudentsSection();
      break;
    case 'annoucements':
      await loadStudentsSectionA();
      break;
    case 'settings':
      break;
    default:
      await loadDashboard();
      await loadEnrollments();
      await loadUsers();
  }

}


function attachEventListeners() {
  // Sidebar navigation
  document.querySelectorAll('.nav-item a[data-nav]').forEach(a => {
    a.addEventListener('click', e => {
      e.preventDefault();
      const key = a.getAttribute('data-nav');
      loadSection(key);

      // close mobile menu if open
      if (window.matchMedia('(max-width:800px)').matches && document.body.classList.contains('mobile-menu-open')) {
        closeMobileMenu();
      }
    });
  });

  // Table action delegation
  document.body.addEventListener('click', handleAction);

  const btnCreate = Array.from(document.getElementsByClassName('btnCreate'));
  btnCreate.forEach(btn => {
    btn.addEventListener('click', () => {
      window.location.assign("/user_create");
    });
  });



  // Optionally wire mobile menu
  // wireMobileMenuButton();
}


/* =========================
   Theme functions
   ========================= */

function applyTheme(theme) {
  if (theme === 'dark') {
    document.body.classList.add('theme-dark');
    document.getElementById('btnToggleTheme')?.setAttribute('aria-pressed', 'true');
  } else {
    document.body.classList.remove('theme-dark');
    document.getElementById('btnToggleTheme')?.setAttribute('aria-pressed', 'false');
  }
}

function getSavedTheme() {
  try {
    return localStorage.getItem(THEME_KEY) || DEFAULT_THEME;
  } catch (e) {
    return DEFAULT_THEME;
  }
}

function saveTheme(theme) {
  try {
    localStorage.setItem(THEME_KEY, theme);
  } catch (e) {
    // ignore storage write errors
  }
}

function toggleTheme() {
  const current = document.body.classList.contains('theme-dark') ? 'dark' : 'light';
  const next = current === 'dark' ? 'light' : 'dark';
  applyTheme(next);
  saveTheme(next);
  // showNotification(`Switched to ${next} theme`, 'success', 1500);
}

/* wire toggle button click (call this during init) */
function wireThemeToggleButton() {
  const btn = document.getElementById('btnToggleTheme');
  if (!btn) return;
  btn.addEventListener('click', (e) => {
    e.preventDefault();
    toggleTheme();
  });
}

/* =========================
   Mobile sidebar menu behavior
   ========================= */

const MOBILE_MENU_BTN_ID = 'btnMobileMenu';
const MOBILE_MENU_BODY_CLASS = 'mobile-menu-open';
const MOBILE_BACKDROP_CLASS = 'mobile-menu-backdrop';

function createBackdrop() {
  let bd = document.querySelector('.' + MOBILE_BACKDROP_CLASS);
  if (bd) return bd;
  bd = document.createElement('div');
  bd.className = MOBILE_BACKDROP_CLASS;
  bd.setAttribute('aria-hidden', 'true');
  document.body.appendChild(bd);
  return bd;
}

function openMobileMenu() {
  document.body.classList.add(MOBILE_MENU_BODY_CLASS);
  // lock body scroll
  document.documentElement.style.overflow = 'hidden';
  document.body.style.overflow = 'hidden';
  // ensure backdrop exists
  const bd = createBackdrop();
  // attach click to backdrop to close
  bd.addEventListener('click', closeMobileMenu, { once: false });
  // set aria
  const btn = document.getElementById(MOBILE_MENU_BTN_ID);
  if (btn) btn.setAttribute('aria-expanded', 'true');
}

function closeMobileMenu() {
  document.body.classList.remove(MOBILE_MENU_BODY_CLASS);
  // restore scrolling
  document.documentElement.style.overflow = '';
  document.body.style.overflow = '';
  // remove backdrop (fade handled via CSS opacity)
  const bd = document.querySelector('.' + MOBILE_BACKDROP_CLASS);
  if (bd) {
    bd.removeEventListener('click', closeMobileMenu);
    // remove with small timeout to allow CSS opacity transition
    bd.parentElement && bd.parentElement.removeChild(bd);
  }
  // set aria
  const btn = document.getElementById(MOBILE_MENU_BTN_ID);
  if (btn) btn.setAttribute('aria-expanded', 'false');
}

/* toggle the mobile menu */
function toggleMobileMenu(e) {
  const isOpen = document.body.classList.contains(MOBILE_MENU_BODY_CLASS);
  if (isOpen) closeMobileMenu();
  else openMobileMenu();
}

/* close menu when a nav link is clicked (mobile) */
function closeMenuOnNavClick() {
  document.querySelectorAll('.nav-item a[data-nav]').forEach(a => {
    a.addEventListener('click', (ev) => {
      // only close on mobile
      if (window.matchMedia('(max-width:800px)').matches) {
        // allow other click handlers (like loading a section) to run
        setTimeout(() => closeMobileMenu(), 150);
      }
    });
  });
}

/* wire mobile menu button at init */
function wireMobileMenuButton() {
  const btn = document.getElementById(MOBILE_MENU_BTN_ID);
  if (!btn) return;
  btn.addEventListener('click', (e) => {
    e.preventDefault();
    toggleMobileMenu();
  });

  // close on Esc
  document.addEventListener('keydown', (e) => {
    if (e.key === 'Escape' && document.body.classList.contains(MOBILE_MENU_BODY_CLASS)) {
      closeMobileMenu();
    }
  });

  // ensure nav links close the menu on mobile
  closeMenuOnNavClick();
}

/* =========================
   Users Section
   ========================= */

const userFilter = document.getElementById('userSearch');

let usersPage = 1;
let usersPageSize = 10;
let usersTotalPages = 1;
let usersSortKey = 'id';
let usersSortAsc = true;
let usersFilter = '';

async function loadSectionUsers() {
  const params = new URLSearchParams({
    page: usersPage - 1,
    size: usersPageSize,
    sort: usersSortKey + ',' + (usersSortAsc ? 'asc' : 'desc'),
    filter: usersFilter
  });

  try {
    const res = await fetch(`${ENDPOINTS.SECION_USERS}?${params.toString()}`, {
      headers: {
        'Authorization': 'Bearer ' + getToken() // if JWT required
      }
    });
    if (!res.ok) throw new Error(`Failed to fetch users: ${res.status}`);
    const data = await res.json(); // expect { content: [...], totalPages, totalElements }

    usersTotalPages = data.totalPages || 1;
    updateUsersTable(data.content || []);
    updatePageInfo();
  } catch (err) {
    showNotification(err.message, 'error');
    updateUsersTable([]);
  }
}

function updateUsersTable(users) {
  const tbody = document.getElementById('usersTableBody');
  tbody.innerHTML = '';

  if (!users.length) {
    const tr = document.createElement('tr');
    const td = document.createElement('td');
    td.colSpan = 6;
    td.textContent = 'No users found';
    td.style.textAlign = 'center';
    tr.appendChild(td);
    tbody.appendChild(tr);
    return;
  }

  users.forEach((u, idx) => {
    const tr = document.createElement('tr');

    const joined = u.joined ? u.joined.split('T')[0] : (u.joinedDate || '-');
    const role = (u.role || 'USER').toUpperCase();

    // Determine action type
    let actionAttr = "view-user";

    tr.innerHTML = `
      <td>${u.id}</td>
      <td>${escapeHtml(u.name || u.fullName || '-')}</td>
      <td>${escapeHtml(u.email || '-')}</td>
      <td>${escapeHtml(role)}</td>
      <td>${escapeHtml(joined)}</td>
      <td class="actions">
        <button class="btn btn-sm" data-action="${actionAttr}" data-id="${u.id}">View</button>
      </td>
    `;

    tbody.appendChild(tr);
  });

}

function updatePageInfo() {
  const pageInfo = document.getElementById('pageInfo');
  pageInfo.textContent = `Page ${usersPage} of ${usersTotalPages}`;
}


/* Actions: view / approve / reject */
async function viewUserDetail(id) {
  try {
    const token = localStorage.getItem(TOKEN_KEY);

    const response = await fetch(`/admin/admin-users/${id}`, {
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
    sessionStorage.setItem("pageContentAdminUser", html);
    window.location.href = "renderAdminUser";

  } catch (err) {
    showNotification('Failed to fetch enrollment detail: ' + err.message, 'error');
  }
}


// Pagination buttons
document.getElementById('prevPage').addEventListener('click', () => {
  if (usersPage > 1) {
    usersPage--;
    loadSectionUsers();
  }
});

document.getElementById('nextPage').addEventListener('click', () => {
  if (usersPage < usersTotalPages) {
    usersPage++;
    loadSectionUsers();
  }
});

// Search/filter input
document.getElementById('btnQueryUserSearch').addEventListener('click', (e) => {
  usersFilter = userFilter.value.trim();
  usersPage = 1;
  loadSectionUsers();
});

// Sortable headers
document.querySelectorAll('#users thead th[data-sort]').forEach(th => {
  th.style.cursor = 'pointer';
  th.addEventListener('click', () => {
    const key = th.getAttribute('data-sort');
    if (usersSortKey === key) usersSortAsc = !usersSortAsc;
    else {
      usersSortKey = key;
      usersSortAsc = true;
    }
    loadSectionUsers();
  });
});

// Table actions (edit/delete)
document.getElementById('usersTableBody').addEventListener('click', e => {
  const btn = e.target.closest('button[data-action]');
  if (!btn) return;

  const id = btn.getAttribute('data-id');
  const action = btn.getAttribute('data-action');

  if (action === 'edit') {
    showNotification(`Edit user ${id} — implement modal`, 'info');
  }
});

// async function deleteUser(id) {
//   try {
//     const res = await fetch(`${ENDPOINTS.SECION_USERS}/${id}`, {
//       method: 'DELETE',
//       headers: { 'Authorization': 'Bearer ' + getToken() }
//     });
//     if (!res.ok) throw new Error(`Failed to delete user ${id}`);
//     showNotification(`User ${id} deleted`, 'success');
//     loadSectionUsers();
//   } catch (err) {
//     showNotification(err.message, 'error');
//   }
// }
const dashboardUserFilterBtn = document.getElementById('dashboardViewAll');
const dashboardViewAllValue = document.getElementById('dashboardViewAllValue');

dashboardUserFilterBtn.addEventListener('click', () => {
  usersFilter = dashboardViewAllValue.value.trim();
  dashboardViewAllValue.value = '';
  userFilter.value = usersFilter;
  showSection('users');
  loadSectionUsers();
})



// Helper to clear any table body
function clearTableBody(tbody) {
  tbody.innerHTML = '';
}

/* =========================
   Pre-enrollments Section
   ========================= */

let enrollPage = 1;
let enrollPageSize = 10;
let enrollTotalPages = 1;
let enrollSortKey = 'createdAt';
let enrollSortAsc = false;
let enrollStatusFilter = '';

// Format date helper
function formatDate(dateStr) {
  if (!dateStr) return '-';
  try {
    return new Date(dateStr).toLocaleDateString();
  } catch (e) {
    return dateStr;
  }
}

function updateEnrollPageInfo() {
  const el = document.getElementById('enrollPageInfo');
  if (el) el.textContent = `Page ${enrollPage} of ${enrollTotalPages}`;
}

function clearEnrollTable() {
  const tbody = document.getElementById('enrollmentsTableBody');
  if (tbody) tbody.innerHTML = '';
}

function renderSectionEnrollmentsTable(items = []) {
  const tbody = document.getElementById('enrollmentsTableBody');
  if (!tbody) return;
  clearEnrollTable();

  if (!items.length) {
    const tr = document.createElement('tr');
    tr.innerHTML = `<td colspan="7" style="text-align:center;color:var(--muted);padding:18px">No pre-enrollments found</td>`;
    tbody.appendChild(tr);
    return;
  }

  items.forEach((e, idx) => {
    const submitted = formatDate(e.submitted);
    const statusClass = (e.status || '').toLowerCase();

    if (e.id == 0) {
      return;
    }
    const tr = document.createElement('tr');
    tr.innerHTML = `
      <td>${escapeHtml(String(e.id || ''))}</td>
      <td>${escapeHtml(e.fullNameMm || e.name || '-')}</td>
      <td>${escapeHtml(e.phoneNumber || e.phone || '-')}</td>
      <td>${escapeHtml(e.viberPhoneNumber || e.viber || '-')}</td>
      <td><span class="status-badge ${statusClass}">${escapeHtml((e.status || 'unknown').toUpperCase())}</span></td>
      <td>${escapeHtml(e.createdAt)}</td>
      <td class="actions">
        <button class="btn btn-sm" data-action="view-enrollment" data-id="${e.id}">View</button>
      </td>
    `;
    tbody.appendChild(tr);
  });
}

/**
 * loadEnrollments() - fetches list using server-side pagination, status filter, and sort.
 * Expects backend to accept: page, size, sort (like 'submitted,desc'), status
 * backend response format expected: { content: [...], totalPages, totalElements }
 */
async function loadSectionEnrollments() {
  const params = new URLSearchParams({
    page: enrollPage - 1,
    size: enrollPageSize,
    sort: enrollSortKey + ',' + (enrollSortAsc ? 'asc' : 'desc'),
  });
  if (enrollStatusFilter) params.set('status', enrollStatusFilter);

  try {
    // use shared fetchJson wrapper so Authorization header and error handling are consistent
    const url = `/private/admin/section_pre_enrollments?${params.toString()}`;
    const data = await fetchJson(url);
    // data expected: { content: [...], totalPages, totalElements }
    const content = data.content || data.items || data || [];
    enrollTotalPages = data.totalPages || 1;
    renderSectionEnrollmentsTable(content);
    updateEnrollPageInfo();
  } catch (err) {
    showNotification('Failed to load pre-enrollments: ' + err.message, 'error');
    renderSectionEnrollmentsTable([]);
    enrollTotalPages = 1;
    updateEnrollPageInfo();
  }
}

/* Actions: view / approve / reject */
async function viewEnrollmentDetail(id) {
  try {
    const token = localStorage.getItem(TOKEN_KEY);

    const response = await fetch(`/admin/pre-enrollments/${id}`, {
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
    sessionStorage.setItem("pageContent", html);

    // Redirect to render page
    window.location.href = "render";

  } catch (err) {
    showNotification('Failed to fetch enrollment detail: ' + err.message, 'error');
  }
}


/* Controls & wiring */
function wireEnrollmentsControls() {
  const prev = document.getElementById('enrollPrev');
  const next = document.getElementById('enrollNext');
  const pageSizeSel = document.getElementById('enrollPageSize');
  const filterSel = document.getElementById('enrollmentsFilter');
  const refreshBtn = document.getElementById('btnRefreshEnrollments');

  if (prev) prev.addEventListener('click', () => {
    if (enrollPage > 1) { enrollPage--; loadSectionEnrollments(); }
  });
  if (next) next.addEventListener('click', () => {
    if (enrollPage < enrollTotalPages) { enrollPage++; loadSectionEnrollments(); }
  });

  if (pageSizeSel) {
    pageSizeSel.value = String(enrollPageSize);
    pageSizeSel.addEventListener('change', (ev) => {
      enrollPageSize = Number(ev.target.value) || 10;
      enrollPage = 1;
      loadSectionEnrollments();
    });
  }

  if (filterSel) {
    filterSel.addEventListener('change', () => {
      enrollStatusFilter = filterSel.value;
      enrollPage = 1;
      loadSectionEnrollments();
    });
  }

  if (refreshBtn) refreshBtn.addEventListener('click', () => {
    loadSectionEnrollments();
  });

  // sortable headers support
  document.querySelectorAll('#pre-enrollments thead th[data-sort]').forEach(th => {
    th.style.cursor = 'pointer';
    th.addEventListener('click', () => {
      const key = th.getAttribute('data-sort');
      if (enrollSortKey === key) enrollSortAsc = !enrollSortAsc;
      else {
        enrollSortKey = key;
        enrollSortAsc = true;
      }
      loadSectionEnrollments();
    });
  });

  // delegate action buttons
}

wireEnrollmentsControls();
/* =========================
   Pre-enrollments (old) Section
   ========================= */

let enrollOPage = 1;
let enrollOPageSize = 10;
let enrollOTotalPages = 1;
let enrollOSortKey = 'createdAt';
let enrollOSortAsc = false;
let enrollOStatusFilter = '';

function updateEnrollOPageInfo() {
  const el = document.getElementById('enrollOPageInfo');
  if (el) el.textContent = `Page ${enrollOPage} of ${enrollOTotalPages}`;
}

function clearEnrollOTable() {
  const tbody = document.getElementById('enrollmentsOTableBody');
  if (tbody) tbody.innerHTML = '';
}

function renderSectionEnrollmentsOTable(items = []) {
  const tbody = document.getElementById('enrollmentsOTableBody');
  if (!tbody) return;
  clearEnrollOTable();

  if (!items.length) {
    const tr = document.createElement('tr');
    tr.innerHTML = `<td colspan="7" style="text-align:center;color:var(--muted);padding:18px">No pre-enrollments found</td>`;
    tbody.appendChild(tr);
    return;
  }

  items.forEach((e, idx) => {
    const submitted = formatDate(e.submitted);
    const statusClass = (e.status || '').toLowerCase();

    const tr = document.createElement('tr');
    tr.innerHTML = `
      <td>${escapeHtml(String(e.id || ''))}</td>
      <td>${escapeHtml(e.fullNameMm || e.name || '-')}</td>
      <td>${escapeHtml(e.phoneNumber || e.phone || '-')}</td>
      <td>${escapeHtml(e.viberPhoneNumber || e.viber || '-')}</td>
      <td><span class="status-badge ${statusClass}">${escapeHtml((e.status || 'unknown').toUpperCase())}</span></td>
      <td>${escapeHtml(e.createdAt)}</td>
      <td class="actions">
        <button class="btn btn-sm" data-action="view-enrollment-old" data-id="${e.id}">View</button>
      </td>
    `;
    tbody.appendChild(tr);
  });
}

async function loadSectionEnrollmentsO() {
  const params = new URLSearchParams({
    page: enrollOPage - 1,
    size: enrollOPageSize,
    sort: enrollOSortKey + ',' + (enrollOSortAsc ? 'asc' : 'desc'),
  });
  if (enrollOStatusFilter) params.set('status', enrollOStatusFilter);

  try {
    // use shared fetchJson wrapper so Authorization header and error handling are consistent
    const url = `/private/admin/section_pre_enrollments_old?${params.toString()}`;
    const data = await fetchJson(url);
    // data expected: { content: [...], totalPages, totalElements }
    const content = data.content || data.items || data || [];
    enrollOTotalPages = data.totalPages || 1;
    renderSectionEnrollmentsOTable(content);
    updateEnrollOPageInfo();
  } catch (err) {
    showNotification('Failed to load pre-enrollments: ' + err.message, 'error');
    renderSectionEnrollmentsOTable([]);
    enrollOTotalPages = 1;
    updateEnrollOPageInfo();
  }
}

/* Actions: view / approve / reject */
async function viewEnrollmentODetail(id) {
  try {
    const token = localStorage.getItem(TOKEN_KEY);

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


/* Controls & wiring */
function wireEnrollmentsOControls() {
  const prev = document.getElementById('enrollOPrev');
  const next = document.getElementById('enrollONext');
  const pageSizeSel = document.getElementById('enrollOPageSize');
  const filterSel = document.getElementById('enrollmentsOFilter');
  const refreshBtn = document.getElementById('btnRefreshEnrollmentsO');

  if (prev) prev.addEventListener('click', () => {
    if (enrollOPage > 1) { enrollOPage--; loadSectionEnrollmentsO(); }
  });
  if (next) next.addEventListener('click', () => {
    if (enrollOPage < enrollTotalPages) { enrollOPage++; loadSectionEnrollmentsO(); }
  });

  if (pageSizeSel) {
    pageSizeSel.value = String(enrollOPageSize);
    pageSizeSel.addEventListener('change', (ev) => {
      enrollOPageSize = Number(ev.target.value) || 10;
      enrollOPage = 1;
      loadSectionEnrollmentsO();
    });
  }

  if (filterSel) {
    filterSel.value = enrollOStatusFilter || '';
    filterSel.addEventListener('change', (ev) => {
      enrollOStatusFilter = filterSel.value;
      enrollPage = 1;
      loadSectionEnrollmentsO();
    });
  }

  if (refreshBtn) refreshBtn.addEventListener('click', () => {
    loadSectionEnrollmentsO();
  });

  // sortable headers support
  document.querySelectorAll('#pre-enrollments thead th[data-sort]').forEach(th => {
    th.style.cursor = 'pointer';
    th.addEventListener('click', () => {
      const key = th.getAttribute('data-sort');
      if (enrollOSortKey === key) enrollOSortAsc = !enrollOSortAsc;
      else {
        enrollOSortKey = key;
        enrollOSortAsc = true;
      }
      loadSectionEnrollmentsO();
    });
  });
}

wireEnrollmentsOControls();

/* =========================
      Enrollments Section
   ========================= */
let enrollPageR = 1;
let enrollPageSizeR = 10;
let enrollTotalPagesR = 1;
let enrollSortKeyR = 'createdAt';
let enrollSortAscR = false;
let enrollStatusFilterR = '';

function updateEnrollPageInfoR() {
  const el = document.getElementById('enrollPageInfoR');
  if (el) el.textContent = `Page ${enrollPageR} of ${enrollTotalPagesR}`;
}

function clearEnrollTableR() {
  const tbody = document.getElementById('enrollmentsTableBodyR');
  if (tbody) tbody.innerHTML = '';
}

function renderSectionEnrollmentsTableR(items = []) {
  const tbody = document.getElementById('enrollmentsTableBodyR');
  if (!tbody) return;
  clearEnrollTableR();

  if (!items.length) {
    const tr = document.createElement('tr');
    tr.innerHTML = `<td colspan="7" style="text-align:center;color:var(--muted);padding:18px">No enrollments found</td>`;
    tbody.appendChild(tr);
    return;
  }

  items.forEach((e, idx) => {
    const submitted = formatDate(e.submitted);
    const statusClass = (e.enrollmentStatus || '').toLowerCase();

    const tr = document.createElement('tr');
    tr.innerHTML = `
      <td>${escapeHtml(String(e.id || ''))}</td>
      <td>${escapeHtml(e.name || '-')}</td>
      <td>${escapeHtml(e.rollNo || '-')}</td>
      <td>${escapeHtml(e.major || '-')}</td>
      <td><span class="status-badge ${statusClass}">${escapeHtml((e.enrollmentStatus || 'unknown').toUpperCase())}</span></td>
      <td>${escapeHtml(e.birthDate || 'None')}</td>
      <td class="actions">
        <button class="btn btn-sm" data-action="view-enrollmentR" data-id="${e.enrollmentId}">View</button>
      </td>
    `;
    tbody.appendChild(tr);
  });
}

async function loadSectionEnrollmentsR() {
  const params = new URLSearchParams({
    page: enrollPageR - 1,
    size: enrollPageSizeR,
    sort: enrollSortKeyR + ',' + (enrollSortAscR ? 'asc' : 'desc'),
  });
  if (enrollStatusFilterR) params.set('status', enrollStatusFilterR);

  try {
    // use shared fetchJson wrapper so Authorization header and error handling are consistent
    const url = `/private/admin/section_enrollments?${params.toString()}`;
    const data = await fetchJson(url);
    // data expected: { content: [...], totalPages, totalElements }
    const content = data.content || data.items || data || [];
    enrollTotalPagesR = data.totalPages || 1;
    renderSectionEnrollmentsTableR(content);
    updateEnrollPageInfoR();
  } catch (err) {
    showNotification('Failed to load pre-enrollments: ' + err.message, 'error');
    renderSectionEnrollmentsTableR([]);
    enrollTotalPagesR = 1;
    updateEnrollPageInfoR();
  }
}

/* Actions: view / approve / reject */
async function viewEnrollmentDetailR(id) {
  try {
    const token = localStorage.getItem(TOKEN_KEY);

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

    // Get HTML as string
    const html = await response.text();

    // Store HTML temporarily in sessionStorage
    sessionStorage.setItem("pageContentEnrollmentAdmin", html);

    // Redirect to render page
    window.location.href = "render_preview_enrollment";

  } catch (err) {
    showNotification('Failed to fetch enrollment detail: ' + err.message, 'error');
  }
}

/* Controls & wiring */
function wireEnrollmentsControlsR() {
  const prev = document.getElementById('enrollPrevR');
  const next = document.getElementById('enrollNextR');
  const pageSizeSel = document.getElementById('enrollPageSizeR');
  const filterSel = document.getElementById('enrollmentsFilterR');
  const refreshBtn = document.getElementById('btnRefreshEnrollmentsR');

  if (prev) prev.addEventListener('click', () => {
    if (enrollPageR > 1) { enrollPageR--; loadSectionEnrollmentsR(); }
  });
  if (next) next.addEventListener('click', () => {
    if (enrollPageR < enrollTotalPagesR) { enrollPageR++; loadSectionEnrollmentsR(); }
  });

  if (pageSizeSel) {
    pageSizeSel.value = String(enrollPageSizeR);
    pageSizeSel.addEventListener('change', (ev) => {
      enrollPageSizeR = Number(ev.target.value) || 10;
      enrollPageR = 1;
      loadSectionEnrollmentsR();
    });
  }

  if (filterSel) {
    filterSel.value = enrollStatusFilterR || '';
    filterSel.addEventListener('change', (ev) => {
      enrollStatusFilterR = ev.target.value;
      enrollPage = 1;
      loadSectionEnrollmentsR();
    });
  }

  if (refreshBtn) refreshBtn.addEventListener('click', () => {
    loadSectionEnrollmentsR();
  });

  // sortable headers support
  document.querySelectorAll('#enrollments thead th[data-sort]').forEach(th => {
    th.style.cursor = 'pointer';
    th.addEventListener('click', () => {
      const key = th.getAttribute('data-sort');
      if (enrollSortKeyR === key) enrollSortAscR = !enrollSortAscR;
      else {
        enrollSortKeyR = key;
        enrollSortAscR = true;
      }
      loadSectionEnrollmentsR();
    });
  });
}

wireEnrollmentsControlsR();

/* =========================
      Students Section
   ========================= */

let studentPage = 1;
let studentPageSize = 10;
let studentTotalPages = 1;
let studentSortKey = 'createdAt';
let studentSortAsc = false;
let studentStatusFilter = '';
let studentFilter = '';

function updateStudentPageInfo() {
  const el = document.getElementById('studentPageInfo');
  if (el) el.textContent = `Page ${studentPage} of ${studentTotalPages}`;
}

function clearStudentTable() {
  const tbody = document.getElementById('studentsTableBody');
  if (tbody) tbody.innerHTML = '';
}

function renderStudentsTable(items = []) {
  const tbody = document.getElementById('studentsTableBody');
  if (!tbody) return;
  clearStudentTable();

  if (!items.length) {
    const tr = document.createElement('tr');
    tr.innerHTML = `<td colspan="7" style="text-align:center;color:var(--muted);padding:18px">No students found</td>`;
    tbody.appendChild(tr);
    return;
  }

  items.forEach((s) => {
    const birth = s.birthDate ? formatDate(s.birthDate) : 'None';
    const statusClass = (s.status || '').toLowerCase();

    const tr = document.createElement('tr');
    tr.innerHTML = `
      <td>${escapeHtml(String(s.id || ''))}</td>
      <td>${escapeHtml(s.fullName || s.name || '-')}</td>
      <td>${escapeHtml(s.rollNo || '-')}</td>
      <td>${escapeHtml(s.major || '-')}</td>
      <td>${escapeHtml(s.semester || '-')}</td>
      <td>${escapeHtml(s.birthDate || 'None')}</td>
      <td class="actions">
        <button class="btn btn-sm" data-action="view-student" data-id="${s.id || ''}">View</button>
      </td>
    `;
    tbody.appendChild(tr);
  });
}

async function loadStudentsSection() {
  const params = new URLSearchParams({
    page: studentPage - 1,
    size: studentPageSize,
    sort: studentSortKey + ',' + (studentSortAsc ? 'asc' : 'desc'),
    filter: studentFilter
  });
  if (studentStatusFilter) params.set('status', studentStatusFilter);

  try {
    const url = `/private/admin/section_students?${params.toString()}`;
    const data = await fetchJson(url);
    const content = data.content || data.items || data || [];
    studentTotalPages = data.totalPages || 1;
    renderStudentsTable(content);
    updateStudentPageInfo();
  } catch (err) {
    showNotification('Failed to load students: ' + err.message, 'error');
    renderStudentsTable([]);
    studentTotalPages = 1;
    updateStudentPageInfo();
  }
}

async function viewStudentDetail(id) {
  try {
    const token = localStorage.getItem(TOKEN_KEY);

    const response = await fetch(`/admin/students/${id}`, {
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
    sessionStorage.setItem("pageContentDetailStudent", html);
    window.location.href = "render_student_detail";
  } catch (err) {
    showNotification('Failed to fetch student detail: ' + err.message, 'error');
  }
}


function wireStudentsControls() {
  const prev = document.getElementById('studentPrev');
  const next = document.getElementById('studentNext');
  const pageSizeSel = document.getElementById('studentPageSize');
  // const filterSel = document.getElementById('studentsFilter');
  const studentSel = document.getElementById('studentSearch');

  if (prev) prev.addEventListener('click', () => {
    if (studentPage > 1) { studentPage--; loadStudentsSection(); }
  });
  if (next) next.addEventListener('click', () => {
    if (studentPage < studentTotalPages) { studentPage++; loadStudentsSection(); }
  });

  if (pageSizeSel) {
    pageSizeSel.value = String(studentPageSize);
    pageSizeSel.addEventListener('change', (ev) => {
      studentPageSize = Number(ev.target.value) || 10;
      studentPage = 1;
      loadStudentsSection();
    });
  }

  // if (filterSel) {
  //   filterSel.value = studentStatusFilter || '';
  //   filterSel.addEventListener('change', (ev) => {
  //     studentStatusFilter = ev.target.value;
  //     studentPage = 1;
  //     loadStudentsSection();
  //   });
  // }

  // Search/filter input
  document.getElementById('btnQuerystudentSearch').addEventListener('click', () => {
    console.log(studentSel.value);
    studentFilter = studentSel.value.trim();
    studentPage = 1;
    loadStudentsSection();
  });


  document.querySelectorAll('#students thead th[data-sort]').forEach(th => {
    th.style.cursor = 'pointer';
    th.addEventListener('click', () => {
      const key = th.getAttribute('data-sort');
      if (studentSortKey === key) studentSortAsc = !studentSortAsc;
      else {
        studentSortKey = key;
        studentSortAsc = true;
      }
      loadStudentsSection();
    });
  });
}

wireStudentsControls();


/* =========================
      Annoucements Section
   ========================= */

let studentPageA = 1;
let studentPageSizeA = 10;
let studentTotalPagesA = 1;
let studentSortKeyA = 'publishDate';
let studentSortAscA = false;
let studentStatusFilterA = '';
let studentFilterA = '';

function updateStudentPageInfoA() {
  const el = document.getElementById('studentPageInfoA');
  if (el) el.textContent = `Page ${studentPageA} of ${studentTotalPagesA}`;
}

function clearStudentTableA() {
  const tbody = document.getElementById('studentsTableBodyA');
  if (tbody) tbody.innerHTML = '';
}

function renderStudentsTableA(items = []) {
  const tbody = document.getElementById('studentsTableBodyA');
  if (!tbody) return;
  clearStudentTableA();

  if (!items.length) {
    const tr = document.createElement('tr');
    tr.innerHTML = `<td colspan="7" style="text-align:center;color:var(--muted);padding:18px">No students found</td>`;
    tbody.appendChild(tr);
    return;
  }

  items.forEach((s) => {
    const publicDate = s.publishDate ? formatDate(s.publishDate) : 'None';
    const expiryDate = s.expiryDate ? formatDate(s.expiryDate) : 'None';

    const tr = document.createElement('tr');
    tr.innerHTML = `
      <td>${escapeHtml(String(s.id || ''))}</td>
      <td>${escapeHtml(s.title || s.name || '-')}</td>
      <td>${escapeHtml(s.pdfFileName || '-')}</td>
      <td>${publicDate}</td>
      <td>${expiryDate}</td>
      <td class="actions">
        <button class="btn btn-sm" data-action="view-announcement" data-id="${s.id || ''}">View</button>
      </td>
    `;
    tbody.appendChild(tr);
  });
}

async function loadStudentsSectionA() {
  const params = new URLSearchParams({
    page: studentPageA - 1,
    size: studentPageSizeA,
    sort: studentSortKeyA + ',' + (studentSortAscA ? 'asc' : 'desc'),
    filter: studentFilterA
  });
  if (studentStatusFilterA) params.set('status', studentStatusFilterA);

  try {
    const url = `/private/admin/section_annoucements?${params.toString()}`;
    const data = await fetchJson(url);
    const content = data.content || data.items || data || [];
    studentTotalPagesA = data.totalPagesA || 1;
    renderStudentsTableA(content);
    updateStudentPageInfoA();
  } catch (err) {
    showNotification('Failed to load students: ' + err.message, 'error');
    renderStudentsTableA([]);
    studentTotalPagesA = 1;
    updateStudentPageInfoA();
  }
}

async function viewStudentDetailA(id) {
  try {
    const token = localStorage.getItem(TOKEN_KEY);

    const response = await fetch(`/admin/announcements/${id}`, {
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
    sessionStorage.setItem("pageContentAnnouncement", html);
    window.location.href = "render_announcement_detail";
  } catch (err) {
    showNotification('Failed to fetch student detail: ' + err.message, 'error');
  }
}


function wireStudentsControlsA() {
  const prev = document.getElementById('studentPrevA');
  const next = document.getElementById('studentNextA');
  const pageSizeSel = document.getElementById('studentPageSizeA');
  const filterSel = document.getElementById('studentsFilterA');
  const filterAnnouncement = document.getElementById('announcementSearch');

  if (prev) prev.addEventListener('click', () => {
    if (studentPageA > 1) { studentPageA--; loadStudentsSectionA(); }
  });
  if (next) next.addEventListener('click', () => {
    if (studentPageA < studentTotalPagesA) { studentPageA++; loadStudentsSectionA(); }
  });

  if (pageSizeSel) {
    pageSizeSel.value = String(studentPageSizeA);
    pageSizeSel.addEventListener('change', (ev) => {
      studentPageSizeA = Number(ev.target.value) || 10;
      studentPageA = 1;
      loadStudentsSectionA();
    });
  }

  // if (filterSel) {
  //   filterSel.value = studentStatusFilterA || '';
  //   filterSel.addEventListener('change', (ev) => {
  //     studentStatusFilterA = ev.target.value;
  //     studentPageA = 1;
  //     loadStudentsSectionA();
  //   });
  // }

  document.getElementById('btnQueryAnnouncementSearch').addEventListener('click', () => {
    studentFilterA = filterAnnouncement.value.trim();
    studentPageA = 1;
    loadStudentsSectionA();
  });

  document.querySelectorAll('#annoucements thead th[data-sort]').forEach(th => {
    th.style.cursor = 'pointer';
    th.addEventListener('click', () => {
      const key = th.getAttribute('data-sort');
      if (studentSortKeyA === key) studentSortAscA = !studentSortAscA;
      else {
        studentSortKeyA = key;
        studentSortAscA = true;
      }
      loadStudentsSectionA();
    });
  });
}

wireStudentsControlsA();

async function goCreateAnnouncement() {
  try {
    const token = localStorage.getItem(TOKEN_KEY);

    const response = await fetch(`/admin/announcement_admin`, {
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
    sessionStorage.setItem("pageContentAnnouncementAdmin", html);
    window.location.href = "render_announcement_admin";
  } catch (err) {
    showNotification('Failed to fetch student detail: ' + err.message, 'error');
  }
}


document.getElementById('btnAnnouceCreate').addEventListener('click', goCreateAnnouncement)

function initStudentsSectionA() {
  wireStudentsControlsA();
  loadStudentsSectionA();
}

/* =========================
   Settings
   ========================= */
(() => {
  'use strict';

  const API_BASE = '/api/admin/settings';
  const JWT_KEY = 'jwt';

  // DOM elements (using IDs from your HTML)
  const paymentPhoneEl = document.getElementById('paymentPhone');
  const paymentFileEl = document.getElementById('paymentQR'); // file input (hidden)
  const paymentImgEl = document.querySelector('.admin-settings-img-wrapper img.admin-settings-img');
  const enrollmentFeeEl = document.getElementById('enrollmentFee');
  const dormitoryFeeEl = document.getElementById('dormitoryFee');
  const paymentNameEl = document.getElementById('paymentName');

  const enrollDateStartEl = document.getElementById('enrollDate');
  const enrollDateEndEl = document.getElementById('enrollDateEnd');
  const enrollOpenEl = document.getElementById('enrollOpen');
  const academicYearEl = document.getElementById('academicYear');

  // Buttons: there are two save buttons (payment, enrollment). Select by order.
  const saveButtons = Array.from(document.querySelectorAll('.admin-settings-section .admin-settings-save'));
  const savePaymentBtn = saveButtons[0];
  const saveEnrollmentBtn = saveButtons[1];

  // Local state for a selected file (so we know to upload)
  let selectedQrFile = null;
  // Preserve current server path for QR (used when no new file selected)
  let currentQrPath = null;

  // Utility: get JWT header object or throw
  function getAuthHeaders() {
    const token = localStorage.getItem(JWT_KEY);
    if (!token) throw new Error('Authorization token not found in localStorage (key: "jwt").');
    return {
      'Authorization': `Bearer ${token}`
    };
  }

  // Fetch current payment settings and populate fields
  async function loadPaymentSettings() {
    try {
      const res = await fetch(`${API_BASE}/payment`, { headers: getAuthHeaders() });
      if (!res.ok) throw new Error(`Failed to load payment settings (${res.status})`);
      const data = await res.json();

      paymentPhoneEl.value = data.paymentPhoneNumber ?? '';
      enrollmentFeeEl.value = data.enrollmentFee != null ? data.enrollmentFee : '';
      dormitoryFeeEl.value = data.dormitoryFee != null ? data.dormitoryFee : '';
      paymentNameEl.value = data.paymentName ?? '';

      // QR path: use image element src if provided, otherwise leave default
      currentQrPath = data.paymentQrPath ?? paymentImgEl.getAttribute('src') ?? null;
      if (!currentQrPath.startsWith("/public")) {
        currentQrPath = "/public" + currentQrPath;
      }
      if (currentQrPath) {
        paymentImgEl.src = currentQrPath;
      }
    } catch (err) {
      console.error('loadPaymentSettings:', err);
      // Minimal notification to admin
      alert('Unable to load payment settings. Check console for details.');
    }
  }

  // Fetch current enrollment settings and populate fields
  async function loadEnrollmentSettings() {
    try {
      const res = await fetch(`${API_BASE}/enrollment`, { headers: getAuthHeaders() });
      if (!res.ok) throw new Error(`Failed to load enrollment settings (${res.status})`);
      const data = await res.json();

      // LocalDate expected format: yyyy-MM-dd
      if (data.enrollmentStart) enrollDateStartEl.value = data.enrollmentStart;
      if (data.enrollmentEnd) enrollDateEndEl.value = data.enrollmentEnd;
      enrollOpenEl.value = (data.enrollmentOpen === true) ? 'on' : 'off';
      academicYearEl.value = data.academicYear ?? '';
    } catch (err) {
      console.error('loadEnrollmentSettings:', err);
      alert('Unable to load enrollment settings. Check console for details.');
    }
  }

  // Preview selected image immediately
  function setupFilePreview() {
    if (!paymentFileEl) return;
    paymentFileEl.addEventListener('change', (ev) => {
      const file = ev.target.files && ev.target.files[0];
      if (!file) {
        selectedQrFile = null;
        // if no file selected, revert to current QR path
        if (currentQrPath) paymentImgEl.src = currentQrPath;
        return;
      }

      // Basic validation: image only
      if (!file.type.startsWith('image/')) {
        alert('Please select a valid image file for the payment QR code.');
        paymentFileEl.value = '';
        return;
      }

      selectedQrFile = file;

      // Preview using object URL
      const previewUrl = URL.createObjectURL(file);
      paymentImgEl.src = previewUrl;

      // release object URL when image is loaded
      paymentImgEl.onload = () => {
        URL.revokeObjectURL(previewUrl);
      };
    });
  }

  // Upload QR file to server. Expects the backend to expose:
  // POST /api/admin/settings/payment/upload
  // - Request: multipart/form-data with field 'file'
  // - Response: { filePath: '/uploads/...' } (200)
  async function uploadQrFile(file) {
    if (!file) return null;

    const form = new FormData();
    form.append('file', file);

    const headers = getAuthHeaders();
    // Do not set Content-Type; browser will set multipart boundary
    const res = await fetch(`${API_BASE}/payment/upload`, {
      method: 'POST',
      headers: headers,
      body: form
    });

    if (!res.ok) {
      const text = await res.text().catch(() => '');
      throw new Error(`QR upload failed (${res.status}). ${text}`);
    }
    const json = await res.json();
    // Expect { filePath: '...' }
    if (!json.filePath) throw new Error('Upload response missing filePath.');
    return json.filePath;
  }

  // Save payment settings
  async function savePaymentSettings() {
    try {
      savePaymentBtn.disabled = true;
      savePaymentBtn.textContent = 'Saving...';

      // Build payload
      const payload = {
        paymentPhoneNumber: paymentPhoneEl.value?.trim() || null,
        paymentQrPath: currentQrPath, // fallback; may be replaced below
        enrollmentFee: enrollmentFeeEl.value ? parseFloat(enrollmentFeeEl.value) : null,
        dormitoryFee: dormitoryFeeEl.value ? parseFloat(dormitoryFeeEl.value) : null,
        paymentName: paymentNameEl.value?.trim() || null
      };

      // If admin selected a new QR image, upload it first
      if (selectedQrFile) {
        const filePath = await uploadQrFile(selectedQrFile);
        payload.paymentQrPath = filePath;
        currentQrPath = filePath; // update local state
        selectedQrFile = null;
        // clear file input
        paymentFileEl.value = '';
      }

      const headers = {
        ...getAuthHeaders(),
        'Content-Type': 'application/json'
      };

      const res = await fetch(`${API_BASE}/payment`, {
        method: 'PUT',
        headers,
        body: JSON.stringify(payload)
      });

      if (!res.ok) {
        const text = await res.text().catch(() => '');
        throw new Error(`Saving payment settings failed (${res.status}). ${text}`);
      }

      const updated = await res.json();
      // update local fields in case backend modified them
      paymentPhoneEl.value = updated.paymentPhoneNumber ?? paymentPhoneEl.value;
      enrollmentFeeEl.value = updated.enrollmentFee != null ? updated.enrollmentFee : enrollmentFeeEl.value;
      dormitoryFeeEl.value = updated.dormitoryFee != null ? updated.dormitoryFee : dormitoryFeeEl.value;
      paymentNameEl.value = updated.paymentName ?? paymentNameEl.value;
      if (updated.paymentQrPath) {
        currentQrPath = updated.paymentQrPath;
        if (!currentQrPath.startsWith("/public")) {
          currentQrPath = "/public" + currentQrPath;
        }
        paymentImgEl.src = currentQrPath;
      }

      alert('Payment settings saved successfully.');
    } catch (err) {
      console.error('savePaymentSettings:', err);
      alert('Failed to save payment settings. Check console for details.');
    } finally {
      savePaymentBtn.disabled = false;
      savePaymentBtn.textContent = 'Save Changes';
    }
  }

  // Save enrollment settings
  async function saveEnrollmentSettings() {
    try {
      saveEnrollmentBtn.disabled = true;
      saveEnrollmentBtn.textContent = 'Saving...';

      // Map select 'on'/'off' to boolean
      const enrollmentOpenValue = enrollOpenEl.value === 'on';

      const payload = {
        enrollmentStart: enrollDateStartEl.value || null, // ISO date 'YYYY-MM-DD' or null
        enrollmentEnd: enrollDateEndEl.value || null,
        enrollmentOpen: enrollmentOpenValue,
        academicYear: academicYearEl.value?.trim() || null
      };

      const headers = {
        ...getAuthHeaders(),
        'Content-Type': 'application/json'
      };

      const res = await fetch(`${API_BASE}/enrollment`, {
        method: 'PUT',
        headers,
        body: JSON.stringify(payload)
      });

      if (!res.ok) {
        const text = await res.text().catch(() => '');
        throw new Error(`Saving enrollment settings failed (${res.status}). ${text}`);
      }

      // Optionally use returned object to refresh UI
      const updated = await res.json();
      if (updated.enrollmentStart) enrollDateStartEl.value = updated.enrollmentStart;
      if (updated.enrollmentEnd) enrollDateEndEl.value = updated.enrollmentEnd;
      enrollOpenEl.value = (updated.enrollmentOpen === true) ? 'on' : 'off';
      academicYearEl.value = updated.academicYear ?? academicYearEl.value;

      alert('Enrollment settings saved successfully.');
    } catch (err) {
      console.error('saveEnrollmentSettings:', err);
      alert('Failed to save enrollment settings. Check console for details.');
    } finally {
      saveEnrollmentBtn.disabled = false;
      saveEnrollmentBtn.textContent = 'Save Changes';
    }
  }

  // Wire up event listeners and initial load
  function init() {
    try {
      // Attach listeners
      savePaymentBtn.addEventListener('click', (e) => {
        e.preventDefault();
        savePaymentSettings();
      });

      saveEnrollmentBtn.addEventListener('click', (e) => {
        e.preventDefault();
        saveEnrollmentSettings();
      });

      setupFilePreview();

      // Load initial data in parallel
      loadPaymentSettings();
      loadEnrollmentSettings();
    } catch (err) {
      console.error('init error:', err);
    }
  }



  init();

})();



/* =========================
   Profile set up
   ========================= */

async function initProfile() {
  profileName = document.getElementById("profile-name");
  profileRole = document.getElementById("profile-role");

  try {
    const data = await fetchJson(ENDPOINTS.PROFILE);
    profileName.innerText =
      data.fullName.length > 20
        ? data.fullName.substring(0, 20) + "..."
        : data.fullName;

    profileRole.innerText =
      data.roles.join(", ").length > 20
        ? data.roles.join(", ").substring(0, 20) + "..."
        : data.roles.join(", ");

  } catch (err) {
    showNotification('Failed to load profile: ' + err.message, 'error');
  }

}




/* =========================
   Init
   ========================= */


// minimal guard: redirect to login if no token
if (!getToken()) {
  redirectToLogin();
}

//admin profile setup
initProfile();

// Set logo and avatar paths (non-critical)
const logo = document.querySelector('.brand-logo');
if (logo) logo.src = LOGO_FILE;
const avatar = document.querySelector('.avatar');
if (avatar) avatar.src = AVATAR_FILE;

attachEventListeners();

// default load: dashboard
loadSection('dashboard').catch(err => {
  console.error('Failed loading initial section', err);
});


// DARK THEME SAVED
const savedTheme = getSavedTheme();
applyTheme(savedTheme);

// Wire toggle button
wireThemeToggleButton();

// after wireThemeToggleButton(); call:
wireMobileMenuButton();

