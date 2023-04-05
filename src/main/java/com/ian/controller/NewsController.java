package com.ian.controller;

import com.ian.common.Result;
import com.ian.service.impl.NewsService;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/news")
public class NewsController {
    @Resource
    private NewsService newsService;

    @PostMapping("/page")
    public Result page(Pageable pageable) {
        return Result.success(newsService.page(pageable));
    }
}
