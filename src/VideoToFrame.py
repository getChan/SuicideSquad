import cv2
vidcap = cv2.VideoCapture('../data/raw/thank.mp4')
frameCount = vidcap.get(cv2.CAP_PROP_FRAME_COUNT)
print('framecount', frameCount)
success,image = vidcap.read()
success = True
if not success :
    print('Video Read Error')
count = 0
while success:
  success,image = vidcap.read()
  cv2.imwrite("../data/frames/thank%d.jpg" % count, image)     # save frame as JPEG file
  if cv2.waitKey(10) == 27:                     # exit if Escape is hit
      break
  count += 1