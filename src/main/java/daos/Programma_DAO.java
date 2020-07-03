/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daos;

import data.DAO_Interface;
import data.DataException;
import models.Programma;

/**
 *
 * @author leonardo
 */
public interface Programma_DAO  extends DAO_Interface<Programma>{
    
    public Programma checkExistence(String titolo) throws DataException;
    
    
    
}
