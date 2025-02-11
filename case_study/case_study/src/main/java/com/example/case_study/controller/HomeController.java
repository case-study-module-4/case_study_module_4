package com.example.case_study.controller;

import com.example.case_study.dto.PostDTO;
import com.example.case_study.dto.SearchDto;
import com.example.case_study.model.RealEstate;
import com.example.case_study.service.IPostService;
import com.example.case_study.service.IRealEstateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/home")
public class HomeController {

    @Autowired
    private IRealEstateService realEstateService;

    @Autowired
    private IPostService postService;

    @GetMapping("")
    public String home(Model model) {
        SearchDto searchDto = new SearchDto();
        List<PostDTO> posts = postService.getListDefault();
        model.addAttribute("posts", posts);
        model.addAttribute("postDto", searchDto);
        return "home/home";
    }

//    @GetMapping("/search")
//    public String searchRealEstate(@RequestParam(required = false) String location,
//                                   @RequestParam(required = false) String type,
//                                   @RequestParam(required = false) Short price,
//                                   @RequestParam(required = false) Short area,
//                                   Model model) {
//
//        // Chuyển dữ liệu từ form thành SearchDto
//        SearchDto searchDto = new SearchDto(location, type, price, area);
//
//        // Thực hiện tìm kiếm bất động sản
//        List<RealEstate> results = realEstateService.searchRealEstate(searchDto);
//
//        // Thêm kết quả tìm kiếm vào model
//        model.addAttribute("realEstates", results);
//        model.addAttribute("searchDto", searchDto); // Truyền searchDto vào model để Thymeleaf hiển thị
//
//        return "/home/search-results"; // Trả về trang home.html với kết quả tìm kiếm
//    }


    @GetMapping("/search")
    public String searchPosts(
            @RequestParam(name = "location", required = false) String location,
            @RequestParam(name = "type", required = false) String type,
            @RequestParam(name = "price", required = false) String price,
            @RequestParam(name = "area", required = false) String area,
            Model model
    ) {
        List<PostDTO> posts = postService.searchPosts(location, type, price, area);
        model.addAttribute("posts", posts);
        model.addAttribute("postDto", new PostDTO());
        return "/home/home";
    }


}
