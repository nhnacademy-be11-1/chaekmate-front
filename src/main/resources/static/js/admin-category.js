document.addEventListener('DOMContentLoaded', function () {
    const categoryModal = document.getElementById('categoryModal');
    const parentCategoryNameInput = document.getElementById('parentCategoryName');
    const parentCategoryIdInput = document.getElementById('parentCategoryId');
    const clearButton = document.getElementById('clearParentCategory');

    if (!categoryModal) return;

    categoryModal.addEventListener('click', function (event) {
        const target = event.target;

        // Handle clicking the 'Edit' button first
        const editBtn = target.closest('.edit-btn');
        if (editBtn) {
            event.preventDefault();
            const id = editBtn.dataset.id;
            const currentName = editBtn.dataset.name;
            const parentId = editBtn.dataset.parentId || '';

            const newName = prompt("새 카테고리 이름을 입력하세요:", currentName);

            if (newName && newName.trim() !== '' && newName !== currentName) {
                const form = document.createElement('form');
                form.method = 'post';
                form.action = `/admin/categories/${id}/update`;

                const nameInput = document.createElement('input');
                nameInput.type = 'hidden';
                nameInput.name = 'name';
                nameInput.value = newName;
                form.appendChild(nameInput);

                const parentIdInput = document.createElement('input');
                parentIdInput.type = 'hidden';
                parentIdInput.name = 'parentCategoryId';
                parentIdInput.value = parentId;
                form.appendChild(parentIdInput);

                document.body.appendChild(form);
                form.submit();
            }
            return; // Stop further processing
        }

        // Handle clicking a category link to select it as a parent
        const categoryLink = target.closest('a.category-link, .category-tree-item > a');
        if (categoryLink) {
            event.preventDefault();
            const id = categoryLink.dataset.id;
            const name = categoryLink.dataset.name;

            parentCategoryIdInput.value = id;
            parentCategoryNameInput.value = (id === '') ? '선택된 카테고리 없음' : name;

            const modalInstance = bootstrap.Modal.getInstance(categoryModal);
            modalInstance.hide();
        }
    });

    // Handle the 'Clear Parent' button on the main form
    if (clearButton) {
        clearButton.addEventListener('click', function () {
            parentCategoryIdInput.value = '';
            parentCategoryNameInput.value = '선택된 카테고리 없음';
        });
    }
});
