import base64
import os

import face_recognition
from flask import Flask, jsonify, request


app = Flask(__name__)


def load_image(image_base64):
    img_data = base64.b64decode(image_base64)
    file_stream = '1.jpg'
    file = open(file_stream, 'wb')
    file.write(img_data)
    file.close()
    img = face_recognition.load_image_file(file_stream)
    os.remove(file_stream)
    return img


@app.route('/face_encodings', methods=['POST'])
def face_encodings():
    image_base64 = request.form['image_base64']
    img = load_image(image_base64)
    encodings = face_recognition.face_encodings(img)
    if len(encodings) == 0:
        result = {
            "success": False,
            "message": "image is error",
            "data": ""
        }
    else:
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
