
/*
 * Sophie Live Tan, Farina Sorori, Edison Qu, Brianne Woon
 * June 20, 2022
 * A Boggle application that displays a 5x5 board of random letters and allows the user to enter words
 * with 3 or more letters that are found on the board depending on the difficulty. The application
 * reports if the word entered by the user is indeed on the board. If not, the program should report that it
 * was not found. There will be a scoreboard that will accommodate two players or a computer to see who wins
 * when one of them reaches a certain number (tournament score). When a tournament score is reached, the
 * user who won will be congratulated.
 */

import java.util.*;
import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

import org.w3c.dom.ls.LSOutput;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.*;

public class Boggle extends JFrame implements ActionListener {

    // variable declarations

    // components for the GUI
    JPanel menu, instructionsPanel, optionsPanel, titlePanel, modePanel, difficultyPanel, tournamentScorePanel, backToMenuPanel,
            gamePanel, boardTitlePanel, boardPanel, elementDisplay, turnPanel, timerPanel, wordPanel, enterAndSearchPanel, outputPanel, buttonPanel, shakeUpBoardPanel, congratulationsPanel;

    JLabel boggleTitle, playerOne, playerOneScore, playerTwo, playerTwoScore, timerLabel, enterWordLabel, outputMessage, errorLabel, welcome,
            mode, tournamentScore, difficulty, optionsTitle, modeOption, difficultyOption, tournamentScoreOption, instructionsTitle, congratulations, congratulations2, playAgain;

    JButton okayButton, pass, pause, restart, exit, play, instructions, options, backMenu, singleBtn, multiBtn,
            easyBtn, mediumBtn, hardBtn, enterBtn, backToMenu, shakeUpBoard, playAgainBtn, mainMenu;

    JLabel[][] boggleBoardLabel;
    BoxLayout menuLayout, instructionsLayout, optionsLayout, congratulationsLayout;
    FlowLayout modeLayout, difficultyLayout, tournamentScoreLayout;
    JTextArea instructionsInformation;
    JTextField tournamentScoreField, enterWordTextField;

    // class variables for the settings in the main menu
    static String playerMode = "Single";
    static int tournamentScoreInput = 10;
    static String difficultyInput = "Easy";
    static int difficultyNumber;

    // variables for the timer
    private static int currentSeconds = 15;
    private Timer timer;
    private static int colourInterval = 1;
    private Timer colourTimer;

    // class variables for the back end
    static Random random = new Random();
    static HashSet<String> wordList = new HashSet<>();
    static HashSet<String> prefixes = new HashSet<>();
    static boolean[][] visitedArray = new boolean[5][5];
    static char[][] boggleBoard;

    // initialize the variables for the tournament and changing turns
    static String word;
    static int turn = 1;
    static int passCounter;
    static int playerOneScoreNumber;
    static int playerTwoScoreNumber;
    static String winner;


    // fill in the Diceboard with the respective Dice on each row and col, randomizes the dice position
    public static char[][][] fillDiceBoard(char[][][] diceBoard) {
        String[][] diceArray = {{"AAAFRS", "AAEEEE", "AAFIRS", "ADENNN", "AEEEEM"}, {"AEEGMU", "AEGMNN", "AFIRSY", "BJKQXZ", "CCNSTW"}, {"CEIILT", "CEILPT", "CEIPST", "DDLNOR", "DHHLOR"}, {"DHHNOT", "DHLNOR", "EIIITT", "EMOTTT", "ENSSSU"}, {"FIPRSY", "GORRVW", "HIPRRY", "NOOTUW", "OOOTTU"}};
        for (int rowNum = 0; rowNum < 5; rowNum++) {
            for (int colNum = 0; colNum < 5; colNum++) {
                for (int index = 0; index < 6; index++) {
                    diceBoard[rowNum][colNum][index] = diceArray[colNum][rowNum].charAt(index);
                }
            }
        }
        return diceBoard;
    }

    // this is also used interchangeably with shake the board
    public static char[][] newBoard(char[][][] diceBoard) {
        char[][] boggleBoard = new char[5][5];
        for (int rowNum = 0; rowNum < 5; rowNum++) {
            for (int colNum = 0; colNum < 5; colNum++) {
                int index = random.nextInt(6);
                // randomly choose an integer from 0,6 so you can choose a random index for the dice
                boggleBoard[rowNum][colNum] = diceBoard[rowNum][colNum][index];
            }
        }

        return boggleBoard;
    }

    public static boolean isPassable(char[][] boggleBoard, int rowNum, int colNum) {
        if (rowNum >= boggleBoard.length || rowNum < 0 || colNum < 0 || colNum >= boggleBoard[0].length) {
            return false;
        }
        return true;
    }


    public static HashSet<String> dictionaryWords() throws FileNotFoundException {
        // create a hashset
        HashSet<String> dictionaryList = new HashSet<>();
        // file reader that creates something from the dictionary.txt
        File file = new File("dictionary.txt");
        // scanner read the file
        Scanner reader = new Scanner(file);
        while (reader.hasNextLine()) {
            // while the reader has a next line
            String words = reader.nextLine();
            // add the words
            dictionaryList.add(words);
            String wordPrefix = "";

            for (int charIndex = 0; charIndex < words.length(); charIndex++) {
                // add each prefix of  the word in order to initialize DFS on each character of the word
                wordPrefix += String.valueOf(words.charAt(charIndex));
                prefixes.add(wordPrefix);
            }
        }
        reader.close();
        return dictionaryList;
    }

    public static void findWords(char[][] boggleBoard, int rowNum, int colNum, HashSet<String> dictionaryWords, String letters) {
        // if the character is already vistied
        if (visitedArray[rowNum][colNum]) {
            return;
        }
        // make the character visited
        visitedArray[rowNum][colNum] = true;

        // add all teh words taht are more than 3 characters
        if (dictionaryWords.contains(letters) && letters.length() >= 3) {
            // add it to wordList
            wordList.add(letters);
        }

        // if hte prefixes contains the letter
        if (!prefixes.contains(letters)) {
            visitedArray[rowNum][colNum] = false;
            return;
        }


        // find all the neighbours, in the left, right, top, bottom locations
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                // if its the current location, skip
                if (dx == 0 && dy == 0) {
                    continue;
                }
                // if it isn't passible, continue
                if (!isPassable(boggleBoard, rowNum + dx, colNum + dy)) {
                    continue;
                }

                // create a new variable that adds it to the recursion method
                String newLetters = letters + Character.toLowerCase(boggleBoard[rowNum + dx][colNum + dy]);

                // recursive call
                findWords(boggleBoard, rowNum + dx, colNum + dy, dictionaryWords, newLetters);

            }

        }
        // make the element visited
        visitedArray[rowNum][colNum] = false;
    }

    public Boggle() throws IOException { // constructor
        // setting title and a fixed size for the frame
        setTitle("Boggle");
        setSize(600, 700);
        setResizable(false);

        // initializing the menu panel
        menu = new JPanel();
        menuLayout = new BoxLayout(menu, BoxLayout.Y_AXIS);
        menu.setLayout(menuLayout);
        menu.setBackground(new Color(0, 204, 204));

        // initializing and editing the "Welcome to Boggle!" label
        welcome = new JLabel("Welcome to Boggle!");
        welcome.setFont(new Font("Century Gothic", Font.BOLD, 50));
        welcome.setMinimumSize(new Dimension(100, 400));
        welcome.setAlignmentX(Component.CENTER_ALIGNMENT);
        welcome.setForeground(new java.awt.Color(0, 29, 219)); // may want to change colour

        // initializing and editing the play button
        play = new JButton("      Play      ");
        play.setFont(new Font("Century Gothic", Font.BOLD, 20)); // maybe want a more rounded font
        play.setAlignmentX(Component.CENTER_ALIGNMENT);
        play.addActionListener(this); // adds the frame class as an action listener to the button

        // initializing and editing the instructions button
        instructions = new JButton("Instructions");
        instructions.setFont(new Font("Century Gothic", Font.BOLD, 20)); // maybe want a more rounded font
        instructions.setAlignmentX(Component.CENTER_ALIGNMENT);
        instructions.addActionListener(this); // adds the frame class as an action listener to the button

        // initializing and editing the options button
        options = new JButton("   Options   ");
        options.setFont(new Font("Century Gothic", Font.BOLD, 20)); // maybe want a more rounded font
        options.setAlignmentX(Component.CENTER_ALIGNMENT);
        options.addActionListener(this); // adds the frame class as an action listener to the button

        // setting the default modes on the main menu
        mode = new JLabel("Mode: " + playerMode);
        mode.setAlignmentX(Component.CENTER_ALIGNMENT);
        mode.setFont(new Font("Century Gothic", Font.PLAIN, 15));
        difficulty = new JLabel("Difficulty: " + difficultyInput);
        difficulty.setAlignmentX(Component.CENTER_ALIGNMENT);
        difficulty.setFont(new Font("Century Gothic", Font.PLAIN, 15));
        tournamentScore = new JLabel("Tournament Score: " + tournamentScoreInput);
        tournamentScore.setAlignmentX(Component.CENTER_ALIGNMENT);
        tournamentScore.setFont(new Font("Century Gothic", Font.PLAIN, 15));

        // adding components to the menu panel and spacing them out
        menu.add(Box.createRigidArea(new Dimension(0, 70)));
        menu.add(welcome);
        menu.add(Box.createRigidArea(new Dimension(0, 70)));
        menu.add(play);
        menu.add(Box.createRigidArea(new Dimension(0, 5)));
        menu.add(instructions);
        menu.add(Box.createRigidArea(new Dimension(0, 5)));
        menu.add(options);
        menu.add(Box.createRigidArea(new Dimension(0, 5)));
        menu.add(mode);
        menu.add(Box.createRigidArea(new Dimension(0, 5)));
        menu.add(tournamentScore);
        menu.add(Box.createRigidArea(new Dimension(0, 5)));
        menu.add(difficulty);

        // initializing the timer with the method createTimer()
        createTimer();

        // adding main menu and set visible to true
        add(menu);
        setVisible(true);
    }

    public static String AIChooseWord() {
        // initialize the AI word
        String AIword = "";
        // since it is a hashset, it is a .iterator method in order to get all the elements

        Iterator value = wordList.iterator();
        // while there is another word in the wordList

        while (value.hasNext()) {

            String wordInList = (String) value.next();
            // if its easy, find a word that is easy, medium or hard that is dependent on the length

            if (wordInList.length() >= 3 && difficultyInput.equals("Easy")) {
                // make the AI word in the wordInList dependening on the difficulty
                AIword = wordInList;
                break;
            } else if (wordInList.length() >= 4 && difficultyInput.equals("Medium")) {
                AIword = wordInList;
                break;
            } else if (wordInList.length() >= 5 && difficultyInput.equals("Hard")) {
                AIword = wordInList;
                break;
            }
        }
        wordList.remove(AIword);
        return (AIword);
    }

    ;


    // method to count the points for each word
    public int points(String word) {
        if (word.length() == 3 || word.length() == 4) { // if length of the word is 3 or 4, the word is 1 point
            return 1;
        } else if (word.length() == 5) { // if length of the word is 5, the word is 2 points
            return 2;
        } else if (word.length() == 6) { // if the length of the word is 6, the word is 3 points
            return 3;
        } else if (word.length() == 7) { // if the length of the word is 7, the word is 5 points
            return 5;
        } else if (word.length() >= 8) { // if the length of the word is 8 or greater, the word is 11 points
            return 11;
        }
        return 0;
    }

    // method to initialize timer
    public void createTimer() {
        timer = new Timer(1000, e -> { // initializing Timer object called "timer", with 1 second delay
            currentSeconds--; // decrement
            if (currentSeconds < 0) {
                currentSeconds = 15; // setting timer to 15 when timer goes out of bounds

                // if the turn is on player one, change the labels to be red
                if (turn == 1) {
                    if (playerMode.equals("Single")) { // if on single player mode, the AI will choose a word when turn = 1
                        currentSeconds = 0;
                        enterAndSearchPanel.setVisible(false);
                        word = AIChooseWord(); // calling the choose word function for the ai to randomly select a word from the list

                        // outputting AI result
                        outputMessage.setText("Result: AI enters \"" + word + "\". +" + points(word) + " points!");
                        outputPanel.add(outputMessage);

                        // adding respective points for the AI
                        playerTwoScoreNumber += points(word);
                        playerTwoScore.setText(String.valueOf(playerTwoScoreNumber));

                        // if the other player's score reaches the tournament score, brings up congrats panel with player one
                        if (playerOneScoreNumber >= tournamentScoreInput) {
                            winner = "Player One";
                            playerOneScoreNumber = 0;
                            playerTwoScoreNumber = 0;
                            showCongrats();
                        }

                        // if the winner is the AI, make it the winner and reset
                        if (playerTwoScoreNumber >= tournamentScoreInput) {
                            winner = "AI";
                            playerOneScoreNumber = 0;
                            playerTwoScoreNumber = 0;
                            showCongrats();
                            turn = 0;
                        }

                    }
                    if (playerTwoScoreNumber < tournamentScoreInput) {
                        turn = 2;
                    }

                    // changing label colours, indicating who's turn it is
                    playerTwo.setForeground(Color.RED);
                    playerOne.setForeground(Color.BLACK);

                } else { // if on turn 2
                    // if the player mode is single and the turn is on turn 2, the panel where user enters words should reappear after disappearing for the AI
                    if (playerMode.equals("Single")) {
                        enterAndSearchPanel.setVisible(true);
                    }

                    turn = 1; // changing the turn back to 1 and adjusting label colour
                    playerOne.setForeground(Color.RED);
                    playerTwo.setForeground(Color.BLACK);
                }
            }
            // if the timer is at less than 10 seconds, displays the seconds
            if (currentSeconds < 10) {
                timerLabel.setText("Timer: 00:0" + currentSeconds); // setting timer label if timer is below 10
                // changing timer text label to red when the timer reaches 3 seconds
                if (currentSeconds <= 3) {
                    timerLabel.setForeground(Color.RED);
                }
                // if the timer is at greater than 10 seconds, displays the seconds
            } else if (currentSeconds >= 10) {
                timerLabel.setForeground(Color.BLACK); // setting text back to black
                timerLabel.setText("Timer: 00:" + currentSeconds); // setting timer label if timer is above 10
            }
        });
    }

    // method for the action listeners
    public void actionPerformed(ActionEvent event) {
        // storing the name of the button pressed in an object
        Object component = event.getSource();
        String command = event.getActionCommand();

        // if play is pressed, sets menu to disappear and creates a new boggle board, finding all possible words for the board and changing the labels
        if (component.equals(play)) {
            menu.setVisible(false);

            // calling functions to create the board, from the algorithm
            char[][][] diceBoard = new char[5][5][6];
            fillDiceBoard(diceBoard);
            boggleBoard = newBoard(diceBoard);

            // finds all possible words given the boggle board
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) {
                    try {
                        findWords(boggleBoard, i, j, dictionaryWords(), Character.toLowerCase(boggleBoard[i][j]) + "");
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }

            // brings up the GUI for the game
            showGame();
            currentSeconds = 15;
            timer.start();
        } else if (component.equals(instructions)) {
            // pops up the instructions file

            menu.setVisible(false); // closes the menu frame and opens the instructions frame

            // creates the instructions panel and sets layout
            instructionsPanel = new JPanel();
            instructionsLayout = new BoxLayout(instructionsPanel, BoxLayout.Y_AXIS);
            instructionsPanel.setLayout(instructionsLayout);
            instructionsPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 50, 20));

            // creates the instruction title for the panel
            instructionsTitle = new JLabel("Instructions");
            instructionsTitle.setFont(new Font("Century Gothic", Font.BOLD, 42));
            instructionsTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

            // shows the instructions in a text area that wraps text, no need to split up the lines
            instructionsInformation = new JTextArea("How to play Boggle:\n"
                    + "1. A 5 by 5 board of dice will be shaken up to show 25 random letters.\n"
                    + "2. The first player will enter a word found on the board (the longer the word the better!).\n"
                    + "3. The next player will enter a different word found on the board.\n"
                    + "4. Steps 2 and 3 will continue, alternating between players, until both players"
                    + " can no longer find a word.\n"
                    + "5. If both players pass, you have the option to shake up the board.\n"
                    + "6. The length of the word entered determines the number of points you earn.\n"
                    + "7. First player to reach the tournament score wins the game.\n"
                    + "\n"
                    + "Setting Features:\n"
                    + "1. Mode: Singleplayer (Player vs. AI) vs. Multiplayer (Player vs. Player)\n"
                    + "2. Difficulty: Easy (minimum 3 letters), Medium (minimum 4 letters), Hard "
                    + "(minimum 5 letters)\n"
                    + "3. Tournament Scores: You can set the score that must be met in order to win the game.");
            instructionsInformation.setAlignmentX(Component.CENTER_ALIGNMENT);
            instructionsInformation.setFont(new Font("Century Gothic", Font.PLAIN, 13));
            instructionsInformation.setOpaque(false);
            instructionsInformation.setLineWrap(true);
            instructionsInformation.setWrapStyleWord(true);

            // button to return to the main menu
            backMenu = new JButton("Back to Menu");
            backMenu.setFont(new Font("Century Gothic", Font.PLAIN, 15));
            backMenu.setAlignmentX(Component.CENTER_ALIGNMENT);
            backMenu.addActionListener(this);

            // adding the title, information and back to menu button to the panel with spacing
            instructionsPanel.add(Box.createRigidArea(new Dimension(0, 30)));
            instructionsPanel.add(instructionsTitle);
            instructionsPanel.add(Box.createRigidArea(new Dimension(0, 30)));
            instructionsPanel.add(instructionsInformation);
            instructionsPanel.add(backMenu);

            // adding the instructions panel to the frame and setting visible to true
            add(instructionsPanel);
            instructionsPanel.setVisible(true);
            setVisible(true);
        }

        // if components is clicked
        else if (component.equals(options)) {
            menu.setVisible(false);

            // options panel
            optionsPanel = new JPanel();
            optionsLayout = new BoxLayout(optionsPanel, BoxLayout.Y_AXIS);
            optionsPanel.setLayout(optionsLayout);

            // title page and panel
            titlePanel = new JPanel();
            optionsPanel.add(Box.createRigidArea(new Dimension(0, 60)));
            optionsTitle = new JLabel("Options");
            optionsTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
            optionsTitle.setFont(new Font("Century Gothic", Font.BOLD, 40));
            titlePanel.add(optionsTitle);

            // mode panel and layout
            modePanel = new JPanel();
            modeLayout = new FlowLayout();
            modePanel.setLayout(modeLayout);

            // label for mode and the player mode
            modeOption = new JLabel("Mode: ");
            modeOption.setFont(new Font("Century Gothic", Font.PLAIN, 18));
            singleBtn = new JButton("Single");
            singleBtn.setFont(new Font("Century Gothic", Font.PLAIN, 15));

            // if the playermode is single, sets the background to be light gray to show it's selected
            if (playerMode.equals("Single")) {
                singleBtn.setBackground(Color.LIGHT_GRAY);
            }
            singleBtn.setContentAreaFilled(false);
            singleBtn.setOpaque(true);
            singleBtn.addActionListener(this);

            // if the playermode is multi, sets the background to be light gray for the multi button
            multiBtn = new JButton("Multi");
            multiBtn.addActionListener(this);
            multiBtn.setFont(new Font("Century Gothic", Font.PLAIN, 15));
            if (playerMode.equals("Multi")) {
                multiBtn.setBackground(Color.LIGHT_GRAY);
                multiBtn.setContentAreaFilled(false);
                multiBtn.setOpaque(true);
            }

            // add the buttons to the mode Panel
            modePanel.add(modeOption);
            modePanel.add(singleBtn);
            modePanel.add(multiBtn);

            // add the difficulty panels
            difficultyPanel = new JPanel();
            difficultyLayout = new FlowLayout();
            difficultyPanel.setLayout(difficultyLayout);

            // make the difficulty buttons
            difficultyOption = new JLabel("Difficulty: ");
            difficultyOption.setFont(new Font("Century Gothic", Font.PLAIN, 18));
            easyBtn = new JButton("Easy");


            // check to see if the difficulty is easy, hard or medium
            if (difficultyInput.equals("Easy")) {
                easyBtn.setBackground(Color.LIGHT_GRAY);
                easyBtn.setContentAreaFilled(false);
                easyBtn.setOpaque(true);
            }
            easyBtn.setFont(new Font("Century Gothic", Font.PLAIN, 15));
            easyBtn.addActionListener(this);

            // check to see if the difficulty is easy, hard or medium
            mediumBtn = new JButton("Medium");
            mediumBtn.addActionListener(this);
            mediumBtn.setFont(new Font("Century Gothic", Font.PLAIN, 15));
            if (difficultyInput.equals("Medium")) {
                mediumBtn.setBackground(Color.LIGHT_GRAY);
                mediumBtn.setContentAreaFilled(false);
                mediumBtn.setOpaque(true);
            }

            // check to see if the difficulty is easy, hard or medium
            hardBtn = new JButton("Hard");
            hardBtn.addActionListener(this);
            hardBtn.setFont(new Font("Century Gothic", Font.PLAIN, 15));

            if (difficultyInput.equals("Hard")) {
                hardBtn.setBackground(Color.LIGHT_GRAY);
                hardBtn.setContentAreaFilled(false);
                hardBtn.setOpaque(true);
            }

            // adding all of the buttons and labels to the difficulty panel
            difficultyPanel.add(difficultyOption);
            difficultyPanel.add(easyBtn);
            difficultyPanel.add(mediumBtn);
            difficultyPanel.add(hardBtn);

            // add the tournament score panel and layout
            tournamentScorePanel = new JPanel();
            tournamentScoreLayout = new FlowLayout();
            tournamentScorePanel.setLayout(tournamentScoreLayout);

            // add the tournament score
            tournamentScoreOption = new JLabel("Tournament Score: ");
            tournamentScoreOption.setFont(new Font("Century Gothic", Font.PLAIN, 18));
            tournamentScoreField = new JTextField(10);
            enterBtn = new JButton("Enter");
            enterBtn.addActionListener(this);
            enterBtn.setFont(new Font("Century Gothic", Font.PLAIN, 15));

            // add the tournament score panel
            tournamentScorePanel.add(tournamentScoreOption);
            tournamentScorePanel.add(tournamentScoreField);
            tournamentScorePanel.add(enterBtn);

            // go back to the menu panel
            backToMenuPanel = new JPanel();
            backToMenu = new JButton("Back To Menu");
            backToMenu.setFont(new Font("Century Gothic", Font.PLAIN, 18));
            backToMenu.addActionListener(this);
            backToMenuPanel.add(backToMenu);

            // options panel title, mode, difficulty, tournament, and go back to menu
            optionsPanel.add(titlePanel);
            optionsPanel.add(modePanel);
            optionsPanel.add(difficultyPanel);
            optionsPanel.add(tournamentScorePanel);
            optionsPanel.add(backToMenuPanel);

            add(optionsPanel);

            optionsPanel.setVisible(true);
        }

        // go back to the backMenu from the instructions panel
        if (component.equals(backMenu)) {
            instructionsPanel.setVisible(false);
            menu.setVisible(true);
        }

        // go back to the main menu from the options panel
        // notice the change form backMenu & back To Menu
        if (component.equals(backToMenu)) {
            optionsPanel.setVisible(false);
            menu.setVisible(true);
        }

        // if single button is pressed, changes the background to light gray to show it is selected
        if (component.equals(singleBtn)) {
            playerMode = "Single";
            multiBtn.setBackground(null);
            singleBtn.setBackground(Color.LIGHT_GRAY);
            singleBtn.setContentAreaFilled(false);
            singleBtn.setOpaque(true);
            mode.setText("Mode: " + playerMode);

            // if multi button is pressed, changes the background to light gray to show it is selected
        } else if (component.equals(multiBtn)) {
            playerMode = "Multi";
            singleBtn.setBackground(null);
            multiBtn.setBackground(Color.LIGHT_GRAY);
            multiBtn.setContentAreaFilled(false);
            multiBtn.setOpaque(true);
            mode.setText("Mode: " + playerMode);
        }

        // if easy button is pressed, changes the background to light gray to show it is selected
        if (component.equals(easyBtn)) {
            difficultyInput = "Easy";
            difficultyNumber = 3;
            // removes highlight for the other two options
            hardBtn.setBackground(null);
            mediumBtn.setBackground(null);

            easyBtn.setBackground(Color.LIGHT_GRAY);
            easyBtn.setContentAreaFilled(false);
            easyBtn.setOpaque(true);
            difficulty.setText("Difficulty: " + difficultyInput);

            // if medium button is pressed, changes the background to light gray to show it is selected
        } else if (component.equals(mediumBtn)) {
            difficultyInput = "Medium";
            difficultyNumber = 4;

            // removes highlight for the other two options
            easyBtn.setBackground(null);
            hardBtn.setBackground(null);

            mediumBtn.setBackground(Color.LIGHT_GRAY);
            mediumBtn.setContentAreaFilled(false);
            mediumBtn.setOpaque(true);
            difficulty.setText("Difficulty: " + difficultyInput);

            // if hard button is pressed, changes the background to light gray to show it is selected
        } else if (component.equals(hardBtn)) {
            difficultyInput = "Hard";
            difficultyNumber = 5;

            // removes highlight for the other two options
            easyBtn.setBackground(null);
            mediumBtn.setBackground(null);

            hardBtn.setBackground(Color.LIGHT_GRAY);
            hardBtn.setContentAreaFilled(false);
            hardBtn.setOpaque(true);
            difficulty.setText("Difficulty: " + difficultyInput);

        }

        if (component.equals(enterBtn)) {
            try {
                // enter button make sure to see if the number is a valid number or not
                tournamentScoreInput = Integer.parseInt(tournamentScoreField.getText());
                tournamentScore.setText("Tournament Score: " + tournamentScoreInput);
            } catch (NumberFormatException e) {
                errorLabel = new JLabel("Number not entered. Please enter a valid number.");
                tournamentScorePanel.add(errorLabel);
                setVisible(true);
                tournamentScoreInput = 50;
                // put the tournament score and add it to the menu
                tournamentScore.setText("Tournament Score: " + tournamentScoreInput);
                menu.add(tournamentScore);
            }
        }
        // sets the game panel to be invisible and makes the meny visible again when exit is pressed
        if (component.equals(exit)) {
            gamePanel.setVisible(false); // hiding game panel
            passCounter = 0;
            timer.stop(); // stopping timer
            menu.setVisible(true);
        }
        // if pause is pressed, stops the timer and sets label to resume
        if (component.equals(pause)) {
            timer.stop();
            pause.setText("Resume");
        }
        // if resume is pressed, starts the timer off again
        if (command.equals("Resume")) {
            timer.start(); // starting timer where it left off
            pause.setText("Pause"); // setting label to pause

        }
        // if pass is pressed, changes turns and adds to the pass counter
        if (component.equals(pass)) {
            // setting timer to -1 seconds to restart it and switch turns in the createTimer() method
            currentSeconds = -1;

            passCounter++;
            if (passCounter == 2) {
                // shake up the board if they are passed more than twice
                shakeUpBoardPanel = new JPanel();
                shakeUpBoard = new JButton("Shake Up The Board!");
                shakeUpBoard.setFont((new Font("Century Gothic", Font.PLAIN, 15)));
                shakeUpBoard.setAlignmentX(Component.CENTER_ALIGNMENT);
                shakeUpBoard.addActionListener(this);

                shakeUpBoardPanel.add(shakeUpBoard);
                gamePanel.add(shakeUpBoardPanel);

                // shake up board
                shakeUpBoardPanel.setVisible(true);
            }

        }
        // restart button, sets scores to be 0 and pass counter to be zero
        if (component.equals(restart)) {
            passCounter = 0;
            playerOneScoreNumber = 0;
            playerTwoScoreNumber = 0;
            playerOneScore.setText(String.valueOf(playerOneScoreNumber));
            playerTwoScore.setText(String.valueOf(playerTwoScoreNumber));
            outputMessage.setText("Game restarted.");
            turn = 2;
            passCounter = 0;

            // diceBoard with a triple array
            char[][][] diceBoard = new char[5][5][6];
            fillDiceBoard(diceBoard);

            // make the boggleBoard into dice board
            boggleBoard = newBoard(diceBoard);

            // change the labels for the boggle board
            for (int r = 0; r < boggleBoard.length; r++) {
                for (int c = 0; c < boggleBoard[0].length; c++) {
                    //make it a string
                    boggleBoardLabel[r][c].setText(boggleBoard[r][c] + "");
                }
            }
            // finds all the words for a given boggle board
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) {
                    try {
                        findWords(boggleBoard, i, j, dictionaryWords(), Character.toLowerCase(boggleBoard[i][j]) + "");
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }

            // setting the timer to -1 seconds to restart and switch turns in the createTimer() method
            currentSeconds = -1;
        }
        // if the okay button is pressed, switches turns and adds points
        if (component.equals(okayButton)) {

            if (playerMode.equals("Multi")) { // if the mode is in multi, two players are playing
                // finds the word in the textfield
                word = enterWordTextField.getText();
                String wordLowercase = word.toLowerCase();

                // if the word is in the dictionary and it is longer than the difficulty set
                if (wordList.contains(wordLowercase) && word.length() >= difficultyNumber) {
                    outputMessage.setText("Result: \"" + word + "\" is valid. +" + points(word) + " points!");
                    outputPanel.add(outputMessage);
                    wordList.remove(wordLowercase);
                    // add and change score

                    // setting the timer to -1 seconds to restart and switch turns in the createTimer() method
                    currentSeconds = -1;

                    // if the turn is at 1, adds points to player one's sscores
                    if (turn == 1) {
                        playerOneScoreNumber += points(word);
                        playerOneScore.setText(String.valueOf(playerOneScoreNumber));
                        if (playerOneScoreNumber >= tournamentScoreInput && playerMode.equals("Multi")) {
                            winner = "Player One";
                            showCongrats();
                        }
                    } else { // if the turn is at 2, adds points to player two's scores
                        playerTwoScoreNumber += points(word);
                        playerTwoScore.setText(String.valueOf(playerTwoScoreNumber));
                        if (playerTwoScoreNumber >= tournamentScoreInput) {
                            winner = "Player Two";
                            showCongrats();
                        }
                    }

                } else {
                    outputMessage.setText("Result: Invalid word entered. Try again!");
                }

                enterWordTextField.setText(null);
            } else {
                // checks if its a word, if it is a word, change turns. if not then stay at current turn, if turn is changed, ask ai to find a word
                word = enterWordTextField.getText();
                String wordLowercase = word.toLowerCase();

                // if word entered is in the dictionary and is longer than the difficulty, shows that it is valid and adds the points
                if (wordList.contains(wordLowercase) && word.length() >= difficultyNumber) {
                    outputMessage.setText("Result: \"" + word + "\" is valid. +" + points(word) + " points!");
                    outputPanel.add(outputMessage);
                    wordList.remove(wordLowercase);
                    // add and change score

                    // setting the timer to -1 seconds to restart and switch turns in the createTimer() method
                    currentSeconds = -1;

                    playerOneScoreNumber += points(word);
                    playerOneScore.setText(String.valueOf(playerOneScoreNumber));

                } else {
                    outputMessage.setText("Result: Invalid word entered. Try again!");
                }
                // sets the text field to be empty
                enterWordTextField.setText(null);
            }


        }

        // if shake up the board is pressed, makes the shake up board panel visible and creates a new boggle boeard
        if (component.equals(shakeUpBoard)) {
            shakeUpBoardPanel.setVisible(false);
            passCounter = 0;

            char[][][] diceBoard = new char[5][5][6];
            fillDiceBoard(diceBoard);

            boggleBoard = newBoard(diceBoard);

            // change the labels for the boggle board
            for (int r = 0; r < boggleBoard.length; r++) {
                for (int c = 0; c < boggleBoard[0].length; c++) {
                    // boggleBoardLabel[r][c] = new JLabel(boggleBoard[r][c] + "", SwingConstants.CENTER); // centering labels
                    boggleBoardLabel[r][c].setText(boggleBoard[r][c] + "");
                }
            }

            timer.stop(); // stops the timer so that two do not run at the same time
            // finds the words for the new boggle board
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) {
                    try {
                        findWords(boggleBoard, i, j, dictionaryWords(), Character.toLowerCase(boggleBoard[i][j]) + "");
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
            timer.start(); // starting the timer to continue where it left off
        }
        // if main menu is selected, closes the congratulations panel and sets the meny to be visible
        // sets pass counter and scores to be zero to reset the game
        if (component.equals(mainMenu)) {
            congratulationsPanel.setVisible(false);
            timer.stop(); // stopping timer
            colourTimer.stop(); // stopping colour timer
            passCounter = 0;
            playerOneScoreNumber = 0;
            playerTwoScoreNumber = 0;
            menu.setVisible(true);
        }
    }

    // method that creates game board ands sets it visible
    public void showGame() {
        // initializing the game panel with a box layout and empty border
        gamePanel = new JPanel();
        gamePanel.setLayout(new BoxLayout(gamePanel, BoxLayout.Y_AXIS));
        gamePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // set an empty border to the panel

        boardTitlePanel = new JPanel(); // initializing panel for title of the board
        boggleTitle = new JLabel("   Boggle");
        boggleTitle.setFont(new Font("Century Gothic", Font.BOLD, 30)); // setting font and size
        boardTitlePanel.add(boggleTitle); // adding components
        gamePanel.add(boardTitlePanel);

        // panel for the turns, scores, and timer
        elementDisplay = new JPanel(); // initializing main panel
        elementDisplay.setLayout(new FlowLayout(FlowLayout.LEFT, 60, 5)); // setting flow layout and gaps between sub panels

        turnPanel = new JPanel(); // initializing subpanel called turnPanel
        turnPanel.setLayout(new GridLayout(2, 2, 5, 0)); // setting a grid layout with gaps

        // initializing player labels and centering them to the subpanel
        playerOne = new JLabel("Player One", SwingConstants.CENTER);
        playerOne.setForeground(Color.RED); // setting player one default to be red (indicate the first turn)
        playerTwo = new JLabel("Player Two", SwingConstants.CENTER);

        if (playerMode.equals("Single")) {
            playerTwo.setText("AI"); // setting player two to AI if single mode is selected
        }

        // initializing player scores and centering them to the subpanel
        playerOneScore = new JLabel("0", SwingConstants.CENTER);
        playerTwoScore = new JLabel("0", SwingConstants.CENTER);

        // setting black borders, fonts and sizes
        playerOne.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        playerTwo.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        playerOneScore.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        playerTwoScore.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        playerOne.setFont(new Font("Century Gothic", Font.PLAIN, 17));
        playerTwo.setFont(new Font("Century Gothic", Font.PLAIN, 17));
        playerOneScore.setFont(new Font("Century Gothic", Font.PLAIN, 17));
        playerTwoScore.setFont(new Font("Century Gothic", Font.PLAIN, 17));

        // adding components to subpanel
        turnPanel.add(playerOne);
        turnPanel.add(playerTwo);
        turnPanel.add(playerOneScore);
        turnPanel.add(playerTwoScore);
        elementDisplay.add(turnPanel); // adding subpanel to main panel

        timerPanel = new JPanel(); // initializing another subpanel called timerPanel
        timerPanel.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 10)); // creating empty border

        // initializing label for timer, setting fonts and size
        timerLabel = new JLabel("Timer: 00:15");
        timerLabel.setFont(new Font("Century Gothic", Font.PLAIN, 25)); // setting font and size

        // adding components
        timerPanel.add(timerLabel);
        elementDisplay.add(timerPanel); // adding subpanel to main panel
        gamePanel.add(elementDisplay); // adding main panel to game panel

        // panel for game board
        boardPanel = new JPanel(); // initialization
        boardPanel.setLayout(new GridLayout(5, 5, 10, 10)); // setting grid layout with gaps to the board panel
        boardPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 10));
        boggleBoardLabel = new JLabel[5][5];
        for (int r = 0; r < boggleBoard.length; r++) {
            for (int c = 0; c < boggleBoard[0].length; c++) {
                boggleBoardLabel[r][c] = new JLabel(boggleBoard[r][c] + "", SwingConstants.CENTER); // centering labels
                boggleBoardLabel[r][c].setFont(new Font("Century Gothic", Font.PLAIN, 30)); // setting font and size
                boggleBoardLabel[r][c].setBackground(Color.WHITE); // setting button background to white
                boggleBoardLabel[r][c].setOpaque(true);
                Border raisedBorder = BorderFactory.createRaisedBevelBorder(); // creating raised border
                boggleBoardLabel[r][c].setBorder(raisedBorder);
                boardPanel.add(boggleBoardLabel[r][c]); // adding buttons
            }
        }
        gamePanel.add(boardPanel);

        // panel for entering and checking words
        wordPanel = new JPanel(); // initializing a main word panel
        wordPanel.setLayout(new BoxLayout(wordPanel, BoxLayout.Y_AXIS)); // setting a box layout to the main word panel

        // subpanel for entering a word
        enterAndSearchPanel = new JPanel();
        enterAndSearchPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 3, 10)); // setting flow layout with gaps
        enterWordLabel = new JLabel("Enter Word: ");
        enterWordLabel.setFont(new Font("Century Gothic", Font.PLAIN, 17)); // setting font and size
        enterWordTextField = new JTextField(10);
        okayButton = new JButton("OK");
        okayButton.addActionListener(this);
        okayButton.setFont(new Font("Century Gothic", Font.PLAIN, 17)); // setting font and size

        // adding components
        enterAndSearchPanel.add(enterWordLabel);
        enterAndSearchPanel.add(enterWordTextField);
        enterAndSearchPanel.add(okayButton);
        wordPanel.add(enterAndSearchPanel);

        // subpanel for the result
        outputPanel = new JPanel();
        outputPanel.setBorder(BorderFactory.createEmptyBorder(1, 30, 10, 20)); // setting empty border
        outputMessage = new JLabel("Result: ");
        outputMessage.setFont(new Font("Century Gothic", Font.PLAIN, 17)); // setting font and size
        outputPanel.add(outputMessage);
        wordPanel.add(outputPanel);
        gamePanel.add(wordPanel);

        // panel for pass, pause, restart, and exit buttons
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 3)); // setting flow layout with gaps
        pass = new JButton("Pass");
        pause = new JButton("Pause");
        restart = new JButton("Restart");
        exit = new JButton("Exit");

        // adds the frame class as an action listener to the button
        pass.addActionListener(this);
        pause.addActionListener(this);
        restart.addActionListener(this);
        exit.addActionListener(this);

        // setting font and size
        pass.setFont(new Font("Century Gothic", Font.PLAIN, 17));
        pause.setFont(new Font("Century Gothic", Font.PLAIN, 17));
        restart.setFont(new Font("Century Gothic", Font.PLAIN, 17));
        exit.setFont(new Font("Century Gothic", Font.PLAIN, 17));

        // adding components
        buttonPanel.add(pass);
        buttonPanel.add(pause);
        buttonPanel.add(restart);
        buttonPanel.add(exit);
        gamePanel.add(buttonPanel);

        add(gamePanel);
        gamePanel.setVisible(true); // setting visibility

        setVisible(true);
    }

    // method to set a congratulations panel as visible when a player wins
    public void showCongrats() {
        timer.stop(); // stopping board timer

        // initializing congratulations panel
        congratulationsPanel = new JPanel();
        congratulationsLayout = new BoxLayout(congratulationsPanel, BoxLayout.Y_AXIS);
        congratulationsPanel.setLayout(congratulationsLayout);
        congratulationsPanel.setBackground(new Color(204, 229, 255));

        // congratulations labels
        congratulations = new JLabel(winner + " is the Winner!");
        congratulations.setAlignmentX(Component.CENTER_ALIGNMENT);
        congratulations.setFont(new Font("Century Gothic", Font.BOLD, 30));
        congratulations2 = new JLabel("Congratulations!");
        congratulations2.setAlignmentX(Component.CENTER_ALIGNMENT);
        congratulations2.setFont(new Font("Century Gothic", Font.BOLD, 30));
        congratulations2.setForeground(new Color(220, 169, 28));

        // button to return to the main menu
        mainMenu = new JButton("Main Menu");
        mainMenu.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainMenu.setFont(new Font("Century Gothic", Font.BOLD, 18));
        mainMenu.addActionListener(this);

        //changes congratulations label colours
        congratulations.setForeground(new Color(220, 38, 28)); // setting default colour
        congratulations2.setForeground(new Color(220, 38, 28)); // setting default colour
        colourTimer = new Timer(500, e -> {
            // conditional statement for changing colour depending on the value of the colourInterval variable
            if (colourInterval == 1) {
                congratulations.setForeground(new Color(220, 38, 28));
                congratulations2.setForeground(new Color(220, 38, 28));
                colourInterval++; // increment
            } else if (colourInterval == 2) {
                congratulations.setForeground(new Color(220, 169, 28));
                congratulations2.setForeground(new Color(220, 169, 28));
                colourInterval++; // increment
            } else if (colourInterval == 3) {
                congratulations.setForeground(new Color(178, 220, 28));
                congratulations2.setForeground(new Color(178, 220, 28));
                colourInterval++; // increment
            } else if (colourInterval == 4) {
                congratulations.setForeground(new Color(28, 207, 220));
                congratulations2.setForeground(new Color(28, 207, 220));
                colourInterval++; // increment
            } else if (colourInterval == 5) {
                congratulations.setForeground(new Color(169, 28, 220));
                congratulations2.setForeground(new Color(169, 28, 220));
                colourInterval++; // increment
            } else if (colourInterval == 6) {
                congratulations.setForeground(new Color(220, 28, 140));
                congratulations2.setForeground(new Color(220, 28, 140));
                colourInterval = 1; // setting to back to first colour
            }
        });
        colourTimer.start(); // start colour timer

        // adding components
        congratulationsPanel.add(Box.createRigidArea(new Dimension(0, 90)));
        congratulationsPanel.add(congratulations);
        congratulationsPanel.add(congratulations2);
        congratulationsPanel.add(Box.createRigidArea(new Dimension(0, 90)));
        congratulationsPanel.add(mainMenu);
        congratulationsPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        add(congratulationsPanel);

        // set it visible
        congratulationsPanel.setVisible(true);
        gamePanel.setVisible(false);
    }

    public static void main(String args[]) throws IOException {

        char[][][] diceBoard = new char[5][5][6];

        // fill up the dice board
        fillDiceBoard(diceBoard);

        // make to boggle board into the filled dice board
        boggleBoard = newBoard(diceBoard);


        // check every single element possible for the boggle board
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                try {
                    // call the recursive method
                    findWords(boggleBoard, i, j, dictionaryWords(), Character.toLowerCase(boggleBoard[i][j]) + "");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        // call boggle and create an instance
        Boggle JFrame = new Boggle();
        JFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // exits the application when closed
    }


}