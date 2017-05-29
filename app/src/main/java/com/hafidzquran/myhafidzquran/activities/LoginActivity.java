package com.hafidzquran.myhafidzquran.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.hafidzquran.myhafidzquran.R;
import com.hafidzquran.myhafidzquran.SessionManager;
import com.hafidzquran.myhafidzquran.apirequests.LoginRequestBody;
import com.hafidzquran.myhafidzquran.apiresults.LoginResult;
import com.hafidzquran.myhafidzquran.apiservices.LoginService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtUsername;
    private EditText edtPassword;
    private SessionManager sesi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sesi = new SessionManager(LoginActivity.this);
        if (sesi.isLoggedIn()){
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            finish();
        }
        edtUsername = (EditText)findViewById(R.id.edt_username);
        edtPassword = (EditText)findViewById(R.id.edt_password);
        Button btnLogin = (Button)findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(this);
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
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
                    loginGuru();
                }
                break;
        }
    }

    private void loginGuru(){
        final ProgressDialog progress = new ProgressDialog(LoginActivity.this);
        progress.setMessage("Mengecek data..");
        progress.setCancelable(false);
        progress.show();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.BaseURL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
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
                        sesi.createLoginSession(edtUsername.getText().toString(), isi.get(0).getEMAIL());
                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
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
            public void onFailure(Call<LoginResult> call, Throwable t) {
                progress.dismiss();
                builder.setMessage("Tidak dapat tersambung ke server, cek koneksi internet anda");
                builder.setCancelable(false);
                builder.setPositiveButton("OK", null);
                builder.create().show();
            }
        });
    }
}
