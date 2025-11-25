document.addEventListener('DOMContentLoaded', function() {

    function uploadReviewImage(file, statusElement) {
        return new Promise((resolve, reject) => {
            const formData = new FormData();
            formData.append('image', file);

            statusElement.textContent = '업로드 중...';
            statusElement.style.color = 'blue';

            // Assuming a non-admin endpoint exists for image uploads
            fetch('/images/upload', {
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
                    statusElement.textContent = '이미지 준비 완료!';
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

    const reviewImageUploadInput = document.getElementById('review-image-upload-input');
    const imagesPreviewContainer = document.getElementById('review-images-preview-container');
    const urlsContainer = document.getElementById('review-image-urls-container');

    if (reviewImageUploadInput) {
        reviewImageUploadInput.addEventListener('change', function(event) {
            const file = event.target.files[0];
            if (!file) return;

            const status = document.getElementById('review-image-status');

            uploadReviewImage(file, status)
                .then(imageUrl => {
                    const newImageId = `new-image-${Date.now()}`;

                    // Create hidden input for the URL
                    const hiddenInput = document.createElement('input');
                    hiddenInput.type = 'hidden';
                    hiddenInput.name = 'imageUrls'; // Match the DTO field
                    hiddenInput.value = imageUrl;
                    hiddenInput.id = `hidden-${newImageId}`;
                    urlsContainer.appendChild(hiddenInput);

                    // Create preview element
                    const previewDiv = document.createElement('div');
                    previewDiv.className = 'col-md-3 mb-3';
                    previewDiv.id = `preview-${newImageId}`;
                    previewDiv.innerHTML = `
                        <div class="position-relative">
                            <img src="${imageUrl}" class="img-fluid rounded" alt="Review Image Preview" onerror="this.onerror=null;this.src='/img/no-image.jpeg';">
                            <button type="button" class="btn btn-sm btn-warning position-absolute remove-new-btn"
                                    style="top: 5px; right: 5px;"
                                    data-target-id="${newImageId}">
                                &times;
                            </button>
                        </div>
                    `;
                    imagesPreviewContainer.appendChild(previewDiv);

                    event.target.value = '';
                })
                .catch(() => {
                    event.target.value = '';
                });
        });
    }

    // Remove a newly added image
    if (imagesPreviewContainer) {
        imagesPreviewContainer.addEventListener('click', function(event) {
            if (event.target.classList.contains('remove-new-btn')) {
                const targetId = event.target.dataset.targetId;
                const hiddenInput = document.getElementById(`hidden-${targetId}`);
                const previewDiv = document.getElementById(`preview-${targetId}`);

                if (hiddenInput) hiddenInput.remove();
                if (previewDiv) previewDiv.remove();
            }
        });
    }
});
