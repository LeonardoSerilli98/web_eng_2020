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
public class Canale_Imp extends DataItemImpl<Integer> implements Canale {
    
    private String nome;
    private Immagine immagine;
    private List<Palinsesto> palinsesti;
        
    public Canale_Imp(){
        super();
        nome = "";
        immagine = null;
        palinsesti = null;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Immagine getImmagine() {
        return immagine;
    }

    public void setImmagine(Immagine immagine) {
        this.immagine = immagine;
    }

    @Override
    public List<Palinsesto> getPalinsesti() {
        return palinsesti;
    }

    @Override
    public void setPalinsesti(List<Palinsesto> palinsesti) {
        this.palinsesti = palinsesti; 
    }
    
    
}
