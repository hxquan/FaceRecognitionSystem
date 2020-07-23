import json


class TestSession:

    eye_ar_thresh = 0.25
    eye_ar_consec_frames = 48
    mou_ar_thresh = 0.70
    mou_ar_consec_frames = 20
    counter = 0
    yawnStatus = False
    yawns = 0
    count = 0
    YC = 0


    def get_info(self):
        info = dict().fromkeys(['counter', 'yawns', 'count', 'YC'], 0)

        print(str(info))
        return info
