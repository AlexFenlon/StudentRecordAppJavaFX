package ie.mtu.oopassignment.controller;

import ie.mtu.oopassignment.model.Database;
import ie.mtu.oopassignment.model.ModuleModel;
import ie.mtu.oopassignment.model.StudentModel;
import javafx.scene.control.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Creates methods to do with the Module Model
 */
public class ModuleController {
    private final Connection conn;
    MainController mainController = new MainController();
        public ModuleController() throws SQLException, ClassNotFoundException {
            // Connect to the database
            conn = Database.getConnection();
            // Create the modules table if it does not exist
            try {
                // SQL statement to create the modules table
                String sql = """
                        CREATE TABLE IF NOT EXISTS modules (
                        moduleName TEXT,
                        CRN TEXT,
                        semester INTEGER,
                        grade TEXT,
                        studentNo TEXT,
                        FOREIGN KEY (studentNo) REFERENCES students(studentNo)
                        );""";


                // Execute the SQL statement
                conn.createStatement().execute(sql);
            } catch (SQLException sqle) {
                // Print the error message to the console
                System.out.println(sqle.getMessage());
            }
        }

    /**
     * This method limits the semester input to only allow 1 or 2.
     * @param semesterInput is an input for the students current semester
     */
            public void semesterLimit(TextField semesterInput) {
        //this only allows the user to enter 1 or 2
        semesterInput.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[1-2]?")) {
                semesterInput.setText(oldValue);
            }
        });
    }

    /**
     * This method only allows M and 3 digits, it auto sets M and the user just needs to enter 3 numbers and allows it to be cleared and added an M back to it
     * @param crnInput is an input the module code
     */
    public static void crnLimit(TextField crnInput) {
        // This regex matches an optional "M" at the start, followed by up to 3 digits
        String regex = "M?\\d{0,3}";

        crnInput.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches(regex)) {
                // If the new value doesn't match the desired pattern, revert to the old value
                crnInput.setText(oldValue);
            } else if (newValue.length() > 4) {
                // If the new value has more than 4 characters, truncate it to the first 4 characters
                crnInput.setText(newValue.substring(0, 4));
            }
        });

        // Set the initial text of the text field to "M"
        crnInput.setText("M");

        // When the CRN input field loses focus, check if it's empty and add back the "M" if needed
        crnInput.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                String text = crnInput.getText();
                if (!text.startsWith("M")) {
                    crnInput.setText("M" + text);
                }
            }
        });
    }


    /**
     * This method creates a limit for the gradeInput, it allows 0 to 100 to be entered as well as NP, if NP is typed, it gives "Not Complete"
     * @param gradeInput is the input for the module grade
     */
    public static void gradeLimit(TextField gradeInput) {
        gradeInput.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("(?i)NP|[0-9]{0,3}")) { // (?i) makes the regex case-insensitive
                // if the new value doesn't match the desired pattern, revert to the old value
                gradeInput.setText(oldValue);
            } else if (newValue.equals("NP")) {
                // if "NP" is entered, change it to "Not Complete"
                gradeInput.setText("Not Complete");
            } else {
                // convert the new value to an integer
                int newGrade = Integer.parseInt(newValue);

                // make sure the new value is between 0 and 100
                if (newGrade < 0 || newGrade > 100) {
                    // if the new value is not between 0 and 100, revert to the old value
                    gradeInput.setText(oldValue);
                }
            }
        });
    }


    /**
     * This method adds module information to the selected student to the database and displays it on the 3rd tab
     * @param addModuleBtn is the button that is in the view
     * @param studentDropdown is a dropdown menu full of the students names in the database
     * @param moduleInput is the input for the module name
     * @param crnInput is an input the module code
     * @param semesterModuleInput is the semester inptut for modules only
     * @param gradeInput is the input for the module grade
     * @param students is an ArrayList of students in the database
     * @param listInput a TextArea that displays to the user
     */
    //event handler for adding modules
    public void moduleButton(Button addModuleBtn, ComboBox<String> studentDropdown, TextField moduleInput, TextField crnInput, TextField semesterModuleInput, TextField gradeInput, ArrayList<StudentModel> students, TextArea listInput) {
        addModuleBtn.setOnAction(e -> {
            // Check if any of the input fields is empty
            if (studentDropdown.getSelectionModel().isEmpty() || moduleInput.getText().isEmpty() || crnInput.getText().isEmpty() || semesterModuleInput.getText().isEmpty() || gradeInput.getText().isEmpty()) {
                // Show an alert to the user
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Please enter a value for all fields");
                alert.showAndWait();
                return;
            }

            // Get the currently selected student from the studentDropdown
            String selectedStudentName = studentDropdown.getSelectionModel().getSelectedItem();
            StudentModel selectedStudent = null;
            for (StudentModel student : students) {
                if (student.getName().equals(selectedStudentName)) {
                    selectedStudent = student;
                    break;
                }
            }

            // If the selected student is not found, add a new student
            if (selectedStudent == null) {
                StudentModel newStudent = new StudentModel();
                students.add(newStudent);

                // Update the list of students
                mainController.updateList(students, listInput);

                // Select the new student in the ComboBox
                studentDropdown.getSelectionModel().select(selectedStudentName);

                // Update the selected student variable
                selectedStudent = newStudent;
            }

// Get the module, crn, semester, and grade from the input fields
            String module = moduleInput.getText();
            String crn = crnInput.getText();
            int semester = 0;
            String grade = gradeInput.getText();

// Parse the semesterInput as an integer if it's not empty
            if (!semesterModuleInput.getText().isEmpty()) {
                semester = Integer.parseInt(semesterModuleInput.getText());
            }

            // Add the module and grade to the selected student's list of modules
            ModuleModel moduledata = new ModuleModel(module, crn, semester, 0);
            selectedStudent.getModules().add(String.valueOf(moduledata));

            // Add the module to the database
            try {
                String sql = "INSERT INTO modules (moduleName, CRN, semester, grade, studentNo) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, module);
                pstmt.setString(2, crn);
                pstmt.setInt(3, semester);
                pstmt.setString(4, grade); // Set the grade to the parsed grade
                pstmt.setString(5, selectedStudent.getStudentNo()); // Set the studentNo to the selected student's studentNo

                pstmt.executeUpdate();
            } catch (SQLException sqle) {
                // Print the error message to the console
                System.out.println(sqle.getMessage());
            }

            // Clear the input fields
            mainController.updateList(students,listInput);
            moduleInput.clear();
            crnInput.clear();
            semesterModuleInput.clear();
            gradeInput.clear();

        });
    }


    /**
     * This method changes the listInput list of all the students and their associated modules and only shows the modules that the student has passed. It uses a flag to toggle between showing all the modules and only the passed ones.
     * @param students is an ArrayList of students in the database
     * @param listInput a TextArea that displays to the user
     * @param displayPassed is the button that the user presses to show the passed modules
     */
    public void DisplayPassedButton(ArrayList<StudentModel> students, TextArea listInput, Button displayPassed) {
        final boolean[] showPassed = {false}; // Initialize flag

        displayPassed.setOnAction(e -> {
            // Clear the list input
            listInput.clear();

            // Toggle the flag
            showPassed[0] = !showPassed[0];

            // Update the button text based on the flag
            if (showPassed[0]) {
                displayPassed.setText("Display All");
            } else {
                displayPassed.setText("Display Passed");
            }

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

                    // Loop through the results and add each module to the list input if the student passed the module
                    while (rs.next()) {
                        String moduleName = rs.getString("moduleName");
                        String crn = rs.getString("CRN");
                        int semester = rs.getInt("semester");
                        int grade = rs.getInt("grade");
                        if (!showPassed[0] || grade >= 40) {
                            listInput.appendText(moduleName + " (" + crn + ") - Semester " + semester + " - Grade: " + grade + "\n");
                        }
                    }

                    // Close the result set and statement
                    rs.close();
                    pstmt.close();

                } catch (SQLException sqle) {
                    System.out.println(sqle.getMessage());
                }

                // Add an extra line break between students
                listInput.appendText("\n");
            }

            // Add an extra line break between students
            listInput.appendText("\n");
        });
    }
}
