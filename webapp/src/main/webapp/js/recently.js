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

    // 初期カードデータ取得
    fetch(`${window.cardServletUrl}?action=list`) // window.cardServletUrl はグローバル変数
        .then(res => res.json())
        .then(data => {
            allCardsData = data;
            // 初期表示（必要ならここで表示）
            const sorted = sortCardsByDate(allCardsData);
            displayCards(sorted, cardList, showDetailModal);
        })
        .catch(error => {
            console.error('カードデータ取得エラー:', error);
            cardList.textContent = 'カードデータの取得に失敗しました。';
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
        if (isNaN(days) || days < 1) {
            alert('1以上の数字を入力してください。');
            return;
        }
        const filtered = filterCardsByRecentDays(allCardsData, days);
        const sorted = sortCardsByDate(filtered);
        displayCards(sorted, cardList, showDetailModal);
    });
});
