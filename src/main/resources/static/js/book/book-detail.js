$(document).ready(function() {
  // These variables are expected to be defined in the HTML before this script is loaded
  // const bookId = /* defined in HTML */;
  // const maxStock = /* defined in HTML */;

  var $quantityInput = $('#quantityInput');

  // 수량 감소
  $('.btn-minus').on('click', function() {
    var now = parseInt($quantityInput.val());
    if (now > 1) {
      $quantityInput.val(now = now - 1);
    }
  });

  // 수량 증가
  $('.btn-plus').on('click', function() {
    var now = parseInt($quantityInput.val());
    if (typeof maxStock !== 'undefined' && maxStock > 0 && now < maxStock) {
      $quantityInput.val(now = now + 1);
    } else if (typeof maxStock !== 'undefined' && maxStock === 0) {
      alert('현재 품절 상태입니다.');
    } else if (typeof maxStock !== 'undefined' && now >= maxStock) {
      alert('재고 수량을 초과할 수 없습니다. (최대 ' + maxStock + '권)');
    } else if (typeof maxStock === 'undefined') { // maxStock is not defined
        $quantityInput.val(now = now + 1); // 재고 무한 가정
    }
  });

  // 직접 입력 시 유효성 검사 (재고 및 최소 1)
  $quantityInput.on('change', function() {
    var now = parseInt($quantityInput.val());
    if (isNaN(now) || now < 1) {
      $quantityInput.val(1);
    } else if (typeof maxStock !== 'undefined' && maxStock > 0 && now > maxStock) {
      alert('재고 수량을 초과할 수 없습니다. (최대 ' + maxStock + '권)');
      $quantityInput.val(maxStock);
    }
  });

  // 품절 시 구매 버튼 비활성화
  if (typeof maxStock !== 'undefined' && maxStock === 0) {
    $('#addToCartBtn').prop('disabled', true).text('품절 (장바구니 불가)');
    $('#buyNowBtn').prop('disabled', true).text('품절 (주문 불가)');
    $('.btn-minus').prop('disabled', true);
    $('.btn-plus').prop('disabled', true);
  }
});

// 장바구니 담기 버튼 클릭 이벤트
$('#addToCartBtn').on('click', function() {
  // bookId is expected to be defined in the HTML
  var quantity = parseInt($('#quantityInput').val());
  // 실제 장바구니 로직 (AJAX 호출 등)을 여기에 구현해!
  alert('도서 ID: ' + bookId + ', 수량: ' + quantity + '권을 장바구니에 담았습니다! (실제 로직 구현 필요)');
});

// 바로 주문 버튼 클릭 이벤트
$('#buyNowBtn').on('click', function() {
  // bookId is expected to be defined in the HTML
  var quantity = parseInt($('#quantityInput').val());
  // 실제 바로 주문 로직 (결제 페이지 이동 등)을 여기에 구현해!
  alert('도서 ID: ' + bookId + ', 수량: ' + quantity + '권으로 바로 주문을 진행합니다! (실제 로직 구현 필요)');
  // 예: location.href = '/orders/checkout?bookId=' + bookId + '&quantity=' + quantity;
});
