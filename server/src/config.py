from model import Model
from preprocessing import VidToFrame
from preprocessing import FrameCount
import numpy as np
import sys
import os

class run_classification(object):
    def __init__(self, file):
        self.file = file

    def FileToFrame(self):
        return VidToFrame(self.file)

if __name__ == "__main__":
    ## 파일 불러와야함
    path = '../uploads'
    label = os.listdir(path)
    video = path + '/'+ label[0]
    input_data = run_classification(video)

    ## 레이블 불러와야댐 좆밥드랑
    if label[0] == '0':
        label = [1,0,0,0,0]
    elif label[0] == '1':
        label = [0,1,0,0,0]
    elif label[0] == '2':
        label = [0,0,1,0,0]
    elif label[0] == '3':
        label = [0,0,0,1,0]
    elif label[0] == '4':
        label = [0,0,0,0,1]
    c3dnet = Model()
    ans = c3dnet.run(np.expand_dims(input_data.FileToFrame(), axis=0), np.expand_dims(label, axis=0))
    os.remove(video)
    ans = str(ans[0])
    print(ans)
