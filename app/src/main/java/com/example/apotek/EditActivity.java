package com.example.apotek;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
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

public class EditActivity extends AppCompatActivity {

    //Declaration Variables
    DBHelper helper;
    EditText TxNomor, TxNama, TxNamaObat, TxTanggal, TxAlamat;
    Spinner SpND;
    long id;
    DatePickerDialog datePickerDialog;
    SimpleDateFormat dateFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        helper = new DBHelper(this);

        id = getIntent().getLongExtra(DBHelper.row_id, 0);

        //Declaration Variable from activity_edit.xml to here || get the id from activity_edit.xml to these variables
        TxNomor = (EditText)findViewById(R.id.txNomor_Edit);
        TxNama = (EditText)findViewById(R.id.txNama_Edit);
        TxNamaObat = (EditText)findViewById(R.id.txNamaObat_Edit);
        TxTanggal = (EditText)findViewById(R.id.txTglObat_Edit);
        TxAlamat = (EditText)findViewById(R.id.txAlamat_Edit);
        SpND = (Spinner)findViewById(R.id.spND_Edit);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        TxTanggal.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                showDateDialog();
            }
        });
        getData();
    }

    private void showDateDialog(){
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

    private void getData(){
        Cursor cursor = helper.oneData(id);
        if(cursor.moveToFirst()){
            String nomor = cursor.getString(cursor.getColumnIndex(DBHelper.row_nomor));
            String nama = cursor.getString(cursor.getColumnIndex(DBHelper.row_nama));
            String namaObat = cursor.getString(cursor.getColumnIndex(DBHelper.row_namaObat));
            String nd = cursor.getString(cursor.getColumnIndex(DBHelper.row_nd));
            String tanggal = cursor.getString(cursor.getColumnIndex(DBHelper.row_tglObat));
            String alamat = cursor.getString(cursor.getColumnIndex(DBHelper.row_alamat));

            TxNomor.setText(nomor);
            TxNama.setText(nama);


            if(nd.equals("Doctor-Andika")){
                SpND.setSelection(0);
            }else if(nd.equals("Doctor-Tarish")){
                SpND.setSelection(1);
            }

            TxNamaObat.setText(namaObat);
            TxTanggal.setText(tanggal);
            TxAlamat.setText(alamat);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.edit_menu, menu);
        return true;
    }

    //Edit data to database
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.savev_edit:
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
                    Toast.makeText(EditActivity.this, "Data Tidak Boleh Kosong", Toast.LENGTH_SHORT);
                }else{
                    helper.updateData(values, id);
                    Toast.makeText(EditActivity.this, "Data Tersimpan", Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
        switch (item.getItemId()){
            case R.id.delete_edit:
                AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this);
                builder.setMessage("Data ini akan dihapus.");
                builder.setCancelable(true);
                builder.setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        helper.deleteData(id);
                        Toast.makeText(EditActivity.this, "Data Terhapus", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }
}