const http = require('http');
 
const hostname = '117.16.244.58';
const port =3000;
 				
http.createServer((req, res) => {		
  res.writeHead(200, { 'Content-Type': 'text/plain' });
  res.end('Hello World\n');
}).listen(port, hostname, () => {		
  console.log(`Server running at https://${hostname}:${port}/`);
});
