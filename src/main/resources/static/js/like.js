document.addEventListener('DOMContentLoaded', function () {
    // 모든 'btn-like' 클래스를 가진 버튼에 이벤트 리스너를 위임합니다.
    document.body.addEventListener('click', function (event) {
        const likeButton = event.target.closest('.btn-like');

        if (likeButton) {
            event.preventDefault();
            const bookId = likeButton.dataset.bookId;
            if (!bookId) {
                console.error('찜하기 버튼에 data-book-id 속성이 없습니다.');
                return;
            }

            // CSRF 토큰 가져오기 (Thymeleaf + Spring Security 사용 시)
            const csrfToken = document.querySelector('meta[name="_csrf"]')?.getAttribute('content');
            const csrfHeader = document.querySelector('meta[name="_csrf_header"]')?.getAttribute('content');

            const headers = {
                'Content-Type': 'application/json',
            };
            if (csrfToken && csrfHeader) {
                headers[csrfHeader] = csrfToken;
            }

            const icon = likeButton.querySelector('i');
            const isCurrentlyLiked = icon.classList.contains('fas'); // Check if it's a solid heart
            const actionType = isCurrentlyLiked ? 'unlike' : 'like'; // Determine action based on current state

            fetch('/likes/action', {
                method: 'POST',
                headers: headers,
                credentials:"include",
                body: JSON.stringify({ bookId: bookId, actionType: actionType })
            })
            .then(response => {
                if (response.status === 401) {
                    alert('로그인이 필요합니다.');
                    this.location.href = '/login';
                    return;
                }
                if (!response.ok) {
                    // 서버에서 에러 메시지를 json 형태로 보냈을 경우를 대비
                    return response.json().then(err => { throw new Error(err.message || '찜하기 처리 중 오류가 발생했습니다.') });
                }
                return response.json();
            })
            .then(data => {
                if (data) {
                    const icon = likeButton.querySelector('i');
                    const textSpan = likeButton.querySelector('span');

                    if (data.liked) {
                        // '찜' 상태로 변경
                        icon.classList.remove('far'); // 빈 하트
                        icon.classList.add('fas'); // 꽉 찬 하트
                        icon.style.color = 'red';
                        if (textSpan) {
                            textSpan.textContent = ' 찜 취소';
                        }

                    } else {
                        // '찜 취소' 상태로 변경
                        icon.classList.remove('fas');
                        icon.classList.add('far');
                        icon.style.color = ''; // 원래 색상으로
                        if (textSpan) {
                            textSpan.textContent = ' 찜하기';
                        }
                    }
                }
            })
            .catch(error => {
                console.error('찜하기 오류:', error);
                alert(error.message);
            });
        }
    });
});
