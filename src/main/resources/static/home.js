function getAllNotes() {
    var sessionToken = sessionStorage.getItem('session');

    // Check if the user is authenticated
    if (!sessionToken) {
        // If not authenticated, redirect to the login page
        window.location.href = '/login.html';
    }

    fetch('/api/note', {
        method: 'GET',
        headers: {
            'Authorization': sessionToken
        }
    })
    .then(response => response.json())
    .then(data => {
        // Display notes in boxes
        var notesContainer = document.getElementById('notesContainer');
    
        data.forEach(note => {
            var noteBox = document.createElement('div');
            noteBox.style.border = '1px solid #ccc';
            noteBox.style.marginBottom = '10px';
            noteBox.style.padding = '10px';
            noteBox.innerHTML = `
                <h3>${note.title}</h3>
                <p>${note.content}</p>
                <p>Priority: ${note.priority}</p>
                <p>Color: ${note.colour}</p>
            `;
            notesContainer.appendChild(noteBox);
        });
    })
    .catch(error => {
        console.error('Error:', error);
    });
}