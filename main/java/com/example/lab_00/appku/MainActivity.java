package com.example.lab_00.appku;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sp;
    Button signOut;
    TextView username;
    EditText pencarian, id_mk, nama_mk, sks_mk;
    ListView listMataKuliah;
    ArrayList<String> data;
    ArrayAdapter<String> adapter;
    FloatingActionButton tambah;
    MataKuliahHelper mkHelper;
    SQLiteDatabase db;
    AlertDialog.Builder builder;
    AlertDialog dialog;
    LayoutInflater inflater;
    View viewInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sp = getSharedPreferences("statusLogin", MODE_PRIVATE);

        signOut = (Button) findViewById(R.id.sign_out);

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor e = sp.edit();

                e.clear();
                e.commit();

                Intent i = new Intent(MainActivity.this, LoginActivity.class);

                startActivity(i);
            }
        });

        username = (TextView) findViewById(R.id.username);

        username.setText("Aditya Wicaksono (" + sp.getString("username", "") + ")");

        /*String[] data = {
                "Pemrograman Mobile",
                "Sistem Client Server",
                "Perancangan Web"
        };*/

        data = new ArrayList<String>();

        mkHelper = new MataKuliahHelper(getApplicationContext());

        db = mkHelper.getReadableDatabase();

        //Cursor cursor = db.rawQuery("SELECT * FROM tbl_mata_kuliah", null);
        Cursor cursor = db.query(
                "tbl_mata_kuliah",
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );

        cursor.moveToFirst();

        while (! cursor.isAfterLast()) {
            data.add(cursor.getString(0));

            cursor.moveToNext();
        }

        cursor.close();

        adapter = new ArrayAdapter<String>(
                MainActivity.this,
                android.R.layout.simple_list_item_1,
                data
        );

        listMataKuliah = (ListView) findViewById(R.id.list_mata_kuliah);

        listMataKuliah.setAdapter(adapter);

        listMataKuliah.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                builder = new AlertDialog.Builder(MainActivity.this);

                inflater = getLayoutInflater();

                viewInflater = inflater.inflate(R.layout.custom_dialog, null);

                id_mk = viewInflater.findViewById(R.id.id_mk);
                nama_mk = viewInflater.findViewById(R.id.nama_mk);
                sks_mk = viewInflater.findViewById(R.id.sks_mk);

                Cursor cursor = db.rawQuery(
                        "SELECT * FROM tbl_mata_kuliah WHERE id_mata_kuliah='" + adapter.getItem(position) + "'",
                        null
                        );

                cursor.moveToFirst();

                id_mk.setText(cursor.getString(0));
                nama_mk.setText(cursor.getString(1));
                sks_mk.setText(cursor.getString(2));

                builder.setTitle("Form Mata Kuliah")
                        .setIcon(R.drawable.ic_edit)
                        .setView(viewInflater)
                        .setCancelable(true)
                        .setPositiveButton("Simpan", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                db = mkHelper.getWritableDatabase();

                                String id = id_mk.getText().toString();

                                db.execSQL("UPDATE tbl_mata_kuliah SET id_mata_kuliah='" +
                                        id + "', mata_kuliah='" +
                                        nama_mk.getText().toString() + "', sks='" +
                                        sks_mk.getText().toString() + "' WHERE id_mata_kuliah='" +
                                        adapter.getItem(position) + "'");

                                adapter.remove(adapter.getItem(position));
                                adapter.add(id);
                            }
                        })
                        .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                dialog = builder.create();

                dialog.show();
            }
        });

        listMataKuliah.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                builder = new AlertDialog.Builder(MainActivity.this);

                builder.setTitle("Konfirmasi")
                        .setIcon(R.drawable.ic_ask)
                        .setMessage("Apakah Anda yakin?")
                        .setCancelable(true)
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                db = mkHelper.getWritableDatabase();

                                String id = adapter.getItem(position);

                                db.execSQL("DELETE FROM tbl_mata_kuliah WHERE id_mata_kuliah='" + id + "'");

                                adapter.remove(id);
                            }
                        })
                        .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                dialog = builder.create();

                dialog.show();

                return false;
            }
        });

        pencarian = (EditText) findViewById(R.id.pencarian);

        pencarian.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        tambah = (FloatingActionButton) findViewById(R.id.tambah);

        tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder = new AlertDialog.Builder(MainActivity.this);

                inflater = getLayoutInflater();

                viewInflater = inflater.inflate(R.layout.custom_dialog, null);

                builder.setTitle("Form Mata Kuliah")
                        .setIcon(R.drawable.ic_add)
                        .setView(viewInflater)
                        .setCancelable(true)
                        .setPositiveButton("Simpan", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                id_mk = (EditText) viewInflater.findViewById(R.id.id_mk);
                                nama_mk = (EditText) viewInflater.findViewById(R.id.nama_mk);
                                sks_mk = (EditText) viewInflater.findViewById(R.id.sks_mk);

                                db = mkHelper.getWritableDatabase();

                                db.execSQL("INSERT INTO tbl_mata_kuliah VALUES('" +
                                        id_mk.getText().toString() + "', '" +
                                        nama_mk.getText().toString() + "', '" +
                                        sks_mk.getText().toString() + "')");

                                adapter.add(id_mk.getText().toString());
                            }
                        })
                        .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                dialog = builder.create();

                dialog.show();
            }
        });
    }
}
