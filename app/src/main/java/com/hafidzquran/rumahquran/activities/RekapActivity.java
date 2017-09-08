package com.hafidzquran.rumahquran.activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.hafidzquran.rumahquran.R;
import com.hafidzquran.rumahquran.SessionManager;
import com.hafidzquran.rumahquran.apirequests.GetDataSantriBody;
import com.hafidzquran.rumahquran.apirequests.GetRekapBody;
import com.hafidzquran.rumahquran.apiresults.GetDataSantriResult;
import com.hafidzquran.rumahquran.apiresults.GetRekapResult;
import com.hafidzquran.rumahquran.apiservices.GetDataSantriService;
import com.hafidzquran.rumahquran.apiservices.GetRekapService;
import com.hafidzquran.rumahquran.ui.RuquButton;
import com.hafidzquran.rumahquran.ui.RuquEditText;
import com.hafidzquran.rumahquran.ui.RuquTextview;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RekapActivity extends AppCompatActivity implements View.OnClickListener {
    private RuquEditText edtDari, edtSampai;
    private Calendar myCalendar = Calendar.getInstance();
    private ProgressDialog progress;
    private SessionManager sesi;
    private Retrofit retrofit;
    private Spinner spinSantri;
    private TableLayout tableRekap;
    private RuquTextview txtLancar, txtUlang;
    private int totalLancar = 0;
    private int totalUlang = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rekap);

        retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.BaseURL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        sesi = new SessionManager(this);
        tableRekap = (TableLayout)findViewById(R.id.table_rekap);
        RuquButton btnCari = (RuquButton)findViewById(R.id.btn_cari);
        btnCari.setOnClickListener(this);
        RuquButton btnBersihkan = (RuquButton)findViewById(R.id.btn_bersihkan);
        btnBersihkan.setOnClickListener(this);
        spinSantri = (Spinner)findViewById(R.id.spinner_daftar_santri);
        edtDari = (RuquEditText) findViewById(R.id.edt_dari_tanggal);
        edtSampai = (RuquEditText)findViewById(R.id.edt_sampai_tanggal);
        txtLancar = (RuquTextview) findViewById(R.id.txt_total_lancar);
        txtUlang = (RuquTextview) findViewById(R.id.txt_total_ulang);
        edtDari.setOnClickListener(this);
        edtSampai.setOnClickListener(this);
        if (isOnline())
            ambilDaftarSantri();
        else {
            Toast.makeText(this, getString(R.string.cek_koneksi), Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void ambilDaftarSantri(){
        progress = new ProgressDialog(RekapActivity.this);
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
                        List<GetDataSantriResult.isiData> daftarSantri = response.body().getDATA();
                        List<String> list = new ArrayList<>();
                        list.add("Semua Santri");
                        for (int i=0; i<daftarSantri.size(); i++){
                            list.add(daftarSantri.get(i).getID() + "-" + daftarSantri.get(i).getNAMA());
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(RekapActivity.this,
                                R.layout.support_simple_spinner_dropdown_item, list);
                        spinSantri.setAdapter(adapter);
                    } else {
                        Toast.makeText(RekapActivity.this, response.body().getDESC(), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } else {
                    Toast.makeText(RekapActivity.this, "Terdapat masalah respon balik dari server", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<GetDataSantriResult> call, Throwable t) {
                progress.dismiss();
                Toast.makeText(RekapActivity.this, "Cek koneksi anda dan coba lagi", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private DatePickerDialog.OnDateSetListener dateDari = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String formatLayar = "dd MMM yyyy";
            final SimpleDateFormat sdf = new SimpleDateFormat(formatLayar, Locale.getDefault());
            edtDari.setText(sdf.format(myCalendar.getTime()));
        }
    };

    private DatePickerDialog.OnDateSetListener dateSampai = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String formatLayar = "dd MMM yyyy";
            final SimpleDateFormat sdf = new SimpleDateFormat(formatLayar, Locale.getDefault());
            edtSampai.setText(sdf.format(myCalendar.getTime()));
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_cari:
                if (isOnline()){
                    ambilDataRekap();
                } else {
                    Toast.makeText(this, getString(R.string.cek_koneksi), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.edt_dari_tanggal:
                new DatePickerDialog(RekapActivity.this, dateDari, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
            case R.id.edt_sampai_tanggal:
                new DatePickerDialog(RekapActivity.this, dateSampai, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
            case R.id.btn_bersihkan:
                edtDari.setText("");
                edtSampai.setText("");
                tableRekap.removeAllViews();
                break;
        }
    }

    private void ambilDataRekap() {
        DateFormat formatDb = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        Date tanggalDari, tanggalSampai;
        String isiTanggalDari = "0", isiTanggalSampai = "0";
        String[] idnya = {"0"};
        if (!spinSantri.getSelectedItem().toString().equals("Semua Santri")) {
             idnya = spinSantri.getSelectedItem().toString().split(Pattern.quote("-"));
        }
        if (!edtDari.getText().toString().equals("")){
            try {
                tanggalDari = formatDb.parse(edtDari.getText().toString());
                isiTanggalDari = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(tanggalDari);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (!edtSampai.getText().toString().equals("")){
            try {
                tanggalSampai = formatDb.parse(edtSampai.getText().toString());
                isiTanggalSampai = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(tanggalSampai);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        progress = new ProgressDialog(RekapActivity.this);
        progress.setMessage("Mengambil data rekap..");
        progress.setCancelable(false);
        progress.show();
        GetRekapBody body = new GetRekapBody(isiTanggalDari, isiTanggalSampai, idnya[0],
                sesi.getUserDetails().get("username"));
        GetRekapService service = retrofit.create(GetRekapService.class);
        Call<GetRekapResult> result = service.getRespond(body);
        result.enqueue(new Callback<GetRekapResult>() {
            @Override
            public void onResponse(Call<GetRekapResult> call, Response<GetRekapResult> response) {
                progress.dismiss();
                if (response.body()!=null){
                    if (response.body().getCODE().equals("0")) {
                        for (int i = 0; i < response.body().getDATA().size(); i++) {
                            if (response.body().getDATA().get(i).getKELANCARAN().equals("Lancar")) {
                                totalLancar = totalLancar + 1;
                            } else {
                                totalUlang = totalUlang + 1;
                            }
                        }
                        txtLancar.setText("Lancar : "+totalLancar);
                        txtUlang.setText("Ulang : "+totalUlang);
                        totalLancar = 0;
                        totalUlang = 0;
                        drawTable(tableRekap, response.body());
                    } else {
                        Toast.makeText(RekapActivity.this, response.body().getDESC(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(RekapActivity.this, getString(R.string.retrofit_body_null), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetRekapResult> call, Throwable t) {

            }
        });
    }

    private void drawTable(TableLayout table, final GetRekapResult result){
        table.removeAllViews();
        TableRow header = new TableRow(RekapActivity.this);
        TableRow.LayoutParams hlp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
        header.setLayoutParams(hlp);
        RuquTextview headTanggal = new RuquTextview(RekapActivity.this);
        headTanggal.setText("Tanggal");
        headTanggal.setGravity(Gravity.CENTER_HORIZONTAL);
        //headSantri.setLayoutParams(new TableRow.LayoutParams(250, ViewGroup.LayoutParams.WRAP_CONTENT));
        headTanggal.setTextSize(15);
        headTanggal.setTextColor(Color.BLACK);
        headTanggal.setTypeface(headTanggal.getTypeface(), Typeface.BOLD);
        headTanggal.setPadding(25, 0, 25, 3);
        header.addView(headTanggal);
        RuquTextview headDetail = new RuquTextview(RekapActivity.this);
        headDetail.setText("Detail");
        headDetail.setTypeface(headDetail.getTypeface(), Typeface.BOLD);
        headDetail.setTextSize(15);
        headDetail.setTextColor(Color.BLACK);
        header.addView(headDetail);
        table.addView(header,0);
        table.setColumnShrinkable(1, true);

        if (result.getDATA().size() != 0){
            for (int i=0; i<result.getDATA().size(); i++){
                final TableRow row = new TableRow(RekapActivity.this);
                TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                row.setLayoutParams(lp);
                if (i % 2 == 0) {
                    row.setBackgroundColor(Color.parseColor("#e5f7f7"));
                }
                final RuquTextview tanggal = new RuquTextview(RekapActivity.this);
                tanggal.setText(result.getDATA().get(i).getTANGGAL());
                tanggal.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
                tanggal.setGravity(Gravity.CENTER);
                tanggal.setPadding(25, 0, 25, 3);
                row.addView(tanggal);

                LinearLayout linear = new LinearLayout(RekapActivity.this);
                //linear.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                //        ViewGroup.LayoutParams.WRAP_CONTENT, 1));
                linear.setOrientation(LinearLayout.VERTICAL);

                final RuquTextview juz = new RuquTextview(RekapActivity.this);
                juz.setText("Juz : "+ result.getDATA().get(i).getJUZ());
                juz.setTextColor(Color.BLACK);
                linear.addView(juz);
                final RuquTextview halaman = new RuquTextview(RekapActivity.this);
                halaman.setText("Halaman : " + result.getDATA().get(i).getHALAMAN());
                halaman.setTextColor(Color.BLACK);
                linear.addView(halaman);
                final RuquTextview kelancaran = new RuquTextview(RekapActivity.this);
                kelancaran.setText("Kelancaran : " + result.getDATA().get(i).getKELANCARAN());
                kelancaran.setTextColor(Color.BLACK);
                linear.addView(kelancaran);
                final RuquTextview murojaah = new RuquTextview(RekapActivity.this);
                if (result.getDATA().get(i).getMUROJAAH().equals("0"))
                    murojaah.setText("Murojaah : Tidak Selesai");
                else
                    murojaah.setText("Murojaah : Selesai");
                murojaah.setTextColor(Color.BLACK);
                linear.addView(murojaah);
                final RuquTextview santri = new RuquTextview(RekapActivity.this);
                santri.setText("Santri : " + result.getDATA().get(i).getSANTRI());
                santri.setTextColor(Color.BLACK);
                linear.addView(santri);

                row.addView(linear);
                /*final int tempVariableI = i;
                row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(RekapActivity.this);
                        builder.setMessage("Apa yang ingin anda lakukan?")
                                .setPositiveButton("Ubah", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int ii) {
                                        Intent intent = new Intent(RekapActivity.this, TambahUbahHafalanActivity.class);
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
                                        hapusHafalan(result.getDATA().get(tempVariableI).getID());
                                    }
                                })
                                .setNeutralButton("Batal", null)
                                .setCancelable(false);
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                });*/
                table.addView(row, i+1);
            }
        }
    }
}
