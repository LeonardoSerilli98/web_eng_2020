/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.util.List;

/**
 *
 * @author leonardo
 */
public class Preferenza_Imp implements Preferenza{
    
    private int key;
    private List<Canale> canali;
    private Fascia fascia;
    
    public Preferenza_Imp(){
        
        key = 0;
        canali = null;
        fascia = null;      
                
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public List<Canale> getCanali() {
        return canali;
    }

    public void setCanali(List<Canale> canali) {
        this.canali = canali;
    }

    public Fascia getFascia() {
        return fascia;
    }

    public void setFascia(Fascia fascia) {
        this.fascia = fascia;
    }
    
    
    
}
