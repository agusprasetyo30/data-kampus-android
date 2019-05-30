package com.example.uas.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.uas.DetailActivity;
import com.example.uas.LihatDataActivity;
import com.example.uas.R;
import com.example.uas.UpdateActivity;
import com.example.uas.models.Universitas;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserAdapterHolder> {
	private Context mContext;
	private ArrayList<Universitas> listUniversitas;
	private AdapterView.OnItemClickListener mListener;
	private FirebaseAuth auth;
	
	//Membuat Interfece
	public interface dataListener {
		void onDeleteData(Universitas data, int position);
	}
	
	//Deklarasi objek dari Interfece
	dataListener listener;
	
	public UserAdapter(ArrayList<Universitas> universitas, Context context) {
		mContext = context;
		listUniversitas = universitas;
		listener = (LihatDataActivity) context;
	}
	
	@NonNull
	@Override
	public UserAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
		View v = LayoutInflater.from(mContext).inflate(R.layout.activity_listlihatdata, parent, false);
		return new UserAdapterHolder(v);
	}
	
	@SuppressLint("SetTextI18n")
	@Override
	public void onBindViewHolder(@NonNull UserAdapterHolder holder, final int position) {
		final String namaUniversitas = listUniversitas.get(position).getNama();
		final String namaDaerah = listUniversitas.get(position).getKabupaten();
		
		holder.namaUniv.setText(namaUniversitas);
		holder.namaKabupaten.setText(namaDaerah);
		
		Universitas uploadCurrent = listUniversitas.get(position);
		//	holder.textViewName.setText(uploadCurrent.getName());
		Picasso.with(mContext)
			.load(uploadCurrent.getUrlLogo())
			.placeholder(R.mipmap.ic_launcher)
			.fit()
			.centerCrop()
			.into(holder.logoUniv);
		
		try {
			auth = FirebaseAuth.getInstance(); //Mendapakan Instance Firebase Autentifikasi
		} catch (Exception e) {
		
		}
		
		try {
			if (auth.getCurrentUser() != null) {
				//Menampilkan Menu Update dan Delete saat user melakukan long klik pada salah satu item
				holder.ListItem.setOnLongClickListener(new View.OnLongClickListener() {
					@Override
					public boolean onLongClick(final View view) {
						final String[] action = {"Update", "Delete"};
						AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());
						alert.setItems(action, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int i) {
								switch (i) {
									case 0:
							  /*
								 Berpindah Activity pada halaman layout updateData
								 dan mengambil data pada listMahasiswa, berdasarkan posisinya
								 untuk dikirim pada activity updateData
								*/
									Bundle bundle = new Bundle();
									bundle.putString("dataNama", listUniversitas.get(position).getNama());
									bundle.putString("dataAlamat", listUniversitas.get(position).getAlamat());
									bundle.putString("dataKabupaten", listUniversitas.get(position).getKabupaten());
									bundle.putString("dataWebsite", listUniversitas.get(position).getWebsite());
									bundle.putDouble("dataLatitute", listUniversitas.get(position).getLatitute());
									bundle.putDouble("dataLongitute", listUniversitas.get(position).getLongitude());
									bundle.putString("dataUrlLogo", listUniversitas.get(position).getUrlLogo());
									bundle.putString("getPrimaryKey", listUniversitas.get(position).getKey());
									Intent intent = new Intent(view.getContext(), UpdateActivity.class);
									intent.putExtras(bundle);
									mContext.startActivity(intent);
									break;
									
									case 1:
										//Menggunakan interface untuk mengirim data mahasiswa, yang akan dihapus
										listener.onDeleteData(listUniversitas.get(position), position);
										break;
								}
							}
						});
						alert.create();
						alert.show();
						return true;
					}
				});
			}
		} catch (Exception e) {
		
		}
		
		holder.ListItem.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Bundle bundle = new Bundle();
				bundle.putString("dataNama", listUniversitas.get(position).getNama());
				bundle.putString("dataAlamat", listUniversitas.get(position).getAlamat());
				bundle.putString("dataKabupaten", listUniversitas.get(position).getKabupaten());
				bundle.putString("dataWebsite", listUniversitas.get(position).getWebsite());
				bundle.putDouble("dataLatitute", listUniversitas.get(position).getLatitute());
				bundle.putDouble("dataLongitute", listUniversitas.get(position).getLongitude());
				bundle.putString("dataUrlLogo", listUniversitas.get(position).getUrlLogo());
				bundle.putString("getPrimaryKey", listUniversitas.get(position).getKey());
				Intent intent = new Intent(v.getContext(), DetailActivity.class);
				intent.putExtras(bundle);
				mContext.startActivity(intent);
			}
		});
	}
	
	@Override
	public int getItemCount() {
		return listUniversitas.size();
	}
	
	public class UserAdapterHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
		View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
		private TextView namaUniv;
		private TextView namaKabupaten;
		private ImageView logoUniv;
		private LinearLayout ListItem;
		
		public UserAdapterHolder(@NonNull View itemView) {
			super(itemView);
			
			namaUniv = itemView.findViewById(R.id.namaUnivUpdate);
			namaKabupaten = itemView.findViewById(R.id.daerahUniv);
			logoUniv = itemView.findViewById(R.id.logoUniv);
			ListItem = itemView.findViewById(R.id.list_item);
			
		}
		
		@Override
		public boolean onMenuItemClick(MenuItem item) {
			return false;
		}
		
		@Override
		public void onClick(View v) {
		
		}
		
		@Override
		public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
		
		}
	}
}
