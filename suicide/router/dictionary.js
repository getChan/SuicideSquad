
/*
 * 수호단어 조회를 담당하는 라우팅 함수 정의
 */

var listword = function(req, res) {
	console.log('dictionary 모듈 안에 있는 listword 호출됨.');

	// 데이터베이스 객체 참조
	var database = req.app.get('database');
    
    // 데이터베이스 객체가 초기화된 경우, 모델 객체의 findAll 메소드 호출
	if (database.db) {
		// 1. 모든 사용자 검색
		database.WordModel.findAll(function(err, results) {
			// 에러 발생 시, 클라이언트로 에러 전송
			if (err) {
                console.error('단어 리스트 조회 중 에러 발생 : ' + err.stack);
                
                res.writeHead('200', {'Content-Type':'text/html;charset=utf8'});
				res.write('<h2>사용자 리스트 조회 중 에러 발생</h2>');
                res.write('<p>' + err.stack + '</p>');
				res.end();
                
                return;
            }

			if (results) {
				console.dir(results);

				res.writeHead('200', {'Content-Type':'application/json;charset=utf8'});
				res.write(JSON.stringify(results));
				res.end();
				
			} else {
				res.writeHead('200', {'Content-Type':'text/html;charset=utf8'});
				res.write('<h2>단어 리스트 조회  실패</h2>');
				res.end();
			}
		});
	} else {
		res.writeHead('200', {'Content-Type':'text/html;charset=utf8'});
		res.write('<h2>데이터베이스 연결 실패</h2>');
		res.end();
	}
	
};

module.exports.listword = listword;

