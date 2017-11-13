package com.hafidzquran.rumahquran.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import com.hafidzquran.rumahquran.R;
import com.hafidzquran.rumahquran.SessionManager;
import com.hafidzquran.rumahquran.apirequests.GetDataGuruBody;
import com.hafidzquran.rumahquran.apirequests.HapusGuruBody;
import com.hafidzquran.rumahquran.apiresults.GetDataGuruResult;
import com.hafidzquran.rumahquran.apiresults.HapusGuruResult;
import com.hafidzquran.rumahquran.apiservices.GetDataGuruService;
import com.hafidzquran.rumahquran.apiservices.HapusGuruService;
import com.hafidzquran.rumahquran.ui.RuquButton;
import com.hafidzquran.rumahquran.ui.RuquTextview;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AdminActivity extends AppCompatActivity implements View.OnClickListener {
    private SessionManager sesi;
    private ProgressDialog progress;
    private Retrofit retrofit;
    private TableLayout tableGuru;
    private AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.BaseURL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        sesi = new SessionManager(this);
        builder = new AlertDialog.Builder(AdminActivity.this);
        RuquButton btnTambah = (RuquButton)findViewById(R.id.btn_add);
        RuquButton btnLogOut = (RuquButton)findViewById(R.id.btn_log_out);
        RuquButton btnRefresh = (RuquButton)findViewById(R.id.btn_refresh);
        tableGuru = (TableLayout)findViewById(R.id.table_daftar_guru);
        btnTambah.setOnClickListener(this);
        btnRefresh.setOnClickListener(this);
        btnLogOut.setOnClickListener(this);

        if (isOnline())
            ambilDaftarGuru();
        else {
            builder.setMessage("Cek koneksi internet anda");
            builder.setCancelable(false);
            builder.setPositiveButton("OK", null);
            builder.create().show();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_add:
                startActivity(new Intent(this, DaftarActivity.class));
                break;
            case R.id.btn_refresh:
                if (isOnline())
                    ambilDaftarGuru();
                else {
                    builder.setMessage("Cek koneksi internet anda");
                    builder.setCancelable(false);
                    builder.setPositiveButton("OK", null);
                    builder.create().show();
                }
                break;
            case R.id.btn_log_out:
                AlertDialog.Builder builder = new AlertDialog.Builder(AdminActivity.this);
                builder.setMessage("Apakah anda yakin ingin Logout?")
                        .setCancelable(true)
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                sesi.logoutUser();
                                finish();
                            }
                        })
                        .setNegativeButton("Tidak", null)
                        .setNeutralButton("Batal", null).create().show();
                break;
        }
    }

    private void ambilDaftarGuru(){
        tableGuru.removeAllViews();
        progress = new ProgressDialog(AdminActivity.this);
        progress.setMessage("Mengambil data guru..");
        progress.setCancelable(false);
        progress.show();
        GetDataGuruBody body = new GetDataGuruBody(sesi.getUserDetails().get("username"));
        GetDataGuruService service = retrofit.create(GetDataGuruService.class);
        Call<GetDataGuruResult> result = service.getRespond(body);

        result.enqueue(new Callback<GetDataGuruResult>() {
            @Override
            public void onResponse(Call<GetDataGuruResult> call, Response<GetDataGuruResult> response) {
                progress.dismiss();
                if (response.body()!=null) {
                    if (response.body().getCODE().equals("0")) {
                        drawTable(tableGuru, response.body());
                    } else {
                        Toast.makeText(AdminActivity.this, response.body().getDESC(), Toast.LENGTH_SHORT).show();
                    }
                } else
                    Toast.makeText(AdminActivity.this, getString(R.string.retrofit_body_null), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<GetDataGuruResult> call, Throwable t) {
                progress.dismiss();
                Toast.makeText(AdminActivity.this, getString(R.string.retrofit_failure), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void drawTable(TableLayout table, final GetDataGuruResult result){
        table.removeAllViews();
        TableRow header = new TableRow(AdminActivity.this);
        TableRow.LayoutParams hlp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
        header.setLayoutParams(hlp);
        RuquTextview headSantri = new RuquTextview(AdminActivity.this);
        headSantri.setText(R.string.nama_guru);
        headSantri.setLayoutParams(new TableRow.LayoutParams(250, ViewGroup.LayoutParams.WRAP_CONTENT));
        headSantri.setTextSize(15);
        headSantri.setTextColor(Color.BLACK);
        headSantri.setTypeface(headSantri.getTypeface(), Typeface.BOLD);
        headSantri.setPadding(25, 0, 25, 3);
        header.addView(headSantri);
        RuquTextview headEmail = new RuquTextview(AdminActivity.this);
        headEmail.setText(R.string.email);
        headEmail.setTypeface(headEmail.getTypeface(), Typeface.BOLD);
        headEmail.setTextSize(15);
        headEmail.setTextColor(Color.BLACK);
        header.addView(headEmail);
        RuquTextview headUsername = new RuquTextview(AdminActivity.this);
        headUsername.setText(R.string.username);
        headUsername.setTypeface(headEmail.getTypeface(), Typeface.BOLD);
        headUsername.setTextSize(15);
        headUsername.setTextColor(Color.BLACK);
        header.addView(headUsername);
        table.addView(header,0);
        table.setColumnShrinkable(1, true);

        if (result.getDATA().size() != 0){
            for (int i=0; i<result.getDATA().size(); i++){
                final TableRow row = new TableRow(AdminActivity.this);
                TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                row.setLayoutParams(lp);
                row.setPadding(0,10,0,10);
                if (i % 2 == 0) {
                    row.setBackgroundColor(Color.parseColor("#e5f7f7"));
                }
                final RuquTextview guru = new RuquTextview(AdminActivity.this);
                guru.setText(result.getDATA().get(i).getNAMA());
                guru.setLayoutParams(new TableRow.LayoutParams(250, ViewGroup.LayoutParams.WRAP_CONTENT));
                guru.setPadding(25, 0, 25, 3);
                row.addView(guru);
                final RuquTextview email = new RuquTextview(AdminActivity.this);
                email.setText(result.getDATA().get(i).getEMAIL());
                email.setPadding(0,0,25,0);
                row.addView(email);
                final RuquTextview usernameGuru = new RuquTextview(AdminActivity.this);
                usernameGuru.setText(result.getDATA().get(i).getUSERNAME());
                //idGuru.setVisibility(GONE);
                row.addView(usernameGuru);
                row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(AdminActivity.this);
                        builder.setMessage("Apa yang ingin anda lakukan dengan guru "+guru.getText().toString()+"?")
                                .setPositiveButton("Ubah", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Intent intent = new Intent(AdminActivity.this, DaftarActivity.class);
                                        intent.putExtra("nama", guru.getText().toString());
                                        intent.putExtra("email", email.getText().toString());
                                        intent.putExtra("username", usernameGuru.getText().toString());
                                        startActivityForResult(intent, 100);
                                    }
                                })
                                .setNegativeButton("Hapus", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if (isOnline())
                                            konfirmasiHapus(usernameGuru.getText().toString());
                                        else
                                            Toast.makeText(AdminActivity.this, getString(R.string.cek_koneksi), Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .setNeutralButton("Batal", null)
                                .setCancelable(false);
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                });
                table.addView(row, i+1);
            }
        }
    }

    private void konfirmasiHapus(final String unameGuru){
        AlertDialog.Builder builder = new AlertDialog.Builder(AdminActivity.this);
        builder.setMessage("Apa anda yakin ingin menghapus?")
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        hapusGuru(unameGuru);
                    }
                })
                .setNeutralButton("Batal", null)
                .setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void hapusGuru(String usernameGuru) {
        progress = new ProgressDialog(AdminActivity.this);
        progress.setMessage("Menghapus santri..");
        progress.setCancelable(false);
        progress.show();
        HapusGuruBody body = new HapusGuruBody(sesi.getUserDetails().get("username"), usernameGuru);
        HapusGuruService service = retrofit.create(HapusGuruService.class);
        Call<HapusGuruResult> result = service.getRespond(body);
        result.enqueue(new Callback<HapusGuruResult>() {
            @Override
            public void onResponse(Call<HapusGuruResult> call, Response<HapusGuruResult> response) {
                progress.dismiss();
                if (response.body()!=null) {
                    Toast.makeText(AdminActivity.this, response.body().getDESC(), Toast.LENGTH_SHORT).show();
                    if (response.body().getCODE().equals("0") && isOnline()){
                        ambilDaftarGuru();
                    }
                } else
                    Toast.makeText(AdminActivity.this, getString(R.string.retrofit_body_null), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<HapusGuruResult> call, Throwable t) {
                progress.dismiss();
                Toast.makeText(AdminActivity.this, getString(R.string.retrofit_failure), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = null;
        if (cm != null) {
            netInfo = cm.getActiveNetworkInfo();
        }
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
