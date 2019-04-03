import cv2
import numpy as np
import os
import random

def VidToFrame(load_path):
    frames = list()
    total_frames = FrameCount(load_path)
    # 사용할 프레임 수
    FRAME_NUM = 30
    
    #프레임 리스트 생성
    #start_frame = np.random.randint(0, total_frames-FRAME_NUM)
    frame_list = []
    ran_num = random.randrange(0,total_frames+1)

    for i in range(FRAME_NUM):
        while ran_num in frame_list:
            ran_num = random.randrange(0,total_frames+1)
        frame_list.append(ran_num)
    
    frame_list.sort()

    try:
        vidcap = cv2.VideoCapture(load_path)
        success, image = vidcap.read()

        if not success :
            print('Video Read Error')
        cnt = 0 
        frame_cnt = 0
        while success:
            
            success, image = vidcap.read()

            hsv = cv2.cvtColor(image, cv2.COLOR_BGR2YCrCb)
            lower_skin = np.array([0, 133, 77])
            upper_skin = np.array([255, 173, 127])

            skinMask = cv2.inRange(hsv, lower_skin, upper_skin)
            kernel = cv2.getStructuringElement(cv2.MORPH_ELLIPSE, (11, 11))
            skinMask = cv2.erode(skinMask, kernel, iterations = 2)
            skinMask = cv2.dilate(skinMask, kernel, iterations = 2)
            skinMask = cv2.GaussianBlur(skinMask, (3, 3), 0)
            skin = cv2.bitwise_and(image, image, mask = skinMask)

            width = 112
            height = 112
            
            mask = cv2.resize(skin, dsize=(width, height), interpolation=cv2.INTER_LINEAR)
            # mask = cv2.resize(image, dsize=(width, height), interpolation=cv2.INTER_LINEAR)

            if cv2.waitKey(10) == 27:                     # exit if Escape is hit
                break
            if frame_cnt > FRAME_NUM:
                break
            if frame_list[frame_cnt] == cnt:
                frames.append(mask)
                frame_cnt += 1
            cnt += 1

        vidcap.release()
    except Exception:
        pass
    frames = np.asarray(frames)

    return frames

def FrameCount(load_path):
    vidcap = cv2.VideoCapture(load_path)
    frameCount = vidcap.get(cv2.CAP_PROP_FRAME_COUNT)
    return int(frameCount)

def getDataFrame(load_path):
    ### 레이블 전처리
    labels=[]
    label_num = 0
    label_dict={'cute':0, 'hi':1, 'love':2, 'thank':3, 'wait':4}

    data = []
    for parent, dirname, filename in os.walk(load_path):
        if dirname != []: #디렉토리이름
            continue
        else:
            # one-hot encoding
            one_hot = [0]*len(label_dict)
            one_hot[label_num] = 1
            for _ in range(len(filename)):
                labels.append(one_hot)
            label_num += 1
            # load_data
            for f in filename:
                data.append(VidToFrame(parent+'\\'+f))
                
    data = np.asarray(data)
    labels = np.asarray(labels,dtype=np.int)

    return data,labels