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

            fetch('/likes/action', {
                method: 'POST',
                headers: headers,
                body: JSON.stringify({ bookId: bookId })
            })
            .then(response => {
                if (response.status === 401) {
                    alert('로그인이 필요합니다.');
                    window.location.href = '/login';
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
                    if (data.liked) {
                        // '찜' 상태로 변경
                        icon.classList.remove('far'); // 빈 하트
                        icon.classList.add('fas'); // 꽉 찬 하트
                        icon.style.color = 'red';
                        // 찜하기 버튼 텍스트가 있다면 변경 (예: book-detail)
                        const textNode = likeButton.childNodes[likeButton.childNodes.length - 1];
                        if (textNode.nodeType === Node.TEXT_NODE && textNode.textContent.includes('찜하기')) {
                            textNode.textContent = ' 찜 취소';
                        }

                    } else {
                        // '찜 취소' 상태로 변경
                        icon.classList.remove('fas');
                        icon.classList.add('far');
                        icon.style.color = ''; // 원래 색상으로
                        const textNode = likeButton.childNodes[likeButton.childNodes.length - 1];
                        if (textNode.nodeType === Node.TEXT_NODE && textNode.textContent.includes('찜 취소')) {
                            textNode.textContent = ' 찜하기';
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
