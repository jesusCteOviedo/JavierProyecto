package com.mygdx.game;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;


import java.io.Serializable;
import java.util.ArrayList;




public class Enemigos extends Actor implements Serializable  {




    private ArrayList<Enemigo> enemigos;
    private ArrayList<ProgressBar>barraEnemigo;
    private Skin mySkin;
    private ArrayList<Label>nombreEnemigo;
    public Enemigos(ArrayList <Enemigo> e){

        enemigos=e;

        mySkin = new Skin(Gdx.files.internal("skin/glassy-ui.json"));

    }


    @Override
    public void draw(Batch batch, float alpha){

        barraEnemigo=new ArrayList<ProgressBar>();
        nombreEnemigo=new ArrayList<Label>();
        for(int i=0;i<enemigos.size();i++){

            float aux = (i + 1);
            float a = aux / (enemigos.size() + 1);

            float width = a * Gdx.graphics.getWidth();
            if(enemigos.get(i)!=null) {

                batch.draw(enemigos.get(i).getTextura(enemigos.get(i).getIdGradico()), width, Gdx.graphics.getWidth() / 2 - 150, 50, 50);
                barraEnemigo.add(new ProgressBar(0, enemigos.get(i).getVida(), 1, false, mySkin));
                barraEnemigo.get(i).setValue(enemigos.get(i).getVida());
                barraEnemigo.get(i).setPosition( width,Gdx.graphics.getWidth() / 2 - 250);
                barraEnemigo.get(i).draw(batch,alpha);
                nombreEnemigo.add(new Label(enemigos.get(i).getNombre(),mySkin,"big"));
                nombreEnemigo.get(i).setPosition( width,Gdx.graphics.getWidth() / 2);
                nombreEnemigo.get(i).draw(batch,alpha);
            }
        }

    }




    public void redimensionarVector(int numero){
        if (numero>=0 && numero<enemigos.size())

            enemigos.remove(numero);
    }


    public Enemigo getEnemigo(int valorDado) {
        if (valorDado>=0 && valorDado<enemigos.size())
            return enemigos.get(valorDado);
        return null;
    }


    public int size(){return enemigos.size();}


}


