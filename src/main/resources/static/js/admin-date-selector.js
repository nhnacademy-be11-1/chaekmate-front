document.addEventListener("DOMContentLoaded", function() {
    const input = document.querySelector(".datetime-picker");

    // 기존 값 저장
    let initialValue = input.value;

    // flatpickr 인스턴스
    const fp = flatpickr(input, {
        enableTime: true,
        dateFormat: String.raw`Y-m-d\TH:i`,
        defaultDate: initialValue || null,
        allowInput: true,
        clickOpens: false, // 클릭으로 열리지 않게
        onClose: function(selectedDates) {
            if (!selectedDates.length) {
                // 선택 취소시 기존 값 복원
                input.value = initialValue;
            }
        }
    });

    // 바꾸기 버튼
    document.querySelector(".datetime-changer").addEventListener("click", function() {
        fp.open();
    });

    // 초기화 버튼
    document.querySelector(".datetime-clear").addEventListener("click", function() {
        input.value = "";
        initialValue = "";
    });

    // 날짜 선택 완료 시 실제 값 업데이트
    fp.config.onChange.push(function(selectedDates, dateStr) {
        if (selectedDates.length) {
            initialValue = dateStr; // 확정값으로 저장
        }
    });
});
