package com.whut.api.impl;


import com.whut.api.DetectionApi;
import com.whut.util.HttpClient;
import org.apache.http.NameValuePair;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DetectionApiImpl implements DetectionApi {

    private static final String detect_by_images_URL = "http://127.0.0.1:5000/detect_by_images";

    @Override
    public String detectByImages(String path, int count) {
        Object [] params = new Object[]{"path", "count"};
        Object [] values = new Object[]{path, count};
        List<NameValuePair> paramsList = HttpClient.getParams(params, values);
        String res;
        try {
            res = (String) HttpClient.sendPost(detect_by_images_URL, paramsList);
        } catch (Exception e) {
            return null;
        }
        if (res == null) {
            return null;
        }
        return res;
    }
}