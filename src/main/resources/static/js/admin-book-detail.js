document.addEventListener('DOMContentLoaded', function() {
    const form = document.querySelector('form');
    const selectedTagsDisplay = document.getElementById('selected-tags-display');
    const hiddenTagsInput = document.getElementById('hidden-tags-input');
    const selectedCategoriesDisplay = document.getElementById('selected-categories-display');
    const hiddenCategoriesInput = document.getElementById('hidden-categories-input');

    // Function to add a tag to the display and hidden input
    function addTag(tagId, tagName) {
        if (!document.getElementById(`selected-tag-${tagId}`)) {
            // Add to display
            const tagSpan = document.createElement('span');
            tagSpan.id = `selected-tag-${tagId}`;
            tagSpan.className = 'badge bg-primary me-1 mb-1';
            tagSpan.innerHTML = `<span>${tagName}</span><button type="button" class="btn-close btn-close-white ms-1" aria-label="Remove" data-tag-id="${tagId}"></button>`;
            selectedTagsDisplay.appendChild(tagSpan);

            // Add to hidden input
            const hiddenInput = document.createElement('input');
            hiddenInput.type = 'hidden';
            hiddenInput.name = `tagIds[${hiddenTagsInput.children.length}]`;
            hiddenInput.value = tagId;
            hiddenTagsInput.appendChild(hiddenInput);

            updateHiddenInputNames(hiddenTagsInput, 'tagIds');
        }
    }

    // Function to add a category to the display and hidden input
    function addCategory(categoryId, categoryName) {
        if (!document.getElementById(`selected-category-${categoryId}`)) {
            // Add to display
            const categorySpan = document.createElement('span');
            categorySpan.id = `selected-category-${categoryId}`;
            categorySpan.className = 'badge bg-secondary me-1 mb-1';
            categorySpan.innerHTML = `<span>${categoryName}</span><button type="button" class="btn-close btn-close-white ms-1" aria-label="Remove" data-category-id="${categoryId}"></button>`;
            selectedCategoriesDisplay.appendChild(categorySpan);

            // Add to hidden input
            const hiddenInput = document.createElement('input');
            hiddenInput.type = 'hidden';
            hiddenInput.name = `categoryIds[${hiddenCategoriesInput.children.length}]`;
            hiddenInput.value = categoryId;
            hiddenCategoriesInput.appendChild(hiddenInput);

            updateHiddenInputNames(hiddenCategoriesInput, 'categoryIds');
        }
    }

    // Function to update hidden input names to maintain correct indexing
     function updateHiddenInputNames(container, namePrefix) {
        Array.from(container.querySelectorAll('input[type="hidden"]')).forEach((input, index) => {
            input.name = `${namePrefix}[${index}]`;
        });
    }

    // Tag Selection Modal Logic (Event Delegation)
    document.getElementById('tagSelectionModal').addEventListener('click', function(event) {
        if (event.target.classList.contains('select-tag-btn')) {
            const tagId = event.target.dataset.tagId;
            const tagName = event.target.dataset.tagName;
            addTag(tagId, tagName);
            
            // Close modal
            const modal = bootstrap.Modal.getInstance(document.getElementById('tagSelectionModal'));
            modal.hide();
        }
    });

    // Category Selection Modal Logic (Event Delegation)
    document.getElementById('categorySelectionModal').addEventListener('click', function(event) {
        if (event.target.classList.contains('select-category-btn')) {
            const categoryId = event.target.dataset.categoryId;
            const categoryName = event.target.dataset.categoryName;
            addCategory(categoryId, categoryName);

            // Close modal
            const modal = bootstrap.Modal.getInstance(document.getElementById('categorySelectionModal'));
            modal.hide();
        }
    });

    // Remove Tag from display and hidden input (Event Delegation)
    selectedTagsDisplay.addEventListener('click', function(event) {
        if (event.target.classList.contains('btn-close') && event.target.dataset.tagId) {
            const tagIdToRemove = event.target.dataset.tagId;
            document.getElementById(`selected-tag-${tagIdToRemove}`).remove(); // Remove from display
            
            // Remove from hidden inputs
            Array.from(hiddenTagsInput.children).forEach(input => {
                if (input.value == tagIdToRemove) {
                    input.remove();
                }
            });
            updateHiddenInputNames(hiddenTagsInput, 'tagIds'); // Re-index
        }
    });

    // Remove Category from display and hidden input (Event Delegation)
    selectedCategoriesDisplay.addEventListener('click', function(event) {
        if (event.target.classList.contains('btn-close') && event.target.dataset.categoryId) {
            const categoryIdToRemove = event.target.dataset.categoryId;
            document.getElementById(`selected-category-${categoryIdToRemove}`).remove(); // Remove from display

            // Remove from hidden inputs
            Array.from(hiddenCategoriesInput.children).forEach(input => {
                if (input.value == categoryIdToRemove) {
                    input.remove();
                }
            });
            updateHiddenInputNames(hiddenCategoriesInput, 'categoryIds'); // Re-index
        }
    });

    // On initial load, ensure hidden inputs for existing tags/categories are correctly named/indexed
    updateHiddenInputNames(hiddenTagsInput, 'tagIds');
    updateHiddenInputNames(hiddenCategoriesInput, 'categoryIds');


    // Image upload logic
    const imageUpload = document.getElementById('imageUpload');
    const uploadImageBtn = document.getElementById('uploadImageBtn');
    const imageUrlHidden = document.getElementById('imageUrlHidden');
    const imagePreview = document.getElementById('imagePreview');
    const currentImagePreview = document.getElementById('currentImagePreview');
    const uploadStatus = document.getElementById('uploadStatus'); // Get upload status element

    // Initialize image preview if imageUrlHidden has a value
    if (imageUrlHidden.value) {
        imagePreview.src = imageUrlHidden.value;
        imagePreview.style.display = 'block';
    }

    uploadImageBtn.addEventListener('click', function() {
        const file = imageUpload.files[0];
        if (!file) {
            alert('업로드할 이미지를 선택해주세요.');
            return;
        }

        const formData = new FormData();
        formData.append('image', file);

        uploadStatus.textContent = '업로드 중...';
        uploadStatus.style.color = 'blue';
        uploadImageBtn.disabled = true; // Disable button during upload

        fetch('/admin/images/upload', {
            method: 'POST',
            body: formData
        })
        .then(response => {
            if (!response.ok) {
                return response.json().then(err => { throw new Error(err.error || '이미지 업로드 실패'); });
            }
            return response.json();
        })
        .then(data => {
            if (data.imageUrl) {
                imageUrlHidden.value = data.imageUrl;
                imagePreview.src = data.imageUrl;
                imagePreview.style.display = 'block';
                uploadStatus.textContent = '업로드 성공!';
                uploadStatus.style.color = 'green';
                alert('이미지 업로드 성공!');
            } else {
                uploadStatus.textContent = '이미지 업로드 실패: ' + (data.message || '알 수 없는 오류');
                uploadStatus.style.color = 'red';
                alert('이미지 업로드 실패: ' + (data.message || '알 수 없는 오류'));
            }
        })
        .catch(error => {
            console.error('Error uploading image:', error);
            uploadStatus.textContent = '업로드 실패: ' + error.message;
            uploadStatus.style.color = 'red';
            alert('이미지 업로드 중 오류가 발생했습니다: ' + error.message);
        })
        .finally(() => {
            uploadImageBtn.disabled = false; // Re-enable button
        });
    });

    // Optional: Display a local preview when file is selected
    imageUpload.addEventListener('change', function() {
        if (imageUpload.files && imageUpload.files[0]) {
            const reader = new FileReader();
            reader.onload = function(e) {
                imagePreview.src = e.target.result;
                imagePreview.style.display = 'block';
            };
            reader.readAsDataURL(imageUpload.files[0]);
            uploadStatus.textContent = ''; // Clear status on new file selection
        } else {
            imagePreview.src = '';
            imagePreview.style.display = 'none';
            imageUrlHidden.value = '';
            uploadStatus.textContent = ''; // Clear status
        }
    });
});