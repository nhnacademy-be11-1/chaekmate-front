document.addEventListener('DOMContentLoaded', function () {
    const sidebar = document.querySelector('.sidebar');

    if (sidebar) {
        sidebar.addEventListener('click', function (event) {
            if (event.target.classList.contains('toggle-btn')) {
                const btn = event.target;
                const subcategoryList = btn.parentElement.nextElementSibling;

                if (subcategoryList && subcategoryList.classList.contains('subcategory-list')) {
                    if (subcategoryList.style.display === 'none' || subcategoryList.style.display === '') {
                        subcategoryList.style.display = 'block';
                        btn.textContent = '-';
                    } else {
                        subcategoryList.style.display = 'none';
                        btn.textContent = '+';
                    }
                }
            }
        });
    }
});
