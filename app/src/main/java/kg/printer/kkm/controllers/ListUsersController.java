package kg.printer.kkm.controllers;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import kg.printer.kkm.R;
import kg.printer.kkm.domains.Administrator;
import kg.printer.kkm.domains.User;
import kg.printer.kkm.repositories.BaseDataDAO;
import kg.printer.kkm.repositories.LocalDataBaseDAO;

import java.util.ArrayList;

public class ListUsersController extends BaseController implements View.OnClickListener, LocalDataBaseDAO {

    private ListView lv_data;
    private Button btn_add;
    private ArrayList<String> data = new ArrayList<>();

    private BaseDataDAO dbHelper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_u_list_users);

        initView();
        addListener();
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();

        readData();
    }

    @Override
    public void initView() {
        lv_data = findViewById(R.id.lv_data);
        btn_add = findViewById(R.id.btn_add);
    }

    @Override
    public void addListener() {
        btn_add.setOnClickListener(this);
    }

    @Override
    public void init() {
        dbHelper = new BaseDataDAO(getApplicationContext());

        if (data.isEmpty()) {
            db = dbHelper.getWritableDatabase();

            // select administrator
            Cursor cursor = db.query("users", null, "is_admin = 1", null, null, null, null);

            if (!cursor.moveToFirst())
                addData();

            cursor.close();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
                turnToActivityWithPosition(User.class, -1, 1);
                break;
            default:
                break;
        }
    }

    @Override
    public void readData() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all users
        Cursor cursor = db.query("users", null, null, null, null, null, null);

        data.clear();

        while (cursor.moveToNext()) {
            int positionColIndex = cursor.getColumnIndex("position");
            data.add(cursor.getString(positionColIndex));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, data);
        lv_data.setAdapter(adapter);

        lv_data.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    turnToActivity(Administrator.class);
                } else {
                    turnToActivityWithPosition(User.class, position, 0);
                }
            }
        });

        cursor.close();
    }

    @Override
    public void updateData() {

    }

    @Override
    public void addData() {
        ContentValues cv = new ContentValues();

        cv.put("is_admin", 1);
        cv.put("position_on_list", 0);
        cv.put("position", "Администратор");
        cv.put("surname", "");
        cv.put("name", "");
        cv.put("second_name", "");

        db.insert("users", null, cv);
    }

    @Override
    public void deleteData() {

    }

    @Override
    public int lastPosition() {
        return 0;
    }

}