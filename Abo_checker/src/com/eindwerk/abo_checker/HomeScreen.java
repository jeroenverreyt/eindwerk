package com.eindwerk.abo_checker;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class HomeScreen extends Activity implements OnClickListener {

	Button bBerekenTarief, bHuidigVerbruik, bInformatie;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_screen);
		
		initVars();
	}

	private void initVars() {
		
		bBerekenTarief = (Button)findViewById(R.id.bHomeBereken);
		bHuidigVerbruik = (Button)findViewById(R.id.bHomeHuidigVerbruik);
		bInformatie = (Button)findViewById(R.id.bHomeInformatie);
		
		bBerekenTarief.setOnClickListener(this);
		bHuidigVerbruik.setOnClickListener(this);
		bInformatie.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		
		switch(v.getId()){
		case R.id.bHomeBereken:
			//om service starten te testen, moet in oncreate 
			startService(new Intent(this, UpdaterService.class));
			break;
			
		case R.id.bHomeHuidigVerbruik:
			Intent i = new Intent(HomeScreen.this, HuidigVerbruik.class);
			startActivity(i);
			
			
			break;
			
		case R.id.bHomeInformatie:
			//om service stoppen te testen
			stopService(new Intent(this, UpdaterService.class));
			break;
		}
		
		
	}

	

}
