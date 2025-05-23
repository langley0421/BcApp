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

const SERVLET_BASE_URL = '/webapp/cardServlet'; // Added constant

document.addEventListener('DOMContentLoaded', () => {
    const dom = getDOMSelectors();     // DOM要素をまとめて取得
    // let allCardsData = [];             // 명刺データの全件保持用 // Removed

    // --- JSONファイルからデータを取得 --- // Removed fetch block
    // fetch('../JSON/data.json')
    //     .then(res => res.json())
    //     .then(data => {
    //         allCardsData = data;
    //         displayCards(allCardsData, dom.cardList, showDetailModal); // 初期表示
    //         setupCardFilters(dom, allCardsData);
    //     });
    loadInitialCards(dom); // Added call to loadInitialCards

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
    dom.resetButton.addEventListener('click', () => resetModalInputs());
    // 「送信」ボタンでカード追加処理
    dom.submitButton.addEventListener('click', () => {
        // Updated to use the new handleCardSubmission from utils.js
        // It now requires the modal element, a success callback, and a reset callback.
        handleCardSubmission(dom.modal, () => loadInitialCards(dom), () => {
            // The resetCallback in utils.js now expects the modal element.
            // However, the original reset button click is fine if dom.resetButton is correctly wired.
            // Let's assume dom.resetButton.click() correctly triggers resetModalInputs(dom.modal)
            // or directly resets the form. If resetModalInputs is preferred:
            // resetModalInputs(dom.modal); 
            dom.resetButton.click(); // This should trigger the existing reset logic for the modal.
        });
    });
    // モーダル外クリックでモーダルを閉じる
    setupModalCloseOnOutsideClick(dom.modal);
}


// ---カードの表示切り替え・検索などのイベント設定---
function setupCardFilters(dom) { // Removed cardsData parameter
    // 「ホーム」リンクで全カードを表示
    dom.homeLink.addEventListener('click', () => {
        toggleViewMode('search'); // Assuming this function correctly sets the view
        loadInitialCards(dom);
    });

    // 「お気に入り」リンクでお気に入りカードのみ表示
    dom.favoriteLink.addEventListener('click', () => {
        toggleViewMode('search'); // Or a different view if applicable for favorites
        loadFavoriteCards(dom);
    });

    // 検索ボタンでフィルターを適用
    dom.searchButton.addEventListener('click', () => {
        toggleViewMode('search'); // Ensure search input is visible
        performSearch(dom);
    });
}

// Added loadFavoriteCards function
async function loadFavoriteCards(dom) {
    try {
        const response = await fetch(`${SERVLET_BASE_URL}?action=list&favorite=true`);
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        const cards = await response.json();
        // Assuming servlet returns an array directly, or an object with {success: false, message: ...} on error
        if (Array.isArray(cards)) {
            displayCards(cards, dom.cardList, showDetailModal);
        } else if (cards.success === false) {
             console.error('Error from servlet (favorites):', cards.message);
             dom.cardList.innerHTML = '<p>Error loading favorite cards.</p>';
        } else {
            console.error('Unexpected response format for favorites:', cards);
            dom.cardList.innerHTML = '<p>Error loading favorite cards: Unexpected format.</p>';
        }
    } catch (error) {
        console.error('Failed to load favorite cards:', error);
        dom.cardList.innerHTML = '<p>Failed to load favorite cards.</p>';
    }
}

// Added performSearch function
async function performSearch(dom) {
    const query = dom.searchInput.value.trim();
    // Optional: if query is empty, load all cards or show message
    if (!query) {
        // loadInitialCards(dom); // Or display a message "Please enter a search term"
        // For now, let server handle empty search term if it's designed to return all cards
        // Or, prevent empty search:
        // dom.cardList.innerHTML = '<p>Please enter a search term.</p>';
        // return; 
    }
    try {
        const response = await fetch(`${SERVLET_BASE_URL}?action=list&searchTerm=${encodeURIComponent(query)}`);
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        const cards = await response.json();
        // Assuming servlet returns an array directly, or an object with {success: false, message: ...} on error
        if (Array.isArray(cards)) {
            displayCards(cards, dom.cardList, showDetailModal);
        } else if (cards.success === false) {
             console.error('Error from servlet (search):', cards.message);
             dom.cardList.innerHTML = '<p>Error performing search.</p>';
        } else {
            console.error('Unexpected response format for search:', cards);
            dom.cardList.innerHTML = '<p>Error performing search: Unexpected format.</p>';
        }
    } catch (error) {
        console.error('Failed to perform search:', error);
        dom.cardList.innerHTML = '<p>Search failed.</p>';
    }
}

// Added loadInitialCards function
async function loadInitialCards(dom) {
    try {
        const response = await fetch(`${SERVLET_BASE_URL}?action=list`); // Assuming CardServlet is mapped to /cardServlet
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        const cards = await response.json(); // This is an array of CardInfo directly, or an error object
        // Our CardServlet returns array directly for list action if successful,
        // or {success:false, message:...} for errors (as handled in CardServlet.java for unknown action).
        // For list action itself, CardDAO returns an empty list on SQL error, not a success:false object.
        // So, we primarily expect an array here.
        if (Array.isArray(cards)) {
            displayCards(cards, dom.cardList, showDetailModal);
            // The prompt for this step was to update setupCardFilters.
            // If setupCardFilters itself needs to be called *after* initial load,
            // that would be a change to this function. But the prompt says:
            // "Call setupCardFilters(dom) in DOMContentLoaded"
            // "It no longer needs allCardsData"
            // This implies setupCardFilters just sets up listeners and doesn't need the data itself.
        } else if (cards.success === false && cards.message) { // Handling explicit error response from servlet
             console.error('Error from servlet (initial load):', cards.message);
             dom.cardList.innerHTML = `<p>Error loading cards: ${cards.message}.</p>`;
        } else {
            // Unrecognized response structure from servlet for initial load
            console.error('Unrecognized response from servlet (initial load):', cards);
            dom.cardList.innerHTML = '<p>Error loading cards: Unrecognized response. Check console.</p>';
        }
    } catch (error) {
        console.error('Failed to load initial cards:', error);
        dom.cardList.innerHTML = '<p>Failed to load cards. Please try again later.</p>';
    }
}
