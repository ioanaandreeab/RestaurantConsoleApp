package proj.exceptii;

public class ExceptiePersonalizata extends IllegalArgumentException{
    private String mesaj;

    public String getMesaj(){
        return this.mesaj;
    }

    public void setMesaj(String mesaj) {
        this.mesaj = mesaj;
    }

    public ExceptiePersonalizata(String mesaj) {
        super();
        this.mesaj = mesaj;
    }
}
