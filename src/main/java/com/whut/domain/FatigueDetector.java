package com.whut.domain;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.whut.api.DetectionApi;
import com.whut.api.impl.DetectionApiImpl;
import com.whut.util.Java2MatTools;
import org.apache.log4j.Logger;


import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.videoio.VideoWriter;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FatigueDetector {

    private static final Logger LOGGER = Logger.getLogger(VideoWriter.class);

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    private String username;

    public FatigueDetector(String username) {
        this.username = username;
    }

    private static class P {
        int x;
        int y;

        public P(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private static final int[] leftEyeIndex = new int[]{36, 37, 38, 39, 40, 41};
    private static final int[] rightEyeIndex = new int[]{42, 43, 44, 45, 46, 47};

    //眼睛
    private static final double thresh = 0.3;
    private static final int MAX_IMAGE_SIZE = 1000;

    private List<Double> list = new ArrayList<>(MAX_IMAGE_SIZE);
    private DetectionApi detectionApi = new DetectionApiImpl();
    private VideoWriter writer = new VideoWriter();

    private long count = 0;

    private String filename;

    public void writeVideo(String base64, int width, int height) {
        if (count++ % MAX_IMAGE_SIZE == 0) {
            if (writer.isOpened()) {
                writer.release();
            }
            filename = String.format("/Users/yy/Downloads/图片/%s/%d.avi", username, (count / MAX_IMAGE_SIZE) + 1);
            writer.open(filename, VideoWriter.fourcc('M', 'J', 'P', 'G'), 15, new Size(width, height));
        }
        try {
            Mat mat = Java2MatTools.base642Mat(base64);
            writer.write(mat);
        } catch (IOException e) {
            LOGGER.error(e);
        }
    }

    public double degree(String base64, int width, int height) {


        JSONArray array = JSONArray.parseArray(base64);

        if (array.size() == 0) {
            return 0.0;
        }

        P[] landmarks = new P[array.size()];
        for (int i = 0; i < array.size(); ++i) {
            JSONObject object = array.getJSONObject(i);
            landmarks[i] = new P(object.getInteger("x"), object.getInteger("y"));
        }

        double ear = eyeAspectRatio(landmarks);
        list.add(ear);
        if (list.size() == MAX_IMAGE_SIZE) {
            double blink = getBlink();
            list.remove(0);
            return (blink / MAX_IMAGE_SIZE) * 3;
        }
        return 0.0;
    }

    private int getBlink() {
        int blink = 0;
        for (Double aList : list) {
            if (aList < thresh) {
                blink++;
            }
        }
        return blink;
    }

    /**
     * 计算眼睛的睁开度
     *
     * @param landmarks
     * @return
     */
    private double eyeAspectRatio(P[] landmarks) {

        double a = dist(landmarks[leftEyeIndex[1]], landmarks[leftEyeIndex[5]]);
        double b = dist(landmarks[leftEyeIndex[2]], landmarks[leftEyeIndex[4]]);
        double c = dist(landmarks[leftEyeIndex[0]], landmarks[leftEyeIndex[3]]);
        double left = (a + b) / 2 / c;
        a = dist(landmarks[rightEyeIndex[1]], landmarks[rightEyeIndex[5]]);
        b = dist(landmarks[rightEyeIndex[2]], landmarks[rightEyeIndex[4]]);
        c = dist(landmarks[rightEyeIndex[0]], landmarks[rightEyeIndex[3]]);
        double right = (a + b) / 2 / c;
        return (left + right) / 2.0;
    }


    private double dist(P p1, P p2) {
        double a = Math.pow(p1.x - p2.x, 2);
        double b = Math.pow(p1.y - p2.y, 2);
        return Math.pow(a + b, 0.5);
    }


}
