
async function profileSetup() {
    const token = localStorage.getItem("jwt");
    if (!token) return; // silently exit if no token

    try {
        const response = await fetch("/admin/users/me", {
            headers: {
                "Authorization": `Bearer ${token}`,
                "Content-Type": "application/json"
            }
        });

        if (!response.ok) return; // silently exit if bad response

        const user = await response.json();
        // Limit fullName to 10 words
        let displayName = "User";
        if (user.fullName) {
            const words = user.fullName.trim().split(/\s+/);
            displayName = words.slice(0, 10).join(" ");
        }

        document.getElementById("user-fullname").textContent = displayName;

        document.getElementById("user-roles").textContent = user.roles ? `(${Array.from(user.roles).join(", ")})` : "";

        // Toggle UI
        document.getElementById("login-btn").style.display = "none";
        document.getElementById("register-btn").style.display = "none";
        document.getElementById("user-section").style.display = "flex";

        if (user.userType) {
            document
                .getElementById("user-section")
                .setAttribute("data-user-type", user.userType);
        }

        // Sign out handler
        document.getElementById("signout-btn").addEventListener("click", () => {
            localStorage.removeItem("jwt");
            location.reload();
        });

    } catch (err) {
        console.error("User fetch failed", err);
        // silently ignore
    }
}
profileSetup();


//profile clickable
profileBtn = document.getElementById('profile-user');

profileBtn.addEventListener('click', async () => {
    const userType = document
        .getElementById("user-section")
        .getAttribute("data-user-type");

    if (userType == "STUDENT") {

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
    } else if (userType == "ADMIN") {
        window.location.href = "/admin";
    }
})
