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

    private int vida,defensa,ataque;
    private Texture texture;


    public Heroes(){
        posiconX=100;
        posicionY=100;
        vida=100;
        defensa=50;
        ataque=50;
        texture = new Texture(Gdx.files.internal("knight.png"));
    }

    @Override
    public void draw(Batch batch, float alpha){
        batch.draw(texture, getX(), getY(), 32, 32);

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
        int def=this.defensa*2;
        def=def-ataque;
        ataque=ataque-def/10;
        vida=vida-ataque ;

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



    private void writeObject(java.io.ObjectOutputStream stream)
            throws IOException
    {
        //stream.defaultWriteObject();

        stream.writeObject(vida);
        stream.writeObject(defensa);
        stream.writeObject(ataque);
        stream.writeObject(posiconX);
        stream.writeObject(posicionY);

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


    }



}




