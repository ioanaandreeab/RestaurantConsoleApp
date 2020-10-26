package proj.clase;

import proj.clase.Categorie;
import proj.clase.Comanda;
import proj.clase.Meniu;
import proj.clase.Produs;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Restaurant {
    private String nume;
    private Meniu[] meniuri;
    private List<Comanda> comenzi;

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public Meniu[] getMeniuri() {
        return meniuri;
    }

    public void setMeniuri(Meniu[] meniuri) {
        this.meniuri = meniuri;
    }

    public List<Comanda> getComenzi() {
        return comenzi;
    }

    public void setComenzi(List<Comanda> comenzi) {
        this.comenzi = comenzi;
    }

    public Restaurant(String nume) {
        this.nume = nume;
        this.meniuri = new Meniu[0];
        this.comenzi = new ArrayList<Comanda>();
    }

    public void incarcaComenziDinFisier(String path) {
        try {
            FileInputStream file = new FileInputStream(path);
            ObjectInputStream reader = new ObjectInputStream(file);
            while (true) {
                try {
                    Comanda comanda = (Comanda) reader.readObject();
                    System.out.println(comanda);
                    adaugaComanda(comanda);
                } catch (EOFException e) {
                    break;
                }
            }
        } catch (Exception e) {
           setComenzi(new ArrayList<>());
        }
    }

    public void incarcaMeniuDinFisier(String path) {
        Meniu meniu;
        if(path.contains("meniu_bauturi.txt")) {
            meniu = new Meniu("Bauturi");
        } else {
            meniu = new Meniu("Mancare");
        }

        Scanner in = null;
        Categorie categorieCurenta = null;
        List<Categorie> categorii = new ArrayList<>();
        try {
            in = new Scanner(new FileReader(path));

            while(in.hasNextLine()) {
                String line = in.nextLine();
                if(line.contains("Categorie:")){
                    categorieCurenta = new Categorie(line.split(":")[1]);
                    categorii.add(categorieCurenta);
                } else {
                    String denumireProdus = line.split(",")[0];
                    int cantitateProdus = Integer.valueOf(line.split(",")[1]);
                    String unitateMasura = line.split(",")[2];
                    double pret = Double.valueOf(line.split(",")[3]);
                    Produs produs = new Produs(denumireProdus,cantitateProdus,unitateMasura,pret);
                    try {
                        categorieCurenta.adaugaProdus(produs);
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }
            }
            in.close();

            if(categorii.size() != 0) {
                for (Categorie categorie : categorii) {
                    meniu.adaugaCategorie(categorie);
                }
                adaugaMeniu(meniu);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void salveazaComenziInFisier(String path) {
        try {
            FileOutputStream fileOut = new FileOutputStream(path);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            for(Comanda comanda : this.comenzi) {
                objectOut.writeObject(comanda);
            }
            objectOut.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public Meniu getMeniu(String tipMeniu) {
        Meniu meniuCautat = null;
        for(int i=0;i<this.meniuri.length;i++) {
            if(this.meniuri[i].getTipMeniu().equals(tipMeniu)) {
                meniuCautat = this.meniuri[i];
            }
        }
        return meniuCautat;
    }

    public void adaugaComanda(Comanda comanda) {
        this.comenzi.add(comanda);
    }

    public void adaugaMeniu(Meniu meniu) {
        Meniu[] meniuriFinale = new Meniu[this.meniuri.length + 1];
        for(int i=0;i<this.meniuri.length;i++) {
            meniuriFinale[i] = this.meniuri[i];
        }
        meniuriFinale[this.meniuri.length] = meniu;
        setMeniuri(meniuriFinale);
    }
}
