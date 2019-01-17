import cv2
import numpy as np

def rotate(imgPath):
    src = cv2.imread(imgPath, cv2.IMREAD_COLOR)
    height, width, channel = src.shape
    rot = cv2.getRotationMatrix2D((width/2,height/2), 10,1)
    dst = cv2.warpAffine(src,rot,(width,height))
    cv2.imwrite(imgPath,dst)

path = 'C:\\Users\\zmfna\\Desktop\\hand.jpg'
rotate(path)