package Tema.SGBD;

import jdk.internal.org.xml.sax.ContentHandler;
import jdk.internal.org.xml.sax.InputSource;
import jdk.internal.org.xml.sax.XMLReader;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class StudentsXMLParser {
    public ArrayList parseXml(InputStream in)
    {

        ArrayList<Student> students = new ArrayList<Student>();
        try
        {
            StudentParserHandler handler = new StudentParserHandler();

            XMLReader parser = (XMLReader) XMLReaderFactory.createXMLReader();

            parser.setContentHandler((ContentHandler) handler);

            InputSource source = new InputSource(in);

            parser.parse(source);

            students = handler.getStudentsList();

        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (jdk.internal.org.xml.sax.SAXException e) {
            e.printStackTrace();
        }
        return students;
    }
}
