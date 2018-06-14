package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.io.IOException;
import java.io.Serializable;

import Test.AccionesBatalla;

public class Heroes extends Actor implements AccionesBatalla,Serializable {




    private int posiconX,posicionY;
    private String nombre;



    private String path;

    private int vida,defensa,ataque,poder;
    private Texture texture;
    private int vida_max;


    public Heroes(){
        posiconX=100;
        posicionY=100;
        vida=100;
        defensa=50;
        ataque=50;
        texture = new Texture(Gdx.files.internal("knight.png"));
    }
//String nombre, int vida, int ataque, int defensa, String path,int posiconX,int posicionY,knight.png


    public Heroes(String nombre, int vida, int ataque, int defensa, String path,int posiconX,int posicionY,int poder){
        this.posiconX=posiconX;
        this.posicionY=posicionY;
        this.vida=vida;
        this.defensa=ataque;
        this.ataque=defensa;
        this.poder=poder;
        this.nombre=nombre;
        this.path=path;
        this.vida_max=vida;
        texture = new Texture(Gdx.files.internal(path));
    }

    @Override
    public void draw(Batch batch, float alpha){

        batch.draw(texture, getX(), getY(), 50, 50);

    }

    @Override
    public void recibirDaño(int ataque) {
        int diferencia=defensa-ataque;
        vida=vida-diferencia;

    }

    @Override
    public boolean estavivo() {
        if(vida<=0){
            return false;
        }else {
            return true;
        }
    }

    @Override
    public void reducirDaño(int ataque) {
        int resultado=ataque-this.defensa*2;
        if (resultado<0) resultado=0;
        vida=vida-resultado ;
        if (vida<0) vida=0;
    }

    public void regenerar(){
        this.vida=vida+(int)(vida*0.10);
        if (vida>vida_max)
            vida=vida_max;
    }

    public void subirNivel(){
        vida_max=vida_max+(int)(vida_max*0.10);
        vida=vida_max;
        ataque=ataque+(int)(ataque*0.10);
        defensa=defensa+(int)(defensa*0.10);


    }


    public int getVida() {
        return vida;
    }

    public void setVida(int vida) {
        this.vida = vida;
    }

    public int getDefensa() {
        return defensa;
    }

    public void setDefensa(int defensa) {
        this.defensa = defensa;
    }

    public int getAtaque() {
        return ataque;
    }

    public void setAtaque(int ataque) {
        this.ataque = ataque;
    }

    public int getPosiconX() {
        return posiconX;
    }

    public void setPosiconX(int posiconX) {
        this.posiconX = posiconX;
    }

    public int getPosicionY() {
        return posicionY;
    }

    public void setPosicionY(int posicionY) {
        this.posicionY = posicionY;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getPoder() {
        return poder;
    }

    public void setPoder(int poder) {
        this.poder = poder;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    private void writeObject(java.io.ObjectOutputStream stream)
            throws IOException
    {
        //stream.defaultWriteObject();

        stream.writeObject(vida);
        stream.writeObject(defensa);
        stream.writeObject(ataque);
        stream.writeObject(posiconX);
        stream.writeObject(posicionY);
        stream.writeObject(nombre);
        stream.writeObject(path);
        stream.writeObject(poder);

    }

    private void readObject(java.io.ObjectInputStream stream)
            throws IOException, ClassNotFoundException
    {
        //stream.defaultReadObject();

        vida=(Integer) stream.readObject();
        defensa=(Integer)stream.readObject();
        ataque=(Integer)stream.readObject();
        posiconX=(Integer)stream.readObject();
        posicionY=(Integer)stream.readObject();
        nombre=(String)stream.readObject();
        path=(String)stream.readObject();
        poder=(Integer) stream.readObject();

    }


    @Override
    public String toString() {
        return posiconX + ","+ posicionY + "," + vida + "," + defensa + "," + ataque+ "," + nombre+ "," + path+ "," + poder;
    }

}




