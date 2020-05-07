/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

/**
 *
 * @author leonardo
 */
public class Ricerca_Imp implements Ricerca{
    private int key;
    private Canale canale;
    private Programma programma;
    private Genere genere;
    private Fascia fascia;

    public int getKey(){
        return this.key;
    }
    public void setKey(int key){
        this.key = key;
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
