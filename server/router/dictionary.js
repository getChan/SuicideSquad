
/*
 * 수호단어 조회를 담당하는 라우팅 함수 정의
 */

var findAllDocuments = function (db, collectionName,callback) {
    // Get the documents collection
    var collection = db.collection(collectionName);
    // Find some documents
    collection.find({}).toArray(function (err, docs) {
        //assert.equal(err, null);
        console.log("전체 레코드 출력");
        console.log(docs);
        callback(docs);
        return docs;
    });
}

var findDocuments = function (input, db, collectionName, callback) {
    var collection = db.collection(collectionName); //컬렉션 참조
    collection.find({"word": { "$regex":input}}).toArray(function (err, docs) {
        //assert.equal(err, null);
        console.log('단어 :' + input);
        console.log(docs);
        console.log("검색된 레코드 수:" + docs.length);
        callback(docs);
        return docs;
    });
}

var searchWord = function (word, database, collectionName, callback) {
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
module.exports.findAllDocuments = findAllDocuments;
module.exports.findDocuments = findDocuments;
module.exports.searchWord = searchWord;
