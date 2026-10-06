document.querySelectorAll('.tab-btn').forEach(btn => {
    btn.addEventListener('click', () => {
        // Remove active class
        document.querySelectorAll('.tab-btn').forEach(b => b.classList.remove('active'));
        document.querySelectorAll('.tab-content').forEach(c => c.classList.remove('active'));

        // Add active class
        btn.classList.add('active');
        document.getElementById(btn.dataset.tab).classList.add('active');
        
        // Hide result box when switching tabs
        document.getElementById('result-box').classList.add('hidden');
    });
});

async function runQuery(type) {
    let endpoint = `/api/query/${type}`;
    let payload = {};

    if (type === 'exact') {
        payload.text1 = document.getElementById('exact-text').value;
        payload.text2 = document.getElementById('exact-pattern').value;
    } else if (type === 'fuzzy') {
        payload.text1 = document.getElementById('fuzzy-text1').value;
        payload.text2 = document.getElementById('fuzzy-text2').value;
    } else if (type === 'similarity') {
        payload.text1 = document.getElementById('sim-text1').value;
        payload.text2 = document.getElementById('sim-text2').value;
    } else if (type === 'primality') {
        payload.number = parseInt(document.getElementById('prim-number').value, 10);
    }

    try {
        const response = await fetch(endpoint, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });

        if (!response.ok) {
            throw new Error('Network response was not ok');
        }

        const data = await response.json();
        displayResult(data);
    } catch (error) {
        console.error('Error executing query:', error);
        alert('Failed to execute query. Make sure the server is running.');
    }
}

function displayResult(data) {
    document.getElementById('result-box').classList.remove('hidden');
    document.getElementById('res-algo').textContent = data.algorithm || 'Unknown';
    document.getElementById('res-time').textContent = data.timeNs ? data.timeNs.toLocaleString() : '0';
    document.getElementById('res-ops').textContent = data.operations ? data.operations.toLocaleString() : '0';
    document.getElementById('res-complex').textContent = data.complexity || 'Unknown';
    
    let outputVal = data.result !== undefined ? data.result : 'No output returned';
    if(typeof outputVal === 'boolean') {
        outputVal = outputVal ? "True" : "False";
    }
    document.getElementById('res-output').textContent = outputVal;
    
    // Animate box entry
    const resBox = document.getElementById('result-box');
    resBox.style.animation = 'none';
    resBox.offsetHeight; /* trigger reflow */
    resBox.style.animation = 'fadeIn 0.5s ease forwards';
}
