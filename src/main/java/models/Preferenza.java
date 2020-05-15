/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import data.DataItem;
import java.util.List;

/**
 *
 * @author leonardo
 */
public interface Preferenza extends DataItem<Integer>{

    List<Canale> getCanali();
    void setCanali(List<Canale> canali);

    Fascia getFascia();
    void setFascia(Fascia fascia);
    
    
}
