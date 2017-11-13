package com.hafidzquran.rumahquran.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Switch;

import com.hafidzquran.rumahquran.R;
import com.hafidzquran.rumahquran.SessionManager;
import com.hafidzquran.rumahquran.apirequests.LoginRequestBody;
import com.hafidzquran.rumahquran.apiresults.LoginResult;
import com.hafidzquran.rumahquran.apiservices.LoginAdminService;
import com.hafidzquran.rumahquran.apiservices.LoginService;
import com.hafidzquran.rumahquran.ui.RuquButton;
import com.hafidzquran.rumahquran.ui.RuquEditText;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private RuquEditText edtUsername, edtPassword;
    private SessionManager sesi;
    private Switch switchAdmin;
    private ProgressDialog progress;
    private Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sesi = new SessionManager(LoginActivity.this);
        if (sesi.isLoggedIn()){
            if (sesi.getUserDetails().get("posisi").equals("guru"))
                startActivity(new Intent(LoginActivity.this, MenuActivity.class));
            else
                startActivity(new Intent(LoginActivity.this, AdminActivity.class));
            finish();
        }
        RuquButton btnLogin = (RuquButton)findViewById(R.id.btn_login);
        switchAdmin = (Switch)findViewById(R.id.switch_admin);
        switchAdmin.setChecked(false);
        edtUsername = (RuquEditText) findViewById(R.id.edt_username);
        edtPassword = (RuquEditText)findViewById(R.id.edt_password);
        btnLogin.setOnClickListener(this);
        RuquButton btnDaftar = (RuquButton)findViewById(R.id.btn_daftar);
        btnDaftar.setOnClickListener(this);
        progress = new ProgressDialog(LoginActivity.this);
        retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.BaseURL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = null;
        if (cm != null) {
            netInfo = cm.getActiveNetworkInfo();
        }
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_daftar:
                startActivity(new Intent(LoginActivity.this, DaftarActivity.class));
                break;
            case R.id.btn_login:
                if (edtUsername.getText().toString().equals("") || edtPassword.getText().toString().equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setMessage("Username atau password harus diisi");
                    builder.setCancelable(false);
                    builder.setPositiveButton("OK", null);
                    builder.create().show();
                } else if (!isOnline()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setMessage("Cek koneksi internet anda");
                    builder.setCancelable(false);
                    builder.setPositiveButton("OK", null);
                    builder.create().show();
                } else {
                    if (!switchAdmin.isChecked())
                        loginGuru();
                    else
                        loginAdmin();
                }
                break;
        }
    }

    private void loginAdmin() {
        progress.setMessage("Mengecek data..");
        progress.setCancelable(false);
        progress.show();
        final AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        LoginRequestBody body = new LoginRequestBody(edtUsername.getText().toString(), edtPassword.getText().toString());
        LoginAdminService service = retrofit.create(LoginAdminService.class);
        Call<LoginResult> result = service.getRespond(body);
        result.enqueue(new Callback<LoginResult>() {
            @Override
            public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                if (response.body() != null){
                    progress.dismiss();
                    List<LoginResult.isiData> isi = response.body().getDATA();
                    if (response.body().getCODE().equals("0")){
                        sesi.createLoginSession(edtUsername.getText().toString(), isi.get(0).getEMAIL(), "admin");
                        startActivity(new Intent(LoginActivity.this, AdminActivity.class));
                        finish();
                    } else {
                        builder.setMessage(response.body().getDESC());
                        builder.setCancelable(false);
                        builder.setPositiveButton("OK", null);
                        builder.create().show();
                    }
                } else {
                    progress.dismiss();
                    builder.setMessage(getString(R.string.retrofit_body_null));
                    builder.setCancelable(false);
                    builder.setPositiveButton("OK", null);
                    builder.create().show();
                }
            }

            @Override
            public void onFailure(Call<LoginResult> call, Throwable t) {
                progress.dismiss();
                builder.setMessage(getString(R.string.retrofit_failure));
                builder.setCancelable(false);
                builder.setPositiveButton("OK", null);
                builder.create().show();
                Log.d("pesan", t.toString());
            }
        });
    }

    private void loginGuru(){
        progress.setMessage("Mengecek data..");
        progress.setCancelable(false);
        progress.show();
        final AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        LoginRequestBody body = new LoginRequestBody(edtUsername.getText().toString(), edtPassword.getText().toString());
        LoginService service = retrofit.create(LoginService.class);
        Call<LoginResult> result = service.getRespond(body);
        result.enqueue(new Callback<LoginResult>() {
            @Override
            public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                if (response.body() != null){
                    progress.dismiss();
                    List<LoginResult.isiData> isi = response.body().getDATA();
                    if (response.body().getCODE().equals("0")){
                        sesi.createLoginSession(edtUsername.getText().toString(), isi.get(0).getEMAIL(), "guru");
                        startActivity(new Intent(LoginActivity.this, MenuActivity.class));
                        finish();
                    } else {
                        builder.setMessage(response.body().getDESC());
                        builder.setCancelable(false);
                        builder.setPositiveButton("OK", null);
                        builder.create().show();
                    }
                } else {
                    progress.dismiss();
                    builder.setMessage(getString(R.string.retrofit_body_null));
                    builder.setCancelable(false);
                    builder.setPositiveButton("OK", null);
                    builder.create().show();
                }
            }

            @Override
            public void onFailure(Call<LoginResult> call, Throwable t) {
                progress.dismiss();
                builder.setMessage(getString(R.string.retrofit_failure));
                builder.setCancelable(false);
                builder.setPositiveButton("OK", null);
                builder.create().show();
                Log.d("pesan", t.toString());
            }
        });
    }
}
