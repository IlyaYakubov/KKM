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
import kg.printer.kkm.repositories.DatabaseDAO;

public class UnitActivity extends UIViewController.BaseAdapter implements View.OnClickListener {

    private EditText etName, etFullName, etCodeName;
    private Button btnOk;

    private int newItem; // 1 true - 0 false
    private int listIndex;

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
        etName = findViewById(R.id.et_name);
        etFullName = findViewById(R.id.et_full_name);
        etCodeName = findViewById(R.id.et_code_name);

        btnOk = findViewById(R.id.btn_ok);
    }

    @Override
    public void addListener() {
        btnOk.setOnClickListener(this);
    }

    @Override
    public void init() {
        dbHelper = new DatabaseDAO(getApplicationContext());

        Intent intent = getIntent();
        listIndex = intent.getIntExtra("listIndex", -1);
        newItem = intent.getIntExtra("newItem", 1);

        if (newItem == 0) {
            readData();
        } else {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_ok:
                if (etName.getText().toString().isEmpty()) {
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

    public void readData() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select unit
        Cursor cursor = db.rawQuery("select * from units where position_on_list = ?"
                , new String[] { String.valueOf(listIndex) });

        if (cursor.moveToFirst()) {
            int nameColIndex = cursor.getColumnIndex("name");
            int fuulNameColIndex = cursor.getColumnIndex("full_name");
            int codeColIndex = cursor.getColumnIndex("code");

            etName.setText(cursor.getString(nameColIndex));
            etFullName.setText(cursor.getString(fuulNameColIndex));
            etCodeName.setText(cursor.getString(codeColIndex));
        }

        cursor.close();
    }

    public void addData() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();

        String name = etName.getText().toString();
        String fullName = etFullName.getText().toString();
        String codeName = etCodeName.getText().toString();

        cv.put("name", name);
        cv.put("full_name", fullName);
        cv.put("code", codeName);

        if (newItem == 1) {
            cv.put("position_on_list", lastPosition() + 1);
            db.insert("units", null, cv);
        } else {
            cv.put("position_on_list", listIndex);
            db.update("units", cv, "position_on_list = ?", new String[] { String.valueOf(listIndex) });
        }
    }

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
