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

            dos.writeBytes(str + "\n");

            str1 = br.readLine();

            System.out.println(str1);
        }

        dos.close();
        br.close();
        kb.close();
        s.close();
    }
}

//1:A75,A80,A77,A81,A74,A78, Raspunsuri: A75,A77,A78