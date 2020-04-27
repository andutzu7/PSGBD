package Tema.SGBD;

public class Student
{
    @Override
    public String toString() {
        return "Student{" +
                "nume='" + nume + '\'' +
                ", info='" + info + '\'' +
                ", obiect='" + obiect + '\'' +
                '}';
    }

    private String nume;
    private String info;
    private String obiect;

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getObiect() {
        return obiect;
    }

    public void setObiect(String obiect) {
        this.obiect = obiect;
    }
}