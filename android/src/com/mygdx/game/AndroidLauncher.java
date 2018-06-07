package com.mygdx.game;



import android.content.Intent;
import android.os.Bundle;



import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;


public class AndroidLauncher extends AndroidApplication {




	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		String message = intent.getStringExtra(empezar.EXTRA_MESSAGE);

		Segundoplano s=new Segundoplano();
		s.execute();


		initialize(new Main(message),config);





	}



}
