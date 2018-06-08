package BasesDeDatos;

import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.Enemigo;
import com.mygdx.game.Enemigos;
import com.mygdx.game.Escuadron;

import java.io.Serializable;
import java.util.ArrayList;

public class EscuadronBBDD implements Serializable {


    private ArrayList<Enemigo> enemigos;


    private float posicionX,posicionY;

    private int fila,columna,idGrafico;

    private String id;


    public EscuadronBBDD(Escuadron escuadron){
        enemigos=escuadron.getEnemigos();
        this.posicionX=escuadron.getPosicionX();
        this.posicionY=escuadron.getPosicionY();
        this.fila=escuadron.getFila();
        this.columna=escuadron.getColumna();
        this.idGrafico=escuadron.getId();
        this.id=escuadron.getId_escuadron();
    }


    public EscuadronBBDD(Escuadron escuadron,ArrayList<Enemigo>enemigo){
        enemigos=enemigo;
        this.posicionX=escuadron.getPosicionX();
        this.posicionY=escuadron.getPosicionY();
        this.fila=escuadron.getFila();
        this.columna=escuadron.getColumna();
        //this.id=escuadron.getId();

    }



    public float getPosicionX() {
        return posicionX;
    }

    public float getPosicionY() {
        return posicionY;
    }

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }

    public int getIdGrafico() {
        return idGrafico;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public Enemigo getEnemigo(int valorDado) {
        if (valorDado>=0 && valorDado<enemigos.size())
            return enemigos.get(valorDado);
        return null;
    }

    public int size(){return enemigos.size();}

    public ArrayList<Enemigo> getEnemigos() {
        return enemigos;
    }

}
