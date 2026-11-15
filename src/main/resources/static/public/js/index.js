document.addEventListener("DOMContentLoaded", () => {
    // Mobile Navigation Toggle Script
    const navOpenBtn = document.getElementById('nav-open-btn');
    const navCloseBtn = document.getElementById('nav-close-btn');
    const mobileNav = document.getElementById('mobile-nav');
    const body = document.body;

    if (navOpenBtn && mobileNav && navCloseBtn) {
        navOpenBtn.addEventListener('click', () => {
            mobileNav.classList.add('is-open');
            body.classList.add('nav-open');
        });
        navCloseBtn.addEventListener('click', () => {
            mobileNav.classList.remove('is-open');
            body.classList.remove('nav-open');
        });
    }

    // CAROUSEL SCRIPT
    const carousel = document.querySelector("#hero-carousel-container");
    if (carousel) {
        const slidesContainer = carousel.querySelector(".carousel-slides");
        const slides = carousel.querySelectorAll(".carousel-slide");
        const prevBtn = carousel.querySelector(".carousel-btn.prev");
        const nextBtn = carousel.querySelector(".carousel-btn.next");
        const dotsContainer = carousel.querySelector(".carousel-dots");
        let currentIndex = 0;
        let slideInterval;
        const totalSlides = slides.length;

        if (totalSlides > 1) {
            slides.forEach((_, i) => {
                const dot = document.createElement("button");
                dot.classList.add("carousel-dot");
                if (i === 0) dot.classList.add("active");
                dot.addEventListener("click", () => goToSlide(i));
                dotsContainer.appendChild(dot);
            });
            const dots = dotsContainer.querySelectorAll(".carousel-dot");

            function goToSlide(index) {
                currentIndex = index;
                slidesContainer.style.transform = `translateX(-${currentIndex * 100}%)`;
                dots.forEach((dot, i) => dot.classList.toggle("active", i === currentIndex));
                resetInterval();
            }

            function nextSlide() {
                currentIndex = (currentIndex + 1) % totalSlides;
                goToSlide(currentIndex);
            }

            function prevSlide() {
                currentIndex = (currentIndex - 1 + totalSlides) % totalSlides;
                goToSlide(currentIndex);
            }

            function startInterval() {
                clearInterval(slideInterval);
                slideInterval = setInterval(nextSlide, 5000);
            }

            function resetInterval() {
                startInterval();
            }

            nextBtn.addEventListener("click", nextSlide);
            prevBtn.addEventListener("click", prevSlide);

            carousel.addEventListener('mouseenter', () => clearInterval(slideInterval));
            carousel.addEventListener('mouseleave', startInterval);

            startInterval();
        }
    }

    // STATS COUNTER SCRIPT
    const statsSection = document.querySelector("#stats");
    if (statsSection) {
        const observer = new IntersectionObserver((entries, observer) => {
            entries.forEach(entry => {
                if (entry.isIntersecting) {
                    const counters = statsSection.querySelectorAll('.stat-number');
                    counters.forEach(counter => {
                        const animate = () => {
                            const goal = +counter.getAttribute('data-goal');
                            const count = +counter.innerText.replace('+', '');
                            const speed = 200; // Lower is faster
                            const increment = Math.ceil(goal / speed);

                            if (count < goal) {
                                counter.innerText = Math.min(count + increment, goal);
                                setTimeout(animate, 10);
                            } else {
                                counter.innerText = goal + (counter.dataset.goal.includes('+') ? '+' : '');
                            }
                        };
                        animate();
                    });
                    observer.unobserve(entry.target);
                }
            });
        }, { threshold: 0.5 });
        observer.observe(statsSection);
    }
});

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

const trackingPinBtn = document.getElementById('trackingPin');
const popupOverlay = document.getElementById('popupOverlay');
const closePopupBtn = document.getElementById('closePopup');
const submitPinBtn = document.getElementById('submitPin');
const statusMsg = document.getElementById('statusMsg');
const pinInput = document.getElementById('pinInput');

// Show popup
trackingPinBtn.addEventListener('click', (e) => {
    e.preventDefault();
    popupOverlay.style.display = 'flex';
    statusMsg.textContent = '';
    statusMsg.className = 'status';
    pinInput.value = '';
});

// Close popup
closePopupBtn.addEventListener('click', () => {
    popupOverlay.style.display = 'none';
});

// Submit pin
submitPinBtn.addEventListener('click', async () => {
    const pin = pinInput.value.trim();
    if (!pin) {
        statusMsg.textContent = 'Please enter a pin.';
        statusMsg.className = 'status rejected';
        return;
    }

    try {
        // Example request to backend
        const response = await fetch('/check-tracking-pin', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ pin })
        });

        if (!response.ok) {
            throw new Error('Network error');
        }

        const result = await response.json();
        const status = result.status;
        const remark = result.reason;

        statusMsg.textContent = status.charAt(0).toUpperCase() + status.slice(1) +
            (remark ? `: ${remark}` : "");
        statusMsg.className = 'status ' + status;
    } catch (err) {
        statusMsg.textContent = 'Error checking pin.';
        statusMsg.className = 'status rejected';
    }
});

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
