const form = document.getElementById('userForm');
const submitBtn = document.getElementById('submitBtn');
const feedback = document.getElementById('feedback');

submitBtn.addEventListener('click', async () => {
    const fullName = form.username.value.trim();
    const email = form.email.value.trim();
    const role = form.role.value; // e.g., "ADMIN" or "STUDENT"
    const password = form.password.value;
    const confirmPassword = form.confirmPassword.value;

    // Validation
    if (!fullName || !email || !role || !password || !confirmPassword) {
        feedback.innerHTML = '<span class="error">All required fields must be filled.</span>';
        return;
    }
    if (password !== confirmPassword) {
        feedback.innerHTML = '<span class="error">Passwords do not match.</span>';
        return;
    }

    // Send request
    const token = localStorage.getItem('jwt');

    submitBtn.disabled = true;
    submitBtn.textContent = 'Creating…';
    try {
        const response = await fetch('/admin/users/create', {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            },
            body: JSON.stringify({ fullName, email, password, role })
        });

        let data = {};
        try {
            data = await response.json();
        } catch {
            data = {};
        }

        if (response.ok) {
            feedback.innerHTML = `<span class="ok">${data.message || 'User created successfully.'}</span>`;
            form.reset();
        } else {
            let message = 'An unexpected error occurred';

            switch (response.status) {
                case 400:
                    message = data.message || 'Invalid request. Please check the input.';
                    break;
                case 401:
                    message = 'Unauthorized. Please log in again.';
                    break;
                case 403:
                    message = 'Forbidden. You do not have permission.';
                    break;
                case 409:
                    message = data.message || 'Conflict: resource already exists.';
                    break;
                case 500:
                    message = 'Server error. Please try again later.';
                    break;
                default:
                    message = data.message || `Error ${response.status}`;
            }

            feedback.innerHTML = `<span class="error">${message}</span>`;
        }
    } catch (err) {
        feedback.innerHTML = `<span class="error">Network error: ${err.message}</span>`;
    } finally {
        submitBtn.disabled = false;
        submitBtn.textContent = 'Create';
    }
});

const toggleThemeBtn = document.getElementById("toggleTheme");
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