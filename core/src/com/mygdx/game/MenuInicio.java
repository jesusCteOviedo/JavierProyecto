package com.mygdx.game;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;


import Test.Cliente;



public class MenuInicio implements Screen {


    private Stage stage;
    private Skin mySkin;
    private Button nueva;
    private Button cargar;
    private Button salir;
    private Game game;
    private Music music;
    private String id_usuario;



    public MenuInicio(Game agame,String id){
        this.game=agame;
        this.id_usuario=id;
        int row_height = Gdx.graphics.getWidth() / 9;
        int col_width = Gdx.graphics.getWidth() / 4;


        stage = new Stage();
        mySkin = new Skin(Gdx.files.internal("skin/glassy-ui.json"));
        nueva = new TextButton("Nueva Partida",mySkin,"default");
        cargar = new TextButton("Cargar Partida",mySkin,"default");
        salir = new TextButton("Salir",mySkin,"default");




        nueva.setSize(col_width,row_height);
        nueva.setPosition(Gdx.graphics.getWidth()/2-250,Gdx.graphics.getHeight()/2+100);
        nueva.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                music.stop();

            //    game.setScreen(new Mapa(game,new Datos(id_usuario,0,null,0),0));
                game.setScreen(new Mapa(game,new Datos(id_usuario)));

            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        stage.addActor(nueva);


        cargar.setSize(col_width,row_height);
        cargar.setPosition(Gdx.graphics.getWidth()/2-250,col_width/2+90);
        cargar.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {

                Cliente client =new Cliente();

                Datos d= client.prueba1(id_usuario);
                game.setScreen(new Mapa(game,d));
                return true;
            }
        });
        stage.addActor(cargar);

        salir.setSize(col_width,row_height);
        salir.setPosition(Gdx.graphics.getWidth()/2-250,col_width/2-200);
        salir.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {

                Gdx.app.exit();

                return true;
            }
        });
        stage.addActor(salir);

        music = Gdx.audio.newMusic(Gdx.files.internal("music/RPGThemev001.mp3"));
        music.play();
        music.setLooping(true);
        music.setVolume(100f);

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
        music.pause();
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
