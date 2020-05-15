/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proxys;

import daos.Preferenza_DAO_Imp;
import daos.Ricerca_DAO_Imp;
import data.DataException;
import data.DataLayer;
import models.Preferenza;
import models.Ricerca;
import models.Utente_Imp;
import java.util.logging.Level;

import java.util.logging.Logger;
import data.Data_ItemProxy;

/**
 *
 * @author leonardo
 */
public class Utente_Proxy extends Utente_Imp implements Data_ItemProxy{
        
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
    public void setKey(Integer key) {
        super.setKey(key); 
        this.dirty = true;
    }
       
    @Override
    public void setEmail(String email) {
        super.setEmail(email); 
        this.dirty = true;
    }
    
     @Override
    public void setPassword(String password) {
        super.setPassword(password); 
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
        if(super.getPreferenza() == null && preferenza_key > 0){
            try {
                super.setPreferenza(((Preferenza_DAO_Imp) dataLayer.getDAO(Preferenza.class)).read(preferenza_key));
            } catch (DataException ex) {
                Logger.getLogger(Utente_Proxy.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return super.getPreferenza();
    }

    @Override
    public void setRicerca(Ricerca ricerca) {
        super.setRicerca(ricerca); 
        this.ricerca_key = ricerca.getKey();
        this.dirty = true;
    }

    @Override
    public Ricerca getRicerca() {
        if(super.getRicerca() == null && ricerca_key > 0){
            try{
                super.setRicerca(((Ricerca_DAO_Imp) dataLayer.getDAO(Ricerca.class)).read(ricerca_key));
            } catch (DataException ex) {
                Logger.getLogger(Utente_Proxy.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return super.getRicerca();
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
