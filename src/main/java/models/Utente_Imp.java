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
    private String token;
    private boolean preferenzaMail;
    private boolean notAuthFlag;
    private String UUID;

    public Utente_Imp(){    
        super();
        email = "";
        password = "";
        ricerca = null;
        preferenza = null;
        token = "";
        preferenzaMail = false;
        notAuthFlag = true;
        UUID = "";
        
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
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public boolean getPreferenzaMail() {
        return this.preferenzaMail;
    }

    @Override
    public void setPreferenzaMail(boolean preferenzaMail) {
        this.preferenzaMail = preferenzaMail;
    }

    @Override
    public boolean getNotAuthFlag() {
        return this.notAuthFlag;
    }

    @Override
    public void setNotAuthFlag(boolean notAuthFlag) {
        this.notAuthFlag = notAuthFlag;
    }

    @Override
    public String getUUID() {
        return this.UUID;
    }

    @Override
    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

}
