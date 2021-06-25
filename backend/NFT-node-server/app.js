var express = require('express')
  , http = require('http')
  , fs = require('fs')
  , app = express();

var router = require('./router/main')(app,fs);
const Eureka = require('eureka-js-client').Eureka;


app.set('views', __dirname + '/views');
app.set('view engine', 'ejs');
app.engine('html', require('ejs').renderFile);
app.use(express.static('views'));                                                                   

// APIGateway 속성
const client = new Eureka({
  // application instance information
  instance: {
    app: 'NFT-node-service',
    hostName: 'localhost',
    ipAddr: '127.0.0.1',
    port: {
      '$': 9000,
      '@enabled': true
    },
    vipAddress: 'NFT-node-service',
    dataCenterInfo: {
      '@class': 'com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo',
      name: 'MyOwn', // 'Netflix' | 'Amazon' | 'MyOwn'
    },
    registerWithEureka: true,
    fetchRegistry: true
  },
  eureka: {
    // eureka server host / port
    host: 'localhost',
    port: 8761,
    servicePath:'/eureka/apps/' 
  },
});


var server = http.createServer(app);
server.listen(9000, function() {
  console.log('Express server listening on port ' + server.address().port);
});


client.start(error => {
  console.log(error);
  app.get('/', (req, res) => {
    res.send('client');
    res.end();
  })
});


client.on('deregistered', () => {
  process.exit();
  console.log('after deregistered');
})

client.on('started', () => {
console.log("eureka host  " + 8761);
})



