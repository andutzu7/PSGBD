package Tema.SGBD;
/*
* Clientul isi transmite emailul si daca id-ul nu exista ii e creata lista. Apoi continua pana cand ramane fara intrebari,
* La final primeste scorul si
* */

import java.io.*;
import java.net.*;

class Main {

    public static void main(String args[])
            throws Exception
    {

        Socket s = new Socket("localhost", 888);

        DataOutputStream dos
                = new DataOutputStream(
                s.getOutputStream());

        BufferedReader br
                = new BufferedReader(
                new InputStreamReader(
                        s.getInputStream()));

        BufferedReader kb
                = new BufferedReader(
                new InputStreamReader(System.in));
        String str, str1;
        while (!(str = kb.readLine()).equals("exit")) {

            char[] string=new char[10000];
            dos.writeBytes(str + "\n");

            br.read(string);

            System.out.println(string);
        }

        dos.close();
        br.close();
        kb.close();
        s.close();

    }

}

//1:A75,A80,A77,A81,A74,A78, Raspunsuri: A75,A77,A78