/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proxys;

import daos.Canale_DAO;
import daos.Fascia_DAO_Imp;
import data.DataException;
import data.DataLayer;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Canale;
import models.Fascia;
import models.Preferenza_Imp;
import data.Data_ItemProxy;

/**
 *
 * @author leonardo
 */
public class Preferenza_Proxy extends Preferenza_Imp implements Data_ItemProxy{
        
    protected DataLayer dataLayer;
    protected boolean dirty;
    protected int fascia_key;
        
    public Preferenza_Proxy(DataLayer d) {
        super();
        
        this.dataLayer = d;
        this.dirty = false;
        this.fascia_key = 0;
    }
    
    //METODI SET DELL'IMPLEMENTAZIONE DEL MODELLO (tolti campi di tipo LIST o dotati di PROXY_KEY)
    
    @Override
    public void setKey(Integer key) {
        super.setKey(key); 
        this.dirty = true;
    }
    
    //METODI SET/GET DEI CAMPI DOTATI DI PROXY_KEY

    @Override
    public void setFascia(Fascia fascia) {
        super.setFascia(fascia);
        this.fascia_key = fascia.getKey();
        this.dirty = true;
    }

    @Override
    public Fascia getFascia() {
        
        
        if (super.getFascia() == null && fascia_key > 0) {           
            try {               
                super.setFascia(((Fascia_DAO_Imp) dataLayer.getDAO(Fascia.class)).read(fascia_key));               
            } catch (DataException ex) {        
                Logger.getLogger(Preferenza_Proxy.class.getName()).log(Level.SEVERE, null, ex);  
            }
        }
        return super.getFascia();
    }
    
     //METODI SET/GET DEI CAMPI DI TIPO LIST

    @Override
    public List<Canale> getCanali() {
        if(super.getCanali() == null){
            try{
                super.setCanali(((Canale_DAO) dataLayer.getDAO(Canale.class)).getCanaliByPreferenza(this));
            } catch (DataException ex) {
                Logger.getLogger(Preferenza_Proxy.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return super.getCanali(); 
    }

    @Override
    public void setCanali(List<Canale> canali) {
        super.setCanali(canali);
        this.dirty = true; 
    }
     
    //METODI DEL PROXY
    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    public boolean isDirty() {
        return dirty;
    }
        
    //SETTER PROXY_KEYS

    public void setFascia_key(int fascia_key) {
        this.fascia_key = fascia_key;
        super.setFascia(null);
    }
        
      
   
}
