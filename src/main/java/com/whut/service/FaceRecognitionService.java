package com.whut.service;

import com.whut.api.FaceRecognition;
import com.whut.dao.FaceInfoRepository;
import com.whut.dao.entity.FaceInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.List;

@Service
public class FaceRecognitionService {

    @Autowired
    FaceRecognition faceRecognition;
    @Autowired
    FaceInfoRepository faceInfoRepository;

    private static final double MAX_TOLERANCE = 0.41;

    public boolean saveFaceInfo(String username, String base64Image)
    {
        double[] encodings = faceRecognition.faceEncodings(base64Image);
        if (encodings == null){
            //图片不清晰，人脸不完全
            return false;
        }
        String encodingStr = Arrays.toString(encodings);
        encodingStr = encodingStr.substring(1, encodingStr.length()-1);
        FaceInfo faceInfo = faceInfoRepository.findByUsername(username);
        if (faceInfo == null){
            faceInfo = new FaceInfo(encodingStr, base64Image, username);
        }else {
            faceInfo.setFaceEncodings(encodingStr);
            faceInfo.setBase64Image(base64Image);
        }
        faceInfoRepository.save(faceInfo);
        return true;
    }

    public FaceInfo recognition(String base64Image)
    {
        double[] encodings = faceRecognition.faceEncodings(base64Image);
        if (encodings == null){
            //图片不清晰，人脸不完全
            return null;
        }
        List<FaceInfo> faceInfoList = faceInfoRepository.findAll();
        return findClosest(faceInfoList, encodings);
    }

    private FaceInfo findClosest(List<FaceInfo> faceInfoList, double[] p){
        int pos = -1;
        double min = Double.MAX_VALUE;
        for (int i = 0; i < faceInfoList.size(); ++i){
            FaceInfo faceInfo = faceInfoList.get(i);
            double[] p1 = conver(faceInfo.getFaceEncodings());
            double dis = faceDistance(p, p1);
            if (dis < min){
                min = dis;
                pos = i;
            }
        }
        if (pos == -1 || min > MAX_TOLERANCE){
            FaceInfo faceNotRecog = new FaceInfo(null,null,"error");

            return faceNotRecog;
        }
        return faceInfoList.get(pos);
    }

    private double[] conver(String encodings){
        String[] points = encodings.split(",");
        double[] arr = new double[points.length];
        for (int i = 0; i < points.length; ++i){
            arr[i] = Double.parseDouble(points[i]);
        }
        return arr;
    }

    private double faceDistance(double[] p1, double[] p2){
        double dis = 0;
        for (int i = 0; i < p1.length && i < p2.length; ++i){
            dis += Math.pow(p1[i] - p2[i], 2);
        }
        return Math.pow(dis, 0.5);
    }

}
