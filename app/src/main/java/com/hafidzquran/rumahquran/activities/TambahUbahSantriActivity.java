package com.hafidzquran.rumahquran.activities;

import android.app.ProgressDialog;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.hafidzquran.rumahquran.R;
import com.hafidzquran.rumahquran.SessionManager;
import com.hafidzquran.rumahquran.apirequests.TambahSantriBody;
import com.hafidzquran.rumahquran.apirequests.UbahSantriBody;
import com.hafidzquran.rumahquran.apiresults.TambahSantriResult;
import com.hafidzquran.rumahquran.apiresults.UbahSantriResult;
import com.hafidzquran.rumahquran.apiservices.TambahSantriService;
import com.hafidzquran.rumahquran.apiservices.UbahSantriService;
import com.hafidzquran.rumahquran.ui.RuquButton;
import com.hafidzquran.rumahquran.ui.RuquEditText;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TambahUbahSantriActivity extends AppCompatActivity implements View.OnClickListener {
    private RuquEditText edtNama;
    private Retrofit retrofit;
    private ProgressDialog progress;
    private AlertDialog.Builder builder;
    private SessionManager sesi;
    private Spinner spinTahun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_ubah_santri);

        retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.BaseURL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        sesi = new SessionManager(this);
        spinTahun = (Spinner)findViewById(R.id.spin_tahun);
        ArrayList<String> years = new ArrayList<>();
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = 2010; i <= thisYear; i++) {
            years.add(Integer.toString(i));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, years);
        spinTahun.setAdapter(adapter);
        edtNama = (RuquEditText) findViewById(R.id.edt_nama_santri);
        RuquButton btnSimpan = (RuquButton) findViewById(R.id.btn_simpan);
        edtNama.setText(getIntent().getStringExtra("nama"));
        btnSimpan.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_simpan:
                if (edtNama.getText().toString().equals("") ||
                        spinTahun.getSelectedItem().toString().equals("")){
                    Toast.makeText(this, "Nama dan tahun tidak boleh kosong", Toast.LENGTH_SHORT).show();
                } else if (getIntent().getStringExtra("nama")!=null){
                    ubahSantri();
                } else {
                    tambahSantribaru();
                }
                break;
        }
    }

    private void tambahSantribaru(){
        progress = new ProgressDialog(TambahUbahSantriActivity.this);
        progress.setMessage("Menyimpan data santri..");
        progress.setCancelable(false);
        progress.show();
        builder = new AlertDialog.Builder(TambahUbahSantriActivity.this);
        TambahSantriBody body = new TambahSantriBody(edtNama.getText().toString(),
                spinTahun.getSelectedItem().toString(),
                sesi.getUserDetails().get("username"));
        TambahSantriService service = retrofit.create(TambahSantriService.class);
        Call<TambahSantriResult> result = service.getRespond(body);
        result.enqueue(new Callback<TambahSantriResult>() {
            @Override
            public void onResponse(Call<TambahSantriResult> call, Response<TambahSantriResult> response) {
                if (response.body() != null){
                    progress.dismiss();
                    if (response.body().getCODE().equals("0")){
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        builder.setMessage(response.body().getDESC());
                        builder.setCancelable(false);
                        builder.setPositiveButton("OK", null);
                        builder.create().show();
                    }
                } else {
                    progress.dismiss();
                    builder.setMessage("Server tidak ditemukan");
                    builder.setCancelable(false);
                    builder.setPositiveButton("OK", null);
                    builder.create().show();
                }
            }

            @Override
            public void onFailure(Call<TambahSantriResult> call, Throwable t) {
                progress.dismiss();
                builder.setMessage("Tidak dapat tersambung ke server, cek koneksi internet anda");
                builder.setCancelable(false);
                builder.setPositiveButton("OK", null);
                builder.create().show();
            }
        });
    }

    private void ubahSantri(){
        progress = new ProgressDialog(TambahUbahSantriActivity.this);
        progress.setMessage("Menyimpan data santri..");
        progress.setCancelable(false);
        progress.show();
        builder = new AlertDialog.Builder(TambahUbahSantriActivity.this);
        UbahSantriBody body = new UbahSantriBody(getIntent().getStringExtra("id"),
                edtNama.getText().toString(),
                spinTahun.getSelectedItem().toString());
        UbahSantriService service = retrofit.create(UbahSantriService.class);
        Call<UbahSantriResult> result = service.getRespond(body);
        result.enqueue(new Callback<UbahSantriResult>() {
            @Override
            public void onResponse(Call<UbahSantriResult> call, Response<UbahSantriResult> response) {
                if (response.body() != null){
                    progress.dismiss();
                    if (response.body().getCODE().equals("0")){
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        builder.setMessage(response.body().getDESC());
                        builder.setCancelable(false);
                        builder.setPositiveButton("OK", null);
                        builder.create().show();
                    }
                } else {
                    progress.dismiss();
                    builder.setMessage("Server tidak ditemukan");
                    builder.setCancelable(false);
                    builder.setPositiveButton("OK", null);
                    builder.create().show();
                }
            }

            @Override
            public void onFailure(Call<UbahSantriResult> call, Throwable t) {
                progress.dismiss();
                builder.setMessage("Tidak dapat tersambung ke server, cek koneksi internet anda");
                builder.setCancelable(false);
                builder.setPositiveButton("OK", null);
                builder.create().show();
            }
        });
    }
}
