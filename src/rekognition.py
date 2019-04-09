import boto3
import urllib.request
import json
import re
import pymongo

class rekognition(object):
    def __init__(self):
        ## 사물인식 후보
        self.objects = []
        self.words = []
        self.results = []
        
    def getObjects(self, filename):
        ## 이미지 파일 load
        filename = filename
        ## api객체생성
        client = boto3.client('rekognition')

        with open(filename, 'rb') as photo:
            response = client.detect_labels(Image={'Bytes': photo.read()})
        
        ## 상위 후보부터 append
        for i in response['Labels']:
            self.objects.append(i['Name'])

    def translate(self):
        # 네이버 기계번역 API
        client_id = "NXQWw2ISBXysLVAJCAoA"
        client_secret = "rA04JXOQqO"
        for obj in self.objects:
            engword = urllib.parse.quote(obj)
            data = "source=en&target=ko&text=" + engword
            url = "https://openapi.naver.com/v1/language/translate"
            request = urllib.request.Request(url)
            request.add_header("X-Naver-Client-Id",client_id)
            request.add_header("X-Naver-Client-Secret",client_secret)
            response = urllib.request.urlopen(request, data=data.encode("utf-8"))
            rescode = response.getcode()
            if(rescode==200):
                response_body = response.read()
                translated = json.loads(response_body.decode('utf-8'))['message']['result']['translatedText']
                ## 특수문자 제거
                symbols = '[.]'
                translated = re.sub(symbols, '', translated)
                self.words.append(translated)
            else:
                print("Error Code:" + rescode)

    def dbQuery(self):
        ## MongoDB query
        conn = pymongo.MongoClient('127.0.0.1', 27017)
        db = conn.get_database('suicide')
        collection = db.get_collection('dictionary')
        for word in self.words:
            query = collection.find_one({'word':{word}})
            if query != None:
                self.results.append(query['id'])
                break

if __name__ == "__main__":
    test = rekognition()
    test.getObjects('../data/ex.jpg')
    test.translate()
    print(test.words)
    test.dbQuery()
    print(test.results)