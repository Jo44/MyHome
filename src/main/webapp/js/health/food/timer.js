/////////////////////////////
/////     Food Timer    /////
/////////////////////////////
//   @author Jonathan      //
//   @version 1.2          //
//   @since 17/01/2025     //
/////////////////////////////

/* Global */

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
const stopButton = document.getElementById("btnStop");
const goButton = document.getElementById("btnGo");
const timerElement = document.getElementById("periodLeft");
const stateElement = document.getElementById("state");
// Audio - [Eclipse] localhost:8080/MyHome/audio/track.mp3 <-> ../../audio/track.mp3 [Raspberry]
var audioBellSingle = '../../audio/chrono/bell_single.mp3';
var volume = document.getElementById("volume");
let interval;

// Start Timer
function startTimer(duration) {
	// Initialise l'interval
	let timer = duration, minutes, seconds;
	interval = setInterval(function() {
		minutes = parseInt(timer / 60, 10);
		seconds = parseInt(timer % 60, 10);
		minutes = minutes < 10 ? "0" + minutes : minutes;
		seconds = seconds < 10 ? "0" + seconds : seconds;
		if (--timer < 0) {
			// Timer = 00:00
			playSound();
			chronoBckgnd.style.backgroundColor = colorOff;
			stateElement.textContent = stateStop;
			timerElement.textContent = "00:00";
			clearInterval(interval);
		} else {
			// Timer > 00:00
			timerElement.textContent = minutes + ":" + seconds;
		}
	}, 1000);
}

// Stop Timer
function stopTimer() {
    clearInterval(interval);
    chronoBckgnd.style.backgroundColor = colorOff;
    stateElement.textContent = stateStop;
	timerElement.textContent = "00:00";
}

// Play Sound
function playSound() {
	var audio = new Audio(audioBellSingle);
	audio.volume = volume.value;
	audio.play();
}

/* Listeners */

// Preset - 5 min
fiveMinutesButton.addEventListener("click", function() {
	chronoBckgnd.style.backgroundColor = colorRunning;
	stateElement.textContent = stateRunning;
	timerElement.textContent = "--:--";
	clearInterval(interval);
	startTimer(300);
});

// Preset - 3 min
threeMinutesButton.addEventListener("click", function() {
	chronoBckgnd.style.backgroundColor = colorRunning;
	stateElement.textContent = stateRunning;
	timerElement.textContent = "--:--";
	clearInterval(interval);
	startTimer(180);
});

// Preset - 2 min
twoMinutesButton.addEventListener("click", function() {
	chronoBckgnd.style.backgroundColor = colorRunning;
	stateElement.textContent = stateRunning;
	timerElement.textContent = "--:--";
	clearInterval(interval);
	startTimer(120);
});

// Preset - 1 min 30
oneMinuteThirtyButton.addEventListener("click", function() {
	chronoBckgnd.style.backgroundColor = colorRunning;
	stateElement.textContent = stateRunning;
	timerElement.textContent = "--:--";
	clearInterval(interval);
	startTimer(90);
});

// Preset - 1 min
oneMinuteButton.addEventListener("click", function() {
	chronoBckgnd.style.backgroundColor = colorRunning;
	stateElement.textContent = stateRunning;
	timerElement.textContent = "--:--";
	clearInterval(interval);
	startTimer(60);
});

// Bouton Stop
stopButton.addEventListener("click", function() {
	stopTimer();
});

// Bouton Go
goButton.addEventListener("click", function() {
	chronoBckgnd.style.backgroundColor = colorRunning;
	stateElement.textContent = stateRunning;
	timerElement.textContent = "--:--";
	const timeValue = customTime.value;
	const [hours, minutes, seconds] = timeValue.split(':').map(Number);
	const secondsValue = (hours * 3600) + (minutes * 60) + seconds;
	clearInterval(interval); 
	startTimer(secondsValue);
});
