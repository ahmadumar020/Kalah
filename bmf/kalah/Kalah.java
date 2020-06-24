package kalah;

import com.qualitascorpus.testsupport.IO;
import com.qualitascorpus.testsupport.MockIO;

import java.lang.reflect.Array;

/**
 * This class is the starting point for a Kalah implementation using
 * the test infrastructure.
 */
public class Kalah {

	// Game initialisation constants
	static final int p1STORE = 7; // boardArray index that refers to player 1's store
	static final int p2STORE = 0; // boardArray index that refers to player 2's store
	static final int BOARD_SIZE = 14; //number of holes in the board. Has to be an even number
	static final int INITIAL_SCORE = 4; //initial number of marbles in each hole
	static final int P1_START = 1;
	static final int P2_START = 8;


	//board orientation
	static final int orientation = 1; //1 means horizontal, 2 means vertical.

	// create an instance board of the boardClass
	boardClass board = new boardClass();



	int playerInput; // integer to store player input

	String string1 = "Player P1's turn - Specify house number or 'q' to quit: ";
	String string2 = "Player P2's turn - Specify house number or 'q' to quit: ";

	//initialises a player's turn; true means Player 1's turn
	boolean player1Turn = true;
	// check for whether game has ended
	boolean endGame = false;

	boolean robotMode = true;

	int automatedPlayer = 2;


	public static void main(String[] args) {

		new Kalah().play(new MockIO());
	}


	/***
	 * This method just runs the game using while loop. Makes calls to other methods.
	 * @param io
	 */
	public void play(IO io) {
		int[] boardArray = board.initialiseBoard(BOARD_SIZE, p1STORE,p2STORE,INITIAL_SCORE);

		boolean endGame = false; //loop ends when game ends

		//displays initial board
		board.displayBoard(io, boardArray);
		//initial input from Player1
		playerInput = io.readInteger(string1, 1, 6, -1, "q");


		while (!checkEndGame(player1Turn,boardArray) && playerInput != -1){
			gamelogic(io, boardArray);
		}

		if (playerInput == -1) {
			io.println("Game over");
			board.displayBoard(io, boardArray);
		} else if (checkEndGame(player1Turn,boardArray)) {
			board.endGameDisplay(io, boardArray);
		}
	}


	/***
	 * Contains the game logic; takes care of moves, wraps, and captures
	 * @param io
	 * @param boardArray
	 */
	public void gamelogic(IO io, int[] boardArray){
		if (player1Turn) {
			if (boardArray[playerInput] != 0) {
				int moves = boardArray[playerInput];
				//boolean checkEmpty = false;
				boardArray[playerInput] = 0;
				int arrayIndex = playerInput;
				for (int i = 0; i < moves; i++) {
					arrayIndex++;
					if (arrayIndex > BOARD_SIZE -1) {
						arrayIndex = p2STORE;
					}
					if (arrayIndex == p2STORE) {
						i--;
					} else {
						int captureIndex = BOARD_SIZE - arrayIndex;
						if (i == (moves - 1)  && (p2STORE<arrayIndex) && (p1STORE>arrayIndex) && boardArray[arrayIndex] == 0 && boardArray[captureIndex] != 0) {

							//adds all the marbles from the capture to p1 store
							boardArray[p1STORE] = boardArray[p1STORE] + 1 + boardArray[captureIndex];
							boardArray[captureIndex] = 0;
						} else {
							boardArray[arrayIndex] = boardArray[arrayIndex] + 1;
						}
					}
				}
				if (arrayIndex == p1STORE) {
					board.displayBoard(io, boardArray);
					if (checkEndGame(player1Turn,boardArray)) {
						endGame = true;
					} else {
						playerInput = io.readInteger(string1, 1, 6, -1, "q");
					}
				} else {
					player1Turn = !player1Turn;
					board.displayBoard(io, boardArray);
					if (checkEndGame(player1Turn,boardArray)) {
						endGame = true;
					} else {
						//player 2 turn now so we check if robotMode is enabled or not
						if (robotMode){
							int status = checkBestMove(boardArray,automatedPlayer);
							if (status==1){
								io.println("Player P2 (Robot) chooses house #"+playerInput+" because it leads to an extra move");
							}
							else if(status==2){
								io.println("Player P2 (Robot) chooses house #"+playerInput+" because it leads to a capture");
							}

							else if(status==3){
								io.println("Player P2 (Robot) chooses house #"+playerInput+" because it is the first legal move");
							}
						}
						else {
							playerInput = io.readInteger(string2, 1, 6, -1, "q");
						}
					}
				}
			} else {
				io.println("House is empty. Move again.");
				board.displayBoard(io, boardArray);
				playerInput = io.readInteger(string1, 1, 6, -1, "q");
			}
		} // end of player 1 logic


		else if (!player1Turn) {
			int p2Index = playerInput + (BOARD_SIZE/2);
			if (boardArray[p2Index] != 0) {
				int moves = boardArray[p2Index];
				boardArray[p2Index] = 0;
				int arrayIndex = p2Index;
				for (int i = 0; i < moves; i++) {
					arrayIndex++;
					if (arrayIndex > BOARD_SIZE -1) {
						arrayIndex = 0;
					}
					if (arrayIndex == p1STORE) {
						i--;
					} else {
						int captureIndex = BOARD_SIZE - arrayIndex;
						if ((i == (moves - 1)) && (p1STORE < arrayIndex) && (BOARD_SIZE > arrayIndex) && (boardArray[arrayIndex] == 0) && (boardArray[captureIndex] != 0)) {
							boardArray[p2STORE] = boardArray[p2STORE] + 1 + boardArray[captureIndex];
							boardArray[captureIndex] = 0;
						} else {
							boardArray[arrayIndex] = boardArray[arrayIndex] + 1;
						}
					}
				}
				if (arrayIndex == p2STORE) {

					board.displayBoard(io, boardArray);
					if (checkEndGame(player1Turn,boardArray)) {
						endGame = true;
					} else {
						//player 2 turn now so we check if robotMode is enabled or not
						if (robotMode){
							int status = checkBestMove(boardArray,automatedPlayer);
							if (status==1){
								io.println("Player P2 (Robot) chooses house #"+playerInput+" because it leads to an extra move");
							}
							else if(status==2){
								io.println("Player P2 (Robot) chooses house #"+playerInput+" because it leads to a capture");
							}

							else if(status==3){
								io.println("Player P2 (Robot) chooses house #"+playerInput+" because it is the first legal move");
							}
						}
						else {
							playerInput = io.readInteger(string2, 1, 6, -1, "q");
						}
					}
				} else {
					player1Turn = !player1Turn;
					board.displayBoard(io, boardArray);
					if (checkEndGame(player1Turn,boardArray)) {
						endGame = true;
					} else {
						playerInput = io.readInteger(string1, 1, 6, -1, "q");
					}
				}
			} else {
				io.println("House is empty. Move again.");
				board.displayBoard(io, boardArray);
				//player 2 turn again so we check if robotMode is enabled or not
				if (robotMode){
					int status = checkBestMove(boardArray,automatedPlayer);
					if (status==1){
						io.println("Player P2 (Robot) chooses house #"+playerInput+" because it leads to an extra move");
					}
					else if(status==2){
						io.println("Player P2 (Robot) chooses house #"+playerInput+" because it leads to a capture");
					}

					else if(status==3){
						io.println("Player P2 (Robot) chooses house #"+playerInput+" because it is the first legal move");
					}
				}
				else {
					playerInput = io.readInteger(string2, 1, 6, -1, "q");
				}
			}
		} // end of player 2 logic
		endGame = checkEndGame(player1Turn,boardArray); // do an endgame check
	}


	/***
	 * checks if all the holes for any one player are empty and stores as a boolean in result
	 * @param array
	 * @return boolean result
	 */
	public boolean checkEndGame(boolean player1Turn, int[] array) {
		int k;
		boolean check = true;

		if (player1Turn) { k = P1_START;}
		else {k = P2_START;}

//         loop through active player's scores (1-6) to check if all 0
		for (int i = k; i < k+6; i++) {
			if (array[i] != 0) {
				check = false;
				break;
			}
		}
		return check;
	}

	public int checkBestMove(int[] array, int player){
		int legalMove =0;
		int captureMove = 0;
		int k=0;
		//this if statement sets k to first pit of the automated player.
		if (player ==1){k=1;}
		else {k=8;}

		//iterate through the for loop and check for possibilities of an extra move, capture else a legal move
		for(int i=k;i<(k+6);i++){
			if(array[i]==0){continue;}
			if (array[i] != 0 && legalMove==0){
				legalMove = i;
			}
			int moves = i + array[i] % 13;
			int finalHouse = BOARD_SIZE - moves;
			if (finalHouse== p1STORE) {finalHouse=8;}
			if (finalHouse== p2STORE) {
				playerInput= i - BOARD_SIZE/2;
				return 1;
			}
			if(moves>BOARD_SIZE){
				finalHouse = Math.abs(BOARD_SIZE-moves);
				if (finalHouse>6){
					finalHouse++; //add one to skip p1Store

					//if wrapping took place then p1 pits have new marbles thus capture can take place
					if (captureMove==0 && array[finalHouse]==0) {
						captureMove = i - BOARD_SIZE / 2;
					}
				}
			}
			else {finalHouse=moves;}

			//checks all conditions needed for a capture.
			if(finalHouse>p1STORE && finalHouse <BOARD_SIZE && array[finalHouse]==0 && captureMove==0 && array[BOARD_SIZE - finalHouse]!=0){
				captureMove = i-BOARD_SIZE/2;
			}
		}
		if(captureMove!=0){
			playerInput = captureMove;
			return 2;
		}

		//if no extra move and no capture, we do the lowest legal move which is stored in variable legalMove
		playerInput = legalMove - BOARD_SIZE/2;
		return 3;
	}
} //end of class Kalah
