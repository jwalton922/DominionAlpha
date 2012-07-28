var character;
var timer_is_on = 0;
var time = new Date();
var dominion;

function initCharacters() {
    pc = new PlayerCharacter();
    pc.setName('laidback');
    pausedAction = createActionSpriteList("images/knight/knight_pause/", "paused", "PNG", 96, 96, 5, 9, true);
    movingAction = createActionSpriteList("images/knight/knight_move/", "running", "PNG", 96, 96, 5, 9, true);
    attackingAction = createActionSpriteList("images/knight/knight_attack/", "attack", "PNG", 96, 96, 5, 9, true);
    dyingAction = createActionSpriteList("images/knight/knight_dying/", "tipping", "PNG", 96, 96, 5, 9, false);

    pc.addAction("pause", pausedAction);
    pc.addAction("move", movingAction);
    pc.addAction("attack", attackingAction);
    pc.addAction("dying", dyingAction);
    pc.addActionToQueue("pause");
    players.push(pc);

    pc2 = new PlayerCharacter();
    pc2.setName('dubs');
    pausedAction2 = createActionSpriteList("images/knight/knight_pause/", "paused", "PNG", 96, 96, 5, 9, true);
    movingAction2 = createActionSpriteList("images/knight/knight_move/", "running", "PNG", 96, 96, 5, 9, true);
    attackingAction2 = createActionSpriteList("images/knight/knight_attack/", "attack", "PNG", 96, 96, 5, 9, true);
    dyingAction2 = createActionSpriteList("images/knight/knight_dying/", "tipping", "PNG", 96, 96, 5, 9, false);

    pc2.addAction("pause", pausedAction2);
    pc2.addAction("move", movingAction2);
    pc2.addAction("attack", attackingAction2);
    pc2.addAction("dying", dyingAction2);
    pc2.addActionToQueue("pause");
    players.push(pc2);

    computer = new PlayerCharacter();
    computer.setName('computer');
    pausedAction3 = createActionSpriteList("images/knight/knight_pause/", "paused", "PNG", 96, 96, 5, 9, true);
    movingAction3 = createActionSpriteList("images/knight/knight_move/", "running", "PNG", 96, 96, 5, 9, true);
    attackingAction3 = createActionSpriteList("images/knight/knight_attack/", "attack", "PNG", 96, 96, 5, 9, true);
    dyingAction3 = createActionSpriteList("images/knight/knight_dying/", "tipping", "PNG", 96, 96, 5, 9, false);

    computer.addAction("pause", pausedAction3);
    computer.addAction("move", movingAction3);
    computer.addAction("attack", attackingAction3);
    computer.addAction("dying", dyingAction3);
    computer.addActionToQueue("pause");
    players.push(computer);

    getInitialPosition();
}

function imageTest(){
    image = new Image();
    image.src = "images/zxypng.PNG";
    character = new Sprite(image);
    character.createFrames(96, 96, 5, 13);
    console.log("Done running imageTest()");
}

function doTest() {
    image = new Image();
    image.src = "images/zxypng.PNG";
    character = new Sprite(image);
    character.createFrames(96, 96, 5, 13);
}

function load() {
    console.debug("Loading");
    initCanvas();
    thisClientPlayerName = $("#username").html().trim();
    //getInitialPosition();
   // initCharacters();
}

function start()
{
    //alert('Starting!');

    //doTest();
    //timedCount();
    
    methodEndTime = 0;
    return setInterval("timedCount()",80);
}

function timedCount() {

    //updateFrameIndices();
    time = new Date();
    var td = time.getTime()-methodEndTime;
    //console.log("Time since last call: "+td);
    //console.log("Start of timedCount: "+time.getTime());
    var stime= time.getTime();
    var methodStartTime = stime;
    //console.log("Started updating characters ");
    updateCharacters();
    updateNumbers();
    time = new Date();
    var etime = time.getTime();
    var diff = etime - stime;
    //console.log("Finished updating characters: "+diff+" ms");
    time = new Date();
    stime = time.getTime();
    //console.log("Started drawing");
    draw();
    time = new Date();
    etime = time.getTime();
    diff = etime - stime;
    //console.log("Finished drawing "+diff+" ms");
    
    time = new Date();
    methodEndTime = time.getTime();
    var methodTime = methodEndTime - methodStartTime;

    //console.log("Method time: "+methodTime);
}

function doTimer()
{
    if(!timer_is_on){
        timer_is_on = 1;
        timedCount();
    } else {
        timer_is_on = 0;
    }
}



function updateFrameIndices() {
    character.updateFrame();
}

function loadWorldObjectSprite(id, objectName,x,y, name, owner, resource_left, type){
    var worldObject;
    //alert("loadWorldObjectSprinte: "+id+" name = "+objectName);
    if(objectName == "tree_1"){
        var treeImage = new Image();
        treeImage.src = "images/world_images/trees/trees.png";
        treeImage.onload = preloadImage(treeImage);
        var treeSprite = new Sprite(treeImage);
        treeSprite.createFrames(128, 128, 4, 21);
        //worldObject = new WorldObject(id,treeSprite,0,x,y,128,128, name, owner, resource_left, type);
        worldObject = new DominionEntity();
        worldObject.initEntity(id, type, treeImage, 128, 128, x, y, name);
        //console.info("new world object: "+worldObject.id);
        return worldObject;
    } else if(objectName == "castle") {
        var castleImage = new Image();
        castleImage.src = "images/world_images/buildings/castle.png";
        castleImage.onload = preloadImage(castleImage);
        var castleSprite = new Sprite(castleImage);
        castleSprite.createFrames(512, 512, 1, 1);
//        worldObject = new WorldObject(id, castleSprite,0,x,y,512,512, name, owner, resource_left, type);
        worldObject = new DominionEntity();
        worldObject.initEntity(id, type, castleImage, 512, 512, x, y, name);
        //console.info("new world object: "+worldObject.id);
        return worldObject;
    } else if(objectName == "foresters_lodge") {
        var lodgeImage = new Image();
        lodgeImage.src = "images/world_images/buildings/foresters_lodge.png";
        lodgeImage.onload = preloadImage(lodgeImage);
        var lodgeSprite = new Sprite(lodgeImage);
        lodgeSprite.createFrames(256, 256, 1, 1);
        worldObject = new DominionEntity();
        worldObject.initEntity(id, type, lodgeImage, 128, 128, x, y, name);
//        worldObject = new WorldObject(id, lodgeSprite,0,x,y,256,256, name, owner, resource_left, type);
        //console.info("new world object: "+worldObject.id);
        return worldObject;
    } else {
        return null;
    }
}