/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

/**
 *
 * @author leonardo
 */
public interface Immagine {
    
    public int getKey();
    public void setKey(int key);

    public String getNome();
    public void setNome(String nome);

    public String getTipo();
    public void setTipo(String tipo);

    public long getTaglia();
    public void setTaglia(long taglia);
    
}
