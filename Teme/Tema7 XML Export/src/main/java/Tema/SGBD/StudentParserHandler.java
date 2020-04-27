package Tema.SGBD;

import java.util.ArrayList;
import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class StudentParserHandler extends DefaultHandler {

    private ArrayList studentsList = new ArrayList();

    private Stack elementStack = new Stack();

    private Stack objectStack = new Stack();

    public void startDocument() throws SAXException {
        System.out.println("Beginning: ");
    }

    public void endDocument() throws SAXException {
        System.out.println("Ending ");
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        elementStack.push(qName);

        if ("STUDENT".equals(qName)) {
            //New User instance
            Student s = new Student();

            if ( attributes != null && attributes.getLength() == 1)
            {
                s.setNume(attributes.getValue(0));
                s.setInfo(attributes.getValue(1));
                s.setObiect(attributes.getValue(2));
            }
            objectStack.push(s);
        }
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        //Remove last added  element
        this.elementStack.pop();

        //User instance has been constructed so pop it from object stack and push in userList
        if ("STUDENT".equals(qName)) {
            Student object = (Student) objectStack.pop();
            studentsList.add(object);
        }
    }


    public void characters(char[] ch, int start, int length) throws SAXException {
        String value = new String(ch, start, length).trim();

        if (value.length() == 0) {
            return;
        }

        if ("NUME".equals(currentElement())) {
            Student s = (Student) this.objectStack.peek();
            s.setNume(value);
        } else if ("INFO".equals(currentElement())) {
            Student s = (Student) this.objectStack.peek();
            s.setInfo(value);
        } else if("OBIECT".equals(currentElement())) {
            Student s = (Student) this.objectStack.peek();
            s.setObiect(value);
        }
    }


    private String currentElement() {
        return (String)elementStack.peek();
    }

    //Accessor for userList object
    public ArrayList getStudentsList() {
        return studentsList;
    }
}