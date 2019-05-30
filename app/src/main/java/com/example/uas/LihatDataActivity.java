package com.example.uas;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.uas.adapters.UserAdapter;
import com.example.uas.models.Universitas;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LihatDataActivity extends AppCompatActivity implements UserAdapter.dataListener  {
	private FirebaseAuth auth;
	
	//Deklarasi Variable untuk RecyclerView
	private RecyclerView recyclerView;
	private RecyclerView.Adapter adapter;
	private RecyclerView.LayoutManager layoutManager;
	
	//Deklarasi Variable Database Reference dan ArrayList dengan Parameter Class Model kita.
	private DatabaseReference reference;
	private ArrayList<Universitas> dataUniversitas;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lihat_data);
		recyclerView = (RecyclerView) findViewById(R.id.datalist);
		getSupportActionBar().setTitle("Data Universitas");
		MyRecyclerView();
		GetData();
	}
	
	//Berisi baris kode untuk mengambil data dari Database dan menampilkannya kedalam Adapter
	private void GetData(){
		//Mendapatkan Referensi Database
		reference = FirebaseDatabase.getInstance().getReference();
		reference.child("mahasiswa")
			.addValueEventListener(new ValueEventListener() {
				@Override
				public void onDataChange(DataSnapshot dataSnapshot) {
					//Inisialisasi ArrayList
					dataUniversitas = new ArrayList<>();
					
					for (DataSnapshot snapshot : dataSnapshot.getChildren()){
						//Mapping data pada DataSnapshot ke dalam objek mahasiswa
						Universitas mahasiswa = snapshot.getValue(Universitas.class);
						
						//Mengambil Primary Key, digunakan untuk proses Update dan Delete
						mahasiswa.setKey(snapshot.getKey());
						dataUniversitas.add(mahasiswa);
					}
					
					//Inisialisasi Adapter dan data Mahasiswa dalam bentuk Array
					adapter = new UserAdapter(dataUniversitas, LihatDataActivity.this);
					
					//Memasang Adapter pada RecyclerView
					recyclerView.setAdapter(adapter);
				}
				
				@Override
				public void onCancelled(DatabaseError databaseError) {
              /*
                Kode ini akan dijalankan ketika ada error dan
                pengambilan data error tersebut lalu memprint error nya
                ke LogCat
               */
					Toast.makeText(getApplicationContext(),"Data Gagal Dimuat", Toast.LENGTH_LONG).show();
					Log.e("MyListActivity", databaseError.getDetails()+" "+databaseError.getMessage());
				}
			});
	}
	
	//Methode yang berisi kumpulan baris kode untuk mengatur RecyclerView
	private void MyRecyclerView(){
		//Menggunakan Layout Manager, Dan Membuat List Secara Vertical
		layoutManager = new LinearLayoutManager(this);
		recyclerView.setLayoutManager(layoutManager);
		recyclerView.setHasFixedSize(true);
		
		//Membuat Underline pada Setiap Item Didalam List
		DividerItemDecoration itemDecoration = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL);
		itemDecoration.setDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.line));
		recyclerView.addItemDecoration(itemDecoration);
	}
	
	@Override
	public void onDeleteData(Universitas data, int position) {
//		String userID = auth.getUid();
		if(reference != null) {
			reference.child("mahasiswa")
				.child(data.getKey())
				.removeValue()
				.addOnSuccessListener(new OnSuccessListener<Void>() {
					@Override
					public void onSuccess(Void aVoid) {
						Toast.makeText(LihatDataActivity.this, "Data Berhasil Dihapus", Toast.LENGTH_SHORT).show();
					}
				});
		}
	}
}
