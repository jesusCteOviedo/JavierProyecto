package com.mygdx.game;

import java.io.Serializable;
import java.util.ArrayList;

public class Datos implements Serializable {

    private boolean vacio;
    private Heroes heroe;
    private ArrayList<Escuadron> escuadrones, borrar;
    private String id_usario;
    private String id_mapa;
    private String id_mapaNuevo;
    private String id_mapaViejo;
    private ArrayList<String>mapas_viejos;



    private int puntuacionJugador;


    private int level;

    //creamos los datos de un nuevo juego
    public Datos(String id_usuario){
        this.id_usario=id_usuario;
        this.heroe=new Heroes("caballero",500,50,50,"knight.png",900,50,0);
        this.escuadrones=new ArrayList<Escuadron>();
        this.level=0;
        borrar=new ArrayList<Escuadron>();
        id_mapa=id_usario+"m"+level;
        id_mapaNuevo=null;
        id_mapaViejo=null;
        puntuacionJugador=0;
        vacio=true;
    }

    public Datos(String id_usario,Heroes heroe, ArrayList<Escuadron> escuadrones,String id_mapa,String id_mapaViejo,int level,int puntuacionJugador) {
        this.id_usario=id_usario;
        this.heroe = heroe;
        this.escuadrones = escuadrones;
        this.level=level;
        vacio=false;
        borrar=new ArrayList<Escuadron>();
        this.id_mapa=id_mapa;
        this.id_mapaViejo=id_mapaViejo;
        this.puntuacionJugador=puntuacionJugador;
    }

    public Datos(String id_usario,int level,String id_mapaViejo,int puntuacionjugador)
    {
        this.id_usario=id_usario;
        id_mapa=id_usario+"m"+level;
        vacio=true;
        this.id_mapaViejo=id_mapaViejo;
        this.puntuacionJugador=puntuacionjugador;
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


    public String getId_mapaNuevo() {
        return id_mapaNuevo;
    }

    public void setId_mapaNuevo(String id_mapaNuevo) {
        this.id_mapaNuevo = id_mapaNuevo;
    }


    public String getId_mapaViejo() {
        return id_mapaViejo;
    }

    public void setId_mapaViejo(String id_mapaViejo) {
        this.id_mapaViejo = id_mapaViejo;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
        id_mapa=id_usario+"m"+level;
    }

    public int getPuntuacionJugador() {
        return puntuacionJugador;
    }

    public void setPuntuacionJugador(int puntuacionJugador) {
        this.puntuacionJugador = puntuacionJugador;
    }

    public void vaciar() {vacio=true;
    }

    public void llenar() {vacio=false;
    }


}
