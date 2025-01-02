////////////////////////////////////
/////     Sport Chronometer    /////
////////////////////////////////////
//   @author Jonathan             //
//   @version 1.0                 //
//   @since 09/11/2021            //
////////////////////////////////////

// Global variables
var colorOff = '#585858';
var colorRunning = '#449d44';
var colorRest = '#bd403c';
// Audio - [Eclipse] localhost:8080/MyHome/audio/track.mp3 <-> ../../audio/track.mp3 [Raspberry]
var audioLaser = '../../audio/chrono/laser.mp3';
var audioBellSingle = '../../audio/chrono/bell_single.mp3';
var audioBellDouble = '../../audio/chrono/bell_double.mp3';
var audioBellTriple = '../../audio/chrono/bell_triple.mp3';
var defaultTime = '00:00:00';
var presetOneEffort = '00:00:20';
var presetOneRest = '00:00:10';
var	presetOneRepeats = '20';
var presetTwoEffort = '00:15:00';
var presetTwoRest = '00:03:00';
var	presetTwoRepeats = '4';

// UI Components
var chronoDetailsArrow = $('#chronoDetailsArrow');
var chronoDetails = $('#chronoDetails');
var btnAction = $('#btnAction');
var btnStop = $('#btnStop');
var chronoBckgnd = $('#chronoBckgnd');
var periodLeftElement = $('#periodLeft');
var stateElement = $('#state');
var totalLeftElement = $('#totalLeft');
var totalPastElement = $('#totalPast');
var volume = $('#volume');
var delayElement = $('#delay');
var effortPeriodElement = $('#effortPeriod');
var restPeriodElement = $('#restPeriod');
var repeatsElement = $('#repeats');

//////////////////////////

//
//// Chrono
//

// [Singleton]
var Chrono = (function() {
	class chrono {
		constructor() {

			// Variables
			var running;
			var rest;
			var btn;
			var color;
			var state;
			var delay;
			var delayMark;
			var startMark;
			var continueMark;
			var effortPeriod;
			var restPeriod;
			var repeats;
			var periodLeft;
			var totalLeft;
			var totalPast;

			// Start
			this.start = function() {
				// Init
				init();
				running = true;
				startMark = true;
				// Run
				run();
			}

			// Pause
			this.pause = function() {
				running = false;
				btn = btnContinue;
				color = colorOff;
				state = statePause;
				// Update UI
				updateUI(btn, color, state, formatDate(periodLeft), formatDate(totalLeft), formatDate(totalPast));
			}

			// Continue
			this.continue = function() {
				running = true;
				continueMark = true;
				// Run
				run();
			}

			// Stop
			this.stop = function() {
				// Init
				init();
				btn = btnStart;
				color = colorOff;
				state = stateStop;
				// Update UI
				updateUI(btn, color, state, formatDate(periodLeft), formatDate(totalLeft), formatDate(totalPast));
			}

			// Init
			var init = function() {
				
				// Get variables
				delay = (delayElement.val() && delayElement.val() != '' && delayElement.val() >= 0 && delayElement.val() <= 60) ? delayElement.val() : 0;
				effortPeriod = (effortPeriodElement.val() && effortPeriodElement.val() != '') ? formatSeconds(effortPeriodElement.val()) : 0;
				restPeriod = (restPeriodElement.val() && restPeriodElement.val() != '') ? formatSeconds(restPeriodElement.val()) : 0;
				repeats = (repeatsElement.val() && repeatsElement.val() != '' && repeatsElement.val() >= 0 && repeatsElement.val() <= 100) ? repeatsElement.val() : 0;

				// Set variables
				running = false;
				rest = false;
				delayMark = (delay > 0) ? true: false;
				startMark = false;
				continueMark = false;
				periodLeft = parseInt(effortPeriod);
				totalLeft = (parseInt(effortPeriod) + parseInt(restPeriod)) * parseInt(repeats);
				totalPast = 0;

			}

			// Run
			var run = function() {

				// Check running
				if (running) {

					// Check times
					if (totalLeft < 1) {

						// [DONE]

						// Play sound
						playSound(audioBellTriple);
						// Reinit
						init();
						// Update variables
						btn = btnStart;
						color = colorOff;
						state = stateDone;
						// Update UI
						updateUI(btn, color, state, formatDate(periodLeft), formatDate(totalLeft), formatDate(totalPast));

					} else {
						
						// Delays
						if (delayMark || delay > 0) {
						
							// [WAITING]
							
							// Play sounds
							if (delayMark) {
								// [Delay] Start
								playSound(audioLaser);
								delayMark = false;
							}
							if (delay < 4) {
								// [Incoming] Less than 4 seconds
								playSound(audioLaser);
							}
							
							// Set variables
							btn = btnPause;
							color = colorOff;
							state = stateWaiting;
							
							// Update UI
							updateUI(btn, color, state, '-' + delay, formatDate(totalLeft), formatDate(totalPast));
							
							// Update variable
							delay--;
						
						} else {

							// [RUNNING]
							
							// Play sounds
							if (startMark) {
								playSound(audioBellSingle);
								startMark = false;
							}
							if (continueMark) {
								if (rest == false) {
									playSound(audioBellSingle);
								} else {
									playSound(audioBellDouble);
								}
								continueMark = false;
							}
	
							// Check times
							if (periodLeft < 4 && periodLeft > 0) {
	
								// [Incoming] Less than 4 seconds
								// Play sound
								playSound(audioLaser);
	
							} else if (periodLeft < 1) {
	
								// Switch period type & set period left
								if (rest == false) {
	
									// [Effort -> Rest]
									rest = true;
									periodLeft = restPeriod;
									// Play sound
									playSound(audioBellDouble);
	
								} else {
	
									// [Rest -> Effort]
									rest = false;
									periodLeft = effortPeriod;
									// Play sound
									playSound(audioBellSingle);
	
								}
							}
							
							// Set variables
							btn = btnPause;
							if (rest == false) {
								color = colorRunning;
								state = stateRunning;
							} else {
								color = colorRest;
								state = stateRest;
							}
							
							// Update UI
							updateUI(btn, color, state, formatDate(periodLeft), formatDate(totalLeft), formatDate(totalPast));
							
							// Update variables
							periodLeft--;
							totalLeft--;
							totalPast++;
							
						}

						// Running || Waiting ..
						setTimeout(run, 1000);

					}

				}

			}
			
			// Getter
			this.isRunning = function() {
				return running;
			}

		}
	}

	// Instance
	var instance = null;
	return new function() {
		this.getInstance = function() {
			if (instance == null) {
				instance = new chrono();
				instance.constructeur = null;
			}
			return instance;
		}
	}
})();

//////////////////////////

//
//// Instance
//

var chrono = Chrono.getInstance();

// Buttons Start/Pause/Continue
function actionChrono() {
	if (btnAction.text() === btnStart) {
		// Start
		chrono.start();
	} else if (btnAction.text() === btnPause) {
		// Pause
		chrono.pause();
	} else {
		// Continue
		chrono.continue();
	}
	// Anti-spam
	lockSpam();
}

// Button Stop
function stopChrono() {
	// Stop
	chrono.stop();
	// Anti-spam
	lockSpam();
}

//////////////////////////

//
//// Presets
//

// Preset Training
function presetTraining() {
	effortPeriodElement.val(presetOneEffort);
	effortPeriodElement.prop('disabled', true);
	restPeriodElement.val(presetOneRest);
	restPeriodElement.prop('disabled', true);
	repeatsElement.val(presetOneRepeats);
	repeatsElement.prop('disabled', true);
	// If not running
	if (!chrono.isRunning()) {
		periodLeftElement.html(effortPeriodElement.val());
		totalLeftElement.html(formatDate((formatSeconds(effortPeriodElement.val()) + formatSeconds(restPeriodElement.val())) * parseInt(repeatsElement.val())));
		totalPastElement.html(formatDate(0));
	}
}

// Preset Endurance
function presetEndurance() {
	effortPeriodElement.val(presetTwoEffort);
	effortPeriodElement.prop('disabled', true);
	restPeriodElement.val(presetTwoRest);
	restPeriodElement.prop('disabled', true);
	repeatsElement.val(presetTwoRepeats);
	repeatsElement.prop('disabled', true);
	// If not running
	if (!chrono.isRunning()) {
		periodLeftElement.html(effortPeriodElement.val());
		totalLeftElement.html(formatDate((formatSeconds(effortPeriodElement.val()) + formatSeconds(restPeriodElement.val())) * parseInt(repeatsElement.val())));
		totalPastElement.html(formatDate(0));
	}
}

// Preset Custom
function presetCustom() {
	effortPeriodElement.prop('disabled', false);
	restPeriodElement.prop('disabled', false);
	repeatsElement.prop('disabled', false);
}

//
//// Custom
//

// Change custom
function changeCustom() {
	if (!chrono.isRunning()) {
		var newEffortPeriod = (effortPeriodElement.val() && effortPeriodElement.val() != '' && effortPeriodElement.val().length == 8) ? effortPeriodElement.val() : defaultTime;
		var newRestPeriod = (restPeriodElement.val() && restPeriodElement.val() != '' && restPeriodElement.val().length == 8) ? restPeriodElement.val() : defaultTime;
		var newRepeats = (repeatsElement.val() && repeatsElement.val() != '') ? repeatsElement.val() : 1;
		periodLeftElement.html(newEffortPeriod);
		totalLeftElement.html(formatDate((formatSeconds(newEffortPeriod) + formatSeconds(newRestPeriod)) * parseInt(newRepeats)));
	}
}

//
//// Global
//

// Update UI
function updateUI(btn, color, state, pLeft, tLeft, tPast) {
	btnAction.text(btn);
	chronoBckgnd.css("background-color", color);
	periodLeftElement.html(pLeft);
	stateElement.html(state);
	totalLeftElement.html(tLeft);
	totalPastElement.html(tPast);
}

// Switch chrono details
function displayChronoDetails() {
	if (chronoDetails.is(":visible")) {
		// Hide
		chronoDetails.hide();
		// Update arrow direction
		chronoDetailsArrow.toggleClass('fa-arrow-circle-up fa-arrow-circle-down');
	} else {
		// Show
		chronoDetails.show();
		// Update arrow direction
		chronoDetailsArrow.toggleClass('fa-arrow-circle-down fa-arrow-circle-up');
	}
}

// Lock spam buttons
function lockSpam() {
	// Lock buttons
	btnAction.prop('disabled', true);
	btnStop.prop('disabled', true);
	// Wait 1 sec
	setTimeout(function() {
		// Unlock buttons
		btnAction.prop('disabled', false);
		btnStop.prop('disabled', false);
	}, 1000);

}

// Play Sound
function playSound(url) {
	var audio = new Audio(url);
	audio.volume = volume.val();
	audio.play();
}

// Format Date (s -> HH:mm:ss)
function formatDate(seconds) {
	return new Date(seconds * 1000).toISOString().substr(11, 8);
}

// Format Seconds (HH:mm:ss -> s)
function formatSeconds(date) {
	var times = date.split(':');
	return (parseInt(times[0]) * 3600) + (parseInt(times[1]) * 60) + parseInt(times[2]);
}

//////////////////////////
