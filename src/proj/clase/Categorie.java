package proj.clase;

import java.util.ArrayList;
import java.util.List;

public class Categorie {
    private String nume;
    private List<Produs> produse;

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public List<Produs> getProduse() {
        return produse;
    }

    public void setProduse(List<Produs> produse) {
        this.produse = produse;
    }

    public Categorie(String nume) {
        this.nume = nume;
        this.produse = new ArrayList<Produs>();
    }

    public void adaugaProdus(Produs produs) {
        this.produse.add(produs);
    }

    public void afisareProduse() {
        System.out.println("\tCategoria "+this.nume + " contine: ");
        for(Produs produs : this.produse) {
           System.out.println("\t\t " +produs.toString());
        }
    }
}
