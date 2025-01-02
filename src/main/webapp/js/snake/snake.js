/////////////////////////////
/////       Snake       /////
/////////////////////////////
//   @author Jonathan      //
//   @version 1.0          //
//   @since 01/01/2025     //
/////////////////////////////

///////////////////////////////////////////////////////////////////////////////////////

//////////////////////
/* Global Variables */
//////////////////////

// UI variables
const focusCatcher = document.getElementById('focusCatcher');
const canvas = document.getElementById('snakeCanvas');
const ctx = canvas.getContext('2d');
const levelSelect = document.getElementById('level');
const speedSelect = document.getElementById('speed');
const muteMusicBtn = document.getElementById('muteMusic');
const volumeSlider = document.getElementById('volume');
const muteFxBtn = document.getElementById('muteFx');

// Audio variables
// Audio - [Eclipse] http://localhost:8080/MyHome/audio/track.mp3 <-> ../../audio/track.mp3 [Raspberry]
const soundStart = new Audio('../../audio/snake/start.mp3');
const soundEnd = new Audio('../../audio/snake/end.mp3');
const soundRecord = new Audio('../../audio/snake/record.mp3');
const soundFood = new Audio('../../audio/snake/food.mp3');
const soundMusic = new Audio('../../audio/snake/music.mp3');
let soundEffects = [soundStart, soundEnd, soundRecord, soundFood];
let isBckMusicPlaying = false;
let isMusicMuted = false;
let areFxMuted = false;
let volume = 0.3;

// Game variables
let gameId;
let isGameRunning = false;
let snake;
let direction;
let foods;
let levels;
let level;
let selectedLevel = "0";
let speed;
let selectedSpeed = "100";

// Scores variables
let records;
let highscore;
let score;
let baseScore;

///////////////////////////////////////////////////////////////////////////////////////

//////////////////////
/* Global Functions */
//////////////////////

// Affichage du menu
async function displayMenu(title, size, button) {
	if (!isGameRunning) {
		// Récupération des records
		await getRecords();
		// Dessin du menu
		drawMenu(title, size, button);
		// Met à jour l'affichage du highscore
		updateHighscoreDisplay();
	}
}

// Début du jeu
function startGame() {
	if (!isGameRunning) {
		// Audio - Start Sound
		playFX(soundStart);
		// Initialisation du jeu
		initGame();
		// Dessin du jeu
		drawGame();
		// Lancement du timer
		gameId = setInterval(gameloop, speed);
		// Activation du jeu
		isGameRunning = true;
	}
}

// Fin du jeu
async function endGame() {
	if (isGameRunning) {
		// Arrêt du timer
		clearInterval(gameId);
		// Sauvegarde du score
		await saveScore();
		// Désactivation du jeu
		isGameRunning = false;
		let newRecord = isARecord();
		// Affichage du menu
		switch (newRecord) {
			case 2:
				displayMenu('WORLD RECORD', '72px', 'RESTART');
				// Audio - Record Sound
				playFX(soundRecord);
				break;
			case 1:
				displayMenu('PERSONAL RECORD', '60px', 'RESTART');
				// Audio - Record Sound
				playFX(soundRecord);
				break;
			default:
				displayMenu('BRAVO', '72px', 'RESTART');
				// Audio - End Sound
				playFX(soundEnd);
				break;
		}
	}
}

///////////////////////////////////////////////////////////////////////////////////////

///////////////////
/* Game Mecanics */
///////////////////

// Initialisation du jeu
function initGame() {
	// Initialisation du level
	level = selectedLevel;
	// Initialisation de la vitesse
	speed = selectedSpeed;
	// Initialisation de la base de score
	calculateBaseScore(level, speed);
	// Initialisation du score
	score = 0;
	// Position initiale du Snake (centre)
	snake = [{ x: 14, y: 14 }];
	// Direction initiale du Snake (haut)
	direction = { x: 0, y: -1 };
	// Initialisation de la première nourriture
	foods = [];
	addFood();
}

// Génération d'une nouvelle nourriture
function addFood() {
	let validFood;
	let newFood;
	// Détermine s'il reste des positions disponibles
	let freePos = getFreePositions();
	if (freePos > 0) {
		// 10000 essais max pour trouver une case vide (sinon game over)
		let tryLeft = 10000;
		do {
			validFood = true;
			// Crée une nouvelle nourriture
			newFood = {
				x: Math.floor(Math.random() * 30),
				y: Math.floor(Math.random() * 30),
				isEaten: false,
				opacity: 1,
				framesLeft: 5
			};
			// Vérifie qu'elle n'est pas à la place d'une autre nourriture déjà existante
			for (const thatFood of foods) {
				if (thatFood.x === newFood.x && thatFood.y === newFood.y) {
					validFood = false;
					break;
				}
			}
			// Vérifie qu'elle n'est pas à la place du Snake
			for (const thatSnake of snake) {
				if (thatSnake.x === newFood.x && thatSnake.y === newFood.y) {
					validFood = false;
					break;
				}
			}
			// Vérifie qu'elle n'est pas à la place d'un mur
			if (levels[level][newFood.y][newFood.x] === 1) {
				validFood = false;
			}
			tryLeft--;
			// Ajoute la nourriture à la liste des nourritures actives
		} while (validFood === false && tryLeft > 0);
		// Détermine la sortie de boucle
		if (tryLeft > 0) {
			// Position déterminée
			foods.push(newFood);
		} else {
			// Plus d'essais pour déterminer la position
			// Fin du jeu
			endGame();
		}
	} else {
		// Plus de position disponible
		// Fin du jeu
		endGame();
	}
}

// Calcul du nombre de positions disponibles
function getFreePositions() {
	// 900 positions au total
	let freePos = 900;
	// Soustrait le nombre de positions occupées par la nourriture
	freePos -= foods.length;
	// Soustrait le nombre de positions occupées par le Snake
	freePos -= snake.length;
	// Soustrait le nombre de positions occupées par les murs (selon le niveau)
	switch (level) {
		case "2":
			freePos -= 0;
			break;
		case "1":
			freePos -= 0;
			break;
		case "0":
			freePos -= 0;
			break;
	}
	return freePos;
}

// Boucle de jeu (rafraîchie toutes les {speed} ms)
function gameloop() {
	// Met à jour le jeu
	updateGame();
	// Met à jour l'affichage du score
	updateScoreDisplay();
	// Vérifie la collision avec le corps ou un mur
	if (checkSelfCollision() || checkWallCollision()) {
		// Arrête le jeu
		endGame();
	} else {
		// Dessine le jeu
		drawGame();
	}
}

// Mise à jour du jeu
function updateGame() {
	// Calcul de la nouvelle position de la tête
	const head = {
		x: snake[0].x + direction.x,
		y: snake[0].y + direction.y
	};

	// Gestion des bords
	if (head.x < 0) {
		head.x = 30 - 1;
	} else if (head.x >= 30) {
		head.x = 0;
	}
	if (head.y < 0) {
		head.y = 30 - 1;
	} else if (head.y >= 30) {
		head.y = 0;
	}

	// Ajoute la nouvelle tête au début du Snake
	snake.unshift(head);

	// Vérifie si le Snake mange la nourriture
	if (!checkFoodCollision()) {
		// Si le Snake n'a pas mangé, retirer la queue
		snake.pop();
	} else {
		// Si le Snake a mangé la nourriture,
		// Génère une nouvelle nourriture
		addFood();
		// Augmentation du score
		score += baseScore;
		// Audio - Food Sound
		playFX(soundFood);
	}
}

///////////////////////////////////////////////////////////////////////////////////////

/////////////
/* UI Menu */
/////////////

// Dessin du canvas menu
function drawMenu(title, size, button) {
	// Effacer le canvas
	ctx.fillStyle = '#222';
	ctx.fillRect(0, 0, 600, 600);
	// Dessine le titre
	drawTitle(title, size);
	// Dessine le bouton
	drawButton(button);
	// Dessine le scoreboard
	drawScoreboard();
}

// Dessin du titre
function drawTitle(title, size) {
	ctx.fillStyle = '#fff';
	ctx.font = `${size} lcd_solidregular`;
	ctx.textAlign = 'center';
	ctx.fillText(title, 300, 150);
}

// Dessin du bouton
function drawButton(button) {
	// Rectangle
	ctx.beginPath();
	ctx.roundRect(240, 200, 120, 50, 5);
	ctx.closePath();
	ctx.lineWidth = 2;
	ctx.strokeStyle = '#888';
	ctx.stroke();
	ctx.fillStyle = '#333';
	ctx.fill();
	// Texte
	ctx.fillStyle = '#fff';
	ctx.font = '30px digital';
	ctx.textAlign = 'center';
	ctx.fillText(button, 300, 235);
}

// Dessin du scoreboard
function drawScoreboard() {
	// Label PB
	let x = 300;
	let y = 310;
	ctx.fillStyle = '#888';
	ctx.font = '25px digital';
	ctx.textAlign = 'center';
	ctx.fillText('Personal Record', x, y);

	// Record PB
	ctx.fillStyle = '#fff';
	ctx.font = '25px lcd_solidregular';
	ctx.textAlign = 'left';

	// Première colonne
	x = 180;
	y = 345;
	ctx.fillText(records[0][0], x, y);

	// Deuxième colonne
	x = 245;
	y = 345;
	ctx.fillText(records[0][1], x, y);

	// Troisième colonne
	x = 410;
	y = 345;
	ctx.fillText(records[0][2], x, y);

	// Label WR
	x = 300;
	y = 390;
	ctx.fillStyle = '#888';
	ctx.font = '25px digital';
	ctx.textAlign = 'center';
	ctx.fillText('World Records', x, y);

	// Records WR
	const lineHeight = 36;
	ctx.fillStyle = '#fff';
	ctx.font = '25px lcd_solidregular';
	ctx.textAlign = 'left';

	// Première colonne
	x = 180;
	y = 425;
	records.slice(1, 4).forEach(record => {
		ctx.fillText(`${record[0]}`, x, y);
		y += lineHeight;
	});

	// Deuxième colonne
	x = 245;
	y = 425;
	records.slice(1, 4).forEach(record => {
		ctx.fillText(`${record[1]}`, x, y);
		y += lineHeight;
	});

	// Troisième colonne
	x = 410;
	y = 425;
	records.slice(1, 4).forEach(record => {
		ctx.fillText(`${record[2]}`, x, y);
		y += lineHeight;
	});
}

///////////////////////////////////////////////////////////////////////////////////////

/////////////
/* UI Game */
/////////////

// Dessin du canvas jeu
function drawGame() {
	// Efface le canvas
	ctx.fillStyle = '#222';
	ctx.fillRect(0, 0, 600, 600);
	// Dessine les murs
	drawWalls();
	// Dessine le Snake
	drawSnake();
	// Dessine la nourriture
	drawFood();
}

// Dessin des murs
function drawWalls() {
	for (let y = 0; y < 30; y++) {
		for (let x = 0; x < 30; x++) {
			if (levels[level][y][x] === 1) {
				let drawX = x * 20;
				let drawY = y * 20;
				let drawWidth = 20;
				let drawHeight = 20;
				let drawRadius = 5;
				ctx.beginPath();
				ctx.moveTo(drawX + drawRadius, drawY);
				ctx.lineTo(drawX + drawWidth - drawRadius, drawY);
				ctx.quadraticCurveTo(drawX + drawWidth, drawY, drawX + drawWidth, drawY + drawRadius);
				ctx.lineTo(drawX + drawWidth, drawY + drawHeight - drawRadius);
				ctx.quadraticCurveTo(drawX + drawWidth, drawY + drawHeight, drawX + drawWidth - drawRadius, drawY + drawHeight);
				ctx.lineTo(drawX + drawRadius, drawY + drawHeight);
				ctx.quadraticCurveTo(drawX, drawY + drawHeight, drawX, drawY + drawHeight - drawRadius);
				ctx.lineTo(drawX, drawY + drawRadius);
				ctx.quadraticCurveTo(drawX, drawY, drawX + drawRadius, drawY);
				ctx.lineWidth = 2;
				ctx.strokeStyle = '#888';
				ctx.stroke();
				ctx.fillStyle = '#555';
				ctx.fill();
			}
		}
	}
}

// Dessin du Snake
function drawSnake() {
	// Tête du Snake
	let drawX = snake[0].x * 20;
	let drawY = snake[0].y * 20;
	let drawWidth = 20;
	let drawHeight = 20;
	let drawRadius = 5;
	ctx.beginPath();
	ctx.moveTo(drawX + drawRadius, drawY);
	ctx.lineTo(drawX + drawWidth - drawRadius, drawY);
	ctx.quadraticCurveTo(drawX + drawWidth, drawY, drawX + drawWidth, drawY + drawRadius);
	ctx.lineTo(drawX + drawWidth, drawY + drawHeight - drawRadius);
	ctx.quadraticCurveTo(drawX + drawWidth, drawY + drawHeight, drawX + drawWidth - drawRadius, drawY + drawHeight);
	ctx.lineTo(drawX + drawRadius, drawY + drawHeight);
	ctx.quadraticCurveTo(drawX, drawY + drawHeight, drawX, drawY + drawHeight - drawRadius);
	ctx.lineTo(drawX, drawY + drawRadius);
	ctx.quadraticCurveTo(drawX, drawY, drawX + drawRadius, drawY);
	ctx.lineWidth = 1;
	ctx.strokeStyle = '#000';
	ctx.stroke();
	ctx.fillStyle = '#2cd736';
	ctx.fill();
	// Corps du Snake
	for (let i = 1; i < snake.length; i++) {
		ctx.strokeRect(snake[i].x * 20, snake[i].y * 20, 20, 20);
		ctx.fillStyle = '#1b8221';
		ctx.fillRect(snake[i].x * 20, snake[i].y * 20, 20, 20);
	}
}

// Dessin de la nourriture
function drawFood() {
	ctx.fillStyle = '#e5ba27';
	foods = foods.filter(food => {
		if (food.isEaten) {
			// Calcul de l'opacité
			food.opacity = Math.max(0, food.framesLeft / 5);
			food.framesLeft--;
		}
		// Dessine la nourriture selon son opacité
		if (food.opacity > 0) {
			ctx.globalAlpha = food.opacity;
			// Dessine 5 petits cercles
			const offsets = [
				[5, 5],
				[15, 5],
				[5, 15],
				[15, 15],
				[10, 10]
			];
			offsets.forEach(([offsetX, offsetY]) => {
				ctx.beginPath();
				ctx.arc(food.x * 20 + offsetX, food.y * 20 + offsetY, 5, 0, Math.PI * 2);
				ctx.fill();
			});
			ctx.globalAlpha = 1;
		}
		// Conserver uniquement si la nourriture n'est pas consommée ou si elle est encore visible
		return !food.isEaten || food.framesLeft > 0;
	});
}

///////////////////////////////////////////////////////////////////////////////////////

////////////
/* Scores */
////////////

// Récupération des scores
async function getRecords() {
	const url = path + '/snake_score';
	try {
		// GET
		const response = await fetch(url);
		// Vérification de la réponse HTTP
		if (!response.ok) {
			throw new Error(`HTTP ${response.status}`);
		}
		// Récupération du JSON
		records = await response.json();
		highscore = records[1][2];
		console.log('Records récupérés :', records);
	} catch (error) {
		// Gestion des erreurs
		console.error('Erreur lors de la récupération des scores :', error);
		records = [['PB', 'Undefined', '0'], ['#1', 'Undefined', '0'], ['#2', 'Undefined', '0'], ['#3', 'Undefined', '0']];
	}
}

// Sauvegarde du score
async function saveScore() {
	const url = path + '/snake_score';
	const payload = { score: score };
	try {
		// POST
		const response = await fetch(url, {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json',
			},
			body: JSON.stringify(payload),
		});
		// Vérification de la réponse HTTP
		if (!response.ok) {
			throw new Error(`HTTP ${response.status}`);
		}
		// Traitement du résultat JSON
		const json = await response.json();
		const { state, result } = json;
		if (state !== 'success') {
			throw new Error(`State ${state}`);
		} else {
			console.log('Score enregistré : ' + score);
		}
	} catch (error) {
		// Gestion des erreurs
		console.error('Erreur lors de la sauvegarde du score :', error);
	}
}

// Détermine si le score est un record personnel ou mondial
function isARecord() {
	// 2 = mondial / 1 = personnel / 0 = non / -1 = undefined
	let newRecord = -1;
	// Compare le score actuel avec le record mondial
	if (score > records[1][2]) {
		// Nouveau Record mondial
		newRecord = 2;
	} else {
		// Compare le score actuel avec le record personnel
		if (score > records[0][2]) {
			// Nouveau Record personnel
			newRecord = 1;
		} else {
			// Pas de record
			newRecord = 0;
		}
	}
	return newRecord;
}

// Détermine le score de base en fonction du niveau et de la vitesse
function calculateBaseScore(level, speed) {
	baseScore = 1;
	// En fonction du niveau
	switch (level) {
		case "2":
			baseScore += 2;
			break;
		case "1":
			baseScore += 1;
			break;
		case "0":
			baseScore += 0;
			break;
	}
	// En fonction de la vitesse
	switch (speed) {
		case "50":
			baseScore += 2;
			break;
		case "75":
			baseScore += 1;
			break;
		case "100":
			baseScore += 0;
			break;
	}
}

// Mise à jour de l'affichage du highscore
function updateHighscoreDisplay() {
	const highscoreElement = document.getElementById('highscore');
	highscoreElement.textContent = highscore;
}

// Mise à jour de l'affichage du score
function updateScoreDisplay() {
	const scoreElement = document.getElementById('score');
	scoreElement.textContent = score;
}

///////////////////////////////////////////////////////////////////////////////////////

////////////////
/* Collisions */
////////////////

// Vérification de la collision de la tête avec la nourriture
function checkFoodCollision() {
	// La tête du Snake
	const head = snake[0];
	// Parcourir la nourriture pour vérifier la collision
	for (let i = 0; i < foods.length; i++) {
		if (head.x === foods[i].x && head.y === foods[i].y && !foods[i].isEaten) {
			// Collision détectée
			foods[i].isEaten = true;
			return true;
		}
	}
	// Pas de collision
	return false;
}

// Vérification de la collision avec son corps
function checkSelfCollision() {
	// La tête du Snake
	const head = snake[0];
	// Parcourir le corps pour vérifier une collision
	for (let i = 1; i < snake.length; i++) {
		if (head.x === snake[i].x && head.y === snake[i].y) {
			// Collision détectée
			return true;
		}
	}
	// Pas de collision
	return false;
}

// Vérification des collisions avec les murs
function checkWallCollision() {
	// La tête du Snake
	const head = snake[0];
	// Vérifier la collision
	if (levels[level][head.y][head.x] === 1) {
		// Collision détectée
		return true;
	}
	// Pas de collision
	return false;
}

///////////////////////////////////////////////////////////////////////////////////////

////////////
/* Events */
////////////

// Clic Play Button
canvas.addEventListener('click', (event) => {
	if (isOnPlayButton(event) && !isGameRunning) {
		// Joue la musique de background
		if (!isBckMusicPlaying) {
			// Audio - Background Music
			playMusic(soundMusic);
			isBckMusicPlaying = true;
		}
		// Cache le curseur
		canvas.style.cursor = 'none';
		// Lance le jeu
		startGame();
	}
});

// Mouse Move
canvas.addEventListener('mousemove', (event) => {
	if (!isGameRunning) {
		if (isOnPlayButton(event)) {
			// Affiche le pointer
			canvas.style.cursor = 'pointer';
		} else {
			// Affiche le curseur par défaut
			canvas.style.cursor = 'default';
		}
	} else {
		// Affiche le curseur par défaut
		canvas.style.cursor = 'default';
	}
});

// On Play Button
function isOnPlayButton(event) {
	const rect = canvas.getBoundingClientRect();
	const x = event.clientX - rect.left;
	const y = event.clientY - rect.top;

	return x >= 240 && x <= 360	&& y >= 200 && y <= 250;
}

// Touches directionnelles
document.addEventListener('keydown', (event) => {
	if (isGameRunning) {
		switch (event.key) {
			case 'ArrowUp':
				if (direction.y === 0) direction = { x: 0, y: -1 };
				break;
			case 'ArrowDown':
				if (direction.y === 0) direction = { x: 0, y: 1 };
				break;
			case 'ArrowLeft':
				if (direction.x === 0) direction = { x: -1, y: 0 };
				break;
			case 'ArrowRight':
				if (direction.x === 0) direction = { x: 1, y: 0 };
				break;
		}
	}
});

// Select level
levelSelect.addEventListener('change', function() {
	selectedLevel = levelSelect.value;
	focusCatcher.focus();
});

// Select speed
speedSelect.addEventListener('change', function() {
	selectedSpeed = speedSelect.value;
	focusCatcher.focus();
});

// Clic bouton Mute Musique
muteMusicBtn.addEventListener('click', () => {
	isMusicMuted = !isMusicMuted;
	soundMusic.muted = isMusicMuted;
	muteMusicBtn.style.backgroundImage = isMusicMuted
		? "url('img/snake/mute-music-on.png')"
		: "url('img/snake/mute-music-off.png')";
	focusCatcher.focus();
});

// Slider volume Musique / FX
volumeSlider.addEventListener('input', (event) => {
	setVolume(event.target.value);
	focusCatcher.focus();
});

// Clic bouton Mute FX
muteFxBtn.addEventListener('click', () => {
	areFxMuted = !areFxMuted;
	soundEffects.forEach(sound => sound.muted = areFxMuted);
	muteFxBtn.style.backgroundImage = areFxMuted
		? "url('img/snake/mute-fx-on.png')"
		: "url('img/snake/mute-fx-off.png')";
	focusCatcher.focus();
});

///////////////////////////////////////////////////////////////////////////////////////

///////////
/* Audio */
///////////

// Lecture du sound effect
function playFX(audio) {
	// Configuration de la boucle
	audio.loop = false;
	// Configuration du volume
	audio.volume = volume;
	// Lecture du son
	audio.play();
}

// Lecture de la musique
function playMusic(audio) {
	// Configuration de la boucle
	audio.loop = true;
	// Initialisation du volume à 0
	audio.volume = 0;
	// Lecture du son
	audio.play();
	// Fondu en ouverture
	let fadeInterval = setInterval(() => {
		audio.volume += 0.01;
		if (audio.volume >= volume) {
			clearInterval(fadeInterval);
		}
	}, 100);
}

// Réglage du volume
function setVolume(value) {
	// Configuration du volume
	volume = value;
	soundMusic.volume = value;
	soundEffects.forEach(sound => sound.volume = value);
}

///////////////////////////////////////////////////////////////////////////////////////

////////////
/* Levels */
////////////

// Initialisation des niveaux
function initLevels() {
	levels = [
		// Niveau 1 : Pas de murs
		[
			[0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
			[0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
			[0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
			[0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
			[0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
			[0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
			[0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
			[0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
			[0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
			[0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
			[0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
			[0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
			[0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
			[0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
			[0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
			[0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
			[0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
			[0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
			[0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
			[0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
			[0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
			[0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
			[0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
			[0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
			[0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
			[0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
			[0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
			[0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
			[0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
			[0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
		],
		// Niveau 2 : 2 barres verticales
		[
			[0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
			[0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
			[0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
			[0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
			[0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
			[0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
			[0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0],
			[0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0],
			[0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0],
			[0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0],
			[0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0],
			[0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0],
			[0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0],
			[0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0],
			[0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0],
			[0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0],
			[0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0],
			[0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0],
			[0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0],
			[0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0],
			[0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0],
			[0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0],
			[0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0],
			[0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0],
			[0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
			[0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
			[0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
			[0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
			[0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
			[0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
		],
		// Niveau 3 : 5 obstacles
		[
			[0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
			[0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
			[0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
			[0, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0],
			[0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0],
			[0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0],
			[0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0],
			[0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0],
			[0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0],
			[0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
			[0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
			[0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
			[0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
			[0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
			[0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
			[0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
			[0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
			[0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
			[0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
			[0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
			[0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
			[0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0],
			[0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0],
			[0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0],
			[0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0],
			[0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0],
			[0, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0],
			[0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
			[0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
			[0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
		]
	];
}

///////////////////////////////////////////////////////////////////////////////////////

///////////////////
/* Au chargement */
///////////////////

// Initialisation préalable au jeu
window.onload = async function() {
	// Initialisation des niveaux
	initLevels();
	// Initialisation du volume
	setVolume(volume);
	// Lancement du menu
	await displayMenu('SNAKE', '86px', 'START');
}

///////////////////////////////////////////////////////////////////////////////////////
