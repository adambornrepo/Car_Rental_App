
const loggedIn = JSON.parse(localStorage.getItem("per/login"));
if (loggedIn) {
    const phoneNumber = localStorage.getItem("phoneNumber");
    console.log("Kullanıcının telefon numarası: ", phoneNumber);
    // HTML elementine telefon numarasını yerleştir
    document.getElementById("phone").innerText = phoneNumber;
} else {
    // kullanıcı giriş yapmamış
    console.log("Kullanıcı giriş yapmadı");
    window.location.href = "signin.html";

    // HTML elementlerine kullanıcı bilgilerini yerleştir
    document.getElementById("name").innerText = loggedIn.name;
    document.getElementById("surname").innerText = loggedIn.lastname;
    document.getElementById("username").innerText = loggedIn.username;
    document.getElementById("phone").innerText = loggedIn.phoneNumber;
    document.getElementById("birthdate").innerText = loggedIn.birthdate;
    document.getElementById("license-year").innerText = loggedIn.licenseYear;
}

function subForm(event) {
    // Form verilerini topla
    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;

    // JSON verisi oluştur
    const loginData = {
        username: username,
        password: password
    };

    const xhr = new XMLHttpRequest();
    xhr.open("POST", "/per/login");
    xhr.setRequestHeader("Content-Type", "application/json;charset=UTF-8");

    xhr.onload = function() {
        if (xhr.status === 200) {

            window.location.href = "per.html";
            const loggedIn = JSON.parse(localStorage.getItem("per/login"));
        } else {
            // Giriş başarısız oldu, hata mesajı göster
            alert(xhr.responseText);
        }
    };
    xhr.send(JSON.stringify(loginData));

    // Sayfa yenilemeyi önle
    event.preventDefault();
}

