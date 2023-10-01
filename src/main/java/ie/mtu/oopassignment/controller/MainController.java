package ie.mtu.oopassignment.controller;

import ie.mtu.oopassignment.model.Database;
import ie.mtu.oopassignment.model.StudentModel;
import ie.mtu.oopassignment.view.StudentRecordsView;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * This method controls the rest of the program
 */
public class MainController {
    private final Connection conn;

    public MainController() throws SQLException, ClassNotFoundException {
        conn = Database.getConnection();
    }

    // sets up arraylist of students and allows use of methods in the method class
    public final ArrayList<StudentModel> students = new ArrayList<>();
    public StudentRecordsView view = new StudentRecordsView();

    /**
     *  This method updates the list input by displaying all the details of each student  and all the modules associated with the student. It clears the list input before displaying the new details.
     * @param students is an ArrayList of students in the database
     * @param listInput a TextArea that displays to the user
     */
    public void updateList(ArrayList<StudentModel> students, TextArea listInput) {
        // Clear the list input
        listInput.clear();

        // Add each student to the list input
        for (StudentModel student : students) {
            listInput.appendText("Name: " + student.getName() + "\n");
            listInput.appendText("Student Number: " + student.getStudentNo() + "\n");
            listInput.appendText("Date of Birth: " + student.getDOB() + "\n");
            listInput.appendText("Current Semester: " + student.getSemester() + "\n");

            try {
                // Query the database for the modules associated with this student
                String sql = "SELECT * FROM modules WHERE studentNo = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, student.getStudentNo());
                ResultSet rs = pstmt.executeQuery();

                // Add a header for the module list
                listInput.appendText("Modules:\n");

                // Loop through the results and add each module to the list input
                while (rs.next()) {
                    String moduleName = rs.getString("moduleName");
                    String crn = rs.getString("CRN");
                    int semester = rs.getInt("semester");
                    String grade = rs.getString("grade");
                    listInput.appendText(moduleName + " (" + crn + ") - Semester " + semester + " - Grade: " + grade + "\n");
                }

                // Close the result set and statement
                rs.close();
                pstmt.close();

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

            // Add an extra line break between students
            listInput.appendText("\n");
        }

        // Add an extra line break between students
        listInput.appendText("\n");
    }


    /**
     * This creates a popup stage and gives the user options to close the program, close the popup or create a memory leak
     * @param stage This is the  main stage window of program
     */
    //add event handler for close button
    public void exitButton(Stage stage) {
        stage.setOnCloseRequest(e -> {
            // sets up 2nd window that doesn't allow user to interact with the main window
            e.consume();
            VBox vbox2 = new VBox(10);
            vbox2.setPadding(new Insets(10));
            Stage confirmStage = new Stage();
            confirmStage.initModality(Modality.APPLICATION_MODAL);

            HBox hbox9 = new HBox(5);
            //creates a memory leak and displays time in console once it ran out oof memory.
            Button memLeak = new Button("Memory Leak");
            memLeak.setOnAction(mLe -> {
                long startTime = System.currentTimeMillis();
                ArrayList<StudentModel> students = new ArrayList<>();
                try {
                    while (true) {
                        students.add(new StudentModel());
                    }
                } catch (OutOfMemoryError eM) {
                    long endTime = System.currentTimeMillis();
                    System.out.println("Memory ran out after " + (endTime - startTime) + " milliseconds.");
                }
            });

            Button exitBtn = new Button("Exit");
            Button cancelBtn = new Button("Cancel");
            hbox9.getChildren().addAll(memLeak, exitBtn, cancelBtn);
            //creates buttons and asks user if they would like to quit

            exitBtn.setOnAction(ev -> System.exit(0)); // quits without saving
            cancelBtn.setOnAction(ev -> {
                //this closes the small prompt and not the program
                confirmStage.close();
            });

            vbox2.getChildren().add(hbox9);

            Scene scene2 = new Scene(vbox2, 282, 112);
            confirmStage.setScene(scene2);
            confirmStage.showAndWait();
            //creates the exit prompt
        });
    }

    /**
     * This method loads the list of students and modules from the database, clears the existing lists and dropdown options, and updates everything
     * @param students is an ArrayList of students in the database
     * @param studentDropdown is a dropdown menu full of the students names in the database
     * @param listInput a TextArea that displays to the user
     */
    public void load(ArrayList<StudentModel> students, ComboBox<String> studentDropdown, TextArea listInput) {
        System.out.println("Loading students and modules");
        try {
            // Check that the database connection is valid
            if (conn == null || conn.isClosed()) {
                System.err.println("Error: Database connection is null or closed");
                return;
            }

            // Clear the existing lists and dropdown options
            students.clear();
            studentDropdown.getItems().clear();

            // Load the students and modules from the database into the ArrayLists and the student dropdown
            String sql = "SELECT * FROM students";
            ResultSet rs = conn.createStatement().executeQuery(sql);
            while (rs.next()) {
                // Create a new student object
                StudentModel student = new StudentModel(
                        rs.getString("name"),
                        rs.getString("studentNo"),
                        rs.getString("DOB"),
                        rs.getInt("semester")
                );
                students.add(student);

                // Add the student's name to the student dropdown
                studentDropdown.getItems().add(student.getName());
            }

            // Update the list of students in the GUI
            updateList(students, listInput);
        } catch (SQLException ex) {
            System.err.println("Error loading students from database: " + ex.getMessage());
            ex.printStackTrace();
        }
    }



}