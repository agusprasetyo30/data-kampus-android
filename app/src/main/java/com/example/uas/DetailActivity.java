package com.example.uas;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity implements OnMapReadyCallback {
	private TextView namaUniv;
	private TextView alamatUniv;
	private TextView kabupatenUniv;
	private TextView websiteUniv;
	private ImageView logoUniv;
	private Double latitude ;
	private Double longitude;
	
//	Map
	private GoogleMap mMap;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
			.findFragmentById(R.id.mapDetil);
		mapFragment.getMapAsync(this);
		
		namaUniv = findViewById(R.id.namaUnivUpdate);
		alamatUniv = findViewById(R.id.alamatUnivUpdate);
		kabupatenUniv = findViewById(R.id.kabupatenUnivUpdate);
		websiteUniv = findViewById(R.id.websiteUnivUpdate);
		logoUniv = findViewById(R.id.logoUnivDetil);
		getData();
	}
	
	//Menampilkan data yang akan di update
	private void getData() {
		final String getNama = getIntent().getExtras().getString("dataNama");
		final String getAlamat = getIntent().getExtras().getString("dataAlamat");
		final String getKabupaten = getIntent().getExtras().getString("dataKabupaten");
		final String getWebsite = getIntent().getExtras().getString("dataWebsite");
		final String getUrl = getIntent().getExtras().getString("dataUrlLogo");
		final Double getLatitude = getIntent().getExtras().getDouble("dataLatitute");
		final Double getLongitude = getIntent().getExtras().getDouble("dataLongitute");
		namaUniv.setText(getNama.toString());
		alamatUniv.setText(getAlamat.toString());
		kabupatenUniv.setText(getKabupaten.toString());
		websiteUniv.setText(getWebsite.toString());
		latitude = getLatitude;
		longitude = getLongitude;
		
		Picasso.with(DetailActivity.this)
			.load(getUrl)
			.placeholder(R.mipmap.ic_launcher)
			.fit()
			.centerInside()
			.into(logoUniv);
	}
	
	@Override
	public void onMapReady(GoogleMap googleMap) {
		mMap = googleMap;
		mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		mMap.getUiSettings().setZoomControlsEnabled(true);
		mMap.getUiSettings().setZoomGesturesEnabled(true);
		mMap.getUiSettings().setCompassEnabled(true);
		
		// Add a marker in Sydney and move the camera
		LatLng lokasi = new LatLng(latitude, longitude);
		MarkerOptions marker = new MarkerOptions().position(lokasi).title(namaUniv.getText().toString());
		mMap.addMarker(marker);
		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lokasi, 16));
	}
}
