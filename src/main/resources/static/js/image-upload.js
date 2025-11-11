document.addEventListener('DOMContentLoaded', function() {
    const imageUpload = document.getElementById('imageUpload');
    const uploadImageBtn = document.getElementById('uploadImageBtn');
    const imageUrlHidden = document.getElementById('imageUrlHidden');
    const imagePreview = document.getElementById('imagePreview');
    const uploadStatus = document.getElementById('uploadStatus'); // Get upload status element

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
