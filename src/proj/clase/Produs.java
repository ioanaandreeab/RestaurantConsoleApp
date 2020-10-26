package proj.clase;

import java.io.Serializable;

public class Produs implements Serializable {
    private static final long serialVersionUID=555;
    private static int idHelper = 0;
    private int id;
    private String denumire;
    private int cantitate;
    private String unitateMasura;
    private double pret;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDenumire() {
        return denumire;
    }

    public void setDenumire(String denumire) {
        this.denumire = denumire;
    }

    public int getCantitate() {
        return cantitate;
    }

    public void setCantitate(int cantitate) {
        this.cantitate = cantitate;
    }

    public String getUnitateMasura() {
        return unitateMasura;
    }

    public void setUnitateMasura(String unitateMasura) {
        this.unitateMasura = unitateMasura;
    }

    public double getPret() {
        return pret;
    }

    public void setPret(double pret) {
        this.pret = pret;
    }

    public Produs(String denumire, int cantitate, String unitateMasura, double pret) {
        this.id = idHelper++;
        this.denumire = denumire;
        this.cantitate = cantitate;
        this.unitateMasura = unitateMasura;
        this.pret = pret;
    }

    @Override
    public String toString() {
        return denumire +
                ", " + cantitate +
                " " + unitateMasura +
                ", " + pret +
                " lei";
    }
}
