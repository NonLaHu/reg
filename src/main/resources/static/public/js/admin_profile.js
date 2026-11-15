const userIdEl = document.getElementById("userId");
const emailEl = document.getElementById("email");
const fullNameEl = document.getElementById("fullName");
const userTypeEl = document.getElementById("userType");
const statusEl = document.getElementById("status");
const rolesEl = document.getElementById("roles");
const createdAtEl = document.getElementById("createdAt");
const resetForm = document.getElementById("resetForm");
const resetMessage = document.getElementById("resetMessage");
const toggleThemeBtn = document.getElementById("toggleTheme");

let currentUserId = null;

const token = localStorage.getItem("jwt"); // or wherever you store it
fetch("/admin/users/me", {
    headers: {
        "Accept": "application/json",
        "Authorization": `Bearer ${token}`
    }
})
.then(res => {
    if (!res.ok) throw new Error("Unauthorized");
    return res.json();
})
.then(data => {
    currentUserId = data.id;
    userIdEl.textContent = data.id;
    emailEl.textContent = data.email;
    fullNameEl.textContent = data.fullName;
    userTypeEl.textContent = data.userType;
    statusEl.textContent = data.status;
    rolesEl.textContent = Array.from(data.roles).join(", ");
    createdAtEl.textContent = new Date(data.createdAt).toLocaleString();
})
.catch(err => {
    console.error(err);
    alert("Failed to load profile.");
});

// Reset password
resetForm.addEventListener("submit", e => {
  e.preventDefault();

  const oldPassword = document.getElementById("oldPassword").value.trim();
  const newPassword = document.getElementById("newPassword").value.trim();
  const confirmPassword = document.getElementById("confirmPassword").value.trim();

  // Validation
  if (!oldPassword || !newPassword || !confirmPassword) {
    resetMessage.textContent = "All fields are required.";
    return;
  }

  if (newPassword.length < 8) {
    resetMessage.textContent = "New password must be at least 8 characters.";
    return;
  }

  if (newPassword !== confirmPassword) {
    resetMessage.textContent = "New password and confirmation do not match.";
    return;
  }

    // Call your existing backend endpoint
    fetch("/api/reset-password", {
        method: "POST",
        headers: { "Content-Type": "application/json",
        "Authorization": `Bearer ${token}` },
        body: JSON.stringify({
            oldPassword: oldPassword,
            newPassword: newPassword
        })
    })
    .then(async res => {
        const text = await res.text();
        if (!res.ok) throw new Error(text);
        resetMessage.textContent = text;
        resetForm.reset();
    })
    .catch(err => {
        resetMessage.textContent = err.message || "Failed to reset password.";
        console.error(err);
    });
});



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

