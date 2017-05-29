package com.hafidzquran.myhafidzquran.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.hafidzquran.myhafidzquran.R;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        Button btnTambah = (Button)findViewById(R.id.btn_add);
        Button btnRekap = (Button)findViewById(R.id.btn_rekap);
        btnTambah.setOnClickListener(this);
        btnRekap.setOnClickListener(this);
    }

    private void ambilDaftarSantri(){
        final ProgressDialog progress = new ProgressDialog(HomeActivity.this);
        progress.setMessage("Mengecek data..");
        progress.setCancelable(false);
        progress.show();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.BaseURL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
    }

    @Override
    public void onClick(View view) {
        int requestUbah = 1;
        int requestRekap = 2;
        switch (view.getId()){
            case R.id.btn_add:
                startActivityForResult(new Intent(HomeActivity.this, TambahActivity.class), requestUbah);
                break;
            case R.id.btn_rekap:
                startActivityForResult(new Intent(HomeActivity.this, RekapActivity.class), requestRekap);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                break;
            case 2:
                break;
        }
    }
}
