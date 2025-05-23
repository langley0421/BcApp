import {
    displayCards,              
    showDetailModal,           
    toggleViewMode,            
    sortCardsByDate,           
    filterCardsByRecentDays    
} from './utils.js';

import {
    getDOMSelectors           
} from './domSelectors.js';

document.addEventListener('DOMContentLoaded', () => {
    // 必要なDOM要素を取得
    const { recentlyLink, cardList, dayFilterInput, applyFilterButton } = getDOMSelectors();
    // 全てのカードデータを格納する変数
    let allCardsData = [];

    fetch('../JSON/data.json')
        .then(res => res.json())
        .then(data => {
            allCardsData = data;
        });

    // ---「最近追加」リンククリック時の処理---
    recentlyLink.addEventListener('click', () => {
        toggleViewMode('recent'); 
        const sorted = sortCardsByDate(allCardsData); 
        displayCards(sorted, cardList, showDetailModal); 
    });

    // ---「日数でフィルター」ボタンのクリック処理---
    applyFilterButton.addEventListener('click', () => {
        const days = parseInt(dayFilterInput.value, 10); 
        const filtered = filterCardsByRecentDays(allCardsData, days); 
        const sorted = sortCardsByDate(filtered); 
        displayCards(sorted, cardList, showDetailModal); 
    });
});
