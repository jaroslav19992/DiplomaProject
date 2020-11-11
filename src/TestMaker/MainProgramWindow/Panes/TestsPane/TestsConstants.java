package TestMaker.MainProgramWindow.Panes.TestsPane;

public interface TestsConstants {
    String EVAL_SYSTEM_12 = "12-ти бальна система";
    String EVAL_SYSTEM_5 = "5-ти бальна система";
    String EVAL_SYSTEM_100 = "100-ти бальна система";

    String ONE_ANSWER = "Питання з однією відповіддю";
    String SEVERAL_ANSWERS = "Питання з кількома правильними варіантами відповіді";
    String COMPLIANCE_QUESTION = "Питання на відповідність";

    int DEFAULT_NUMBER_OF_VARIANTS = 2;

    double REMOVE_VARIANT_BUTTON_IMAGE_FIT_WIDTH = 20;
    double REMOVE_VARIANT_BUTTON_IMAGE_FIT_HEIGHT = 20;
    int TEXT_FIELD_PREF_WIDTH = 400;
    double hBOX_SPACING = 10.0;
    double TEXT_AREA_PREF_HEIGHT = 50;

    String CLOSE_WINDOW_CONFIRMATION_HEADER = "Ви дійсно бажаєте закрити поточне вікно?";
    String CLOSE_WINDOW_CONFIRMATION_CONTEXT = "Вся введена інформація буде втрачена";
    String CLOSE_WINDOW_CONFIRMATION_TITLE = "Підтвердіть дію";
    String QUESTION_ALERT_TITLE = "Оберіть дію";
    String QUESTION_ALERT_HEADER = "Питання тесту потребують уваги";
    String REMOVE_QUESTION_ALERT_CONTEXT = "Ви дійсно бажаєте видалити питання?";
    String NULLABLE_QUESTION_ALERT_CONTEXT = "Тест має одне або декілька не заповнених питань";
    String GLOBAL_MAX_SCORE_ALERT_CONTEXT = "Сума балів за кожне питання менша за максимальну кількість балів " +
            "відповідно до вибраної системи оцінювання. \nПеревірте кількості балів які можливо отримати за питання";
    String EMPTY_ANSWER_ALERT_CONTEXT = "Одне або декілька питань мають не заповнені відповіді";
    String EMPTY_QUESTION_TEXT_ALERT_CONTEXT = "Одне або декілька питань не мають тексту";
    String SINGLE_QUESTION_ALERT_CONTEXT = "Одне або декілька мають лише однин варіант відповіді";
    String DUPLICATES_VARIANTS_QUESTION_ALERT_CONTEXT = "Одне або декілька мають варіанти які дублюються";
    String DUPLICATES_ANSWERS_QUESTION_ALERT_CONTEXT = "Одне або декілька мають варіанти відповіді які дублюються";
}
