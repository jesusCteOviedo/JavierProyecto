package Test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Hero extends Actor implements AccionesBatalla{


    transient  String nombre;
    transient String path;
    transient int vida,ataque,defensa;
    transient int posiconX,posicionY;

    private Texture textura;
    private Skin mySkin;

    public Hero(String nombre, int vida, int ataque, int defensa, String path,int posiconX,int posicionY) {
        this.nombre = nombre;
        this.path = path;
        this.vida = vida;
        this.ataque = ataque;
        this.defensa = defensa;
        this.posiconX=posiconX;
        this.posicionY=posicionY;
        textura = new Texture(Gdx.files.internal(path));
        mySkin = new Skin(Gdx.files.internal("skin/glassy-ui.json"));
    }


    @Override
    public void draw(Batch batch, float alpha){
        ProgressBar p= new ProgressBar(0, getVida(), 1, false, mySkin);
        p.setValue(getVida());
        p.setPosition( Gdx.graphics.getHeight() / 2,Gdx.graphics.getWidth() / 2);
        p.draw(batch,alpha);
        batch.draw(textura, 250,250, 32, 32);

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
        vida-=def ;

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


}
