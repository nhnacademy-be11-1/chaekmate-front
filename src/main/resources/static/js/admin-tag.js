document.addEventListener('DOMContentLoaded', function () {
    document.querySelectorAll('.edit-tag-btn').forEach(button => {
        button.addEventListener('click', function () {
            const tagId = this.dataset.id;
            const currentName = this.dataset.name;

            const newName = prompt("새 태그 이름을 입력하세요:", currentName);

            if (newName && newName.trim() !== '' && newName !== currentName) {
                const form = document.createElement('form');
                form.method = 'post';
                form.action = `/admin/tags/${tagId}/update`;

                const nameInput = document.createElement('input');
                nameInput.type = 'hidden';
                nameInput.name = 'name';
                nameInput.value = newName;
                form.appendChild(nameInput);

                document.body.appendChild(form);
                form.submit();
            }
        });
    });
});