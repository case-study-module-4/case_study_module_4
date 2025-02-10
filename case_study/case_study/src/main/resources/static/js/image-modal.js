document.addEventListener("DOMContentLoaded", function () {
    let images = [];
    let currentIndex = 0;

    // Lấy các phần tử cần thiết
    const modalImage = document.getElementById("modalImage");
    const imageModal = document.getElementById("imageModal");

    // Lấy tất cả các phần tử có thuộc tính data-image (đảm bảo cả 2 trang sử dụng thuộc tính này)
    const imageLinks = document.querySelectorAll('[data-image]');
    images = Array.from(imageLinks).map(link => link.getAttribute('data-image'));

    // Khi modal được mở, cập nhật ảnh dựa trên phần tử kích hoạt
    imageModal.addEventListener('show.bs.modal', function (event) {
        const triggerElement = event.relatedTarget;

        // Cập nhật lại danh sách ảnh trước khi hiển thị modal
        const updatedImageLinks = document.querySelectorAll('[data-image]');
        images = Array.from(updatedImageLinks).map(link => link.getAttribute('data-image'));

        const imageUrl = triggerElement.getAttribute("data-image");
        currentIndex = images.indexOf(imageUrl);
        modalImage.src = imageUrl;
    });

    // Hàm cập nhật ảnh trong modal
    function updateImage(newIndex) {
        if (newIndex < 0) {
            newIndex = images.length - 1;
        } else if (newIndex >= images.length) {
            newIndex = 0;
        }
        currentIndex = newIndex;
        modalImage.src = images[currentIndex];
    }

    // Gán sự kiện cho các nút chuyển ảnh
    document.getElementById("prevImage").addEventListener("click", function () {
        updateImage(currentIndex - 1);
    });
    document.getElementById("nextImage").addEventListener("click", function () {
        updateImage(currentIndex + 1);
    });
});
