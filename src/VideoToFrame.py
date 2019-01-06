import cv2

vidcap = cv2.VideoCapture('../data/raw/감사(1)_Trim.mp4')
frameCount = vidcap.get(cv2.CAP_PROP_FRAME_COUNT)
print('framecount', frameCount)
framePer30 = frameCount // 30
success, image = vidcap.read()

if not success :
    print('Video Read Error')
count = 0
while success:
  success, image = vidcap.read()
  if count % framePer30 == 0:
      grayscale = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)
      _, thresh = cv2.threshold(grayscale, 128, 255, cv2.THRESH_BINARY)
    #   thresh = cv2.adaptiveThreshold(grayscale,255,cv2.ADAPTIVE_THRESH_MEAN_C,\
    #   cv2.THRESH_BINARY,15,-2)
    #   thresh = cv2.adaptiveThreshold(grayscale,255,cv2.ADAPTIVE_THRESH_GAUSSIAN_C,\
    #   cv2.THRESH_BINARY,15,-2)
      cv2.imwrite("../data/frames/thank%d.jpg" % count, thresh)     # save frame as JPEG file
  if cv2.waitKey(10) == 27:                     # exit if Escape is hit
      break
  count += 1