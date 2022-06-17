import java.util.*;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class boggleBoard extends JFrame implements ActionListener{


    // add in components for the GUI
    JPanel menu, instructionsPanel, optionsPanel, titlePanel, modePanel, difficultyPanel, tournamentScorePanel, backToMenuPanel,
            gamePanel, boardTitlePanel, boardPanel, elementDisplay, turnPanel, timerPanel, wordPanel, enterAndSearchPanel, outputPanel, buttonPanel;

    JLabel boggleTitle, playerOne, playerOneScore, playerTwo, playerTwoScore, timer, enterWordLabel, outputMessage, errorLabel,welcome,
            mode, tournamentScore, difficulty, optionsTitle, modeOption, difficultyOption, tournamentScoreOption;

    JButton okayButton, pass, pause, restart, exit, play, instructions, options, backMenu, singleBtn, multiBtn,
            easyBtn, mediumBtn, hardBtn, enterBtn, backToMenu;

    JLabel[][] boggleBoard;
    BoxLayout menuLayout, instructionsLayout, optionsLayout;
    FlowLayout modeLayout, difficultyLayout, tournamentScoreLayout;
    JTextArea instructionsInformation;
    JTextField tournamentScoreField, enterWordTextField;


    // variables for the settings
    static String playerMode = "Single";
    static int tournamentScoreInput = 50;
    static String difficultyInput = "Easy";


    public boggleBoard() throws IOException {
        setTitle("Boggle");
        setSize(600, 600); // changed


        menu = new JPanel();
        menuLayout = new BoxLayout(menu, BoxLayout.Y_AXIS);
        menu.setLayout(menuLayout);

        welcome = new JLabel("Welcome to Boggle!");
        welcome.setFont(new Font("Malgun Gothic", Font.BOLD, 50));
        welcome.setMinimumSize(new Dimension(100, 400));
        welcome.setAlignmentX(Component.CENTER_ALIGNMENT);
        welcome.setForeground(new java.awt.Color(0, 29, 219)); // may want to change colour



        play = new JButton("    Play    ");
        // play.setPreferredSize(new Dimension(100, 120));
        play.setFont(new Font("Times New Roman", Font.PLAIN, 15)); // maybe want a more rounded font
        // play.setBorderPainted(false);
        // play.setBackground(new java.awt.Color(0,128,255));
        // play.setForeground(Color.BLACK);
        // play.setOpaque(true);
        // play.setBorder(raisedBorder);
        // play.setContentAreaFilled(false);
        // play.setFocusPainted(false);
        // play.setBounds(70,80,100,30);
        play.setBorder(new RoundBtn(10));
        play.setAlignmentX(Component.CENTER_ALIGNMENT);
        play.addActionListener(this);



        instructions = new JButton("Instructions");
        // instructions.setPreferredSize(new Dimension(100, 100));
        instructions.setFont(new Font("Times New Roman", Font.PLAIN, 15)); // maybe want a more rounded font
        // instructions.setBorderPainted(false);
        // instructions.setBackground(new java.awt.Color(0,128,255));
        // instructions.setForeground(Color.BLACK);
        // instructions.setOpaque(true);
        instructions.setAlignmentX(Component.CENTER_ALIGNMENT);
        instructions.setBorder(new RoundBtn(10));
        instructions.addActionListener(this);


        options = new JButton("  Options  ");
        // options.setPreferredSize(new Dimension(100, 100));
        options.setFont(new Font("Times New Roman", Font.PLAIN, 15)); // maybe want a more rounded font
        // options.setBorderPainted(false);
        // options.setBackground(new java.awt.Color(0,128,255));
        // options.setForeground(Color.BLACK);
        // options.setOpaque(true);
        options.setBorder(new RoundBtn(10));
        options.setAlignmentX(Component.CENTER_ALIGNMENT);
        options.addActionListener(this);

        // setting the default modes
        mode = new JLabel("Mode: " + playerMode);
        mode.setAlignmentX(Component.CENTER_ALIGNMENT);
        difficulty = new JLabel("Difficulty: " + difficultyInput);
        difficulty.setAlignmentX(Component.CENTER_ALIGNMENT);
        tournamentScore = new JLabel("Tournament Score: " + tournamentScoreInput);
        tournamentScore.setAlignmentX(Component.CENTER_ALIGNMENT);


        //menu.add(new JOutlineLabel("Welcome to Boggle!"));
        menu.add(Box.createRigidArea(new Dimension(0, 150)));
        menu.add(welcome);
        menu.add(Box.createRigidArea(new Dimension(0, 150)));
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


        add(menu);

        setVisible(true);



        // instructions frame --> do i make the panels or frame invisible/visible
		/*
		instructionsFrame = new Frame();
		instructionsFrame.setSize(600, 600);
		instructionsFrame.add(instructionsPanel);
		*/

        ;	}

    class RoundBtn implements Border
    {
        private int r;
        RoundBtn(int r) {
            this.r = r;
        }
        public Insets getBorderInsets(Component c) {
            return new Insets(this.r+1, this.r+1, this.r+2, this.r);
        }
        public boolean isBorderOpaque() {
            return true;
        }
        public void paintBorder(Component c, Graphics g, int x, int y,
                                int width, int height) {
            g.drawRoundRect(x, y, width-1, height-1, r, r);
        }
    }

    public void actionPerformed(ActionEvent event) {
        Object component = event.getSource();

        if(component.equals(play)) {
            menu.setVisible(false);

            gamePanel = new JPanel();
            gamePanel.setLayout(new BoxLayout(gamePanel, BoxLayout.Y_AXIS));
            gamePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // set an empty border to the panel

            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // vanishing the frame when closed

            // panel for title
            boardTitlePanel = new JPanel();
            boggleTitle = new JLabel("Boggle");
            boggleTitle.setFont(new Font("Century Gothic", Font.BOLD, 30)); // setting font
            boardTitlePanel.add(boggleTitle);
            gamePanel.add(boardTitlePanel);

            // panel for the turns, scores, and timer
            elementDisplay = new JPanel(); // initializing main panel
            elementDisplay.setLayout(new FlowLayout(FlowLayout.LEFT, 60, 5)); // setting flow layout and gaps between sub panels

            turnPanel = new JPanel(); // initializing subpanel
            turnPanel.setLayout(new GridLayout(2, 2, 5, 0)); // setting a grid layout with gaps
            // initializing labels and centering them to the center of the panel
            playerOne = new JLabel("Player One", SwingConstants.CENTER);
            playerTwo = new JLabel("Player Two", SwingConstants.CENTER);
            playerOneScore = new JLabel("0", SwingConstants.CENTER);
            playerTwoScore = new JLabel("0", SwingConstants.CENTER);

            // setting borders
            playerOne.setBorder(BorderFactory.createLineBorder(Color.black));
            playerTwo.setBorder(BorderFactory.createLineBorder(Color.black));
            playerOneScore.setBorder(BorderFactory.createLineBorder(Color.black));
            playerTwoScore.setBorder(BorderFactory.createLineBorder(Color.black));

            // setting fonts and sizes
            playerOne.setFont(new Font("Century Gothic", Font.BOLD, 17));
            playerTwo.setFont(new Font("Century Gothic", Font.BOLD, 17));
            playerOneScore.setFont(new Font("Century Gothic", Font.BOLD, 17));
            playerTwoScore.setFont(new Font("Century Gothic", Font.BOLD, 17));

            // adding components
            turnPanel.add(playerOne);
            turnPanel.add(playerTwo);
            turnPanel.add(playerOneScore);
            turnPanel.add(playerTwoScore);
            elementDisplay.add(turnPanel); // adding subpanel to main panel

            timerPanel = new JPanel(); // initializing subpanel
            timerPanel.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 10));
            timer = new JLabel("Timer: 00:15");
            timer.setFont(new Font("Century Gothic", Font.BOLD, 25)); // setting font and size
            timerPanel.add(timer);
            elementDisplay.add(timerPanel); // adding subpanel to main panel
            gamePanel.add(elementDisplay);

            // panel for game board
            boardPanel = new JPanel();
            boardPanel.setLayout(new GridLayout(5, 5, 10, 10)); // setting grid layout with gaps to the board panel
            boardPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 10));
            boggleBoard = new JLabel[5][5];
            for (int r = 0; r < boggleBoard.length; r++) {
                for (int c = 0; c < boggleBoard[0].length; c++) {
                    boggleBoard[r][c] = new JLabel("A", SwingConstants.CENTER); // centering labels
                    boggleBoard[r][c].setFont(new Font("Century Gothic", Font.BOLD, 30)); // setting font and size
                    boggleBoard[r][c].setBackground(Color.WHITE);
                    Border raisedBorder = BorderFactory.createRaisedBevelBorder();
                    boggleBoard[r][c].setBorder(raisedBorder);

                    boggleBoard[r][c].setOpaque(true);
                    boardPanel.add(boggleBoard[r][c]);
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
            enterWordLabel.setFont(new Font("Century Gothic", Font.BOLD, 17)); // setting font and size
            enterWordTextField = new JTextField(10);
            okayButton = new JButton("OK");
            okayButton.setFont(new Font("Century Gothic", Font.BOLD, 17)); // setting font and size

            // adding components
            enterAndSearchPanel.add(enterWordLabel);
            enterAndSearchPanel.add(enterWordTextField);
            enterAndSearchPanel.add(okayButton);
            wordPanel.add(enterAndSearchPanel);

            // subpanel for the result
            outputPanel = new JPanel();
            outputPanel.setBorder(BorderFactory.createEmptyBorder(1, 30, 10, 20));
            outputMessage = new JLabel("Result: ");
            outputMessage.setFont(new Font("Century Gothic", Font.BOLD, 17)); // setting font and size
            outputPanel.add(outputMessage);
            wordPanel.add(outputPanel);
            gamePanel.add(wordPanel);

            // panel for pass, pause, restart, and exit buttons
            buttonPanel = new JPanel();
            buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 3)); // setting flow layout with gaps
            pass = new JButton("Pass");
            pause = new JButton("Pause");
            restart = new JButton("Restart");
            exit = new JButton("Exit");
            exit.addActionListener(this);

            // setting font and size
            pass.setFont(new Font("Century Gothic", Font.BOLD, 17));
            pause.setFont(new Font("Century Gothic", Font.BOLD, 17));
            restart.setFont(new Font("Century Gothic", Font.BOLD, 17));
            exit.setFont(new Font("Century Gothic", Font.BOLD, 17));

            buttonPanel.add(pass);
            buttonPanel.add(pause);
            buttonPanel.add(restart);
            buttonPanel.add(exit);
            gamePanel.add(buttonPanel);

            add(gamePanel);

            gamePanel.setVisible(true);

            setVisible(true);
        }
        else if(component.equals(instructions)) {
            // pops up the instructions file

            menu.setVisible(false); // closes the menu frame and opens the instructions frame

            instructionsPanel = new JPanel();
            instructionsLayout = new BoxLayout(instructionsPanel, BoxLayout.Y_AXIS);
            instructionsPanel.setLayout(instructionsLayout);

            instructionsInformation = new JTextArea("How to play Boggle:\n"
                    + "\n"
                    + "1. A 5 by 5 board of dice will be shaken up to show 25 random letters.\n"
                    + "2. The first player will enter a word found on the board (the longer the word the better!).\n"
                    + "3. The next player will enter a different word found on the board.\n"
                    + "4. Steps 2 and 3 will continue, alternating between players, until both players can no longer\n"
                    + " find a word.\n"
                    + "5. If both players pass, you have the option to shake up the board.\n"
                    + "6. The length of the word entered determines the number of points you earn.\n"
                    + "7. First player to reach the tournament score wins the game.\n"
                    + "\n"
                    + "Setting Features:\n"
                    + "1. Mode: Singleplayer (Player vs. AI) vs. Multiplayer (Player vs. Player)\n"
                    + "2. Difficulty: Easy(minimum 3 letters), Medium (minimum 4 letters), Hard (minimum 5 letters)\n"
                    + "3. Tournament Scores: You can set the score be met in order to win the game");
            instructionsInformation.setAlignmentX(Component.CENTER_ALIGNMENT);
            instructionsInformation.setMaximumSize(new Dimension(700, 350));


            backMenu = new JButton("Back to Menu");
            backMenu.setAlignmentX(Component.CENTER_ALIGNMENT);
            backMenu.addActionListener(this);

            instructionsPanel.add(instructionsInformation);
            instructionsPanel.add(backMenu);

            add(instructionsPanel);

            instructionsPanel.setVisible(true);

            setVisible(true);

        }
        else if(component.equals(options)) {
            menu.setVisible(false);

            optionsPanel = new JPanel();
            optionsLayout = new BoxLayout(optionsPanel, BoxLayout.Y_AXIS);
            optionsPanel.setLayout(optionsLayout);



            titlePanel = new JPanel();
            optionsPanel.add(Box.createRigidArea(new Dimension(0, 60)));
            optionsTitle = new JLabel("Options");
            // optionsTitle.setPreferredSize(new Dimension(300, 200));
            optionsTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
            optionsTitle.setFont(new Font("Times New Roman", Font.BOLD, 40));
            titlePanel.add(optionsTitle);


            modePanel = new JPanel();
            modeLayout = new FlowLayout();
            modePanel.setLayout(modeLayout);

            modeOption = new JLabel("Mode: ");
            singleBtn = new JButton("Single");
            singleBtn.setBackground(Color.LIGHT_GRAY);
            singleBtn.setContentAreaFilled(false);
            singleBtn.setOpaque(true);
            singleBtn.addActionListener(this);
            multiBtn = new JButton("Multi");
            multiBtn.addActionListener(this);

            modePanel.add(modeOption);
            modePanel.add(singleBtn);
            modePanel.add(multiBtn);




            difficultyPanel = new JPanel();
            difficultyLayout = new FlowLayout();
            difficultyPanel.setLayout(difficultyLayout);

            difficultyOption = new JLabel("Difficulty: ");
            easyBtn = new JButton("Easy");
            easyBtn.setBackground(Color.LIGHT_GRAY);
            easyBtn.setContentAreaFilled(false);
            easyBtn.setOpaque(true);
            easyBtn.addActionListener(this);
            mediumBtn = new JButton("Medium");
            mediumBtn.addActionListener(this);
            hardBtn = new JButton("Hard");
            hardBtn.addActionListener(this);

            difficultyPanel.add(difficultyOption);
            difficultyPanel.add(easyBtn);
            difficultyPanel.add(mediumBtn);
            difficultyPanel.add(hardBtn);



            tournamentScorePanel = new JPanel();
            tournamentScoreLayout = new FlowLayout();
            tournamentScorePanel.setLayout(tournamentScoreLayout);

            tournamentScoreOption = new JLabel("Tournament Score: ");
            tournamentScoreField = new JTextField(10);
            enterBtn = new JButton("Enter");
            enterBtn.addActionListener(this);

            tournamentScorePanel.add(tournamentScoreOption);
            tournamentScorePanel.add(tournamentScoreField);
            tournamentScorePanel.add(enterBtn);


            backToMenuPanel = new JPanel();
            backToMenu = new JButton("Back To Menu");
            backToMenu.addActionListener(this);
            backToMenuPanel.add(backToMenu);



            optionsPanel.add(titlePanel);
            optionsPanel.add(modePanel);
            optionsPanel.add(difficultyPanel);
            optionsPanel.add(tournamentScorePanel);
            optionsPanel.add(backToMenuPanel);

            add(optionsPanel);

            optionsPanel.setVisible(true);

        }

        if(component.equals(backMenu)) {
            instructionsPanel.setVisible(false);
            menu.setVisible(true);
        }
        if(component.equals(backToMenu)) {
            optionsPanel.setVisible(false);
            menu.setVisible(true);
        }

        if(component.equals(singleBtn)) {
            playerMode = "Single";
            multiBtn.setBackground(null);
            singleBtn.setBackground(Color.LIGHT_GRAY);
            singleBtn.setContentAreaFilled(false);
            singleBtn.setOpaque(true);
            mode.setText("Mode: " + playerMode);
            menu.add(mode);
        }
        else if(component.equals(multiBtn)) {
            playerMode = "Multi";
            singleBtn.setBackground(null);
            multiBtn.setBackground(Color.LIGHT_GRAY);
            multiBtn.setContentAreaFilled(false);
            multiBtn.setOpaque(true);
            mode.setText("Mode: " + playerMode);
            menu.add(mode);
        }

        if(component.equals(easyBtn)) {
            difficultyInput = "Easy";

            // removes highlight for the other two options
            hardBtn.setBackground(null);
            mediumBtn.setBackground(null);

            easyBtn.setBackground(Color.LIGHT_GRAY);
            easyBtn.setContentAreaFilled(false);
            easyBtn.setOpaque(true);
            difficulty.setText("Difficulty: " + difficultyInput);
            menu.add(difficulty);
        }
        else if(component.equals(mediumBtn)) {
            difficultyInput = "Medium";

            // removes highlight for the other two options
            easyBtn.setBackground(null);
            hardBtn.setBackground(null);

            mediumBtn.setBackground(Color.LIGHT_GRAY);
            mediumBtn.setContentAreaFilled(false);
            mediumBtn.setOpaque(true);
            difficulty.setText("Difficulty: " + difficultyInput);
            menu.add(difficulty);
            // difficultyOption.setText("Difficulty: " + difficultyInput);
            // difficultyPanel.add(difficultyOption);
        }
        else if(component.equals(hardBtn)) {
            difficultyInput = "Hard";

            // removes highlight for the other two options
            easyBtn.setBackground(null);
            mediumBtn.setBackground(null);

            hardBtn.setBackground(Color.LIGHT_GRAY);
            hardBtn.setContentAreaFilled(false);
            hardBtn.setOpaque(true);
            difficulty.setText("Difficulty: " + difficultyInput);
            menu.add(difficulty);
            // difficultyOption.setText("Difficulty: " + difficultyInput);
            // difficultyPanel.add(difficultyOption);
        }

        if(component.equals(enterBtn)) {
            try {
                tournamentScoreInput = Integer.parseInt(tournamentScoreField.getText());
                tournamentScore.setText("Tournament Score: " + tournamentScoreInput);
                menu.add(tournamentScore);
            } catch (NumberFormatException e) {
                errorLabel = new JLabel("Number not entered. Please enter a valid number.");
                tournamentScorePanel.add(errorLabel);
                setVisible(true);
                tournamentScoreInput = 50;
                tournamentScore.setText("Tournament Score: " + tournamentScoreInput);
                menu.add(tournamentScore);
            }
        }

        if(component.equals(exit)) {
            gamePanel.setVisible(false);
            menu.setVisible(true);
        }
    }

    public static void main(String args[]) throws IOException {

        boggleBoard JFrame = new boggleBoard();
        JFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


}


