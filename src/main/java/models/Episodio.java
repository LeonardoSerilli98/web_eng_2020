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
public interface Episodio extends DataItem<Integer>{
    
    public int getNumero();
    public void setNumero(int numero);

    public Stagione getStagione();
    public void setStagione(Stagione stagione);

    public Programma getSerie();
    public void setSerie(Programma serie);
    
    public String getTitolo();
    public void setTitolo(String titolo);
    
}
