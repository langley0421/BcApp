//モジュール化したJavaScriptコード



// ---名刺データから詳細表示用のHTML文字列を生成する関数---
export function generateCardDetailsHTML(data) {
    // Using fields from CardInfo DTO
    const createdDate = data.created_date ? new Date(data.created_date).toLocaleString() : 'N/A';
    const updatedDate = data.update_date ? new Date(data.update_date).toLocaleString() : 'N/A';

    return `
        <h3>${data.name || 'N/A'}</h3>
        <p><strong>会社名:</strong> ${data.company_name || 'N/A'}</p>
        <p><strong>部署:</strong> ${data.department_name || 'N/A'}</p>
        <p><strong>役職:</strong> ${data.position_name || 'N/A'}</p>
        <p><strong>Email:</strong> ${data.email || 'N/A'}</p>
        <p><strong>電話番号 (会社):</strong> ${data.company_phone || 'N/A'}</p>
        <p><strong>住所 (会社):</strong> ${data.company_address || 'N/A'}</p>
        <p><strong>郵便番号 (会社):</strong> ${data.company_zipcode || 'N/A'}</p>
        <p><strong>備考:</strong> ${data.remarks || 'N/A'}</p>
        <p><strong>お気に入り:</strong> ${data.favorite ? 'はい' : 'いいえ'}</p>
        <p><strong>作成日時:</strong> ${createdDate}</p>
        <p><strong>更新日時:</strong> ${updatedDate}</p>
    `;
}


// ---単一の名刺カードを生成する関数。クリック時のコールバックも---
export function createCardElement(cardData, onCardClick) {
    // cardData is expected to be CardInfo DTO
    const card = document.createElement('div');
    card.classList.add('card');
    card.innerHTML = `
        <div class="card-header">
            <span class="card-name">${cardData.name || 'N/A'}</span>
            <span class="card-favorite">${cardData.favorite ? '★' : '☆'}</span>
        </div>
        <p class="card-company">会社: ${cardData.company_name || 'N/A'}</p>
        <p class="card-department">部署: ${cardData.department_name || 'N/A'}</p>
        <p class="card-position">役職: ${cardData.position_name || 'N/A'}</p>
    `;
    // Storing the full card data object for easier access later, especially for showDetailModal if it needs it
    card.dataset.cardId = cardData.card_id; // Use card_id
    // The generateCardDetailsHTML will be called by showDetailModal, 
    // or we can pre-generate and store it if the existing showDetailModal structure in home.js expects it.
    // For now, let's assume onCardClick will handle calling showDetailModal which then generates details.
    // If showDetailModal in home.js expects details on dataset.details, then uncomment next line:
    // card.dataset.details = generateCardDetailsHTML(cardData);


    card.addEventListener('click', () => {
        // Pass the full cardData to the click handler.
        // The original onCardClick(cardData, card) might be from a different structure.
        // The showDetailModal in home.js expects a card *element* or card *data*.
        // If using showDetailModal from home.js, it might expect card.dataset.details
        // Let's ensure it's compatible or adjust.
        // The current home.js showDetailModal expects a card element with dataset.details.
        // So, we need to set card.dataset.details here.
        card.dataset.details = generateCardDetailsHTML(cardData);
        onCardClick(card); // Pass the card element itself, which has .dataset.details
    });

    return card;
}


// ---名刺一覧を表示する。空なら「該当なし」のメッセージを表示---
export function displayCards(cards, cardList, onCardClick) {
    cardList.innerHTML = '';

    if (!Array.isArray(cards) || cards.length === 0) { // Added !Array.isArray(cards) check
        const noResultMessage = document.createElement('p');
        noResultMessage.textContent = '該当する名刺がありません。';
        noResultMessage.classList.add('no-result-message');
        cardList.appendChild(noResultMessage);
        return;
    }

    cards.forEach(cardData => {
        const card = createCardElement(cardData, onCardClick);
        cardList.appendChild(card);
    });
}


// ---「検索モード」「最近追加モード」など、表示切替を行う関数---
export function toggleViewMode(mode) {
    const searchBar = document.querySelector('.search-bar');
    const recentFilter = document.getElementById('recent-filter');

    switch (mode) {
        case 'recent':
            searchBar.classList.add('hidden');
            recentFilter.classList.remove('hidden');
            break;
        case 'search':
            searchBar.classList.remove('hidden');
            recentFilter.classList.add('hidden');
            break;
        case 'none':
        default:
            searchBar.classList.add('hidden');
            recentFilter.classList.add('hidden');
            break;
    }
}


// ---名刺詳細をモーダルで表示し、編集・削除・お気に入り・閉じるボタンを追加する関数---
export function showDetailModal(cardData, cardElement) { // cardElement is the card in the main list
    const detailModal = document.createElement('div');
    detailModal.classList.add('modal', 'show'); // 'show' class makes it visible
    detailModal.id = 'dynamicDetailModal'; // Add an ID for easier removal if needed

    // Generate details HTML using the already modified generateCardDetailsHTML
    const detailsHTML = generateCardDetailsHTML(cardData);

    detailModal.innerHTML = `
        <div class="modal-contents">
            ${detailsHTML}
            <button class="edit-button">編集</button>
            <button class="delete-button">削除</button>
            <button class="favorite-button">${cardData.favorite ? '★ お気に入り' : '☆ お気に入り'}</button>
            <button class="close-button">閉じる</button>
        </div>
    `;
    document.body.appendChild(detailModal);

    // Edit button functionality
    detailModal.querySelector('.edit-button').addEventListener('click', () => {
        const mainModal = document.getElementById('modal'); // Changed to 'modal'
        if (!mainModal) {
            console.error('Main add/edit modal not found!');
            return;
        }

        // Populate main modal
        mainModal.querySelector('input[name="card_id"]').value = cardData.card_id;
        mainModal.querySelector('input[name="name"]').value = cardData.name || '';
        mainModal.querySelector('input[name="email"]').value = cardData.email || '';
        mainModal.querySelector('textarea[name="remarks"]').value = cardData.remarks || '';
        mainModal.querySelector('input[name="favorite"]').checked = cardData.favorite;
        mainModal.querySelector('input[name="company_name"]').value = cardData.company_name || '';
        mainModal.querySelector('input[name="company_zipcode"]').value = cardData.company_zipcode || '';
        mainModal.querySelector('input[name="company_address"]').value = cardData.company_address || '';
        mainModal.querySelector('input[name="company_phone"]').value = cardData.company_phone || '';
        mainModal.querySelector('input[name="department_name"]').value = cardData.department_name || '';
        mainModal.querySelector('input[name="position_name"]').value = cardData.position_name || '';

        openModal(mainModal); // openModal is imported/available from utils.js
        document.body.removeChild(detailModal); // Close/remove detail modal
    });

    // Delete button functionality
    detailModal.querySelector('.delete-button').addEventListener('click', async () => {
        if (confirm('この名刺を削除してもよろしいですか？')) {
            const params = new URLSearchParams();
            params.append('action', 'delete');
            params.append('card_id', cardData.card_id);

            try {
                const response = await fetch(cardServletUrl, { // Path updated
                    method: 'POST',
                    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                    body: params
                });
                const result = await response.json();
                if (result.success) {
                    if(cardElement) cardElement.remove(); // Remove card from main list
                    document.body.removeChild(detailModal);
                    alert('名刺が削除されました。');
                    // Optionally, refresh the list: loadInitialCards(getDOMSelectors()); if global refresh needed
                } else {
                    alert('削除に失敗しました: ' + (result.message || '理由不明'));
                }
            } catch (error) {
                console.error('Error deleting card:', error);
                alert('削除中にエラーが発生しました: ' + error.message);
            }
        }
    });

    // Favorite button functionality
    const favoriteButton = detailModal.querySelector('.favorite-button');
    favoriteButton.addEventListener('click', async () => {
        const newFavoriteStatus = !cardData.favorite;
        const params = new URLSearchParams();
        params.append('action', 'toggleFavorite');
        params.append('card_id', cardData.card_id);
        params.append('isFavorite', newFavoriteStatus.toString());

        try {
            const response = await fetch(cardServletUrl, { // Path updated
                method: 'POST',
                headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                body: params
            });
            const result = await response.json();
            if (result.success) {
                cardData.favorite = newFavoriteStatus; // Update local data
                favoriteButton.textContent = newFavoriteStatus ? '★ お気に入り' : '☆ お気に入り';
                // To update favorite star on the card in the main list:
                if (cardElement) {
                    const favIndicator = cardElement.querySelector('.card-favorite'); // Assuming class .card-favorite on the star
                    if (favIndicator) {
                        favIndicator.textContent = newFavoriteStatus ? '★' : '☆';
                    }
                }
                 alert('お気に入り状態が更新されました。');
            } else {
                alert('お気に入り状態の更新に失敗しました: ' + (result.message || '理由不明'));
            }
        } catch (error) {
            console.error('Error toggling favorite:', error);
            alert('お気に入り状態の更新中にエラーが発生しました: ' + error.message);
        }
    });

    // Close button for detail modal
    detailModal.querySelector('.close-button').addEventListener('click', () => {
        document.body.removeChild(detailModal);
    });

    // Optional: close detail modal if clicked outside of modal-contents
    detailModal.addEventListener('click', (event) => {
        if (event.target === detailModal) { // Clicked on the backdrop
            document.body.removeChild(detailModal);
        }
    });
}


// ---モーダルを「表示」状態にする関数---
export function openModal(modal) {
    modal.classList.add('show');
    modal.classList.remove('hidden');
}


// ---モーダルを「非表示」状態にする関数---
export function closeModal(modal) {
    modal.classList.remove('show');
    modal.classList.add('hidden');
}


// ---モーダルの外側をクリックした時にモーダルを閉じる処理---
export function setupModalCloseOnOutsideClick(modal) {
    modal.addEventListener('click', (event) => {
        if (event.target === modal) {
            closeModal(modal);
        }
    });
}


// ---モーダルフォーム内のすべてのinputをリセットする関数---
export function resetModalInputs(modalElement) { // Changed to accept modalElement
    if (!modalElement) return;

    const inputs = modalElement.querySelectorAll('input[type="text"], input[type="email"], input[type="hidden"], textarea');
    inputs.forEach(input => input.value = '');

    const checkboxes = modalElement.querySelectorAll('input[type="checkbox"]');
    checkboxes.forEach(checkbox => checkbox.checked = false);
}


// ---ダークモードのON/OFFを切り替え、アイコンも更新する関数---
export function toggleDarkMode() {
    const iconContainer = document.getElementById('icon'); // アイコンのコンテナを取得
    document.body.classList.toggle('dark-mode');

    // アイコンを切り替える
    if (document.body.classList.contains('dark-mode')) {
        iconContainer.innerHTML = `<svg class="icon-moon" fill="currentColor" width="24" height="24" viewBox="0 0 24 24"><path d="M9.37,5.51C9.19,6.15,9.1,6.82,9.1,7.5c0,4.08,3.32,7.4,7.4,7.4c0.68,0,1.35-0.09,1.99-0.27C17.45,17.19,14.93,19,12,19 c-3.86,0-7-3.14-7-7C5,9.07,6.81,6.55,9.37,5.51z M12,3c-4.97,0-9,4.03-9,9s4.03,9,9,9s9-4.03,9-9c0-0.46-0.04-0.92-0.1-1.36 c-0.98,1.37-2.58,2.26-4.4,2.26c-2.98,0-5.4-2.42-5.4-5.4c0-1.81,0.89-3.42,2.26-4.4C12.92,3.04,12.46,3,12,3L12,3z"></path></svg>`;
    } else {
        iconContainer.innerHTML = `<svg class="icon-sun" fill="currentColor" width="24" height="24" viewBox="0 0 24 24"><path d="M12,9c1.65,0,3,1.35,3,3s-1.35,3-3,3s-3-1.35-3-3S10.35,9,12,9 M12,7c-2.76,0-5,2.24-5,5s2.24,5,5,5s5-2.24,5-5 S14.76,7,12,7L12,7z M2,13l2,0c0.55,0,1-0.45,1-1s-0.45-1-1-1l-2,0c-0.55,0-1,0.45-1,1S1.45,13,2,13z M20,13l2,0c0.55,0,1-0.45,1-1 s-0.45-1-1-1l-2,0c-0.55,0-1,0.45-1,1S19.45,13,20,13z M11,2v2c0,0.55,0.45,1,1,1s1-0.45,1-1V2c0-0.55-0.45-1-1-1S11,1.45,11,2z M11,20v2c0,0.55,0.45,1,1,1s1-0.45,1-1v-2c0-0.55-0.45-1-1-1C11.45,19,11,19.45,11,20z M5.99,4.58c-0.39-0.39-1.03-0.39-1.41,0 c-0.39,0.39-0.39,1.03,0,1.41l1.06,1.06c0.39,0.39,1.03,0.39,1.41,0s0.39-1.03,0-1.41L5.99,4.58z M18.36,16.95 c-0.39-0.39-1.03-0.39-1.41,0c-0.39,0.39-0.39,1.03,0,1.41l1.06,1.06c0.39,0.39,1.03,0.39,1.41,0c0.39-0.39,0.39-1.03,0-1.41 L18.36,16.95z M19.42,5.99c0.39-0.39,0.39-1.03,0-1.41c-0.39-0.39-1.03-0.39-1.41,0l-1.06,1.06c-0.39,0.39-0.39,1.03,0,1.41 s1.03,0.39,1.41,0L19.42,5.99z M7.05,18.36c0.39-0.39,0.39-1.03,0-1.41c-0.39-0.39-1.03-0.39-1.41,0l-1.06,1.06 c-0.39,0.39-0.39,1.03,0,1.41s1.03,0.39,1.41,0L7.05,18.36z"></path></svg>`;
    }
}


// ---サイドバーのアクティブリンクを設定する関数---
export function setActiveLink(navLinks, activeClass = 'active', defaultHref = '#section1') {
    // 初期アクティブ設定
    const defaultLink = [...navLinks].find(link => link.getAttribute('href') === defaultHref);
    if (defaultLink) {
        defaultLink.classList.add(activeClass);
    }

    navLinks.forEach(link => {
        link.addEventListener('click', () => {
            navLinks.forEach(nav => nav.classList.remove(activeClass));
            link.classList.add(activeClass);
        });
    });
}


// ---フォームからカードデータを取得する関数---
export function getCardFormData() {
    return {
        company: document.querySelector('input[name="company"]').value,
        name: document.querySelector('input[name="name"]').value,
        zipcode: document.querySelector('input[name="zipcode"]').value,
        address: document.querySelector('input[name="address"]').value,
        department: document.querySelector('input[name="department"]').value,
        phone: document.querySelector('input[name="phone"]').value,
        position: document.querySelector('input[name="position"]').value,
        email: document.querySelector('input[name="email"]').value,
        remarks: document.querySelector('input[name="remarks"]').value,
        favorite: false
    };
}


// ---バリデーション付きでカードを追加する関数---
// export function handleCardSubmission(cardList, modal, showDetailModal, resetCallback) { // Old version
//     const cardData = getCardFormData();

//     if (!cardData.name) {
//         alert('氏名は必須です。');
//         return;
//     }

//     const card = createCardElement(cardData, showDetailModal);
//     cardList.appendChild(card);
//     closeModal(modal);
//     resetCallback();
// }

// New getCardFormDataFromModal function
export function getCardFormDataFromModal(modalElement) {
    if (!modalElement) {
        console.error("Modal element not provided to getCardFormDataFromModal");
        return null;
    }
    const data = {};
    data.card_id = modalElement.querySelector('input[name="card_id"]')?.value || "0"; // Default to "0" or empty for add
    data.name = modalElement.querySelector('input[name="name"]')?.value;
    data.email = modalElement.querySelector('input[name="email"]')?.value;
    data.remarks = modalElement.querySelector('textarea[name="remarks"]')?.value;
    data.favorite = modalElement.querySelector('input[name="favorite"]')?.checked;

    data.company_name = modalElement.querySelector('input[name="company_name"]')?.value;
    data.company_zipcode = modalElement.querySelector('input[name="company_zipcode"]')?.value;
    data.company_address = modalElement.querySelector('input[name="company_address"]')?.value;
    data.company_phone = modalElement.querySelector('input[name="company_phone"]')?.value;

    data.department_name = modalElement.querySelector('input[name="department_name"]')?.value;
    data.position_name = modalElement.querySelector('input[name="position_name"]')?.value;
    
    return data;
}


// Rewritten handleCardSubmission
export async function handleCardSubmission(modal, onSuccessRefreshCallback, resetCallback) {
    const formData = getCardFormDataFromModal(modal);

    if (!formData) {
        alert('モーダル要素が見つかりません。');
        return;
    }

    if (!formData.name || !formData.company_name) {
        alert('氏名と会社名は必須です。');
        return;
    }

    const params = new URLSearchParams();
    // Determine action: 'add' or 'update'
    const cardId = parseInt(formData.card_id, 10);
    if (cardId > 0) {
        params.append('action', 'update');
    } else {
        params.append('action', 'add');
    }

    for (const key in formData) {
        if (formData[key] !== undefined && formData[key] !== null) { 
             params.append(key, formData[key] === true ? 'true' : formData[key] === false ? 'false' : formData[key]);
        }
    }
    
    try {
        const response = await fetch(cardServletUrl, { // Path updated
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: params
        });

        if (!response.ok) {
            const errorData = await response.json().catch(() => null);
            throw new Error(`HTTP error ${response.status}: ${errorData?.message || 'Failed to add card'}`);
        }

        const result = await response.json();
        if (result.success) {
            closeModal(modal); // closeModal needs to be available in this scope
            if (resetCallback) resetCallback(); // It should reset the form after add/update
            if (onSuccessRefreshCallback) onSuccessRefreshCallback(); // Refresh the list
            alert(params.get('action') === 'update' ? '名刺が更新されました。' : '名刺が追加されました。');
        } else {
            alert('カードの保存に失敗しました: ' + (result.message || '理由不明'));
        }
    } catch (error) {
        console.error('Error submitting card:', error);
        alert('カードの送信中にエラーが発生しました: ' + error.message);
    }
}


// ---名刺カードを最近追加順にソートする関数---
// export function sortCardsByDate(cards) { // This function seems to use 'created_at' which is not in CardInfo.
//     //スプレット構文　...cards中身取り出して展開
//     return [...cards].sort((a, b) => new Date(b.created_at) - new Date(a.created_at));
// }
// If sorting by 'created_date' (Timestamp from CardInfo) is needed:
export function sortCardsByDate(cards) {
    return [...cards].sort((a, b) => {
        const dateA = a.created_date ? new Date(a.created_date).getTime() : 0;
        const dateB = b.created_date ? new Date(b.created_date).getTime() : 0;
        return dateB - dateA; // For descending order (newest first)
    });
}


// ---名刺カードを最近追加された順にフィルタリングする関数---
// export function filterCardsByRecentDays(cards, days) { // This function also uses 'created_at'
//     const now = new Date();
//     return cards.filter(card => {
//         const createdAt = new Date(card.created_at);
//         const diffDays = (now - createdAt) / (1000 * 60 * 60 * 24);
//         return diffDays <= days;
//     });
// }
// If filtering by 'created_date' (Timestamp from CardInfo) is needed:
export function filterCardsByRecentDays(cards, days) {
    const now = new Date().getTime();
    const daysInMillis = days * 24 * 60 * 60 * 1000;
    return cards.filter(card => {
        const createdAt = card.created_date ? new Date(card.created_date).getTime() : 0;
        if (createdAt === 0) return false; // Don't include cards without a creation date
        return (now - createdAt) <= daysInMillis;
    });
}
