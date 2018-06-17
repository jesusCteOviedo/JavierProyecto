package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;


import java.io.*;




import Test.AccionesBatalla;

public class Enemigo implements AccionesBatalla,Serializable {

    transient  String nombre;
    transient String path;
    transient int vida;
    transient int ataque;
    transient int defensa;
    transient int fila;
    transient int columna;
    private String id;
    private Texture textura;



    private int idGradico;

    private TextureRegion[][] tmp;
    private TextureRegion[] walkFrames;



    private int jefe;

    public Enemigo(String nombre, int vida, int ataque, int defensa, String path,int fila,int columna,String id,int idGrafico,int jefe){
        this.nombre=nombre;
        this.path=path;
        this.vida=vida;
        this.ataque=ataque;
        this.defensa=defensa;
        this.fila=fila;
        this.columna=columna;
        this.id=id;
        this.idGradico=idGrafico;
        this.jefe=jefe;
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





    public String recibirDaño(int ataque) {
        int diferencia=defensa-ataque;
        vida=vida-ataque;
        return "daño ocasionado a "+this.nombre+ " "+diferencia;
    }

    public boolean estavivo() {
        if(vida<=0){
            return false;
        }else {

            return true;
        }
    }




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

    public String reducirDaño(int ataque) {
        int def=this.defensa*2;
        def=def-ataque;
        vida-=def ;
        return "daño ocasionado a "+this.nombre+ " "+def;
    }


    public int getFila() {
        return fila;
    }

    public void setFila(int fila) {
        this.fila = fila;
    }

    public int getColumna() {
        return columna;
    }

    public void setColumna(int columna) {
        this.columna = columna;
    }


    public int getIdGradico() {
        return idGradico;
    }

    public void setIdGradico(int idGradico) {
        this.idGradico = idGradico;
    }

    public int getJefe() {
        return jefe;
    }

    public void setJefe(int jefe) {
        this.jefe = jefe;
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
        stream.writeObject(id);
        stream.writeObject(idGradico);
        stream.writeObject(jefe);
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
        idGradico=(Integer)stream.readObject();
        jefe=(Integer)stream.readObject();

    }

    @Override
    public String toString() {
        return  nombre  + "," + path  + "," + vida + "," + ataque + "," + defensa + "," + fila + "," + columna + "," + id  + "," + idGradico+"," + jefe;
    }
}
