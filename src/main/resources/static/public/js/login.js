//THEME
// theme-toggle.js — include after DOM or at end of body
(function () {
  'use strict';

  const THEME_KEY = 'atashi-theme';
  const btn = document.getElementById('themeToggle');
  if (!btn) return;


  function applyTheme(theme) {
    if (theme === 'dark') {
      document.documentElement.setAttribute('data-theme', 'dark'); // main switch
      document.body.classList.add('dark');                         // compatibility
      btn.setAttribute('aria-pressed', 'true');
      localStorage.setItem(THEME_KEY, 'dark');
    } else {
      document.documentElement.removeAttribute('data-theme');
      document.body.classList.remove('dark');
      btn.setAttribute('aria-pressed', 'false');
      localStorage.setItem(THEME_KEY, 'light');
    }
  }

  function getSavedOrSystemTheme() {
    const saved = localStorage.getItem(THEME_KEY);
    if (saved === 'dark' || saved === 'light') return saved;
    if (window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches) return 'dark';
    return 'light';
  }

  function toggleTheme() {
    const currentIsDark = document.documentElement.getAttribute('data-theme') === 'dark';
    applyTheme(currentIsDark ? 'light' : 'dark');
  }

  const initial = getSavedOrSystemTheme();
  applyTheme(initial);

  btn.addEventListener('click', (e) => {
    e.preventDefault();
    toggleTheme();
  });

  if (window.matchMedia) {
    window.matchMedia('(prefers-color-scheme: dark)').addEventListener('change', e => {
      if (!localStorage.getItem(THEME_KEY)) {
        applyTheme(e.matches ? 'dark' : 'light');
      }
    });
  }
})();

//student dashboard rendering
async function FetchAndGoDashboard(){
    try {
        const token = localStorage.getItem('jwt');

        const response = await fetch(`/user_dashboard`, {
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
        sessionStorage.setItem("pageContentStudentDashboard", html);

        // Redirect to render page
        window.location.href = "dashboard";

    } catch (err) {
        console.log('Failed to fetch enrollment detail: ' + err.message, 'error');
    }
}

//ping check
fetch('/auth/ping') 
    .then(response => response.json())
    .then(data => {
        if (data.status === 'ok') {
            console.log('API connected successfully.');
        } else {
            console.log('API connected successfully.');
        }
    })
    .catch(error => {
        console.error('API error:', error);
        console.log('API connected successfully.');
    });

const registrationForm = document.getElementById('registrationForm');

registrationForm.addEventListener('submit', (e) => {
    e.preventDefault();

    const formData = {
        password: registrationForm.password.value,
        email: registrationForm.email.value,
    };

    fetch('/auth/login', { 
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(formData)
    })
    .then(async response => {
        const data = await response.json();
        if (response.ok) {                  
            console.log('jwt:'+data.token+'\n expire in:'+data.expiresIn);
            localStorage.setItem("jwt", data.token);
            registrationForm.reset();
            if (data.admin){
                window.location.href = "/admin";
            }else {
                FetchAndGoDashboard();
            }
        } else {                             
            const message = data.message || 'Registration failed. Try again.';
            alert(message);
        }
    })
    .catch(error => {
        console.error('Network error:', error);
        alert('Network error. Please try again later.');
    });
});
const toggleBtn = document.getElementById('togglePasswordBtn');
const passwordInput = document.getElementById('password');
const eyeOpenIcon = document.getElementById('eyeOpenIcon');
const eyeSlashIcon = document.getElementById('eyeSlashIcon');

toggleBtn.addEventListener('click', () => {
  const isHidden = passwordInput.type === 'password';
  passwordInput.type = isHidden ? 'text' : 'password';

  // toggle icons
  eyeOpenIcon.style.display = isHidden ? 'none' : 'inline';
  eyeSlashIcon.style.display = isHidden ? 'inline' : 'none';

  toggleBtn.setAttribute('aria-label', isHidden ? 'Hide password' : 'Show password');
});

