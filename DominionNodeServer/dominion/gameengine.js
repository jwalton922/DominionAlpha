

function updateNumbers(){
  var numsToKeep = new Array();
  for(i = 0; i < numbers.length; i++){
    var currY = numbers[i].getY();
    var change = numbers[i].getOrigY()-currY;
    // console.info("Change = "+change);
    if(change < 30){
      numbers[i].setY(currY-3);
      numsToKeep.push(numbers[i]);
    }
  }

  numbers = numsToKeep;
}

function updateCharacters(){
  for(var key in npcs){
    var npc = npcs[key];
    npc.updateState();
  }

  for(var key in players){
    var player = players[key];
    player.updateState();
  }
}



