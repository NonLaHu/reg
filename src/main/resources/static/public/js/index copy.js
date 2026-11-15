//Theme Toggle
const themeBtn = document.getElementById("themeToggle");
themeBtn.addEventListener("click", () => {
    document.body.classList.toggle("dark");
});

//Language Toggle
const langBtn = document.getElementById("langToggle");
langBtn.addEventListener("click", () => {
    const elements = document.querySelectorAll(".lang");
    elements.forEach(el => {
        const current = el.innerText;
        const en = el.dataset.en;
        const my = el.dataset.my;
        el.innerText = (current === en) ? my : en;
    });
});

langBtn.click();
themeBtn.click();

document.addEventListener("DOMContentLoaded", function() {
    const dropdownBtn = document.createElement("button");
    dropdownBtn.className = "dropdown-btn";
    dropdownBtn.textContent = "Find Login Link";

    const dropdownContent = document.createElement("div");
    dropdownContent.className = "dropdown-content";

    const navLinks = document.querySelectorAll("nav ul li a");
    navLinks.forEach(link => {
        dropdownContent.appendChild(link);
    });

    document.querySelector("nav ul").remove();

    const nav = document.querySelector("nav");
    nav.style.position = "relative"; 
    nav.appendChild(dropdownBtn);
    nav.appendChild(dropdownContent);

    dropdownBtn.addEventListener("click", () => {
        dropdownContent.style.display = dropdownContent.style.display === "block" ? "none" : "block";
    });

    window.addEventListener("click", function(e) {
        if (!nav.contains(e.target)) {
            dropdownContent.style.display = "none";
        }
    });
    
const goToEnrollmentA = document.getElementById('new'); 
async function goToEnrollment() {
    try {
        const token = localStorage.getItem('jwt');

        const response = await fetch(`/page/enrollment`, {
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
        sessionStorage.setItem("pageContentEnrollment", html);

        // Redirect to render page
        window.location.href = "render_enrollment";

    } catch (err) {
        console.log('Failed to fetch enrollment detail: ' + err.message, 'error');
    }
}
goToEnrollmentA.addEventListener("click",goToEnrollment);
});
