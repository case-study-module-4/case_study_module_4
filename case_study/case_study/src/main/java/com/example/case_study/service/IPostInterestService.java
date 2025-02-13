package com.example.case_study.service;

import com.example.case_study.model.Account;
import com.example.case_study.model.PostInterest;

import java.util.List;

public interface IPostInterestService extends IService<PostInterest> {
    //     Lưu thông tin khi người dùng nhấn vào bài đăng.
    void logPostClick(Integer postId, Account account);

    List<PostInterest> getPostInterestsByOwnerId(Integer userId);
}
