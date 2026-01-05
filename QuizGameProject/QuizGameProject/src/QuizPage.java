/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author user
 */
import javax.swing.*;   // Provides classes for Swing components like JFrame, JLabel, JButton, etc.

import java.awt.*;        // Provides classes for GUI components and layout management like GridLayout, JPanel, etc.
import java.awt.event.*;   // Provides classes for handling events like button clicks using ActionListener.
import java.io.*;          // Provides classes for handling file reading and writing (BufferedReader, FileReader, etc.)
import java.util.*;         // Provides classes for data structures like Queue and LinkedList.
import java.util.Timer;     // Provides Timer class for scheduling tasks to run at a specified time.
import java.util.TimerTask; // Provides TimerTask class for creating tasks that can be scheduled by a Timer.

public class QuizPage extends JFrame implements ActionListener {

    // Declaring components for the GUI
    private JLabel questionLabel, timerLabel; // Labels to display the question and timer countdown.
    private JRadioButton[] options = new JRadioButton[4]; // Radio buttons for displaying multiple-choice options.
    private ButtonGroup group;  // Group to ensure only one radio button can be selected at a time.
    private JButton nextButton, logoutButton; // Buttons for moving to the next question and logging out.

    // Queues to store quiz data
    private Queue<String> questionQueue = new LinkedList<>();            // Stores the list of questions.
    private Queue<String[]> optionsQueue = new LinkedList<>();            // Stores the list of options for each question.
    private Queue<Integer> correctAnswersQueue = new LinkedList<>();      // Stores the index of the correct answer for each question.

    private int score = 0;             // Keeps track of the user's score.
    private int currentQuestion = 0;   // Keeps track of the current question number.
    private int totalQuestions = 0;    // Keeps track of the total number of questions in the quiz.

    private Timer timer;               // Timer for handling question countdown.
    private int questionTimeRemaining = 30;  // Time limit for each question (in seconds).
    private int totalTimeTaken = 0;          // Total time taken to complete the entire quiz.

    public QuizPage() {
        // Setting up the quiz window
        setTitle("Quiz Game - Data Structures");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(7, 1));  // Using a grid layout with 7 rows and 1 column for placing components vertically.

        // Initializing labels to display question and timer
        questionLabel = new JLabel("Question will appear here");
        timerLabel = new JLabel("Timer: 30s");
        
        add(questionLabel);
        add(timerLabel);

        // Setting up radio buttons for multiple-choice options
        group = new ButtonGroup();
        for (int i = 0; i < 4; i++) {
            options[i] = new JRadioButton();
            group.add(options[i]);
            add(options[i]);
        }

        // Panel to hold navigation buttons
        JPanel buttonPanel = new JPanel();
        
        nextButton = new JButton("Next");
        nextButton.addActionListener(this);  // Registering action listener for the 'Next' button.
        buttonPanel.add(nextButton);
        
        logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> {  // Lambda function to handle the logout action.
            dispose();  // Closes the quiz window.
            new QuizLogin();  // Returns to the login page.
        });
        buttonPanel.add(logoutButton);
        
        add(buttonPanel);

        loadQuestions();   // Load questions from the file
        displayQuestion();  // Display the first question
        
        setLocationRelativeTo(null);  // Centering the window on the screen.
        setVisible(true);  // Making the window visible.
    }

    private void loadQuestions() {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/questions.txt"))) {
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Question: ")) {
                    String question = line.substring(10).trim();
                    questionQueue.add(question);

                    line = reader.readLine();
                    if (line != null && line.startsWith("Options: ")) {
                        String[] optionsArray = line.substring(9).split("\\|");
                        optionsQueue.add(optionsArray);
                    }

                    line = reader.readLine();
                    if (line != null && line.startsWith("Answer: ")) {
                        int correctAnswerIndex = Integer.parseInt(line.substring(8).trim()) - 1;
                        correctAnswersQueue.add(correctAnswerIndex);
                    }

                    reader.readLine(); // Read the empty line separator
                }
            }

            totalQuestions = questionQueue.size();

            if (totalQuestions == 0) {
                JOptionPane.showMessageDialog(this, "No questions found in the file.");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading questions: " + e.getMessage());
        }
    }

    private void displayQuestion() {
        if (questionQueue.isEmpty()) {
            showResult();
            return;
        }

        // Reset timer for the new question
        questionTimeRemaining = 30; 
        if (timer != null) {
            timer.cancel();
        }

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> {
                    if (questionTimeRemaining > 0) {
                        timerLabel.setText("Timer: " + questionTimeRemaining + "s");
                        questionTimeRemaining--;
                        totalTimeTaken++;
                    } else {
                        timer.cancel();
                        totalTimeTaken++;
                        displayQuestion(); // Automatically move to next question if time is up
                    }
                });
            }
        }, 0, 1000);

        // Display the next question and options
        String question = questionQueue.poll();
        String[] optionsArray = optionsQueue.poll();

        questionLabel.setText("Q" + (currentQuestion + 1) + ": " + question);
        group.clearSelection();

        for (int i = 0; i < options.length; i++) {
            if (i < optionsArray.length) {
                options[i].setText(optionsArray[i]);
                options[i].setVisible(true);
            } else {
                options[i].setVisible(false);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!isOptionSelected()) {
            JOptionPane.showMessageDialog(this, "Please select an option before proceeding.");
            return;
        }

        timer.cancel(); // Stop the timer for this question

        int selectedAnswerIndex = getSelectedOptionIndex();
        int correctAnswerIndex = correctAnswersQueue.poll();

        if (selectedAnswerIndex == correctAnswerIndex) {
            score++;
        }

        currentQuestion++;
        displayQuestion();
    }

    private boolean isOptionSelected() {
        for (JRadioButton option : options) {
            if (option.isSelected()) {
                return true;
            }
        }
        return false;
    }

    private int getSelectedOptionIndex() {
        for (int i = 0; i < options.length; i++) {
            if (options[i].isSelected()) {
                return i;
            }
        }
        return -1;
    }

    private void showResult() {
        JOptionPane.showMessageDialog(this,
                "Quiz Finished!\n" +
                "Your score: " + score + "/" + totalQuestions + "\n" +
                "Total Time Taken: " + totalTimeTaken + " seconds.");
        
        dispose(); // Close the Quiz window
        new QuizLogin(); // Redirect to login page
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(QuizPage::new);
    }
}
