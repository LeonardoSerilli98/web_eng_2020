/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import data.DataItem;

/**
 *
 * @author leonardo
 */
public interface Immagine extends DataItem<Integer>{

    public String getNome();
    public void setNome(String nome);

    public String getTipo();
    public void setTipo(String tipo);

    public long getTaglia();
    public void setTaglia(long taglia);
    
    public Stagione getStagione();
    public void setStagione(Stagione stagione);
    
    public Programma getProgramma();
    public void setProgramma(Programma programma);
    
    
    
}
