/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import data.DataItemImpl;
import java.util.List;

/**
 *
 * @author leonardo
 */
public class Preferenza_Imp extends DataItemImpl<Integer> implements Preferenza{
    
    private List<Canale> canali;
    private Fascia fascia;
    
    public Preferenza_Imp(){
        
        super();
        canali = null;
        fascia = null;      
                
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
