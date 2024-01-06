function login() {
    var username = document.getElementById("username").value;
    var password = document.getElementById("password").value;

    // Create JSON payload
    var payload = {
        emailId: username,
        password: password
    };

    // Make API call to /login
    fetch('/api/auth/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(payload)
    })
    .then(response => response.json())
    .then(data => {
        var tokenType = data.authType;
        var authToken = data.authToken;

        // Store the session token for further use
        sessionStorage.setItem('session', tokenType + " " + authToken);
        window.location.href = '/home.html';
    })
    .catch(error => {
        console.error('Error:', error);
    });
}