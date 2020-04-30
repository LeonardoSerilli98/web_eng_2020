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
public interface Utente {
    
    public int getKey();
    public void setKey(int key);

    public String getEmail();
    public void setEmail(String email);

    public Ricerca getRicerca();
    public void setRicerca(Ricerca ricerca);

    public Preferenza getPreferenza();
    public void setPreferenza(Preferenza preferenza);
}
