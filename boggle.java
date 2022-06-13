import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class boggle {
    static Random random = new Random();

    public static char[][][] fillDiceBoard(char[][][] diceBoard, String[][] diceArray) {
        for (int rowNum = 0; rowNum < 5; rowNum++) {
            for (int colNum = 0; colNum < 5; colNum++) {
                for (int index = 0; index < 6; index++) {
                    // mixed up the row and col for the diceArray because I copied and pasted it wrong, but it should still work
                    diceBoard[rowNum][colNum][index] = diceArray[colNum][rowNum].charAt(index);
                }
            }
        }

        return diceBoard;
    }

    // this is also used interchangeably with shake the board
    public static char[][] newBoard(char[][][] diceBoard){
        char[][] boggleBoard = new char[5][5];
        for (int rowNum = 0; rowNum < 5; rowNum++) {
            for (int colNum = 0; colNum < 5; colNum++) {
                int index = random.nextInt(6);
                boggleBoard[rowNum][colNum] = diceBoard[rowNum][colNum][index];
                }
            }

        return boggleBoard;
    }

    public static ArrayList<String>  findWords(char[][] boggleBoard){
        ArrayList<String> wordList = new ArrayList<>();


        return wordList;
    }

    public static void printBoard(char[][] boggleBoard){
        System.out.println(Arrays.deepToString(boggleBoard).replace("], ", "]\n"));
    }

    public static void main(String[] args){
        String[][] diceArray = {{"AAAFRS", "AAEEEE", "AAFIRS" , "ADENNN", "AEEEEM"},{"AEEGMU" , "AEGMNN" , "AFIRSY" , "BJKQXZ" , "CCNSTW"} , {"CEIILT" , "CEILPT" , "CEIPST" , "DDLNOR" , "DHHLOR"} , {"DHHNOT" , "DHLNOR" , "EIIITT" , "EMOTTT" , "ENSSSU" }, {"FIPRSY" , "GORRVW" , "HIPRRY" , "NOOTUW" , "OOOTTU"}};
        char[][][] diceBoard = new char[5][5][6];
        fillDiceBoard(diceBoard, diceArray);


        char[][] boggleBoard  = newBoard(diceBoard);

        printBoard(boggleBoard);
        System.out.println("hii");

    }
}
