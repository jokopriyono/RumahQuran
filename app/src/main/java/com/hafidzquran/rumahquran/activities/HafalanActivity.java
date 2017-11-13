package com.hafidzquran.rumahquran.activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import com.hafidzquran.rumahquran.R;
import com.hafidzquran.rumahquran.SessionManager;
import com.hafidzquran.rumahquran.apirequests.GetHafalanBody;
import com.hafidzquran.rumahquran.apirequests.HapusHafalanBody;
import com.hafidzquran.rumahquran.apiresults.GetHafalanResult;
import com.hafidzquran.rumahquran.apiresults.HapusHafalanResult;
import com.hafidzquran.rumahquran.apiservices.GetHafalanService;
import com.hafidzquran.rumahquran.apiservices.HapusHafalanService;
import com.hafidzquran.rumahquran.ui.RuquButton;
import com.hafidzquran.rumahquran.ui.RuquEditText;
import com.hafidzquran.rumahquran.ui.RuquTextview;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HafalanActivity extends AppCompatActivity implements View.OnClickListener {
    private RuquEditText edtTgl;
    private Calendar myCalendar = Calendar.getInstance();
    private TableLayout tableHafalan;
    private SessionManager sesi;
    private ProgressDialog progress;
    private Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hafalan);

        retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.BaseURL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        sesi = new SessionManager(HafalanActivity.this);
        tableHafalan = (TableLayout)findViewById(R.id.table_hafalan) ;
        RuquButton btnTambah = (RuquButton) findViewById(R.id.btn_tambah);
        btnTambah.setOnClickListener(this);
        edtTgl = (RuquEditText) findViewById(R.id.edt_tanggal_hafalan);
        edtTgl.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_tambah:
                startActivityForResult(new Intent(HafalanActivity.this, TambahUbahHafalanActivity.class), 100);
                break;
            case R.id.edt_tanggal_hafalan:
                new DatePickerDialog(HafalanActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
        }
    }

    private void cekHafalan() {
        tableHafalan.removeAllViews();
        String formatLayar = "dd MMM yyyy";
        String formatDb = "yyyy-MM-dd";
        final SimpleDateFormat sdf = new SimpleDateFormat(formatLayar, Locale.getDefault());
        SimpleDateFormat sdfDB = new SimpleDateFormat(formatDb, Locale.getDefault());

        progress = new ProgressDialog(HafalanActivity.this);
        progress.setMessage("Mengecek data..");
        progress.setCancelable(false);
        progress.show();
        GetHafalanBody body = new GetHafalanBody(sesi.getUserDetails().get("username"), sdfDB.format(myCalendar.getTime()));
        GetHafalanService service = retrofit.create(GetHafalanService.class);
        Call<GetHafalanResult> result = service.getRespond(body);
        result.enqueue(new Callback<GetHafalanResult>() {
            @Override
            public void onResponse(Call<GetHafalanResult> call, Response<GetHafalanResult> response) {
                progress.dismiss();
                edtTgl.setText(sdf.format(myCalendar.getTime()));
                if (response.body()!=null){
                    if (response.body().getCODE().equals("0"))
                        drawTable(tableHafalan, response.body());
                    else
                        Toast.makeText(HafalanActivity.this, response.body().getDESC(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(HafalanActivity.this, getString(R.string.retrofit_body_null), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetHafalanResult> call, Throwable t) {
                progress.dismiss();
                Toast.makeText(HafalanActivity.this, getString(R.string.retrofit_failure), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void drawTable(TableLayout table, final GetHafalanResult result){
        table.removeAllViews();
        TableRow header = new TableRow(HafalanActivity.this);
        TableRow.LayoutParams hlp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
        header.setLayoutParams(hlp);
        RuquTextview headSantri = new RuquTextview(HafalanActivity.this);
        headSantri.setText(R.string.tgl);
        headSantri.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, 0.25f));
        headSantri.setGravity(Gravity.CENTER_HORIZONTAL);
        //headSantri.setLayoutParams(new TableRow.LayoutParams(250, ViewGroup.LayoutParams.WRAP_CONTENT));
        headSantri.setTextSize(15);
        headSantri.setTextColor(Color.BLACK);
        headSantri.setTypeface(headSantri.getTypeface(), Typeface.BOLD);
        headSantri.setPadding(25, 0, 25, 3);
        header.addView(headSantri);
        RuquTextview headTahun = new RuquTextview(HafalanActivity.this);
        headTahun.setText(R.string.detil);
        headTahun.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        headTahun.setTypeface(headTahun.getTypeface(), Typeface.BOLD);
        headTahun.setTextSize(15);
        headTahun.setTextColor(Color.BLACK);
        header.addView(headTahun);
        table.addView(header,0);
        table.setColumnShrinkable(1, true);

        if (result.getDATA().size() != 0){
            for (int i=0; i<result.getDATA().size(); i++){
                final TableRow row = new TableRow(HafalanActivity.this);
                TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                row.setLayoutParams(lp);
                if (i % 2 == 0) {
                    row.setBackgroundColor(Color.parseColor("#e5f7f7"));
                }
                final RuquTextview tanggal = new RuquTextview(HafalanActivity.this);
                tanggal.setText(result.getDATA().get(i).getTANGGAL());
                tanggal.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT, 0.25f));
                tanggal.setGravity(Gravity.CENTER);
                row.addView(tanggal);

                LinearLayout linear = new LinearLayout(HafalanActivity.this);
                linear.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT, 1));
                linear.setOrientation(LinearLayout.VERTICAL);

                final RuquTextview juz = new RuquTextview(HafalanActivity.this);
                String showContent ;
                showContent = "Juz : "+ result.getDATA().get(i).getJUZ();
                juz.setText(showContent);
                linear.addView(juz);
                final RuquTextview halaman = new RuquTextview(HafalanActivity.this);
                showContent = "Halaman : " + result.getDATA().get(i).getHALAMAN();
                halaman.setText(showContent);
                linear.addView(halaman);
                final RuquTextview kelancaran = new RuquTextview(HafalanActivity.this);
                showContent = "Kelancaran : " + result.getDATA().get(i).getKELANCARAN();
                kelancaran.setText(showContent);
                linear.addView(kelancaran);
                final RuquTextview murojaah = new RuquTextview(HafalanActivity.this);
                if (result.getDATA().get(i).getMUROJAAH().equals("0")) {
                    showContent = "Murojaah : Tidak Selesai";
                    murojaah.setText(showContent);
                } else {
                    showContent = "Murojaah : Tidak Selesai";
                    murojaah.setText(showContent);
                }
                linear.addView(murojaah);
                final RuquTextview santri = new RuquTextview(HafalanActivity.this);
                showContent = "Santri : " + result.getDATA().get(i).getSANTRI();
                santri.setText(showContent);
                linear.addView(santri);

                row.addView(linear);
                final int tempVariableI = i;
                row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(HafalanActivity.this);
                        builder.setMessage("Apa yang ingin anda lakukan?")
                                .setPositiveButton("Ubah", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int ii) {
                                        Intent intent = new Intent(HafalanActivity.this, TambahUbahHafalanActivity.class);
                                        intent.putExtra("id", result.getDATA().get(tempVariableI).getID());
                                        intent.putExtra("halaman", result.getDATA().get(tempVariableI).getHALAMAN());
                                        intent.putExtra("idsantri", result.getDATA().get(tempVariableI).getIDSANTRI());
                                        intent.putExtra("juz", result.getDATA().get(tempVariableI).getJUZ());
                                        intent.putExtra("kelancaran", result.getDATA().get(tempVariableI).getKELANCARAN());
                                        intent.putExtra("keterangan", result.getDATA().get(tempVariableI).getKETERANGAN());
                                        intent.putExtra("murojaah", result.getDATA().get(tempVariableI).getMUROJAAH());
                                        intent.putExtra("santri", result.getDATA().get(tempVariableI).getSANTRI());
                                        intent.putExtra("tanggal", result.getDATA().get(tempVariableI).getTANGGAL());
                                        startActivityForResult(intent, 200);
                                    }
                                })
                                .setNegativeButton("Hapus", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int ii) {
                                        if (isOnline()) {
                                            konfirmasiHapus(result.getDATA().get(tempVariableI).getID());
                                        } else {
                                            Toast.makeText(HafalanActivity.this, "Cek koneksi internet, lalu coba lagi", Toast.LENGTH_SHORT).show();
                                        }
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
        AlertDialog.Builder builder = new AlertDialog.Builder(HafalanActivity.this);
        builder.setMessage("Apa anda yakin ingin menghapus?")
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (isOnline()) {
                            hapusHafalan(unameGuru);
                        } else {
                            Toast.makeText(HafalanActivity.this, "Cek koneksi internet, lalu coba lagi", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNeutralButton("Batal", null)
                .setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void hapusHafalan(@NonNull String idHafalan){
        progress = new ProgressDialog(HafalanActivity.this);
        progress.setMessage("Menghapus hafalan..");
        progress.setCancelable(false);
        progress.show();
        HapusHafalanBody body = new HapusHafalanBody(sesi.getUserDetails().get("username"), idHafalan);
        HapusHafalanService service = retrofit.create(HapusHafalanService.class);
        Call<HapusHafalanResult> result = service.getRespond(body);
        result.enqueue(new Callback<HapusHafalanResult>() {
            @Override
            public void onResponse(Call<HapusHafalanResult> call, Response<HapusHafalanResult> response) {
                progress.dismiss();
                if (response.body()!=null) {
                    if (response.body().getCODE().equals("0"))
                        if (isOnline()) {
                            cekHafalan();
                        } else {
                            Toast.makeText(HafalanActivity.this, "Cek koneksi internet, lalu coba lagi", Toast.LENGTH_SHORT).show();
                        }
                    else
                        Toast.makeText(HafalanActivity.this, response.body().getDESC(), Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(HafalanActivity.this, "Respon server bermasalah", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<HapusHafalanResult> call, Throwable t) {
                progress.dismiss();
                Toast.makeText(HafalanActivity.this, "Server tidak merespon", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            if (isOnline()) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                cekHafalan();
            } else {
                Toast.makeText(HafalanActivity.this, "Cek koneksi internet, lalu coba lagi", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 100: // kode untuk tambah hafalan
            case 200: // kode untuk ubah hafalan
                if (resultCode==RESULT_OK){
                    if (isOnline()) {
                        if (!edtTgl.getText().toString().equals(""))
                            cekHafalan();
                        else
                            tableHafalan.removeAllViews();
                    } else {
                        Toast.makeText(this, "Cek koneksi internet, lalu coba lagi", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
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
