// 숫자 포맷팅 함수 (천단위 콤마)
function formatNumber(num) {
    return num.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",") + "원";
}

// 장바구니 총합 계산 함수
function calculateCartTotal() {
    const cartItems = document.querySelectorAll('.cart-item');
    let subtotal = 0;

    cartItems.forEach(item => {
        const price = parseInt(item.getAttribute('data-price'));
        const quantity = parseInt(item.getAttribute('data-quantity'));
        subtotal += price * quantity;
    });

    const shipping = 10;
    const total = subtotal + shipping;

    // DOM 업데이트
    const subtotalElement = document.getElementById('subtotal');
    const totalElement = document.getElementById('total');

    if (subtotalElement) {
        subtotalElement.textContent = formatNumber(subtotal);
    }
    if (totalElement) {
        totalElement.textContent = formatNumber(total);
    }
}

// 개별 아이템 총액 업데이트 함수
function updateItemTotal(row) {
    const price = parseInt(row.getAttribute('data-price'));
    const quantity = parseInt(row.getAttribute('data-quantity'));
    const total = price * quantity;

    const totalElement = row.querySelector('.item-total');
    if (totalElement) {
        totalElement.textContent = formatNumber(total);
    }
}

// 수량 증가 버튼 이벤트
function plusButtons() {
    document.querySelectorAll('.btn-plus').forEach(function(button) {
        button.addEventListener('click', function(e) {
            e.preventDefault();
            e.stopPropagation();

            const bookId = this.getAttribute('data-book-id');
            const row = document.querySelector('.cart-item[data-book-id="' + bookId + '"]');
            const quantityInput = row.querySelector('.item-quantity');

            const quantity = parseInt(row.getAttribute('data-quantity'));
            const newQuantity = quantity + 1;

            // 버튼 비활성화 (중복 클릭 방지)
            this.disabled = true;
            const originalHTML = this.innerHTML;
            this.innerHTML = '<i class="fa fa-spinner fa-spin"></i>';

            // 서버에 수량 업데이트 API 호출
            fetch('/carts/items/' + bookId, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    quantity: newQuantity
                })
            })
                .then(function(response) {
                    console.log('응답 상태:', response.status);

                    if (!response.ok) {
                        return response.text().then(function(text) {
                            throw new Error(text || '수량 변경 실패');
                        });
                    }

                    return response.json();
                })
                .then(function(data) {
                    console.log('수량 변경 성공:', data);

                    // 성공 시 DOM 업데이트
                    row.setAttribute('data-quantity', newQuantity);
                    quantityInput.value = newQuantity;

                    updateItemTotal(row);
                    calculateCartTotal();

                    // 버튼 복원
                    button.disabled = false;
                    button.innerHTML = originalHTML;
                })
                .catch(function(error) {
                    console.error('에러 발생:', error);

                    // 버튼 복원
                    button.disabled = false;
                    button.innerHTML = originalHTML;
                });
        });
    });

}

// 수량 감소 버튼 이벤트
function minusButtons() {
    document.querySelectorAll('.btn-minus').forEach(function(button) {
        button.addEventListener('click', function(e) {
            e.preventDefault();
            e.stopPropagation();

            const bookId = this.getAttribute('data-book-id');
            const row = document.querySelector('.cart-item[data-book-id="' + bookId + '"]');
            const quantityInput = row.querySelector('.item-quantity');

            const quantity = parseInt(row.getAttribute('data-quantity'));

            if (quantity > 1) {
                const newQuantity = quantity - 1;

                // 버튼 비활성화 (중복 클릭 방지)
                this.disabled = true;
                const originalHTML = this.innerHTML;
                this.innerHTML = '<i class="fa fa-spinner fa-spin"></i>';

                // 서버에 수량 업데이트 API 호출
                fetch('/carts/items/' + bookId, {
                    method: 'PUT',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({
                        quantity: newQuantity
                    })
                })
                    .then(function(response) {
                        console.log('응답 상태:', response.status);

                        if (!response.ok) {
                            return response.text().then(function(text) {
                                throw new Error(text || '수량 변경 실패');
                            });
                        }

                        return response.json();
                    })
                    .then(function(data) {
                        console.log('수량 변경 성공:', data);

                        // 성공 시 DOM 업데이트
                        row.setAttribute('data-quantity', newQuantity);
                        quantityInput.value = newQuantity;

                        updateItemTotal(row);
                        calculateCartTotal();

                        // 버튼 복원
                        button.disabled = false;
                        button.innerHTML = originalHTML;
                    })
                    .catch(function(error) {
                        console.error('에러 발생:', error);

                        // 버튼 복원
                        button.disabled = false;
                        button.innerHTML = originalHTML;
                    });
            }
        });
    });

}

// 아이템 삭제 버튼 이벤트
function removeButtons() {
    document.querySelectorAll('.btn-remove').forEach(function(button) {
        button.addEventListener('click', function(e) {
            e.preventDefault();
            e.stopPropagation();

            const bookId = this.getAttribute('data-book-id');

            if (confirm('이 상품을 장바구니에서 삭제하시겠습니까?')) {
                // 버튼 비활성화 (중복 클릭 방지)
                this.disabled = true;
                const originalHTML = this.innerHTML;
                this.innerHTML = '<i class="fa fa-spinner fa-spin"></i>';

                // 서버에 삭제 API 호출
                fetch('/carts/items/' + bookId, {
                    method: 'DELETE'
                })
                    .then(function(response) {
                        console.log('응답 상태:', response.status);

                        if (!response.ok) {
                            return response.text().then(function(text) {
                                throw new Error(text || '삭제 실패');
                            });
                        }

                        // DELETE 요청은 응답 본문이 없을 수 있으므로 상태 코드만 확인
                        return true;
                    })
                    .then(function() {
                        console.log('삭제 성공');

                        // 성공 시 DOM에서 제거
                        const row = document.querySelector('.cart-item[data-book-id="' + bookId + '"]');
                        row.remove();

                        calculateCartTotal();

                        // 장바구니가 비었는지 확인
                        if (document.querySelectorAll('.cart-item').length === 0) {
                            location.reload(); // 페이지 새로고침
                        }
                    })
                    .catch(function(error) {
                        console.error('에러 발생:', error);

                        // 버튼 복원
                        button.disabled = false;
                        button.innerHTML = originalHTML;
                    });
            }
        });
    });
}

// 주문하기 버튼 클릭 이벤트
function orderButton() {
    document.querySelector('.btn-order')
        .addEventListener('click', function(e) {
            e.preventDefault();
            e.stopPropagation();

            const orderButton = this;

            // 장바구니 비어있으면 요청 막기
            const cartItems = document.querySelectorAll('.cart-item');
            if (cartItems.length === 0) {
                alert('장바구니가 비어 있습니다.');
                return;
            }

            // 버튼 중복 클릭 방지
            orderButton.disabled = true;
            const originalHTML = orderButton.innerHTML;
            orderButton.innerHTML = '<i class="fa fa-spinner fa-spin"></i>';

            // 장바구니 데이터 추출
            const orderItems = [];

            cartItems.forEach(item => {
                const bookId = parseInt(item.getAttribute('data-book-id'));
                const quantity = parseInt(item.getAttribute('data-quantity'));

                orderItems.push({
                    bookId: bookId,
                    quantity: quantity
                });
            });

            // 서버에 주문 생성 요청
            fetch('/orders', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    items: orderItems
                })
            })
                .then(function(response) {
                    console.log('응답 상태:', response.status);

                    return response.json();
                })
                .then(function(data) {
                    console.log('주문 요청 성공:', data);

                    // 버튼 복원
                    orderButton.disabled = false;
                    orderButton.innerHTML = originalHTML;
                })
                .catch(function(error) {
                    console.error('에러 발생:', error);
                    alert('주문 요청 중 오류가 발생했습니다.');

                    // 버튼 복원
                    orderButton.disabled = false;
                    orderButton.innerHTML = originalHTML;
                });
        });
}


// 페이지 로드 시 초기화
document.addEventListener('DOMContentLoaded', function() {
    calculateCartTotal();   // 총합 계산
    plusButtons();          // 수량 증가 버튼
    minusButtons();         // 수량 감소 버튼
    removeButtons();        // 삭제 버튼
    orderButton();          // 주문 버튼
});