import cv2
import numpy as np

def Frame(load_path):
    # 비디오 파일 열기
    vidcap = cv2.VideoCapture(load_path)
    # 비디오 열기 성공 확인
    if not vidcap.isOpened():
        return 1
    # 프레임률 얻기
    rate = vidcap.get(cv2.CAP_PROP_FPS)

    stop = False
    frame = np.ndarray([200,200]) # 현재 비디오 프레임
    cv2.namedWindow('Extracted Frame')

    # 각 프레임 간을 밀리초 단위로 지연(delay)
    # 비디오 프레임률에 해당
    delay = int(1000//rate)

    # 비디오의 모든 프레임에 대해
    while not stop:
        # 다음 프레임 있으면 읽기
        if not vidcap.read(frame):
            break
        cv2.imshow('Extracted Frame', frame)
        
        # 지연 도입
        # 혹은 중지하기 위해 키 입력
        
        if cv2.waitKey(delay) >= 0:
            stop = True
    
    #비디오 파일 닫기
    vidcap.release()
    pass


if __name__ == '__main__':
    load_path = '..\\data\\raw\\wait\\wait (6).mp4'
    Frame(load_path)
