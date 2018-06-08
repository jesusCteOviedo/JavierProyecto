package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
/*import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
*/

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import Test.ContenedorMapas;


public class MenuInicio implements Screen {


    private Stage stage;
    private Skin mySkin;
    private Button nueva;
    private Button cargar;
    private Button salir;
    private Game game;
    private Music music;
    private String id_usuario;
    private ContenedorMapas maps;


    public MenuInicio(Game agame,String id){
        this.game=agame;
        this.id_usuario=id;
      //  this.maps=new ContenedorMapas();
       // maps.rellenar();
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
                //game.setScreen(new Mapa(game,new Datos(),"maps/town.tmx"));
                          game.setScreen(new Mapa(game,new Datos(id_usuario,0),0));
                //         game.setScreen(new Mapa(game,new Datos(),maps.getPath("maps/level1.tmx"),1));
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
                //System.out.println("Pressed Text Button ataque");
                return true;
            }
        });
        stage.addActor(cargar);

        salir.setSize(col_width,row_height);
        salir.setPosition(Gdx.graphics.getWidth()/2-250,col_width/2-200);
        salir.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                //System.out.println("Pressed Text Button ataque");
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
