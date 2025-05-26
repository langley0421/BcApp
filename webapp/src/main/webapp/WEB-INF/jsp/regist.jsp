<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <title>アカウント作成</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/register.css">
</head>
<body>
    <div class='container'>
        <div class='loginForm'>
            <h1 class='h1'>アカウント作成</h1>
            <% String error = (String) request.getAttribute("error"); %>
            <% if (error != null) { %>
                <p style="color:red;"><%= error %></p>
            <% } %>
            <% String message = (String) request.getAttribute("message"); %>
            <% if (message != null) { %>
                <p style="color:green;"><%= message %></p>
            <% } %>
            <form class='form' id="registerForm" action="register" method="post">
                <div class='inputGroup'>
                    <label for="email" class='label'>メールアドレス</label>
                    <input
                        type="email"
                        id="email"
                        name="email"
                        class='input'
                        placeholder="your@email.com"
                        required
                    />
                </div>
                <div class='inputGroup'>
                    <label for="password" class='label'>パスワード</label>
                    <input
                        type="password"
                        id="password"
                        name="password"
                        class='input'
                        placeholder="●●●●●●●●"
                        required
                    />
                </div>
                <div class='inputGroup'>
                    <label for="confirmPassword" class='label'>パスワード（確認）</label>
                    <input
                        type="password"
                        id="confirmPassword"
                        name="confirmPassword"
                        class='input'
                        placeholder="●●●●●●●●"
                        required
                    />
                </div>
                <button type="submit" class='button'>アカウント作成</button>
            </form>
            <a href="login" class='registerLink'>すでにアカウントをお持ちですか？ ログイン</a>
        </div>
    </div>
</body>
</html>
