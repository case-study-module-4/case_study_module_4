document.addEventListener("DOMContentLoaded", function() {
    const imageInput = document.getElementById("imageFiles");
    const imagePreview = document.getElementById("imagePreview");

    // Đảm bảo khung preview ẩn theo mặc định
    imagePreview.style.display = "none";

    imageInput.addEventListener("change", function() {
        // Xóa toàn bộ nội dung cũ của khung preview
        imagePreview.innerHTML = "";
        let validFilesCount = 0;
        const files = imageInput.files;

        // Duyệt qua từng file được chọn
        for (let i = 0; i < files.length; i++) {
            const file = files[i];
            // Nếu file có MIME type bắt đầu bằng "image/", xem là file hợp lệ
            if (file.type.match(/^image\//)) {
                validFilesCount++;
                // Tạo phần preview cho file ảnh hợp lệ
                const img = document.createElement("img");
                img.src = URL.createObjectURL(file);
                img.style.maxWidth = "200px";
                img.style.marginRight = "10px";
                img.style.marginBottom = "10px";
                imagePreview.appendChild(img);
            } else {
                // Nếu file không hợp lệ, hiển thị modal thông báo
                showModal("File '" + file.name + "' không hợp lệ. Chỉ cho phép upload file ảnh!");
            }
        }

        // Nếu có file hợp lệ thì hiển thị khung preview, nếu không thì ẩn và reset giá trị input
        if (validFilesCount > 0) {
            imagePreview.style.display = "block";
        } else {
            imagePreview.style.display = "none";
            imageInput.value = "";
        }
    });
});

/**
 * Hàm hiển thị modal thông báo sử dụng Bootstrap Modal
 * @param {string} message - Nội dung thông báo
 */
function showModal(message) {
    const modalElement = document.getElementById('fileTypeModal');
    modalElement.querySelector('.modal-body').textContent = message;
    const modal = new bootstrap.Modal(modalElement);
    modal.show();
}
