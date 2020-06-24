package kalah;

import com.qualitascorpus.testsupport.IO;
import static kalah.Kalah.*;



/* The small map below maps boardArray index to hole
   The P1 and P2 store index are stated in the Kalah Class as a static variable.


The numbers in the [] brackets show the index of the boardArray which corresponds to the score of that hole
+---------------+
|       | P2  0 |
+-------+-------+
| 1[ 1] | 6[13] |
| 2[ 2] | 5[12] |
| 3[ 3] | 4[11] |
| 4[ 4] | 3[10] |
| 5[ 5] | 2[ 9] |
| 6[ 6] | 1[ 8] |
+-------+-------+
| P1  7 |       |
+---------------+
*/

public class boardClass {

    public boardClass() {

    }


    public static int[] initialiseBoard(int size,int p1STORE, int p2STORE, int inititalScore) {
        int[] board = new int[size];
        for (int i = 0; i < size; i++) {
            if ((i == p2STORE) || (i == p1STORE)) {
                board[i] = 0;
            } else {
                board[i] = inititalScore;
            }
        }
        return board;
    }

    /***
     *  This method iterates through boardArray to check for any double digits. Spaces are added for single
     *  digits to maintain symmetry of board. spaceCheck array stores " " or "" corrsponding to each hole.
     *  Then it prints the board with the correct values
     * @param io
     * @param array
     */
    public void displayBoard(IO io, int[] array) {


        String[] spaceCheck = new String[14]; //stores " " or "" for each hole

        // iterate through boardArray
        for (int j = 0; j < 14; j++) {
            if (array[j] < 10) {
                spaceCheck[j] = " ";
            } else {
                spaceCheck[j] = "";
            }
        }

        if (orientation == 2) {
            io.println("+---------------+");
            io.println("|       | P2 " + spaceCheck[0] + array[0] + " |");
            io.println("+-------+-------+");
            io.println("| 1[" + spaceCheck[1] + array[1] + "] | 6[" + spaceCheck[13] + array[13] + "] |");
            io.println("| 2[" + spaceCheck[2] + array[2] + "] | 5[" + spaceCheck[12] + array[12] + "] |");
            io.println("| 3[" + spaceCheck[3] + array[3] + "] | 4[" + spaceCheck[11] + array[11] + "] |");
            io.println("| 4[" + spaceCheck[4] + array[4] + "] | 3[" + spaceCheck[10] + array[10] + "] |");
            io.println("| 5[" + spaceCheck[5] + array[5] + "] | 2[" + spaceCheck[9] + array[9] + "] |");
            io.println("| 6[" + spaceCheck[6] + array[6] + "] | 1[" + spaceCheck[8] + array[8] + "] |");
            io.println("+-------+-------+");
            io.println("| P1 " + spaceCheck[7] + array[7] + " |       |");
            io.println("+---------------+");
        }


        // the code below prints the board horizontally
        else {
            io.println("+----+-------+-------+-------+-------+-------+-------+----+");
            io.println("| P2 | 6[" + spaceCheck[13] + array[13] + "] | 5[" + spaceCheck[12] + array[12] + "] | 4[" + spaceCheck[11] + array[11] + "] | 3[" + spaceCheck[10] + array[10] + "] | 2[" + spaceCheck[9] + array[9] + "] | 1[" + spaceCheck[8] + array[8] + "] | " + spaceCheck[7] + array[7] + " |");
            io.println("|    |-------+-------+-------+-------+-------+-------|    |");
            io.println("| " + spaceCheck[0] + array[0] + " | 1[" + spaceCheck[1] + array[1] + "] | 2[" + spaceCheck[2] + array[2] + "] | 3[" + spaceCheck[3] + array[3] + "] | 4[" + spaceCheck[4] + array[4] + "] | 5[" + spaceCheck[5] + array[5] + "] | 6[" + spaceCheck[6] + array[6] + "] | P1 |");
            io.println("+----+-------+-------+-------+-------+-------+-------+----+");
        }
    }


    /***
     * when the game ends due to empty houses, checks
     * @param io
     * @param array
     */
    public void endGameDisplay(IO io, int[] array) {
        io.println("Game over");
        displayBoard(io, array);
        for (int i=0;i<BOARD_SIZE;i++){
            if (0<i && i<7){
                array[p1STORE] += array[i];
            }
            else if (7<i && i<14){
                array[p2STORE] += array[i];
            }
        }
        io.println("\tplayer 1:" + array[7]);
        io.println("\tplayer 2:" + array[0]);
        if (array[p1STORE] > array[p2STORE]) {
            io.println("Player 1 wins!");
        } else if (array[p1STORE] == array[p2STORE]) {
            io.println("A tie!");
        } else {
            io.println("Player 2 wins!");
        }
    }
}
