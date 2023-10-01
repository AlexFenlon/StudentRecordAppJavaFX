package ie.mtu.oopassignment.controller;

import ie.mtu.oopassignment.model.Database;
import ie.mtu.oopassignment.model.StudentModel;
import javafx.scene.control.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Creates methods to do with the Student Model
 */
public class StudentController {

    private final Connection conn;
    private final MainController mainController = new MainController();

    public StudentController() throws SQLException, ClassNotFoundException {
        // Connect to the database
       conn = Database.getConnection();
        // Create the students table if it does not exist
        try {
            String sql = """
                    CREATE TABLE IF NOT EXISTS students (
                    name TEXT,
                    studentNo TEXT PRIMARY KEY,
                    DOB DATE,
                    semester INTEGER
                    );""";
            conn.createStatement().execute(sql);
        } catch (SQLException sqle) {
            System.out.println(sqle.getMessage());
        }
    }

    /**
     * This method adds restrictions to the name, only allowing a name with A - Z characters and no numbers
     * @param nameInput The input for the name of a Student or Module
     */
    public void nameLimit(TextField nameInput) {
        //this only allows letters to be entered as a name
        nameInput.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\sa-zA-Z*")) {
                nameInput.setText(newValue.replaceAll("[^\\sa-zA-Z]", ""));
            }
        });
    }

    /**
     * This method only allows R followed by 8 numbers to be entered.
     * @param studentNoInput is the input for the Student Number
     *
     */
    public void studentNoLimit(TextField studentNoInput) {
        //this only allows r followed by 8 numbers to be entered as a number
        studentNoInput.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[rR]?\\d{0,8}")) {
                studentNoInput.setText(oldValue);
            }
        });
    }

    /**
     * This method adds a new student to the database
     * @param addBtn is the Button that is pressed in view
     * @param nameInput is the input for the Student Name
     * @param studentNoInput is the input for the Student Number
     * @param dobInput is a date picker for the Students Date of Birth
     * @param semesterInput is an input for the students current semester
     * @param studentDropdown is a dropdown menu full of the students names in the database
     * @param deleteDropdown is a dropdown menu full of the students names in the database for deleting a student
     * @param students is an ArrayList of students in the database
     * @param listInput a TextArea that displays to the user
     */
    public void addStudent(Button addBtn, TextField nameInput, TextField studentNoInput, DatePicker dobInput, TextField semesterInput, ComboBox<String> studentDropdown, ComboBox<String> deleteDropdown, ArrayList<StudentModel> students, TextArea listInput) {
        addBtn.setOnAction(e -> {
            // Check if any of the input fields is empty
            if (nameInput.getText().isEmpty() || studentNoInput.getText().isEmpty() || dobInput.getValue() == null) {
                // Show an alert to the user
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Please enter a value for all fields");
                alert.showAndWait();
                return;
            }
            // gets name, student number and date of birth from user, adds it to the database, updates the list and clears input fields
            String name = nameInput.getText();
            String studentNo = studentNoInput.getText();
            LocalDate DOB = dobInput.getValue();
            int semester = Integer.parseInt(semesterInput.getText());

            // Check if student already exists in the database
            try {
                String sql = "SELECT COUNT(*) FROM students WHERE name = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, name);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    // Student already exists in the database, do not add again
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning");
                    alert.setHeaderText(null);
                    alert.setContentText("This student already exists in the database");
                    alert.showAndWait();
                    return;
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }

            // Insert student data into the database
            try {
                String sql = "INSERT INTO students(name, studentNo, DOB, semester) VALUES(?,?,?,?)";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, name);
                pstmt.setString(2, studentNo);
                pstmt.setString(3, String.valueOf(DOB));
                pstmt.setInt(4, semester);
                pstmt.executeUpdate();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }

            // Update the student dropdown with the new name if it doesn't already exist
            if (!studentDropdown.getItems().contains(name)) {
                studentDropdown.getItems().add(name);
            }

            // Update the list of students
            mainController.updateList(students, listInput);
            mainController.load(students,studentDropdown, listInput);
            mainController.load(students,deleteDropdown, listInput);


            // Clear input fields
            nameInput.clear();
            studentNoInput.clear();
            dobInput.setValue(LocalDate.now());
            semesterInput.clear();
        });
    }

    /**
     * @param removeBtn is the button that you press once you select the student in the dropdown to delete them and its associations
     * @param students is an ArrayList of students in the database
     * @param listInput a TextArea that displays to the user
     * @param deleteDropdown is a dropdown menu full of the students names in the database for deleting a student
     */
    public void removeStudent(Button removeBtn, ArrayList<StudentModel> students, TextArea listInput, ComboBox<String> deleteDropdown) {
        removeBtn.setOnAction(e -> {
            String selectedStudent = deleteDropdown.getValue();
            if (selectedStudent != null) {
                // Retrieve the corresponding student from the list of students
                StudentModel removedStudent = null;
                for (StudentModel student : students) {
                    if (student.getName().equals(selectedStudent)) {
                        removedStudent = student;
                        break;
                    }
                }
                if (removedStudent == null) {
                    return; // Student not found
                }
                String studentNo = removedStudent.getStudentNo();

                try {
                    // Delete the corresponding student from the database
                    String sql = "DELETE FROM students WHERE studentNo = ?";
                    PreparedStatement pstmt = conn.prepareStatement(sql);
                    pstmt.setString(1, studentNo);
                    pstmt.executeUpdate();

                    // Delete the corresponding modules from the database
                    sql = "DELETE FROM modules WHERE studentNo = ?";
                    pstmt = conn.prepareStatement(sql);
                    pstmt.setString(1, studentNo);
                    pstmt.executeUpdate();
                } catch (SQLException sqle) {
                    System.out.println(sqle.getMessage());
                }

                // Remove the corresponding student from the list of students and the student dropdown
                students.remove(removedStudent);
                deleteDropdown.getItems().remove(selectedStudent);

                // Update the list of students
                mainController.updateList(students, listInput);
            }
        });
    }
}

