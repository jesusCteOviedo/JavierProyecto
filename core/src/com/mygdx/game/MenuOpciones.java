package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import Test.Cliente;


public class MenuOpciones implements Screen {


    private Stage stage;
    private Skin mySkin;
    private Button guardar;
    private Button volver;
    private Button salir;
    private Game game;

    private Datos d;
    private int level;


    public MenuOpciones(Game agame, Datos datos,int leve){
        this.game=agame;

        this.d=datos;
        this.level=leve;
        int row_height = Gdx.graphics.getWidth() / 9;
        int col_width = Gdx.graphics.getWidth() / 4;


        stage = new Stage();
        mySkin = new Skin(Gdx.files.internal("skin/glassy-ui.json"));
        guardar = new TextButton("Guardar Partida",mySkin,"default");
        volver = new TextButton("Volver Partida",mySkin,"default");
        salir = new TextButton("Salir del juego",mySkin,"default");




        guardar.setSize(col_width,row_height);
        guardar.setPosition(Gdx.graphics.getWidth()/2-250,Gdx.graphics.getHeight()/2+100);
        guardar.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                Cliente client=new Cliente();
                client.guardar(d,level);

            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        stage.addActor(guardar);


        volver.setSize(col_width,row_height);
        volver.setPosition(Gdx.graphics.getWidth()/2-250,col_width/2+90);
        volver.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {

                game.setScreen(new Mapa(game, d));
                return true;
            }
        });
        stage.addActor(volver);

        salir.setSize(col_width,row_height);
        salir.setPosition(Gdx.graphics.getWidth()/2-250,col_width/2-200);
        salir.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {

                //vuelve a inicra la apicaion
                System.exit(0);
                //

                Gdx.app.exit();

                return true;
            }
        });
        stage.addActor(salir);
    }


    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
