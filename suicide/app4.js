var express = require('express');
var app = express();

app.get('/', function(req,res){
    res.send('안녕하세요');
    
});

app.listen(3000,function(){
    console.log(' 서버실행됨');
});

