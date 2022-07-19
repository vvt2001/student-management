package com.example.studentmanagement;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class ListActivity extends AppCompatActivity {

    SQLiteDatabase db;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.student_list_view);

        // Open DB

        String path = getFilesDir() + "/mydb";
        try {
            db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.CREATE_IF_NECESSARY);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // createTable();

        findViewById(R.id.button_insert).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.beginTransaction();
                try {
//                    Faker faker = new Faker();
//                    String name = faker.name.name();
//                    String phone = faker.phoneNumber.phoneNumber();
//
//                    db.execSQL("insert into tblAMIGO(name, phone) values('" + name + "', '" + phone + "')");

                    ContentValues cv = new ContentValues();

                    cv.put("name", "ABC");
                    cv.put("phone", "555-1010");
                    long ret = db.insert("tblAMIGO", null, cv);
                    Log.v("TAG", "ret = " + ret);

                    cv.put("name", "DEF");
                    cv.put("phone", "555-2020");
                    ret = db.insert("tblAMIGO", null, cv);
                    Log.v("TAG", "ret = " + ret);

                    cv.clear();

                    ret = db.insert("tblAMIGO", null, cv);
                    Log.v("TAG", "ret = " + ret);

                    ret = db.insert("tblAMIGO", "name", cv);
                    Log.v("TAG", "ret = " + ret);

                    db.setTransactionSuccessful();
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    db.endTransaction();
                }
            }
        });

        findViewById(R.id.button_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.beginTransaction();
                try {
//                    db.execSQL("update tblAMIGO set phone='555-0000' where name='AAA'");

                    ContentValues cv = new ContentValues();
                    cv.put("name", "Maria");

                    long ret = db.update("tblAMIGO", cv, "recID > 9 and recID < 16", null);
                    Log.v("TAG", "ret = " + ret);

                    db.setTransactionSuccessful();
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    db.endTransaction();
                }
            }
        });

        findViewById(R.id.button_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.beginTransaction();
                try {
//                    db.execSQL("delete from tblAMIGO where recID<5");

                    long ret = db.delete("tblAMIGO", "recID > 9 and recID < 16", null);
                    Log.v("TAG", "ret = " + ret);

                    db.setTransactionSuccessful();
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    db.endTransaction();
                }
            }
        });

        findViewById(R.id.button_select).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String sql = "select * from tblAMIGO";
//                Cursor cs = db.rawQuery(sql, null);
                String[] columns = {"recID", "name", "phone"};
                Cursor cs = db.query("tblAMIGO", columns,
                        null, null, null, null ,null);

                Log.v("TAG", "# records: " + cs.getCount());

                cs.moveToPosition(-1);
                while (cs.moveToNext()) {
                    int recID = cs.getInt(0);
                    String name = cs.getString(1);
                    String phone = cs.getString(2);

                    Log.v("TAG", recID + " --- " +name + " --- " + phone);
                }

                ItemAdapter adapter = new ItemAdapter(cs);
                listView.setAdapter(adapter);
            }
        });
    }

    public void createTable() {
        db.beginTransaction();
        try {
            db.execSQL("create table tblAMIGO(" +
                    "recID integer PRIMARY KEY autoincrement," +
                    "name text," +
                    "phone text)");

            db.execSQL("insert into tblAMIGO(name, phone) values('AAA', '555-1111')");
            db.execSQL("insert into tblAMIGO(name, phone) values('BBB', '555-2222')");
            db.execSQL("insert into tblAMIGO(name, phone) values('CCC', '555-3333')");

            db.setTransactionSuccessful();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    @Override
    protected void onDestroy() {
        db.close();
        super.onDestroy();
    }
}
