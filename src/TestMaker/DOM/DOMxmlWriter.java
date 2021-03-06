package TestMaker.DOM;

import TestMaker.MainProgramWindow.Panes.TestsPane.Question;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static TestMaker.DOM.DOMConstants.*;

public class DOMxmlWriter {

    private final Document document;

    public DOMxmlWriter(ArrayList<Question>
                                questionsList, String testName,
                        int numberOfQuestions, int numberOfTestingAttempts,
                        int timeLimit, int evaluationSystem) throws ParserConfigurationException, IOException, SAXException, TransformerConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        //Now we have access to all xml tags in file
        document = builder.newDocument();
        Element rootElement = document.createElement(ROOT_XML_TAG);
        document.appendChild(rootElement);

        //Add test properties elements to xml
        Element testNameElement = document.createElement(TEST_NAME_TAG);
        testNameElement.setTextContent(testName);
        rootElement.appendChild(testNameElement);

        Element numberOfQuestionsElement = document.createElement(NUMBER_OF_QUESTIONS_TAG);
        numberOfQuestionsElement.setTextContent(String.valueOf(numberOfQuestions));
        rootElement.appendChild(numberOfQuestionsElement);

        Element numberOfAttempts = document.createElement(NUMBER_OF_ATTEMPTS);
        numberOfAttempts.setTextContent(String.valueOf(numberOfTestingAttempts));
        rootElement.appendChild(numberOfAttempts);

        Element timeLimitElement = document.createElement(TIME_LIMIT_TAG);
        timeLimitElement.setTextContent(String.valueOf(timeLimit));
        rootElement.appendChild(timeLimitElement);

        Element evaluationSystemElement = document.createElement(EV_SYSTEM_TAG);
        evaluationSystemElement.setTextContent(String.valueOf(evaluationSystem));
        rootElement.appendChild(evaluationSystemElement);

        //Create questions elements in xml
        Element questions = document.createElement(QUESTIONS_XML_TAG);
        for (Question question : questionsList) {
            questions.appendChild(createQuestion(question));
        }
        rootElement.appendChild(questions);
    }

    /**
     * Creates question element with all it's child from Question exemplar
     *
     * @param question Question class exemplar
     * @return question element
     */
    private Element createQuestion(Question question) {
        //Create question element
        Element questionElement = document.createElement(QUESTION_XML_TAG);
        //Set question attributes
        questionElement.setAttribute(QUESTION_TYPE_TAG, question.getQuestionType());
        questionElement.setAttribute(QUESTION_TEXT_TAG, question.getQuestionText());
        questionElement.setAttribute(QUESTION_SCORE_TAG, String.valueOf(question.getQuestionScore()));
        //Create child question elements for answer variants
        Element answerVariants = document.createElement(ANSWER_VARIANTS_TAG);
        Element correctAnswers = document.createElement(CORRECT_ANSWERS_TAG);
        questionElement.appendChild(answerVariants);
        questionElement.appendChild(correctAnswers);
        //Fill elements for question answers with questions
        ArrayList<String> questionsArrayList = question.getQuestionVariants();
        for (String questionString : questionsArrayList) {
            Element variant = document.createElement(VARIANT_TAG);
            variant.setAttribute(VARIANT_TEXT_TAG, questionString);
            answerVariants.appendChild(variant);
        }
        //Fill elements for question answers with answers
        ArrayList<String> answerVariantsList = question.getAnswerVariants();
        for (String answerString : answerVariantsList) {
            Element variant = document.createElement(VARIANT_TAG);
            variant.setAttribute(VARIANT_TEXT_TAG, answerString);
            correctAnswers.appendChild(variant);
        }
        return questionElement;
    }

    /**
     * Creates test file from early created document
     *
     * @param testFileName desired question name
     * @return created testFile
     * @throws TransformerException
     */
    public File getTestFile(String testFileName) throws TransformerException {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(document);

            File tempTestFile = File.createTempFile(testFileName, "xml");
            tempTestFile.deleteOnExit();

            StreamResult console = new StreamResult(System.out);
            StreamResult testFileStream = new StreamResult(tempTestFile);
            transformer.transform(source, console);
            transformer.transform(source, testFileStream);
            return tempTestFile;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}