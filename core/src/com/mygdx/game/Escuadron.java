package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;


import java.util.ArrayList;

public class Escuadron extends Actor  {

    private ArrayList<Enemigo> enemigos;
    private Texture textura;

    private float posicionX,posicionY;

    private int fila,columna,id;



    private String id_escuadron;

    private Texture texture;

    private TextureRegion[][] tmp;
    private TextureRegion[] walkFrames;



    public Escuadron(float posicionX, float posicionY, String rutaTextura, int fila, int columna, String id_escuadron,int id){
        enemigos=new ArrayList<Enemigo>();
        this.posicionX=posicionX;
        this.posicionY=posicionY;
        this.id=id;
        this.fila=fila;
        this.columna=columna;
        this.id_escuadron=id_escuadron;
       // textura=new Texture(Gdx.files.internal(rutaTextura));
        textura=new Texture(Gdx.files.internal(rutaTextura));

        tmp = TextureRegion.split(textura,
                textura.getWidth() / fila,
                textura.getHeight() / columna);


        walkFrames = new TextureRegion[fila *columna];
        int index = 0;
        for (int i = 0; i < columna; i++) {
            for (int j = 0; j < fila; j++) {
                walkFrames[index++] = tmp[i][j];
            }
        }


    }

    public void draw(Batch batch, float alpha){

            if(estanVivos()){ //     batch.draw(escuadrones.get(i).getTextura(), width, Gdx.graphics.getWidth() / 2 - 150, 50, 50);
                //batch.draw(getTextura(), getPosicionX(), getPosicionY(), 50, 50);
                batch.draw( getTextura(id), getPosicionX(), getPosicionY(), 50, 50);


        }
    }


    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }

    public int getId() {
        return id;
    }

    public String getId_escuadron(){return this.id_escuadron;}

    public void setId_escuadron(String id_escuadron) {
        this.id_escuadron = id_escuadron;
    }

    public float getPosicionX() {
        return posicionX;
    }

    public float getPosicionY() {
        return posicionY;
    }

    public boolean estanVivos() {
        return true;
    }

    /*public Texture getTextura() {
        return textura;
    }*/

    public TextureRegion getTextura(int id) {
        return walkFrames[id];
    }

    public Enemigo getEnemigo(int valorDado) {
        if (valorDado>=0 && valorDado<enemigos.size())
            return enemigos.get(valorDado);
        return null;
    }

    public void addEnemigo(Enemigo e){
        enemigos.add(e);
    }
  

    public int size(){return enemigos.size();}

    public ArrayList<Enemigo> getEnemigos() {
        return enemigos;
    }
}
