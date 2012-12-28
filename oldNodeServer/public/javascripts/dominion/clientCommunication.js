var Communication = {};
Communication.socket = null;
Communication.init = function() {
    console.info("Opening socket to server: " + DominionConfig.serverAddress);
    this.socket = io.connect(DominionConfig.serverAddress);
    this.initSocket();

}
Communication.serverDataArray = [];

Communication.initSocket = function() {
    this.socket.on('ClientPlayerDataResponse', Communication.handleClientPlayerData);

    this.socket.on('server data', function(data) {
        this.serverDataArray.push(data);
    });
}

Communication.retrieveServerData = function(){
    //clone server data since we will be clearing it out
    var clonedData = $.extend(true, {}, this.serverDataArray);
    //clear server data
    this.serverDataArray = [];
    
    return clonedData;
}

Communication.handleClientPlayerData = function(data) {
    console.debug("Received client player data");
    var newPlayer = GameEngine.initPlayer(data);
    console.debug("Created client player object: " + newPlayer.toString());
    GameEngine.currentPlayer = newPlayer;
    RenderEngine.initCanvas(newPlayer.getX(), newPlayer.getY());
    GameEngine.start();
}

Communication.requestClientData = function(clientUsername, token) {
    var requestData = {};
    requestData.username = clientUsername;
    requestData.authToken = token;
    requestData.type = "ClientPlayerDataRequest";
    this.socket.emit('ClientPlayerDataRequest', requestData);
}



function sendActionRequest(action, x, y) {
    console.log("requesting action " + action + " x=" + x + " y=" + y);
    var data = {};
    data.action = action;
    data.entityType = "player";
    data.x = x;
    data.y = y;
    data.name = thisClientPlayerName;
    data.entityId = clientId;
    //console.info("Sending action request for "+data.username+"user id = "+data.userid+" action ="+data.action+" at "+x+","+y);
    socket.emit('action request', data);
}
