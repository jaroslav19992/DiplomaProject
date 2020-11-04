package TestMaker.MainProgramWindow.Panes.TestsPane;

import java.util.ArrayList;

public class Classroom {
    private String classroomName;
    private ArrayList<Pupil> classPupils;

    public Classroom(String classroomName, ArrayList<Pupil> classPupils) {
        this.classroomName = classroomName;
        this.classPupils = classPupils;
    }

    public Classroom(String classroomName) {
        this.classroomName = classroomName;
        classPupils = new ArrayList<>();
    }

    public void addPupil(Pupil pupil) {
        classPupils.add(pupil);
    }

    public String getClassroomName() {
        return classroomName;
    }

    public void setClassroomName(String classroomName) {
        this.classroomName = classroomName;
    }

    public ArrayList<Pupil> getClassPupils() {
        return classPupils;
    }

    public void setClassPupils(ArrayList<Pupil> classPupils) {
        this.classPupils = classPupils;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("Classroom "+classroomName+":\n");
        for (Pupil pupil: classPupils) {
            stringBuilder.append("\t*"+pupil+"\n");
        }
        return stringBuilder.toString();
    }
}
