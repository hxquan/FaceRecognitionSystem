//package com.whut.util; /**
// * 	此代码转自他人博客, 自己加了点注释, 自己使用过, 可以使用
// */
//import java.io.File;
//import java.util.ArrayList;
//
//import org.jim2mov.core.DefaultMovieInfoProvider;
//import org.jim2mov.core.FrameSavedListener;
//import org.jim2mov.core.ImageProvider;
//import org.jim2mov.core.Jim2Mov;
//import org.jim2mov.core.MovieInfoProvider;
//import org.jim2mov.core.MovieSaveException;
//import org.jim2mov.utils.MovieUtils;
//
////
//public class FilesToMov implements ImageProvider, FrameSavedListener{
//    // 文件数组
//    private ArrayList<String> fileArray;
//    // 文件类型
//    private int type;
//    // 主函数
//    public static void main(String[] args) throws MovieSaveException {
//        ArrayList<String> fileArray = new ArrayList<>();
//        // 图片路径
//        File[] listFiles = new File("/Users/yy/Downloads/图片/yangyun").listFiles();
//
//        for (int i = 0; i < listFiles.length; i++) {
//            fileArray.add(listFiles[i].getAbsolutePath());
//        }
//        new FilesToMov(fileArray, MovieInfoProvider.TYPE_QUICKTIME_JPEG, "t.avi");
//    }
//    /**
//     * 	图片转视频
//     * @param fileArray 文件路径数组
//     * @param type 格式
//     * @param path 文件名
//     * @throws MovieSaveException
//     */
//    public FilesToMov(ArrayList<String> fileArray, int type, String path) throws MovieSaveException {
//        this.fileArray = fileArray;
//        this.type = type;
//        DefaultMovieInfoProvider dmip = new DefaultMovieInfoProvider(path);
//        // 设置帧频率
//        dmip.setFPS(7);
//        // 设置帧数--一张图片一帧
//        dmip.setNumberOfFrames(fileArray.size());
//        // 设置视频高度
//        dmip.setMWidth(320);
//        // 设置视频宽度
//        dmip.setMHeight(240);
//        new Jim2Mov(this, dmip, this).saveMovie(this.type);;
//    }
//	// 每一张图片都会调一次此方法
//    @Override
//    public void frameSaved(int frameNumber) {
//        System.out.println("Saved frame: " + frameNumber);
//    }
//
//    @Override
//    public byte[] getImage(int frame) {
//        try {
//            return MovieUtils.convertImageToJPEG(new File(fileArray.get(frame)), 1.0f);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//}
