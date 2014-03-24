package com.eindwerk.abo_checker;


import java.util.Calendar;
import java.util.Date;
import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


public class HuidigVerbruik extends Activity implements OnClickListener {

	private static final String CONTENT_SMS = "content://sms";
	public static final String ACTION_NEW_OUTGOING_SMS = "android.provider.Telephony.NEW_OUTGOING_SMS";

	long rxBytes;
	long txBytes;
	long totalMegaBytes;

	int monthDifference, yearDifference, yearNow, monthNow;

	TextView tvSms, tvBel, tvData;
	Button pMonth, nMonth;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.huidig_verbruik);

		initVars();
		callHistory();
		smsHistory();
		dataHistory();
	}

	private void initVars() {
		yearNow = monthNow = monthDifference = yearDifference = 0;
		// Datum van vandaag
		Calendar cal = Calendar.getInstance();
		yearNow = cal.get(Calendar.YEAR);
		monthNow = cal.get(Calendar.MONTH) + 1;

		

		tvSms = (TextView) findViewById(R.id.tvAantalSms);
		tvBel = (TextView) findViewById(R.id.tvAantalBelminuten);
		tvData = (TextView) findViewById(R.id.tvAantalData);
		pMonth = (Button) findViewById(R.id.bPrevious);
		nMonth = (Button) findViewById(R.id.bNext);

		pMonth.setOnClickListener(this);
		nMonth.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {

		case R.id.bNext:

			// naar volgende maand gaan
			if ((monthNow + monthDifference) == 12) {
				monthDifference = 1 - monthNow;
				yearDifference += 1;

			} else {
				monthDifference += 1;
			}

			callHistory();
			smsHistory();
			dataHistory();

			break;

		case R.id.bPrevious:

			// naar vorige maand gaan
			if ((monthNow + monthDifference) == 1) {
				monthDifference = 12 - monthNow;
				yearDifference -= 1;
			} else {
				monthDifference -= 1;
			}

			callHistory();
			smsHistory();
			dataHistory();
			break;

		}
	}

	private void callHistory() {

		Cursor mCursor = getContentResolver().query(CallLog.Calls.CONTENT_URI,
				null, null, null, null);

		int date = mCursor.getColumnIndex(CallLog.Calls.DATE);
		int duration = mCursor.getColumnIndex(CallLog.Calls.DURATION);
		int type = mCursor.getColumnIndex(CallLog.Calls.TYPE);

		int durationPerMonth = 0;

		while (mCursor.moveToNext()) {
			
			String callduration = mCursor.getString(duration);
			String calltype = mCursor.getString(type);
			String calldate = mCursor.getString(date);
			Date d = new Date(Long.valueOf(calldate));

			if (d.getMonth() + 1 == monthNow + monthDifference
					&& d.getYear() + 1900 == yearNow + yearDifference) {
				switch (Integer.parseInt(calltype)) {
				case CallLog.Calls.OUTGOING_TYPE:
					
					// enkel de uitgaande belminuten hebben we nodig
					durationPerMonth += Integer.parseInt(callduration);

					break;
				case CallLog.Calls.INCOMING_TYPE:
					
					break;
				case CallLog.Calls.MISSED_TYPE:
					
					break;
				}

			}

		}

		tvBel.setText(durationPerMonth + " seconden");

	}

	private void smsHistory() {
		
		//verzonden berichten hebben type 2 en protocol is null

		Cursor mCursor = getContentResolver().query(Uri.parse(CONTENT_SMS),
				null, "type = 2 AND protocol is null", null, null);

		int totalSendMessages = 0;

		int date = mCursor.getColumnIndex("date");
		String calldate = "";
		
	
		while (mCursor.moveToNext()) {

			calldate = mCursor.getString(date);
			Date d = new Date(Long.valueOf(calldate));
		
			if (d.getMonth() + 1 == monthNow + monthDifference
					&& d.getYear() + 1900 == yearNow + yearDifference) {

				totalSendMessages += 1;

			}

		}

		tvSms.setText(totalSendMessages + "");

	}

	private void dataHistory() {

		DataHistory dh = new DataHistory(this);
		String totalData;
		dh.open();
		totalData = dh.getData(monthNow + monthDifference, yearNow
				+ yearDifference);

		tvData.setText(totalData + "");
		dh.close();
	}

}
