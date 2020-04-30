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
public interface Preferenza {
    
    public int getKey();
    public void setKey(int key);

    public List<Canale> getCanali();
    public void setCanali(List<Canale> canali);

    public Orario getFascia();
    public void setFascia(Orario fascia);
    
    
}
