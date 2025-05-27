<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>名刺管理</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/home.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dark_mode.css">
    <script>
    </script>
</head>
<body>
    <header class="header">
        <h1>BizCardy</h1>
        <button id="dark-mode-toggle">
            <span id="icon">
                <!-- 省略：SVGアイコン -->
            </span>
        </button>
    </header>

    <aside class="sidebar">
        <ul>
            <li><a href="#section1" class="nav-link">ホーム</a></li>
            <li><a href="#section2" class="nav-link">お気に入り</a></li>
            <li><a href="#section3" class="nav-link">最近追加</a></li>
            <li><a href="#section4" class="nav-link">名刺検索(仮)</a></li>
            <li><a href="#section5" class="nav-link">アカウント設定</a></li>
        </ul>
    </aside>

    <main class="content">
        <div class="search-bar">
            <input type="text" placeholder="名刺を検索 ．．．" class="search-input">
            <button class="search-button">検索</button>
        </div>
        
        <div id="recent-filter" class="recent-filter hidden">
          <label>
              何日以内:
              <input class="recent-filter-input" type="number" id="day-filter" min="1" value="7">
              <button class="recent-filter-button" id="apply-filter">絞り込む</button>
          </label>
        </div>

        <div id="card-list" class="card-list">
            <c:forEach var="card" items="${cardList}">
                <div class="card">
                    <h3>${card.name}</h3>
                    <p>
  						${card.companyName}
						<c:if test="${not empty card.departmentName}">（${card.departmentName}）</c:if>
					</p>
                </div>
            </c:forEach>
        </div>

        <div id="modal" class="modal hidden">
            <div class="modal-content">
                <h2>登録フォーム</h2>
                	<form action="${pageContext.request.contextPath}/cardRegister" method="post">
	                	<table class="modal-table">
		                    <input type="hidden" name="card_id" value="0">
		                    <tr>
		                        <td>
		                            <label>会社名<br>
		                                <input type="text" name="company_name">
		                            </label>
		                        </td>
		                        <td>
		                            <label>郵便番号<br>
		                                <input type="text" name="company_zipcode">
		                            </label>
		                        </td>
		                    </tr>
		                    <tr>
		                        <td>
		                            <label>氏名（※必須）<br>
		                                <input type="text" name="name">
		                            </label>
		                        </td>
		                        <td>
		                            <label>住所<br>
		                                <input type="text" name="company_address">
		                            </label>
		                        </td>
		                    </tr>
		                    <tr>
		                        <td>
		                            <label>部署名<br>
		                                <input type="text" name="department_name">
		                            </label>
		                        </td>
		                        <td>
		                            <label>電話番号<br>
		                                <input type="text" name="company_phone">
		                            </label>
		                        </td>
		                    </tr>
		                    <tr>
		                        <td>
		                            <label>役職名<br>
		                                <input type="text" name="position_name">
		                            </label>
		                        </td>
		                        <td>
		                            <label>メールアドレス<br>
		                                <input type="text" name="email">
		                            </label>
		                        </td>
		                    </tr>
		                    <tr>
		                        <td colspan="2">
		                            <label>備考<br>
		                                <textarea name="remarks"></textarea>
		                            </label>
		                        </td>
		                    </tr>
		                    <tr>
		                        <td colspan="2">
		                            <label>
		                                <input type="checkbox" name="favorite"> お気に入り
		                            </label>
		                        </td>
		                    </tr>
		                    <tr>
		                        <td colspan="2">
		                            <div class="modal-button-container">
		                                <button type="submit" class="modal-button-submit">送信</button>
		                                <button type="reset" class="modal-button-reset">リセット</button>
		                            </div>
		                            <span id="error_message"></span>
		                        </td>
		                    </tr>
		                </table>
	            	</form>
                <button class="close-button">閉じる</button>
            </div>
        </div>
        <button class="add-button">
            <span>＋</span>
        </button>
    </main>

    <!-- JavaScript -->
    <script type="module" src="${pageContext.request.contextPath}/js/home.js"></script>
    <script type="module" src="${pageContext.request.contextPath}/js/recently.js"></script>
    
</body>
</html>
