var amqp = require('amqp');

var connection = amqp.createConnection({
  host: 'localhost'
});

connection.addListener('ready', function(){
  exchange = connection.exchange('some-exchange4', {
     type : 'direct'
  });
  console.log("publishing message");
  exchange.publish("action-topic", "apptest message!");
  
});