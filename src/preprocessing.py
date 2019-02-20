#%%
import cv2
import numpy as np
import os
#%%
def VidToFrame(load_path):
    frames = list()
    try:
        vidcap = cv2.VideoCapture(load_path)
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
            mask = cv2.threshold(mask, cv2.THRESH_BINARY)
            image = cv2.cvtColor(image,cv2.COLOR_BGR2GRAY)

            width = 720
            height = 480
            mask = cv2.resize(mask, dsize=(width, height), interpolation=cv2.INTER_AREA)
            img = cv2.resize(image, dsize=(width, height), interpolation=cv2.INTER_AREA)

            mask = mask - img
            
            if cv2.waitKey(10) == 27:                     # exit if Escape is hit
                break
            frames.append(mask)
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