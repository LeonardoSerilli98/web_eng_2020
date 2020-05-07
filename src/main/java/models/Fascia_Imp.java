/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.sql.Time;

/**
 *
 * @author leonardo
 */
public class Fascia_Imp implements Fascia{
    
    private int key;
    private Time inizio;
    private Time fine;
    private String fascia;


    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
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
