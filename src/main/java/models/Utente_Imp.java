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
public class Utente_Imp implements Utente{
    private int key;
    private String email;
    private String password;
    private Ricerca ricerca;
    private Preferenza preferenza;

    public int getKey(){
        return key;
    }
    public void setKey(int key){
        this.key = key;
    }

    public String getEmail(){
        return this.email;
    }
    public void setEmail(String email){
        this.email = email;
    }

    public Ricerca getRicerca(){
        return this.ricerca;
    }
    public void setRicerca(Ricerca ricerca){
        this.ricerca = ricerca;
    }

    public Preferenza getPreferenza(){
        return this.preferenza;
    }
    public void setPreferenza(Preferenza preferenza){
        this.preferenza = preferenza;
    }

    
}
