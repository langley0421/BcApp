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

document.addEventListener('DOMContentLoaded', () => {
    const dom = getDOMSelectors();     // DOM要素をまとめて取得
    let allCardsData = [];             // 名刺データの全件保持用

    // --- JSONファイルからデータを取得 ---
    fetch('../JSON/data.json')
        .then(res => res.json())
        .then(data => {
            allCardsData = data;
            displayCards(allCardsData, dom.cardList, showDetailModal); // 初期表示
            setupCardFilters(dom, allCardsData);
        });

    // --- 各イベントの初期設定 ---
    setupUIEvents(dom);                
    setupModalEvents(dom);        
    setupCardFilters(dom, allCardsData);    
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
    dom.resetButton.addEventListener('click', () => resetModalInputs());
    // 「送信」ボタンでカード追加処理
    dom.submitButton.addEventListener('click', () => {
        handleCardSubmission(dom.cardList, dom.modal, showDetailModal, () => dom.resetButton.click());
    });
    // モーダル外クリックでモーダルを閉じる
    setupModalCloseOnOutsideClick(dom.modal);
}


// ---カードの表示切り替え・検索などのイベント設定---
function setupCardFilters(dom, allCardsData) {
    // 「ホーム」リンクで全カードを表示
    dom.homeLink.addEventListener('click', () => {
        toggleViewMode('search');
        displayCards(allCardsData, dom.cardList, showDetailModal);
    });

    // 「お気に入り」リンクでお気に入りカードのみ表示
    dom.favoriteLink.addEventListener('click', () => {
        const favs = allCardsData.filter(card => card.favorite);
        displayCards(favs, dom.cardList, showDetailModal);
    });

    // 検索ボタンでフィルターを適用
    dom.searchButton.addEventListener('click', () => {
        const query = dom.searchInput.value.trim().toLowerCase();
        const filtered = allCardsData.filter(card =>
            card.name.toLowerCase().includes(query) ||
            card.company.toLowerCase().includes(query) ||
            card.department.toLowerCase().includes(query) ||
            card.remarks.toLowerCase().includes(query)
        );
        displayCards(filtered, dom.cardList, showDetailModal);
    });
}
