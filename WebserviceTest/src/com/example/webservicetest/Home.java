package com.example.webservicetest;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class Home extends Activity implements OnClickListener{

	EditText etSms, etBel, etData; 
	Button calculate;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		
		etSms = (EditText)findViewById(R.id.etSms);
		etBel = (EditText)findViewById(R.id.etBelminuten);
		etData = (EditText)findViewById(R.id.etData);
		calculate = (Button)findViewById(R.id.bCalculate);
		calculate.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		String sms = etSms.getText().toString();
		String bel = etBel.getText().toString();
		String data = etData.getText().toString();
		
		Bundle basket = new Bundle();
		basket.putString("sms", sms);
		basket.putString("bel", bel);
		basket.putString("data", data);
		Intent i = new Intent(Home.this, Provider.class);
		i.putExtras(basket);
		startActivity(i);
		
	}

	
	
	
}
