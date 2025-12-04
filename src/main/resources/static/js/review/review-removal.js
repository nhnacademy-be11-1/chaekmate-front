document.addEventListener('DOMContentLoaded', function () {

    const itemsPerLoad = 3; // 한번에 표시할 리뷰 개수
    const reviewItems = document.querySelectorAll('.review-item');
    const loadMoreBtn = document.getElementById('load-more-btn');

    let currentIndex = 0;

    function showNextItems() {
        for (let i = currentIndex; i < currentIndex + itemsPerLoad; i++) {
            if (reviewItems[i]) {
                reviewItems[i].style.display = 'block';
            }
        }
        currentIndex += itemsPerLoad;

        if (currentIndex >= reviewItems.length) {
            loadMoreBtn.style.display = 'none';
        }
    }

    // 초기에는 모두 숨기고, 첫 N개만 보여줌
    reviewItems.forEach(item => item.style.display = 'none');
    showNextItems();

    loadMoreBtn.addEventListener('click', showNextItems);
});
