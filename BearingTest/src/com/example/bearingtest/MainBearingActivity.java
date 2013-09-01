package com.example.bearingtest;

import java.util.List;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
// Inflate the menu; this adds items to the action bar if it is present.
//////////////////////////////////////////

public class MainBearingActivity extends Activity {
	volatile float bearing = 0;
	
	volatile float lat = 0;
	
	volatile float lng = 0;
	
	GingerbreadLastLocationFinder bestLocator;
	
	float red = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(new InnerView(this));
		bestLocator = new GingerbreadLastLocationFinder(this);
		InnerThread locationUpdate = new InnerThread();
		locationUpdate.start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_bearing, menu);
		return true;
	}

	// ////////////////////////////////////////

	// //////////////////////////////////////////
	private class InnerView extends View {

		private Paint textPaint = new Paint();

		public InnerView(Context context) {
			super(context);
			textPaint.setTextSize(40);
		}

		@Override
		public void onDraw(Canvas c) {
			c.drawARGB(255,
					(int) (255 * Math.pow(Math.sin(red += 0.00051), 2)), 140,
					240);

			c.drawText("bearing: " + bearing, 20, 200, textPaint);
			c.drawText("latitud:" + lat, 20, 250, textPaint);
			c.drawText("longtud:" + lng, 20, 300, textPaint);
			invalidate();
		}
	}

	class InnerThread extends Thread {
		public void run() {
			while (true) {
				int minDistance = 1000000;
				int minTime = 1000000;
				Location bestL = bestLocator.getLastBestLocation(minDistance,
						minTime);
				bearing = bestL.getBearing();
				lat = (float) bestL.getLatitude();
				lng = (float) bestL.getLongitude();
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

}
