package com.example.case_study.controller;

import com.example.case_study.dto.SearchDto;
import com.example.case_study.model.RealEstate;
import com.example.case_study.service.IRealEstateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/home")
public class HomeController {

    @Autowired
    private IRealEstateService realEstateService;

    @GetMapping("")
    public String home(Model model) {
        SearchDto searchDto = new SearchDto();
        model.addAttribute("searchDto", searchDto);
        return "/home/home";
    }

    @GetMapping("/search")
    public String searchRealEstate(@RequestParam(required = false) String location,
                                   @RequestParam(required = false) String type,
                                   @RequestParam(required = false) Short price,
                                   @RequestParam(required = false) Short area,
                                   Model model) {

        // Chuyển dữ liệu từ form thành SearchDto
        SearchDto searchDto = new SearchDto(location, type, price, area);

        // Thực hiện tìm kiếm bất động sản
        List<RealEstate> results = realEstateService.searchRealEstate(searchDto);

        // Thêm kết quả tìm kiếm vào model
        model.addAttribute("realEstates", results);
        model.addAttribute("searchDto", searchDto); // Truyền searchDto vào model để Thymeleaf hiển thị

        return "/home/search-results"; // Trả về trang home.html với kết quả tìm kiếm
    }

    // Phương thức chuyển đổi giá trị mức giá thành Double
//    private Double parsePrice(String price) {
//        if (price == null || price.isEmpty()) return null;
//        if (price.equals("0")) return 1000000000.0; // 1 tỷ
//        if (price.equals("1")) return 3000000000.0; // 3 tỷ
//        if (price.equals("2")) return 5000000000.0; // 5 tỷ
//        return null;
//    }

    // Phương thức chuyển đổi giá trị diện tích thành Double
//    private Double parseArea(String area) {
//        if (area == null || area.isEmpty()) return null;
//        if (area.equals("0-50")) return 50.0;
//        if (area.equals("50-100")) return 100.0;
//        return null;
//    }

}
