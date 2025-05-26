<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/login.css">
</head>
<body>
    <div class='container'>
        <div class='loginForm'>
            <h1 class='h1'>ログイン</h1>
            <% String error = (String) request.getAttribute("error"); %>
            <% if (error != null) { %>
                <p style="color:red;"><%= error %></p>
            <% } %>

            <% String message = (String) request.getAttribute("message"); %>
            <% if (message != null) { %>
                <p style="color:green;"><%= message %></p>
            <% } %>
            <form class='form' action="login" method="post">
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
                        placeholder='●●●●●●●●'
                        required
                    />
                </div>
                <button type="submit" class='button'>ログイン</button>
                 <!-- 以下デバッグ用 -->
                <!-- <button type="submit" class="button"><a href="./home.html" class="debug">ログイン</a></button> -->
            </form>
            <div class="divider">
                <span>または</span>
            </div>
                <a href="register" class='registerLink'>
                    アカウントを作成する
                </a>
        </div>
    </div>
</body>
</html>