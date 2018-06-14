package Test;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.mygdx.game.Datos;
import com.mygdx.game.Enemigos;
import com.mygdx.game.Escuadron;
import com.mygdx.game.Heroes;
import com.mygdx.game.Mapa;

import java.util.ArrayList;
import java.util.Random;


public class BatallaCopia  implements  Screen {

    private Skin mySkin;
    private Stage stage;
    private Game game;
    private Button button3;
    private SpriteBatch bacth;
    private BitmapFont bit;
    private Heroes myActor;
    private int num_escuadron;
    // private Enemigos myEnemi;
    private Enemigos myEnemi;
    // private ArrayList<Enemigo> enemigos;
    private Datos datos;

    private ProgressBar barraHero;
    private Music music;
    private String mapa;

    private int id;
    private int num;
    private boolean tienePoder;

    public BatallaCopia(Game agame, final Datos dato, int num, int idmap) {
        this.game = agame;
        this.mapa=mapa;
        this.id=idmap;
        this.num=num;
        stage = new Stage();
        bacth = new SpriteBatch();
        bit = new BitmapFont();

        int row_height = Gdx.graphics.getWidth() / 9;
        int col_width = Gdx.graphics.getWidth() / 8;

        this.datos=dato;
        myActor=datos.getHeroe();

        myEnemi=new Enemigos(datos.getEscruadrones().get(num).getEnemigos());
        num_escuadron=num;
        Texture texture = new Texture(Gdx.files.internal("fruina.png"));
        mySkin= new Skin(Gdx.files.internal("skin/glassy-ui.json"));

        barraHero=new ProgressBar(0, myActor.getVida(), 10, false, mySkin);
        barraHero.setValue(myActor.getVida());
        barraHero.setBounds((Gdx.graphics.getWidth() / 2 - col_width * 2)+300, col_width / 2*3,250,250);
        //barraHero.setPosition((Gdx.graphics.getWidth() / 2 - col_width * 2)+300, col_width / 2*4);

        Image im = new Image(texture);

        im.scaleBy(Gdx.graphics.getHeight() / im.getHeight(), Gdx.graphics.getWidth() / im.getWidth());

        stage.addActor(im);


        //el boton esta a escala 1:12 de la pantalla e largo

        Skin mySkin = new Skin(Gdx.files.internal("skin/glassy-ui.json"));







        Button button2 = new TextButton("Ataque", mySkin, "default");
        button2.setSize(col_width, row_height);
        button2.setPosition(Gdx.graphics.getWidth() / 2 - col_width * 2, col_width / 2);
        button2.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                sistemaBatallaConAtaque();

            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Pressed Text Button ataque");
                return true;
            }
        });


        stage.addActor(button2);


        button3 = new TextButton("Defensa", mySkin, "default");
        button3.setSize(col_width, row_height);
        button3.setPosition(Gdx.graphics.getWidth() / 2 + col_width, col_width / 2);
        button3.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                sistemaBatallaConDefensa();
                return true;
            }
        });

        stage.addActor(button3);


       /* Button button4 = new TextButton("Volver", mySkin, "default");
        button4.setSize(col_width, row_height);
        button4.setPosition(0, col_width / 2);
        button4.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                // game.setScreen(Mapa(game));

            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        stage.addActor(button4);*/

        myActor.setPosition((Gdx.graphics.getWidth() / 2 - col_width * 2)+400, (col_width / 2*4)+50);
        stage.addActor(myActor);

        stage.addActor(barraHero);

        for (int i=0;i<myEnemi.size();i++){

            stage.addActor(myEnemi);

        }

        music = Gdx.audio.newMusic(Gdx.files.internal("music/Fighting.mp3"));
        music.play();
        music.setLooping(true);
        music.setVolume(100f);
        System.out.println("HEROPODER "+datos.getHeroe().getPoder());



    }


    private void sistemaBatallaConAtaque() {
        int row_height = Gdx.graphics.getWidth() / 9;
        int col_width = Gdx.graphics.getWidth() / 8;
        System.out.println("Hero ataco");
        Random r = new Random();
        int valorDado = r.nextInt(4);
        while (myEnemi.getEnemigo(valorDado) == null){
            valorDado = r.nextInt(4);
        }

        if(myEnemi.getEnemigo(valorDado).getJefe()<=0) {
            myEnemi.getEnemigo(valorDado).recibirDaño(myActor.getAtaque());
        }else{
            if(myActor.getPoder()==1){
                myEnemi.getEnemigo(valorDado).recibirDaño(myActor.getAtaque());
            }
        }

        if(myEnemi.getEnemigo(valorDado).estavivo()){
            for(int i=0;i<myEnemi.size();i++) {
                myActor.recibirDaño(myEnemi.getEnemigo(i).getAtaque());
            }
            barraHero.setValue(myActor.getVida());
        }else{
            if(myEnemi.getEnemigo(valorDado).getJefe()==-1) {
                datos.getHeroe().setPoder(1);
            }
            datos.setPuntuacionJugador(datos.getPuntuacionJugador()+1);
            myEnemi.redimensionarVector(valorDado);
        }


    }



    private void sistemaBatallaConDefensa(){
        myActor.regenerar();
        for(int i=0;i<myEnemi.size();i++){
            myActor.reducirDaño(myEnemi.getEnemigo(i).getAtaque());
            barraHero.setValue(myActor.getVida());
            System.out.println("Vida del hero: "+myActor.getVida());
        }
    }

    private boolean comprobarMuertos(){

        return myEnemi.size()>0;
    }



    @Override
    public void show() {

        Gdx.input.setInputProcessor(stage);
        stage.setDebugAll(true);
    }

    @Override
    public void render(float delta) {
        if(comprobarMuertos()) {
            Gdx.gl.glClearColor(1, 1, 1, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            stage.act();
            stage.draw();
        }else{
            music.stop();
            datos.eliminarEscuadron(this.num_escuadron);
            this.game.setScreen(new Mapa(game,datos));
        }
        if(myActor.getVida()<=0){
            this.game.setScreen(new GameOver(game,datos.getIdUsuario()));
        }

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
