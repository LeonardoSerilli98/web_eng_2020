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
    
    int getKey(); 
    void setKey(int key);

    String getNome();
    void setNome(String nome);
    
    Immagine getImmagine();
    void setImmagine(Immagine immagine);
    

    
}
