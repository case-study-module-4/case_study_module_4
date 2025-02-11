document.addEventListener("DOMContentLoaded", function () {
    const ctx = document.getElementById('directionChart').getContext('2d');
    const directionInput = document.getElementById("direction");

    const directions = ["Đông", "Đông Nam", "Nam", "Tây Nam", "Tây", "Tây Bắc", "Bắc", "Đông Bắc"];
    // Đặt màu nền trắng cho các phần, thay đổi khi chọn
    const colors = Array(directions.length).fill("#ffffff");

    let selectedIndex = null;

    const chart = new Chart(ctx, {
        type: 'doughnut',
        data: {
            labels: directions,
            datasets: [{
                data: Array(directions.length).fill(1),
                backgroundColor: colors,
                borderColor: '#000',
                borderWidth: 1,
                offset: 10,
            }]
        },
        options: {
            responsive: true,
            // Điều chỉnh góc quay của hình tròn ở đây:
            rotation: -Math.PI / 5, // Ví dụ: quay sao cho phần đầu tiên nằm ở vị trí 12 giờ.
            cutout: "50%", // Tạo rỗng ở giữa
            plugins: {
                legend: {
                    display: false // Ẩn chú thích mặc định
                }
            },
            onClick: (event, elements) => {
                if (elements.length > 0) {
                    const index = elements[0].index;
                    selectedIndex = index;
                    directionInput.value = directions[index];

                    // Cập nhật màu: phần được chọn đổi màu
                    chart.data.datasets[0].backgroundColor = colors.map((color, i) =>
                        i === selectedIndex ? "#cc0000" : "#ffffff"
                    );
                    chart.update();
                }
            }
        },
        plugins: [{
            afterDraw: function (chart) {
                const ctx = chart.ctx;
                ctx.save();

                const meta = chart.getDatasetMeta(0);
                const innerRadius = meta.data[0].innerRadius;
                const outerRadius = meta.data[0].outerRadius;
                const centerX = chart.chartArea.left + chart.chartArea.width / 2;
                const centerY = chart.chartArea.top + chart.chartArea.height / 2;
                // Tính vị trí trung gian giữa inner và outer, có thể cộng thêm offset nếu cần
                const radius = (innerRadius + outerRadius) / 2 + 10;

                ctx.font = "bold 14px Arial";
                ctx.fillStyle = "#000";
                ctx.textAlign = "center";
                ctx.textBaseline = "middle";

                directions.forEach((direction, index) => {
                    // Lưu ý: Nếu bạn muốn thay đổi vị trí của chữ, hãy điều chỉnh offset bán kính hoặc góc offset ở đây.
                    const angle = (index / directions.length) * (2 * Math.PI) - Math.PI / 2.5;
                    const x = centerX + Math.cos(angle) * radius;
                    const y = centerY + Math.sin(angle) * radius;
                    ctx.fillText(direction, x, y);
                });

                ctx.restore();
            }
        }]
    });
    initDirectionChart();
});