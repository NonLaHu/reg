(async () => {
    const jwt = localStorage.getItem('jwt');

    if (!jwt) {
        window.location.href = '/';
        return;
    }

    try {
        const htmlRes = await fetch('/private/admin', {
            headers: { 'Authorization': 'Bearer ' + jwt }
        });

        if (!htmlRes.ok) throw new Error('Unauthorized');

        const adminHtml = await htmlRes.text();

        const jsRes = await fetch('/private/js/admin.js', {
            headers: { 'Authorization': 'Bearer ' + jwt }
        });

        if (!jsRes.ok) throw new Error('Unauthorized');

        const adminJs = await jsRes.text();

        document.body.innerHTML = adminHtml;

        const script = document.createElement('script');
        script.textContent = adminJs;
        document.body.appendChild(script);

    } catch (err) {
        console.error('Failed to load admin panel:', err);
        window.location.href = '/';
    }
})();
