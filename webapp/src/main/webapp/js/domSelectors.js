// ---DOMセレクター---
//後から追加した分を移動しないといけない

export function getDOMSelectors() {
    return {
        darkModeToggle: document.getElementById('dark-mode-toggle'),
        cardList: document.getElementById('card-list'),
        dayFilterInput: document.getElementById('day-filter'),
        applyFilterButton: document.getElementById('apply-filter'),
        navLinks: document.querySelectorAll('.sidebar .nav-link'),
        addButton: document.querySelector('.add-button'),
        modal: document.querySelector('.modal'),
        editModal:document.querySelector('.edit-modal'),
        closeButton: document.querySelector('.close-button'),
        resetButton: document.querySelector('.modal-button-reset'),
        submitButton: document.querySelector('.modal-button-submit'),
        homeLink: document.querySelector('.nav-link[href="#section1"]'),
        favoriteLink: document.querySelector('.nav-link[href="#section2"]'),
        recentlyLink: document.querySelector('.nav-link[href="#section3"]'),
        searchInput: document.querySelector('.search-input'),
        searchButton: document.querySelector('.search-button')
    };
}
