package TestMaker.MainProgramWindow.Panes.TestsPane;

public class Pupil {
    private final int usernameHash;
    private final String firstName;
    private final String lastName;
    private String classroom;

    public Pupil(int usernameHash, String firstName, String lastName) {
        this.usernameHash = usernameHash;
        this.firstName = firstName;
        this.lastName = lastName;
        this.classroom = "";
    }

    public Pupil(int usernameHash, String firstName, String lastName, String classroom) {
        this.usernameHash = usernameHash;
        this.firstName = firstName;
        this.lastName = lastName;
        this.classroom = classroom;
    }


    public int getUsernameHash() {
        return usernameHash;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    @Override
    public String toString() {
        return lastName + " " + firstName;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        Pupil pupilToCompare;
        try {
            pupilToCompare = (Pupil) obj;
        } catch (ClassCastException e) {
            return false;
        }
        return usernameHash == pupilToCompare.usernameHash &&
                (firstName.equals(pupilToCompare.firstName) || firstName.equals(pupilToCompare.getFirstName())) &&
                (lastName.equals(pupilToCompare.lastName) || lastName.equals(pupilToCompare.getLastName())) &&
                (classroom.equals(pupilToCompare.classroom) || lastName.equals(pupilToCompare.getClassroom()));
    }
}
