package com.whut.api;

public interface DetectionApi {

    //人脸图片转code
    String detectByImages(String path, int count);
}