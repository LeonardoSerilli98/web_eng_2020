/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import java.sql.ResultSet;
import java.util.List;

/**
 *
 * @author leonardo
 */
public interface DAO_Interface<T> {
    
    T makeObj();
    T makeObj(ResultSet rs) throws DataException;
    
    List<T> getAll() throws DataException;
    
    void create(T item) throws DataException;
    T read(int key) throws DataException;
    void update(T item) throws DataException;
    void delete(T item) throws DataException;
    
     
}
