document.querySelector(".form").addEventListener('submit', function(event) {
    event.preventDefault(); // フォームのデフォルト動作（リロード）を止める
    login();
});

function setCookie(name, value, days) {
    const date = new Date();
    date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000)); // 日数をミリ秒に変換
    const expires = "expires=" + date.toUTCString(); // UTC形式に変換
    document.cookie = name + "=" + value + ";" + expires + ";path=/"; // クッキーを設定
}

function login() {
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;
    const userData = JSON.parse(localStorage.getItem("userData")) || {}; // ローカルストレージからユーザーデータを取得

    console.log(userData); // デバッグ用

    if(userData[email] && userData[email].password === password) { // ユーザー名とパスワードが一致するか確認
        setCookie("loggedInUser", email, 1); // クッキーにユーザー名を保存
        window.location.href = "home.html"; // ログイン成功後、index.htmlにリダイレクト
    } else {
        alert("メールアドレス または パスワードが違います"); // ユーザー名またはパスワードが無効な場合のアラート
    }
}