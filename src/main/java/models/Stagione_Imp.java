/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.util.List;

/**
 *
 * @author leonardo
 */
public class Stagione_Imp implements Stagione {
    
    private int key;
    private int numero;
    private Immagine immagine;
    private List<Episodio> episodi;

    public int getKey(){
        return this.key;
    }
    public void setKey(int key){
        this.key = key;
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

   

}
