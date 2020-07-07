/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import data.DataException;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Canale;
import models.Palinsesto;
import models.Preferenza;
import models.Ricerca;
import models.Utente;

/**
 *
 * @author leonardo
 */
public class SomeDailyJob implements Runnable {

    @Override
    public void run() {
        try {
         deleteOldPalinsesti();
         sendDailyMail();
        } catch (DataException ex) {
            Logger.getLogger(SomeDailyJob.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void deleteOldPalinsesti() throws DataException {

        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
        String ThreeDaysAgo = sdf.format(java.util.Date.from(LocalDateTime.now().minusDays(3).atZone(ZoneId.systemDefault()).toInstant()));

        List<Palinsesto> palinsesti = Database.getDatalayer().getPalinsestoDAO().getAll();
        for (Palinsesto p : palinsesti) {
            if (p.getData().before(Date.valueOf(ThreeDaysAgo))) {
                Database.getDatalayer().getPalinsestoDAO().delete(p);
            }
        }

    }

    public void sendDailyMail() throws DataException {

        List<Utente> utenti = Database.getDatalayer().getUtenteDAO().getUtentiMailAbilitate();
        
      
        if (utenti != null) {
            for (Utente u : utenti) {
                System.out.println(u.getEmail());
                if (u.getRicerca() != null) {
                    Ricerca r = Database.getDatalayer().getRicercaDAO().read(u.getRicerca().getKey());
                    List<Palinsesto> palinsestiByRicerca = Database.getDatalayer().getPalinsestoDAO().ricerca(r);
                    System.out.println("palinsesti by ricerca salvata");
                    printPalinsesti(palinsestiByRicerca);
                }

                List<Palinsesto> palinsestiByPreferenza = new ArrayList();
                Preferenza p = u.getPreferenza();
                for (Canale c : p.getCanali()) {
                    for (Palinsesto pal : Database.getDatalayer().getPalinsestoDAO().getPalinsestiByCanale(c, p.getFascia())) {
                        palinsestiByPreferenza.add(pal);
                    }
                }

                System.out.println("palinsesti by preferenza");
                printPalinsesti(palinsestiByPreferenza);

            }
        }

    }

    public void printPalinsesti(List<Palinsesto> palinsesti) {
        for (Palinsesto p : palinsesti) {
            
            System.out.println("_____________________________________________");
            System.out.println("inizio: " + p.getInizio());
            System.out.println("fine: " + p.getFine());
            System.out.println("data: " + p.getData());
            System.out.println("programma: " + p.getProgramma().getNome());
            System.out.println("episodio: " + p.getEpisodio().getStagione().getNumero() + "x" + p.getEpisodio().getNumero());
            System.out.println("canale: " + p.getCanale().getNome());
            System.out.println("fascia: " + p.getFascia().getFascia());
            System.out.println("_______________________________________");

        }

    }
}
