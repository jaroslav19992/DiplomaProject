package TestMaker.MainProgramWindow.Panes.TestsPane.TeacherPane;

public class AccessedPupil {
    private final int usernameHash;
    private final String firstName;
    private final String lastName;

    public AccessedPupil(int usernameHash, String firstName, String lastName) {
        this.usernameHash = usernameHash;
        this.firstName = firstName;
        this.lastName = lastName;
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

    @Override
    public String toString() {
        return lastName + " " + firstName;
    }
}
