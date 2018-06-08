package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;


import java.io.*;




import Test.AccionesBatalla;

public class Enemigo implements AccionesBatalla,Serializable {






    /*
    private String nombre;
    private String path;
    private int vida,ataque,defensa,id,fila,columna;*/


    transient  String nombre;
    transient String path;
    transient int vida,ataque,defensa,fila,columna;
    private String id;
    private Texture textura;



    private int idGradico;

    private TextureRegion[][] tmp;
    private TextureRegion[] walkFrames;

    public Enemigo(String nombre, int vida, int ataque, int defensa, String path,int fila,int columna,String id,int idGrafico){
        this.nombre=nombre;
        this.path=path;
        this.vida=vida;
        this.ataque=ataque;
        this.defensa=defensa;
        this.fila=fila;
        this.columna=columna;
        this.id=id;
        this.idGradico=idGrafico;

        textura=new Texture(Gdx.files.internal(path));

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





    public void recibirDaño(int ataque) {
        int diferencia=defensa-ataque;
        vida=vida-ataque;

    }
    public boolean estavivo() {
        if(vida<=0){
            return false;
        }else {

            return true;
        }
    }


   /* public Texture getTextura (){
        return textura;
    }*/

    public TextureRegion getTextura(int id) {
        return walkFrames[id];
    }

    public int getAtaque() {
        return ataque;
    }

    public int getVida() {
        return vida;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getPath() {
        return path;
    }

    public int getDefensa() {
        return defensa;
    }

    public void reducirDaño(int ataque) {
        int def=this.defensa*2;
        def=def-ataque;
        vida-=def ;
    }


    public int getIdGradico() {
        return idGradico;
    }

    public void setIdGradico(int idGradico) {
        this.idGradico = idGradico;
    }

    private void writeObject(java.io.ObjectOutputStream stream)
            throws IOException
    {
        //stream.defaultWriteObject();
        stream.writeObject(nombre);
        stream.writeObject(path);
        stream.writeObject(vida);
        stream.writeObject(defensa);
        stream.writeObject(ataque);
        stream.writeObject(fila);
        stream.writeObject(columna);
        stream.writeObject(ataque);
        stream.writeObject(id);
    }

    private void readObject(java.io.ObjectInputStream stream)
            throws IOException, ClassNotFoundException
    {
        //stream.defaultReadObject();
        nombre=(String)stream.readObject();
        path=(String)stream.readObject();
        vida=(Integer) stream.readObject();
        defensa=(Integer)stream.readObject();
        ataque=(Integer)stream.readObject();
        fila=(Integer)stream.readObject();
        columna=(Integer)stream.readObject();
        id=(String)stream.readObject();

    }



}
