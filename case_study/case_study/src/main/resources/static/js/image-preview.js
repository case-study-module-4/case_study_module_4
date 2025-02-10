function initImagePreview(inputSelector, previewSelector) {
    const input = document.querySelector(inputSelector);
    const preview = document.querySelector(previewSelector);
    let dataTransfer = new DataTransfer();

    input.addEventListener('change', function () {
        for (let i = 0; i < input.files.length; i++) {
            dataTransfer.items.add(input.files[i]);
        }
        input.files = dataTransfer.files;
        updatePreview();
    });

    function updatePreview() {
        preview.innerHTML = '';
        for (let i = 0; i < dataTransfer.files.length; i++) {
            const file = dataTransfer.files[i];
            const reader = new FileReader();
            reader.onload = function(e) {
                const container = document.createElement('div');
                container.classList.add('image-container');

                const img = document.createElement('img');
                img.src = e.target.result;
                img.setAttribute('data-image', e.target.result);
                img.setAttribute('data-bs-toggle', 'modal');
                img.setAttribute('data-bs-target', '#imageModal');

                const btn = document.createElement('button');
                btn.type = 'button';
                btn.innerText = 'Xóa';
                btn.classList.add('btn', 'btn-danger', 'btn-sm', 'remove-btn');
                btn.addEventListener('click', function () {
                    removeFile(i);
                });

                container.appendChild(img);
                container.appendChild(btn);
                preview.appendChild(container);
            };
            reader.readAsDataURL(file);
        }
    }

    function removeFile(index) {
        const newDT = new DataTransfer();
        for (let i = 0; i < dataTransfer.files.length; i++) {
            if (i !== index) {
                newDT.items.add(dataTransfer.files[i]);
            }
        }
        dataTransfer = newDT;
        input.files = dataTransfer.files;
        updatePreview();
    }
}
