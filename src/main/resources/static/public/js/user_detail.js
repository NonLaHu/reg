// user_detail.js — JWT / Bearer token version
// Assumes JWT stored in localStorage under key 'access_token'.
// If you use HttpOnly cookies, remove token handling and rely on server cookie auth.

(function () {
    'use strict';

    // CONFIG
    const TOKEN_KEY = 'jwt';               // <- change if your token key differs
    const API_PREFIX = '/admin/users';              // base API path
    const MIN_PASSWORD_LENGTH = 8;

    // Utilities
    const $ = sel => document.querySelector(sel);
    const $$ = sel => Array.from(document.querySelectorAll(sel));
    const ID = (function() {
        // Add a hidden element in your template: <span data-user-id="${user.id}" style="display:none"></span>
        const el = document.querySelector('span[data-user-id]');
        if (el) return el.getAttribute('data-user-id');
        // Fallback: try to parse from header "ID: 123" text
        const headerSpan = document.querySelector('.card-header .sub');
        if (headerSpan) {
            const m = headerSpan.textContent.match(/ID:\s*(\d+)/);
            if (m) return m[1];
        }
        console.warn('user_detail.js: user id not found. Add <span data-user-id="${user.id}" style="display:none"></span> for reliability.');
        return null;
    })();

    // DOM refs
    const messageEl = $('#message');
    const grantBtn = $('#grantBtn');
    const roleSelect = $('#roleSelect');
    const currentRolesList = $('#currentRoles');
    const resetBtn = $('#resetBtn');
    const generateBtn = $('#generateBtn');
    const newPasswordInput = $('#newPassword');
    const themeToggle = $('#themeToggle');

    // Authorization header helper
    function authHeader() {
        const token = localStorage.getItem(TOKEN_KEY);
        if (!token) return null;
        return { 'Authorization': 'Bearer ' + token };
    }

    function apiUrlFor(path) {
        if (!ID) throw new Error('User ID not available');
        // path like '/roles/{role}/grant' or '/reset-password'
        return `${API_PREFIX}/${encodeURIComponent(ID)}${path}`;
    }

    // Generic fetch wrapper (adds Authorization)
    async function apiFetch(path, opts = {}) {
        const headers = opts.headers || {};
        const auth = authHeader();
        if (!auth) {
            // Redirect to login or show message
            showMessage('Not authenticated. Please log in.', 'error', 6000);
            throw new Error('Missing auth token');
        }
        Object.assign(headers, auth);
        headers['Content-Type'] = headers['Content-Type'] || 'application/json';
        const merged = Object.assign({}, opts, { headers });
        const res = await fetch(apiUrlFor(path), merged);
        if (!res.ok) {
            let payload = {};
            try { payload = await res.json(); } catch (_) {}
            const msg = payload.message || `Server returned ${res.status}`;
            throw new Error(msg);
        }
        // return JSON or empty object
        const text = await res.text().catch(() => '');
        return text ? JSON.parse(text) : {};
    }

    // UI helpers
    function showMessage(text, type = 'success', ttl = 4000) {
        if (!messageEl) return console.log(text);
        messageEl.textContent = text;
        messageEl.className = 'msg show ' + (type === 'error' ? 'error' : 'success');
        if (ttl > 0) setTimeout(() => messageEl.className = 'msg', ttl);
    }

    function setBusy(flag) {
        const allBtns = document.querySelectorAll('button, select, input');
        allBtns.forEach(b => b.disabled = flag);
        document.body.style.cursor = flag ? 'wait' : '';
    }

    // Grant role
    grantBtn?.addEventListener('click', async () => {
        const role = roleSelect.value;
        if (!role) { showMessage('Please choose a role to grant.', 'error'); return; }
        if (!confirm(`Grant role "${role}" to this user?`)) return;
        setBusy(true);
        try {
            await apiFetch(`/roles/${encodeURIComponent(role)}/grant`, { method: 'POST' });
            showMessage(`Role "${role}" granted.`);
            if (![...$$('.role-name')].some(el => el.textContent === role)) {
                const li = document.createElement('li');
                li.innerHTML = `<span class="role-name">${role}</span>
                                <button class="btn danger small revoke-btn" type="button" data-role="${role}">Revoke</button>`;
                currentRolesList.appendChild(li);
                attachRevokeFor(li.querySelector('.revoke-btn'));
            }
        } catch (err) {
            showMessage(err.message || 'Failed to grant role', 'error', 6000);
        } finally {
            setBusy(false);
        }
    });

    // Revoke
    function attachRevokeFor(btn) {
        if (!btn) return;
        btn.addEventListener('click', async () => {
            const role = btn.getAttribute('data-role');
            if (!role) return;
            if (!confirm(`Revoke role "${role}" from this user?`)) return;
            setBusy(true);
            try {
                await apiFetch(`/roles/${encodeURIComponent(role)}/revoke`, { method: 'POST' });
                showMessage(`Role "${role}" revoked.`);
                const li = btn.closest('li');
                if (li) li.remove();
            } catch (err) {
                showMessage(err.message || 'Failed to revoke role', 'error', 6000);
            } finally {
                setBusy(false);
            }
        });
    }
    $$('.revoke-btn').forEach(attachRevokeFor);

    // Reset password (superadmin-provided)
    resetBtn?.addEventListener('click', async () => {
        const newPassword = newPasswordInput.value?.trim();
        if (!newPassword || newPassword.length < MIN_PASSWORD_LENGTH) {
            showMessage(`Password must be at least ${MIN_PASSWORD_LENGTH} characters`, 'error');
            return;
        }
        if (!confirm('Are you sure you want to reset the password for this user?')) return;
        setBusy(true);
        try {
            await apiFetch('/reset-password', { method: 'POST', body: JSON.stringify({ newPassword }) });
            showMessage('Password reset successfully.');
            newPasswordInput.value = '';
        } catch (err) {
            showMessage(err.message || 'Failed to reset password', 'error', 6000);
        } finally {
            setBusy(false);
        }
    });

    // Generate password
    generateBtn?.addEventListener('click', () => {
        newPasswordInput.value = generatePassword();
    });

    function generatePassword() {
        const chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%&*-_";
        let pw = '';
        for (let i = 0; i < 12; i++) pw += chars[Math.floor(Math.random() * chars.length)];
        return pw;
    }

    // Theme toggle
    function applyTheme(name) {
        document.documentElement.setAttribute('data-theme', name);
        document.body.setAttribute('data-theme', name);
        localStorage.setItem('theme', name);
        if (themeToggle) themeToggle.setAttribute('aria-pressed', name === 'light' ? 'true' : 'false');
    }
    themeToggle?.addEventListener('click', () => {
        const cur = document.documentElement.getAttribute('data-theme') || 'dark';
        applyTheme(cur === 'dark' ? 'light' : 'dark');
    });
    (function initTheme() { applyTheme(localStorage.getItem('theme') || 'dark'); })();

    const toggleBtn = document.getElementById("togglePasswordBtn");
    const passwordInput = document.getElementById("newPassword");
    const eyeOpen = document.getElementById("eyeOpenIcon");
    const eyeSlash = document.getElementById("eyeSlashIcon");

    toggleBtn.addEventListener("click", () => {
    const isPassword = passwordInput.type === "password";
    passwordInput.type = isPassword ? "text" : "password";

    // Toggle icons
    eyeOpen.style.display = isPassword ? "none" : "block";
    eyeSlash.style.display = isPassword ? "block" : "none";

    // Update aria-label
    toggleBtn.setAttribute("aria-label", isPassword ? "Hide password" : "Show password");
    });

})();
