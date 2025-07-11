import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.*;

public class NumberGuessingGame extends JFrame implements ActionListener {
    private int randomNumber;
    private int attemptsLeft;
    private int score;
    private int round;
    private final int[] MAX_ATTEMPTS = {10, 5, 3};

    private JLabel messageLabel;
    private JTextField guessField;
    private JButton guessButton, playAgainButton, nextRoundButton, startButton;
    private JLabel attemptsLabel, scoreLabel, roundLabel;
    private JPanel mainPanel, startPanel;

    public NumberGuessingGame() {
        setTitle("Number Guessing Game");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new CardLayout());

        initStartScreen();
        initGameScreen();
    }

    private void initStartScreen() {
        startPanel = new JPanel();
        startPanel.setBackground(new Color(30, 30, 60));
        startPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel welcomeLabel = new JLabel("Welcome to the Number Guessing Game!");
        welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 36));
        welcomeLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 20, 20, 20);
        startPanel.add(welcomeLabel, gbc);

        startButton = new JButton("Start Game");
        startButton.setFont(new Font("SansSerif", Font.BOLD, 28));
        startButton.setBackground(new Color(173, 216, 230));
        startButton.addActionListener(e -> switchToGameScreen());
        gbc.gridy = 1;
        startPanel.add(startButton, gbc);

        add(startPanel, "Start");
    }

    private void initGameScreen() {
        mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(30, 30, 60));

        round = 1;
        score = 0;
        setupGame();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        roundLabel = new JLabel("Round: " + round);
        roundLabel.setFont(new Font("SansSerif", Font.BOLD, 36));
        roundLabel.setForeground(Color.WHITE);
        gbc.gridy = 0;
        mainPanel.add(roundLabel, gbc);

        messageLabel = new JLabel("Guess a number between 1 and 100");
        messageLabel.setFont(new Font("SansSerif", Font.PLAIN, 28));
        messageLabel.setForeground(Color.CYAN);
        gbc.gridy = 1;
        mainPanel.add(messageLabel, gbc);

        guessField = new JTextField(10);
        guessField.setFont(new Font("SansSerif", Font.PLAIN, 28));
        gbc.gridy = 2;
        mainPanel.add(guessField, gbc);

        guessButton = new JButton("Guess");
        guessButton.setBackground(new Color(144, 238, 144));
        guessButton.setFont(new Font("SansSerif", Font.BOLD, 28));
        guessButton.addActionListener(this);
        gbc.gridy = 3;
        mainPanel.add(guessButton, gbc);

        attemptsLabel = new JLabel("Attempts left: " + attemptsLeft);
        attemptsLabel.setFont(new Font("SansSerif", Font.PLAIN, 24));
        attemptsLabel.setForeground(Color.WHITE);
        gbc.gridy = 4;
        mainPanel.add(attemptsLabel, gbc);

        scoreLabel = new JLabel("Score: " + score);
        scoreLabel.setFont(new Font("SansSerif", Font.PLAIN, 24));
        scoreLabel.setForeground(Color.WHITE);
        gbc.gridy = 5;
        mainPanel.add(scoreLabel, gbc);

        playAgainButton = new JButton("Play Again");
        playAgainButton.setVisible(false);
        playAgainButton.setFont(new Font("SansSerif", Font.PLAIN, 24));
        playAgainButton.addActionListener(e -> restartGame());
        gbc.gridy = 6;
        mainPanel.add(playAgainButton, gbc);

        nextRoundButton = new JButton("Next Round");
        nextRoundButton.setVisible(false);
        nextRoundButton.setFont(new Font("SansSerif", Font.PLAIN, 24));
        nextRoundButton.addActionListener(e -> resetGame());
        gbc.gridy = 7;
        mainPanel.add(nextRoundButton, gbc);

        add(mainPanel, "Game");
    }

    private void switchToGameScreen() {
        CardLayout cl = (CardLayout) getContentPane().getLayout();
        cl.show(getContentPane(), "Game");
    }

    private void setupGame() {
        randomNumber = new Random().nextInt(100) + 1;
        int roundIndex = Math.min(round - 1, MAX_ATTEMPTS.length - 1);
        attemptsLeft = MAX_ATTEMPTS[roundIndex];
    }

    private void resetGame() {
        round++;
        if (round > 3) {
            messageLabel.setText("Game Over! Final Score: " + score);
            guessButton.setEnabled(false);
            guessField.setEnabled(false);
            playAgainButton.setVisible(true);
            nextRoundButton.setVisible(false);
            return;
        }
        setupGame();
        roundLabel.setText("Round: " + round);
        attemptsLabel.setText("Attempts left: " + attemptsLeft);
        messageLabel.setText("Guess a number between 1 and 100");
        guessField.setText("");
        guessButton.setEnabled(true);
        guessField.setEnabled(true);
        playAgainButton.setVisible(false);
        nextRoundButton.setVisible(false);
    }

    private void restartGame() {
        round = 1;
        score = 0;
        setupGame();
        roundLabel.setText("Round: " + round);
        attemptsLabel.setText("Attempts left: " + attemptsLeft);
        messageLabel.setText("Guess a number between 1 and 100");
        scoreLabel.setText("Score: " + score);
        guessField.setText("");
        guessButton.setEnabled(true);
        guessField.setEnabled(true);
        playAgainButton.setVisible(false);
        nextRoundButton.setVisible(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            int guess = Integer.parseInt(guessField.getText());
            attemptsLeft--;

            if (guess == randomNumber) {
                messageLabel.setText("Correct! You guessed it in " + (MAX_ATTEMPTS[Math.min(round - 1, 2)] - attemptsLeft) + " attempts.");
                score += 10;
                scoreLabel.setText("Score: " + score);
                guessButton.setEnabled(false);
                if (round < 3) {
                    nextRoundButton.setVisible(true);
                } else {
                    messageLabel.setText("Game Completed! Final Score: " + score);
                    guessField.setEnabled(false);
                }
                playAgainButton.setVisible(true);
            } else if (guess < randomNumber) {
                messageLabel.setText("Too low! Try again.");
            } else {
                messageLabel.setText("Too high! Try again.");
            }

            if (attemptsLeft == 0 && guess != randomNumber) {
                messageLabel.setText("Out of attempts! Number was: " + randomNumber);
                guessButton.setEnabled(false);
                guessField.setEnabled(false);
                playAgainButton.setVisible(true);
                nextRoundButton.setVisible(false);
            }

            attemptsLabel.setText("Attempts left: " + attemptsLeft);
        } catch (NumberFormatException ex) {
            messageLabel.setText("Please enter a valid number.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            NumberGuessingGame game = new NumberGuessingGame();
            game.setVisible(true);
        });
    }
}

