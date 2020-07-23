//package com.whut.util;
//
//import static org.bytedeco.javacpp.opencv_highgui.imshow;
//import static org.bytedeco.javacpp.opencv_highgui.waitKey;
//import static org.bytedeco.javacpp.opencv_imgcodecs.imread;
//
//import org.bytedeco.javacpp.opencv_core.Mat;
//
///**
// * @author caspar.chen
// * @date 2018-5-25
// */
//public class HelloJavaCV {
//
//    public static void main(String[] args) {
//        //读取原始图片
//        Mat image = imread("E:\\yourimg.jpg");
//        if (image.empty()) {
//            System.err.println("加载图片出错，请检查图片路径！");
//            return;
//        }
//        //显示图片
//        imshow("显示原始图像", image);
//
//        //无限等待按键按下
//        waitKey(0);
//    }
//}
