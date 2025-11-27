document.addEventListener('DOMContentLoaded', function () {
    const cartButtons = document.querySelectorAll('.add-to-cart-btn');

    cartButtons.forEach(function (button) {
        button.addEventListener('click', function (e) {
            e.preventDefault();
            e.stopPropagation();

            const bookId = this.getAttribute('data-book-id');

            if (!bookId) {
                alert('도서 정보를 찾을 수 없습니다.');
                return;
            }

            this.disabled = true;
            const originalHTML = this.innerHTML;
            this.innerHTML = '<i class="fa fa-spinner fa-spin"></i>';

            fetch('/carts/items', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    bookId: Number(bookId),
                    quantity: 1
                })
            })
                .then(function (response) {
                    if (!response.ok) {
                        return response.json().then(function (errorData) {
                            throw {
                                status: response.status,
                                data: errorData
                            };
                        });
                    }
                    return response.json();
                })
                .then(function () {
                    if (confirm('장바구니에 상품이 담겼습니다.\n장바구니로 이동하시겠습니까?')) {
                        window.location.href = '/carts';
                    } else {
                        button.disabled = false;
                        button.innerHTML = originalHTML;
                    }
                })
                .catch(function (error) {
                    let errorMessage = '장바구니 담기 중 에러가 발생했습니다.';

                    if (error.data?.data?.message) {
                        errorMessage = error.data.data.message;
                    } else if (error.data?.message) {
                        errorMessage = error.data.message;
                    } else if (error.message) {
                        errorMessage = error.message;
                    }

                    alert(errorMessage);
                    button.disabled = false;
                    button.innerHTML = originalHTML;
                });
        });
    });
});
