package TestMaker;

import TestMaker.MainProgramWindow.Panes.TestsPane.Question;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class DOMxmlParser {
    private static final String ROOT_XML_TAG = "Test";
    private static final String QUESTION_XML_TAG = "Question";
    private static final String QUESTION_TYPE = "questionType";
    private static final String QUESTION_TEXT = "questionText";
    private static final String TEST_NAME = "testName";
    private static final String IS_RETESTING_ALLOWED = "isRetestingAllowed";
    private static final String TIME_LIMIT = "timeLimit";
    private static final String EV_SYSTEM = "evaluationSystem";
    private static final String NUMBER_OF_QUESTIONS = "numberOfQuestions";
    private static final String VARIANT_ID = "id";
    private static final String VARIANT_TEXT = "text";
    private static final String ANSWER_VARIANTS = "answerVariants";
    private static final String CORRECT_ANSWERS = "answerVariants";

//    private static final String TEST_NAME = "testName";

    private final File xmlFile;
    private final Document document;

    public DOMxmlParser(File xmlFile) throws ParserConfigurationException, IOException, SAXException {
        this.xmlFile = xmlFile;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        //Now we have access to all xml tags in file
        document = builder.parse(xmlFile);
    }

    public ArrayList<Question> getQuestionsList() {
        ArrayList<Question> questionsList = new ArrayList<>();
        //Get XML root child nodes with tag "Question"
        NodeList questionsNodeList = document.getDocumentElement().getElementsByTagName(QUESTION_XML_TAG);

        //for each question create Question object
        for (int i = 0; i < questionsNodeList.getLength(); i++) {
            questionsList.add(getQuestion(questionsNodeList.item(i)));
        }
        return questionsList;
    }

    private Question getQuestion(Node node) {
        Question question = new Question();
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element)node;
            String questionText = element.getAttribute(QUESTION_TEXT);
            String questionType = element.getAttribute(QUESTION_TYPE);

            ArrayList<String> answerVariants = getVariants(element, ANSWER_VARIANTS);
            ArrayList<String> correctVariants = getVariants(element, CORRECT_ANSWERS);

            question.setQuestionText(questionText);
            question.setQuestionType(questionType);
            question.setAnswerVariants(answerVariants);
            question.setSeveralCorrectAnswers(correctVariants);
        }
        return question;
    }

    /**
     * get all variants from variants list with tag "tagName" and create arraylist from them.
     * @param element element with several variants lists
     * @param tagName variants list tag
     * @return list of variants from xml
     */
    private ArrayList<String> getVariants(Element element, String tagName) {
        NodeList nodeList = element.getElementsByTagName(tagName).item(0).getChildNodes();
        ArrayList<String> variants = new ArrayList<>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            if (nodeList.item(i).getNodeType() == Node.ELEMENT_NODE) {
                variants.add(nodeList.item(i).getAttributes().getNamedItem(VARIANT_TEXT).getNodeValue());
            }
        }
        return variants;
    }

    private static String getTag(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = nodeList.item(0);
        return node.getAttributes().getNamedItem("text").toString();
    }

    public String getTestName() {
        return document.getDocumentElement().getElementsByTagName(TEST_NAME).item(0).getTextContent();
    }

    public int getAmountOfQuestions() {
        return Integer.parseInt(document.getDocumentElement().getElementsByTagName(NUMBER_OF_QUESTIONS).item(0).getTextContent());
    }

    public boolean isRetestingAllowed() {
        return Boolean.parseBoolean(document.getDocumentElement().getElementsByTagName(IS_RETESTING_ALLOWED).item(0).getTextContent());
    }

    public String getTestEVSystem() {
        return document.getDocumentElement().getElementsByTagName(EV_SYSTEM).item(0).getTextContent();
    }

}
