package com.mygdx.game;

import com.badlogic.gdx.Game;


public class Main extends Game {

    private String id;


    public Main(String idUsuario){
        this.id=idUsuario;
    }


    @Override
    public void create() {
        this.setScreen(new MenuInicio(this,id));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() { 

    }


}
