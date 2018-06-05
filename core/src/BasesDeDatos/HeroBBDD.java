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





}
