package com.whut.service;

import com.whut.domain.Response;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;



@Service
public class ImageService {

    public Response imageUpload(HttpServletRequest request , MultipartFile image){

        Response response ;
        if (image == null){
            response = new Response(Response.Code.NoImageFileSelected);
            System.out.println(response.toString());
            return  response ;
        }

//        File file = new File(filePath);  //根据目录 路径
//        if (!file.exists()){
//            file.mkdirs();
//        }

        System.out.println(image.getOriginalFilename());
//        image.


         response = new Response(Response.Code.ImageStored);
         return  response ;

    }


}
