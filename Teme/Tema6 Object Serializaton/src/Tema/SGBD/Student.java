package Tema.SGBD;

public class Student implements java.io.Serializable {
    public String nume;
    public String prenume;
    public String numarMatricol;
    public int an;
    public String grupa;

    @Override
    public String toString() {
        return "Student{" +
                "nume='" + nume + '\'' +
                ", prenume='" + prenume + '\'' +
                ", numarMatricol='" + numarMatricol + '\'' +
                ", an=" + an +
                ", grupa='" + grupa + '\'' +
                '}';
    }
}

