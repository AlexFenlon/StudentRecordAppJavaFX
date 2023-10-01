package ie.mtu.oopassignment.model;

public class ModuleModel {
    private final String name;
    private final String crn;
    private final int semester;
    private final int grade;

    public ModuleModel(String name, String crn,int semester, int grade) {
        this.name = name;
        this.crn = crn;
        this.semester = semester;
        this.grade = grade;
    }

    @Override
    public String toString() {
        return name + "\nCode: " + crn + "\nSemester: " + semester + "\nGrade: " + grade;
    }
}
