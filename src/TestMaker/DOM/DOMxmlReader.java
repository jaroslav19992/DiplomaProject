package TestMaker.DOM;

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
import static TestMaker.DOM.DOMConstants.*;

public class DOMxmlReader {

    private final Document document;

    public DOMxmlReader(File xmlFile) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        //Now we have access to all xml tags in file
        document = builder.parse(xmlFile);
    }

    /**
     * Creates array from child elements in "Questions" element
     * @return array list of questions
     */
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

    /**
     * Gets question info from xml and creates an question
     * @param node node with question info in xml file
     * @return question
     */
    private Question getQuestion(Node node) {
        Question question = new Question();
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element questionElement = (Element)node;
            String questionText = questionElement.getAttribute(QUESTION_TEXT_TAG);
            String questionType = questionElement.getAttribute(QUESTION_TYPE_TAG);
            double questionScore = Double.parseDouble(questionElement.getAttribute(QUESTION_SCORE_TAG));

            ArrayList<String> answerVariants = getVariants(questionElement, ANSWER_VARIANTS_TAG);
            ArrayList<String> correctVariants = getVariants(questionElement, CORRECT_ANSWERS_TAG);

            question.setQuestionText(questionText);
            question.setQuestionType(questionType);
            question.setQuestionVariants(answerVariants);
            question.setAnswerVariants(correctVariants);
            question.setQuestionScore(questionScore);
        }
        return question;
    }

    /**
     * get all variants from variants list with tag "tagName" and create arraylist from them.
     * @param element question element in xml
     * @param tagName variants list tag
     * @return list of variants from xml
     */
    private ArrayList<String> getVariants(Element element, String tagName) {
        NodeList nodeList = element.getElementsByTagName(tagName).item(0).getChildNodes();
        ArrayList<String> variants = new ArrayList<>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            if (nodeList.item(i).getNodeType() == Node.ELEMENT_NODE) {
                variants.add(nodeList.item(i).getAttributes().getNamedItem(VARIANT_TEXT_TAG).getNodeValue());
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
        return document.getDocumentElement().getElementsByTagName(TEST_NAME_TAG).item(0).getTextContent();
    }

    public String getTimeLimit() {
        return document.getDocumentElement().getElementsByTagName(TIME_LIMIT_TAG).item(0).getTextContent();
    }

    public int getAmountOfQuestions() {
        return Integer.parseInt(document.getDocumentElement().getElementsByTagName(NUMBER_OF_QUESTIONS_TAG).item(0).getTextContent());
    }

    public int getNumberOfAttempts() {
        return Integer.parseInt(document.getDocumentElement().getElementsByTagName(NUMBER_OF_ATTEMPTS).item(0).getTextContent());
    }

    public String getTestEVSystem() {
        return document.getDocumentElement().getElementsByTagName(EV_SYSTEM_TAG).item(0).getTextContent();
    }

}
