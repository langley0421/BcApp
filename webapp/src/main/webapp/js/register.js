document.getElementById("registerForm").addEventListener('submit', function(event) {
    event.preventDefault();
    register();
});
    

// function showModal(message, redirectUrl) {
//     const alert = document.getElementById('modalWindow');
//     const alertMessage = document.getElementById('modal');
  
//     alertMessage.textContent = message;
//     alert.classList.remove('hidden');
    
//     alert.classList.add('show');
//     setTimeout(() => {
//         alert.classList.remove('show');
//         alert.classList.add('hidden');
//     }, 200000);
//   }

  

function register() {

    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;
    const passwordConfirm = document.getElementById("confirmPassword").value;

    if(password !== passwordConfirm) {
        showModal("パスワードが一致しません。");
        return;
    }

    const userData = JSON.parse(localStorage.getItem("userData")) || {};
    
    if(userData[email]){
        showModal("このメールアドレスは既に登録されています。");
        return;
    }

    userData[email] = { password };
    
    // ローカルストレージにデータを保存
    try {
        localStorage.setItem("userData", JSON.stringify(userData));
        alert("登録が完了しました。");
        window.location.href = "login";

    } catch (error) {
        console.error("ローカルストレージへの保存に失敗しました:", error);
        showModal("データの保存に失敗しました。");
    }
    
}