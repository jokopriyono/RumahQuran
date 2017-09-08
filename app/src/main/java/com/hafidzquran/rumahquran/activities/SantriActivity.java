package com.hafidzquran.rumahquran.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.hafidzquran.rumahquran.R;
import com.hafidzquran.rumahquran.SessionManager;
import com.hafidzquran.rumahquran.apirequests.GetDataSantriBody;
import com.hafidzquran.rumahquran.apirequests.HapusSantriBody;
import com.hafidzquran.rumahquran.apiresults.GetDataSantriResult;
import com.hafidzquran.rumahquran.apiresults.HapusSantriResult;
import com.hafidzquran.rumahquran.apiservices.GetDataSantriService;
import com.hafidzquran.rumahquran.apiservices.HapusSantriService;
import com.hafidzquran.rumahquran.ui.RuquButton;
import com.hafidzquran.rumahquran.ui.RuquTextview;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.view.View.GONE;

public class SantriActivity extends AppCompatActivity implements View.OnClickListener {
    private SessionManager sesi;
    private TableLayout tableSantri;
    private ProgressDialog progress;
    private Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_santri);

        retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.BaseURL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        sesi = new SessionManager(this);
        //recyclerSantri = (RecyclerView)findViewById(R.id.recycler_santri);
        tableSantri = (TableLayout)findViewById(R.id.table_daftar_santri);
        RuquButton btnTambah = (RuquButton)findViewById(R.id.btn_add);
        RuquButton btnRefresh = (RuquButton)findViewById(R.id.btn_refresh);
        btnTambah.setOnClickListener(this);
        btnRefresh.setOnClickListener(this);

        if (isOnline())
            ambilDaftarSantri();
        else
            Toast.makeText(this, getString(R.string.cek_koneksi), Toast.LENGTH_SHORT).show();
    }

    private void ambilDaftarSantri(){
        progress = new ProgressDialog(SantriActivity.this);
        progress.setMessage("Mengambil data santri..");
        progress.setCancelable(false);
        progress.show();
        GetDataSantriBody body = new GetDataSantriBody(sesi.getUserDetails().get("username"));
        GetDataSantriService service = retrofit.create(GetDataSantriService.class);
        Call<GetDataSantriResult> result = service.getRespond(body);

        result.enqueue(new Callback<GetDataSantriResult>() {
            @Override
            public void onResponse(Call<GetDataSantriResult> call, Response<GetDataSantriResult> response) {
                progress.dismiss();
                if (response.body()!=null) {
                    if (response.body().getCODE().equals("0")) {
                        drawTable(tableSantri, response.body());
                    } else {
                        Toast.makeText(SantriActivity.this, response.body().getDESC(), Toast.LENGTH_SHORT).show();
                    }
                } else
                    Toast.makeText(SantriActivity.this, getString(R.string.retrofit_body_null), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<GetDataSantriResult> call, Throwable t) {
                progress.dismiss();
                Toast.makeText(SantriActivity.this, getString(R.string.retrofit_failure), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void drawTable(TableLayout table, final GetDataSantriResult result){
        table.removeAllViews();
        TableRow header = new TableRow(SantriActivity.this);
        TableRow.LayoutParams hlp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
        header.setLayoutParams(hlp);
        RuquTextview headSantri = new RuquTextview(SantriActivity.this);
        headSantri.setText("Nama Santri");
        //headSantri.setLayoutParams(new TableRow.LayoutParams(250, ViewGroup.LayoutParams.WRAP_CONTENT));
        headSantri.setTextSize(15);
        headSantri.setTextColor(Color.BLACK);
        headSantri.setTypeface(headSantri.getTypeface(), Typeface.BOLD);
        headSantri.setPadding(25, 0, 25, 3);
        header.addView(headSantri);
        RuquTextview headTahun = new RuquTextview(SantriActivity.this);
        headTahun.setText("Tahun Ajar");
        headTahun.setTypeface(headTahun.getTypeface(), Typeface.BOLD);
        headTahun.setTextSize(15);
        headTahun.setTextColor(Color.BLACK);
        header.addView(headTahun);
        table.addView(header,0);
        table.setColumnShrinkable(1, true);

        if (result.getDATA().size() != 0){
            for (int i=0; i<result.getDATA().size(); i++){
                final TableRow row = new TableRow(SantriActivity.this);
                TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                row.setLayoutParams(lp);
                row.setPadding(0,20,0,20);
                if (i % 2 == 0) {
                    row.setBackgroundColor(Color.parseColor("#e5f7f7"));
                }
                final RuquTextview idSantri = new RuquTextview(SantriActivity.this);
                idSantri.setText(result.getDATA().get(i).getID());
                idSantri.setVisibility(GONE);
                row.addView(idSantri);
                final RuquTextview santri = new RuquTextview(SantriActivity.this);
                santri.setText(result.getDATA().get(i).getNAMA());
                //santri.setLayoutParams(new TableRow.LayoutParams(250, ViewGroup.LayoutParams.WRAP_CONTENT));
                santri.setPadding(25, 0, 25, 3);
                row.addView(santri);
                final RuquTextview tahun = new RuquTextview(SantriActivity.this);
                tahun.setText(result.getDATA().get(i).getTAHUN());
                row.addView(tahun);
                row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(SantriActivity.this);
                        builder.setMessage("Apa yang ingin anda lakukan?")
                                .setPositiveButton("Ubah", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Intent intent = new Intent(SantriActivity.this,
                                                TambahUbahSantriActivity.class);
                                        intent.putExtra("nama", santri.getText().toString());
                                        intent.putExtra("tahun", tahun.getText().toString());
                                        intent.putExtra("id", idSantri.getText().toString());
                                        startActivityForResult(intent, 100);
                                    }
                                })
                                .setNegativeButton("Hapus", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if (isOnline())
                                            konfirmasiHapus(idSantri.getText().toString());
                                        else
                                            Toast.makeText(SantriActivity.this, getString(R.string.cek_koneksi), Toast.LENGTH_SHORT).show();
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
        AlertDialog.Builder builder = new AlertDialog.Builder(SantriActivity.this);
        builder.setMessage("Apa anda yakin ingin menghapus?")
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (isOnline()) {
                            hapusSantri(unameGuru);
                        } else {
                            Toast.makeText(SantriActivity.this, "Cek koneksi internet, lalu coba lagi", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNeutralButton("Batal", null)
                .setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void hapusSantri(@NonNull String idSantri){
        progress = new ProgressDialog(SantriActivity.this);
        progress.setMessage("Menghapus santri..");
        progress.setCancelable(false);
        progress.show();
        HapusSantriBody body = new HapusSantriBody(sesi.getUserDetails().get("username"), idSantri);
        HapusSantriService service = retrofit.create(HapusSantriService.class);
        Call<HapusSantriResult> result = service.getRespond(body);
        result.enqueue(new Callback<HapusSantriResult>() {
            @Override
            public void onResponse(Call<HapusSantriResult> call, Response<HapusSantriResult> response) {
                progress.dismiss();
                if (response.body()!=null){
                    if (response.body().getCODE().equals("0")) {
                        if (isOnline())
                            ambilDaftarSantri();
                        else
                            Toast.makeText(SantriActivity.this, getString(R.string.cek_koneksi), Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(SantriActivity.this, response.body().getDESC(), Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(SantriActivity.this, getString(R.string.retrofit_body_null), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<HapusSantriResult> call, Throwable t) {
                progress.dismiss();
                Toast.makeText(SantriActivity.this, getString(R.string.retrofit_failure), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_add:
                startActivityForResult(new Intent(SantriActivity.this, TambahUbahSantriActivity.class), 200);
                break;
            case R.id.btn_refresh:
                if (isOnline())
                    ambilDaftarSantri();
                else
                    Toast.makeText(SantriActivity.this, getString(R.string.cek_koneksi), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 100: //Jika user mengubah data dari santri
                if (resultCode == RESULT_OK) {
                    if (isOnline())
                        ambilDaftarSantri();
                    else
                        Toast.makeText(SantriActivity.this, getString(R.string.cek_koneksi), Toast.LENGTH_SHORT).show();
                }
                break;
            case 200: //Jika user menambahkan data santri baru
                if (resultCode == RESULT_OK) {
                    if (isOnline())
                        ambilDaftarSantri();
                    else
                        Toast.makeText(SantriActivity.this, getString(R.string.cek_koneksi), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
