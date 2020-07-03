/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import data.DataItemImpl;
import java.sql.Date;
import java.sql.Time;

/**
 *
 * @author leonardo
 */
public class Ricerca_Imp extends DataItemImpl<Integer>implements Ricerca{

    private Canale canale;
    private Programma programma;
    private Genere genere;
    private Fascia fascia;
    private Time inizioMin;
    private Time inizioMax;
    private Date data;
    private String titolo;

    public Ricerca_Imp(){
        super();
        canale = null;
        programma = null;
        genere = null;
        fascia = null;
        inizioMin = null;
        inizioMax = null;
        data = null;
        titolo = "";
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

    @Override
    public Time getInizioMin() {
        return this.inizioMin;
    }

    @Override
    public void setInizioMin(Time inizioMin) {
        this.inizioMin = inizioMin;
    }

    @Override
    public Time getInizioMax() {
        return this.inizioMax;
    }

    @Override
    public void setInizioMax(Time inizioMax) {
        this.inizioMax = inizioMax;
    }

    @Override
    public Date getData() {
        return this.data;
    }

    @Override
    public void setData(Date data) {
        this.data = data;
    }

    @Override
    public String getTitolo() {
        return this.titolo;
    }

    @Override
    public void setTitolo(String titolo) {
        this.titolo= titolo;
    }
}
