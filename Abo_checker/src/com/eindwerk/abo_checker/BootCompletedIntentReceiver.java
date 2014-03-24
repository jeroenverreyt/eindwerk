package com.eindwerk.abo_checker;


import android.content.BroadcastReceiver;  
import android.content.Context;  
import android.content.Intent;  
import android.content.SharedPreferences;  
import android.preference.PreferenceManager;  

//service laten starten bij boot

public class BootCompletedIntentReceiver extends BroadcastReceiver {  
@Override  
public void onReceive(Context context, Intent intent) {  
if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {  
 Intent pushIntent = new Intent(context, UpdaterService.class);  
 context.startService(pushIntent);  
}  
}  
}  
