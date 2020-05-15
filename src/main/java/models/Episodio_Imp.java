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
public class Episodio_Imp extends DataItemImpl<Integer> implements Episodio {
    
    private int numero;
    private Stagione stagione;
    private Programma serie;
    
    public Episodio_Imp (){
        super();
        numero = 0;
        stagione = null;
        serie = null;
    }   

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public Stagione getStagione() {
        return stagione;
    }

    public void setStagione(Stagione stagione) {
        this.stagione = stagione;
    }

    public Programma getSerie() {
        return serie;
    }

    public void setSerie(Programma serie) {
        this.serie = serie;
    }
    
}
