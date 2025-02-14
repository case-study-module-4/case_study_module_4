function initImagePreview(inputSelector, previewSelector) {
    const input = document.querySelector(inputSelector);
    const preview = document.querySelector(previewSelector);
    let dataTransfer = new DataTransfer();

    // Ẩn khung preview theo mặc định
    preview.style.display = "none";

    input.addEventListener('change', function () {
        // Reset DataTransfer và khung preview
        dataTransfer = new DataTransfer();
        preview.innerHTML = "";
        let validFilesCount = 0;

        // Duyệt qua từng file được chọn
        for (let i = 0; i < input.files.length; i++) {
            const file = input.files[i];
            // Kiểm tra file có phải là ảnh hay không
            if (file.type.match(/^image\//)) {
                validFilesCount++;
                dataTransfer.items.add(file);
            } else {
                // Nếu file không hợp lệ, hiển thị modal thông báo
                showModal("File '" + file.name + "' không hợp lệ. Chỉ cho phép upload file ảnh!");
            }
        }
        // Cập nhật lại input.files với các file hợp lệ
        input.files = dataTransfer.files;

        // Hiển thị hoặc ẩn khung preview dựa trên số file hợp lệ
        preview.style.display = validFilesCount > 0 ? "block" : "none";

        updatePreview();
    });

    function updatePreview() {
        // Xóa nội dung cũ của khung preview
        preview.innerHTML = "";
        for (let i = 0; i < dataTransfer.files.length; i++) {
            const file = dataTransfer.files[i];
            const reader = new FileReader();
            reader.onload = function (e) {
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

    function showModal(message) {
        const modalElement = document.getElementById('fileTypeModal');
        if (modalElement) {
            const modalBody = modalElement.querySelector('.modal-body');
            if (modalBody) {
                modalBody.textContent = message;
            }
            const modal = new bootstrap.Modal(modalElement);
            modal.show();
        } else {
            alert(message);
        }
    }
}
