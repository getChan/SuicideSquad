import cv2
import numpy as np
import matplotlib.pyplot as plt

image = cv2.imread("C:/Users/zmfna/Desktop/1212.jpg")
hsv = cv2.cvtColor(image, cv2.COLOR_BGR2YCrCb)
lower_skin = np.array([0, 133, 77])
upper_skin = np.array([255, 173, 127])
skinMask = cv2.inRange(hsv, lower_skin, upper_skin)       
kernel = cv2.getStructuringElement(cv2.MORPH_ELLIPSE, (11, 11))
skinMask = cv2.erode(skinMask, kernel, iterations = 2)
skinMask = cv2.dilate(skinMask, kernel, iterations = 2)
skinMask = cv2.GaussianBlur(skinMask, (3, 3), 0)
skin = cv2.bitwise_and(image, image, mask = skinMask)

width = 64
height = 36

# image = cv2.resize(skin, dsize=(width, height), interpolation=cv2.INTER_LINEAR)
print(skin)
cv2.imwrite("C:/Users/zmfna/Desktop/1213.jpg",skin)
