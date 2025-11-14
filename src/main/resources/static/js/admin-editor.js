document.addEventListener('DOMContentLoaded', function () {
    // 숨겨진 input 요소에서 초기 내용을 가져옵니다.
    const initialDescription = document.querySelector('#description').value;
    const initialIndex = document.querySelector('#index').value;

    // '상세 설명' 에디터 초기화
    const descriptionEditor = new toastui.Editor({
        el: document.querySelector('#description-editor'), // 에디터를 적용할 div
        initialValue: initialDescription,                  // 초기 내용 설정
        height: '400px',                                   // 에디터 높이
        initialEditType: 'wysiwyg',                        // 초기 모드 (wysiwyg 또는 markdown)
        previewStyle: 'vertical'                           // 마크다운 미리보기 스타일
    });

    // '목차' 에디터 초기화
    const indexEditor = new toastui.Editor({
        el: document.querySelector('#index-editor'),       // 에디터를 적용할 div
        initialValue: initialIndex,                        // 초기 내용 설정
        height: '250px',                                   // 에디터 높이
        initialEditType: 'wysiwyg',
        previewStyle: 'vertical'
    });

    // 폼(form) 제출(submit) 이벤트 처리
    document.querySelector('form').addEventListener('submit', function () {
        // 에디터의 현재 내용을 가져옵니다. (HTML 형식)
        const descriptionContent = descriptionEditor.getHTML();
        const indexContent = indexEditor.getHTML();

        // 숨겨진 input의 값을 에디터 내용으로 업데이트하여 서버로 전송되게 합니다.
        document.querySelector('#description').value = descriptionContent;
        document.querySelector('#index').value = indexContent;
    });
});
