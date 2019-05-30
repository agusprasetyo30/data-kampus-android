package com.example.uas;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class AdminActivity extends AppCompatActivity implements View.OnClickListener{
	private Button btnLogout;
	private Button btnTambah;
	private Button btnLihatData;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin);
		btnLogout = findViewById(R.id.buttonLogout);
		btnLogout.setOnClickListener(this);
		btnTambah = findViewById(R.id.buttonTambahData);
		btnTambah.setOnClickListener(this);
		btnLihatData = findViewById(R.id.buttonLihatData);
		btnLihatData.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.buttonLogout :
				AuthUI.getInstance()
					.signOut(this)
					.addOnCompleteListener(new OnCompleteListener() {
						@Override
						public void onComplete(@NonNull Task task) {
							Toast.makeText(AdminActivity.this, "Logout Berhasil", Toast.LENGTH_SHORT).show();
							startActivity(new Intent(AdminActivity.this, MainActivity.class));
							finish();
						}
					});
			break;
			
			case R.id.buttonTambahData :
				startActivity(new Intent(AdminActivity.this, TambahActivity.class));
				break;
				
			case R.id.buttonLihatData:
				startActivity(new Intent(AdminActivity.this, LihatDataActivity.class));
				break;
			
		}
	}
}
