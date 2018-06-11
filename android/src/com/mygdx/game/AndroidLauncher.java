package com.mygdx.game;



import android.content.Intent;
import android.os.Bundle;



import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import bbdd.CargarPartida;
import bbdd.SegundoplanoCopia;


public class AndroidLauncher extends AndroidApplication {




	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		String message = intent.getStringExtra(empezar.EXTRA_MESSAGE);

		/*Segundoplano s=new Segundoplano();
		s.execute();*/
		/*CargarPartida c=new CargarPartida();
		c.cargarPartida(this);*/

		SegundoplanoCopia s=new SegundoplanoCopia(this);
		s.execute();


		initialize(new Main(message),config);





	}



}
