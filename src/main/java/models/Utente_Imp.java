/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import data.DataItemImpl;

/**
 *
 * @author leonardo
 */
public class Utente_Imp extends DataItemImpl<Integer>implements Utente{
    
    private String email;
    private String password;
    private Ricerca ricerca;
    private Preferenza preferenza;

    public Utente_Imp(){    
        super();
        email = "";
        password = "";
        ricerca = null;
        preferenza = null;
    }

    public String getEmail(){
        return this.email;
    }
    public void setEmail(String email){
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
