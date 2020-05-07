/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proxys;

import data.DataLayer;
import models.Preferenza;
import models.Ricerca;
import models.Utente_Imp;

/**
 *
 * @author leonardo
 */
public class Utente_Proxy extends Utente_Imp{
        
    protected DataLayer dataLayer;
    protected boolean dirty;
    protected int ricerca_key;
    protected int preferenza_key;
        
    public Utente_Proxy(DataLayer d) {
        super();
        
        this.dataLayer = d;
        this.dirty = false;
        this.ricerca_key = 0;
        this.preferenza_key = 0;
    }
    
    //METODI SET DELL'IMPLEMENTAZIONE DEL MODELLO (tolti campi di tipo LIST o dotati di PROXY_KEY)
     
    @Override
    public void setEmail(String email) {
        super.setEmail(email); 
        this.dirty = true;
    }
    
    @Override
    public void setKey(int key) {
        super.setKey(key); 
        this.dirty = true;
    }
         
    //METODI SET/GET DEI CAMPI DOTATI DI PROXY_KEY
    
    @Override
    public void setPreferenza(Preferenza preferenza) {
        super.setPreferenza(preferenza); 
        this.preferenza_key = preferenza.getKey();
        this.dirty = true;
    }

    @Override
    public Preferenza getPreferenza() {
        return super.getPreferenza(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setRicerca(Ricerca ricerca) {
        super.setRicerca(ricerca); 
        this.ricerca_key = ricerca.getKey();
        this.dirty = true;
    }

    @Override
    public Ricerca getRicerca() {
        return super.getRicerca(); //To change body of generated methods, choose Tools | Templates.
    }

    //METODI SET/GET DEI CAMPI DI TIPO LIST

    //METODI DEL PROXY
    
    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    public boolean isDirty() {
        return dirty;
    }
    
    //SETTER PROXY_KEYS

    public void setRicerca_key(int ricerca_key) {
        this.ricerca_key = ricerca_key;
        super.setRicerca(null);
    }

    public void setPreferenza_key(int preferenza_key) {
        this.preferenza_key = preferenza_key;
        super.setPreferenza(null);
    }

    
        
   
}
