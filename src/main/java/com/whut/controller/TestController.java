package com.whut.controller;


import com.whut.domain.Response;
import com.whut.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/test/")
public class TestController {

    @Autowired
    ImageService imageService ;

    @PostMapping("/imageUpload")
    @ResponseBody
    public String testBmp(HttpServletRequest request , MultipartFile image){

        Response response  = imageService.imageUpload(request, image    );

        return  response.toString() ;
    }

    @PostMapping("/jsonFileUpload")
    @ResponseBody
    public String testJsonFile(HttpServletRequest request , MultipartFile image){

//        Response response  = imageService.imageUpload(request, image    );






//        return  response.toString() ;

        return  null ;
    }








}
