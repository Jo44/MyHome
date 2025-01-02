///////////////////////////////
/////     Game of Life    /////
///////////////////////////////
//   @author Jonathan        //
//   @version 1.1            //
//   @since 30/12/2024       //
///////////////////////////////

/* Global */

// Configuration initiale
let speed = 250;
let rows = 50;
let cols = 50;
let grid;
let previousGrid;
let intervalId;

// Bornage des paramètres initiaux
function checkVars() {
	speed < 50 ? 50 : speed;
	speed > 1000 ? 1000 : speed;
	rows < 10 ? 10 : rows;
	rows > 100 ? 100 : rows;
	cols < 10 ? 10 : cols;
	cols > 100 ? 100 : cols;
}

// Création d'un tableau 2D de cellules vides
function createEmptyGrid(rows, cols) {
	return Array.from({ length: rows }, () => Array(cols).fill(0));
}

// Génération d'une grille aléatoire
function randomizeGrid(grid) {
	return grid.map(row => row.map(() => Math.random() > 0.7 ? 1 : 0));
}

// Affichage de la grille
function displayGrid(grid) {
	const gridContainer = document.getElementById('grid-container');
	gridContainer.innerHTML = '';

	grid.forEach((row, rowIndex) => {
		row.forEach((cell, colIndex) => {
			const cellElement = document.createElement('div');
			cellElement.className = `cell ${cell ? 'alive' : 'dead'} ${
			    (cell && !previousGrid[rowIndex][colIndex]) ? 'new' : ''
			}`;
			cellElement.addEventListener('click', () => toggleCell(rowIndex, colIndex));
			gridContainer.appendChild(cellElement);
		});
	});
}

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

// Comptage du nombre de voisins d'une cellule
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

// Râfraichissement
function refresh() {
	if (intervalId) {
		clearInterval(intervalId);
	}

	rows = parseInt(document.getElementById('rows').value, 10);
	cols = parseInt(document.getElementById('cols').value, 10);
	speed = parseInt(document.getElementById('speed').value, 10);

	document.documentElement.style.setProperty('--rows', rows);
	document.documentElement.style.setProperty('--cols', cols);

	previousGrid = createEmptyGrid(rows, cols);
	grid = createEmptyGrid(rows, cols);
	grid = randomizeGrid(grid);
	displayGrid(grid);

	// Boucle pour faire évoluer le jeu
	intervalId = setInterval(() => {
		grid = evolve(grid);
		displayGrid(grid);
	}, speed);
}

// Bascule l'état d'une cellule lorsqu'elle est cliquée
function toggleCell(row, col) {
	// Met en pause l'interval
	clearInterval(intervalId);

	// Modifie l'état de la cellule
	grid[row][col] = grid[row][col] === 1 ? 0 : 1;
	displayGrid(grid);

	// Relance l'interval
	intervalId = setInterval(() => {
		grid = evolve(grid);
		displayGrid(grid);
	}, speed);
}

// Affichage des contrôles du jeu
function displayGameControls() {
	var gameControlsArrow = $('#game-controls-arrow');
	var gameControls = $('#game-controls');
	if (gameControls.is(":visible")) {
		// Cache
		gameControls.hide();
		// Met à jour la direction de la flèche
		gameControlsArrow.toggleClass('fa-arrow-circle-up fa-arrow-circle-down');
	} else {
		// Affiche
		gameControls.show();
		// Met à jour la direction de la flèche
		gameControlsArrow.toggleClass('fa-arrow-circle-down fa-arrow-circle-up');
	}
}

// Met à jour l'affichage des valeurs des sliders
function updateValue(id) {
	const value = document.getElementById(id).value;
	document.getElementById(id + 'Value').textContent = value;
	if (id === 'speed') {
		updateSpeed();
	}
}

// Met à jour la vitesse du jeu
function updateSpeed() {
	speed = parseInt(document.getElementById('speed').value, 10);
	clearInterval(intervalId);
	intervalId = setInterval(() => {
		grid = evolve(grid);
		displayGrid(grid);
	}, speed);
}

// Auto-execution
$(document).ready(function() {
	refresh();
});