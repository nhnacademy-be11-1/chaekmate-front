document.addEventListener('DOMContentLoaded', function () {
    const categoryModal = document.getElementById('categoryModal');
    const parentCategoryNameInput = document.getElementById('parentCategoryName');
    const parentCategoryIdInput = document.getElementById('parentCategoryId');
    const clearButton = document.getElementById('clearParentCategory');

    if (!categoryModal) return; // Execute only if the modal exists on the page

    categoryModal.addEventListener('click', function (event) {
        const link = event.target.closest('.category-tree-item a');
        if (link) {
            event.preventDefault();
            const id = link.getAttribute('data-id');
            const name = link.getAttribute('data-name');

            parentCategoryIdInput.value = id;
            parentCategoryNameInput.value = (id === '') ? '선택된 카테고리 없음' : name;

            var modalInstance = bootstrap.Modal.getInstance(categoryModal);
            modalInstance.hide();
        }
    });

    clearButton.addEventListener('click', function () {
        parentCategoryIdInput.value = '';
        parentCategoryNameInput.value = '선택된 카테고리 없음';
    });
});
