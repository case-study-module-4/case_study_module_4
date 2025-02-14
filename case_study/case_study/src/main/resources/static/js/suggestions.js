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
                    let numberValue;
                    // Kiểm tra đơn vị của chuỗi gợi ý và chuyển đổi tương ứng
                    if (sugg.includes("tỷ")) {
                        const num = parseFloat(sugg.replace("tỷ", "").trim().replace(",", "."));
                        numberValue = num * 1000000000;
                    } else if (sugg.includes("triệu")) {
                        const num = parseFloat(sugg.replace("triệu", "").trim().replace(",", "."));
                        numberValue = num * 1000000;
                    }
                    // Định dạng giá trị theo định dạng "1,234,567"
                    priceInput.value = numberValue.toLocaleString("en-US");
                    suggestions.style.display = "none";
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
