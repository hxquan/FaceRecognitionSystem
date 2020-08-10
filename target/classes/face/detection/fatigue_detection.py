import base64
import os
import cv2
from scipy.spatial import distance as dist
from imutils import face_utils
import numpy as np
import imutils
import dlib

import face_recognition
from flask import Flask, jsonify, request


class FatigueDetection:
    eye_ar_thresh = 0.25
    eye_ar_consec_frames = 48
    mou_ar_thresh = 0.70
    mou_ar_consec_frames = 20
    counter = 0
    yawnStatus = False
    yawns = 0
    count = 0
    YC = 0

    def fatigue_detection(self, picture):
        # camera = request.files.get['vedio']
        #   print(type(camera))
        # frame = request.files['picture']

        frame = picture
        frame = face_recognition.load_image_file(frame)

        predictor_path = "shape_predictor_68_face_landmarks.dat"
        # 初始化dlib的面部检测器（基于HOG），然后创建面部界标预测器
        detector = dlib.get_frontal_face_detector()
        predictor = dlib.shape_predictor(predictor_path)
        # 得到眼睛及嘴巴的索引值
        (lStart, lEnd) = face_utils.FACIAL_LANDMARKS_IDXS["left_eye"]
        (rStart, rEnd) = face_utils.FACIAL_LANDMARKS_IDXS["right_eye"]
        (mStart, mEnd) = face_utils.FACIAL_LANDMARKS_IDXS["mouth"]

        while True:  # 不停地读取每帧图片
            # ret, frame = camera.read()#ret为布尔型，true或者false，表示是否读取到图片，frame表示截取到的一帧图片
            frame = imutils.resize(frame, width=640)  # 将这一帧图片的大小修改为640*640
            gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)  # 将这一帧图像转化为灰度图像
            prev_yawn_status = yawnStatus

            rects = detector(gray, 0)  # 在灰度图像中提取人脸的68个特征点
            # loop over the face detections
            for rect in rects:
                shape = predictor(gray, rect)
                shape = face_utils.shape_to_np(shape)

                leftEye = shape[lStart:lEnd]
                rightEye = shape[rStart:rEnd]
                mouth = shape[mStart:mEnd]
                leftEAR = self.eye_aspect_ratio(leftEye)
                rightEAR = self.eye_aspect_ratio(rightEye)
                mouEAR = self.mouth_aspect_ratio(mouth)

                # average the eye aspect ratio together for both eyes
                ear = (leftEAR + rightEAR) / 2.0  # 眼睛的宽高比

                leftEyeHull = cv2.convexHull(leftEye)
                rightEyeHull = cv2.convexHull(rightEye)
                mouthHull = cv2.convexHull(mouth)
                cv2.drawContours(frame, [leftEyeHull], -1, (0, 255, 255), 1)
                cv2.drawContours(frame, [rightEyeHull], -1, (0, 255, 255), 1)
                cv2.drawContours(frame, [mouthHull], -1, (0, 255, 0), 1)

                if ear < self.eye_ar_thresh:
                    counter += 1
                    cv2.putText(frame, "Eyes Closed ", (10, 30), cv2.FONT_HERSHEY_SIMPLEX, 0.7, (0, 0, 255), 2)

                    # if the eyes were closed for a sufficient number of
                    if counter >= self.eye_ar_consec_frames:  ##########################################这里判别为疲劳后要提醒客户端！！！！！！！！！！！！

                        cv2.putText(frame, "DROWSINESS ALERT!", (10, 50),
                                    cv2.FONT_HERSHEY_SIMPLEX, 0.7, (0, 0, 255), 2)
                        print("疲劳中")

                else:
                    counter = 0
                    cv2.putText(frame, "Eyes Open ", (10, 30), cv2.FONT_HERSHEY_SIMPLEX, 0.7, (0, 255, 0), 2)

                cv2.putText(frame, "EAR: {:.2f}".format(ear), (480, 30),
                            cv2.FONT_HERSHEY_SIMPLEX, 0.7, (0, 0, 255), 2)

            # 打哈欠检测
            if mouEAR > self.mou_ar_thresh:
                count += 1
                # cnt= "Count: " + COUNT
                # cv2.putText(frame, cnt , (10, 60),cv2.FONT_HERSHEY_SIMPLEX, 0.7, (0, 0, 255), 2)
                if count >= self.mou_ar_consec_frames:
                    YC += 1
                    yawnStatus = True
                    cv2.putText(frame, "Yawn detected ", (10, 60), cv2.FONT_HERSHEY_SIMPLEX, 0.7, (0, 0, 255), 2)
                    output_text = "Yawn Count: " + str(self.yawns + 1)
                    cv2.putText(frame, output_text, (10, 90), cv2.FONT_HERSHEY_SIMPLEX, 0.7, (255, 0, 0), 2)
                    cv2.putText(frame, "YC: {:.2f}".format(YC), (480, 50), cv2.FONT_HERSHEY_SIMPLEX, 0.7, (0, 0, 255),
                                2)

                    if self.yawns >= 3:
                        cv2.putText(frame, " DRIVER IS SLEEPY!", (10, 150),
                                    cv2.FONT_HERSHEY_SIMPLEX, 0.7, (0, 0, 255), 2)
                        print("疲劳中")

            else:
                count = 0
                yawnStatus = False
                YC = 0

            if prev_yawn_status == True and yawnStatus == False:
                self.yawns += 1

            cv2.putText(frame, "MAR: {:.2f}".format(mouEAR), (480, 60),
                        cv2.FONT_HERSHEY_SIMPLEX, 0.7, (0, 0, 255), 2)

            cv2.imshow("Frame", frame)

            key = cv2.waitKey(1) & 0xFF

            # if the `q` key was pressed, break from the loop

            if key == ord('q' or 'Q'):
                break
        cv2.destroyAllWindows()

    def eye_aspect_ratio(self, eye):
        # 计算垂直方向之间的欧几里得距离
        A = dist.euclidean(eye[1], eye[5])
        B = dist.euclidean(eye[2], eye[4])
        # 计算水平方向之间的欧几里得距离
        C = dist.euclidean(eye[0], eye[3])
        # 计算宽高比
        ear = (A + B) / (2.0 * C)
        return ear

    def mouth_aspect_ratio(self, mou):
        # 计算水平之间的欧几里得距离
        X = dist.euclidean(mou[0], mou[6])
        # 计算垂直方向之间的欧几里得距离
        Y1 = dist.euclidean(mou[2], mou[10])
        Y2 = dist.euclidean(mou[4], mou[8])
        # 取平均
        Y = (Y1 + Y2) / 2.0
        # 计算嘴巴长宽比
        mar = Y / X
        return mar
