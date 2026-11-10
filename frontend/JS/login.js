document.getElementById("loginform").addEventListener("submit", async function(event) {

    event.preventDefault();

    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;

    const user = {
        email: email,
        password: password
    };

    try {
        const response = await fetch("http://localhost:8020/users/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(user)
        });

        const token = await response.text();

        if (response.ok) {
            localStorage.setItem("token", token);
            localStorage.setItem("userEmail", email);

            alert("Login successful!");
            window.location.href = "dashboard.html";
        } else {
            alert("Login failed.");
        }

    } catch (error) {
        alert("Unable to connect to server.");
        console.error(error);
    }
});