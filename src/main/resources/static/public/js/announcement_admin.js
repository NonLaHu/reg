// announcement-create.js
// Pure JS — handles form, client validation, JWT header, XHR upload with progress and robust feedback.

// CONFIG: change this if you store token under a different key
const JWT_STORAGE_KEY = 'jwt'; // e.g. localStorage.setItem('jwtToken', '<your-token>')

// Endpoints
const CREATE_URL = '/api/announcements/create'; // backend endpoint

const form = document.getElementById('announcementForm');
const submitBtn = document.getElementById('submitBtn');
const resetBtn = document.getElementById('resetBtn');
const fileInput = document.getElementById('pdfFile');
const fileMeta = document.getElementById('fileMeta');
const progressWrap = document.getElementById('progressWrap');
const progressBar = document.getElementById('progressBar');
const progressText = document.getElementById('progressText');
const feedback = document.getElementById('feedback');
const authState = document.getElementById('authState');
const toggleThemeBtn = document.getElementById("toggleTheme");

// File constraints
const MAX_BYTES = 10 * 1024 * 1024; // 5 MB
const ALLOWED_TYPE = 'application/pdf';

// Show token status
const token = (localStorage.getItem(JWT_STORAGE_KEY) || '').trim();
if (!token) {
    authState.textContent = 'JWT token missing. Please authenticate (token expected in localStorage key "' + JWT_STORAGE_KEY + '").';
    authState.style.color = 'var(--danger)';
} else {
    authState.textContent = 'Authenticated (token present).';
    authState.style.color = 'var(--muted)';
}

// File input change handler
fileInput.addEventListener('change', () => {
    feedback.textContent = '';
    const f = fileInput.files[0];
    if (!f) {
        fileMeta.textContent = 'No file selected.';
        return;
    }

    // Validate type
    if (f.type !== ALLOWED_TYPE) {
        fileMeta.textContent = `Invalid file type: ${f.type}. Please upload a PDF.`;
        fileMeta.style.color = 'var(--danger)';
        fileInput.value = ''; // reset
        return;
    }

    // Validate size
    if (f.size > MAX_BYTES) {
        fileMeta.textContent = `File too large: ${(f.size / (1024*1024)).toFixed(2)} MB. Max is 5 MB.`;
        fileMeta.style.color = 'var(--danger)';
        fileInput.value = '';
        return;
    }

    fileMeta.style.color = '';
    fileMeta.textContent = `${f.name} — ${(f.size / 1024).toFixed(0)} KB`;
});

// Reset handler: clear UI bits
resetBtn.addEventListener('click', () => {
    fileMeta.textContent = 'No file selected.';
    progressWrap.hidden = true;
    progressText.textContent = '0%';
    setProgress(0);
    feedback.textContent = '';
});

// Submit handler
submitBtn.addEventListener('click', () => {
    feedback.textContent = '';
    if (!token) {
        feedback.innerHTML = `<span class="error">Authentication token missing. Cannot create announcement.</span>`;
        return;
    }

    // Basic client validation
    const title = document.getElementById('title').value.trim();
    const description = document.getElementById('description').value.trim();
    const publishDate = document.getElementById('publishDate').value; // may be empty
    const expiryDate = document.getElementById('expiryDate').value;
    const file = fileInput.files[0];

    if (!title) {
        feedback.innerHTML = `<span class="error">Title is required.</span>`;
        return;
    }
    if (!file) {
        feedback.innerHTML = `<span class="error">PDF file is required.</span>`;
        return;
    }

    // Prepare form data
    const formData = new FormData();
    formData.append('title', title);
    if (description) formData.append('description', description);
    if (publishDate) formData.append('publishDate', publishDate); // "YYYY-MM-DDTHH:mm"
    if (expiryDate) formData.append('expiryDate', expiryDate);
    formData.append('pdf', file, file.name);

    // Disable UI
    submitBtn.disabled = true;
    submitBtn.textContent = 'Uploading…';
    progressWrap.hidden = false;
    setProgress(0);

    // Use XHR for reliable upload progress
    const xhr = new XMLHttpRequest();
    xhr.open('POST', CREATE_URL, true);
    xhr.setRequestHeader('Authorization', 'Bearer ' + token);

    // Upload progress
    xhr.upload.addEventListener('progress', (e) => {
        if (e.lengthComputable) {
            const pct = Math.round((e.loaded / e.total) * 100);
            setProgress(pct);
        }
    });

    // Response handling
    xhr.onreadystatechange = function () {
        if (xhr.readyState !== 4) return;
        submitBtn.disabled = false;
        submitBtn.textContent = 'Create';
        if (xhr.status >= 200 && xhr.status < 300) {
            // Success
            let message = 'Announcement created successfully.';
            try {
                const json = JSON.parse(xhr.responseText || '{}');
                // If backend returns useful fields, include them
                if (json.title) message = `Created: ${escapeHtml(json.title)}`;
            } catch (e) {
                // non-json response ok
            }
            feedback.innerHTML = `<span class="ok">${escapeHtml(message)}</span>`;
            // Reset form
            form.reset();
            fileMeta.textContent = 'No file selected.';
            progressWrap.hidden = true;

            // Option: redirect to admin list after short delay
            setTimeout(() => {
                // If you want to redirect to admin list, uncomment:
                // window.location.href = '/admin/announcements';
            }, 900);
            return;
        }

        // Error handling
        let errMsg = `Server error (status ${xhr.status}).`;
        try {
            const payload = JSON.parse(xhr.responseText || '{}');
            if (payload && payload.message) errMsg = payload.message;
            else if (payload && payload.error) errMsg = payload.error;
        } catch (e) {
            // response not json
            if (xhr.responseText) errMsg = xhr.responseText;
        }

        // Distinguish auth errors
        if (xhr.status === 401 || xhr.status === 403) {
            errMsg = 'Authentication failed (invalid or expired token). Please re-authenticate.';
        } else if (xhr.status === 413) {
            errMsg = 'Uploaded file too large for server. Reduce file size and retry.';
        }

        feedback.innerHTML = `<span class="error">${escapeHtml(errMsg)}</span>`;
        progressWrap.hidden = true;
        setProgress(0);
    };

    // Network error
    xhr.onerror = function () {
        submitBtn.disabled = false;
        submitBtn.textContent = 'Create';
        feedback.innerHTML = `<span class="error">Network error. Check connection and try again.</span>`;
        progressWrap.hidden = true;
        setProgress(0);
    };

    // Send
    xhr.send(formData);
});

// Helper: set progress bar
function setProgress(percent) {
    const p = Math.max(0, Math.min(100, percent));
    progressBar.style.setProperty('--pct', `${p}%`);
    progressBar.querySelector('::after'); // no-op to keep style
    // update pseudo-element via inline style:
    progressBar.style.background = 'linear-gradient(90deg, rgba(11,99,214,0.1) 0%, rgba(11,99,214,0.1) 100%)';
    // Instead update the ::after width by setting style on ::after is not possible;
    // use inline child element to reflect progress:
    if (!progressBar._fill) {
        const fill = document.createElement('div');
        fill.style.position = 'absolute';
        fill.style.left = '0';
        fill.style.top = '0';
        fill.style.bottom = '0';
        fill.style.width = '0%';
        fill.style.background = 'linear-gradient(90deg, var(--accent), #2e9bf5)';
        fill.style.transition = 'width 0.15s linear';
        fill.style.borderRadius = '8px';
        progressBar.style.position = 'relative';
        progressBar.appendChild(fill);
        progressBar._fill = fill;
    }
    progressBar._fill.style.width = p + '%';
    progressText.textContent = p + '%';
}

// Helper: escape html in messages
function escapeHtml(s) {
    return String(s)
        .replace(/&/g, '&amp;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;')
        .replace(/"/g, '&quot;')
        .replace(/'/g, '&#39;');
}

const savedTheme = localStorage.getItem("theme");
if (savedTheme) {
    document.body.classList.add(savedTheme);
    toggleThemeBtn.textContent = savedTheme === "dark" ? "☀️ Light" : "🌙 Dark";
} else {
    // Default theme
    document.body.classList.add("dark");
    toggleThemeBtn.textContent = "☀️ Light";
}

toggleThemeBtn.addEventListener("click", () => {
    if (document.body.classList.contains("dark")) {
        document.body.classList.replace("dark", "light");
        toggleThemeBtn.textContent = "🌙 Dark";
        localStorage.setItem("theme", "light");
    } else {
        document.body.classList.replace("light", "dark");
        toggleThemeBtn.textContent = "☀️ Light";
        localStorage.setItem("theme", "dark");
    }
});
