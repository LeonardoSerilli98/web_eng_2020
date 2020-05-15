/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import data.DataItemImpl;
import java.sql.Time;
import java.util.Date;

/**
 *
 * @author leonardo
 */
public class Palinsesto_Imp extends DataItemImpl<Integer>implements Palinsesto {
    
    private Programma programma;
    private Canale canale;
    private Time inizio;
    private Time fine;
    private Fascia fascia;
    private Date data;
    private Episodio episodio;

    public Palinsesto_Imp() {
        super();
        programma = null;
        canale = null;
        inizio = null;
        fine = null;
        fascia = null;
        data = null;
        episodio = null;
    }


    public Programma getProgramma() {
        return programma;
    }

    public void setProgramma(Programma programma) {
        this.programma = programma;
    }

    public Canale getCanale() {
        return canale;
    }

    public void setCanale(Canale canale) {
        this.canale = canale;
    }

    public Time getInizio() {
        return inizio;
    }

    public void setInizio(Time inizio) {
        this.inizio = inizio;
    }

    public Time getFine() {
        return fine;
    }

    public void setFine(Time fine) {
        this.fine = fine;
    }

    public Fascia getFascia() {
        return fascia;
    }

    public void setFascia(Fascia fascia) {
        this.fascia = fascia;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Episodio getEpisodio() {
        return episodio;
    }

    public void setEpisodio(Episodio episodio) {
        this.episodio = episodio;
    }
    
    
    
}
