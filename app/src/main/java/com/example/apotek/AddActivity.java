package com.example.apotek;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddActivity extends AppCompatActivity {

    //Declaration Variables
    DBHelper helper;
    EditText TxNomor, TxNama, TxNamaObat, TxTanggal, TxAlamat;
    Spinner SpND;
    long id;
    DatePickerDialog datePickerDialog;
    SimpleDateFormat dateFormatter;

    //Add Data
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        helper = new DBHelper(this);

        id = getIntent().getLongExtra(DBHelper.row_id, 0);

        //Declaration variables from activity_add.xml
        TxNomor = (EditText)findViewById(R.id.txNomor_Add);
        TxNama = (EditText)findViewById(R.id.txNama_Add);
        TxNamaObat = (EditText)findViewById(R.id.txNama_Obat_Add);
        TxTanggal = (EditText)findViewById(R.id.txTglObat_Add);
        TxAlamat = (EditText)findViewById(R.id.txAlamat_Add);
        SpND = (Spinner)findViewById(R.id.spND_Add);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        TxTanggal.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                showDateDialog();
            }
        });
    }

    //Opsi Set Kalender
    private void showDateDialog() {
        Calendar calendar = Calendar.getInstance();

        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, dayOfMonth);
                TxTanggal.setText(dateFormatter.format(newDate.getTime()));
            }
        },calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_menu, menu);
        return true;
    }

    //Add Data to Database
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_add:
                String nomor = TxNomor.getText().toString().trim();
                String nama = TxNama.getText().toString().trim();
                String namaObat = TxNamaObat.getText().toString().trim();
                String tanggal = TxTanggal.getText().toString().trim();
                String alamat = TxAlamat.getText().toString().trim();
                String nd = SpND.getSelectedItem().toString().trim();

                ContentValues values = new ContentValues();
                values.put(DBHelper.row_nomor, nomor);
                values.put(DBHelper.row_nama, nama);
                values.put(DBHelper.row_namaObat, namaObat);
                values.put(DBHelper.row_tglObat, tanggal);
                values.put(DBHelper.row_alamat, alamat);
                values.put(DBHelper.row_nd, nd);

                if(nomor.equals("") || nama.equals("") || namaObat.equals("") || tanggal.equals("") || alamat.equals("")){
                    Toast.makeText(AddActivity.this, "Data Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
                }else{
                    helper.insertData(values);
                    Toast.makeText(AddActivity.this, "Data Tersimpan", Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
        return super.onOptionsItemSelected(item);
    }
}