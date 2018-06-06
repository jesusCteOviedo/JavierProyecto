package Test;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.mygdx.game.MenuInicio;

public class GameOver  implements Screen {

    private Stage stage;
    private Button menuInicio;
    private Button volverMapa;
    private Skin skin;
    private Label label;
    private Game game;

    public GameOver(Game agame){
        this.game=agame;
        stage=new Stage();
        skin=new Skin(Gdx.files.internal("skin/glassy-ui.json"));

        int row_height = Gdx.graphics.getWidth() / 9;
        int col_width = Gdx.graphics.getWidth() / 2;

        label=new Label("GAME OVER",skin,"big-black");
        label.setPosition(Gdx.graphics.getWidth()/2-250,Gdx.graphics.getHeight()/2+300);
        menuInicio=new TextButton("Volver Menu Principal", skin, "default");
        menuInicio.setSize(col_width,row_height);
        menuInicio.setPosition(Gdx.graphics.getWidth()/2-450,Gdx.graphics.getHeight()/2-150);
        menuInicio.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new MenuInicio(game));
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        stage.addActor(label);
        stage.addActor(menuInicio);
    }


    @Override
    public void show() {

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
