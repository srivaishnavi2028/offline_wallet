document.getElementById("regform").addEventListener("submit", async function(event) {

    event.preventDefault();

    const name = document.getElementById("name").value;
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;

    const user = {
        name: name,
        email: email,
        password: password
    };

    try {

        const response = await fetch("http://localhost:8020/users/register", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(user)
        });

        const data = await response.json();

        if (response.ok) {
           alert("Registration successful! Please log in.");
            console.log(data);
        } else {
            alert("Registration failed.");
            console.log(data);
        }

    } catch (error) {

        alert("Unable to connect to server.");
        console.error(error);
    }

});