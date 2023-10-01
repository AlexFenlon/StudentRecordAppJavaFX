package ie.mtu.oopassignment.testing;

import ie.mtu.oopassignment.controller.StudentController;
import ie.mtu.oopassignment.model.StudentModel;
import javafx.scene.control.*;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class StudentControllerTest {

    // StudentController object to be used in the test cases
    private StudentController sc;

    // Create a new StudentController object before each test case
    @Before
    public void setUp() {
        try {
            sc = new StudentController();
        } catch (Exception e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }
    }

    // Test the nameLimit method with a TextField containing invalid characters
    @Test
    public void testInvalidNameInput() {
        TextField nameInput = new TextField();
        nameInput.setText("Jam35 M4RT3R");
        sc.nameLimit(nameInput);
        assertEquals("", nameInput.getText());
    }

    // Test the nameLimit method with a TextField containing valid characters
    @Test
    public void testValidNameInput() {
        TextField nameInput = new TextField();
        nameInput.setText("James Marter");
        sc.nameLimit(nameInput);
        assertEquals("James Marter", nameInput.getText());
    }

    // Test the studentNoLimit method with a TextField containing an invalid student number
    @Test
    public void testInvalidStudentNoInput() {
        TextField studentNoInput = new TextField();
        studentNoInput.setText("R0034567890");
        sc.studentNoLimit(studentNoInput);
        assertEquals("", studentNoInput.getText());
    }

    // Test the studentNoLimit method with a TextField containing a valid student number
    @Test
    public void testValidStudentNoInput() {
        TextField studentNoInput = new TextField();
        studentNoInput.setText("R12345678");
        sc.studentNoLimit(studentNoInput);
        assertEquals("R12345678", studentNoInput.getText());
    }

    // Test the addStudent method with valid input
    @Test
    public void testAddStudentValidInput() {
        Button addBtn = new Button();
        TextField nameInput = new TextField();
        nameInput.setText("Jack Fyves");
        TextField studentNoInput = new TextField();
        studentNoInput.setText("R00345678");
        DatePicker dobInput = new DatePicker();
        dobInput.setValue(LocalDate.of(2000, 1, 1));
        TextField semesterInput = new TextField();
        semesterInput.setText("1");
        ComboBox<String> studentDropdown = new ComboBox<>();
        ComboBox<String> deleteDropdown = new ComboBox<>();
        ArrayList<StudentModel> students = new ArrayList<>();
        TextArea listInput = new TextArea();
        sc.addStudent(addBtn, nameInput, studentNoInput, dobInput, semesterInput, studentDropdown, deleteDropdown, students, listInput);
        assertEquals(1, students.size());
    }

}