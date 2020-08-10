package com.whut.api;

import java.math.BigDecimal;
import java.util.List;

public interface FaceRecognition {

    double[] faceEncodings(String imageBase64);
    String faceLocations(String imageBase64);
    String faceLandmarks(String imageBase64);


}
