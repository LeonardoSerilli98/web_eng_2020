/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proxys;

import data.DataLayer;
import models.Immagine_Imp;

/**
 *
 * @author leonardo
 */
public class Immagine_Proxy extends Immagine_Imp{
        
    protected DataLayer dataLayer;
    protected boolean dirty;
        
    public Immagine_Proxy(DataLayer d) {
        super();
        
        this.dataLayer = d;
        this.dirty = false;
        
    }
    
    //METODI SET DELL'IMPLEMENTAZIONE DEL MODELLO (tolti campi di tipo LIST o dotati di PROXY_KEY)

    @Override
    public void setKey(int key) {
        super.setKey(key); 
        this.dirty = true;
    }
        
    @Override
    public void setNome(String nome) {
        super.setNome(nome); 
        this.dirty = true;
    }
    
    @Override
    public void setTipo(String tipo) {
        super.setTipo(tipo); 
        this.dirty = true;
    }
    
    @Override
    public void setTaglia(long taglia) {
        super.setTaglia(taglia); 
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
