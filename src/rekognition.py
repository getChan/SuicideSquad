import boto3

class rekognition(object):
    # def __init__(self):
        
    def getObjects(self):
        ## 이미지 파일 load
        file = '../data/photo.jpg'
        ## api객체생성
        client = boto3.client('rekognition')

        with open(file, 'rb') as photo:
            response = client.detect_labels(Image={'Bytes': photo.read()})
        ## 사물인식 후보
        
        objects = []
        ## 상위 후보부터 append
        for i in response['Labels']:
            objects.append(i['Name'])
        ### 상위 범주
            # for x in response['Labels'][0]['Parents']:
            #     objects.append(x['Name']) 

        print(objects)

    # def get(self, parameter_list):
    #     pass

if __name__ == "__main__":
    rekognition().getObjects()