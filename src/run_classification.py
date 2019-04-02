from model import Model
from preprocessing import VidToFrame

class run_classification(object):
    def __init__(self, file):
        self.file = file

    def FileToFrame(self):
        return VidToFrame(self.file)


if __name__ == "__main__":
    ## 파일 불러와야함
    input_data = run(path)
    ## 레이블 불러와야댐 좆밥드랑
    label = ㅇㅋ?
    c3dnet = Model()
    print(c3dnet.run(np.expand_dims(input_data, axis=0), np.expand_dims(label, axis=0)))