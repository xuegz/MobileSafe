package com.example.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

public class LocationService extends Service {
	private LocationManager manager;
	private MyLocationListener my;
	private SharedPreferences sp;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		manager = (LocationManager) getSystemService(LOCATION_SERVICE);
		my = new MyLocationListener();
		Criteria criteria=new Criteria();
		String provider=manager.getBestProvider(criteria, true);
		criteria.setCostAllowed(true);
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		manager.requestLocationUpdates(provider,0, 0,my);
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		manager.removeUpdates(my);
	}
	
	class MyLocationListener implements LocationListener{
		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			String longitude = "经度：" + location.getLongitude();
			String latitude = "纬度：" + location.getLatitude();
			//String accuracy = "精度：" + location.getAccuracy();
			String myLocation=longitude+","+latitude;
			sp=getSharedPreferences("info", MODE_PRIVATE);
			sp.edit().putString("location", myLocation).commit();
			stopSelf();//停止service
		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			System.out.println("onProviderDisabled");
		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			System.out.println(provider);
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			System.out.println("onStatusChanged");
		}
		
	}
}
