/*
 * 설정
 */

module.exports = {
	server_port: 3000,
	db_url: 'mongodb://localhost:27017/data',
	db_schemas: [
        {file:'./word_schema', collection:'dictionary', schemaName:'WordSchema', modelName:'WordModel'}
	],
	route_info: [
        {file:'./dictionary', path:'/process/dictionary', method:'listword', type:'post'}
        ,{file:'./user', path:'/process/listuser', method:'listuser', type:'post'}
        ,{file:'./user', path:'/process/adduser', method:'adduser', type:'post'}
	    ,{file:'./device', path:'/process/adddevice', method:'adddevice', type:'post'}
	    ,{file:'./device', path:'/process/listdevice', method:'listdevice', type:'post'}
	    ,{file:'./device', path:'/process/register', method:'register', type:'post'}
	    ,{file:'./device', path:'/process/sendall', method:'sendall', type:'post'}
	]
}