package com.spring;

import com.spring.pojo.AddBlogInfoRequest;
import com.spring.pojo.BlogInfoResponse;
import com.spring.pojo.Result;
import com.spring.pojo.UpBlogRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "blog-service", path = "/blog")
public interface BlogServiceAPI {

    @RequestMapping("/getList")
    Result<List<BlogInfoResponse>> getList();

    @RequestMapping("/getBlogDetail")
    Result<BlogInfoResponse> getBlogDeatail(@RequestParam("blogId") Integer blogId);

    @RequestMapping("/add")
    Result<Boolean> addBlog(@Validated @RequestBody AddBlogInfoRequest addBlogInfoRequest);

    /**
     * 更新博客
     */
    @RequestMapping("/update")
    Result<Boolean> updateBlog(@RequestBody UpBlogRequest upBlogRequest);

    @RequestMapping("/delete")
    Result<Boolean> deleteBlog(@RequestParam("blogId") Integer blogId);

}
