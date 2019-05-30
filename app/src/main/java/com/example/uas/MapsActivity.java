package com.example.uas;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, OnMapClickListener{
	
	private GoogleMap mMap;
	private EditText txtKordinat;
	double latitude = -7.946487;
	double longitude = 112.615601;
	private Marker lokasi = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maps);
		// Obtain the SupportMapFragment and get notified when the map is ready to be used.
		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
			.findFragmentById(R.id.map);
		mapFragment.getMapAsync(this);
		txtKordinat = findViewById(R.id.txtKordinat);
	}
	
	
	/**
	 * Manipulates the map once available.
	 * This callback is triggered when the map is ready to be used.
	 * This is where we can add markers or lines, add listeners or move the camera. In this case,
	 * we just add a marker near Sydney, Australia.
	 * If Google Play services is not installed on the device, the user will be prompted to install
	 * it inside the SupportMapFragment. This method will only be triggered once the user has
	 * installed Google Play services and returned to the app.
	 */
	@Override
	public void onMapReady(GoogleMap googleMap) {
		mMap = googleMap;
		mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		mMap.getUiSettings().setZoomControlsEnabled(true);
		mMap.getUiSettings().setZoomGesturesEnabled(true);
		mMap.getUiSettings().setCompassEnabled(true);
		mMap.setOnMapClickListener(this);
		
		// Add a marker in Sydney and move the camera
		LatLng sydney = new LatLng(latitude, longitude);
//		mMap.addMarker(new MarkerOptions().position(sydney).title("Politeknik negeri malang"));
		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15));
	}
	
	@Override
	public void onMapClick(LatLng latLng) {
		if (lokasi != null) {
			lokasi.remove();
			lokasi = null;
		}
		
		if (lokasi == null) {
			Toast.makeText(this, latLng.toString(), Toast.LENGTH_LONG).show();
			txtKordinat.setText(latLng.toString());
			
			LatLng data = new LatLng(latLng.latitude, latLng.longitude);
			MarkerOptions marker = new MarkerOptions().position(data).title(latLng.toString());
			lokasi = mMap.addMarker(marker);
			mMap.animateCamera(CameraUpdateFactory.newLatLng(data));
		}
		
	}
	
}
