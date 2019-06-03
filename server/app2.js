/*(function() {
    var childProcess = require("child_process");
    var oldSpawn = childProcess.spawn;
    function mySpawn() {
        console.log('spawn called');
        console.log(arguments);
        var result = oldSpawn.apply(this, arguments);
        return result;
    }
    childProcess.spawn = mySpawn;
})();*/


var express = require('express'); 
var app = express();
var fs = require('fs');
var exec = require("child_process").exec;
  
app.listen(3000, function() { 
    console.log('server running on port 3000'); 
} ) 
  
// Function callName() is executed whenever  
// url is of the form localhost:3000/name 
app.get('/model', function(req,res){
	console.log('/model 호출됨');

	var yourscript = exec('sh ./config.sh',(error, stdout, stderr) => {	
		   console.log(stdout);
		   console.log(stderr);
		   if  (error !== null) {
			console.log('exec error: ${error}');
		   }
		   var data=fs.readFileSync('config.txt', 'utf8');
		   console.log(data);
		   res.writeHead('200', { 'Content-Type': 'text/html;charset=utf8'});
		   res.write('<p>'+data+'</p>');
		   res.end();
	});
	
}); 

app.get('/',function(req,res){
    console.log('/ hello');
    res.send('hello');
})
  


