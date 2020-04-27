package Tema.SGBD;
import javax.xml.parsers.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;

import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) throws FileNotFoundException
    {
        File xmlFile = new File("../RESULT.xml");

        StudentsXMLParser parser = new StudentsXMLParser();

        ArrayList users = parser.parseXml(new FileInputStream(xmlFile));

        System.out.println(users);
    }
}
