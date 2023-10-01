package ie.mtu.oopassignment.view;

import ie.mtu.oopassignment.controller.MainController;
import ie.mtu.oopassignment.controller.ModuleController;
import ie.mtu.oopassignment.controller.StudentController;
import ie.mtu.oopassignment.model.StudentModel;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;


public class StudentRecordsView {
    public Scene createUI(MainController mainController, StudentController studentController, ModuleController moduleController, Stage stage) {
// sets up arraylist of students and allows use of methods in the method class
        final ArrayList<StudentModel> students=new ArrayList<>();
        stage.setTitle("Student Records App");



        // Create TabPane
        TabPane tabPane=new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        // Create Add Student Tab
        Tab addStudentTab = new Tab("Add Student");
        addStudentTab.setTooltip(new Tooltip("This is for the user to add a student and their details"));

        HBox hbox1 = new HBox(5);
        TextField nameInput = new TextField();
        Tooltip nameTip = new Tooltip("Enter your name here!");
        Tooltip.install(nameInput, nameTip);
        hbox1.getChildren().addAll(new Label("Enter your Name: \t \t\t"), nameInput);
        studentController.nameLimit(nameInput);

        HBox hbox2 = new HBox(5);
        TextField studentNoInput = new TextField();
        Tooltip studentNoTip = new Tooltip("Enter R followed by 8 numbers");
        Tooltip.install(studentNoInput, studentNoTip);
        hbox2.getChildren().addAll(new Label("Enter Student Number: \t\t"), studentNoInput);
        studentController.studentNoLimit(studentNoInput);

        HBox hbox3 = new HBox(5);
        DatePicker dobInput = new DatePicker();
        Tooltip dobTip = new Tooltip("DD/MM/YYYY");
        Tooltip.install(dobInput, dobTip);
        hbox3.getChildren().addAll(new Label("Enter your Date of Birth: \t\t"), dobInput);

        HBox hbox35 = new HBox(5);
        TextField semesterInput = new TextField();
        Tooltip semesterTip = new Tooltip("Enter a 1 for Sept - Dec and 2 for Jan - May");
        Tooltip.install(semesterInput, semesterTip);
        hbox35.getChildren().addAll(new Label("Enter your Current Semester: \t"), semesterInput);
        moduleController.semesterLimit(semesterInput);

        HBox hbox4 = new HBox(5);
        Button addBtn = new Button("Add");
        Tooltip addTip = new Tooltip("Enter your details and click add to add it to the list");
        Tooltip.install(addBtn, addTip);
        hbox4.getChildren().addAll(addBtn);

        Label help = new Label("Hover over any input or button for help!");
        HBox helpHbox = new HBox(5);
        helpHbox.getChildren().add(help);

        VBox vbox = new VBox(5);
        vbox.getChildren().addAll(hbox1, hbox2, hbox3, hbox35, hbox4, helpHbox);
        addStudentTab.setContent(vbox);

        // Create Module Tab
        Tab addModuleTab = new Tab("Add Modules");
        addModuleTab.setTooltip(new Tooltip("This is for the user to select a student and add modules to them"));

        HBox hbox5 = new HBox(5);
        ComboBox<String> studentDropdown = new ComboBox<>();
        studentDropdown.setTooltip(new Tooltip("Select a Student"));

        TextField moduleInput = new TextField();
        Tooltip moduleTip = new Tooltip("Enter Module name here!");
        Tooltip.install(moduleInput, moduleTip);
        hbox5.getChildren().addAll(new Label("Enter the Module Name: \t"), moduleInput);
        studentController.nameLimit(moduleInput);

        HBox hbox55 = new HBox(5);
        TextField semesterModuleInput = new TextField();
        Tooltip semesterModuleTip = new Tooltip("Enter a 1 for Sept - Dec and 2 for Jan - May");
        Tooltip.install(semesterModuleInput, semesterModuleTip);
        hbox55.getChildren().addAll(new Label("Enter the Module Semester: \t"), semesterModuleInput);
        moduleController.semesterLimit(semesterModuleInput);

        HBox hbox6 = new HBox(5);
        TextField crnInput = new TextField();
        Tooltip crnTip = new Tooltip("Enter M and 3 numbers");
        Tooltip.install(crnInput, crnTip);
        hbox6.getChildren().addAll(new Label("Enter the Module Code: \t\t"), crnInput);
        ModuleController.crnLimit(crnInput);

        HBox hbox66 = new HBox(5);
        TextField gradeInput = new TextField();
        Tooltip gradeTip = new Tooltip("Enter a number between 0 - 100 or NP for not pass");
        Tooltip.install(gradeInput, gradeTip);
        hbox66.getChildren().addAll(new Label("Enter the Module Grade: \t"), gradeInput);
        ModuleController.gradeLimit(gradeInput);

        HBox hbox7 = new HBox(5);
        Button addModuleBtn = new Button("Add Module to Student");
        Tooltip addModuleTip = new Tooltip("Enter your details and click add to add it to the list on the right tab");
        Tooltip.install(addModuleBtn, addModuleTip);

        hbox7.getChildren().addAll(addModuleBtn);


        VBox vbox2 = new VBox(5);
        vbox2.getChildren().addAll(studentDropdown, hbox5, hbox6, hbox55,hbox66, hbox7);

        // add the VBox to the Tab
        addModuleTab.setContent(vbox2);

        // Create Student List Tab
        Tab studentListTab=new Tab("Student List");
        HBox studentListHbox = new HBox(5);
        studentListTab.setTooltip(new Tooltip("This is a list of all the students and modules associated with them"));

        Button removeBtn=new Button("Remove");
        Tooltip remove=new Tooltip("Highlight the student you want to remove and click the remove button");
        Tooltip.install(removeBtn,remove);

        ComboBox<String> deleteDropdown = new ComboBox<>();

        HBox hboxEmpty = new HBox(5);

        Button displayPassed = new Button("Show Students with passed Modules");
        Tooltip displayTip=new Tooltip("Shows the passed Modules of Students");
        Tooltip.install(removeBtn,displayTip);

        TextArea listInput = new TextArea();
        listInput.setEditable(false);
        VBox studentListVbox=new VBox(5);
        studentListHbox.getChildren().addAll(removeBtn,deleteDropdown,hboxEmpty,displayPassed);
        studentListVbox.getChildren().addAll(studentListHbox,listInput);
        studentListTab.setContent(studentListVbox);

        // Add all tabs to the tab pane
        tabPane.getTabs().addAll(addStudentTab,addModuleTab,studentListTab);

        // Create scene and add menu bar and tab pane
        Scene scene=new Scene(new VBox(),430,330);
        ((VBox)scene.getRoot()).getChildren().addAll(tabPane);

        // set up actions
        mainController.load(students, studentDropdown, listInput);
        mainController.load(students,deleteDropdown, listInput);
        studentController.addStudent(addBtn, nameInput, studentNoInput, dobInput,semesterInput, studentDropdown,deleteDropdown,students, listInput);
        studentController.removeStudent(removeBtn, students,listInput,deleteDropdown);
        moduleController.DisplayPassedButton(students,listInput,displayPassed);
        moduleController.moduleButton(addModuleBtn,studentDropdown,moduleInput,crnInput,semesterModuleInput,gradeInput,students,listInput);
        mainController.exitButton(stage);

        return scene;
    }
}

