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
public interface Utente extends DataItem<Integer>{
    
    public String getToken();
    public void setToken(String token);
    
    public String getEmail();
    public void setEmail(String email);

    public String getPassword();
    public void setPassword(String password);
    
    public Ricerca getRicerca();
    public void setRicerca(Ricerca ricerca);

    public Preferenza getPreferenza();
    public void setPreferenza(Preferenza preferenza);
    
    public boolean getPreferenzaMail();
    public void setPreferenzaMail(boolean preferenzaMail);
    
    public boolean getNotAuthFlag();
    public void setNotAuthFlag(boolean notAuthFlag);
    
    public String getUUID();
    public void setUUID(String UUID);
}
