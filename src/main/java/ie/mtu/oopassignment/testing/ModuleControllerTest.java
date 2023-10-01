package ie.mtu.oopassignment.testing;

import ie.mtu.oopassignment.controller.ModuleController;
import javafx.scene.control.TextField;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ModuleControllerTest {

    private static ModuleController moduleController;

    @Before
    public static void setUp() throws Exception {
        moduleController = new ModuleController();
    }

    @Test
    public void testSemesterLimit() {
        TextField semesterInput = new TextField();
        moduleController.semesterLimit(semesterInput);

        // Test with invalid input
        semesterInput.setText("a");
        assertEquals("", semesterInput.getText());

        // Test with valid input
        semesterInput.setText("1");
        assertEquals("1", semesterInput.getText());

        // Test with valid input
        semesterInput.setText("2");
        assertEquals("2", semesterInput.getText());
    }
}
