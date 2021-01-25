package com.example.apotek;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.LayoutInflater;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    //Declaration Variables
    ListView listView;
    DBHelper helper;
    LayoutInflater inflater;
    View dialogView;
    TextView Tv_Nomor, Tv_Nama, Tv_NamaObat, Tv_ND, Tv_Tanggal, Tv_Alamat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Declaration Variable id=fab from (activity_main.xml) fab is an id add icon
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        
        //Method for clicking the add button to the next class java
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //To add command from button events fab(add icon)
                startActivity(new Intent( MainActivity.this, AddActivity.class));
            }
        });

        helper = new DBHelper(this);
        listView = (ListView)findViewById(R.id.list_data);
        listView.setOnItemClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setListView(){
        Cursor cursor = helper.allData();
        CustomCursorAdapter customCursorAdapter = new CustomCursorAdapter(this, cursor, 1);
        listView.setAdapter(customCursorAdapter);
    }


    //Method when the name of the text in the apk on hold
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int i, long x) {
        TextView getId = (TextView)view.findViewById(R.id.listID);
        final long id = Long.parseLong(getId.getText().toString());
        final Cursor cur = helper.oneData(id);
        cur.moveToFirst();

        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity. this);
        builder.setTitle("Pilih Opsi");

        //Add a List
        String[] options = {"Lihat Data", "Edit Data", "Hapus Data"};
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        final AlertDialog.Builder viewData = new AlertDialog.Builder(MainActivity.this);
                        inflater = getLayoutInflater();
                        dialogView = inflater.inflate(R.layout.view_data, null);
                        viewData.setView(dialogView);
                        viewData.setTitle("Lihat Data");

                        //Declaration Variable MainActivity from widget layout (view_data.xml)
                        Tv_Nomor = (TextView) dialogView.findViewById(R.id.tv_No);
                        Tv_Nama = (TextView) dialogView.findViewById(R.id.tv_Nama);
                        Tv_NamaObat = (TextView) dialogView.findViewById(R.id.tv_NamaObat);
                        Tv_Tanggal = (TextView) dialogView.findViewById(R.id.tv_Tanggal);
                        Tv_ND = (TextView) dialogView.findViewById(R.id.tv_ND);
                        Tv_Alamat = (TextView) dialogView.findViewById(R.id.tv_Alamat);

                        //get value from database SQLite
                        Tv_Nomor.setText("Nomor: " + cur.getString(cur.getColumnIndex(DBHelper.row_nomor)));
                        Tv_Nama.setText("Nama: " + cur.getString(cur.getColumnIndex(DBHelper.row_nama)));
                        Tv_NamaObat.setText("Nama Obat: " + cur.getString(cur.getColumnIndex(DBHelper.row_namaObat)));
                        Tv_Tanggal.setText("Tanggal Obat: " + cur.getString(cur.getColumnIndex(DBHelper.row_tglObat)));
                        Tv_ND.setText("Nama Dokter: " + cur.getString(cur.getColumnIndex(DBHelper.row_nd)));
                        Tv_Alamat.setText("Alamat: " + cur.getString(cur.getColumnIndex(DBHelper.row_alamat)));

                        viewData.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        viewData.show();
                }
                switch (which) {
                    case 1:
                        Intent iddata = new Intent(MainActivity.this, EditActivity.class);
                        iddata.putExtra(DBHelper.row_id, id);
                        startActivity(iddata);
                }
                switch (which) {
                    case 2:
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                        builder1.setMessage("Data ini akan dihapus.");
                        builder1.setCancelable(true);
                        builder1.setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                helper.deleteData(id);
                                Toast.makeText(MainActivity.this, "Data Terhapus", Toast.LENGTH_SHORT).show();
                                setListView();
                            }
                        });
                        builder1.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        AlertDialog alertDialog = builder1.create();
                        alertDialog.show();
                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setListView();
    }
}