package com.hafidzquran.rumahquran.activities;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.hafidzquran.rumahquran.R;
import com.hafidzquran.rumahquran.apirequests.DaftarRequestBody;
import com.hafidzquran.rumahquran.apiresults.DaftarResult;
import com.hafidzquran.rumahquran.apiservices.DaftarService;
import com.hafidzquran.rumahquran.ui.RuquButton;
import com.hafidzquran.rumahquran.ui.RuquEditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class DaftarActivity extends AppCompatActivity implements View.OnClickListener {
    private RuquEditText edtNamaLengkap, edtUser, edtEmail, edtPass, edtConfirmPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar);

        edtNamaLengkap = (RuquEditText)findViewById(R.id.edt_nama_guru);
        edtNamaLengkap.setText(getIntent().getStringExtra("nama"));
        edtEmail = (RuquEditText)findViewById(R.id.edt_email);
        edtEmail.setText(getIntent().getStringExtra("email"));
        edtUser = (RuquEditText)findViewById(R.id.edt_username);
        edtUser.setText(getIntent().getStringExtra("username"));
        edtPass = (RuquEditText)findViewById(R.id.edt_password);
        edtConfirmPass = (RuquEditText)findViewById(R.id.edt_confirm_password);
        RuquButton btnDaftar = (RuquButton) findViewById(R.id.btn_daftar);
        btnDaftar.setOnClickListener(this);
    }

    // method untuk mendeteksi apakah inputan email sesuai dengan format email atau tidak
    private static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private void daftarGuru(){
        final ProgressDialog progress = new ProgressDialog(DaftarActivity.this);
        progress.setMessage("Mendaftarkan data..");
        progress.setCancelable(false);
        progress.show();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.BaseURL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        DaftarRequestBody body = new DaftarRequestBody(edtUser.getText().toString(),
                edtNamaLengkap.getText().toString(), edtEmail.getText().toString(),
                edtPass.getText().toString(), getString(R.string.kodeaplikasi));
        DaftarService service = retrofit.create(DaftarService.class);
        Call<DaftarResult> result = service.getRespond(body);
        result.enqueue(new Callback<DaftarResult>() {
            @Override
            public void onResponse(Call<DaftarResult> call, Response<DaftarResult> response) {
                progress.dismiss();
                if (response.body() != null){
                    progress.dismiss();
                    if (response.body().getCODE().equals("0")){
                        Toast.makeText(DaftarActivity.this, response.body().getDESC(), Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(DaftarActivity.this, response.body().getDESC(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(DaftarActivity.this, "Terdapat masalah respon balik dari server", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DaftarResult> call, Throwable t) {
                progress.dismiss();
                Toast.makeText(DaftarActivity.this, "Gagal tersambung ke server, cek koneksi internet anda", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_daftar:
                if (edtUser.getText().toString().equals("") ||
                        edtPass.getText().toString().equals("") ||
                        edtConfirmPass.getText().toString().equals("") ||
                        edtNamaLengkap.getText().toString().equals("") ||
                        edtEmail.getText().toString().equals("")){
                    Toast.makeText(this, "Mohon lengkapi semua field yang kosong", Toast.LENGTH_SHORT).show();
                } else if (!edtPass.getText().toString().equals(edtConfirmPass.getText().toString())){
                    Toast.makeText(this, "Konfirmasi password tidak cocok", Toast.LENGTH_SHORT).show();
                } else if (edtUser.getText().length() < 4){
                    Toast.makeText(this, "Username minimal 4 karakter", Toast.LENGTH_SHORT).show();
                } else if (!isEmailValid(edtEmail.getText().toString())) {
                    Toast.makeText(this, "Email tidak sesuai formatnya", Toast.LENGTH_SHORT).show();
                } else {
                    daftarGuru();
                }
                break;
        }
    }
}
