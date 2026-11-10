const token = localStorage.getItem("token");
const email =localStorage.getItem("userEmail") ||localStorage.getItem("email");
let walletId;
let html5QrCode;
if (!token) {
    window.location.href = "login.html";
}
document.addEventListener("DOMContentLoaded", function () {

    const emailElement =
        document.getElementById("userEmail");

    if (emailElement && email) {

        emailElement.textContent =
            "Logged in as: " + email;
    }

    loadWallet();
});
function getHeaders() {

    return {
        "Authorization": "Bearer " + token,
        "Content-Type": "application/json"
    };
}


// ============================================
// LOAD WALLET
// ============================================

async function loadWallet() {

    try {

        const response = await fetch(
            "http://localhost:8020/wallet/my-wallet",
            {
                method: "GET",
                headers: getHeaders()
            }
        );

        if (response.status === 401) {

            logout();

            return;
        }

        if (!response.ok) {

            const text =
                await response.text();

            console.error(
                "Wallet request failed:",
                response.status,
                text
            );

            throw new Error(
                "Failed to load wallet. Status: " +
                response.status
            );
        }

        const wallet =
            await response.json();

        walletId = wallet.id;

        const balanceElement =
            document.getElementById("balance");

        if (balanceElement) {

            balanceElement.textContent =
                "Balance: ₹" + wallet.balance;
        }

    } catch (error) {

        console.error(error);

        alert("Unable to load wallet");
    }
}


// ============================================
// SHOW SECTIONS
// ============================================

function showSection(sectionId) {

    const sections =
        document.querySelectorAll(".section");

    sections.forEach(function (section) {

        section.style.display = "none";
    });


    const selectedSection =
        document.getElementById(sectionId);

    if (selectedSection) {

        selectedSection.style.display = "block";
    }


    // Load transactions
    if (sectionId === "transactions-section") {

        loadTransactions();
    }


    // Generate My QR Code
    if (sectionId === "my-qr-section") {

        generateMyQRCode();
    }
}


// ============================================
// ADD MONEY
// ============================================

async function addMoney() {

    const amount =
        document.getElementById("addamount").value;

    if (!amount || Number(amount) <= 0) {

        alert("Enter a valid amount");

        return;
    }


    try {

        const response = await fetch(
            `http://localhost:8020/wallet/my-wallet/add?amount=${encodeURIComponent(amount)}`,
            {
                method: "POST",
                headers: getHeaders()
            }
        );


        if (response.status === 401) {

            logout();

            return;
        }


        if (!response.ok) {

            const text =
                await response.text();

            console.error(
                "Add money failed:",
                response.status,
                text
            );

            // Try to show backend error message
            try {

                const errorData =
                    JSON.parse(text);

                alert(
                    errorData.message ||
                    "Unable to add money"
                );

            } catch (error) {

                alert(
                    "Unable to add money. Status: " +
                    response.status
                );
            }

            return;
        }


        const data =
            await response.json();

        alert(data.message);


        document.getElementById(
            "addamount"
        ).value = "";


        await loadWallet();

        await loadTransactions();

    } catch (error) {

        console.error(error);

        alert("Unable to add money");
    }
}


// ============================================
// PAY MONEY
// ============================================

async function payMoney() {

    const amount =
        document.getElementById("payamount").value;

    const receiver =
        document.getElementById("receiver").value.trim();


    // --------------------------------------------
    // VALIDATE AMOUNT
    // --------------------------------------------

    if (!amount || Number(amount) <= 0) {

        alert("Enter a valid amount");

        return;
    }


    // --------------------------------------------
    // VALIDATE RECEIVER
    // --------------------------------------------

    if (!receiver) {

        alert("Enter receiver email");

        return;
    }


    console.log(
        "========== PAYMENT START =========="
    );

    console.log(
        "Amount:",
        amount
    );

    console.log(
        "Receiver:",
        receiver
    );

    console.log(
        "Token exists:",
        !!token
    );


    try {

        const url =
            `http://localhost:8020/wallet/my-wallet/pay?amount=${encodeURIComponent(amount)}&receiver=${encodeURIComponent(receiver)}`;


        console.log(
            "Payment URL:",
            url
        );


        const response =
            await fetch(
                url,
                {
                    method: "POST",
                    headers: getHeaders()
                }
            );


        console.log(
            "Payment response status:",
            response.status
        );


        // --------------------------------------------
        // UNAUTHORIZED
        // --------------------------------------------

        if (response.status === 401) {

            console.error(
                "JWT authentication failed"
            );

            logout();

            return;
        }


        // --------------------------------------------
        // FORBIDDEN
        // --------------------------------------------

        if (response.status === 403) {

            const text =
                await response.text();

            console.error(
                "403 FORBIDDEN:",
                text
            );

            alert(
                "Payment rejected by server (403).\n" +
                "Check Spring Security / JWT."
            );

            return;
        }


        // --------------------------------------------
        // OTHER HTTP ERRORS
        // --------------------------------------------

        if (!response.ok) {

            const text =
                await response.text();

            console.error(
                "Payment failed:",
                response.status,
                text
            );


            // ----------------------------------------
            // READ BACKEND ERROR MESSAGE
            // ----------------------------------------

            try {

                const errorData =
                    JSON.parse(text);

                alert(
                    errorData.message ||
                    "Payment failed"
                );

            } catch (error) {

                alert(
                    "Payment failed.\nStatus: " +
                    response.status
                );
            }

            return;
        }


        // --------------------------------------------
        // SUCCESS RESPONSE
        // --------------------------------------------

        const data =
            await response.json();


        console.log(
            "Payment response:",
            data
        );


        alert(data.message);


        // --------------------------------------------
        // CLEAR INPUTS
        // --------------------------------------------

        document.getElementById(
            "payamount"
        ).value = "";


        document.getElementById(
            "receiver"
        ).value = "";


        // --------------------------------------------
        // REFRESH WALLET
        // --------------------------------------------

        await loadWallet();


        // --------------------------------------------
        // REFRESH TRANSACTIONS
        // --------------------------------------------

        await loadTransactions();


        console.log(
            "========== PAYMENT END =========="
        );


    } catch (error) {

        console.error(
            "Payment JavaScript error:",
            error
        );

        alert(
            "Unable to make payment"
        );
    }
}


// ============================================
// LOAD TRANSACTIONS
// ============================================

async function loadTransactions() {

    const transactionsContainer =
        document.getElementById(
            "transactions"
        );


    if (!transactionsContainer) {

        return;
    }


    try {

        const response =
            await fetch(
                "http://localhost:8020/wallet/my-transactions",
                {
                    method: "GET",
                    headers: getHeaders()
                }
            );


        if (response.status === 401) {

            logout();

            return;
        }


        if (!response.ok) {

            const text =
                await response.text();

            console.error(
                "Transaction request failed:",
                response.status,
                text
            );

            throw new Error(
                "Failed to load transactions"
            );
        }


        const transactions =
            await response.json();


        // --------------------------------------------
        // CLEAR OLD TRANSACTIONS
        // --------------------------------------------

        transactionsContainer.replaceChildren();


        // --------------------------------------------
        // NO TRANSACTIONS
        // --------------------------------------------

        if (transactions.length === 0) {

            const emptyMessage =
                document.createElement("p");

            emptyMessage.textContent =
                "No transactions yet.";


            transactionsContainer.appendChild(
                emptyMessage
            );

            return;
        }


        // --------------------------------------------
        // DISPLAY TRANSACTIONS
        // --------------------------------------------

        transactions.forEach(
            function (transaction) {

                const transactionDiv =
                    document.createElement("div");

                transactionDiv.className =
                    "transaction-item";


                const typeElement =
                    document.createElement("h4");

                typeElement.textContent =
                    transaction.type;


                const amountElement =
                    document.createElement("p");

                amountElement.textContent =
                    "Amount: ₹" +
                    transaction.amount;


                const receiverElement =
                    document.createElement("p");

                receiverElement.textContent =
                    "Receiver: " +
                    (transaction.receiver || "N/A");


                const dateElement =
                    document.createElement("p");

                dateElement.textContent =
                    "Date: " +
                    (transaction.createdAt || "N/A");


                transactionDiv.appendChild(
                    typeElement
                );

                transactionDiv.appendChild(
                    amountElement
                );

                transactionDiv.appendChild(
                    receiverElement
                );

                transactionDiv.appendChild(
                    dateElement
                );


                transactionsContainer.appendChild(
                    transactionDiv
                );

            }
        );


    } catch (error) {

        console.error(error);

        transactionsContainer.textContent =
            "Unable to load transactions.";
    }
}


// ============================================
// QR SCANNER
// ============================================

function openScanner() {

    // Show actual scanner container
    const scannerBox =
        document.getElementById(
            "qr-scanner"
        );


    if (scannerBox) {

        scannerBox.style.display =
            "block";
    }
    // Prevent multiple scanners
    if (html5QrCode) {
        return;
    }
    // Check QR scanner library
    if (typeof Html5Qrcode === "undefined") {
        alert("QR scanner library could not be loaded.");
        return;
    }
    html5QrCode =new Html5Qrcode("reader");
    html5QrCode.start(
        {
            facingMode: "environment"
        },
        {
            fps: 10,
            qrbox: 250
        },
        // QR CODE SUCCESS
        function (decodedText) {
            console.log("QR Code detected:",decodedText);
            const receiverElement =document.getElementById("receiver");
            if (receiverElement) {
                receiverElement.value =decodedText;
            }
            closeScanner();
            // Open Pay Money section
            showSection(
                "pay-money"
            );
        },
        // QR SCANNING ERROR
        function (errorMessage) {

            // Scanner continuously checks frames.
            // No alert needed.
        }
    ).catch(function (error) {

        console.error(
            "QR scanner error:",
            error
        );
        alert(
            "Unable to start QR scanner. " +
            "Please allow camera access.");
        html5QrCode = null;
        const scannerBox =document.getElementById("qr-scanner");
        if (scannerBox) {
            scannerBox.style.display ="none";
        }
    });
}
// CLOSE QR SCANNER

function closeScanner() {
    if (html5QrCode) {
        html5QrCode.stop()
            .then(function () {
                html5QrCode.clear();
                html5QrCode = null;
            })
            .catch(function (error) {
                console.error("QR scanner close error:",error);
                html5QrCode = null;
            });
    }
    const scannerBox =document.getElementById("qr-scanner");
    if (scannerBox) {
        scannerBox.style.display ="none";
    }
}
// GENERATE MY QR CODE
function generateMyQRCode() {
    const qrContainer =
        document.getElementById("myQrCode");
    const qrEmail =
        document.getElementById("qrEmail");
    if (!qrContainer || !email) {
        return;
    }
    // Check QR generator library
    if (typeof QRCode === "undefined") {
        alert("QR code generator library could not be loaded.");
        return;
    }
    // Clear old QR code
    qrContainer.innerHTML = "";
    // Generate QR using user's email
    new QRCode(
        qrContainer,
        {
            text: email,
            width: 220,
            height: 220
        }
    );
    // Display email below QR
    if (qrEmail) {
        qrEmail.textContent ="Payment ID: " + email;
    }
}
function logout() {
    localStorage.removeItem("token");
    localStorage.removeItem("userEmail");
    localStorage.removeItem("email");
    window.location.href = "login.html";
}