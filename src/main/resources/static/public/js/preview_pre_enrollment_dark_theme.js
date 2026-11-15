(function(){
  'use strict';
  const THEME_KEY = 'theme';
  const toggleBtn = document.getElementById('themeToggle');
  const iconEl = document.getElementById('themeToggleIcon');
  const textEl = document.getElementById('themeToggleText');

  function applyTheme(theme) {
    if (theme === 'dark') {
      document.documentElement.setAttribute('data-theme','dark');
      if (toggleBtn) toggleBtn.setAttribute('aria-pressed','true');
      if (iconEl) iconEl.textContent = '☀️';    // sun icon (switch to light)
      if (textEl) textEl.textContent = 'Light';
      localStorage.setItem(THEME_KEY, 'dark');
    } else {
      document.documentElement.removeAttribute('data-theme');
      if (toggleBtn) toggleBtn.setAttribute('aria-pressed','false');
      if (iconEl) iconEl.textContent = '🌙';    // moon icon (switch to dark)
      if (textEl) textEl.textContent = 'Dark';
      localStorage.setItem(THEME_KEY, 'light');
    }
  }

  function getInitialTheme() {
    const saved = localStorage.getItem(THEME_KEY);
    if (saved === 'dark' || saved === 'light') return saved;
    // fall back to system preference
    if (window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches) {
      return 'dark';
    }
    return 'light';
  }

  function toggleTheme() {
    const isDark = document.documentElement.getAttribute('data-theme') === 'dark';
    applyTheme(isDark ? 'light' : 'dark');
  }

  // wire button
  if (toggleBtn) {
    toggleBtn.addEventListener('click', (e) => {
      e.preventDefault();
      toggleTheme();
    });
    // keyboard accessible: space/enter handled by button automatically
  }

  // initialize on DOMContentLoaded (or run immediately if DOM already ready)
  if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', () => applyTheme(getInitialTheme()));
  } else {
    applyTheme(getInitialTheme());
  }

  // Optional: react to system change when user hasn't explicitly set a preference
  window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').addEventListener('change', e => {
    const saved = localStorage.getItem(THEME_KEY);
    if (!saved) { // only auto-switch if user didn't choose manually
      applyTheme(e.matches ? 'dark' : 'light');
    }
  });

})();