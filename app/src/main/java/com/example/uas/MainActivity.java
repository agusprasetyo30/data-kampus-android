package com.example.uas;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Collections;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
	private Button btnLihatData;
	private Button btnLogin;
	private ProgressBar progressBar;
	private FirebaseAuth auth;
	
	//Membuat Kode Permintaan
	private int RC_SIGN_IN = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		progressBar = findViewById(R.id.progress);
		progressBar.setVisibility(View.GONE);
		btnLihatData = findViewById(R.id.buttonLihatData);
		btnLogin = findViewById(R.id.buttonLogin);
		
		btnLihatData.setOnClickListener(this);
		btnLogin.setOnClickListener(this);
		
		try {
			auth = FirebaseAuth.getInstance(); //Mendapakan Instance Firebase Autentifikasi
		}catch (Exception e){
			Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
		}
		
		try {
			if(auth.getCurrentUser() != null){
				updateUI();
			}
		}catch (Exception e){
			Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}
	
	//Tampilan User Interface pada Activity setelah user Terautentikasi
	private void updateUI(){
		startActivity(new Intent(MainActivity.this, AdminActivity.class));
		progressBar.setVisibility(View.GONE);
		finish();
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.buttonLihatData :
				startActivity(new Intent(MainActivity.this, LihatDataActivity.class));
				break;
			
			case R.id.buttonLogin :
				// Statement program untuk login/masuk
				startActivityForResult(AuthUI.getInstance()
						.createSignInIntentBuilder()
						
						//Memilih Provider atau Method masuk yang akan kita gunakan
						.setAvailableProviders(Collections.singletonList(new AuthUI.IdpConfig.GoogleBuilder().build()))
						.setIsSmartLockEnabled(false)
						.build(),
					RC_SIGN_IN);
				progressBar.setVisibility(View.VISIBLE);
				break;
		}
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// RC_SIGN_IN adalah kode permintaan yang Anda berikan ke startActivityForResult, saat memulai masuknya arus.
		if (requestCode == RC_SIGN_IN) {
			
			//Berhasil masuk
			if (resultCode == RESULT_OK) {
				Toast.makeText(MainActivity.this, "Login Berhasil", Toast.LENGTH_SHORT).show();
				updateUI();
			}else {
				progressBar.setVisibility(View.GONE);
				Toast.makeText(MainActivity.this, "Login Dibatalkan", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
}
