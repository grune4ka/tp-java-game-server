// JavaScript Document
var boardPos = 0;
$(document).ready(function() {
		$('#game_space').css('background-position -30px -30px');
		setInterval(function() {
			$.get("/updateGameData", {'boardPos' : boardPos}, function(data) {
	      	var x = data.ballXPos;
	      	var y = data.ballYPos;
				$('#game_space').css('background-position', x + "px " + y + "px");
				$('#enemy').css('background-position', data.enemyPosition + "px " +  10 + "px");                        
	      }, 'json');    
   	}, 25);

		$("#game_space").mousemove(function(e){
			boardPos = e.pageX - $("#me").offset().left - 25;
			$("#me").css('background-position', boardPos + "px " + 0 + "px");
   	}); 
});
