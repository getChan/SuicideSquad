import os
from six.moves import xrange
import tensorflow as tf
import PIL.Image as Image
import random
import numpy as np
import cv2
import time
# 프레임 입력받아 이미지 배열 리턴
def get_frames_data(filename, num_frames_per_clip=60):
    ret_arr = []
    s_index = 0
    # filenames <- 파일이름들 배열
    for parent, dirnames, filenames in os.walk(filename):
        # 지정한 프레임수보다 적다면 리턴
        if(len(filenames)<num_frames_per_clip):
            return [], s_index
        filenames = sorted(filenames)
        # 0~(총 프레임수-지정프레임수) 중 랜덤정수 추출
        s_index = random.randint(0, len(filenames)-num_frames_per_clip)
        # s_index 부터 60개 프레임 선택해서 read후 리턴
        for i in range(s_index, s_index+num_frames_per_clip):
            image_name = str(filename)+'/'+str(filenames[i]) # 
            img = Image.open(image_name)
            img_data = np.array(img)
            ret_arr.append(img_data)
    return ret_arr, s_index

def read_clip_and_label(filename, batch_size, start_pos=-1, num_frames_per_clip=60, crop_size=112, shuffle=False):
    lines = open(filename, 'r')
    read_dirnames=[]
    data = []
    label = []
    batch_index = 0
    next_batch_start = -1
    lines =list(lines)
    

if __name__ == "__main__":
    a,b= get_frames_data('../data/frames/cute/')
    print(b)
