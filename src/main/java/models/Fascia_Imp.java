/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import data.DataItemImpl;
import java.sql.Time;

/**
 *
 * @author leonardo
 */
public class Fascia_Imp extends DataItemImpl<Integer> implements Fascia{
    
    private Time inizio;
    private Time fine;
    private String fascia;
    
    public Fascia_Imp(){
        super();
        inizio = null;
        fine = null;
        fascia = "";
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

    public String getFascia() {
        return fascia;
    }

    public void setFascia(String fascia) {
        this.fascia = fascia;
    }
}
