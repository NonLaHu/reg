function updateYears() {
    const majorId = document.getElementById("major").value;
    const yearSelect = document.getElementById("majorYear");

    [...yearSelect.options].forEach(opt => {
        if (!opt.value) return; // skip "Select Year"
        opt.hidden = opt.getAttribute("data-major-id") !== majorId;
    });

    yearSelect.value = ""; // reset selection
}

document.getElementById("major").addEventListener("change", updateYears);

//check if nrc are correct and other data are correct -------------------------------------
function check_dataFormat(e){
    //file
    const fileInput = document.getElementById('studentIdCardUrl');
    const errorEl = document.getElementById('passCertificateError');
    if (!fileInput.files.length) {
        errorEl.textContent = 'Please select a file';
        return true;
    }
    if (errorEl.textContent != '') {
        return true;
    }

    //NRC check
    const inputNrcCheck = document.getElementById('nrc')
    const selectedNrcCheck = document.querySelector('input[name="nrcType"]:checked')?.value;
    const responseMessageCheck = document.getElementById('errorMessage');
    responseMessage.textContent='';

    if (selectedNrcCheck == 'advanced'){
        advanced_nrc_update();
    }
    else if (selectedNrcCheck == 'pendingNRC'){
        inputNrcCheck.value=''
    }else {
        const nrcRegex = /^[၀-၉]{1,2}\/[က-အ]{3}\([\u1000-\u109F]+\)[၀-၉]{6}$/;
        
        if(!nrcRegex.test(inputNrcCheck.value)){
            responseMessageCheck.textContent = 'make sure the nrc format is of "၀၀/ကကက(နိုင်)၁၂၃၄၅၆"';
            return true;
        }
    }

    //PHONE NUMBER CHECK IF THEY ARE ALL 0-9
    const phoneNumberCheck = document.getElementById('phoneNumber');
    const viberPhoneNumberCheck = document.getElementById('viberPhoneNumber');
    const errorGlobalMessage = document.getElementById('errorGlobalMessage');
    errorGlobalMessage.textContent =''; 

    // Regex to match only digits
    const phoneRegex = /^(?:\d+|[၀-၉]+)$/;  

    function validatePhone(inputEl) {
        const value = inputEl?.value.trim();
        if (!value) {
            return true;
        }
        return phoneRegex.test(value);
    }

    if (!validatePhone(phoneNumberCheck)) {
        errorGlobalMessage.textContent='Phone numver must contain only digits';
        return true;
    }

    if (!validatePhone(viberPhoneNumberCheck)) {
        errorGlobalMessage.textContent='Phone numver must contain only digits';
        return true;
    }

    return false;
}

//form submission -------------------------------------
const form = document.getElementById('preEnrollmentForm');
const responseMessage = document.getElementById('responseMessage');
form.addEventListener('submit', function(event) {
    event.preventDefault();

    if(check_dataFormat(event)){return}

    const dto = {
        fullNameMm: document.getElementById('fullNameMm').value,
        gender: document.getElementById('gender').value,
        prevRollNo: document.getElementById('prevRollNo').value,
        majorId: document.getElementById('major').value,
        majorYearId: document.getElementById('majorYear').value,
        phoneNumber: document.getElementById('phoneNumber').value,
        viberPhoneNumber: document.getElementById('viberPhoneNumber').value
    };
    const emailValue = document.getElementById('email').value;
    const nrcValue = document.getElementById('nrc').value;
    if (nrcValue.trim() !== "") {
        dto.nrc = nrcValue;
    }
    if (emailValue.trim() !== "") {
        dto.nrc = emailValue;
    }

    const formData = new FormData();
    formData.append('dto', new Blob([JSON.stringify(dto)], { type: 'application/json' }));

    const fileInput = document.getElementById('studentIdCardUrl');
    if (fileInput.files.length > 0) {
        formData.append('studentIdCardUrl', fileInput.files[0]);
    }

    fetch('/api/pre_enrollments_old', {
        method: 'POST',
        body: formData 
    })
    .then(res => res.json())
    .then(data => {
        if (data.status === "ok") {
            responseMessage.textContent = "Submission successful! Please wait for our contact to your Viber number.";
            form.reset();
        } else {
            responseMessage.textContent = "Error: " + (data.message || "Unknown error");
        }
    })
    .catch(error => {
        responseMessage.textContent = "Error: " + error.message;
    });
});

// Nrc Selection between advanced used and normal used -------------------------------------
(function () {
    const radios = document.querySelectorAll('input[name="nrcType"]');
    const nrc = document.getElementById('nrcSection');
    const adv = document.getElementById('advancedNrcSection');
    const pending = document.getElementById('pendingSection');
    const nrcInput = document.getElementById('nrc')

    const PENDING_VALUE="pendingNRC";
    const NRC_VALUE="nrc";
    const ADVANCED_VALUE="advanced";


    function update() {
    const selected = document.querySelector('input[name="nrcType"]:checked')?.value;

    if (selected == PENDING_VALUE){
        nrc.classList.add('hidden');
        adv.classList.add('hidden');
        pending.classList.remove('hidden');
        console.log("gg")
        nrcInput.value=""
    }else if (selected == NRC_VALUE){
        nrc.classList.remove('hidden');
        adv.classList.add('hidden');
        pending.classList.add('hidden');
    }else if (selected == ADVANCED_VALUE){
        nrc.classList.add('hidden');
        adv.classList.remove('hidden');
        pending.classList.add('hidden');
    }

    }
    radios.forEach(r => r.addEventListener('change', update));
})();

//Nrc Advanced Use -------------------------------------
const regionSelect = document.getElementById('region');
const codeSelect   = document.getElementById('code');
const typeSelect   = document.getElementById('type');
const numberSelect   = document.getElementById('nrc_number');
const inputNrc = document.getElementById("nrc");

function advanced_nrc_change(){
    const selectedRegion = regionSelect.value;

    [...codeSelect.options].forEach(opt => {
        if (!opt.value) return;
        opt.hidden = opt.getAttribute("data-code") !== selectedRegion;
    });
    codeSelect.value = "";
};
regionSelect.addEventListener('change',advanced_nrc_change);

//convert En digits to mm digits
function digitEnToMn(str) {
    if (!str) return '';
    const map = ['၀','၁','၂','၃','၄','၅','၆','၇','၈','၉'];
    return str.toString().replace(/\d/g, d => map[d]);
}

function advanced_nrc_update() {
    const region = regionSelect?.value || '';
    const code   = codeSelect?.value || '';
    const type   = typeSelect?.value || '';
    const number = numberSelect?.value || '';

    let nrcString = '';

    if (region) nrcString += digitEnToMn(region);
    if (code) nrcString += '/' + code;
    if (type) nrcString += `(${type})`;
    if (number) nrcString += number;

    inputNrc.value = nrcString;
}

const elementsNRC = [regionSelect, codeSelect, typeSelect, numberSelect];
elementsNRC.forEach(el => {
    el?.addEventListener('change', advanced_nrc_update);
});
advanced_nrc_change();


// Language switch functions -------------------------------------
const TOGGLE_SELECTOR = '.lang-toggle';
const DATA_ATTR = 'data-text-mm';
const ORIGINAL_ATTR = 'data-text-original';
const toggle = document.querySelector(TOGGLE_SELECTOR);
const elements = Array.from(document.querySelectorAll('[' + DATA_ATTR + ']'));
elements.forEach(el => {
if (!el.hasAttribute(ORIGINAL_ATTR)) {
    if (['input','textarea','select'].includes(el.tagName.toLowerCase()) || el.isContentEditable) {
    el.setAttribute(ORIGINAL_ATTR, el.value ?? '');
    } else {
    el.setAttribute(ORIGINAL_ATTR, el.textContent ?? '');
    }
}
});
function isFormControl(el){ const tag=(el.tagName||'').toLowerCase(); return tag==='input'||tag==='textarea'||tag==='select'||el.isContentEditable }
function applyState(checked){
elements.forEach(el=>{
    const mm = el.getAttribute(DATA_ATTR) || '';
    const original = el.getAttribute(ORIGINAL_ATTR) || '';
    if (checked) {
    if (isFormControl(el)) { el.value = mm || original; if (el.placeholder !== undefined && mm) el.placeholder = mm; }
    else el.textContent = mm || original;
    } else {
    if (isFormControl(el)) { el.value = original; }
    else el.textContent = original;
    }
});
}
toggle.addEventListener('click', function(){ const next = this.getAttribute('aria-checked')!=='true'; this.setAttribute('aria-checked', String(next)); this.setAttribute('aria-pressed', String(next)); applyState(next); });
toggle.addEventListener('keydown', function(e){ if (e.key==='Enter' || e.key===' ') { e.preventDefault(); this.click(); }});

document.getElementById('clearBtn').addEventListener('click', function () {
const fields = document.querySelectorAll('input, textarea');

fields.forEach(field => {
    if (field.type === 'checkbox' || field.type === 'radio') {
    field.checked = false;
    } else {
    field.value = '';
    }
});
});

// FILE PREVIEW -------------------------------------
(() => {
const input = document.getElementById('studentIdCardUrl');
const preview = document.getElementById('passCertificatePreview');
const thumb = document.getElementById('passCertificateThumb');
const nameEl = document.getElementById('passCertificateName');
const metaEl = document.getElementById('passCertificateMeta');
const clearBtn = document.getElementById('passCertificateClear');
const errorEl = document.getElementById('passCertificateError');

const MAX_BYTES = 3 * 1024 * 1024; // 3MB

// Utility: format bytes
function formatBytes(bytes) {
    if (bytes === 0) return '0 B';
    const units = ['B','KB','MB','GB'];
    const i = Math.floor(Math.log(bytes) / Math.log(1024));
    return (bytes / Math.pow(1024, i)).toFixed(i ? 1 : 0) + ' ' + units[i];
}

// Clean preview area
function clearPreview() {
    preview.hidden = true;
    thumb.innerHTML = '';
    nameEl.textContent = '';
    metaEl.textContent = '';
    errorEl.textContent = '';
}

// Render file preview
function renderPreview(file) {
    clearPreview(); // start clean

    if (!file) return;

    // Basic validation
    if (file.size > MAX_BYTES) {
    errorEl.textContent = `File too large — max ${formatBytes(MAX_BYTES)}.`;
    return;
    }

    const type = file.type || '';
    nameEl.textContent = file.name;
    metaEl.textContent = `Size: ${formatBytes(file.size)} • ${type || 'unknown'}`;

    if (type.startsWith('image/')) {
        // show thumbnail using object URL for best performance
        const url = URL.createObjectURL(file);
        const img = document.createElement('img');
        img.src = url;
        img.alt = file.name;
        img.onload = () => URL.revokeObjectURL(url);
        thumb.appendChild(img);
        errorEl.textContent = '';
    } else  {
        // generic file icon
        thumb.innerHTML = `
            <svg width="48" height="48" viewBox="0 0 24 24" fill="none" aria-hidden="true" xmlns="http://www.w3.org/2000/svg">
            <rect x="3" y="4" width="14" height="16" rx="2" stroke="#9ca3af" stroke-width="1.2"></rect>
            <path d="M17 4v4" stroke="#9ca3af" stroke-width="1.2" stroke-linecap="round"></path>
            </svg>`;
        errorEl.textContent = 'please only select img files';
    }
    preview.hidden = false;
}

input.addEventListener('change', (e) => {
    const file = input.files && input.files[0];
    renderPreview(file);
});

clearBtn.addEventListener('click', (e) => {
    e.preventDefault();
    clearPreview();
    input.value=""
    document.querySelector('.file-label').focus();
});
clearPreview();

})();


