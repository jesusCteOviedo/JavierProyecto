package BasesDeDatos;

import com.mygdx.game.Heroes;

import java.io.Serializable;

public class HeroBBDD implements Serializable {


    private int posiconX,posicionY;

    private int vida,defensa,ataque;

    public HeroBBDD(Heroes h){
        posiconX=(int)h.getX();
        posicionY=(int)h.getY();
        vida=h.getVida();
        defensa=h.getDefensa();
        ataque=h.getAtaque();
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
