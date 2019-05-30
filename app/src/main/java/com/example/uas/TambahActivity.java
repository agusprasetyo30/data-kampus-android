package com.example.uas;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.uas.models.Universitas;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

public class TambahActivity extends AppCompatActivity implements
	OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMyLocationButtonClickListener,
	GoogleMap.OnMyLocationClickListener,
	ActivityCompat.OnRequestPermissionsResultCallback {
	
	private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
	//	Untuk gambar
	private static final int PICK_IMAGE_REQUEST = 1;
	
	private GoogleMap mMap;
	private boolean mPermissionDenied = false;
	
	private Marker lokasi = null;
	double latitude = -7.296264;
	double longitude = 112.736672;
	private EditText txtNamaUniv;
	private EditText txtAlamat;
	private Spinner kabupaten;
	private EditText txtWebsite;
	private EditText txtLatitude;
	private EditText txtLongitude;
	private Button btnSimpan;
	
	// Upload logo
	private Uri mImageUri;
	
	private StorageReference mStorageRef;
	private DatabaseReference mDatabaseRef;
	private StorageTask mUploadTask;
	private Button btnUpload;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_tambah);
		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
			.findFragmentById(R.id.mapUpdate);
		mapFragment.getMapAsync(this);
		txtNamaUniv = findViewById(R.id.namaUnivUpdate);
		txtAlamat = findViewById(R.id.alamatUnivUpdate);
		kabupaten = findViewById(R.id.kabupatenUnivUpdate);
		txtLatitude = findViewById(R.id.latitudeUpdate);
		txtLongitude = findViewById(R.id.longitudeUpdate);
		btnUpload = findViewById(R.id.btnTambahLogo);
		btnSimpan = findViewById(R.id.btnUpdate);
		txtWebsite = findViewById(R.id.websiteUnivUpdate);
		
		mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
		mDatabaseRef = FirebaseDatabase.getInstance().getReference("mahasiswa");
		
		btnUpload.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				openFileChooser();
			}
		});
		
		btnSimpan.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mUploadTask != null && mUploadTask.isInProgress()) {
					Toast.makeText(getApplicationContext(), "Upload in progress", Toast.LENGTH_SHORT).show();
				} else {
					uploadFile();
				}
			}
		});
	}
	
	@Override
	public void onMapReady(GoogleMap googleMap) {
		mMap = googleMap;
		mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		mMap.getUiSettings().setZoomControlsEnabled(true);
		mMap.getUiSettings().setZoomGesturesEnabled(true);
		mMap.getUiSettings().setCompassEnabled(true);
		mMap.setOnMapClickListener(this);
		mMap.setOnMyLocationButtonClickListener(this);
		mMap.setOnMyLocationClickListener(this);
		enableMyLocation();
		
		// Add a marker in Sydney and move the camera
		LatLng lokasi = new LatLng(latitude, longitude);
		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lokasi, 13/2));
	}
	
	@Override
	public void onMapClick(LatLng latLng) {
		if (lokasi != null) {
			lokasi.remove();
			lokasi = null;
		}
		
		if (lokasi == null) {
//			Toast.makeText(this, latLng.toString(), Toast.LENGTH_LONG).show();
			txtLatitude.setText(String.valueOf(latLng.latitude));
			txtLongitude.setText(String.valueOf(latLng.longitude));
			
			LatLng data = new LatLng(latLng.latitude, latLng.longitude);
			MarkerOptions marker = new MarkerOptions().position(data).title("Lokasi terkini");
			lokasi = mMap.addMarker(marker);
			mMap.animateCamera(CameraUpdateFactory.newLatLng(data));
		}
	}
	
	// Ketika tombol ditekan
	@Override
	public boolean onMyLocationButtonClick() {
		Toast.makeText(this, "Lokasi Terkini", Toast.LENGTH_LONG).show();
		return false;
	}
	
	@Override
	public void onMyLocationClick(@NonNull Location location) {
		Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG).show();
	}
	
	private void enableMyLocation() {
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
			!= PackageManager.PERMISSION_GRANTED) {
			// Permission to access the location is missing.
			PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
				Manifest.permission.ACCESS_FINE_LOCATION, true);
		} else if (mMap != null) {
			// Access to the location has been granted to the app.
			mMap.setMyLocationEnabled(true);
		}
	}
	
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
														@NonNull int[] grantResults) {
		if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
			return;
		}
		
		if (PermissionUtils.isPermissionGranted(permissions, grantResults,
			Manifest.permission.ACCESS_FINE_LOCATION)) {
			// Enable the my location layer if the permission has been granted.
			enableMyLocation();
		} else {
			// Display the missing permission error dialog when the fragments resume.
			mPermissionDenied = true;
		}
	}
	
	@Override
	protected void onResumeFragments() {
		super.onResumeFragments();
		if (mPermissionDenied) {
			// Permission was not granted, display error dialog.
			showMissingPermissionError();
			mPermissionDenied = false;
		}
	}
	
	/**
	 * Displays a dialog with error message explaining that the location permission is missing.
	 */
	private void showMissingPermissionError() {
		PermissionUtils.PermissionDeniedDialog
			.newInstance(true).show(getSupportFragmentManager(), "dialog");
	}
	
	//	UPLOAD FOTO DAN TOMBOL
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
			&& data != null && data.getData() != null) {
			mImageUri = data.getData();
		}
	}
	
	private void openFileChooser() {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(intent, PICK_IMAGE_REQUEST);
	}
	
	private String getFileExtension(Uri uri) {
		ContentResolver cR = getContentResolver();
		MimeTypeMap mime = MimeTypeMap.getSingleton();
		return mime.getExtensionFromMimeType(cR.getType(uri));
	}
	
	private void uploadFile() {
		if (mImageUri != null) {
			final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
				+ "." + getFileExtension(mImageUri));
			
			mUploadTask = fileReference.putFile(mImageUri)
				.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
					private static final String TAG ="TambahActivity" ;
					@Override
					public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
						Toast.makeText(getApplicationContext(), "Data berhasil disimpan", Toast.LENGTH_LONG).show();
						
						Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
						while (!urlTask.isSuccessful());
						Uri downloadUrl = urlTask.getResult();
						
						Log.d(TAG, "onSuccess: firebase download url: " + downloadUrl.toString());
						
						Universitas universitas = new Universitas();
						universitas.setNama(txtNamaUniv.getText().toString());
						universitas.setAlamat(txtAlamat.getText().toString());
						universitas.setKabupaten(kabupaten.getSelectedItem().toString());
						universitas.setWebsite(txtWebsite.getText().toString());
						universitas.setLatitute(Double.valueOf(txtLatitude.getText().toString()));
						universitas.setLongitude(Double.valueOf(txtLongitude.getText().toString()));
						universitas.setUrlLogo(downloadUrl.toString());
						
						String uploadId = mDatabaseRef.push().getKey();
						mDatabaseRef.child(uploadId).setValue(universitas);
						clearData();
					}
				})
				.addOnFailureListener(new OnFailureListener() {
					@Override
					public void onFailure(@NonNull Exception e) {
						Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
					}
				})
				.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
					@Override
					public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
					}
				});
		} else {
			Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
		}
	}
	
	public void clearData() {
		txtNamaUniv.setText("");
		txtAlamat.setText("");
		txtWebsite.setText("");
		txtLongitude.setText("");
		txtLatitude.setText("");
		lokasi.remove();
		lokasi = null;
		kabupaten.setSelection(0);
		txtNamaUniv.requestFocus();
	}
	
}
