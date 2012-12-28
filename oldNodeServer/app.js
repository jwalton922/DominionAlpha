
/**
 * Module dependencies.
 */
var amqp = require('amqp');

//var engine = require(__dirname+'/dominion/gameengine_server.js');
//var database = require(__dirname+'/dominion/database.js');
//var comm = require(__dirname+'/dominion/communication.js');
var express = require('express');
//var sqlite3 = require('sqlite3').verbose();
var clientSockets = []
var io = require('socket.io');
var dataRequestMap = {};

var app = module.exports = express.createServer()
, io = io.listen(app);
io.configure(function(){
  io.set('log level', 0);
});
//var db = new sqlite3.Database('/home/jwalton/workspace/Dominion_0.1/db/dominion.sqlite');
//var npcs = [];
//var buildings = [];
//var resources = [];
//var worldObjects = [];

// Configuration

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
  app.use(express.static(__dirname+'/dominion'))
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

// Routes
app.get('/jasmineTest', function(req, res){
  res.sendfile(__dirname + '/views/SpecRunner.html');

});
app.get('/', function(req, res){
  res.render('index.jade', {
    title: "Dominion Online"
  });
});

app.get('/game', function(req, res){
  console.log("rendering GameClient for user: "+req.session.username)
  res.render('GameClient.jade', {
    title: "Dominion Online",
    locals: {
      session: req.session
    }
  });
});

app.post('/users/login', function(req,res){
  console.log("username: "+req.body.user.username+" is logging in");
  req.session.username = req.body.user.username;
  req.session.token = Math.random();
  res.redirect('/game');
});

app.post('/users/signup', function(req,res){
  console.log("Signing up new user: "+req.body.user.username+" is logging in, password: "+req.body.user.password1);
  var username = req.body.user.username;
  var password = req.body.user.password1;
  var pcclass = req.body.user.pcclass;
  var stmtText = "INSERT INTO user (username,password,class,x,y) VALUES ('"+username+"','"+password+"','"+pcclass+"',10,10)";
  console.log("Insert sql: "+stmtText);
  var stmt = db.prepare(stmtText);
  //var stmt = db.prepare("INSERT INTO user (username,password,class,x,y) VALUES ('dubs','pword','Knight',10,10)");
  stmt.run();
  res.redirect('/game');
});
var exchange = null;
//io.sockets.on('connection', function(socket){comm.configureSocketMessages(socket,engine)});
io.sockets.on('connection', function(socket){
  console.log("socket.io connection");
  socket.on('action request', function(data){

    console.log("Action request: "+data.name+" action = "+data.action+" at "+data.x+","+data.y);
    
    exchange.publish("action-topic", data);
  //engine.processActionRequest(user,id, action, x, y);
  });

  socket.on('ClientPlayerDataRequest', function(data){
    console.log("Received ClientPlayerDataRequest for "+data.username);
    var time = Date.now();
    var uniqueKey = data.username+"_"+time;
    data.socketKey = uniqueKey;
    dataRequestMap[uniqueKey] = socket;
    exchange.publish("data.request.routing.key", data);
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
    });
    connection.queue('dominion-node-server-queue', function(queue){
      queue.bind('DOMINION_CLIENT_EXCHANGE', 'DOMINION_DATA_REQUEST_TOPIC');
      queue.subscribe(function (data_message, headers, deliveryInfo) {
          console.log("headers: %j", headers);
          console.log("deliveryInfo: %j", deliveryInfo);
        //console.log(data_message.data);
        var message = JSON.parse(data_message.data);
        console.log("Received data message: "+JSON.stringify(message));
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
    var queue = connection.queue('dominion-node-queue2',function(queue){
      console.log("Queue connection established: "+queue.name);
      queue.bind('DOMINION_CLIENT_EXCHANGE', 'DOMINION_GAME_STATE_TOPIC');
      var timeOfLastMessage = Date.now();
      queue.subscribe(function (state_message, headers, deliveryInfo) {
         // console.log("headers: %j", headers);
         // console.log("deliveryInfo: %j, deliveryInfo");
        var time = Date.now();
        var delta = time - timeOfLastMessage;
        timeOfLastMessage = time;
        //console.log(delta+' ms since last message. Got a message with routing key ' + deliveryInfo.routingKey+" info: "+deliveryInfo.toString());
        //      for(var key in state_message){
        //        console.log(key+': '+state_message[key]);
        //      }
        //      console.log("Message: "+state_message);
        var gameData = JSON.parse(state_message.data);
        //console.log("Player data: "+gameData.players);
        io.sockets.emit("server data", gameData);

      });
    });
  });
}
console.log("After listener added");
app.listen(3000);
console.log("Express server listening on port %d in %s mode", app.address().port, app.settings.env);

//setInterval(function(){console.log("Interval")}, 200);
//database.queryUsersWithCallbacks({}, engine);
//setInterval(function(){comm.pushToClient(engine, io.sockets);},50);
//var test = comm.pushToClients(database,io.sockets);
var number = 0;
//setInterval(function(){test(database,io.sockets)},200);
//initializeComputer();
//setInterval(updateClients2,80);
//setInterval(updateComputer, 150);
//loadWorldObjects();
//loadNPCsToMemory();
//database.setDatabase(db);
//database.loadWorldObjects();





// this was working with the RabbitTest java code
//connection.addListener('ready', function(){
//  var exchange = connection.exchange('some-exchange4', {
//    type : 'direct'
//  });
//  var queue = connection.queue('dominion-queue2',function(queue){
//    console.log("Queue connection established: "+queue.name);
//    queue.bind('some-exchange4', 'key.b.a');
//    queue.subscribe(function (message, headers, deliveryInfo) {
//
//      console.log('Got a message with routing key ' + deliveryInfo.routingKey+" info: "+deliveryInfo.toString());
//
//      console.log("Message: "+message);
//    });
//  });
  
//  queue.bind('some-exchange', 'key.b.a');
//  exchange.publish("key.a.b", {
//    message : 'this is my message'
//  });
  
//});