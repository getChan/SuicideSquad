import preprocessing as pre
from matplotlib import pyplot


if __name__ == '__main__':
    load_path = 'C:/Users/zmfna/Documents/GitHub/SuicideSquad/data/raw/cute/1.mp4'
    frame = pre.VidToFrame(load_path)
    print(frame)
    