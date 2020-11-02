package proj.clase;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Meniu {
    private String tipMeniu;
    private List<Categorie> categorii;

    public String getTipMeniu() {
        return tipMeniu;
    }

    public void setTipMeniu(String tipMeniu) {
        this.tipMeniu = tipMeniu;
    }

    public List<Categorie> getCategorii() {
        return categorii;
    }

    public void setCategorii(List<Categorie> categorii) {
        this.categorii = categorii;
    }

    public Meniu(String tipMeniu) {
        this.tipMeniu = tipMeniu;
        this.categorii = new ArrayList<Categorie>();
    }

    public void adaugaCategorie(Categorie categorie) {
        this.categorii.add(categorie);
    }

    public void afisareMeniu() {
        System.out.println("Meniul "+this.tipMeniu + " contine: ");
        for(Categorie categorie : this.categorii) {
            categorie.afisareProduse();
        }
    }

    public List<Produs> getProdusePeCategorie(String numeCategorie) {
        Categorie categorieCautata = this.categorii.stream().filter(categorie -> categorie.getNume() == numeCategorie).collect(Collectors.toList()).get(0);
        return categorieCautata.getProduse();
    }
}
