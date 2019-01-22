#%%
#import modules
import cv2
import numpy as np
import glob
from os import listdir
from os.path import isfile, join
#%%
#variable
width = 112
height = 112

#%%
#function
def resize(loadPath,savePath):
        images = listdir(loadPath)

        print(images)
        for i in range(len(images)):
                image = images[i]
                cnt = 0
                while image:
                    src = cv2.imread(image, cv2.IMREAD_COLOR)
                    height, width, channel = src.shape
                    rot = cv2.getRotationMatrix2D((width/2,height/2), 10,1)
                    dst = cv2.warpAffine(src,rot,(width,height))
                    cv2.imwrite(savePath%cnt,dst)
                    cnt = cnt + 1


#%%
#excute main
if __name__ == '__main__':
        load_path = 'C:/Users/zmfna/Documents/GitHub/SuicideSquad/data/raw/wait/'
        save_path = "C:/Users/zmfna/Documents/GitHub/SuicideSquad/data/raw/wait/%d.jpg"
        resize(load_path,save_path)