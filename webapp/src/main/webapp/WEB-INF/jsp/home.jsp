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
    <script type="text/javascript">
        window.cardServletUrl = "${pageContext.request.contextPath}/cardServlet";
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
            <li><a href="#section5" class="nav-link">アカウント設定</a></li>
        </ul>
    </aside>

    <main class="content">
    <%-- <%
        String message = (String) session.getAttribute("message");
        if (message != null) {
            session.removeAttribute("message");
    %>
            <p style="color:green; text-align:center;"><%= message %></p>
    <%
        }
        String error = (String) session.getAttribute("error");
        if (error != null) {
            session.removeAttribute("error");
    %>
            <p style="color:red; text-align:center;"><%= error %></p>
    <%
        }
    %> --%>
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
        <div id="edit-modal" class="modal hidden">
		    <div class="modal-content">
		        <h2>編集フォーム</h2>
		        <form action="${pageContext.request.contextPath}/cardEdit" method="post">
		            <table class="modal-table">
		                <input type="hidden" name="card_id" value="${card.cardId}">
		                <tr>
		                    <td>
		                        <label>会社名<br>
		                            <input type="text" name="company_name" value="${card.companyName}">
		                        </label>
		                    </td>
		                    <td>
		                        <label>郵便番号<br>
		                            <input type="text" name="company_zipcode" value="${card.companyZipcode}">
		                        </label>
		                    </td>
		                </tr>
		                <tr>
		                    <td>
		                        <label>氏名（※必須）<br>
		                            <input type="text" name="name" value="${card.name}">
		                        </label>
		                    </td>
		                    <td>
		                        <label>住所<br>
		                            <input type="text" name="company_address" value="${card.companyAddress}">
		                        </label>
		                    </td>
		                </tr>
		                <tr>
		                    <td>
		                        <label>部署名<br>
		                            <input type="text" name="department_name" value="${card.departmentName}">
		                        </label>
		                    </td>
		                    <td>
		                        <label>電話番号<br>
		                            <input type="text" name="company_phone" value="${card.companyPhone}">
		                        </label>
		                    </td>
		                </tr>
		                <tr>
		                    <td>
		                        <label>役職名<br>
		                            <input type="text" name="position_name" value="${card.positionName}">
		                        </label>
		                    </td>
		                    <td>
		                        <label>メールアドレス<br>
		                            <input type="text" name="email" value="${card.email}">
		                        </label>
		                    </td>
		                </tr>
		                <tr>
		                    <td colspan="2">
		                        <label>備考<br>
		                            <textarea name="remarks">${card.remarks}</textarea>
		                        </label>
		                    </td>
		                </tr>
		                <tr>
		                    <td colspan="2">
		                        <label>
		                            <input type="checkbox" name="favorite" ${card.favorite ? "checked" : ""}> お気に入り
		                        </label>
		                    </td>
		                </tr>
		                <tr>
		                    <td colspan="2">
		                        <div class="modal-button-container">
		                            <button type="submit" class="modal-button-submit">保存</button>
		                            <button type="reset" class="modal-button-reset">リセット</button>
		                        </div>
		                        <span id="edit_error_message"></span>
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
    <script>
    // ページ読み込み時にデータ取得
    window.addEventListener('DOMContentLoaded', () => {
        fetch('cardServlet')
            .then(response => {
                if (!response.ok) {
                    throw new Error('ネットワークエラー: ' + response.status);
                }
                return response.json();
            })
            .then(data => {
                console.log('取得したカード一覧:', data);
                // 必要があればここでDOMに反映も可能
            })
            .catch(error => {
                console.error('取得エラー:', error);
            });
    });
</script>
    
    
</body>
</html>
