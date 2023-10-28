package kg.printer.kkm.view;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import kg.printer.kkm.R;
import kg.printer.kkm.controllers.UIViewController;
import kg.printer.kkm.domains.Organization;
import kg.printer.kkm.repositories.DatabaseDAO;
import kg.printer.kkm.repositories.Database;

public class UnitActivity extends UIViewController.BaseAdapter implements View.OnClickListener, Database {

    private EditText et_name, et_full_name, et_code_name;
    private Button btn_ok;

    private int newElement; // 1 true - 0 false
    private int position_on_list;

    private DatabaseDAO dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit);

        initView();
        addListener();
        init();
    }

    @Override
    public void initView() {
        et_name = findViewById(R.id.et_name);
        et_full_name = findViewById(R.id.et_full_name);
        et_code_name = findViewById(R.id.et_code_name);

        btn_ok = findViewById(R.id.btn_ok);
    }

    @Override
    public void addListener() {
        btn_ok.setOnClickListener(this);
    }

    @Override
    public void init() {
        dbHelper = new DatabaseDAO(getApplicationContext());

        Intent intent = getIntent();
        position_on_list = intent.getIntExtra("position_on_list", -1);
        newElement = intent.getIntExtra("new_element", 1);

        if (newElement == 0) {
            readData();
        } else {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_ok:
                if (et_name.getText().toString().isEmpty()) {
                    UIViewController.ToastAdapter.show(this, "Заполните наименование единицы измерения");
                } else {
                    addData();
                    hideKeyboard(view);
                    finish();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public Organization readData() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select unit
        Cursor cursor = db.rawQuery("select * from units where position_on_list = ?"
                , new String[] { String.valueOf(position_on_list) });

        if (cursor.moveToFirst()) {
            int nameColIndex = cursor.getColumnIndex("name");
            int fuulNameColIndex = cursor.getColumnIndex("full_name");
            int codeColIndex = cursor.getColumnIndex("code");

            et_name.setText(cursor.getString(nameColIndex));
            et_full_name.setText(cursor.getString(fuulNameColIndex));
            et_code_name.setText(cursor.getString(codeColIndex));
        }

        cursor.close();
        return null;
    }

    @Override
    public void updateData() {

    }

    @Override
    public void addData() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();

        String name = et_name.getText().toString();
        String fullName = et_full_name.getText().toString();
        String codeName = et_code_name.getText().toString();

        cv.put("name", name);
        cv.put("full_name", fullName);
        cv.put("code", codeName);

        if (newElement == 1) {
            cv.put("position_on_list", lastPosition() + 1);
            db.insert("units", null, cv);
        } else {
            cv.put("position_on_list", position_on_list);
            db.update("units", cv, "position_on_list = ?", new String[] { String.valueOf(position_on_list) });
        }
    }

    @Override
    public void deleteData() {

    }

    @Override
    public int lastPosition() {
        dbHelper = new DatabaseDAO(getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all units
        Cursor cursor = db.query("units", null, null, null, null, null, null);

        int lastPosition = -1;
        while (cursor.moveToNext()) {
            lastPosition++;
        }

        cursor.close();

        return lastPosition;
    }

    private void hideKeyboard(View view) {
        View viewLayout = this.getCurrentFocus();
        if (viewLayout != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) UnitActivity.this.getSystemService(UIViewController.BaseAdapter.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}
