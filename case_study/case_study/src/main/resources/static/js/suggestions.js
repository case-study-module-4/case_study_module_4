document.addEventListener("DOMContentLoaded", () => {
    const priceInput = document.getElementById("price");
    const suggestions = document.getElementById("price-suggestions");

    priceInput.addEventListener("input", () => {
        const value = parseFloat(priceInput.value);
        suggestions.innerHTML = ""; // Xóa gợi ý cũ
        if (!isNaN(value)) {
            // Tạo gợi ý giá trị
            const suggestionValues = generateSuggestions(value);
            suggestionValues.forEach((sugg) => {
                const div = document.createElement("div");
                div.textContent = sugg;
                div.classList.add("price-suggestion");
                div.addEventListener("click", () => {
                    priceInput.value = sugg.replace(/[^\d.]/g, "").replace(",", ".");
                    suggestions.style.display = "none"; // Ẩn gợi ý sau khi chọn
                });
                suggestions.appendChild(div);
            });
            suggestions.style.display = "flex"; // Hiện danh sách gợi ý
        } else {
            suggestions.style.display = "none"; // Ẩn nếu không có giá trị
        }
    });
});

/**
 * Hàm tạo gợi ý giá trị theo logic
 */
function generateSuggestions(value) {
    const suggestions = [];
    // Tạo gợi ý giá trị theo các bội số
    suggestions.push(`${Math.round(value * 10).toLocaleString("vi-VN")} triệu`);
    suggestions.push(`${(value / 10).toFixed(1).replace(/\.0+$/, "").toLocaleString("vi-VN")} tỷ`);
    suggestions.push(`${Math.round(value * 1).toLocaleString("vi-VN")} tỷ`);
    return suggestions;
}
