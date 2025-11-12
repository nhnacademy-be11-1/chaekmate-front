document.addEventListener('DOMContentLoaded', function() {

    // Reusable function to upload a file to MinIO and get the URL
    function uploadImage(file, statusElement) {
        return new Promise((resolve, reject) => {
            const formData = new FormData();
            formData.append('image', file);

            statusElement.textContent = '업로드 중...';
            statusElement.style.color = 'blue';

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
                    statusElement.textContent = '이미지 준비 완료! 최종 저장을 눌러 반영하세요.';
                    statusElement.style.color = 'green';
                    resolve(data.imageUrl);
                } else {
                    reject(new Error(data.message || '알 수 없는 오류'));
                }
            })
            .catch(error => {
                console.error('Error uploading image:', error);
                statusElement.textContent = `오류: ${error.message}`;
                statusElement.style.color = 'red';
                reject(error);
            });
        });
    }

    // --- Thumbnail Management ---
    const thumbnailInput = document.getElementById('thumbnail-upload-input');
    if (thumbnailInput) {
        thumbnailInput.addEventListener('change', function(event) {
            const file = event.target.files[0];
            if (!file) return;

            const preview = document.getElementById('thumbnail-preview');
            const status = document.getElementById('thumbnail-status');
            const hiddenUrlInput = document.getElementById('new-thumbnail-url');

            const reader = new FileReader();
            reader.onload = e => preview.src = e.target.result;
            reader.readAsDataURL(file);

            uploadImage(file, status)
                .then(imageUrl => {
                    hiddenUrlInput.value = imageUrl;
                })
                .catch(() => {
                    hiddenUrlInput.value = '';
                    // Optionally reset preview to original if upload fails
                });
        });
    }

    // --- Detail Image Management ---
    const detailImageUploadInput = document.getElementById('detail-image-upload-input');
    const newImagesPreviewContainer = document.getElementById('new-detail-images-preview-container');
    const newUrlsContainer = document.getElementById('new-detail-urls-container');
    const existingImagesContainer = document.getElementById('detail-images-container');
    const deletedIdsInput = document.getElementById('deleted-image-ids');

    // Add new detail image
    if (detailImageUploadInput) {
        detailImageUploadInput.addEventListener('change', function(event) {
            const file = event.target.files[0];
            if (!file) return;

            const status = document.getElementById('detail-image-status');

            uploadImage(file, status)
                .then(imageUrl => {
                    const newImageId = `new-image-${Date.now()}`;

                    // Create hidden input for the URL
                    const hiddenInput = document.createElement('input');
                    hiddenInput.type = 'hidden';
                    hiddenInput.name = 'newDetailImageUrls';
                    hiddenInput.value = imageUrl;
                    hiddenInput.id = `hidden-${newImageId}`;
                    newUrlsContainer.appendChild(hiddenInput);

                    // Create preview element
                    const previewDiv = document.createElement('div');
                    previewDiv.className = 'col-md-3 mb-3';
                    previewDiv.id = `preview-${newImageId}`;
                    previewDiv.innerHTML = `
                        <div class="position-relative">
                            <img src="${imageUrl}" class="img-fluid rounded" alt="New Detail Image">
                            <button type="button" class="btn btn-sm btn-warning position-absolute remove-new-btn"
                                    style="top: 5px; right: 5px;"
                                    data-target-id="${newImageId}">
                                &times;
                            </button>
                        </div>
                    `;
                    newImagesPreviewContainer.appendChild(previewDiv);
                    
                    // Clear the file input for the next upload
                    event.target.value = '';
                })
                .catch(() => {
                    event.target.value = '';
                });
        });
    }

    // Remove a newly added (but not yet saved) detail image
    if (newImagesPreviewContainer) {
        newImagesPreviewContainer.addEventListener('click', function(event) {
            if (event.target.classList.contains('remove-new-btn')) {
                const targetId = event.target.dataset.targetId;
                const hiddenInput = document.getElementById(`hidden-${targetId}`);
                const previewDiv = document.getElementById(`preview-${targetId}`);

                if (hiddenInput) hiddenInput.remove();
                if (previewDiv) previewDiv.remove();
            }
        });
    }

    // Mark an existing detail image for deletion
    if (existingImagesContainer) {
        existingImagesContainer.addEventListener('click', function(event) {
            if (event.target.classList.contains('delete-detail-btn')) {
                const button = event.target;
                const imageId = button.dataset.imageId;
                const imageContainer = button.closest('.position-relative');

                // Toggle deletion status
                if (button.classList.contains('btn-success')) { // It was marked for deletion, now cancel
                    button.classList.remove('btn-success');
                    button.classList.add('btn-danger');
                    button.innerHTML = '&times;';
                    imageContainer.querySelector('img').style.opacity = '1';

                    // Remove from hidden input
                    const currentIds = deletedIdsInput.value.split(',').filter(id => id.trim() !== '');
                    const newIds = currentIds.filter(id => id !== imageId);
                    deletedIdsInput.value = newIds.join(',');

                } else { // Mark for deletion
                    button.classList.remove('btn-danger');
                    button.classList.add('btn-success');
                    button.innerHTML = '&#x21A9;'; // Undo arrow
                    imageContainer.querySelector('img').style.opacity = '0.5';

                    // Add to hidden input
                    const currentIds = deletedIdsInput.value.split(',').filter(id => id.trim() !== '');
                    if (!currentIds.includes(imageId)) {
                        currentIds.push(imageId);
                        deletedIdsInput.value = currentIds.join(',');
                    }
                }
            }
        });
    }
});