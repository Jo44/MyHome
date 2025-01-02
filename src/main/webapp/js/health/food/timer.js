/////////////////////////////
/////     Food Timer    /////
/////////////////////////////
//   @author Jonathan      //
//   @version 1.1          //
//   @since 28/12/2024     //
/////////////////////////////

// Variables
const colorOff = '#585858';
const colorRunning = '#449d44';
const chronoBckgnd = document.getElementById("chronoBckgnd");
const fiveMinutesButton = document.getElementById("btn5Min");
const threeMinutesButton = document.getElementById("btn3Min");
const twoMinutesButton = document.getElementById("btn2Min");
const oneMinuteThirtyButton = document.getElementById("btn1Min30");
const oneMinuteButton = document.getElementById("btn1Min");
const customTime = document.getElementById("customTime");
const goButton = document.getElementById("btnGo");
const timerElement = document.getElementById("periodLeft");
const stateElement = document.getElementById("state");
// Audio - [Eclipse] localhost:8080/MyHome/audio/track.mp3 <-> ../../audio/track.mp3 [Raspberry]
var audioBellSingle = '../../audio/chrono/bell_single.mp3';
var volume = document.getElementById("volume");
let interval;

// Start Timer
function startTimer(duration) {
	let timer = duration, minutes, seconds;
	interval = setInterval(function() {
		minutes = parseInt(timer / 60, 10);
		seconds = parseInt(timer % 60, 10);

		minutes = minutes < 10 ? "0" + minutes : minutes;
		seconds = seconds < 10 ? "0" + seconds : seconds;

		timerElement.textContent = minutes + ":" + seconds;

		if (--timer < 0) {
			clearInterval(interval);
			chronoBckgnd.style.backgroundColor = colorOff;
			timerElement.textContent = "00:00";
			stateElement.textContent = stateStop;
			playSound();
		}
	}, 1000);
}

// Listeners
fiveMinutesButton.addEventListener("click", function() {
	clearInterval(interval);
	chronoBckgnd.style.backgroundColor = colorRunning;
	stateElement.textContent = stateRunning;
	startTimer(300);
});

threeMinutesButton.addEventListener("click", function() {
	clearInterval(interval);
	chronoBckgnd.style.backgroundColor = colorRunning;
	stateElement.textContent = stateRunning;
	startTimer(180);
});

twoMinutesButton.addEventListener("click", function() {
	clearInterval(interval);
	chronoBckgnd.style.backgroundColor = colorRunning;
	stateElement.textContent = stateRunning;
	startTimer(120);
});

oneMinuteThirtyButton.addEventListener("click", function() {
	clearInterval(interval);
	chronoBckgnd.style.backgroundColor = colorRunning;
	stateElement.textContent = stateRunning;
	startTimer(90);
});

oneMinuteButton.addEventListener("click", function() {
	clearInterval(interval);
	chronoBckgnd.style.backgroundColor = colorRunning;
	stateElement.textContent = stateRunning;
	startTimer(60);
});

goButton.addEventListener("click", function() {
	clearInterval(interval);
	chronoBckgnd.style.backgroundColor = colorRunning;
	stateElement.textContent = stateRunning;
	const timeValue = customTime.value;
	const [hours, minutes, seconds] = timeValue.split(':').map(Number);
	const secondsValue = (hours * 3600) + (minutes * 60) + seconds; 
	startTimer(secondsValue);
});

// Play Sound
function playSound() {
	var audio = new Audio(audioBellSingle);
	audio.volume = volume.value;
	audio.play();
}
