package com.hafidzquran.rumahquran.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.hafidzquran.rumahquran.R;
import com.hafidzquran.rumahquran.SessionManager;
import com.hafidzquran.rumahquran.ui.RuquButton;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        RuquButton btnSantri = (RuquButton)findViewById(R.id.btn_santri);
        RuquButton btnHafalan = (RuquButton)findViewById(R.id.btn_hafalan);
        RuquButton btnRekap = (RuquButton)findViewById(R.id.btn_rekap);
        RuquButton btnLogOut = (RuquButton)findViewById(R.id.btn_log_out);

        btnSantri.setOnClickListener(this);
        btnHafalan.setOnClickListener(this);
        btnRekap.setOnClickListener(this);
        btnLogOut.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_santri:
                startActivity(new Intent(this, SantriActivity.class));
                break;
            case R.id.btn_hafalan:
                startActivity(new Intent(this, HafalanActivity.class));
                break;
            case R.id.btn_rekap:
                startActivity(new Intent(this, RekapActivity.class));
                break;
            case R.id.btn_log_out:
                AlertDialog.Builder builder = new AlertDialog.Builder(MenuActivity.this);
                builder.setMessage("Apakah anda yakin ingin Logout?")
                        .setCancelable(true)
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                SessionManager sesi = new SessionManager(MenuActivity.this);
                                sesi.logoutUser();
                                finish();
                            }
                        })
                        .setNegativeButton("Tidak", null)
                        .setNeutralButton("Batal", null).create().show();
                break;
        }
    }
}
