
/**
 * Module dependencies.
 */
var amqp = require('amqp');
var express = require('express');
var io = require('socket.io');
var dataRequestMap = {};

var app = module.exports = express.createServer()
, io = io.listen(app);
io.configure(function(){
  io.set('log level', 0);
});

app.configure(function(){
  app.set('views', __dirname + '/views');
  app.set('view engine', 'jade');
  app.use(express.bodyParser());
  app.use(express.cookieParser());
  app.use(express.session({
    secret: 'test'
  }));
  app.use(express.methodOverride());
  app.use(app.router);
  app.use(express.static(__dirname + '/public'));
});
app.dynamicHelpers({
  session: function (req, res) {
    return req.session;
  }
});
app.configure('development', function(){
  app.use(express.errorHandler({
    dumpExceptions: true,
    showStack: true
  }));
});
app.configure('production', function(){
  app.use(express.errorHandler());
});

app.get('/', function(req, res){
  res.render('BrowserNodeJavaExample.jade', {
    title: "BrowserNodeJavaExample"
  });
});

var exchange = null;

io.sockets.on('connection', function(socket){
  console.log("socket.io connection");

  socket.on('ClientRequest', function(data){
    console.log("Received ClientRequest for "+data.clientId);
    var time = Date.now();
    var uniqueKey = data.clientId+"_"+time;
    data.socketKey = uniqueKey;
    dataRequestMap[uniqueKey] = socket;
    exchange.publish("data.topic", data);
  });
});

var connection = null;
try {
  connection = amqp.createConnection({
    host: 'localhost'
  });
} catch(e){
  console.log("Error creating amqp connection, probably need to start RabbitMQ");
}
if(connection != null){
  connection.addListener('ready', function(){
    exchange = connection.exchange('some-exchange4', {
      type : 'direct'
    });!
    connection.queue('DOMINION_CLIENT_EXCHANGE', function(queue){
      queue.bind('DOMINION_CLIENT_EXCHANGE', 'DOMINION_DATA_REQUEST_TOPIC');
      queue.subscribe(function (data_message, headers, deliveryInfo) {
        console.log(data_message.data);
        var message = JSON.parse(data_message.data);
        console.log("Received data message: "+message);
        console.log("Looking up socket for key: "+message.socketKey);

        var socket = dataRequestMap[message.socketKey];
        if(socket != null){
          socket.emit(message.type, message);
        } else {
          console.log("Could not find socket for key: "+data_message.socketKey);
        }

      });
    });
    console.log("adding socket.io emit function on rabbitmq message");
    var queue = connection.queue('DOMINION_CLIENT_EXCHANGE',function(queue){
      console.log("Queue connection established: "+queue.name);
      queue.bind('DOMINION_CLIENT_EXCHANGE', 'DOMINION_GAME_STATE_TOPIC');
      var timeOfLastMessage = Date.now();
      queue.subscribe(function (state_message, headers, deliveryInfo) {
        var time = Date.now();
        var delta = time - timeOfLastMessage;
        timeOfLastMessage = time;
        //console.log(delta+' ms since last message. Got a message with routing key ' + deliveryInfo.routingKey+" info: "+deliveryInfo.toString());
        //      for(var key in state_message){
        //        console.log(key+': '+state_message[key]);
        //      }
        //      console.log("Message: "+state_message);
        var gameData = JSON.parse(state_message.data);
        // console.log("Player data: "+gameData.players);
        io.sockets.emit("server data", gameData);

      });
    });
  });
}
console.log("After listener added");
app.listen(3000);
console.log("Express server listening on port %d in %s mode", app.address().port, app.settings.env);

app.listen(3000);

