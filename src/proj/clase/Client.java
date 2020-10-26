package proj.clase;

import java.io.Serializable;

public class Client implements Serializable {
    private static final long serialVersionUID=444;
    private String nume;
    private String nrTelefon;
    private boolean cardFidelitate;

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getNrTelefon() {
        return nrTelefon;
    }

    public void setNrTelefon(String nrTelefon) {
        this.nrTelefon = nrTelefon;
    }

    public boolean isCardFidelitate() {
        return cardFidelitate;
    }

    public void setCardFidelitate(boolean cardFidelitate) {
        this.cardFidelitate = cardFidelitate;
    }

    public Client(String nume, String nrTelefon, boolean cardFidelitate) {
        this.nume = nume;
        this.nrTelefon = nrTelefon;
        this.cardFidelitate = cardFidelitate;
    }

    @Override
    public String toString() {
        return "Client: " +
                nume +
                " nr. telefon: " + nrTelefon;
    }
}
