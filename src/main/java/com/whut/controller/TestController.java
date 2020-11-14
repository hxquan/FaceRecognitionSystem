package com.whut.controller;

import com.whut.domain.Response;
import com.whut.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/test/")
public class TestController {

    @Autowired
    ImageService imageService;

    @PostMapping("imageUpload")
    @ResponseBody
    public String testBmp(@RequestParam("file") MultipartFile file,HttpServletRequest request) {

        if (!file.isEmpty()) {
            try {
                BASE64Encoder encoder = new BASE64Encoder();
                // 通过base64来转化图片
                String data = encoder.encode(file.getBytes());
//                System.out.println(data);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        System.out.println(request.getParameter("name"));


        return null;
    }

//        Response response  = imageService.imageUpload(request, image);
//
//        return  response.toString() ;


    @PostMapping("/jsonFileUpload")
    @ResponseBody
    public String testJsonFile(HttpServletRequest request, MultipartFile image) {

//        Response response  = imageService.imageUpload(request, image    );

//        return  response.toString() ;

        return null;
    }

}
