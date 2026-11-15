// public/js/form.js
// document.addEventListener('DOMContentLoaded', () => {
// ---------- CONFIG ----------
// Map frontend major short codes to DB major IDs (CHANGE these ids to match your DB).
const MAJOR_ID_MAP = {
  IST: 1,
  CE: 2,
  ECE: 3,
  PRE: 4,
  AME: 5
};

const FILETYPE_BY_INPUT_NAME = {
  'self_picture': 1,
  'nrc_photos': 2,
  'signatures': 3,
  'recommendations_docs': 4,
  'payment_slip': 5
};

const API_ENDPOINT = '/api/enrollments';

// ---------- ELEMENTS ----------
const form = document.getElementById('admissionForm');
const langToggle = document.getElementById('langToggle');
const themeToggle = document.getElementById('themeToggle');
const otherResponsibility = document.getElementById('otherResponsibility');
const dormFeeSelect = form.querySelector('select[name="dorm_fee_responsible"]');

// ---------- UTIL ----------
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

function showToast(message, isError = false) {
  // Minimal feedback - replace with your UI toast if available.
  if (isError) {
    alert('Error: ' + message);
  } else {
    alert('Success: ' + message);
  }
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



function isoDate(value) {
  if (!value) return null;
  // value is already in YYYY-MM-DD from <input type=date>
  return value;
}

function toUpperEnum(s) {
  if (!s) return null;
  return s.toString().trim().toUpperCase();
}

// ---------- LANGUAGE ----------

const LANG_KEY = 'utycc_lang';
function setLanguage(lang) {
  // lang: 'en' or 'my'
  document.querySelectorAll('.lang').forEach(el => {
    const en = el.dataset.en;
    const my = el.dataset.my;
    if (lang === 'my' && typeof my !== 'undefined') {
      el.textContent = my;
    } else if (lang === 'en' && typeof en !== 'undefined') {
      el.textContent = en;
    }
  });
  localStorage.setItem(LANG_KEY, lang);
}

function initLanguage() {
  const saved = localStorage.getItem(LANG_KEY) || 'en';
  setLanguage(saved);

  langToggle.addEventListener('click', () => {
    const current = localStorage.getItem(LANG_KEY) || 'en';
    setLanguage(current === 'en' ? 'my' : 'en');
  });
}

// ---------- THEME ----------
const THEME_KEY = 'utycc_theme';
function setTheme(theme) {
  // theme: 'light' | 'dark'
  document.body.classList.toggle('dark');
  localStorage.setItem(THEME_KEY, theme);
}

function initTheme() {
  const saved = localStorage.getItem(THEME_KEY) || 'light';
  setTheme(saved);
  themeToggle.addEventListener('click', () => {
    const current = localStorage.getItem(THEME_KEY) || 'light';
    setTheme(current === 'light' ? 'dark' : 'light');
  });
}

// ---------- DORM OTHER ----------

function initDormToggle() {
  function refreshOtherBlock() {
    const v = dormFeeSelect.value;
    if (v === 'other') {
      otherResponsibility.style.display = '';
    } else {
      otherResponsibility.style.display = 'none';
    }
  }
  dormFeeSelect.addEventListener('change', refreshOtherBlock);
  refreshOtherBlock();
}

// ---------- FORM -> DTO MAPPING ----------
function buildDtoFromForm(formEl) {
  const get = name => {
    const el = formEl.querySelector(`[name="${name}"]`);
    return el ? el.value : null;
  };

  // Self / personal
  const personal = {
    fullNameMm: get('self_name_mm') || null,
    fullNameEn: get('self_name_eng') || null,
    gender: (get('self_gender') ? toUpperEnum(get('self_gender')) : null),
    dateOfBirth: isoDate(get('self_dob')),
    ethnic: get('self_ethnic') || null,
    religion: get('self_religion') || null
  };

  const contact = {
    phoneNumber: get('self_phone') || null,
    usualPhoneNumber: get('self_usual_phone') || null,
    email: null, // no email in form; keep null
    birthPlace: get('self_birthplace') || null,
    usualAddressRegion: null, // not provided as id; backend can accept string if desired
    usualAddressTownship: null,
    fullAddress: get('self_usual_address') || null
  };

  const nrc = {
    nrcStatus: null,
    nrcType: null,
    nrc: get('self_nrc') || null,
    nationalNrc: get('self_another_nrc') || null
  };

  // academics - use entrance year / roll
  const academics = [];
  const academic = {
    academicYear: get('self_entrance_year') || null,
    uniEntranceRollno: get('self_roll_no') || null,
    uniEntranceYear: get('self_entrance_year') || null,
    majorYearId: null,
    rollNo: get('self_roll_no') || null
  };
  academics.push(academic);

  // parents
  const parents = [];

  // helper to build parent dto
  function parentBuilder(prefix, parentType) {
    const parent = {
      parentType: parentType, // MOTHER | FATHER (server expects enum)
      fullNameMm: get(`${prefix}_name_mm`) || null,
      fullNameEn: get(`${prefix}_name_eng`) || null,
      ethnic: get(`${prefix}_ethnic`) || null,
      religion: get(`${prefix}_religion`) || null,
      occupation: get(`${prefix}_job`) || null,
      dateOfBirth: null,
      isDeceased: !!get(`${prefix}_death_date`),
      dateOfDeath: isoDate(get(`${prefix}_death_date`)),
      nrcs:
      {
        nrcStatus: null,
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

  // majors
  const majors = [];
  // expected numeric rank for each major input
  [
    ['major_ist', 'IST'],
    ['major_ce', 'CE'],
    ['major_ece', 'ECE'],
    ['major_pre', 'PRE'],
    ['major_ame', 'AME']
  ].forEach(([inputName, code]) => {
    const val = get(inputName);
    if (val) {
      majors.push({
        majorId: MAJOR_ID_MAP[code] || null,
        preferenceRank: Number(val)
      });
    }
  });

  // dorm
  const scholarshipSupportValue = get('scholarship_support');
  const dorm = {
    dormFeeResponsible: get('dorm_fee_responsible') || null,
    otherName: get('other_name') || null,
    otherRelationship: get('other_relationship') || null,
    otherAddress: get('other_address') || null,
    scholarshipSupport: scholarshipSupportValue ? (scholarshipSupportValue.toLowerCase() === 'yes') : false,
    scholarshipType: get('scholarship_type') || null,
    anotherScholarship: get('another_scholarship') || null
  };

  // files are sent separately via FormData. For the DTO, create file metadata placeholders
  // If you want server to process files by reading form parts, it can; otherwise you can upload files first and include file IDs here.
  const files = []; // we leave empty: server will use multipart file parts

  // top-level
  const dto = {
    isFresher: null, // unknown from form
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

async function handleSubmit(e) {
  e.preventDefault();
  try {
    // your DTO
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

    showToast(resJson?.message || 'Enrollment accepted');
    form.reset();
    setLanguage(localStorage.getItem(LANG_KEY) || 'en');
  } catch (err) {
    console.error(err);
    showToast(err.message || 'Unexpected error', true);
  }
}

// helper to convert file -> base64
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


// ---------- INITIALIZE ----------
initLanguage();
initTheme();
initDormToggle();

form.addEventListener('submit', handleSubmit);
// });
