const citySelect = document.getElementById("city");
const districtSelect = document.getElementById("district");
const wardSelect = document.getElementById("ward");

// Hàm tải tỉnh/thành phố
async function loadCities() {
    const response = await fetch("https://provinces.open-api.vn/api/p/");
    const cities = await response.json();

    citySelect.innerHTML = '<option value="">Chọn tỉnh/thành phố</option>';
    cities.forEach(city => {
        const option = document.createElement("option");
        option.value = city.code;
        option.textContent = city.name;
        citySelect.appendChild(option);
    });
}

// Hàm tải quận/huyện
async function loadDistricts() {
    const cityCode = citySelect.value;
    if (!cityCode) {
        districtSelect.innerHTML = '<option value="">Chọn quận/huyện</option>';
        wardSelect.innerHTML = '<option value="">Chọn phường/xã</option>';
        return;
    }

    const response = await fetch(`https://provinces.open-api.vn/api/p/${cityCode}?depth=2`);
    const cityData = await response.json();

    districtSelect.innerHTML = '<option value="">Chọn quận/huyện</option>';
    cityData.districts.forEach(district => {
        const option = document.createElement("option");
        option.value = district.code;
        option.textContent = district.name;
        districtSelect.appendChild(option);
    });

    wardSelect.innerHTML = '<option value="">Chọn phường/xã</option>';
}

// Hàm tải phường/xã
async function loadWards() {
    const districtCode = districtSelect.value;
    if (!districtCode) {
        wardSelect.innerHTML = '<option value="">Chọn phường/xã</option>';
        return;
    }

    const response = await fetch(`https://provinces.open-api.vn/api/d/${districtCode}?depth=2`);
    const districtData = await response.json();

    wardSelect.innerHTML = '<option value="">Chọn phường/xã</option>';
    districtData.wards.forEach(ward => {
        const option = document.createElement("option");
        option.value = ward.code;
        option.textContent = ward.name;
        wardSelect.appendChild(option);
    });
}

// Gọi hàm khi load trang
loadCities();