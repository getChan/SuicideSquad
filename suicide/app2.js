var express = require('express');
var http = require('http');
var path = require('path');
var assert = require('assert');
var fs = require('fs');
//express의 미들웨어 불러오기
var bodyParser = require('body-parser');
var cookieParser = require('cookie-parser');
var static = require('serve-static');
var errorHandler = require('errorhandler');
// 에러 핸들러 모듈 사용
var expressErrorHandler = require('express-error-handler');
var expressSession = require('express-session');
var app = express();
//기본속성 설정
app.set('port', process.env.Port || 3000);
//body-parse를 사용해  application/x-www.form-urlencoded 파싱
app.use(bodyParser.urlencoded({
    extended: false
}));
//body-parser를 사용ㅎ application/json 파싱
app.use(bodyParser.json());
//public 폴더를 static으로 오픈 ->public 폴더를 특정 패스로 접근할 수 있도록 static 미들웨어를 사용한것임
app.use('/public', static(path.join(__dirname, 'public')));
 

//cookie-parse 설정
app.use(cookieParser());
//세션 설정
app.use(expressSession({
    secret: 'my key'
    , resave: true
    , saveUninitialized: true
}));
//데이터베이스 설정
var MongoClient = require('mongodb').MongoClient;
//데이터베이스 변수
var dbName = 'suicide';
var collectionName = 'dictionary';
var database;
function connectDB() {
    //var inputWord = "가까이";
    var databaseUrl = 'mongodb://localhost:27017';
    MongoClient.connect(databaseUrl, { useNewUrlParser: true}, function (err, client) {
        var inputWord = "사랑";
        assert.equal(null, err);
        console.log('database 연결 되었음 ' + databaseUrl);
        database = client.db(dbName);//데이터베이스 변수에 할당
    });
}
var findAllDocuments = function (db, callback) {
    // Get the documents collection
    var collection = db.collection(collectionName);
    // Find some documents
    collection.find({}).toArray(function (err, docs) {
        assert.equal(err, null);
        console.log("전체 레코드 출력");
        console.log(docs);
        callback(docs);
        return docs;
    });
}
var findDocuments = function (input, db, callback) {
    var collection = db.collection(collectionName); //컬렉션 참조
    collection.find({"word": { "$regex":input}}).toArray(function (err, docs) {
        assert.equal(err, null);
        console.log('단어 :' + input);
        console.log(docs);
        console.log("검색된 레코드 수:" + docs.length);
        callback(docs);
        return docs;
    });
}
var searchWord = function (word, database, callback) {
        console.log('searchWord 호출됨.');
        //dictionary 컬렉션 참조
        var collection = database.collection(collectionName); //컬렉션 참조
        console.log('dictionary 컬렉션 참조.');
        console.log(word);
        //단어를 검색
        collection.find({"word":{"$regex":word}}).toArray(function (err, docs) {
            if (err) {
                callback(err, null);
                return;
            }
            if (docs.length > 0) {
                console.log('단어 %s 를 찾음!', word);
                callback(null, docs);
            }
            else {
                console.log("일치하는 단어를 찾지 못함. ");
                callback(null, null);
            }
        });
    }
    //라우터 객체 참조
var router = express.Router();
router.route('/process/login').post(function (req, res) {
    console.log('/process/login 호출됨.');
    var paramId = req.body.id || req.query.id;
    var paramPassword = req.body.password || req.query.password;
    res.writeHead('200', {
        'Content-Type': 'text/html;charset=utf8'
    });
    res.write('<div><p>Param id : ' + paramId + '</p></div>');
    res.write('<div><p>Param password : ' + paramPassword + '</p></div>');
    res.write("<br><br><a href='/public/login2.html'>로그인 페이지로 돌아가기</a>");
    res.end();
});
app.get('/',function(req,res){
    res.send('hello world');
});

app.get('/hello',function(req,res){
    res.send('hello hello hello');
});


app.post('/process/word', function (req, res) {
    console.log('/process/word 호출됨.');
    var paramWord = req.body.wordName || req.query.wordName;
    console.log(paramWord + ' 검색');
    if (database) {
        searchWord(paramWord, database, function (err, docs) {
            if (err) {
                throw err;
            }
            if (docs) {
                
                console.dir(docs);
                res.writeHead('200', {
                    'Content-Type': 'text/html;charset=utf8'
                });
                res.write('<div><p>요청단어: ' + paramWord + '</p></div>');
                res.write('<p>' + JSON.stringify(docs) + '</p>');
                res.write('<p> 레코드 수:' + docs.length + '</p>');
                for(var i = 0; i< docs.length; i++){
                    res.write('<p>'+i+'번째 단어</p>');
                    res.write('<p> 단어:'+docs[i].word+'</p>');
                    res.write('<p> 수형 설명:'+docs[i].handExplain+'</p>');
                    res.write('<p> 단어 정보:'+docs[i].wordInformation+'</p>');
                    res.write('<p> 단어 예시:'+docs[i].wordExample+'</p>');
                    res.write('<p> 단어 설명:'+docs[i].wordExplain+'</p>');
                    res.write('<br><a href=http://sldict.korean.go.kr/front/sign/include/controlVideoSpeed.do?origin_no='+docs[i].id+'> 동영상 보러가기.</a>');
                    res.write('<br><img src="/public/img/'+docs[i].id+'(1).png>"');
                }
                res.write("<br><br><a href='/public/word.html'>단어검색 사이트로 돌아가기.</a>");
                res.end();
                console.log(docs[0].wordInformation);
            }
            else {
                res.writeHead('200', {
                    'Content-Type': 'text/html;charset=utf8'
                });
                res.write('<div><p>단어검색실패</p></div>');
                res.write("<br><br><a href='/public/word.html'>단어 검색 페이지로 돌아가기</a>");
                res.end();
            }
        });
    }
    else {
        res.writeHead('200', {
            'Content-Type': 'text/html;charset=utf8'
        });
        res.write('<div><p>데이터베이스 연결 실패</p></div>');
        res.end();
    }
});
// 404 에러 페이지 처리
var errorHandler = expressErrorHandler({
    static: {
        '404': './public/404.html'
    }
});
app.use(expressErrorHandler.httpError(404));
app.use(errorHandler);
//라우터 객체 등록
app.use('/', router);
http.createServer(app).listen(app.get('port'), function () {
    console.log('서버가 시작되었습니다. 포트번호:' + app.get('port'));
    connectDB();
});