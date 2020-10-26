package proj.clase;

import java.io.Serializable;
import java.util.*;

public class Comanda implements Serializable {
    private static int nrComanda;
    private Client client;
    private double pret;
    private int discount;
    private HashMap<Produs, Integer> produse;
    private Date data;

    public static int getNrComanda() {
        return nrComanda;
    }

    public static void setNrComanda(int nrComanda) {
        Comanda.nrComanda = nrComanda;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public double getPret() {
        return pret;
    }

    public void setPret(double pret) {
        this.pret = pret;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public HashMap<Produs, Integer> getProduse() {
        return produse;
    }

    public void setProduse(HashMap<Produs, Integer> produse) {
        this.produse = produse;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Comanda(Client client, HashMap<Produs, Integer> produse) {
        nrComanda++;
        this.client = client;
        this.discount = 0;
        this.produse = produse;
        for (Map.Entry<Produs, Integer> produs : this.produse.entrySet()) {
            this.pret += produs.getKey().getPret()*produs.getValue();
        }
        this.data = new Date();
    }


    @Override
    public String toString() {
        List<String> stringProduseComandate = new ArrayList<>(this.produse.size());
        for (Map.Entry<Produs, Integer> produs : this.produse.entrySet()) {
          stringProduseComandate.add("\nProdus : " + produs.getKey() + ", Cantitate: " + produs.getValue());
        }
        return "Comanda: " +
                "\n"+ client.toString() +
                "Produse:" + stringProduseComandate +
                ", Pret:" + pret +
                ", Discount:" + discount +
                "\nData: " + data ;
    }
}
