// JavaScript 5cument
var boardPos = 0;
$(document).ready(function() {
		$('#game_space').css("background-position", "-30px -30px");
		setInterval(function() {
			$.get("/updateGameData", {'boardPos' : boardPos}, function(data) {
				if (data.results == "true") {
					window.location = "/results?me=" + data.yourPoints + "&enemy=" + data.enemyPoints;
					return;
				}
				var x = data.ballXPos;
	      	var y = data.ballYPos;	      	

				if ($.browser.webkit == true) {
					$('#game_space').animate({"background-position-x" : x, "background-position-y" : y}, 20, "linear");
					$('#enemy').css('background-position', data.enemyPosition + "px " +  20 + "px");             
				}
				else {
					$('#game_space').css('background-position', x + "px " + y + "px");
					$('#enemy').css('background-position', data.enemyPosition + "px " +  20 + "px");             
				}
				$('#enemyPoints').html(data.enemyPoints);
				$('#yourPoints').html(data.yourPoints);
	      }, 'json');    
   	}, 40);

		$(document).mousemove(function(e){
			boardPos = e.pageX - $("#me").offset().left - 25;
			if (boardPos < 100 ) {
				boradPos = 100;
				$("#me").css('background-position', boardPos + "px " + 5 + "px");
				return;
			}
			if (boardPos > 315) {
				boardPos = 315;
				$("#me").css('background-position', boardPos + "px " + 5 + "px");
				return;
			}
			$("#me").css('background-position', boardPos + "px " + 5 + "px");
   	}); 
});
