package com.mygdx.game;

import java.io.Serializable;
import java.util.ArrayList;

public class Datos implements Serializable {

    private boolean vacio;
    private Heroes heroe;
    private ArrayList<Escuadron> escuadrones;


    public Datos(Heroes heroe, ArrayList<Escuadron> escuadrones) {
        this.heroe = heroe;
        this.escuadrones = escuadrones;
        vacio=false;
    }

    public Datos() {
        vacio=true;
    }

    public boolean esVacio(){return vacio;}

    public Heroes getHeroe() {
        return heroe;
    }

    public ArrayList<Escuadron>  getEscruadrones() {
        return escuadrones;
    }

    public void setHeroe(Heroes heroe) {
        this.heroe = heroe;
    }

    public void setEscruadrones(ArrayList<Escuadron> escradrones) {
        this.escuadrones = escradrones;
    }

    public void eliminarEscuadron(int num_escuadron) {
        if (num_escuadron>=0 && num_escuadron<escuadrones.size()){
            escuadrones.remove(num_escuadron);
        }
    }
}
