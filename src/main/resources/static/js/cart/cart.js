// 배송 정책 데이터 (서버에서 전달받은 값)
let deliveryPolicy = {
    freeStandardAmount: 0,
    deliveryFee: 0
};

// 페이지 로드 시 배송 정책 초기화
function initDeliveryPolicy() {
    const deliveryElement = document.getElementById('shipping');
    if (deliveryElement && deliveryElement.hasAttribute('data-delivery-fee')) {
        deliveryPolicy.deliveryFee = parseInt(deliveryElement.getAttribute('data-delivery-fee'));
        deliveryPolicy.freeStandardAmount = parseInt(deliveryElement.getAttribute('data-free-amount'));
    }
}

// 숫자 포맷팅 함수 (천단위 콤마)
function formatNumber(num) {
    return num.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",") + "원";
}

// 배송비 계산 함수
function calculateShippingFee(subtotal) {
    // 무료 배송 기준 금액 이상이면 배송비 0원
    if (subtotal >= deliveryPolicy.freeStandardAmount) {
        return 0;
    }
    // 그 외에는 배송비 부과
    return deliveryPolicy.deliveryFee;
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

    // 배송비 계산
    const shippingFee = calculateShippingFee(subtotal);
    const total = subtotal + shippingFee;

    // DOM 업데이트
    const subtotalElement = document.getElementById('subtotal');
    const shippingElement = document.getElementById('shipping');
    const totalElement = document.getElementById('total');

    if (subtotalElement) {
        subtotalElement.textContent = formatNumber(subtotal);
    }

    if (shippingElement) {
        // 무료 배송일 경우 표시
        if (shippingFee === 0) {
            shippingElement.innerHTML = '<span class="text-success font-weight-bold">무료배송</span>';
        } else {
            shippingElement.textContent = formatNumber(shippingFee);

            // 무료 배송까지 남은 금액 표시 (선택사항)
            const remainingAmount = deliveryPolicy.freeStandardAmount - subtotal;
            if (remainingAmount > 0) {
                shippingElement.innerHTML = '<div class="text-right">' + formatNumber(shippingFee) +
                    '<br><small class="text-muted">' +
                    formatNumber(remainingAmount) + ' 더 담으면 무료배송</small></div>';
            }
        }
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
                method: 'POST',
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
                    method: 'POST',
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
                fetch('/carts/items/delete/' + bookId, {
                    method: 'POST'
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
    const orderBtn = document.querySelector('.btn-order');

    if (orderBtn) {
        orderBtn.addEventListener('click', function(e) {
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
            orderButton.innerHTML = '<i class="fa fa-spinner fa-spin"></i> 재고 확인 중...';

            // 장바구니 데이터 추출
            const orderItems = [];

            cartItems.forEach(item => {
                const bookId = Number(item.getAttribute('data-book-id'));
                const quantity = parseInt(item.getAttribute('data-quantity'));
                const stock = parseInt(item.getAttribute('data-stock') || '0');

                orderItems.push({
                    bookId: bookId,
                    quantity: quantity,
                    stock: stock
                });
            });

            // 재고 검증
            const insufficientStockItems = [];
            orderItems.forEach(item => {
                if (item.quantity > item.stock) {
                    insufficientStockItems.push({
                        bookId: item.bookId,
                        requestedQty: item.quantity,
                        availableStock: item.stock
                    });
                }
            });

            // 재고 부족한 상품이 있으면 주문 중단
            if (insufficientStockItems.length > 0) {
                let errorMessage = '재고가 부족한 상품이 있습니다:\n\n';

                insufficientStockItems.forEach(item => {
                    const itemElement = document.querySelector(`.cart-item[data-book-id="${item.bookId}"]`);
                    const bookTitle = itemElement.querySelector('.book-title')?.textContent || '도서';

                    errorMessage += `• ${bookTitle}\n`;
                    errorMessage += `  요청 수량: ${item.requestedQty}개, 재고: ${item.availableStock}개\n\n`;
                });

                alert(errorMessage);

                // 버튼 복원
                orderButton.disabled = false;
                orderButton.innerHTML = originalHTML;
                return;
            }

            // 재고 검증 통과 시 주문 진행
            orderButton.innerHTML = '<i class="fa fa-spinner fa-spin"></i> 주문 처리 중...';

            // 서버에 주문 생성 요청
            fetch('/orders', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    items: orderItems.map(item => ({
                        bookId: item.bookId,
                        quantity: item.quantity
                    }))
                })
            })
                .then(function(response) {
                    console.log('응답 상태:', response.status);

                    return response.json();
                })
                .then(function(data) {
                    console.log('주문 요청 성공:', data);

                    const encoded = encodeURIComponent(JSON.stringify(orderItems.map(item => ({
                        bookId: item.bookId,
                        quantity: item.quantity
                    }))));

                    // 페이지 이동 (버튼은 복원하지 않음 - 이동될 때까지 로딩 상태 유지)
                    window.location.href = data.redirectUrl + `?items=${encoded}`;

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
}

// 장바구니 비우기 버튼 이벤트
function flushCartButton() {
    const flushBtn = document.querySelector('.btn-flush-cart');

    if (flushBtn) {
        flushBtn.addEventListener('click', function(e) {
            e.preventDefault();
            e.stopPropagation();

            // 확인 대화상자
            if (confirm('장바구니를 비우시겠습니까?')) {
                // 버튼 비활성화 (중복 클릭 방지)
                this.disabled = true;
                const originalHTML = this.innerHTML;
                this.innerHTML = '<i class="fa fa-spinner fa-spin"></i> 처리중...';

                // 장바구니 비우기 API 호출
                fetch('/carts/flush', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    }
                })
                    .then(function(response) {
                        console.log('응답 상태:', response.status);

                        if (!response.ok) {
                            throw new Error('장바구니 비우기에 실패했습니다.');
                        }

                        // 성공 시 페이지 새로고침
                        alert('장바구니가 비워졌습니다.');
                        location.reload();
                    })
                    .catch(function(error) {
                        console.error('Error:', error);
                        alert('장바구니 비우기 중 오류가 발생했습니다.');

                        // 버튼 복원
                        flushBtn.disabled = false;
                        flushBtn.innerHTML = originalHTML;
                    });
            }
        });
    }
}

// 페이지 로드 시 초기화
document.addEventListener('DOMContentLoaded', function() {
    initDeliveryPolicy();   // 배송 정책 초기화
    calculateCartTotal();   // 총합 계산
    plusButtons();          // 수량 증가 버튼
    minusButtons();         // 수량 감소 버튼
    removeButtons();        // 삭제 버튼
    orderButton();          // 주문 버튼
    flushCartButton();      // 장바구니 비우기 버튼
});