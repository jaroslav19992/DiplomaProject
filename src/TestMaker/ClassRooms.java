package TestMaker;

import java.util.ArrayList;

public class ClassRooms {
    static final int NUMBER_OF_CLASSES = 12;
    public static ArrayList<String> classesLetters = new ArrayList<>();

    public static String[] getClassesList() {
        classesLetters.add("А");
        classesLetters.add("Б");
        classesLetters.add("В");
        classesLetters.add("Г");
        classesLetters.add("Д");

        String[] classRooms = new String[NUMBER_OF_CLASSES * classesLetters.size()];
        int currentClass = 1;
        for (int i = 0; i < NUMBER_OF_CLASSES; i++) {
            StringBuilder classRoom = new StringBuilder(currentClass + "-");
            for (int j = 0; j < classesLetters.size(); j++) {
                classRoom.append(classesLetters.get(j));
                classRooms[i*classesLetters.size()+j] = String.valueOf(classRoom);
                classRoom.delete(classRoom.length() - 1, classRoom.length());
            }
            currentClass++;
        }
        return classRooms;
    }
}
