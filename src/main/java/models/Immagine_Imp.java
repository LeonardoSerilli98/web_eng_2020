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
public class Immagine_Imp implements Immagine{
    
    private int key;
    private String nome;
    private String tipo;
    private long taglia;

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public long getTaglia() {
        return taglia;
    }

    public void setTaglia(long taglia) {
        this.taglia = taglia;
    }
    
}
