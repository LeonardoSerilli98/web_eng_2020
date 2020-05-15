/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proxys;

import data.DataLayer;
import java.sql.Time;
import models.Fascia_Imp;
import data.Data_ItemProxy;

/**
 *
 * @author leonardo
 */
public class Fascia_Proxy extends Fascia_Imp implements Data_ItemProxy{
        
    protected DataLayer dataLayer;
    protected boolean dirty;
   
    public Fascia_Proxy(DataLayer d) {
        super();
        
        this.dataLayer = d;
        this.dirty = false;

    }
    
    
    //METODI SET DELL'IMPLEMENTAZIONE DEL MODELLO (tolti campi di tipo LIST o dotati di PROXY_KEY)
    
    @Override
    public void setKey(Integer key) {
        super.setKey(key); 
        this.dirty = true;
    }
    
    @Override
    public void setFascia(String fascia) {
        super.setFascia(fascia);
        this.dirty = true;
    }

    @Override
    public void setFine(Time fine) {
        super.setFine(fine);
        this.dirty = true;
    }
    
    @Override
    public void setInizio(Time inizio) {
        super.setInizio(inizio); 
        this.dirty = true;
    }

    
    
    //METODI SET/GET DEI CAMPI DOTATI DI PROXY_KEY
    //METODI SET/GET DEI CAMPI DI TIPO LIST
   
    //METODI DEL PROXY
    
    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    public boolean isDirty() {
        return dirty;
    }
    
    //SETTER PROXY_KEYS
    
}
