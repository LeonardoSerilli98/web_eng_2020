/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import data.DataItemImpl;
import java.util.List;

/**
 *
 * @author leonardo
 */
public class Stagione_Imp extends DataItemImpl<Integer> implements Stagione {
    
    private int numero;
    private Immagine immagine;
    private Programma programma;
    private List<Episodio> episodi;

    public Stagione_Imp(){
        super();
        numero = 0;
        immagine = null;
        programma = null;
        episodi = null;
    }
    
    public int getNumero(){
        return this.numero;
    }
    public void setNumero(int numero){
        this.numero = numero;
    }

    public Immagine getImmagine(){
        return this.immagine;
    }
    public void setImmagine(Immagine immagine){
        this.immagine = immagine;
    }

    public List<Episodio> getEpisodi(){
        return this.episodi;
    }
    public void setEpisodi(List<Episodio> episodi){
        this.episodi = episodi;
    }

    public Programma getProgramma() {
        return programma;
    }

    public void setProgramma(Programma programma) {
        this.programma = programma;
    }

   

}
