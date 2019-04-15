var express= require('express');
var http = require('http');
var static = require('serve-static');
var path = require('path');

var bodyParser = require('body-parser');
var cookieParser = require('cookie-parser');

var expressSession = require('express-session');
var fs=require("fs");
var db = require('./database/database');

var app =express();
var server = app.listen(3000,function(){
    console.log("3000번 포트로 익스프레스 실행");
})
app.use(express.static('public'));

app.use(bodyParser.json());
app.use(bodyParser.urlencoded());
app.use(session({
 secret: '@#@$MYSIGN#@$#$',
 resave: false,
 saveUninitialized: true
}));

// 라우터모듈은 main.js를 불러와 app에 전달
var router = require('./router/main') (app,fs);