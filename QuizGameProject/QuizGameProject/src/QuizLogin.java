/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author user
 */
// Importing necessary Java Swing components for creating the GUI
import javax.swing.*;  // Provides classes for creating windows, buttons, labels, text fields, etc.

// Importing AWT packages for layout management and basic UI operations
import java.awt.*;  // Provides classes for managing graphical user interface elements
import java.awt.event.*;  // Provides interfaces and classes for event handling


public class QuizLogin extends JFrame implements ActionListener {

    // Declaring UI components for the login interface
    private JTextField classIdField;  // Text field for user to enter the Class ID
    private JButton loginButton, performanceButton;  // Buttons for login and performance analysis

    // Constructor to initialize the GUI components
    public QuizLogin() {
        setTitle("Quiz Login");  // Setting the title of the window
        setSize(300, 150);  // Setting the window size (width x height)
        setDefaultCloseOperation(EXIT_ON_CLOSE);  // Ensures the application exits when the window is closed
        setLayout(new FlowLayout());  // Setting layout manager to FlowLayout

        // Creating label and text field for entering Class ID
        JLabel label = new JLabel("Enter Class ID:");
        classIdField = new JTextField(15);

        // Creating buttons for login and performance analysis
        loginButton = new JButton("Login");
        performanceButton = new JButton("Performance Analysis");

        // Adding action listeners to the buttons for handling click events
        loginButton.addActionListener(this);
        performanceButton.addActionListener(this);

        // Adding components to the window
        add(label);
        add(classIdField);
        add(loginButton);
        add(performanceButton);

        setLocationRelativeTo(null);  // Center the window on the screen
        setVisible(true);  // Make the window visible to the user
    }

    // Handling button click events
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {  // If the 'Login' button is clicked
            String classId = classIdField.getText().trim();  // Get and trim the text from the input field

            if (classId.equals("12345")) {  // Check if entered Class ID is valid
                JOptionPane.showMessageDialog(this, "Login Successful! Proceeding to Quiz...");
                
                QuizPage quizPage = new QuizPage();  // Open the QuizPage window
                quizPage.setVisible(true);  // Make the QuizPage visible
                
                dispose(); // Close the login window to free up resources
            } else {
                // Display error message if the Class ID is incorrect
                JOptionPane.showMessageDialog(this, "Invalid Class ID. Try again with a valid ID.");
            }
        } else if (e.getSource() == performanceButton) {  // If the 'Performance Analysis' button is clicked
            PerformanceAnalysis performanceWindow = new PerformanceAnalysis();  // Open PerformanceAnalysis window
            performanceWindow.setVisible(true);  // Make the PerformanceAnalysis window visible
        }
    }

    // Main method to run the application
    public static void main(String[] args) {
        new QuizLogin();  // Create and display the login window
    }
}
