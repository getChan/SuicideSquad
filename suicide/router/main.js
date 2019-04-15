module.exports = function(app, fs)
{
    
    app.get('/dictionary/:word', function(req,res){
        var word = req.query.word;
        console.log('사전에서 '+word+' 단어 검색');
         //데이터베이스 읽어서 word 없으면 콜백함수로 err 또는 null 있으면 data 전송
        
    });
    
/* 위 아래 라우터는 무관*/
     app.get('/',function(req,res){
         console.log("localhost:3000 !!")
         res.render('index', {
             title: "MY HOMEPAGE",
             length: 5
         })
     });



    app.get('/list', function (req, res) {
       fs.readFile( __dirname + "/../data/" + "user.json", 'utf8', function (err, data) {
           console.log( data );
           console.log(" localhost:3000/list!");
           res.end( data );
       });
    })
    
    app.get('/getUser/:username', function(req,res){
        fs.readFile(__dirname +"/../data/user.json", 'utf8',function(err,data){
            var username = req.params.username;
            console.log(username+"으로 접속");
            var users = JSON.parse(data);
            res.json(users[req.params.username]);
        });
    });
    
     app.post('/addUser/:username', function(req, res){

        var result = {  };
        var username = req.params.username;

        // CHECK REQ VALIDITY
        if(!req.body["password"] || !req.body["name"]){
            result["success"] = 0;
            result["error"] = "invalid request";
            res.json(result);
            return;
        }

        // LOAD DATA & CHECK DUPLICATION
        fs.readFile( __dirname + "/../data/user.json", 'utf8',  function(err, data){
            var users = JSON.parse(data);
            if(users[username]){
                // DUPLICATION FOUND
                result["success"] = 0;
                result["error"] = "duplicate";
                res.json(result);
                return;
            }

            // ADD TO DATA
            users[username] = req.body;

            // SAVE DATA
            fs.writeFile(__dirname + "/../data/user.json",
                         JSON.stringify(users, null, '\t'), "utf8", function(err, data){
                result = {"success": 1};
                res.json(result);
            })
        })
    });
    
    app.get('/login/:username/:password', function(req,res){
        var sess;
        sess = req.session; //세션 초기 설정 매우 간단
        
        fs.readFile(__dirname +"/../data/user.json", "utf8", function(err,data){
            var users= JSON.parse(data);
            var username = req.params.username;
            var password = req.params.password;
            var result = { };
            if(!users[username]){
                //없는 유저면
                result["success"] = 0;
                result["error"] = "not found";
                res.json(result);
                return;
            }
            
            if(users[username]["password"] == password){
                result["success"] = 1;
                sess.username =username;
                sess.name = users[username]["name"];
                res.json(result);
                
            }else{
                result["success"] = 0;
                result["error"] = "incorrect";
                res.json(result);
            }
        })
    });
    
    app.get('/logout', function(req,res){
        sess=req.session;
    })


}