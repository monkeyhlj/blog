package com.monkey.web;

import com.monkey.NotFindException;
import com.monkey.service.BlogService;
import com.monkey.service.TagsService;
import com.monkey.service.TypeService;
import com.monkey.vo.BlogQuery;
import com.sun.deploy.net.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.swing.*;
import java.io.IOException;

@Controller
public class IndexController {

   /* @GetMapping("/{id}/{name}")
    public String index(@PathVariable Integer id,@PathVariable String name){
       // int i=9/0; //error
        //*Spring blog = null; //404
        if(blog == null){
            throw  new NotFindException("博客不存在"); //自定义异常
        }*//*
        System.out.println("-----------index--------------");
        return "index";
    }*/

    @Autowired
    BlogService blogService;
    @Autowired
    TypeService typeService;
    @Autowired
    TagsService tagService;

    /**
     * 首页展示渲染
     *
     * @param pageable
     * @param model
     * @return
     */
    @GetMapping("/")
    public String index(@PageableDefault(size = 3, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                        Model model) {
        model.addAttribute("page", blogService.listBlog(pageable));
        model.addAttribute("types", typeService.listTypeTop(6));
        model.addAttribute("tags", tagService.listTagTop(10));
        model.addAttribute("recommendBlogs", blogService.listRecommendBlogTop(8));
        return "index";
    }

    /**
     * 搜索方法
     *
     * @param pageable
     * @param model
     * @return
     */
    @PostMapping("/search")
    public String search(@PageableDefault(size = 8, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                         @RequestParam String query, Model model) {

        model.addAttribute("page", blogService.listBlog("%" + query + "%", pageable));
        model.addAttribute("query", query);
        return "search";
    }

    /**
     * 博客详情
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/blog/{id}")
    public String blog(@PathVariable Long id,Model model){
        model.addAttribute("blog",blogService.getAndconvert(id));
        return "blog";
    }

    @GetMapping("/footer/newblog")
    public String newblogs(Model model) {
        model.addAttribute("newblogs", blogService.listRecommendBlogTop(3));
        return "_fragments :: newblogList";
    }

}
