var express = require('express');
var io = require('socket.io');

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
  res.render('mmoMovementLogin.jade', {
    title: "Dominion Online"
  });
});

app.get('/game', function(req, res){
  console.log("rendering GameClient for user: "+req.session.username)
  res.render('mmoMovementClient.jade', {
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

//io.sockets.on('connection', function(socket){comm.configureSocketMessages(socket,engine)});
io.sockets.on('connection', function(socket){
  console.log("socket.io connection");
  socket.on('action request', function(data){

    console.log("Action request: "+data.name+" action = "+data.action+" at "+data.x+","+data.y);

  });

});

console.log("After listener added");
app.listen(3000);
console.log("Express server listening on port %d in %s mode", app.address().port, app.settings.env);

var number = 0;






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