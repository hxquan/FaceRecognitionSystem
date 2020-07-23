package com.whut.api.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.whut.api.FaceRecognition;
import com.whut.util.HttpClient;
import org.apache.http.NameValuePair;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FaceRecognitionImpl implements FaceRecognition {

    private static final String face_landmarks_url = "http://127.0.0.1:5000/face_landmarks";
    private static final String face_locations_url = "http://127.0.0.1:5000/face_locations";
    private static final String face_encodings_url = "http://127.0.0.1:5000/face_encodings";

    @Override
    public double[] faceEncodings(String imageBase64) {
        JSONObject json = request(imageBase64, face_encodings_url);
        if (json == null || !json.getBoolean("success")){
            //error
            return null;
        }
        else {
            JSONArray encodings = json.getJSONArray("data");
            double[] res = new double[encodings.size()];
            for (int i = 0; i < encodings.size(); ++i){
                res[i] = encodings.getBigDecimal(i).doubleValue();
            }
            return res;
        }
    }

    @Override
    public String faceLocations(String imageBase64) {
        JSONObject json = request(imageBase64, face_locations_url);
        if (json == null || !json.getBoolean("success")){
            //error
        }
        else {
            JSONArray locations = json.getJSONArray("data");
        }
        return null;
    }

    @Override
    public String faceLandmarks(String imageBase64) {
        JSONObject json = request(imageBase64, face_landmarks_url);
        if (json == null || !json.getBoolean("success")){
            //error
        }
        else {
            JSONArray landmarks = json.getJSONArray("data");
        }
        return null;
    }

    private JSONObject request(String imageBase64, String face_landmarks_url) {
        Object [] params = new Object[]{"image_base64"};
        Object [] values = new Object[]{imageBase64};
        List<NameValuePair> paramsList = HttpClient.getParams(params, values);
        JSONObject res;
        try {
            res = (JSONObject) HttpClient.sendPost(face_landmarks_url, paramsList);
        } catch (Exception e) {
            return null;
        }
        if (res == null) {
            return null;
        }
        return res;
    }
}