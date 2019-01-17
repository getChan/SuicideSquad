import cv2
import numpy as np

width = 112
height = 112

def resize(imgPath,svPath):
    src = cv2.imread(imgPath, cv2.IMREAD_COLOR)
    dst = cv2.resize(src, dsize=(width, height), interpolation=cv2.INTER_LINEAR)
    cv2.imwrite(svPath, dst)

loadpath = 'C:\\Users\\zmfna\\Desktop\\hand.jpg'
savepath = 'C:\\Users\\zmfna\\Desktop\\hand1.jpg'
resize(path)