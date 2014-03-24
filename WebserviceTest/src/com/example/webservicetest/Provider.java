package com.example.webservicetest;

import java.awt.List;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class Provider extends ListActivity{

		
	String sms, bel, data;
	private int success;
	private ListView output;
	String sOutput = "";
	JSONParser jsonParser = new JSONParser();
	JSONArray listProv = null;

	//arraylist maken
	ArrayList<HashMap<String, String>> providerList;
	
	
	//link naar de webservice, ip adres moet uiteindelijk adres van onze host worden
	private static final String INDEX_URL = "http://192.168.0.107/webservice_test/index.php";

	//"succes" verwijst naar de variabele succes in onze JSONData dat we aanmaken in de webservice
	private static final String TAG_SUCCESS = "succes";
	private static final String TAG_NAME = "Name";
	private static final String TAG_PROVIDER = "Provider";
	private static final String TAG_PRICE = "Price";
	
	//de array prov (met alle gevonden providers)
	private static final String TAG_PROV = "prov";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.provider);
		
		
			
		providerList = new ArrayList<HashMap<String,String>>();
		
		output = getListView();
		
		
		
		Bundle gotBasket = getIntent().getExtras();
		sms = gotBasket.getString("sms");
		new getProviders().execute();		
	}

	
	
	class getProviders extends AsyncTask<String, String, String>{

		@Override
		protected String doInBackground(String... args) {
			// TODO Auto-generated method stub
			
			
			
			try{
				
				ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
				//paramater sms verwijst naar de POST variabele sms in onze webservice
				params.add(new BasicNameValuePair("sms", sms));
				
				// hier voer je de POST uit op de webservice
				JSONObject json = jsonParser.makeHttpRequest(INDEX_URL, "POST", params);
				
				//de waarde uit de JSONData wordt hier gehaald
				success = json.getInt(TAG_SUCCESS);
				
				//alle gevonden tarieven in een array
				listProv = json.getJSONArray(TAG_PROV);
				
				
				for (int i=0; i<listProv.length(); i++){
					
					//array afgaan en elke naam er uit printen
					JSONObject c = listProv.getJSONObject(i);
					
					// tmp hashmap voor één tarief
                    HashMap<String, String> tarief = new HashMap<String, String>();
					
					String name = c.getString(TAG_NAME);
					String provider = c.getString(TAG_PROVIDER);
					String price = c.getString(TAG_PRICE) + "€ per maand";
					
					tarief.put(TAG_NAME, name);
					tarief.put(TAG_PROVIDER, provider);
					tarief.put(TAG_PRICE, price);
					
					// adding tarief to provider list
                    providerList.add(tarief);
					
				
					
				}
			

			}catch(JSONException e){
				e.printStackTrace();
			}
			
			
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			//output MOET in onPostExecute, als je dit doet in doInBackground crasht de applicatie
		
			ListAdapter adapter = new SimpleAdapter(
                    Provider.this, providerList,
                    R.layout.list_item, new String[] { TAG_PROVIDER, TAG_NAME,
                            TAG_PRICE }, new int[] { R.id.tvProvider,
                            R.id.tvBundel, R.id.tvPrice });
 
            setListAdapter(adapter);
			
		}
		
		
		
	}
}
