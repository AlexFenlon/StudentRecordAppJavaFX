package ie.mtu.oopassignment.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class StudentModel implements Serializable {
    private String name;
    private String studentNo;
    private String DOB;
    private int semester;
    private final List<String> modules;

    public StudentModel(String name, String studentNo, String DOB, int semester) {
        this.name = name;
        this.studentNo = studentNo;
        this.DOB = DOB;
        this.semester = semester;
        this.modules = new ArrayList<>();
    }

    public StudentModel() {
        this.modules = new ArrayList<>();
    }

    public  String getName() {
        return name;
    }

    public String getStudentNo() {
        return studentNo;
    }

    public String getDOB() {
        return DOB;
    }

     public int getSemester() {
        return semester;
    }

    public List<String> getModules() {
        return modules;
    }


    @Override
    public String toString() {
        String moduleList = String.join("\n", modules);
        return name + "\t" + studentNo + "\t" + DOB + "\nModules: \n" + moduleList + "\n";
    }

}
