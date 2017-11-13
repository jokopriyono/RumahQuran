package com.hafidzquran.rumahquran.activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.hafidzquran.rumahquran.R;
import com.hafidzquran.rumahquran.SessionManager;
import com.hafidzquran.rumahquran.apirequests.GetDataSantriBody;
import com.hafidzquran.rumahquran.apirequests.TambahHafalanBody;
import com.hafidzquran.rumahquran.apirequests.UbahHafalanBody;
import com.hafidzquran.rumahquran.apiresults.GetDataSantriResult;
import com.hafidzquran.rumahquran.apiresults.TambahHafalanResult;
import com.hafidzquran.rumahquran.apiresults.UbahHafalanResult;
import com.hafidzquran.rumahquran.apiservices.GetDataSantriService;
import com.hafidzquran.rumahquran.apiservices.TambahHafalanService;
import com.hafidzquran.rumahquran.apiservices.UbahHafalanService;
import com.hafidzquran.rumahquran.ui.RuquButton;
import com.hafidzquran.rumahquran.ui.RuquEditText;

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

public class TambahUbahHafalanActivity extends AppCompatActivity implements View.OnClickListener {
    private RuquEditText edtTgl, edtKeterangan;
    private SessionManager sesi;
    private Retrofit retrofit;
    private Spinner spinSantri, spinJuz, spinHalaman;
    private String idHafalan = null;
    private RadioGroup radioGroupKelancaran;
    private RadioButton radioLancar, radioUlang;
    private CheckBox checkMurojaah;
    private Calendar myCalendar = Calendar.getInstance();
    private ProgressDialog progress;
    private String isiJuz[] = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
    "11", "12", "13", "14", "15", "16", "17", "18", "19", "20",
    "21", "22", "23", "24", "25", "26", "27", "28", "29", "20"};
    private String isiHalaman[] = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_dan_ubah);

        retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.BaseURL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        sesi = new SessionManager(this);

        checkMurojaah = (CheckBox)findViewById(R.id.checkbox_murojaah);
        radioGroupKelancaran = (RadioGroup)findViewById(R.id.radio_group_hafalan);
        radioLancar = (RadioButton)findViewById(R.id.radio_lancar);
        radioUlang = (RadioButton)findViewById(R.id.radio_ulang);
        spinSantri = (Spinner)findViewById(R.id.spinner_daftar_santri);
        edtTgl = (RuquEditText) findViewById(R.id.edt_tanggal_hafalan);
        edtTgl.setOnClickListener(this);
        ArrayAdapter<String> adapterJuz = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, isiJuz);
        ArrayAdapter<String> adapterHalaman = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, isiHalaman);
        adapterJuz.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterHalaman.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinJuz = (Spinner)findViewById(R.id.spin_juz);
        spinHalaman = (Spinner)findViewById(R.id.spin_halaman);
        spinJuz.setAdapter(adapterJuz);
        spinHalaman.setAdapter(adapterHalaman);
        edtKeterangan = (RuquEditText) findViewById(R.id.edt_keterangan);
        RuquButton btnSimpan = (RuquButton) findViewById(R.id.btn_simpan);
        btnSimpan.setOnClickListener(this);

        if (isOnline())
            ambilDaftarSantri();
        else
            Toast.makeText(TambahUbahHafalanActivity.this, getString(R.string.cek_koneksi), Toast.LENGTH_SHORT).show();
    }

    private void ubahHafalan(@NonNull String idSantri, @NonNull String idHafalan) {
        DateFormat formatDb = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        Date tanggal = null;
        String murojaah, kelancaran;
        progress = new ProgressDialog(TambahUbahHafalanActivity.this);
        progress.setMessage("Mengubah data hafalan..");
        progress.setCancelable(false);
        progress.show();
        //==========================================================================
        try {
            tanggal = formatDb.parse(edtTgl.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (checkMurojaah.isChecked())
            murojaah = "1";
        else
            murojaah = "0";
        if (radioLancar.isChecked())
            kelancaran = "Lancar";
        else
            kelancaran = "Ulang";
        //==========================================================================
        UbahHafalanBody body = new UbahHafalanBody(idHafalan,
                sesi.getUserDetails().get("username"),
                new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(tanggal),
                idSantri,
                spinJuz.getSelectedItem().toString(),
                spinHalaman.getSelectedItem().toString(),
                kelancaran,
                murojaah,
                edtKeterangan.getText().toString());
        UbahHafalanService service = retrofit.create(UbahHafalanService.class);
        Call<UbahHafalanResult> result = service.getRespond(body);
        result.enqueue(new Callback<UbahHafalanResult>() {
            @Override
            public void onResponse(Call<UbahHafalanResult> call, Response<UbahHafalanResult> response) {
                progress.dismiss();
                if (response.body() != null){
                    Toast.makeText(TambahUbahHafalanActivity.this, response.body().getDESC(), Toast.LENGTH_SHORT).show();
                    if (response.body().getCODE().equals("0")){
                        setResult(RESULT_OK);
                        finish();
                    }
                } else
                    Toast.makeText(TambahUbahHafalanActivity.this, getString(R.string.retrofit_body_null), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<UbahHafalanResult> call, Throwable t) {
                progress.dismiss();
                Toast.makeText(TambahUbahHafalanActivity.this, getString(R.string.retrofit_failure), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void tambahHafalan(@NonNull String idSantri){
        DateFormat formatDb = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        Date tanggal=null;
        String murojaah, kelancaran;
        progress = new ProgressDialog(TambahUbahHafalanActivity.this);
        progress.setMessage("Menyimpan data hafalan..");
        progress.setCancelable(false);
        progress.show();
        //==========================================================================
        try {
            tanggal = formatDb.parse(edtTgl.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (checkMurojaah.isChecked())
            murojaah = "1";
        else
            murojaah = "0";
        if (radioLancar.isChecked())
            kelancaran = "Lancar";
        else
            kelancaran = "Ulang";
        //==========================================================================
        TambahHafalanBody body = new TambahHafalanBody(
                sesi.getUserDetails().get("username"),
                new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(tanggal),
                idSantri,
                spinJuz.getSelectedItem().toString(),
                spinHalaman.getSelectedItem().toString(),
                kelancaran,
                murojaah,
                edtKeterangan.getText().toString());
        TambahHafalanService service = retrofit.create(TambahHafalanService.class);
        Call<TambahHafalanResult> result = service.getRespond(body);
        result.enqueue(new Callback<TambahHafalanResult>() {
            @Override
            public void onResponse(Call<TambahHafalanResult> call, Response<TambahHafalanResult> response) {
                progress.dismiss();
                if (response.body() != null){
                    Toast.makeText(TambahUbahHafalanActivity.this, response.body().getDESC(), Toast.LENGTH_SHORT).show();
                    if (response.body().getCODE().equals("0")){
                        setResult(RESULT_OK);
                        finish();
                    }
                } else
                    Toast.makeText(TambahUbahHafalanActivity.this, getString(R.string.retrofit_body_null), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<TambahHafalanResult> call, Throwable t) {
                progress.dismiss();
                Toast.makeText(TambahUbahHafalanActivity.this, getString(R.string.retrofit_failure), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ambilDaftarSantri(){
        progress = new ProgressDialog(TambahUbahHafalanActivity.this);
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
                        for (int i=0; i<daftarSantri.size(); i++){
                            list.add(daftarSantri.get(i).getID() + "-" + daftarSantri.get(i).getNAMA());
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(TambahUbahHafalanActivity.this,
                                R.layout.support_simple_spinner_dropdown_item, list);
                        spinSantri.setAdapter(adapter);

                        Intent i = getIntent();
                        if (i.getStringExtra("id")!=null){
                            String juz = getIntent().getStringExtra("juz");
                            idHafalan = i.getStringExtra("id");
                            spinHalaman.setSelection(Integer.parseInt(i.getStringExtra("halaman")) - 1);
                            for (int s=0; s<spinSantri.getCount(); s++){
                                if (spinSantri.getItemAtPosition(s).toString().substring(0,1).equals(i.getStringExtra("idsantri"))){
                                    spinSantri.setSelection(s);
                                    s = spinSantri.getCount();
                                }
                            }
                            spinJuz.setSelection(Integer.parseInt(juz) - 1);
                            if (i.getStringExtra("kelancaran").equals("Lancar"))
                                radioLancar.setChecked(true);
                            else
                                radioUlang.setChecked(true);
                            edtKeterangan.setText(i.getStringExtra("keterangan"));
                            if (i.getStringExtra("murojaah").equals("1"))
                                checkMurojaah.setChecked(true);
                            else
                                checkMurojaah.setChecked(false);
                            DateFormat formatDb = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                            Date tanggal=null;
                            try {
                                tanggal = formatDb.parse(i.getStringExtra("tanggal"));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            edtTgl.setText(new SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(tanggal));
                        }
                    } else {
                        Toast.makeText(TambahUbahHafalanActivity.this, response.body().getDESC(), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } else {
                    Toast.makeText(TambahUbahHafalanActivity.this, "Terdapat masalah respon balik dari server", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<GetDataSantriResult> call, Throwable t) {
                progress.dismiss();
                Toast.makeText(TambahUbahHafalanActivity.this, "Cek koneksi anda dan coba lagi", Toast.LENGTH_SHORT).show();
                finish();
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

    private DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String formatLayar = "dd MMM yyyy";
            final SimpleDateFormat sdf = new SimpleDateFormat(formatLayar, Locale.getDefault());
            edtTgl.setText(sdf.format(myCalendar.getTime()));
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_simpan:
                if (edtTgl.getText().toString().equals(""))
                    Toast.makeText(this, "Tanggal tidak boleh kosong", Toast.LENGTH_SHORT).show();
                else if (radioGroupKelancaran.getCheckedRadioButtonId() == -1)
                    Toast.makeText(this, "Pilih salah satu kelancaran", Toast.LENGTH_SHORT).show();
                else if (!isOnline())
                    Toast.makeText(TambahUbahHafalanActivity.this, getString(R.string.cek_koneksi), Toast.LENGTH_SHORT).show();
                else if (idHafalan!=null){
                    ubahHafalan(spinSantri.getSelectedItem().toString().substring(0,1), idHafalan); // proses ubah hafalan
                } else {
                    String[] idnya = spinSantri.getSelectedItem().toString().split(Pattern.quote("-"));
                    tambahHafalan(idnya[0]); // proses tambah hafalan baru
                }
                break;
            case R.id.edt_tanggal_hafalan:
                new DatePickerDialog(TambahUbahHafalanActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
        }
    }
}
