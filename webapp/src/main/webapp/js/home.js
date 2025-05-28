import {
    toggleDarkMode,                      
    setActiveLink,                       
    openModal, closeModal,              
    resetModalInputs,                   
    setupModalCloseOnOutsideClick,      
    displayCards,                       
    toggleViewMode,                     
    showDetailModal,
    createCardElement             
} from './utils.js';

import { 
    getDOMSelectors                    
} from './domSelectors.js';

document.addEventListener('DOMContentLoaded', () => {
    const dom = getDOMSelectors();     // DOM要素をまとめて取得
    loadInitialCards(dom); // 初期カード読み込み関数の呼び出しを追加

    // --- 各イベントの初期設定 ---
    setupUIEvents(dom);                
    setupModalEvents(dom);        
    setupCardFilters(dom); 
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
    dom.resetButton.addEventListener('click', () => resetModalInputs(dom.modal));
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

// 検索実行関数
function onCardClick(cardData) {
    console.log('カードがクリックされました:', cardData);
    showDetailModal(cardData); // モーダル表示関数にカード情報を渡す
    // 例: 詳細表示など
}

window.performSearch = function () {
    const keyword = document.getElementById('searchInput').value;
    const cardList = document.getElementById('card-list');

    cardList.innerHTML = '';

    fetch('/webapp/searchCard?keyword=' + encodeURIComponent(keyword))
        .then(response => {
            if (!response.ok) {
                throw new Error('HTTPエラー: ' + response.status);
            }
            return response.json();
        })
        .then(data => {
            console.log('受け取ったデータ:', data);

            if (data.length === 0) {
                cardList.textContent = '該当する名刺はありません。';
                return;
            }

            data.forEach(cardData => {
                const cardElement = createCardElement(cardData, onCardClick);
                cardList.appendChild(cardElement);
            });
        })
        .catch(error => {
            console.error('検索エラー:', error);
            cardList.textContent = '検索中にエラーが発生しました。';
        });
};


// 初期カード読み込み関数
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
        .then(cards => { 
            if (Array.isArray(cards)) {
                displayCards(cards, dom.cardList, showDetailModal);
                
            } else if (cards && cards.success === false && cards.message) { 
                 console.error('サーブレットからのエラー :', cards.message);
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
