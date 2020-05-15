/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proxys;

import data.DataLayer;
import models.Genere_Imp;
import data.Data_ItemProxy;

/**
 *
 * @author leonardo
 */
public class Genere_Proxy extends Genere_Imp implements Data_ItemProxy{
        
    protected DataLayer dataLayer;
    protected boolean dirty;
          
    public Genere_Proxy(DataLayer d) {
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
    public void setNome(String nome) {
        super.setNome(nome); 
        this.dirty = true;
    }


    //METODI SET/GET DEI CAMPI DOTATI DI PROXY_KEY
    //METODI SET/GET DEI CAMPI DI TIPO LIST
    
    //METODI DEL PROXY
    
    @Override
    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    @Override
    public boolean isDirty() {
            return dirty;
    }
    
    //SETTER PROXY_KEYS      
        
}
