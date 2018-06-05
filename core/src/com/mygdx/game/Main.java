package com.mygdx.game;

import com.badlogic.gdx.Game;



public class Main extends Game {

    @Override
    public void create() {
        this.setScreen(new MenuInicio(this));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() { 

    }


}
