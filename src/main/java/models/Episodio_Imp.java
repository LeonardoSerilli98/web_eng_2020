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
public class Episodio_Imp implements Episodio {
    
    private int key;
    private int numero;
    private Stagione stagione;
    private Programma serie;
    
    public Episodio_Imp (){
        key = 0;
        numero = 0;
        stagione = null;
        serie = null;
    }   

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
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
