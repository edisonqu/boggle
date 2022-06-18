import org.w3c.dom.ls.LSOutput;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.*;


public class boggle {
    static Random random = new Random();
    static HashSet<String> wordList = new HashSet<>();
    static HashSet<String> prefixes = new HashSet<>();
    static boolean[][] visitedArray = new boolean[5][5];

    public static char[][][] fillDiceBoard(char[][][] diceBoard) {
        String[][] diceArray = {{"AAAFRS", "AAEEEE", "AAFIRS" , "ADENNN", "AEEEEM"},{"AEEGMU" , "AEGMNN" , "AFIRSY" , "BJKQXZ" , "CCNSTW"} , {"CEIILT" , "CEILPT" , "CEIPST" , "DDLNOR" , "DHHLOR"} , {"DHHNOT" , "DHLNOR" , "EIIITT" , "EMOTTT" , "ENSSSU" }, {"FIPRSY" , "GORRVW" , "HIPRRY" , "NOOTUW" , "OOOTTU"}};
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
         if(rowNum >= boggleBoard.length || rowNum < 0  || colNum < 0 || colNum >= boggleBoard[0].length){
            return false;
        }
        return true;
    }
    public static HashSet<String> dictionaryWords() throws FileNotFoundException {
        HashSet<String> dictionaryList = new HashSet<>();
        File file = new File("dictionary.txt");
        Scanner reader = new Scanner(file);
        while (reader.hasNextLine()) {
            String words = reader.nextLine();
            dictionaryList.add(words);
            String wordPrefix = "";
            for (int charIndex = 0; charIndex < words.length(); charIndex++) {
                wordPrefix += String.valueOf(words.charAt(charIndex));
                prefixes.add(wordPrefix);
            }

        }

        reader.close();
        return dictionaryList;
    }
    public static void findWords(char[][] boggleBoard, int rowNum, int colNum, HashSet<String> dictionaryWords, String letters) {
        if(visitedArray[rowNum][colNum]){
            return;
        }
        visitedArray[rowNum][colNum] = true;
        if(dictionaryWords.contains(letters) && letters.length() >=3){
            wordList.add(letters);
        }

        if (!prefixes.contains(letters)) {
            visitedArray[rowNum][colNum] = false;
            return;
        }

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) {
                    continue;
                }
                if (!isPassable(boggleBoard, rowNum + dx, colNum + dy)) {
                    continue;
                }
                String newLetters = letters + Character.toLowerCase(boggleBoard[rowNum+dx][colNum+dy]);
                findWords(boggleBoard, rowNum + dx, colNum + dy, dictionaryWords, newLetters);

            }
        }visitedArray[rowNum][colNum] = false;
    }
    public static void printBoard(char[][] boggleBoard){
        System.out.println(Arrays.deepToString(boggleBoard).replace("], ", "]\n"));
    }
    public static void play(){
        // if multi player
        //  if timer hits 0 or pass is pressed
        //      next turn
        //      reset timer
        //  if valid turn
        //      add points to the static variable player1score
        //      next turn
        //      reset timer


    }
    public static void main(String[] args) throws FileNotFoundException {
        char[][][] diceBoard = new char[5][5][6];
        fillDiceBoard(diceBoard);

        char[][] boggleBoard = newBoard(diceBoard);

        printBoard(boggleBoard);

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                findWords(boggleBoard, i,j, dictionaryWords(), Character.toLowerCase(boggleBoard[i][j])+"");
            }
        }

        System.out.println(wordList);
        System.out.println(wordList.size());

        System.out.println();

    }

}
