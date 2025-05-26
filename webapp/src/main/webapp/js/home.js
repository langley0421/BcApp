import {
    toggleDarkMode,                      
    setActiveLink,                       
    openModal, closeModal,              
    resetModalInputs,                   
    setupModalCloseOnOutsideClick,      
    handleCardSubmission,               
    displayCards,                       
    toggleViewMode,                     
    showDetailModal                     
} from './utils.js';

import { 
    getDOMSelectors                    
} from './domSelectors.js';

// const SERVLET_BASE_URL = '/webapp/cardServlet'; // 削除: グローバル変数 window.cardServletUrl を使用します

document.addEventListener('DOMContentLoaded', () => {
    const dom = getDOMSelectors();     // DOM要素をまとめて取得
    // let allCardsData = [];             // 名刺データの全件保持用 // 削除済み

    // --- JSONファイルからデータを取得 --- // 削除済みフェッチブロック
    // fetch('../JSON/data.json')
    //     .then(res => res.json())
    //     .then(data => {
    //         allCardsData = data;
    //         displayCards(allCardsData, dom.cardList, showDetailModal); // 初期表示
    //         setupCardFilters(dom, allCardsData);
    //     });
    loadInitialCards(dom); // 初期カード読み込み関数の呼び出しを追加

    // --- 各イベントの初期設定 ---
    setupUIEvents(dom);                
    setupModalEvents(dom);        
    setupCardFilters(dom); // Re-enabled and removed allCardsData
});


// ---UI全体のイベント設定---
function setupUIEvents(dom) {
    // ダークモード切り替え
    dom.darkModeToggle.addEventListener('click', toggleDarkMode);
    // サイドバーリンクのアクティブ状態管理
    setActiveLink(dom.navLinks);
}


// ---モーダル操作のイベント設定---
function setupModalEvents(dom) {
    // 「追加」ボタンでモーダルを開く
    dom.addButton.addEventListener('click', () => openModal(dom.modal));
    // 「閉じる」ボタンでモーダルを閉じる
    dom.closeButton.addEventListener('click', () => closeModal(dom.modal));
    // 「リセット」ボタンでフォーム初期化
    dom.resetButton.addEventListener('click', () => resetModalInputs(dom.modal)); // dom.modalを引数として渡す
    // 「送信」ボタンでカード追加処理
    dom.submitButton.addEventListener('click', () => {
        // utils.jsの新しいhandleCardSubmissionを使用するように更新
        // モーダル要素、成功時のコールバック、リセット時のコールバックが必要
        handleCardSubmission(dom.modal, () => loadInitialCards(dom), () => {
            // utils.jsのresetCallbackはモーダル要素を期待するようになった
            // resetModalInputsを直接呼び出す方が明確かもしれない
            resetModalInputs(dom.modal); 
            // dom.resetButton.click(); // これはモーダルの既存のリセットロジックをトリガーするはず
        });
    });
    // モーダル外クリックでモーダルを閉じる
    setupModalCloseOnOutsideClick(dom.modal);
}


// ---カードの表示切り替え・検索などのイベント設定---
function setupCardFilters(dom) { // cardsDataパラメータを削除
    // 「ホーム」リンクで全カードを表示
    dom.homeLink.addEventListener('click', () => {
        toggleViewMode('search'); // この関数がビューを正しく設定すると仮定
        loadInitialCards(dom);
    });

    // 「お気に入り」リンクでお気に入りカードのみ表示
    dom.favoriteLink.addEventListener('click', () => {
        toggleViewMode('search'); // またはお気に入りに適用可能な別のビュー
        loadFavoriteCards(dom);
    });

    // 検索ボタンでフィルターを適用
    dom.searchButton.addEventListener('click', () => {
        toggleViewMode('search'); // 検索入力が表示されるようにする
        performSearch(dom);
    });
}

// お気に入りカード読み込み関数 (async/await を .then().catch() に変更)
function loadFavoriteCards(dom) {
    fetch(`${window.cardServletUrl}?action=list&favorite=true`) // グローバル変数を使用
        .then(response => {
            if (!response.ok) {
                // HTTPエラーの場合、詳細なエラーメッセージを投げる
                return response.json().then(errData => {
                    throw new Error(`HTTPエラー! ステータス: ${response.status}, メッセージ: ${errData.message || '不明なエラー'}`);
                }).catch(() => {
                    // JSON解析エラーまたはその他のネットワークエラー
                    throw new Error(`HTTPエラー! ステータス: ${response.status}`);
                });
            }
            return response.json();
        })
        .then(cards => {
            // サーブレットが直接配列を返すか、エラー時に{success: false, message: ...}を返すと仮定
            if (Array.isArray(cards)) {
                displayCards(cards, dom.cardList, showDetailModal);
            } else if (cards && cards.success === false) { // cardsオブジェクトが存在し、successがfalseの場合
                 console.error('サーブレットからのエラー (お気に入り):', cards.message);
                 dom.cardList.innerHTML = '<p>お気に入りカードの読み込みに失敗しました。</p>';
            } else {
                console.error('お気に入りの予期しないレスポンス形式:', cards);
                dom.cardList.innerHTML = '<p>お気に入りカードの読み込みに失敗しました: 予期しない形式です。</p>';
            }
        })
        .catch(error => {
            console.error('お気に入りカードの読み込みに失敗しました:', error);
            dom.cardList.innerHTML = '<p>お気に入りカードの読み込みに失敗しました。</p>';
        });
}

// 検索実行関数 (async/await を .then().catch() に変更)
function performSearch(dom) {
    const query = dom.searchInput.value.trim();
    // オプション: クエリが空の場合、全カードを読み込むかメッセージを表示
    if (!query) {
        // loadInitialCards(dom); // または "検索語を入力してください" というメッセージを表示
        // 現時点では、サーバーが空の検索語を全カードを返すように設計されている場合はサーバーに処理させる
        // または、空の検索を防止する:
        // dom.cardList.innerHTML = '<p>検索語を入力してください。</p>';
        // return; 
    }
    fetch(`${window.cardServletUrl}?action=list&searchTerm=${encodeURIComponent(query)}`) // グローバル変数を使用
        .then(response => {
            if (!response.ok) {
                return response.json().then(errData => {
                    throw new Error(`HTTPエラー! ステータス: ${response.status}, メッセージ: ${errData.message || '不明なエラー'}`);
                }).catch(() => {
                    throw new Error(`HTTPエラー! ステータス: ${response.status}`);
                });
            }
            return response.json();
        })
        .then(cards => {
            // サーブレットが直接配列を返すか、エラー時に{success: false, message: ...}を返すと仮定
            if (Array.isArray(cards)) {
                displayCards(cards, dom.cardList, showDetailModal);
            } else if (cards && cards.success === false) {
                 console.error('サーブレットからのエラー (検索):', cards.message);
                 dom.cardList.innerHTML = '<p>検索の実行に失敗しました。</p>';
            } else {
                console.error('検索の予期しないレスポンス形式:', cards);
                dom.cardList.innerHTML = '<p>検索の実行に失敗しました: 予期しない形式です。</p>';
            }
        })
        .catch(error => {
            console.error('検索の実行に失敗しました:', error);
            dom.cardList.innerHTML = '<p>検索に失敗しました。</p>';
        });
}

// 初期カード読み込み関数 (async/await を .then().catch() に変更)
function loadInitialCards(dom) {
    fetch(`${window.cardServletUrl}?action=list`) // グローバル変数を使用
        .then(response => {
            if (!response.ok) {
                return response.json().then(errData => {
                    throw new Error(`HTTPエラー! ステータス: ${response.status}, メッセージ: ${errData.message || '不明なエラー'}`);
                }).catch(() => {
                    throw new Error(`HTTPエラー! ステータス: ${response.status}`);
                });
            }
            return response.json();
        })
        .then(cards => { // これはCardInfoの配列直接、またはエラーオブジェクト
            // CardServletは成功時にはlistアクションで配列を直接返す
            // またはエラー時には{success:false, message:...} (CardServlet.javaで未知のアクションに対して処理されるように)
            // listアクション自体については、CardDAOはSQLエラー時に空のリストを返し、success:falseオブジェクトは返さない
            // そのため、ここでは主に配列を期待する
            if (Array.isArray(cards)) {
                displayCards(cards, dom.cardList, showDetailModal);
                // このステップのプロンプトはsetupCardFiltersを更新することだった
                // setupCardFilters自体が初期読み込み後に呼び出される必要がある場合、
                // それはこの関数の変更になる。しかしプロンプトにはこう書かれている：
                // "DOMContentLoadedでsetupCardFilters(dom)を呼び出す"
                // "allCardsDataはもう必要ない"
                // これはsetupCardFiltersがリスナーを設定するだけで、データ自体は必要ないことを意味する
            } else if (cards && cards.success === false && cards.message) { // サーブレットからの明示的なエラーレスポンスを処理
                 console.error('サーブレットからのエラー (初期読み込み):', cards.message);
                 dom.cardList.innerHTML = `<p>カードの読み込みエラー: ${cards.message}.</p>`;
            } else {
                // サーブレットからの初期読み込みでの未認識のレスポンス構造
                console.error('サーブレットからの未認識のレスポンス (初期読み込み):', cards);
                dom.cardList.innerHTML = '<p>カードの読み込みエラー: 未認識のレスポンス。コンソールを確認してください。</p>';
            }
        })
        .catch(error => {
            console.error('初期カードの読み込みに失敗しました:', error);
            dom.cardList.innerHTML = '<p>カードの読み込みに失敗しました。後でもう一度お試しください。</p>';
        });
}
