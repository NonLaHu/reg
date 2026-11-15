const resetLink = document.getElementById("password-reset-link");
const modalOverlay = document.getElementById("modal-overlay");
const modal = document.getElementById("password-modal");
const closeModal = document.getElementById("close-modal");
const form = document.getElementById("reset-form");

const oldPass = document.getElementById("old-pass");
const newPass = document.getElementById("new-pass");
const confirmPass = document.getElementById("confirm-pass");
const newError = document.getElementById("new-error");
const confirmError = document.getElementById("confirm-error");
const submitBtn = document.getElementById("submit-btn");

// Open modal
resetLink.addEventListener("click", () => {
    modalOverlay.style.display = "flex";
    setTimeout(() => modal.classList.add("show"), 10);
});

// Close modal
function hideModal() {
    modal.classList.remove("show");
    setTimeout(() => modalOverlay.style.display = "none", 200);
    form.reset();
    newError.style.display = "none";
    confirmError.style.display = "none";
    submitBtn.disabled = true;
}
closeModal.addEventListener("click", hideModal);
modalOverlay.addEventListener("click", e => {
    if (e.target === modalOverlay) hideModal();
});

// Toggle password visibility
document.querySelectorAll(".toggle-pass").forEach(icon => {
    icon.addEventListener("click", () => {
        const target = document.getElementById(icon.dataset.target);
        if (target.type === "password") {
            target.type = "text";
            icon.classList.remove("fa-eye");
            icon.classList.add("fa-eye-slash");
        } else {
            target.type = "password";
            icon.classList.remove("fa-eye-slash");
            icon.classList.add("fa-eye");
        }
    });
});

// Real-time validation
function validateForm() {
    let valid = true;

    // Check new password length
    if (newPass.value.length < 5) {
        newError.style.display = "block";
        valid = false;
    } else {
        newError.style.display = "none";
    }

    // Check confirm password match
    if (confirmPass.value !== newPass.value || confirmPass.value === "") {
        confirmError.style.display = "block";
        valid = false;
    } else {
        confirmError.style.display = "none";
    }

    submitBtn.disabled = !valid;
}

newPass.addEventListener("input", validateForm);
confirmPass.addEventListener("input", validateForm);

// Form submit
form.addEventListener("submit", e => {
    e.preventDefault();
    resetPassword(oldPass.value, newPass.value)
    hideModal();
});

async function resetPassword(oldPassword, newPassword) {
    const token = localStorage.getItem('jwt');

    const res = await fetch('/api/reset-password', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + token
        },
        body: JSON.stringify({ oldPassword, newPassword })
    });

    if (res.ok) {
        showToast('The password was updated Successfully');
    } else {
        const err = await res.text();
        showToast('The password update encountered a problem');

    }
}


//js Toast
function showToast(message, duration = 3000) {
    const container = document.getElementById('toast-container');
    if (!container) {
        console.error("Toast container not found in HTML!");
        return;
    }

    // Create toast element
    const toast = document.createElement('div');
    toast.textContent = message;
    toast.style.cssText = `
        background: rgba(33, 43, 104, 0.85);
        color: white;
        padding: 12px 20px;
        border-radius: 6px;
        min-width: 200px;
        max-width: 350px;
        box-shadow: 0 4px 12px rgba(0,0,0,0.2);
        cursor: pointer;
        opacity: 0;
        transform: translateY(-20px);
        transition: all 0.3s ease;
    `;

    // Add click to close
    toast.addEventListener('click', () => {
        container.removeChild(toast);
    });

    container.appendChild(toast);

    // Animate in
    requestAnimationFrame(() => {
        toast.style.opacity = 1;
        toast.style.transform = 'translateY(0)';
    });

    // Auto-remove after duration
    setTimeout(() => {
        toast.style.opacity = 0;
        toast.style.transform = 'translateY(-20px)';
        toast.addEventListener('transitionend', () => {
            if (toast.parentElement) container.removeChild(toast);
        });
    }, duration);
}

//remark
const remarkPopup = document.getElementById('remarkPopup');
const remarkContent = document.getElementById('remarkContent');
const closeBtn = document.getElementById('closeRemark');

// Open popup with remark
document.querySelectorAll('.remark-btn').forEach(btn => {
    btn.addEventListener('click', () => {
        const remark = btn.getAttribute('data-remark');
        remarkContent.textContent = remark;
        remarkPopup.style.display = 'flex';
    });
});

// Close popup
closeBtn.addEventListener('click', () => {
    remarkPopup.style.display = 'none';
});

// Close popup on outside click
remarkPopup.addEventListener('click', (e) => {
    if (e.target === remarkPopup) {
        remarkPopup.style.display = 'none';
    }
});