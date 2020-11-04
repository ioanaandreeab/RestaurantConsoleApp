package proj.main;
import de.codeshelf.consoleui.elements.ConfirmChoice;
import de.codeshelf.consoleui.prompt.*;
import de.codeshelf.consoleui.prompt.builder.ListPromptBuilder;
import de.codeshelf.consoleui.prompt.builder.PromptBuilder;
import jline.TerminalFactory;
import org.fusesource.jansi.AnsiConsole;
import proj.clase.ModPlata;
import proj.clase.*;
import proj.exceptii.ExceptiePersonalizata;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static org.fusesource.jansi.Ansi.ansi;


public class Main {
    private static final String MENIU_BAUTURI_ABSOLUTE_PATH = "C:\\Users\\ioana\\Desktop\\Master\\An1\\PPOO\\proj\\src\\meniu_bauturi.txt";
    private static final String MENIU_MANCARE_ABSOLUTE_PATH = "C:\\Users\\ioana\\Desktop\\Master\\An1\\PPOO\\proj\\src\\meniu_mancare.txt";
    private static final String COMENZI_ABSOLUTE_PATH = "C:\\Users\\ioana\\Desktop\\Master\\An1\\PPOO\\proj\\src\\comenzi.dat";
    private static final int VALOARE_DISCOUNT_CARD_FIDELITATE = 10;

    public static void main(String[] args) {
        //produsele comandate de clientul curent
        HashMap<Produs, Integer> produseMap;
        //initializare librarie
        AnsiConsole.systemInstall();

        //restaurant, meniu si comenzi curente
        Restaurant restaurant = new Restaurant("Shift");
        Meniu meniuCurent;
        //incarca comenzi curente in fisier
        restaurant.incarcaComenziDinFisier(COMENZI_ABSOLUTE_PATH);
        List<Comanda> comenziCurente = restaurant.getComenzi();

        //incarca meniuri din fisier
        restaurant.incarcaMeniuDinFisier(MENIU_BAUTURI_ABSOLUTE_PATH);
        restaurant.incarcaMeniuDinFisier(MENIU_MANCARE_ABSOLUTE_PATH);

        System.out.println(ansi().eraseScreen().render("Bine ati venit la restaurantul " + restaurant.getNume()));

        while (true) {
            try {
                ConsolePrompt prompt = new ConsolePrompt();
                PromptBuilder promptBuilder = prompt.getPromptBuilder();

                //meniu
                ListPromptBuilder listPromptBuilder = promptBuilder.createListPrompt();
                listPromptBuilder.name("actiontype")
                        .message("Ce actiune doriti sa realizati?")
                        .newItem().text("Plaseaza comanda").add()
                        .newItem().text("Vizualizare statistici").add()
                        .newItem().text("Creare rapoarte").add()
                        .newItem().text("Paraseste aplicatia").add()
                        .addPrompt();

                HashMap<String, ? extends PromtResultItemIF> result = prompt.prompt(promptBuilder.build());

                ListResult choiceMade = (ListResult) result.get("actiontype");

                switch (choiceMade.getSelectedId()) {
                    case "Plaseaza comanda":
                        produseMap = new HashMap<>();

                        boolean adaugaProduse = true;
                        while (adaugaProduse) {
                            ConsolePrompt promptMenu = new ConsolePrompt();
                            PromptBuilder promptBuilderMenu = prompt.getPromptBuilder();
                            listPromptBuilder = promptBuilderMenu.createListPrompt();

                            //alegere tip meniu
                            listPromptBuilder.name("menutype").message("Alegeti meniul dorit");
                            for (int i = 0; i < restaurant.getMeniuri().length; i++) {
                                listPromptBuilder.newItem().text(restaurant.getMeniuri()[i].getTipMeniu()).add();
                            }
                            listPromptBuilder.addPrompt();
                            result = promptMenu.prompt(promptBuilderMenu.build());
                            choiceMade = (ListResult) result.get("menutype");

                            if (choiceMade.getSelectedId().equals("Mancare")) {
                                //afisare meniu
                                meniuCurent = restaurant.getMeniu("Mancare");
                                restaurant.getMeniu("Mancare").afisareMeniu();
                            } else {
                                meniuCurent = restaurant.getMeniu("Bauturi");
                                restaurant.getMeniu("Bauturi").afisareMeniu();
                            }

                            //alege categoria
                            ConsolePrompt promptCategory = new ConsolePrompt();
                            PromptBuilder promptBuilderCategory = prompt.getPromptBuilder();
                            listPromptBuilder = promptBuilderCategory.createListPrompt();
                            listPromptBuilder.name("categorytype").message("Alegeti categoria dorita");
                            for (int i = 0; i < meniuCurent.getCategorii().size(); i++) {
                                listPromptBuilder.newItem().text(meniuCurent.getCategorii().get(i).getNume()).add();
                            }
                            listPromptBuilder.addPrompt();
                            result = promptCategory.prompt(promptBuilderCategory.build());
                            choiceMade = (ListResult) result.get("categorytype");

                            //alege produsul
                            ConsolePrompt promptProduct = new ConsolePrompt();
                            PromptBuilder promptBuilderProduct = prompt.getPromptBuilder();
                            ListPromptBuilder productListPromptBuilder = promptBuilderProduct.createListPrompt();
                            productListPromptBuilder.name("producttype").message("Alegeti produsul dorit");
                            List<Produs> products = meniuCurent.getProdusePeCategorie(choiceMade.getSelectedId());
                            for (int i = 0; i < products.size(); i++) {
                                productListPromptBuilder.newItem(String.valueOf(products.get(i).getId())).text(products.get(i).toString()).add();
                            }
                            productListPromptBuilder.addPrompt();
                            result = promptProduct.prompt(promptBuilderProduct.build());
                            ListResult productChosen = (ListResult) result.get("producttype");
                            Produs produsAles = products.stream().filter(x -> x.getId() == Integer.valueOf(productChosen.getSelectedId())).collect(Collectors.toList()).get(0);

                            //introdu cantitatea
                            boolean cantitateValida = false;
                            String cantitate = null;
                            while(!cantitateValida) {
                                ConsolePrompt promptClient = new ConsolePrompt();
                                PromptBuilder promptBuilderClient = promptClient.getPromptBuilder();
                                promptBuilderClient.createInputPrompt().name("cantitate").message("Introduceti cantitatea: ").addPrompt();
                                result = promptClient.prompt(promptBuilderClient.build());
                                cantitate = ((InputResult) result.get("cantitate")).getInput();

                                if(cantitate != null) {
                                    if (!(cantitate.equals("null")) && (cantitate.matches("-?(0|[1-9]\\d*)"))) {
                                        cantitateValida = true;
                                    }
                                } else {
                                    try {
                                        throw new ExceptiePersonalizata("Trebuie sa introduceti o valoare numerica pentru cantitate");
                                    } catch (ExceptiePersonalizata e) {
                                        System.out.println(e.getMesaj());
                                    }
                                }
                            }

                            produseMap.put(produsAles, Integer.valueOf(cantitate));

                            //continua
                            ConsolePrompt promptContinua = new ConsolePrompt();
                            PromptBuilder promptBuilderContinua = promptContinua.getPromptBuilder();
                            promptBuilderContinua.createConfirmPromp().name("continua").message("Doriti un nou produs?").defaultValue(ConfirmChoice.ConfirmationValue.YES).addPrompt();
                            result = promptContinua.prompt(promptBuilderContinua.build());
                            ConfirmResult continuaComanda = (ConfirmResult) result.get("continua");
                            String confirm = continuaComanda.getConfirmed().toString();
                            if (confirm.equals("NO")) {
                                adaugaProduse = false;
                            }
                        }

                        //introduceti nume
                        boolean nrTelefonValid = false;
                        boolean numeValid = false;
                        String nume = null;
                        while(!numeValid) {
                            ConsolePrompt promptClient = new ConsolePrompt();
                            PromptBuilder promptBuilderClient = promptClient.getPromptBuilder();
                            promptBuilderClient.createInputPrompt().name("name").message("Introduceti numele: ").addPrompt();
                            result = promptClient.prompt(promptBuilderClient.build());
                            nume = ((InputResult) result.get("name")).getInput();

                            if(nume != null) {
                                if(nume.length() > 3) {
                                    numeValid = true;
                                }
                            }
                            else try {
                                throw new ExceptiePersonalizata("Numele trebuie sa contina cel putin 3 caractere");
                            } catch (ExceptiePersonalizata e) {
                                System.out.println(e.getMesaj());
                            }
                        }

                        //introduceti telefon
                        String telefon = null;
                        while (!nrTelefonValid) {
                            ConsolePrompt promptTelefon = new ConsolePrompt();
                            PromptBuilder promptBuilderTelefon = promptTelefon.getPromptBuilder();
                            promptBuilderTelefon.createInputPrompt().name("phone").message("Introduceti nr. de telefon: ").addPrompt();
                            result = promptTelefon.prompt(promptBuilderTelefon.build());
                            telefon = ((InputResult) result.get("phone")).getInput();
                            if(telefon != null) {
                                if(telefon.charAt(0) == '0' && telefon.length() == 10) {
                                    nrTelefonValid = true;
                                }
                            }
                            else try {
                                throw new ExceptiePersonalizata("Nr. de telefon trebuie sa inceapa cu caracterul 0 si sa aiba 10 caractere");
                            } catch (ExceptiePersonalizata e) {
                                System.out.println(e.getMesaj());
                            }
                        }

                        //card de fidelitate
                        boolean hasCardFidelitate = false;
                        ConsolePrompt promptFidelitate = new ConsolePrompt();
                        PromptBuilder promptBuilderFidelitate = promptFidelitate.getPromptBuilder();
                        promptBuilderFidelitate.createConfirmPromp().name("fidelitate").message("Card de fidelitate?").defaultValue(ConfirmChoice.ConfirmationValue.YES).addPrompt();
                        result = promptFidelitate.prompt(promptBuilderFidelitate.build());
                        ConfirmResult cardFidelitateResult = (ConfirmResult) result.get("fidelitate");
                        String cardFidelitate = cardFidelitateResult.getConfirmed().toString();
                        if(cardFidelitate.equals("YES")) {
                            hasCardFidelitate = true;
                        }

                        //metoda plata
                        ModPlata modPlataAles = null;
                        ConsolePrompt promptPlata = new ConsolePrompt();
                        PromptBuilder promptBuilderPlata = promptPlata.getPromptBuilder();
                        ListPromptBuilder listPlata = promptBuilderPlata.createListPrompt();
                        listPlata.name("paytype").message("Alegeti statistica dorita");
                        for(ModPlata modPlata : ModPlata.values()) {
                            listPlata.newItem().text(modPlata.toString()).add();
                        }
                        listPlata.addPrompt();
                        result = promptPlata.prompt(promptBuilderPlata.build());
                        choiceMade = (ListResult) result.get("paytype");
                        modPlataAles = ModPlata.valueOf(choiceMade.getSelectedId());

                        //comanda
                        ConsolePrompt promptComanda = new ConsolePrompt();
                        PromptBuilder promptBuilderComanda = promptComanda.getPromptBuilder();
                        promptBuilderComanda.createConfirmPromp().name("comanda").message("Plasati comanda?").defaultValue(ConfirmChoice.ConfirmationValue.YES).addPrompt();
                        result = promptComanda.prompt(promptBuilderComanda.build());
                        ConfirmResult proceseazaComanda = (ConfirmResult) result.get("comanda");
                        String confirmComanda = proceseazaComanda.getConfirmed().toString();

                        if (confirmComanda.equals("YES")) {
                            Client clientCurent = new Client(nume, telefon, hasCardFidelitate);
                            int discount = 0;
                            if(hasCardFidelitate) {
                                discount = VALOARE_DISCOUNT_CARD_FIDELITATE;
                                System.out.println("Comanda dvs. beneficiaza de o reducere de " + VALOARE_DISCOUNT_CARD_FIDELITATE + " %.");
                            }
                            Comanda comanda = new Comanda(clientCurent, produseMap, discount);
                            comanda.plaseazaComanda(modPlataAles);
                            comenziCurente.add(comanda);
                            System.out.println("Comanda plasata!\n" + comanda.toString());
                        } else {
                            System.out.println("Ati renuntat la comanda");
                        }

                        break;

                    case "Vizualizare statistici":
                        ConsolePrompt promptStats = new ConsolePrompt();
                        PromptBuilder promptBuilderStats = promptStats.getPromptBuilder();
                        ListPromptBuilder listStats = promptBuilderStats.createListPrompt();
                        listStats.name("stattype").message("Alegeti statistica dorita");
                        listStats.newItem().text("Vanzare totala curenta").add()
                        .newItem().text("Produs preferat").add()
                        .newItem().text("Produse comandate astazi").add();
                        listStats.addPrompt();
                        result = promptStats.prompt(promptBuilderStats.build());
                        choiceMade = (ListResult) result.get("stattype");
                        switch (choiceMade.getSelectedId()) {
                            case "Vanzare totala curenta":
                                System.out.println("In ziua curenta s-a incasat un total de "+restaurant.interogheazaVanzariZiCurenta()+ " lei");
                                break;
                            case "Produs preferat":
                                restaurant.produsPreferat();
                                break;
                            case "Produse comandate astazi":
                                String[] produseComandate = restaurant.getProduseComandateAzi();
                                try {
                                    for(String produs : produseComandate) {
                                        System.out.println("\n"+produs);
                                    }
                                } catch (NullPointerException e) {
                                    System.out.println("Nu au fost comandate inca produse astazi");
                                }
                                break;
                        }
                        break;
                    case "Creare rapoarte":
                        ConsolePrompt promptReports = new ConsolePrompt();
                        PromptBuilder promptBuilderReports = promptReports.getPromptBuilder();
                        ListPromptBuilder listReports = promptBuilderReports.createListPrompt();
                        listReports.name("reporttype").message("Alegeti raportul pe care doriti sa-l generati");
                        listReports.newItem().text("Vanzari zi curenta").add()
                                .newItem().text("Cele mai populare produse").add();
                        listReports.addPrompt();
                        result = promptReports.prompt(promptBuilderReports.build());
                        choiceMade = (ListResult) result.get("reporttype");
                        if(choiceMade.getSelectedId().equals("Vanzari zi curenta")) {
                            restaurant.genereazaRaportVanzariZiCurenta();
                        } else {
                            restaurant.genereazaRaportProdusePopulare();
                        }
                        break;
                    case "Paraseste aplicatia":
                        restaurant.setComenzi(comenziCurente);
                        restaurant.salveazaComenziInFisier(COMENZI_ABSOLUTE_PATH);
                        System.out.println("O zi frumoasa!");
                        System.exit(0);
                        break;
                    default:
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    TerminalFactory.get().restore();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
