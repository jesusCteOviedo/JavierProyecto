package com.mygdx.game;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;


import java.util.ArrayList;
import java.util.Random;

import Test.BatallaCopia;
import Test.Cliente;
import Test.ContenedorMapas;


public class Mapa  implements InputProcessor,Screen {


    private TiledMap tiledMap;
    private OrthographicCamera camera;


    private OrthogonalTiledMapRenderer renderer;

    private Stage stage;
    // private Heroes hero;
    private ArrayList<Escuadron> escuadrones;

    private Game game;
    private Datos datos;


    private Music music;
    private String mapa;
    //identificador mapa para la base de datos
    private String idmap;
    //nivle
    private int level;

    private ArrayList<String> path;
    private String idUsuario;
    Label puntos;


    public Mapa(Game agame, Datos d){
        inicializarPaths();
        this.game=agame;
        // this.idUsuario=idUsuario;
        this.datos=d;
        //  this.mapa=map;

        // this.level=0;

        this.level=d.getLevel();

//        this.maps=new ContenedorMapas();
        //  this.idmap=id;


        inicializar();
        //  maps.rellenar();


        //establecemos el mapa del juego que es secuecial en funcion del nivel
        int aux_mapa=level%path.size();


        //tiledMap = new TmxMapLoader().load(mapa);
        tiledMap = new TmxMapLoader().load(path.get(aux_mapa));


        camera = new OrthographicCamera();
        camera.setToOrtho(false, 640 , 480 );
        renderer = new OrthogonalTiledMapRenderer(tiledMap);
        renderer.getBatch().disableBlending();
        renderer.setView(camera);

        //establecemos la vista de los elemenots para establecer en el mapa
        stage = new Stage(new ScreenViewport());
        stage.setDebugAll(true);

        //turno para el heroe
        //   hero= new Heroes();
        datos.getHeroe().setPosition(900, 50);
        stage.addActor(datos.getHeroe());

        //inicializmos los enemigos y los situamos en el mada

        for (int i=0;i<datos.getEscruadrones().size();i++){
            stage.addActor(datos.getEscruadrones().get(i));
        }


        //************????????????????????????
        Skin mySkin = new Skin(Gdx.files.internal("skin/glassy-ui.json"));
        int row_height = Gdx.graphics.getWidth() / 9;
        int col_width = Gdx.graphics.getWidth() / 8;


        puntos= new Label("Puntos total "+d.getPuntuacionJugador(),mySkin,"big");
        puntos.setPosition(100,55);
        stage.addActor(puntos);


        //boton guardar
        Button button2 = new TextButton("Menu", mySkin, "default");
        button2.setSize(col_width, row_height);
        button2.setPosition(Gdx.graphics.getWidth()-col_width, 0);
        button2.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
               /* Cliente c=new Cliente();

                HeroBBDD her=new HeroBBDD(datos.getHeroe().getPosiconX(),datos.getHeroe().getPosicionY(),datos.getHeroe().getVida(),datos.getHeroe().getDefensa(),datos.getHeroe().getAtaque());
                c.guardar(her);*///
                // game.setScreen(new MenuOpciones(game,datos,mapa,idmap));
                game.setScreen(new MenuOpciones(game,datos,level));
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                return true;
            }
        });
        stage.addActor(button2);


        music = Gdx.audio.newMusic(Gdx.files.internal("music/TownTheme.wav"));
        music.play();
        music.setLooping(true);
        music.setVolume(100f);

        System.out.println(datos.getPuntuacionJugador()+"Puntiacion jugador");

    }

    private void inicializarPaths() {
        path=new ArrayList<String>();
        path.add("maps/level1.tmx");
        path.add("maps/level2.tmx");
        path.add("maps/level3.tmx");
        path.add("maps/level4.tmx");
    }


    //mejora!!!!!
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        int y = Gdx.graphics.getHeight();

        datos.getHeroe().addAction(Actions.moveTo(screenX, y - screenY, 2f));

        return false;
    }


    public void colisonar(){

        Rectangle rec=new Rectangle(datos.getHeroe().getX(),datos.getHeroe().getY(),32,32);
        for (int i=0;i<datos.getEscruadrones().size();i++){
            Rectangle ene = new Rectangle(datos.getEscruadrones().get(i).getPosicionX(),datos.getEscruadrones().get(i).getPosicionY(), 40, 40);
            if (rec.overlaps(ene)) {
                music.stop();
                datos.getHeroe().getActions().clear();
                //pasar batalla
                // game.setScreen(new BatallaCopia(game,datos,i,mapa,idmap));
                game.setScreen(new BatallaCopia(game,datos,i,level));
            }
        }
    }

    private void inicializar(){
        if (datos.esVacio()) {
            Random r = new Random();
            Random numero_escuadrones = new Random();
            Random numero_enemigos=new Random();
            Random situar = new Random();

            ArrayList<Escuadron> escuadrones=new ArrayList<Escuadron>();
            /*Escuadron e = new Escuadron(100, 300, "sprites/characters/Demon01.png",8,8,datos.getId_mapa()+"-"+1,r.nextInt(6)+1);
            e.addEnemigo(new Enemigo("enemigoA", 10, 40, 30, "sprites/characters/Demon01.png",8,8,e.getId_escuadron()+"-"+3,r.nextInt(6)+1,0));
            e.addEnemigo(new Enemigo("enemigoB", 10, 40, 30, "sprites/characters/Demon01.png",8,8,e.getId_escuadron()+"-"+2,r.nextInt(6)+1,0));
            e.addEnemigo(new Enemigo("enemigoC", 10, 40, 30, "sprites/characters/Demon01.png",8,9,e.getId_escuadron()+"-"+1,r.nextInt(6)+1,0));

            escuadrones.add(e);
            e = new Escuadron(250, 400, "sprites/characters/Demon01.png",8,8,datos.getId_mapa()+"-"+2,r.nextInt(6)+1);
            e.addEnemigo(new Enemigo("enemigoA", 10, 40, 30, "sprites/characters/Demon01.png",9,9,e.getId_escuadron()+"-"+1,r.nextInt(6)+1,0));
            e.addEnemigo(new Enemigo("enemigoB", 10, 40, 30, "sprites/characters/Demon01.png",8,9,e.getId_escuadron()+"-"+2,r.nextInt(6)+1,0));
            escuadrones.add(e);

            e = new Escuadron(500, 600, "sprites/characters/Demon01.png",8,8,datos.getId_mapa()+"-"+3,r.nextInt(6)+1);
            e.addEnemigo(new Enemigo("enemigoA", 10, 40, 30, "sprites/characters/Demon01.png",9,9,e.getId_escuadron()+"-"+1,r.nextInt(6)+1,0));
            e.addEnemigo(new Enemigo("enemigoB", 10, 40, 30, "sprites/characters/Demon01.png",8,9,e.getId_escuadron()+"-"+2,r.nextInt(6)+1,0));
            escuadrones.add(e);*/

            //vMin + r.nextInt(vMax - vMin + 1);
            int rango=2+numero_escuadrones.nextInt(3);
            System.out.println(rango+"escuadrones--------");

            int ancho=Gdx.graphics.getWidth()/rango;
            System.out.println(ancho+" ///*/*/*//*/**/*/*/*/*");
            int x;
            char nombre='A';
            for(int i=0;i<rango;i++) {

                int y=situar.nextInt(3*Gdx.graphics.getHeight()/5)+Gdx.graphics.getHeight()/5;
                x=ancho*i+situar.nextInt(ancho-80)+50;
                System.out.println(x+" xxxxxxxxx "+y+" yyyyyyyyyyy");
                Escuadron e = new Escuadron(x, y, "sprites/characters/Demon01.png", 8, 8, datos.getId_mapa() + "-" + i, r.nextInt(6) + 1);
                int rango_enemigo=1+numero_enemigos.nextInt(5);
                System.out.println(rango_enemigo+"enemigos--------");

                for (int j = 0; j < rango_enemigo; j++) {
                    e.addEnemigo(new Enemigo("enemigo"+nombre, 30, 10, 30, "sprites/characters/Demon01.png", 8, 8, e.getId_escuadron() + "-" + j, r.nextInt(6) + 1, 0));
                nombre++;
                }
                escuadrones.add(e);
                System.out.println(rango_enemigo+"enemigos--------");
                String aux=String.valueOf(nombre);
                if(aux.equals("Z")){
                    nombre='A';
                }
            }


            // Heroes heroe=new Heroes("caballero",100,50,50,"knight.png",100,100,0);

            int opcionN=r.nextInt(escuadrones.size());
           /*for(int i=0;i<escuadrones.get(opcionN).getEnemigos().size();i++){

            }*/
            int valor=r.nextInt(escuadrones.get(opcionN).getEnemigos().size());
            escuadrones.get(opcionN).getEnemigo(valor).setJefe(-1);
            System.out.println(escuadrones.get(opcionN).getEnemigo(valor).getId()+"***********"+escuadrones.get(opcionN).getEnemigo(valor).getJefe());


            int opcionS=opcionN;
            while(opcionS==opcionN){
                opcionS=r.nextInt(escuadrones.size());
            }
            valor=r.nextInt(escuadrones.get(opcionS).getEnemigos().size());
            escuadrones.get(opcionS).getEnemigo(valor).setJefe(1);
            escuadrones.get(opcionS).setId(9);//Esto borrar porque solo presentacion
            System.out.println(escuadrones.get(opcionS).getEnemigo(valor).getId()+"/////////////////"+escuadrones.get(opcionS).getEnemigo(valor).getJefe());
            datos.setEscruadrones(escuadrones);
            datos.llenar();
            //   datos=new Datos(datos.getIdUsuario(),heroe,escuadrones,datos.getId_mapa(),datos.getId_mapaViejo(),level,datos.getPuntuacionJugador());
        }
    }


    @Override
    public void show() {
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(this);
        Gdx.input.setInputProcessor(multiplexer);
        //Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.setView(camera);
        renderer.render();
        stage.act();

        stage.draw();
        colisonar();
        if(comprobarMuertos()){
            level++;
            if(level>=4){
                level=0;
            }
            String viejo=datos.getId_mapa();
            // game.setScreen(new Mapa(game,new Datos(datos.getIdUsuario(),level),level));
            datos.getHeroe().subirNivel();
            datos.setPuntuacionJugador(datos.getPuntuacionJugador()+5);
            datos.setId_mapaViejo(datos.getId_mapa());
            datos.setLevel(level);
            datos.vaciar();
            datos.getHeroe().setPoder(0);
            game.setScreen(new Mapa(game,datos));
            //    game.setScreen(new Mapa(game,new Datos(datos.getIdUsuario(),level,viejo,datos.getPuntuacionJugador())));
        }
    }


    public boolean comprobarMuertos(){
        if(datos.getEscruadrones().size()>0){
            return false;
        }else{
            System.out.println(datos.getEscruadrones().size()+"*******");
            return true;
        }
    }


    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {

        return false;
    }
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }



    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }


    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
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