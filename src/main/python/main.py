import base64
import os
import numpy as np
import imutils
import cv2


import face_recognition

from flask import Flask, jsonify, request



app = Flask(__name__)


def load_image(image_base64):
    img_data = base64.b64decode(image_base64)
    file_stream = '1.jpg'
    file = open(file_stream, 'wb')
    file.write(img_data)
    file.close()
    img1 = face_recognition.load_image_file(file_stream)
    #img = imutils.rotate_bound(img, 90)
    trans_img = cv2.transpose(img1)
        #逆时针90度
    # new_img = cv2.flip(trans_img, 0)
        #顺时针90度
    img = cv2.flip(trans_img, 1)
    os.remove(file_stream)
    return img


@app.route('/face_encodings', methods=['POST'])
def face_encodings():
    # img= face_recognition.load_image_file(src)
    # face_locations = face_recognition.face_locations(img)
    # face_encodings = face_recognition.face_encodings(img, face_locations)
    image_base64 = request.form['image_base64']
    img = load_image(image_base64)
    # img64 = load_image(image_base64)
    # img = np.rot90(img64, 1)
    # img = imutils.rotate_bound(img, 90)
    face_locations = face_recognition.face_locations(img)
    if len(face_locations)==0:
        result = {
            "success": False,
            "message": "未检测",
            "data": ""
        }
    else:
        encodings = face_recognition.face_encodings(img,face_locations)
        # if len(encodings) == 0:
        #     result = {
        #         "success": False,
        #         "message": "image is error",
        #         "data": ""
        #     }
        # else:
        result = {
            "success": True,
            "message": "",
            "data": encodings[0].tolist()
        }
    return jsonify(result)


@app.route('/face_locations', methods=['GET', 'POST'])
def face_locations():
    file_stream = request.files['file']
    img = face_recognition.load_image_file(file_stream)
    locations = face_recognition.face_locations(img)
    result = {
        "success": True,
        "message": locations
    }
    return jsonify(result)


@app.route('/face_landmarks', methods=['GET', 'POST'])
def face_landmarks():
    file_stream = request.files['file']
    img = face_recognition.load_image_file(file_stream)
    # Find all facial features in all the faces in the image
    landmarks_list = face_recognition.face_landmarks(img)
    result = {
        "success": True,
        "message": landmarks_list
    }
    return jsonify(result)








if __name__ == "__main__":
    app.run(host='0.0.0.0', port=5000, debug=True)


