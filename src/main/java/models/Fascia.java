/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import data.DataItem;
import java.sql.Time;

/**
 *
 * @author leonardo
 */
public interface Fascia extends DataItem<Integer>{
    
    public Time getInizio();
    public void setInizio(Time inizio);
            
    public Time getFine();
    public void setFine(Time fine);
    
    public String getFascia();            
    public void setFascia(String fascia);
    
}
