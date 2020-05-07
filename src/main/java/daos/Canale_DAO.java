/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daos;

import data.DataException;
import java.util.List;
import models.Canale;
import models.Preferenza;


/**
 *
 * @author leonardo
 */
public interface Canale_DAO{
    
    List<Canale> getCanaliByPreferenza(Preferenza preferenza) throws DataException;
    
    
}
