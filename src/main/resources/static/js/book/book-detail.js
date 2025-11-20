$(document).ready(function() {
    let $quantityInput = $('#quantityInput');
    let $quantityButtons = $('.quantity button');
    let $addToCartBtn = $('#addToCartBtn');
    let $buyNowBtn = $('#buyNowBtn');

    // bookId와 maxStock을 HTML 요소에서 직접 가져와 스코프를 제한합니다.
    let bookId = $addToCartBtn.data('book-id');
    let maxStock = Number.parseInt($addToCartBtn.data('stock')) || 0;

    // main.js 등 다른 스크립트에서 이미 바인딩된 클릭 핸들러를 제거하여 중복 실행을 방지합니다.
    $quantityButtons.off('click');

    // 수량 감소
    $('.btn-minus').on('click', function() {
        let now = Number.parseInt($quantityInput.val());
        if (now > 1) {
            $quantityInput.val(now - 1);
        }
    });

    // 수량 증가
    $('.btn-plus').on('click', function() {
        let now = Number.parseInt($quantityInput.val());
        if (maxStock > 0 && now < maxStock) { // 재고가 있고 현재 수량이 재고보다 적을 때만 증가
            $quantityInput.val(now + 1);
        } else if (maxStock === 0) { // 품절 상태
            alert('현재 품절 상태입니다.');
        } else if (now >= maxStock) { // 재고 초과
            alert('재고 수량을 초과할 수 없습니다. (최대 ' + maxStock + '권)');
        }
        // maxStock이 정의되지 않은 경우는 고려하지 않음 (HTML에서 항상 정의된다고 가정)
    });

    // 직접 입력 시 유효성 검사 (재고 및 최소 1)
    $quantityInput.on('change', function() {
        let now = Number.parseInt($quantityInput.val());
        if (Number.isNaN(now) || now < 1) {
            $quantityInput.val(1);
        } else if (maxStock > 0 && now > maxStock) {
            alert('재고 수량을 초과할 수 없습니다. (최대 ' + maxStock + '권)');
            $quantityInput.val(maxStock);
        }
    });

    // 품절 시 구매 버튼 비활성화
    if (maxStock === 0) {
        $addToCartBtn.prop('disabled', true).text('품절 (장바구니 불가)');
        $buyNowBtn.prop('disabled', true).text('품절 (주문 불가)');
        $('.btn-minus').prop('disabled', true);
        $('.btn-plus').prop('disabled', true);
    }

    // 장바구니 담기 버튼 클릭 이벤트
    $addToCartBtn.on('click', function(e) {
        e.preventDefault();
        e.stopPropagation();

        let quantity = Number.parseInt($quantityInput.val());

        if (!bookId) {
            alert('도서 정보를 찾을 수 없습니다.');
            return;
        }

        // 버튼 비활성화 (중복 클릭 방지)
        $(this).prop('disabled', true);
        let originalHTML = $(this).html();
        $(this).html('<i class="fa fa-spinner fa-spin mr-1"></i> 처리 중...');

        // 장바구니 담기 요청
        fetch('/carts/items', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                bookId: Number(bookId),
                quantity: quantity
            })
        })
            .then(function(response) {
                console.log('응답 상태:', response.status);

                if (!response.ok) {
                    return response.json().then(function(errorData) {
                        // 에러 응답을 throw
                        throw {
                            status: response.status,
                            data: errorData
                        };
                    }).catch(function(error) {
                        // JSON 파싱 실패 시 기본 에러
                        if (error.data) {
                            throw error;
                        }
                        throw new Error('장바구니 담기 실패');
                    });
                }

                return response.json();
            })
            .then(function(data) {
                console.log('장바구니 담기 성공:', data);

                // 성공 알림
                if (confirm(quantity + '권이 장바구니에 담겼습니다.\n장바구니로 이동하시겠습니까?')) {
                    globalThis.location.href = '/carts';
                } else {
                    // 버튼 복원
                    $addToCartBtn.prop('disabled', false);
                    $addToCartBtn.html(originalHTML);
                }
            })
            .catch(function(error) {
                console.error('에러 발생:', error);

                let errorMessage = '장바구니 담기 중 에러가 발생했습니다.';

                // 서버에서 받은 에러 메시지 처리
                if (error.data && error.data.message) {
                    errorMessage = error.data.message;
                } else if (error.message) {
                    errorMessage = error.message;
                }

                alert(errorMessage);

                // 에러 발생 시 버튼 복원
                $addToCartBtn.prop('disabled', false);
                $addToCartBtn.html(originalHTML);
            });
    });

    // 바로 주문 버튼 클릭 이벤트
    $buyNowBtn.on('click', function() {
        let quantity = Number.parseInt($quantityInput.val());
        // 실제 바로 주문 로직 (결제 페이지 이동 등)을 여기에 구현해!
        alert('도서 ID: ' + bookId + ', 수량: ' + quantity + '권으로 바로 주문을 진행합니다! (실제 로직 구현 필요)');
        // 예: location.href = '/orders/checkout?bookId=' + bookId + '&quantity=' + quantity;
    });
});