package proj.exceptii;

public class ExceptieNrTelefon extends IllegalArgumentException{
    private String mesaj;

    public String getMesaj(){
        return this.mesaj;
    }

    public void setMesaj(String mesaj) {
        this.mesaj = mesaj;
    }

    public ExceptieNrTelefon(String mesaj) {
        super();
        this.mesaj = mesaj;
    }
}
