package proj.clase;

import java.io.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

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

    private HashMap<String, Integer> calculeazaVanzariTotalePerProdus() {
        HashMap<String, Integer> produseMap = new HashMap<>();
        for(Comanda comanda: comenzi) {
            for(Map.Entry<Produs, Integer> produs : comanda.getProduse().entrySet()) {
                if(!(produseMap.containsKey(produs.getKey().getDenumire()))) {
                    produseMap.put(produs.getKey().getDenumire(),produs.getValue());
                } else {
                    produseMap.put(produs.getKey().getDenumire(), produseMap.get(produs.getKey().getDenumire())+produs.getValue());
                }
            }
        }
        return produseMap;
    }

    public void genereazaRaportVanzariZiCurenta() {
        String[] produseVandute = new String[]{};
        String[] produseVanduteUpdated = null;
        double[] valoareTotalaProdus = new double[]{};
        double[] valoareTotalaProdusUpdated = null;
        int[] nrComenzi = new int[]{};
        int[] nrComenziUpdated = null;
        for (Comanda comanda : comenzi) {
            LocalDate currentDate = LocalDate.now();
            if(currentDate.getDayOfMonth() == comanda.getData().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getDayOfMonth()) {
                for(Map.Entry<Produs, Integer> produs : comanda.getProduse().entrySet()) {
                    if(Arrays.stream(produseVandute).noneMatch(item-> item.equals(produs.getKey().getDenumire()))) {
                        produseVanduteUpdated = Arrays.copyOf(produseVandute, produseVandute.length + 1);
                        produseVanduteUpdated[produseVandute.length] = produs.getKey().getDenumire();
                        produseVandute = produseVanduteUpdated;

                        valoareTotalaProdusUpdated = Arrays.copyOf(valoareTotalaProdus,valoareTotalaProdus.length + 1);
                        valoareTotalaProdusUpdated[valoareTotalaProdus.length] = produs.getKey().getPret() * produs.getValue();
                        valoareTotalaProdus = valoareTotalaProdusUpdated;

                        nrComenziUpdated = Arrays.copyOf(nrComenzi, nrComenzi.length + 1);
                        nrComenziUpdated[nrComenzi.length] = 1;
                        nrComenzi = nrComenziUpdated;
                    } else {
                        int index = Arrays.stream(produseVandute).collect(Collectors.toList()).indexOf(produs.getKey().getDenumire());
                        valoareTotalaProdus[index] += produs.getKey().getPret() * produs.getValue();
                        nrComenzi[index] += 1;
                    }
                }
            }
        }

        String today = LocalDate.now().getDayOfMonth()+"."+LocalDate.now().getMonth()+"."+LocalDate.now().getYear();

        if(produseVandute.length !=0) {

            try (BufferedWriter out = new BufferedWriter(new FileWriter("vanzari_" + today + ".txt"))) {
                out.write("======================== RAPORT VANZARI ZILNICE - " + today + " ========================");
                out.newLine();
                out.write("Denumire produs | Valoare vanzari | Nr. comenzi ");
                out.newLine();
                out.write("------------------------------------------------------------------------------------------");
                out.newLine();

                for (int i = 0; i < produseVandute.length; i++) {
                    out.write("\n"+produseVandute[i]+ " -------- " + valoareTotalaProdus[i] + " -------- " + nrComenzi[i]);
                }

                out.newLine();
                out.write("==========================================================================================");
                System.out.println("Raport generat cu succes in fisierul vanzari_"+ today +".txt");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Nu exista comenzi in aceasta zi.");
        }
    }

    private HashMap<String, Integer> sortHashMapByValue(HashMap<String, Integer> hm) {
        List<Map.Entry<String, Integer> > list = new LinkedList<Map.Entry<String, Integer> >(hm.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, Integer> >() {
            public int compare(Map.Entry<String, Integer> a,
                               Map.Entry<String, Integer> b)
            {
                return (b.getValue()).compareTo(a.getValue());
            }
        });
        HashMap<String, Integer> temp = new LinkedHashMap<String, Integer>();
        for (Map.Entry<String, Integer> item : list) {
            temp.put(item.getKey(), item.getValue());
        }
        return temp;
    }


    public void genereazaRaportProdusePopulare() {
        HashMap<String,Integer> produseMap = calculeazaVanzariTotalePerProdus();
        produseMap = sortHashMapByValue(produseMap);
        try (BufferedWriter out = new BufferedWriter(new FileWriter("produse_populare.txt"))) {
            out.write("============================= RAPORT PRODUSE POPULARE =============================");
            out.newLine();
            out.write("Denumire produs | Nr. produse comandate ");
            out.newLine();
            out.write("-----------------------------------------------------------------------------------");
            out.newLine();
            for(Map.Entry<String, Integer> produs : produseMap.entrySet()) {
                out.write("\n"+ produs.getKey() + "-------"+produs.getValue());
            }
            out.newLine();

            out.write("===================================================================================");
            System.out.println("Raport generat cu succes in fisierul produse_populare.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public double interogheazaVanzariZiCurenta() {
        double total = 0;
        for (Comanda comanda : comenzi) {
            LocalDate currentDate = LocalDate.now();
            if(currentDate.getDayOfMonth() == comanda.getData().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getDayOfMonth()) {
                total += comanda.getPret();
            }
        }
        return total;
    }

    public void produsPreferat() {
        HashMap<String, Integer> produseMap = calculeazaVanzariTotalePerProdus();
        Map.Entry<String,Integer> produsPreferat = Collections.max(produseMap.entrySet(), Map.Entry.comparingByValue());
        System.out.println("Produsul preferat al clientilor este " + produsPreferat.getKey() + " si a fost comandat de "+
                produsPreferat.getValue() + " ori.");
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

    public String[] getProduseComandateAzi() {
        String[] produseComandate = new String[]{};
        String[] updatedProducts = null;
        for(Comanda comanda: comenzi) {
            for(Map.Entry<Produs, Integer> produs : comanda.getProduse().entrySet()) {
                if(Arrays.stream(produseComandate).noneMatch(item->item.equals(produs.getKey().getDenumire()))) {
                    updatedProducts = Arrays.copyOf(produseComandate, produseComandate.length + 1);
                    updatedProducts[produseComandate.length] = produs.getKey().getDenumire();
                    produseComandate = updatedProducts;
                }
            }
        }
        return produseComandate;
    }

}
