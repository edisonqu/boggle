import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.*;


public class boggle {
    static Random random = new Random();
    static HashSet<String> wordList = new HashSet<>();
    static HashSet<String> prefixes = new HashSet<>();

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
//    public static ArrayList<Character> findNeighbors(char[][] boggleBoard, int rowNum, int colNum){
//        ArrayList<Character> neighbors = new ArrayList<Character>();
//        for(int i = -1; i <= 1; i++) {
//            for (int j = -1; j <= 1; j++) {
//                if (i == 0 && j == 0) {
//                    continue;
//                }
//                if (isPassable(boggleBoard, rowNum + i, colNum + j)) {
//                    neighbors.add(boggleBoard[rowNum + i][rowNum + j]);
//                }
//            }
//        }
//        System.out.println(boggleBoard[rowNum][colNum]);
//        return neighbors;
//    }
    public static HashSet<String> dictionaryWords() throws FileNotFoundException {
        HashSet<String> dictionaryList = new HashSet<>();
        File file = new File("dictionary.txt");
        int max = 0;
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
    // public static String findWords(char[][] boggleBoard, int rowNum, int colNum, HashSet<String> dictionaryWords, String letters, ArrayList<Character> neighbors) {
    public static void findWords(char[][] boggleBoard, int rowNum, int colNum, HashSet<String> dictionaryWords, String letters) {
        if(dictionaryWords.contains(letters)){
            wordList.add(letters);
        }


        if (!prefixes.contains(letters) && !Objects.equals(letters, "")){
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
                letters += boggleBoard[rowNum+dx][colNum+dy];
                findWords(boggleBoard, rowNum + dx, colNum + dy, dictionaryWords, letters);
            }
        }

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
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                findWords(boggleBoard, 0,0, dictionaryWords(), "");
            }
        }
        System.out.println(wordList);
    }
}
