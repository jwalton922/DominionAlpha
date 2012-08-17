BrowserNodeJavaExample = {};
BrowserNodeJavaExample.socket = io.connect('localhost');
BrowserNodeJavaExample.load = function(){
  this.socket.on('ClientDataResponse', BrowserNodeJavaExample.handleResponseData);
}

BrowserNodeJavaExample.requestData = function(){
  var time = Date.now();
  var requestData = {};
  requestData.clientId = time;
  this.socket.emit('ClientRequest', requestData);
}

BrowserNodeJavaExample.handleResponseData = function(data){
  console.info("Received data from server: "+JSON.stringify(data));
  var currentData = $("#returnData").text();
  currentData+="\n"+data.message;
  $("#returnData").text(currentData);
}