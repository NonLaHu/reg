function loadFile(event, outputId) {
    var reader = new FileReader();
    reader.onload = function () {
        document.getElementById(outputId).src = reader.result;
    };
    reader.readAsDataURL(event.target.files[0]);
}

const steps = document.querySelectorAll(".step");
const stepsContainer = document.getElementById("steps");
const sections = document.querySelectorAll(".step-section");
const nextBtns = document.querySelectorAll(".next");
const prevBtn = document.getElementById("prev");


let currentStep = 1;
let prevStep = currentStep;

function updateSteps() {
    if (prevStep == 2 & currentStep == 3) {
        if (!checkStepII()) {
            currentStep = prevStep;
            showToast("Please fill all the *required fields");
            return;
        }
    } else if (prevStep == 3 & currentStep == 4) {
        if (!checkStepIII()) {
            currentStep = prevStep;
            return;
        }
    } else if (prevStep == 4 & currentStep == 5) {
        if (!checkStepIV()) {
            currentStep = prevStep;
            return;
        }
    } else if (prevStep == 5 & currentStep == 6) {
        if (!checkStepV()) {
            currentStep = prevStep;
            return;
        }
    } else if (prevStep == 6 & currentStep == 7) {
        if (!checkStepVI()) {
            currentStep = prevStep;
            return;
        }
    } else if (prevStep == 7 & currentStep == 8) {
        if (!checkStepVII()) {
            currentStep = prevStep;
            return;
        }
    }



    steps.forEach((step, index) => {
        step.classList.toggle("active", index < currentStep);
    });

    sections.forEach((sec, index) => {
        sec.classList.toggle("active", index === currentStep - 1);
    });

    // progress line percentage
    const progressPercent = (currentStep - 1) / (steps.length - 1) * 100;
    stepsContainer.style.setProperty("--progress", progressPercent + "%");

    // disable buttons at edges
    prevBtn.disabled = currentStep === 1;
    nextBtns.forEach((nextBtn)=>{
        nextBtn.disabled = currentStep === steps.length;
    });
    prevStep = currentStep;
}

// Initial progress line
stepsContainer.style.setProperty("--progress", "0%");

nextBtns.forEach((nextBtn)=>{
    nextBtn.addEventListener("click", (e) => {
        e.preventDefault();
        if (currentStep < steps.length) {
            currentStep++;
            updateSteps();
        }
    });
});

prevBtn.addEventListener("click", () => {
    if (currentStep > 1) {
        currentStep--;
        updateSteps();
    }
});

updateSteps();

//nrc
const self_radios = document.querySelectorAll('input[name="self-nrc-available"]');
const sectionYes = document.querySelector(".self-nrc-yes");
const sectionNo = document.querySelector(".self-nrc-no");
const mom_radios = document.querySelectorAll('input[name="mom-nrc-available"]');
const mom_sectionYes = document.querySelector(".mom-nrc-yes");
const mom_sectionNo = document.querySelector(".mom-nrc-no");
const dad_radios = document.querySelectorAll('input[name="dad-nrc-available"]');
const dad_sectionYes = document.querySelector(".dad-nrc-yes");
const dad_sectionNo = document.querySelector(".dad-nrc-no");

const radios = [
    ...self_radios,
    ...mom_radios,
    ...dad_radios
];

// Function to toggle
function toggleSections() {
    if (document.getElementById("self-nrc-available").checked) {
        sectionYes.classList.remove("hidden");
        sectionNo.classList.add("hidden");
    } else {
        sectionYes.classList.add("hidden");
        sectionNo.classList.remove("hidden");
    }

    if (document.getElementById("mom-nrc-available").checked) {
        mom_sectionYes.classList.remove("hidden");
        mom_sectionNo.classList.add("hidden");
    } else {
        mom_sectionYes.classList.add("hidden");
        mom_sectionNo.classList.remove("hidden");
    }

    if (document.getElementById("dad-nrc-available").checked) {
        dad_sectionYes.classList.remove("hidden");
        dad_sectionNo.classList.add("hidden");
    } else {
        dad_sectionYes.classList.add("hidden");
        dad_sectionNo.classList.remove("hidden");
    }
}


radios.forEach(radio => {
    radio.addEventListener("change", toggleSections);
});

toggleSections();

//commit
const commitRadios = document.querySelectorAll('input[name="commitment-yes-no"]');
const guardianRadios = document.querySelectorAll('input[name="whichGuardianCommit"]');

const momCommit = document.querySelector(".mom-commit");
const dadCommit = document.querySelector(".dad-commit");
const guardianCommit = document.querySelector(".guardian-commit");

function updateCommitBlocks() {
    const commitValue = document.querySelector('input[name="commitment-yes-no"]:checked')?.value;
    const guardianValue = document.querySelector('input[name="whichGuardianCommit"]:checked')?.value;

    // hide all blocks by default
    momCommit.classList.add("hidden");
    dadCommit.classList.add("hidden");
    guardianCommit.classList.add("hidden");

    // only show if commitment is accepted
    if (commitValue === "yes" && guardianValue) {
        if (guardianValue === "mom") {
            momCommit.classList.remove("hidden");
        } else if (guardianValue === "dad") {
            dadCommit.classList.remove("hidden");
        } else if (guardianValue === "guardian") {
            guardianCommit.classList.remove("hidden");
        }
    }
}

const selfCommitRadios = document.querySelectorAll('input[name="self-commitment-yes-no"]');
const selfCommitBlock = document.querySelector(".self-commitment");

function updateSelfCommit() {
    const value = document.querySelector('input[name="self-commitment-yes-no"]:checked')?.value;

    if (value === "yes") {
        selfCommitBlock.classList.remove("hidden");
    } else {
        selfCommitBlock.classList.add("hidden");
    }
}


updateSelfCommit();
updateCommitBlocks();
selfCommitRadios.forEach(r => r.addEventListener("change", updateSelfCommit));
commitRadios.forEach(r => r.addEventListener("change", updateCommitBlocks));
guardianRadios.forEach(r => r.addEventListener("change", updateCommitBlocks));

//check Step 2
function checkStepII() {
    const stepSections = document.querySelectorAll('.step-section');
    if (stepSections.length < 2) return false;

    const stepsToCheck = [stepSections[0], stepSections[1]];

    for (let step of stepsToCheck) {
        const requiredFields = step.querySelectorAll('input[required], textarea[required], select[required]');

        for (let field of requiredFields) {
            if (field.type === 'file') {
                if (!field.files || field.files.length === 0) return false;
            } else if (field.type === 'radio') {
                const checkedRadio = step.querySelector(`input[name="${field.name}"]:checked`);
                if (!checkedRadio) return false;
            } else {
                if (!field.value.trim()) return false;
            }
        }
    }

    fillCommitmentFields();
    return true;
}
function checkStepIII() {
    const stepSections = document.querySelectorAll('.step-section');
    const stepsToCheck = stepSections[2];
    const whoCommitInput = stepsToCheck.querySelector('input[name=whichGuardianCommit]:checked');
    if (!whoCommitInput) {
        showToast('Please select who will commit (Mom, Dad, or Guardian).');
        return false;
    }
    const whoCommit = whoCommitInput.value;
    if (whoCommit === 'guardian') {
        // Collect all guardian fields
        const name = stepsToCheck.querySelector('#guardian-name').value.trim();
        const address = stepsToCheck.querySelector('#guardian-full-address').value.trim();
        const job = stepsToCheck.querySelector('#guardian-job').value.trim();
        const phone = stepsToCheck.querySelector('#guardian-phonenumber').value.trim();
        const signature = stepsToCheck.querySelector('#guardianSignature').getAttribute('src');

        // Check if any required field is empty
        if (!name || !address || !job || !phone || !signature) {
            showToast('Please fill in all guardian information and upload a signature.');
            return false;
        }

        // Optional: Validate phone number format
        const phoneRegex = /^[0-9]{6,15}$/;
        if (!phoneRegex.test(phone)) {
            showToast('Please enter a valid phone number.');
            return false;
        }
        fillGuardianPreview()
    }

    const self_commitment = stepsToCheck.querySelector('input[name=self-commitment-yes-no]:checked');
    const guardian_commitment = stepsToCheck.querySelector('input[name=commitment-yes-no]:checked');
    if (!self_commitment || !guardian_commitment) {
        showToast('Please Agree to our terms & condition');
        return false;
    } else {
        if (self_commitment.value == 'no' || guardian_commitment.value == 'no') {
            showToast('Please Agree to our terms & condition');
            return false;
        }
    }
    return true;
}

function checkStepIV() {
    const stepSections = document.querySelectorAll('.step-section');
    const stepsToCheck = stepSections[3];

    // List of required groups
    const requiredGroups = ['qist', 'qce', 'qece', 'qpre', 'qame'];

    for (const group of requiredGroups) {
        const selected = stepsToCheck.querySelector(`input[name="${group}"]:checked`);
        if (!selected) {
            showToast(`Please provide a preference for ${group.slice(1).toUpperCase()}.`);

            return false;
        }
    }

    return true;
}

function checkStepV() {
    const stepSections = document.querySelectorAll('.step-section');
    const stepsToCheck = stepSections[4];

    const requiredGroups = [
        'R75-self-commitment-yes-no',
        '75-guardianParent-commitment-yes-no'
    ];

    for (const group of requiredGroups) {
        const selected = stepsToCheck.querySelector(`input[name="${group}"]:checked`);

        if (!selected) {
            showToast(`Please agree to our term & conditions`);
            return false;
        }

        if (selected.value === 'no') {
            showToast(`Please agree to our term & conditions`);
            return false;
        }
    }

    const guardianParent = stepsToCheck.querySelector('input[name="whichGuardianCommit"]:checked');
    if (!guardianParent) {
        showToast('Please select who will commit (Parent or Guardian).');
        return false;
    }

    return true;
}


function checkStepVI() {
    const stepSections = document.querySelectorAll('.step-section');
    const stepsToCheck = stepSections[5];

    const requiredGroups = [
        'R-self-commitment-yes-no',
        'R-guardianParent-commitment-yes-no'
    ];

    for (const group of requiredGroups) {
        const selected = stepsToCheck.querySelector(`input[name="${group}"]:checked`);

        if (!selected) {
            showToast(`Please agree to our term & conditions`);
            return false;
        }

        if (selected.value === 'no') {
            showToast(`Please agree to our term & conditions`);
            return false;
        }
    }

    const guardianParent = stepsToCheck.querySelector('input[name="RwhichGuardianCommit"]:checked');
    if (!guardianParent) {
        showToast('Please select who will commit (Parent or Guardian).');
        return false;
    }

    return true;
}

function checkStepVII() {
    const dormRadiosValue = document.querySelector('input[name="dorm-yn"]:checked').value;

    if (dormRadiosValue == 'no') {
        return true;
    }

    console.log(dormRadiosValue);


    const stepSections = document.querySelectorAll('.step-section');
    const stepsToCheck = stepSections[6];

    const requiredGroups = [
        'R-self-dorm-commitment-yes-no',
        'R-dormGuardianParent-commitment-yes-no'
    ];

    for (const group of requiredGroups) {
        const selected = stepsToCheck.querySelector(`input[name="${group}"]:checked`);

        if (!selected) {
            showToast(`Please agree to our term & conditions`);
            return false;
        }

        if (selected.value === 'no') {
            showToast(`Please agree to our term & conditions`);
            return false;
        }
    }

    const guardianParent = stepsToCheck.querySelector('input[name="RwhichDormGuardianCommit"]:checked');
    if (!guardianParent) {
        showToast('Please select who will commit (Parent or Guardian).');
        return false;
    }

    const requiredFields = stepsToCheck.querySelectorAll('fieldset input[required]');

    for (let field of requiredFields) {
        if (!field.value.trim()) {
            showToast("Fill the required fields");
            return false;
        }
    }

    return true;
}


//fill automatically
function fillCommitmentFields() {

    const selfNameMM = document.getElementById('self-name-mm').value;
    const selfEthnic = document.getElementById('self-ethnic').value;
    const selfReligion = document.getElementById('self-religion').value;
    const selfNrc = document.getElementById('self-nrc').value || ""; // Optional if serial number
    const selfAddress = document.getElementById('self-full-address').value;
    const selfPhone = document.getElementById('self-phonenumber')?.value || "";
    const selfGender = document.getElementById('gender').value;

    let trimmedName = selfNameMM.trim();

    if (selfGender === 'male') {
        document.querySelectorAll('.phFullNameSir').forEach(e => e.textContent = `မောင်${trimmedName}`);
        document.querySelectorAll('.phFullNameSirFormal').forEach(e => e.textContent = `ကျွန်တော် မောင်${trimmedName}`);
    } else {
        document.querySelectorAll('.phFullNameSir').forEach(e => e.textContent = `မ${trimmedName}`);
        document.querySelectorAll('.phFullNameSirFormal').forEach(e => e.textContent = `ကျွန်မ မ${trimmedName}`);
    }

    document.querySelectorAll('.phFullName').forEach(el => el.textContent = selfNameMM);
    document.querySelectorAll('.phSelfEthnic').forEach(el => el.textContent = selfEthnic);
    document.querySelectorAll('.phSelfReligion').forEach(el => el.textContent = selfReligion);
    document.querySelectorAll('.phSelfNrc').forEach(el => el.textContent = selfNrc);
    document.querySelectorAll('.phSelfUsualAddress').forEach(el => el.textContent = selfAddress);
    document.querySelectorAll('.phSelfPhonenumber').forEach(el => el.textContent = selfPhone);

    const momName = document.getElementById('mom-name').value;
    const momJob = document.getElementById('mom-job').value;
    const momAddress = document.getElementById('mom-full-address').value;
    const momPhone = document.getElementById('mom-phonenumber').value;

    const dadName = document.getElementById('dad-name-mm').value;
    const dadJob = document.getElementById('dad-job').value;
    const dadAddress = document.getElementById('dad-full-address').value;
    const dadPhone = document.getElementById('dad-phonenumber').value;

    document.querySelectorAll('.phMomName').forEach(el => el.textContent = momName);
    document.querySelectorAll('.phMomJob').forEach(el => el.textContent = momJob);
    document.querySelectorAll('.phMomUsualAddress').forEach(el => el.textContent = momAddress);
    document.querySelectorAll('.phMomPhonenumber').forEach(el => el.textContent = momPhone);

    document.querySelectorAll('.phDadName').forEach(el => el.textContent = dadName);
    document.querySelectorAll('.phDadJob').forEach(el => el.textContent = dadJob);
    document.querySelectorAll('.phDadUsualAddress').forEach(el => el.textContent = dadAddress);
    document.querySelectorAll('.phDadPhonenumber').forEach(el => el.textContent = dadPhone);

    const selfSign = document.getElementById('self-sign-img').src;
    const momSign = document.getElementById('mom-sign-img').src;
    const dadSign = document.getElementById('dad-sign-img').src;
    const guardianSign = document.getElementById('parentSignature')?.src || "";

    document.querySelectorAll('.phSelfSignature').forEach(el => el.src = selfSign);
    document.querySelectorAll('.phMomSignature').forEach(el => el.src = momSign);
    document.querySelectorAll('.phDadSignature').forEach(el => el.src = dadSign);
    document.querySelectorAll('.phGuardianSignature').forEach(el => el.src = guardianSign);

    return true;
}

function fillGuardianPreview() {
    const signatureSrc = document.querySelector('#guardianSignature').getAttribute('src');
    const nameValue = document.querySelector('#guardian-name').value.trim();

    // Fill all signature previews
    if (signatureSrc) {
        const previewSigs = document.querySelectorAll('.phGuardianSignature');
        previewSigs.forEach(img => img.src = signatureSrc);
    }

    // Fill all name previews
    if (nameValue) {
        const previewNames = document.querySelectorAll('.phGuardianName');
        previewNames.forEach(span => span.textContent = nameValue);
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
        background: rgba(233, 88, 88, 0.85);
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

//img reloaded
const fileInputs = document.querySelectorAll('input[type="file"]');

fileInputs.forEach(input => {
    const img = input.previousElementSibling;
    if (img && img.tagName === "IMG" && img.src) {
    }

    if (input.files && input.files.length > 0) {
        loadFile({ target: input }, img.id);
    }
});

//75 percent
const guardianParentRadios = document.querySelectorAll('input[name="whichGuardianCommit"]');
const commitment75Radios = document.querySelectorAll('input[name="75-guardianParent-commitment-yes-no"]');

const parent75Commit = document.querySelector(".R75-parent-commitment");
const guardian75Commit = document.querySelector(".R75-guardian-commitment");

function update75CommitBlocks() {
    const commitmentValue = document.querySelector('input[name="75-guardianParent-commitment-yes-no"]:checked')?.value;
    const guardianValue = document.querySelector('input[name="whichGuardianCommit"]:checked')?.value;

    // hide all by default
    parent75Commit.classList.add("hidden");
    guardian75Commit.classList.add("hidden");

    if (commitmentValue === "yes" && guardianValue) {
        if (guardianValue === "parent") {
            parent75Commit.classList.remove("hidden");
        } else if (guardianValue === "guardian") {
            guardian75Commit.classList.remove("hidden");
        }
    }
}

const selfRadios = document.querySelectorAll('input[name="R75-self-commitment-yes-no"]');
const selfSection = document.querySelector('.R75-self-commitment');

function toggleSelfCommitment() {
    const selected = document.querySelector('input[name="R75-self-commitment-yes-no"]:checked').value;
    if (selected === 'yes') {
        selfSection.classList.remove('hidden');
    } else {
        selfSection.classList.add('hidden');
    }
}

toggleSelfCommitment();

selfRadios.forEach(radio => {
    radio.addEventListener('change', toggleSelfCommitment);
});


guardianParentRadios.forEach(r => r.addEventListener("change", update75CommitBlocks));
commitment75Radios.forEach(r => r.addEventListener("change", update75CommitBlocks));

update75CommitBlocks();

// Radio groups
const guardianParentRadiosR = document.querySelectorAll('input[name="RwhichGuardianCommit"]');
const commitmentRadiosR = document.querySelectorAll('input[name="R-guardianParent-commitment-yes-no"]');

// Sections to toggle
const parentCommitR = document.querySelector(".R-parent-commitment");
const guardianCommitR = document.querySelector(".R-guardian-commitment");

function updateRCommitBlocks() {
    const commitmentValue = document.querySelector('input[name="R-guardianParent-commitment-yes-no"]:checked')?.value;
    const guardianValue = document.querySelector('input[name="RwhichGuardianCommit"]:checked')?.value;

    // hide all by default
    parentCommitR.classList.add("hidden");
    guardianCommitR.classList.add("hidden");

    if (commitmentValue === "yes" && guardianValue) {
        if (guardianValue === "parent") {
            parentCommitR.classList.remove("hidden");
        } else if (guardianValue === "guardian") {
            guardianCommitR.classList.remove("hidden");
        }
    }
}

// Self commitment
const selfRadiosR = document.querySelectorAll('input[name="R-self-commitment-yes-no"]');
const selfSectionR = document.querySelector('.R-self-commitment');

function toggleSelfCommitmentR() {
    const selected = document.querySelector('input[name="R-self-commitment-yes-no"]:checked')?.value;
    if (selected === 'yes') {
        selfSectionR.classList.remove('hidden');
    } else {
        selfSectionR.classList.add('hidden');
    }
}

// Initial call
toggleSelfCommitmentR();
updateRCommitBlocks();

// Event listeners
selfRadiosR.forEach(radio => {
    radio.addEventListener('change', toggleSelfCommitmentR);
});

guardianParentRadiosR.forEach(r => r.addEventListener("change", updateRCommitBlocks));
commitmentRadiosR.forEach(r => r.addEventListener("change", updateRCommitBlocks));

// --- DOM references ---
const selfDormRadios = document.querySelectorAll('input[name="R-self-dorm-commitment-yes-no"]');
const selfDormSection = document.querySelector('.R-self-dorm-commitment');

const dormGuardianRadios = document.querySelectorAll('input[name="R-dormGuardianParent-commitment-yes-no"]');
const dormParentCommit = document.querySelector('.R-dorm-parent-commitment');
const dormGuardianCommit = document.querySelector('.R-dorm-guardian-commitment');

const whichDormGuardianRadios = document.querySelectorAll('input[name="RwhichDormGuardianCommit"]');

function toggleHidden(element, show) {
    if (!element) return;
    if (show) {
        element.classList.remove('hidden');
    } else {
        element.classList.add('hidden');
    }
}

// --- Toggle self dorm commitment ---
selfDormRadios.forEach(radio => {
    radio.addEventListener('change', () => {
        toggleHidden(selfDormSection, radio.value === 'yes');
    });
});

// --- Toggle dorm guardian/parent commitment ---
function updateDormCommitSections() {
    const selectedYes = Array.from(dormGuardianRadios).find(r => r.checked)?.value === 'yes';
    const selectedGuardian = Array.from(whichDormGuardianRadios).find(r => r.checked)?.value;

    toggleHidden(dormParentCommit, selectedYes && selectedGuardian === 'parent');
    toggleHidden(dormGuardianCommit, selectedYes && selectedGuardian === 'guardian');
}

// --- Event listeners ---
dormGuardianRadios.forEach(radio => radio.addEventListener('change', updateDormCommitSections));
whichDormGuardianRadios.forEach(radio => radio.addEventListener('change', updateDormCommitSections));

updateDormCommitSections();
toggleHidden(selfDormSection, document.querySelector('input[name="R-self-dorm-commitment-yes-no"]:checked')?.value === 'yes');
const dormRadios = document.querySelectorAll('input[name="dorm-yn"]');
const toggleDormDiv = document.querySelector('.toggle-dorm');

function updateDormVisibility() {
    const selected = document.querySelector('input[name="dorm-yn"]:checked').value;
    if (selected === 'yes') {
        toggleDormDiv.classList.remove('hidden');
    } else {
        toggleDormDiv.classList.add('hidden');
    }
}

dormRadios.forEach(radio => {
    radio.addEventListener('change', updateDormVisibility);
});

updateDormVisibility();

// Academic
const academicRadios = document.querySelectorAll('input[name="yes"]');
const toggleAcademicSection = document.querySelector('.toggleAcademic');

function toggleAcademic() {
    const selectedValue = document.querySelector('input[name="yes"]:checked')?.value;
    if (selectedValue === 'yes') {
        toggleAcademicSection.classList.remove('hidden');
    } else {
        toggleAcademicSection.classList.add('hidden');
    }
}

toggleAcademic();

academicRadios.forEach(radio => {
    radio.addEventListener('change', toggleAcademic);
});
