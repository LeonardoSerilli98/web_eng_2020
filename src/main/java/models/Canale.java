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
public interface Canale {
    
    public int getKey(); 
    public void setKey(int key);

    public String getNome();
    public void setNome(String nome);
    
    public Immagine getImmagine();
    public void setImmagine(Immagine immagine);
    
}
