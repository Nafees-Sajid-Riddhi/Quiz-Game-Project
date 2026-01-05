/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author user
 */

// Importing required classes for building the GUI and handling events
import javax.swing.*;            // For JFrame, JButton, JTextArea, JPanel, JScrollPane, etc.
import java.awt.*;                // For layout management (BorderLayout, JPanel, etc.)
import java.awt.event.*;          // For handling button clicks (ActionListener)
import java.io.*;                 // For reading and writing files (BufferedReader, BufferedWriter, FileReader, FileWriter)
import java.util.*;               // For using Queue and LinkedList

public class PerformanceAnalysis extends JFrame implements ActionListener {

    // GUI components declaration
    private JButton addButton, removeButton, backButton;    // Buttons for performing operations
    private JTextArea resultArea;                           // Text area to display results and messages
    private static final String FILE_PATH = "src/users.txt";// Path to the file where users will be stored
    private Queue<String> userQueue = new LinkedList<>();    // Queue for storing user information

    // Constructor to initialize the GUI and load existing users
    public PerformanceAnalysis() {
        setTitle("Performance Analysis");        // Set the title of the window
        setSize(500, 400);                       // Set the size of the window
        setDefaultCloseOperation(EXIT_ON_CLOSE); // Exit application when the window is closed
        setLayout(new BorderLayout());           // Use BorderLayout for the main layout

        // Initialize buttons
        addButton = new JButton("Add 100 & 150 Users");    // Button to add users
        removeButton = new JButton("Remove 100 & 150 Users"); // Button to remove users
        backButton = new JButton("Back");                  // Button to go back to the main menu
        resultArea = new JTextArea();                      // Text area for displaying results
        resultArea.setEditable(false);                     // Make text area read-only

        // Adding action listeners to buttons to respond to clicks
        addButton.addActionListener(this);
        removeButton.addActionListener(this);
        backButton.addActionListener(this);

        // Create a panel to hold the buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(backButton);

        // Add components to the window (JFrame)
        add(buttonPanel, BorderLayout.NORTH);                // Adding buttons at the top of the window
        add(new JScrollPane(resultArea), BorderLayout.CENTER);// Adding text area in the center with scroll functionality

        loadUsers();    // Load existing users from the file when the application starts

        setLocationRelativeTo(null); // Center the window on the screen
        setVisible(true);             // Display the window
    }

    // Load existing users from the file into the queue
    private void loadUsers() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                userQueue.offer(line);  // Add each line from the file to the queue
            }
            resultArea.append("Loaded " + userQueue.size() + " existing users.\n"); // Display number of users loaded
        } catch (IOException e) {
            resultArea.append("No existing users found.\n"); // Display message if file is not found or empty
        }
    }

    // Handle button clicks
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) { // If 'Add' button is clicked
            addUsers(100);          // Add 100 users
            addUsers(150);          // Add 150 users
            saveUsersToFile();      // Save the updated user list to the file
        }

        if (e.getSource() == removeButton) { // If 'Remove' button is clicked
            removeUsers(100);        // Remove 100 users
            removeUsers(150);        // Remove 150 users
            saveUsersToFile();       // Save the updated user list to the file
        }
        
        if (e.getSource() == backButton) { // If 'Back' button is clicked
            dispose();               // Close the PerformanceAnalysis window
            new QuizLogin();         // Return to the main menu or login page
        }
    }

    // Adds a specified number of users to the queue and calculates the time taken
    private void addUsers(int count) {
        long start = System.nanoTime();  // Record the start time

        // Generate users and add them to the queue
        for (int i = 1; i <= count; i++) {
            userQueue.offer("User" + i + ", email" + i + "@example.com"); // Add a user to the queue
        }

        long duration = (System.nanoTime() - start) / 1_000_000; // Calculate the duration in milliseconds
        resultArea.append("Time taken to add " + count + " users: " + duration + " ms\n"); // Display the result
    }

    // Removes a specified number of users from the queue and calculates the time taken
    private void removeUsers(int count) {
        long start = System.nanoTime(); // Record the start time

        // Remove users from the queue
        for (int i = 0; i < count && !userQueue.isEmpty(); i++) {
            userQueue.poll(); // Remove the user from the queue
        }

        long duration = (System.nanoTime() - start) / 1_000_000; // Calculate the duration in milliseconds
        resultArea.append("Time taken to remove " + count + " users: " + duration + " ms\n"); // Display the result
    }

    // Save the current state of the user list to the file
    private void saveUsersToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (String user : userQueue) {
                writer.write(user);         // Write each user to the file
                writer.newLine();           // Move to the next line
            }
            resultArea.append("User list successfully saved to file.\n"); // Confirm save operation
        } catch (IOException e) {
            resultArea.append("Error saving user list to file.\n"); // Handle any errors during file writing
        }
    }

    // Main method to launch the Performance Analysis window
    public static void main(String[] args) {
        SwingUtilities.invokeLater(PerformanceAnalysis::new); // Run the GUI creation on the Event Dispatch Thread (EDT)
    }
}
