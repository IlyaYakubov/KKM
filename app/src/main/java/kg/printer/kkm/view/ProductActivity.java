package kg.printer.kkm.view;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import kg.printer.kkm.R;
import kg.printer.kkm.controllers.UIViewController;
import kg.printer.kkm.repositories.DatabaseDAO;
import kg.printer.kkm.repositories.Database;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProductActivity extends UIViewController.BaseAdapter implements View.OnClickListener, Database {

    private Spinner spr_basic_unit;
    private EditText et_name, et_coast;
    private Button btn_ok;

    private int newElement; // 1 true - 0 false
    private int position_on_list;

    private DatabaseDAO dbHelper;

    private ArrayAdapter<String> adapterBasicUnit;
    private ArrayList<String> listUnits = new ArrayList<>();

    private String basicUnit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        initView();
        addListener();
        init();
    }

    @Override
    public void initView() {
        readUnitsFromBaseData();

        adapterBasicUnit = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listUnits);
        adapterBasicUnit.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spr_basic_unit = findViewById(R.id.spr_basic_unit);
        spr_basic_unit.setAdapter(adapterBasicUnit);
        spr_basic_unit.setPrompt("Единица измерения");
        spr_basic_unit.setSelection(0);
        spr_basic_unit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                basicUnit = spr_basic_unit.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        et_name = findViewById(R.id.et_name);
        et_coast = findViewById(R.id.et_coast);
        et_coast.setFilters(new InputFilter[] {new ProductActivity.DecimalDigitsInputFilter(2)});

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
                    UIViewController.ToastAdapter.show(this, "Заполните наименование товара");
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
    public void readData() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select good
        Cursor cursor = db.rawQuery("select * from products where position_on_list = ?"
                , new String[] { String.valueOf(position_on_list) });

        if (cursor.moveToNext()) {
            int nameColIndex = cursor.getColumnIndex("name");
            int basicUnitColIndex = cursor.getColumnIndex("unit");
            //int tnvedColIndex = cursor.getColumnIndex("tnved");
            int coastColIndex = cursor.getColumnIndex("coast");

            et_name.setText(cursor.getString(nameColIndex));
            spr_basic_unit.setSelection(adapterBasicUnit.getPosition(cursor.getString(basicUnitColIndex)));
            et_coast.setText(cursor.getString(coastColIndex));
        }

        cursor.close();
    }

    @Override
    public void updateData() {

    }

    @Override
    public void addData() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();

        String name = et_name.getText().toString();
        String coast = et_coast.getText().toString();

        cv.put("name", name);
        cv.put("unit", basicUnit);
        cv.put("coast", coast);

        if (newElement == 1) {
            cv.put("position_on_list", lastPosition() + 1);
            db.insert("products", null, cv);
        } else {
            cv.put("position_on_list", position_on_list);
            db.update("products", cv, "position_on_list = ?", new String[] { String.valueOf(position_on_list) });
        }
    }

    @Override
    public void deleteData() {

    }

    @Override
    public int lastPosition() {
        dbHelper = new DatabaseDAO(getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all products
        Cursor cursor = db.query("products", null, null, null, null, null, null);

        int lastPosition = -1;
        while (cursor.moveToNext()) {
            lastPosition++;
        }

        cursor.close();

        return lastPosition;
    }

    private void readUnitsFromBaseData() {
        dbHelper = new DatabaseDAO(getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all units
        Cursor cursor = db.query("units", null, null, null, null, null, null);

        listUnits.clear();

        while (cursor.moveToNext()) {
            int positionColIndex = cursor.getColumnIndex("name");
            listUnits.add(cursor.getString(positionColIndex));
        }

        cursor.close();
    }

    private void hideKeyboard(View view) {
        View viewLayout = this.getCurrentFocus();
        if (viewLayout != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) ProductActivity.this.getSystemService(UIViewController.BaseAdapter.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public class DecimalDigitsInputFilter implements InputFilter {

        Pattern mPattern;

        public DecimalDigitsInputFilter(int digitsAfterZero) {
            mPattern = Pattern.compile("[0-9]+((\\.[0-9]{0," + (digitsAfterZero-1) + "})?)|(\\.)?");
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            Matcher matcher = mPattern.matcher(dest);
            if(!matcher.matches())
                return "";
            return null;
        }

    }

    public static class Product {

        public String name, price, unit;

        public Product(String _describe, String _price, String _unit) {
            name = _describe;
            price = _price;
            unit = _unit;
        }

    }
}
