const deleteBtn = document.getElementById('deleteBtn');

deleteBtn.addEventListener('click', async function () {
    const titleEl = document.querySelector('.title');
    const titleText = titleEl ? titleEl.textContent.trim() : 'this announcement';

    const confirmed = window.confirm(`Delete "${titleText}"? This action cannot be undone.`);
    if (!confirmed) return;

    // Show simple loading state
    deleteBtn.disabled = true;
    deleteBtn.textContent = 'Deleting…';

    try {
        const announcementId = parseInt(deleteBtn.dataset.id); // add data-id attribute in HTML
        const token = localStorage.getItem('jwt');   // assuming JWT stored here

        const response = await fetch(`/api/announcements/delete/${announcementId}`, {
            method: 'DELETE',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Accept': 'application/json'
            }
        });

        if (response.ok) {
            alert(`"${titleText}" deleted successfully.`);
            window.location.href = '/admin';
        } else {
            const error = await response.json().catch(() => ({ message: 'Unknown error' }));
            alert(`Failed to delete: ${error.message || response.statusText}`);
            deleteBtn.disabled = false;
            deleteBtn.textContent = 'Delete';
        }
    } catch (err) {
        alert(`Error: ${err.message}`);
        deleteBtn.disabled = false;
        deleteBtn.textContent = 'Delete';
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


