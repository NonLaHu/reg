const steps = document.querySelectorAll(".step");
const stepsContainer = document.getElementById("steps");
const sections = document.querySelectorAll(".step-section");
const nextBtns = document.querySelectorAll(".next");
const prevBtn = document.getElementById("prev");

let DormYesNo = false;
let GuardianYesNo = false;

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
    nextBtns.forEach((nextBtn) => {
        nextBtn.disabled = currentStep === steps.length;
    });
    prevStep = currentStep;
}

// Initial progress line
stepsContainer.style.setProperty("--progress", "0%");

nextBtns.forEach((nextBtn) => {
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
            setRadiosEnabled('guardRadio', false);
            GuardianYesNo = false;
        } else if (guardianValue === "dad") {
            dadCommit.classList.remove("hidden");
            setRadiosEnabled('guardRadio', false);
            GuardianYesNo = false;
        } else if (guardianValue === "guardian") {
            guardianCommit.classList.remove("hidden");
            setRadiosEnabled('guardRadio', true);
            GuardianYesNo = true;
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

//nrc check helper
function validateNrcSection(radioId, inputId, fileFrontId, fileBackId, signId) {
    const radio = document.getElementById(radioId);
    const input = document.getElementById(inputId);
    const fileFront = document.getElementById(fileFrontId);
    const fileBack = document.getElementById(fileBackId);
    const fileSign = document.getElementById(signId);


    if (radio.checked) {
        if (!fileFront.files || fileFront.files.length === 0) return false;
        if (!fileBack.files || fileBack.files.length === 0) return false;
        if (input) {
            if (!input.value.trim()) return false;
        }
    }
    if (signId == "mom-sign" && !momDeceaseEnabled || signId == "dad-sign" && !dadDeceaseEnabled || signId == "self-sign") {
        if (!fileSign.files || fileSign.files.length === 0) return false;
    }
    return true;
}

//check Step 2
function checkStepII() {
    const stepSections = document.querySelectorAll('.step-section');
    if (stepSections.length < 2) return false;

    const stepsToCheck = [stepSections[0], stepSections[1]];

    for (let step of stepsToCheck) {
        const requiredFields = step.querySelectorAll('input[required], textarea[required], select[required]');

        for (let field of requiredFields) {
            if (field.type === 'file') {
                if (!field.files || field.files.length === 0) { return false; }
            } else if (field.type === 'radio') {
                const checkedRadio = step.querySelector(`input[name="${field.name}"]:checked`);
                if (!checkedRadio) return false;
            } else {
                if (!field.value.trim()) return false;
            }
        }
    }

    const validSelf = validateNrcSection(
        'self-nrc-available', 'self-nrc', 'self-nrc-front', 'self-nrc-back', 'self-sign'
    );
    const validMom = validateNrcSection(
        'mom-nrc-available', 'mom-nrc', 'mom-nrc-front', 'mom-nrc-back', 'mom-sign'
    );
    const validDad = validateNrcSection(
        'dad-nrc-available', 'dad-nrc', 'dad-nrc-front', 'dad-nrc-back', 'dad-sign'
    );

    if (!validSelf || !validMom || !validDad) {
        console.log("Some NRC sections are incomplete!");
        return false;
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

    const guardianParent = stepsToCheck.querySelector('input[name="whichGuardianCommit1"]:checked');
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
        DormYesNo = false;
        return true;s
    }else{
        DormYesNo = true;
    }

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

    const academicSupport = stepsToCheck.querySelector('input[name="academicSupport"]:checked');
    const academicTextarea = stepsToCheck.querySelector('#academicPrize');

    if (academicSupport.value == "yes") {
        if (!academicTextarea.value.trim()) {
            showToast("Fill the academic field if you had it");
            return false;
        }
    }

    return true;
}

function checkStepVIII() {
    const payslipImg = document.getElementById('payslip');

    if (!payslipImg.files || payslipImg.files.length === 0) {
        showToast("playslip is requried")
        return false;
    }
    return true;
}

//fill automatically
function fillCommitmentFields() {

    const selfNameMM = document.getElementById('self-name-mm').value;
    const selfEthnic = document.getElementById('self-ethnic').value;
    const selfReligion = document.getElementById('self-religion').value;
    const selfAddress = document.getElementById('self-full-address').value;
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
    document.querySelectorAll('.phSelfUsualAddress').forEach(el => el.textContent = selfAddress);

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
function showToast(message, duration = 3000, type = 'error') {
    const container = document.getElementById('toast-container');
    if (!container) {
        console.error("Toast container not found in HTML!");
        return;
    }

    // Create toast element
    const toast = document.createElement('div');
    toast.textContent = message;
    if (type == 'error') {

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
    } else {
        toast.style.cssText = `
        background: rgba(21, 197, 74, 0.85);
        color: white;
        padding: 12px 20px;
        border-radius: 6px;
        min-width: 200px;
        max-width: 350px;
        box-shadow: 0 4px 12px rgba(0,0,0,0.2);
        cursor: pointer;
        opacity: 0;
        transform: translateY(-20px);
        transition: all 0.3s ease;`
    }

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
const guardianParentRadios = document.querySelectorAll('input[name="whichGuardianCommit1"]');
const commitment75Radios = document.querySelectorAll('input[name="75-guardianParent-commitment-yes-no"]');

const parent75Commit = document.querySelector(".R75-parent-commitment");
const guardian75Commit = document.querySelector(".R75-guardian-commitment");

function update75CommitBlocks() {
    const commitmentValue = document.querySelector('input[name="75-guardianParent-commitment-yes-no"]:checked')?.value;
    const guardianValue = document.querySelector('input[name="whichGuardianCommit1"]:checked')?.value;

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
        DormYesNo = true;
        payment_fee_update();
    } else {
        toggleDormDiv.classList.add('hidden');
        DormYesNo = false;
        payment_fee_update();
    }
}

dormRadios.forEach(radio => {
    radio.addEventListener('change', updateDormVisibility);
});

updateDormVisibility();

// Academic
const academicRadios = document.querySelectorAll('input[name="academicSupport"]');
const toggleAcademicSection = document.querySelector('.toggleAcademic');

function toggleAcademic() {
    const selectedValue = document.querySelector('input[name="academicSupport"]:checked')?.value;
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


//decease helper functions
function showDisable(elementId) {
    const el = document.getElementById(elementId);
    if (!el) return;

    const wrapper = el.closest('.id-photo');
    if (wrapper) wrapper.style.display = 'none';
}
function showEnable(elementId) {
    const el = document.getElementById(elementId);
    if (!el) return;

    const wrapper = el.closest('.id-photo');
    if (wrapper) wrapper.style.display = 'flex';
}
//other logics
const momDeceaseInput = document.getElementById('mom-decease-date');
const dadDeceaseInput = document.getElementById('dad-decease-date');
let momDeceaseEnabled = false;
let dadDeceaseEnabled = false;

function setRadiosEnabled(className, enabled) {
    document.querySelectorAll(`.${className}`).forEach(radio => {
        radio.disabled = !enabled;
    });
}

function updateMomSignDisplay() {
    const value = momDeceaseInput.value.trim();

    if (value === '' && momDeceaseEnabled) {
        showEnable('mom-sign-img');
        momDeceaseEnabled = false;
        setRadiosEnabled('momRadio', true);
    } else if (value !== '') {
        showDisable('mom-sign-img');
        momDeceaseEnabled = true;
        setRadiosEnabled('momRadio', false);
    }

    // Disable parentRadio if both mom and dad are deceased
    const dadDead = dadDeceaseInput.value.trim() !== '';
    setRadiosEnabled('parentRadio', !(value !== '' && dadDead));
}

function updateDadSignDisplay() {
    const value = dadDeceaseInput.value.trim();

    if (value === '' && dadDeceaseEnabled) {
        showEnable('dad-sign-img');
        dadDeceaseEnabled = false;
        setRadiosEnabled('dadRadio', true);
    } else if (value !== '') {
        showDisable('dad-sign-img');
        dadDeceaseEnabled = true;
        setRadiosEnabled('dadRadio', false);
    }

    // Disable parentRadio if both mom and dad are deceased
    const momDead = momDeceaseInput.value.trim() !== '';
    setRadiosEnabled('parentRadio', !(value !== '' && momDead));
}

// Attach listeners
momDeceaseInput.addEventListener('input', updateMomSignDisplay);
momDeceaseInput.addEventListener('change', updateMomSignDisplay);
dadDeceaseInput.addEventListener('input', updateDadSignDisplay);
dadDeceaseInput.addEventListener('change', updateDadSignDisplay);

// Initial call to set proper state on page load
updateMomSignDisplay();
updateDadSignDisplay();

//init spans
function initSpans() {
    //debug
    console.log(student)
    //nrc and phonenumber
    const phNrcSelf = document.querySelectorAll('.phSelfNrc');
    const phNoSelf = document.querySelectorAll('.phSelfPhonenumber');

    phNrcSelf.forEach(el => {
        el.textContent = student?.preEnrollment?.nrc ?? "လျှောက်ထားဆဲ";
    });

    phNoSelf.forEach(el => {
        el.textContent = student?.preEnrollment?.phoneNumber ?? "null";
    });

    //academic and name
    const inputEntranceRollNo = document.getElementById('universityEntranceRollNo');
    const inputEntranceYear = document.getElementById('universityEntranceExamYear');
    const inputSelfName = document.getElementById('self-name-mm');

    inputEntranceRollNo.value = student?.preEnrollment?.uniEntranceRollno ?? '';
    inputEntranceYear.value = student?.preEnrollment?.uniEntranceAcademicYear ?? '';
    inputSelfName.value = student?.preEnrollment?.fullNameMm ?? '';

    //GENDER
    const genderSelect = document.getElementById('gender');
    if (genderSelect && student?.preEnrollment?.gender) {
        const genderValue = student.preEnrollment.gender.toLowerCase();
        if (['male', 'female'].includes(genderValue)) {
            genderSelect.value = genderValue;
        }
    }

    //payment
    const phKbzNumber = document.querySelectorAll('.phKbzNumber');
    const phKBZName = document.querySelectorAll('.phKBZName');
    const phAmount = document.querySelectorAll('.phAmount');

    if (detailPayment) {
        phKbzNumber.forEach(el => {
            el.textContent = detailPayment.paymentPhoneNumber ?? "null Fallback";
        });

        phKBZName.forEach(el => {
            el.textContent = detailPayment?.paymentName ?? "null Fallback";
        });

        const totalFee = (detailPayment.enrollmentFee || 0) + (detailPayment.dormitoryFee || 0);

        phAmount.forEach(el => {
            el.textContent = totalFee.toLocaleString();
        });
    }


    //qr
    document.getElementById("paymentQrImg").src = detailPayment.paymentQrPath;

}
initSpans();

function initLogic() {
    const radioNullNrc = document.getElementById('self-nrc-available-no');
    const radioFillNrc = document.getElementById('self-nrc-available');

    if (student.preEnrollment?.nrc) {
        if (radioFillNrc) radioFillNrc.checked = true;
        if (radioNullNrc) {
            const wrapper = radioNullNrc.closest('.radio-button-wrapper');
            if (wrapper) wrapper.style.display = 'none';
        }
    } else {
        if (radioNullNrc) radioNullNrc.checked = true;
        if (radioFillNrc) {
            const wrapper = radioFillNrc.closest('.radio-button-wrapper');
            if (wrapper) wrapper.style.display = 'none';
        }
    }
    toggleSections()
}
initLogic();

//file Validator
const MAX_FILE_SIZE = 5 * 1024 * 1024;

const ALLOWED_TYPES = ['image/jpeg', 'image/png', 'image/webp'];

document.querySelectorAll('input[type="file"]').forEach(input => {
    input.addEventListener('change', (event) => {
        const files = Array.from(event.target.files); // convert FileList to array
        let valid = true;

        files.forEach(file => {
            if (!ALLOWED_TYPES.includes(file.type)) {
                showToast(`Invalid file type: ${file.name}. Only images are allowed.`);
                valid = false;
            } else if (file.size > MAX_FILE_SIZE) {
                showToast(`File too large: ${file.name}. Max size is 5 MB.`);
                valid = false;
            }
        });

        if (!valid) {
            event.target.value = ''; // reset input
            return; // stop further processing
        }

        // If valid, load preview for first file
        if (files.length > 0) {
            const reader = new FileReader();
            reader.onload = function () {
                // find the closest img sibling inside the same parent container
                const previewImg = input.closest('.id-photo')?.querySelector('img');
                if (previewImg) {
                    previewImg.src = reader.result;
                }
            };
            reader.readAsDataURL(files[0]);
        }

    });
});


// Add "Remove" buttons to all .id-photo containers
document.querySelectorAll('.id-photo').forEach(container => {
    const img = container.querySelector('img');
    const input = container.querySelector('input[type="file"]');

    if (!img || !input) return; // skip if missing

    // Create Remove button
    const removeBtn = document.createElement('button');
    removeBtn.type = 'button';
    removeBtn.textContent = 'Remove';
    removeBtn.classList.add('to-remove-btn'); // add class
    removeBtn.style.marginLeft = '10px'; // optional styling

    // Attach click handler
    removeBtn.addEventListener('click', () => {
        input.value = '';       // clear file input
        img.src = '';           // clear preview
    });

    // Append button to container
    container.appendChild(removeBtn);
});

//Function to hide all buttons
function hideAllRemoveButtons() {
    document.querySelectorAll('.to-remove-btn').forEach(btn => {
        btn.style.display = 'none';
    });
}

//Preview Logic
const btnSubmit = document.getElementById('btn-submit-preview');
let previewEnabled = false;

btnSubmit.addEventListener('click', () => {
    if (!previewEnabled) {
        if (checkStepVIII()) {
            changeToSubmitAndBack();
            previewShow();
            hideAllRemoveButtons();
            // disableAllFormControls();
            window.scrollTo({
                top: 0,
                left: 0,
                behavior: 'smooth'
            });
            previewEnabled = true;
        }
    } else {
        handleSubmit();
    }
})

function changeToSubmitAndBack() {
    const btnSubmit = document.getElementById('btn-submit-preview');
    const btnGoBack = document.getElementById('btn-goback');

    btnSubmit.textContent = "Submit"
    btnGoBack.classList.remove("hidden");
}

function disableAllFormControls(form = document.querySelector('form')) {
    if (!form) return;
    form.querySelectorAll('input, select, textarea').forEach(el => {
        el.disabled = true;
    });
}


function previewShow() {
    const stepsVisual = document.getElementById('steps');
    const navContainer = document.getElementById('nav-buttons-container');
    const nextBtns = document.querySelectorAll('.next');

    if (nextBtns) {
        nextBtns.forEach(nextBtn => nextBtn.style.display = 'none');
    }
    if (stepsVisual) stepsVisual.style.display = 'none';
    if (navContainer) navContainer.style.display = 'none';

    const sections = document.querySelectorAll('.step-section');
    sections.forEach(section => section.classList.add('active'));
}

const btnGoBack = document.getElementById('btn-goback');

if (btnGoBack) {
    btnGoBack.addEventListener('click', () => {
        revertPreview();
        btnGoBack.classList.add("hidden");
        enableAllFormControls();
    });
}

function enableAllFormControls(form = document.querySelector('form')) {
    if (!form) return;
    form.querySelectorAll('input, select, textarea, button').forEach(el => {
        el.disabled = false;
    });
}


function revertPreview() {
    // Re-show hidden containers
    const stepsVisual = document.getElementById('steps');
    const navContainer = document.getElementById('nav-buttons-container');
    const nextBtns = document.querySelectorAll('.next');

    if (stepsVisual) stepsVisual.style.display = 'flex';
    if (navContainer) navContainer.style.display = 'flex';
    if (nextBtns) nextBtns.forEach(btn => btn.style.display = 'flex');

    // Remove 'active' class from step sections
    const sections = document.querySelectorAll('.step-section');
    sections.forEach(section => section.classList.remove('active'));
    sections[0].classList.add('active');
    currentStep = 1;
    prevStep = currentStep;
    updateSteps();

    // Show all remove buttons again
    document.querySelectorAll('.to-remove-btn').forEach(btn => btn.style.display = '');

    // Change submit button back to Preview
    const btnSubmit = document.getElementById('btn-submit-preview');
    if (btnSubmit) btnSubmit.textContent = 'Preview';

    // Reset preview flag
    previewEnabled = false;

    // Scroll to top (optional)
    window.scrollTo({ top: 0, left: 0, behavior: 'smooth' });
}


//create scroll bar
function createScrollBar() {
    // Create the progress bar container
    const progressContainer = document.createElement('div');
    progressContainer.id = 'scroll-progress-container';
    document.body.appendChild(progressContainer);

    // Style it (overlay at top, full width)
    Object.assign(progressContainer.style, {
        position: 'fixed',
        top: '0',
        left: '0',
        width: '100%',
        height: '5px',      // thickness of the bar
        backgroundColor: 'rgba(0,0,0,0.1)',
        zIndex: '9999',
    });

    // Create the actual progress bar
    const progressBar = document.createElement('div');
    progressBar.id = 'scroll-progress-bar';
    progressContainer.appendChild(progressBar);

    // Style the inner bar
    Object.assign(progressBar.style, {
        height: '100%',
        width: '0%',           // initial progress
        backgroundColor: '#0d6efd', // green, customize
        transition: 'width 0.1s ease-out',
    });

    // Function to update scroll progress
    function updateScrollProgress() {
        const scrollTop = window.scrollY || document.documentElement.scrollTop;
        const docHeight = document.documentElement.scrollHeight - document.documentElement.clientHeight;
        const scrollPercent = (scrollTop / docHeight) * 100;
        progressBar.style.width = scrollPercent + '%';
    }

    // Listen to scroll
    window.addEventListener('scroll', updateScrollProgress);

    let timeout;
    window.addEventListener('scroll', () => {
        progressContainer.style.opacity = '1';
        clearTimeout(timeout);
        timeout = setTimeout(() => progressContainer.style.opacity = '0', 1000);
    });
}
createScrollBar();


//Form Submission Logic
const API_ENDPOINT = '/api/enrollments';

const MAJOR_ID_MAP = {
    IST: 1,
    CE: 2,
    ECE: 3,
    PRE: 4,
    AME: 5
};

const FILETYPE_BY_INPUT_NAME = {
    'self_picture': 1,
    'nrc_front_self': 2,
    'nrc_back_self': 3,
    'nrc_front_mother': 4,
    'nrc_back_mother': 5,
    'nrc_front_father': 6,
    'nrc_back_father': 7,
    'self_signature': 8,
    'mother_signature': 9,
    'father_signature': 10,
    'community_recommendation': 11,
    'police_recommendation': 12,
    'medical_recommendation': 13,
    'pass_certification': 14,
    'marks_certification': 15,
    'house_hold_registration': 16,
    'payment_slip': 17,
    'guardian_signature': 18
};


async function handleSubmit() {
    try {
        const form = document.querySelector("form");
        // the DTO
        const dto = buildDtoFromForm(form);

        // collect file inputs into [{ fileTypeId, image }]
        const fileInputs = form.querySelectorAll('input[type="file"]');
        const fileEntries = await Promise.all(
            Array.from(fileInputs).map(async input => {
                if (!input.files || input.files.length === 0) return null;

                const file = input.files[0];
                const base64 = await toBase64(file);
                return {
                    fileTypeId: FILETYPE_BY_INPUT_NAME[input.name],
                    image: base64            // base64 string
                };
            })
        );

        const filesPayload = fileEntries.filter(Boolean);

        const payload = {
            dto,
            files: filesPayload
        };

        const token = getAuthToken();
        const headers = {
            'Content-Type': 'application/json',
            ...(token ? { 'Authorization': token.startsWith('Bearer') ? token : `Bearer ${token}` } : {})
        };

        const res = await fetch(API_ENDPOINT, {
            method: 'POST',
            headers,
            body: JSON.stringify(payload)
        });

        const resJson = await res.json().catch(() => null);
        if (!res.ok) {
            showToast(resJson?.message || `Server error (${res.status})`, true);
            return;
        }

        showToast(resJson?.message || 'Enrollment accepted', 10000, 'gg');
        form.reset();
    } catch (err) {
        console.error(err);
        showToast(err.message || 'Unexpected error', true);
    }
}

// ---------- FORM -> DTO MAPPING ----------
function buildDtoFromForm(formEl) {
    const regionSelect = document.getElementById('self-birth-place-region');
    const placeInput = document.getElementById('self-birth-place');
    const get = name => {
        const el = formEl.querySelector(`[name="${name}"]`);
        return el ? el.value : null;
    };
    const getR = name => {
        const checked = formEl.querySelector(`[name="${name}"]:checked`);
        return checked ? checked.value : null;
    };

    // Self / personal
    const personal = {
        fullNameMm: get('self_name_mm') || null,
        fullNameEn: get('self_name_eng') || null,
        gender: (get('self_gender') ? toUpperEnum(get('self_gender')) : null),
        dateOfBirth: student.birthDate,
        ethnic: get('self_ethnic') || null,
        religion: get('self_religion') || null
    };

    const contact = {
        phoneNumber: student?.preEnrollment?.phoneNumber || null,
        usualPhoneNumber: student?.preEnrollment?.phoneNumber || null,
        email: null,
        birthPlace: `${regionSelect.options[regionSelect.selectedIndex].text} - ${placeInput.value.trim()}` || null,
        usualAddressRegion: null,
        usualAddressTownship: null,
        fullAddress: get('self_usual_address') || null
    };

    let nrc_statusS = getR(`self-nrc-available`)

    if (nrc_statusS === "yes") nrc_statusS = "AVAILABLE";
    else if (nrc_statusS === "no") nrc_statusS = "PENDING";
    else nrc_statusS = null;

    const nrc = {
        nrcStatus: null,
        nrcType: null,
        nrc: student?.preEnrollment?.nrc || null,
        // nationalNrc: get('self_another_nrc') || null
    };

    // academics - use entrance year / roll
    const academics = {
        academicYear: get('self_entrance_year') || null,//academic year
        uniEntranceRollno: get('self_roll_no') || null,
        uniEntranceYear: get('self_entrance_year') || null,
        majorYearId: null,
        rollNo: null
    };

    // parents
    const parents = [];

    // helper to build parent dto
    function parentBuilder(prefix, parentType) {
        let nrc_status = getR(`${prefix}-nrc-available`) || null;

        if (nrc_status === "yes") nrc_status = "AVAILABLE";
        else if (nrc_status === "no") nrc_status = "PENDING";
        if (nrc_status = null) {
            if (!!get(`${prefix}_nrc`)) {
                nrc_status = "AVAILABLE";
            } else {
                nrc_status = "PENDING";
            }
        }


        const parent = {
            parentType: parentType, // MOTHER | FATHER (server expects enum)
            fullNameMm: get(`${prefix}_name_mm`) || null,
            fullNameEn: get(`${prefix}_name_eng`) || null,
            ethnic: get(`${prefix}_ethnic`) || null,
            religion: get(`${prefix}_religion`) || null,
            occupation: get(`${prefix}_job`) || null,
            dateOfBirth: null,
            isDeceased: !!get(`${prefix}_death_date`),
            dateOfDeath: get(`${prefix}_death_date`) || null,
            nrcs:
            {
                nrcStatus: nrc_status,
                nrc: get(`${prefix}_nrc`) || null,
                nationalNrc: null
            }
            ,
            contacts:
            {
                phoneNumber: get(`${prefix}_phone`) || null,
                usualAddressRegionId: null,
                usualAddressTownshipId: null,
                fullAddress: get(`${prefix}_address`) || null
            }

        };
        return parent;
    }

    parents.push(parentBuilder('mom', 'MOTHER'));
    parents.push(parentBuilder('dad', 'FATHER'));

    if(GuardianYesNo){    
        parents.push(parentBuilder('guardian', 'GUARDIAN'));
    }

    // majors
    const majors = [];
    // expected numeric rank for each major input
    [
        ['qist', 'IST'],
        ['qce', 'CE'],
        ['qece', 'ECE'],
        ['qpre', 'PRE'],
        ['qame', 'AME']
    ].forEach(([inputName, code]) => {
        const val = getR(inputName);
        if (val) {
            majors.push({
                majorId: MAJOR_ID_MAP[code] || null,
                preferenceRank: Number(val)
            });
        }
    });

    // dorm
    const scholarshipSupportValue = getR('academicSupport');
    let dorm = null;
    if (DormYesNo){
        dorm = {
            
            dormFeeResponsible: get('dorm_fee_responsible') || null,
            otherName: get('dorm_fee_responsible') || null,
            otherRelationship: get('other_relationship') || null,
            otherAddress: get('other_address') || null,
            scholarshipSupport: scholarshipSupportValue ? (scholarshipSupportValue.toLowerCase() === 'yes') : false,
            scholarshipType: get('another_scholarship') || null,
            anotherScholarship: get('another_scholarship') || null
        };
    }

    // files are sent separately via FormData. For the DTO, create file metadata placeholders
    // If you want server to process files by reading form parts, it can; otherwise you can upload files first and include file IDs here.
    const files = []; // we leave empty: server will use multipart file parts

    // top-level
    const dto = {
        isFresher: true, // unknown from form
        academics: academics,
        contact: contact,
        nrc: nrc,
        personal: personal,
        parents: parents,
        dorm: dorm,
        files: files,
        majors: majors
    };

    return dto;
}

//helpers
function getAuthToken() {
    // Priority: localStorage.jwt -> cookie "Authorization" -> null
    const token = localStorage.getItem('jwt');
    if (token) return token;
    // read cookie Authorization=Bearer ...
    const cookies = document.cookie.split(';').map(c => c.trim());
    for (const c of cookies) {
        if (c.startsWith('Authorization=')) {
            return c.substring('Authorization='.length);
        }
    }
    return null;
}


function toBase64(file) {
    return new Promise((resolve, reject) => {
        const reader = new FileReader();
        reader.onload = () => resolve(reader.result);
        reader.onerror = reject;
        reader.readAsDataURL(file);
    });
}


async function collectFilesAsBase64(formEl) {
    const out = [];
    const fileInputs = formEl.querySelectorAll('input[type="file"]');

    for (const input of fileInputs) {
        const inputName = input.name; // used to map to fileTypeId
        const fileTypeId = FILETYPE_BY_INPUT_NAME[inputName];
        if (!input.files || input.files.length === 0) continue;

        for (const file of Array.from(input.files)) {
            const dataUrl = await fileToDataUrl(file);
            out.push({
                fileTypeId: fileTypeId || null,         // server-side will accept null or map accordingly
                fileData: dataUrl,                      // the data URL (preferred) or you can strip prefix to store raw base64
                originalName: file.name,
                size: file.size
            });
        }
    }
    return out;
}

function isoDate(value) {
    if (!value) return null;
    return value;
}

function toUpperEnum(s) {
    if (!s) return null;
    return s.toString().trim().toUpperCase();
}


function readFileInputs(fd, formEl) {
    // Append files to FormData keeping input names. Supports single and multiple file inputs.
    const fileInputs = formEl.querySelectorAll('input[type="file"]');
    fileInputs.forEach(input => {
        const name = input.name;
        if (!input.files) return;
        // For multiple files append each with same key
        Array.from(input.files).forEach((file, idx) => {
            // Append with original field name; server can read multiple parts under same name.
            fd.append(name, file);
        });
    });
}

function fileToDataUrl(file) {
    return new Promise((resolve, reject) => {
        const fr = new FileReader();
        fr.onerror = () => reject(new Error('File read error'));
        fr.onload = () => resolve(fr.result); // data URL e.g. "data:image/png;base64,...."
        fr.readAsDataURL(file);
    });
}

//dorm update fee
function payment_fee_update() {
    const phAmount = document.querySelectorAll('.phAmount');

    if (!detailPayment) return;

    const enrollmentFee = detailPayment.enrollmentFee || 0;
    const dormFee = DormYesNo ? (detailPayment.dormitoryFee || 0) : 0;
    const total = enrollmentFee + dormFee;

    // update all total placeholders
    phAmount.forEach(el => {
        el.textContent = total.toLocaleString();
    });

    // update breakdown info
    const phAmountInfo = document.querySelector(".phAmountInfo");
    if (phAmountInfo) {
        let breakdown = `Enrollment Fee: ${enrollmentFee.toLocaleString()}`;
        if (DormYesNo) breakdown += ` + Dormitory Fee: ${dormFee.toLocaleString()}`;
        breakdown += ` = Total: ${total.toLocaleString()}`;
        phAmountInfo.textContent = breakdown;
    }
}

//image viwer
const qrImg = document.getElementById("paymentQrImg");
const viewer = document.getElementById("imageViewer");
const viewerImg = document.getElementById("viewerImg");
const closeBtn = document.querySelector(".close-btn");

qrImg.addEventListener("click", () => {
    viewer.style.display = "block";
    viewerImg.src = qrImg.src; // use current QR src
});

closeBtn.addEventListener("click", () => {
    viewer.style.display = "none";
});

// close if clicked outside the image
viewer.addEventListener("click", (e) => {
    if (e.target === viewer) {
        viewer.style.display = "none";
    }
});

