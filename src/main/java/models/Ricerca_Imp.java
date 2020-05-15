/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import data.DataItemImpl;

/**
 *
 * @author leonardo
 */
public class Ricerca_Imp extends DataItemImpl<Integer>implements Ricerca{

    private Canale canale;
    private Programma programma;
    private Genere genere;
    private Fascia fascia;

    public Ricerca_Imp(){
        super();
        canale = null;
        programma = null;
        genere = null;
        fascia = null;
    }

    public Canale getCanale(){
        return this.canale;
    }
    public void setCanale(Canale canale){
        this.canale = canale;
    }

    public Programma getProgramma(){
        return this.programma;
    }
    public void setProgramma(Programma programma){
        this.programma = programma;
    }

    public Genere getGenere(){
        return this.genere;
    }
    public void setGenere(Genere genere){
        this.genere = genere;
    }

    public Fascia getFascia(){
        return this.fascia;
    }
    public void setFascia(Fascia fascia){
        this.fascia = fascia;
    }
}
