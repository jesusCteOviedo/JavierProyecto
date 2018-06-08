package com.mygdx.game;

import java.io.Serializable;
import java.util.ArrayList;

public class Datos implements Serializable {

    private boolean vacio;
    private Heroes heroe;
    private ArrayList<Escuadron> escuadrones, borrar;
    private String id_usario;
    private String id_mapa;

    public Datos(String id_usario,Heroes heroe, ArrayList<Escuadron> escuadrones,String id_mapa) {
        this.id_usario=id_usario;
        this.heroe = heroe;
        this.escuadrones = escuadrones;
        vacio=false;
        borrar=new ArrayList<Escuadron>();
        this.id_mapa=id_mapa;
    }

    public Datos(String id_usario,int level)
    {
        this.id_usario=id_usario;
        id_mapa=id_usario+"m"+level;
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
            borrar.add(escuadrones.get(num_escuadron));
            escuadrones.remove(num_escuadron);
        }
    }

    public String getIdUsuario() {
        return id_usario;
    }

    public ArrayList<Escuadron> getBorrar() {
        return borrar;
    }

    public String getId_mapa(){
        return id_mapa;
    }
}
