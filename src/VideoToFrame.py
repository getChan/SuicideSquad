import cv2
import numpy as np

def VidToFrame(load_path, save_path):
    vidcap = cv2.VideoCapture(load_path)
    frameCount = vidcap.get(cv2.CAP_PROP_FRAME_COUNT)
    print('framecount', frameCount)

    success, image = vidcap.read()

    if not success :
        print('Video Read Error')
    cnt = 0
    while success:
        success, image = vidcap.read()
        
        hsv = cv2.cvtColor(image, cv2.COLOR_BGR2YCrCb)
        lower_skin = np.array([0, 133, 77])
        upper_skin = np.array([255, 173, 127])

        mask = cv2.inRange(hsv, lower_skin, upper_skin)

        cv2.imwrite(save_path%cnt, mask)     # save frame as JPEG file
        if cv2.waitKey(10) == 27:                     # exit if Escape is hit
            break
        cnt += 1

    vidcap.release()
    pass


if __name__ == '__main__':
    load_path = '..\\data\\raw\\cute\\cute (1).mp4'
    save_path = "..\\data\\frames\\cute\\cute (1)%d.jpg"
    VidToFrame(load_path, save_path)