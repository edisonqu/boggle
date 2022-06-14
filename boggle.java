import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.*;

public class boggle {
    static Random random = new Random();
    static ArrayList<String> wordList = new ArrayList<>();

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
    public static boolean isPassable(char[][] boggleBoard, int rowNum, int colNum){
         if(rowNum >= boggleBoard[0].length || rowNum == -1 || colNum == -1 || colNum >= boggleBoard[0].length){
            return false;
        }
        return true;
    }
    public static ArrayList<Character> findNeighbors(char[][] boggleBoard, int rowNum, int colNum){
        ArrayList<Character> neighbors = new ArrayList<Character>();
        for(int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) {
                    continue;
                }
                if (isPassable(boggleBoard, rowNum + i, colNum + j)) {
                    neighbors.add(boggleBoard[rowNum + i][rowNum + j]);
                }
            }
        }
        System.out.println(boggleBoard[rowNum][colNum]);
        return neighbors;
    }
    public static HashSet<String> dictionaryWords() throws FileNotFoundException {
        HashSet<String> dictionaryList = new HashSet<>();
        File file = new File("dictionary.txt");
        Scanner reader = new Scanner(file);
        while (reader.hasNextLine()) {
            String words = reader.nextLine();
            dictionaryList.add(words);
        }
        reader.close();
        return dictionaryList;
    }
    public static int DFS(char[][] boggleBoard, int rowNum, int colNum) {
        ArrayList<Character> neighbors = findNeighbors(boggleBoard,rowNum,colNum);
        System.out.println(neighbors);

        return -1;
    }
    public static ArrayList<String>  findWords(char[][] boggleBoard){

        return wordList;
    }

    public static void printBoard(char[][] boggleBoard){
        System.out.println(Arrays.deepToString(boggleBoard).replace("], ", "]\n"));
    }

    public static void main(String[] args) throws FileNotFoundException {
        String[][] diceArray = {{"AAAFRS", "AAEEEE", "AAFIRS" , "ADENNN", "AEEEEM"},{"AEEGMU" , "AEGMNN" , "AFIRSY" , "BJKQXZ" , "CCNSTW"} , {"CEIILT" , "CEILPT" , "CEIPST" , "DDLNOR" , "DHHLOR"} , {"DHHNOT" , "DHLNOR" , "EIIITT" , "EMOTTT" , "ENSSSU" }, {"FIPRSY" , "GORRVW" , "HIPRRY" , "NOOTUW" , "OOOTTU"}};
        char[][][] diceBoard = new char[5][5][6];
        fillDiceBoard(diceBoard, diceArray);
        char[][] boggleBoard = newBoard(diceBoard);
        printBoard(boggleBoard);
        DFS(boggleBoard, 0,0);

    }
}
