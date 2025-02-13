// Kết nối tới server qua WebSocket
const socket = new SockJS('/ws'); // Endpoint WebSocket
const stompClient = Stomp.over(socket);

// Khi kết nối thành công
stompClient.connect({}, function () {
    console.log('WebSocket connected');

    // Lấy username người dùng hiện tại từ DOM
    const username = document.getElementById('loggedInUsername').value;

    // Lắng nghe thông báo từ server cho user cụ thể
    stompClient.subscribe(`/user/${username}/queue/notifications`, function (message) {
        const notification = message.body;

        // Hiển thị thông báo
        addNotificationToHeader(notification);
    });
});

// Hàm thêm thông báo vào thanh header
function addNotificationToHeader(notification) {
    const notificationList = document.getElementById('notification-list');
    const newNotification = document.createElement('li');
    newNotification.textContent = notification;
    notificationList.appendChild(newNotification);

    // Hiển thị số thông báo chưa đọc
    const countBadge = document.getElementById('notification-count');
    countBadge.classList.remove('hidden');
    countBadge.textContent = parseInt(countBadge.textContent || '0') + 1;
}
