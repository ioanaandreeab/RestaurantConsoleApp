package proj.clase;

import java.io.Serializable;
import java.util.*;

public class Comanda implements Serializable {
    private static final long serialVersionUID=333;
    private Client client;
    private double pret;
    private int discount;
    private HashMap<Produs, Integer> produse;
    private Date data;
    private ModPlata modPlata;

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

    public Comanda(Client client, HashMap<Produs, Integer> produse, int discount) {
        this.client = client;
        this.discount = discount;
        this.pret = 0;
        this.produse = produse;
        this.data = new Date();
        this.modPlata = null;
    }

    public void plaseazaComanda(ModPlata modPlata) {
        for (Map.Entry<Produs, Integer> produs : this.produse.entrySet()) {
            this.pret += produs.getKey().getPret()*produs.getValue();
        }
        this.pret -= this.pret * this.discount/100;
        this.modPlata = modPlata;
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
                ", Mod plata: " + modPlata +
                "\nData: " + data ;
    }
}
