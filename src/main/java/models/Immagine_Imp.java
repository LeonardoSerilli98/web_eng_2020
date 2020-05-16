/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import data.DataItemImpl;

/**
 *
 * @author leonardo
 */
public class Immagine_Imp extends DataItemImpl<Integer>implements Immagine{
    
    private String nome;
    private String tipo;
    private long taglia;
    private Stagione stagione;
    private Programma programma;
    
    public Immagine_Imp(){
        
        super();
        nome = "";
        tipo = "";
        taglia = 0;
        stagione = null;
        programma = null;
        
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

    @Override
    public Stagione getStagione() {
        return stagione;
    }

    @Override
    public void setStagione(Stagione stgn) {
        this.stagione = stgn;
    }

    @Override
    public Programma getProgramma() {
        return this.programma;
    }

    @Override
    public void setProgramma(Programma prgrm) {
        this.programma = prgrm;
    }
    
}
