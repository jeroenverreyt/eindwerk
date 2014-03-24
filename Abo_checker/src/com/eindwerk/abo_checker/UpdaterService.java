package com.eindwerk.abo_checker;


import java.util.Calendar;


import android.app.Service;
import android.content.Intent;
import android.net.TrafficStats;
import android.os.IBinder;
import android.util.Log;
import android.widget.TextView;

public class UpdaterService extends Service {
	private static final String TAG = UpdaterService.class.getSimpleName();
	private Updater updater;
	public boolean isRunning = false;
	
	private int teller = 0;
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		updater = new Updater();
		if (!this.isRunning){
			updater.start();
			this.isRunning = true;
		}
		
		Log.d(TAG, "onCreated" + teller);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub

		

		Log.d(TAG, "onStart" + teller);
		return super.onStartCommand(intent, flags, startId);

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		if(this.isRunning){
			updater.interrupt();
			
		}
		updater = null;
	
		
		Log.d(TAG, "onDestroy'd");
	}
	
	public void insertInDatabase(){
		
		long totalData_from_boot, previous_totalData, rxData_from_boot, txData_from_boot, data_per_hour;
		int yearNow, monthNow, dayNow;
		DataHistory dh = new DataHistory(this);
		dh.open();
		
		//Totale data vanaf boot, rx(received), tx(transmitted)
		rxData_from_boot = TrafficStats.getMobileRxBytes()/1048576;
		txData_from_boot = TrafficStats.getMobileTxBytes()/1048576;
		totalData_from_boot = rxData_from_boot + txData_from_boot;
		
		//PER UUR = totalMB_from_boot - de vorige totalMB_from_boot
		//DATA halen uit databank
		previous_totalData = dh.getLastData();
		if (previous_totalData > totalData_from_boot || totalData_from_boot==0){
			 data_per_hour = totalData_from_boot;
		}else{
			 data_per_hour = totalData_from_boot - previous_totalData;
		}
		
		
		//get current date
		Calendar cal = Calendar.getInstance();
		yearNow = cal.get(Calendar.YEAR);
		monthNow = cal.get(Calendar.MONTH) + 1;
		dayNow = cal.get(Calendar.DAY_OF_MONTH);	
		
		
		dh.insertData(dayNow, monthNow, yearNow, totalData_from_boot, data_per_hour);
		
		dh.close();
		
	}

	// UPDATER THREAD
	class Updater extends Thread {
		static final long DELAY = 1000*10;
		private boolean isRunning=true;

		@Override
		public void run() {
			while (isRunning) {
							
				try {
					//Do something
					Log.d(TAG, "Updater running" + teller);
					teller+=1;
					insertInDatabase();
					
					//sleep
					Thread.sleep(DELAY);
				} catch (InterruptedException e) {
					//interrupted
					
					isRunning = false;
				}

			}
		}

	}

}
