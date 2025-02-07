function scanQRCode() {
    const fileInput = document.getElementById('qrInput');
    const formData = new FormData();
    formData.append('file', fileInput.files[0]);

    fetch('http://localhost:8080/api/qrcode/scan', {
        method: 'POST',
        body: formData,
    })
        .then(response => response.text())
        .then(decodedUrl => {
            // Redirect the user to the decoded URL
            window.location.href = decodedUrl;
        })
        .catch(error => console.error('Error:', error));
}