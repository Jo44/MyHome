///////////////////////////////
/////     Game of Life    /////
///////////////////////////////
//   @author Jonathan        //
//   @version 1.2            //
//   @since 17/01/2025       //
///////////////////////////////

/* Variables */

// Elements de l'UI
const gameControlsArrow = document.getElementById('game-controls-arrow');
const lifeContainer = document.getElementById('life');
const gameControls = document.getElementById('game-controls');
const speedSlider = document.getElementById('speed');
const speedText = document.getElementById('speedText');
const rowsSlider = document.getElementById('rows');
const rowsText = document.getElementById('rowsText');
const colsSlider = document.getElementById('cols');
const colsText = document.getElementById('colsText');

// Variables globales
let delay;
let rows;
let cols;
let grid;
let previousGrid;
let intervalId;

/* Boucle principale */

// Génération de la vie
function generate() {
	// Initialise l'interval
	if (intervalId) {
		clearInterval(intervalId);
	}
	// Initialise les paramètres de jeu 
	delay = calculateDelay(parseInt(speedSlider.value, 10));
	rows = parseInt(rowsSlider.value, 10);
	cols = parseInt(colsSlider.value, 10);
	// Initialise le style
	document.documentElement.style.setProperty('--rows', rows);
	document.documentElement.style.setProperty('--cols', cols);
	// Initialise les grilles (précédente et actuelle)
	previousGrid = createEmptyGrid(rows, cols);
	grid = createEmptyGrid(rows, cols);
	grid = randomizeGrid(grid);
	// Affiche la grille
	displayGrid(grid);
	// Lance l'interval
	intervalId = setInterval(() => {
		// Fait évoluer le jeu
		grid = evolve(grid);
		// Affiche la grille
		displayGrid(grid);
	}, delay);
}

/* Initialisation */

// Création d'un tableau 2D de cellules vides
function createEmptyGrid(rows, cols) {
	return Array.from({ length: rows }, () => Array(cols).fill(0));
}

// Génération d'une grille aléatoire
function randomizeGrid(grid) {
	return grid.map(row => row.map(() => Math.random() > 0.7 ? 1 : 0));
}

/* Evolution */

// Evolution du jeu d'une génération
function evolve(grid) {
	previousGrid = grid.map(row => [...row]);
	const newGrid = createEmptyGrid(grid.length, grid[0].length);
	for (let i = 0; i < grid.length; i++) {
		for (let j = 0; j < grid[i].length; j++) {
			const neighbors = countNeighbors(grid, i, j);
			if (grid[i][j] === 1) {
				// Cellule vivante
				newGrid[i][j] = neighbors === 2 || neighbors === 3 ? 1 : 0;
			} else {
				// Cellule morte
				newGrid[i][j] = neighbors === 3 ? 1 : 0;
			}
		}
	}
	return newGrid;
}

// Comptage du nombre de cellules voisines
function countNeighbors(grid, row, col) {
	const neighborsOffsets = [
		[-1, -1], [-1, 0], [-1, 1],
		[0, -1], [0, 1],
		[1, -1], [1, 0], [1, 1]
	];
	return neighborsOffsets.reduce((acc, [offsetRow, offsetCol]) => {
		const newRow = (row + offsetRow + grid.length) % grid.length;
		const newCol = (col + offsetCol + grid[0].length) % grid[0].length;
		acc += grid[newRow][newCol];
		return acc;
	}, 0);
}

/* Affichage */

// Affichage de la grille
function displayGrid(grid) {
	lifeContainer.innerHTML = '';
	grid.forEach((row, rowIndex) => {
		row.forEach((cell, colIndex) => {
			const cellElement = document.createElement('div');
			cellElement.className = `cell ${cell ? 'alive' : 'dead'} ${(cell && !previousGrid[rowIndex][colIndex]) ? 'new' : ''
				}`;
			cellElement.addEventListener('click', () => toggleCell(rowIndex, colIndex));
			lifeContainer.appendChild(cellElement);
		});
	});
}

// Affichage des contrôles de jeu
function displayGameControls() {
	if (gameControls.style.display !== 'none') {
		gameControls.style.display = 'none';
		gameControlsArrow.classList.toggle('fa-arrow-circle-up', 'fa-arrow-circle-down');
	} else {
		gameControls.style.display = 'block';
		gameControlsArrow.classList.toggle('fa-arrow-circle-down', 'fa-arrow-circle-up');
	}
}

/* Contrôles */

// Modification des valeurs des sliders
function updateValue(type) {
	// Selon le slider
	switch (type) {
		case 'speed':
			// Met à jour la vitesse de jeu
			updateSpeed();
			break;
		case 'rows':
			// Met à jour le nombre de lignes
			updateRows();
			break;
		case 'cols':
			// Met à jour le nombre de colonnes
			updateCols();
			break;			
	}
}

// Changement d'état d'une cellule (clic)
function toggleCell(row, col) {
	// Met en pause l'interval
	clearInterval(intervalId);
	// Inverse l'état de la cellule
	grid[row][col] = grid[row][col] === 1 ? 0 : 1;
	displayGrid(grid);
	// Relance l'interval
	intervalId = setInterval(() => {
		grid = evolve(grid);
		displayGrid(grid);
	}, delay);
}

/* Global */

// Convertion du slider en vitesse
function calculateDelay(value) {
	// Slider : 0% <> 100%
	// Delay : 1000ms <> 50 ms
	return 1000 - (value / 100) * 950;
}

// Modification de la vitesse de jeu
function updateSpeed() {
	// Calcule la nouvelle vitesse
	delay = calculateDelay(parseInt(speedSlider.value, 10));
	// Affiche la nouvelle vitesse (en %)
	speedText.textContent = speedSlider.value;
	// Relance l'interval avec la nouvelle vitesse
	clearInterval(intervalId);
	intervalId = setInterval(() => {
		grid = evolve(grid);
		displayGrid(grid);
	}, delay);
}

// Modification du nombre de lignes
function updateRows() {
	rows = parseInt(rowsSlider.value, 10);
	rowsText.textContent = rowsSlider.value;
}

// Modification du nombre de colonnes
function updateCols() {
	cols = parseInt(colsSlider.value, 10);
	colsText.textContent = colsSlider.value;
}

/* Auto-execution */

// Document Ready
$(document).ready(function() {
	// Génère la vie !
	generate();
});
