/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daos;

import data.DAO_Interface;
import data.DataException;
import models.Preferenza;

/**
 *
 * @author leonardo
 */
public interface Preferenza_DAO extends DAO_Interface<Preferenza>{
    
    public Preferenza checkExistence(String username) throws DataException;
    
}
