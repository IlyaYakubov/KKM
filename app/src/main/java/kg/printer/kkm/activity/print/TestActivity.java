package kg.printer.kkm.activity.print;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import kg.printer.kkm.R;
import kg.printer.kkm.app.BaseActivity;
import kg.printer.kkm.utils.BaseData;

import java.util.ArrayList;

public class TestActivity extends BaseActivity implements View.OnClickListener {

    private Spinner spr_goods, spr_units;
    private TextView tv_result;
    private EditText et_quantity;
    private Button btn_exe;

    private BaseData dbHelper;

    private ArrayAdapter<String> adapterGoods, adapterUnit;
    private ArrayList<String> listGoods = new ArrayList<>();
    private ArrayList<String> listUnits = new ArrayList<>();

    private String good, unit, basicUnit = "", coefficient = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        initView();
        addListener();
        init();
    }

    @Override
    public void initView() {
        readGoodsFromBaseData();
        readUnitsFromBaseData();

        adapterGoods = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listGoods);
        adapterGoods.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spr_goods = findViewById(R.id.spr_goods);
        spr_goods.setAdapter(adapterGoods);
        spr_goods.setPrompt("Товар");
        spr_goods.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                good = spr_goods.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        adapterUnit = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listUnits);
        adapterUnit.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spr_units = findViewById(R.id.spr_units);
        spr_units.setAdapter(adapterUnit);
        spr_units.setPrompt("Единица измерения");
        spr_units.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                unit = spr_units.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        tv_result = findViewById(R.id.tv_result);
        et_quantity = findViewById(R.id.et_quantity);
        btn_exe = findViewById(R.id.btn_exe);
    }

    @Override
    public void addListener() {
        btn_exe.setOnClickListener(this);
    }

    @Override
    public void init() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_exe:
                takeCoefficient();
                takeBasicUnit();

                String quantityStr = et_quantity.getText().toString();
                if (!quantityStr.isEmpty() && !coefficient.isEmpty()) {
                    int quantity = Integer.parseInt(quantityStr);
                    int coefficientInt = Integer.parseInt(coefficient);
                    int result = quantity * coefficientInt;

                    tv_result.setText("Количество " + quantityStr + " " + unit + " это " + result + " " + basicUnit);
                }
                break;
            default:
                break;
        }
    }

    public void readGoodsFromBaseData() {
        dbHelper = new BaseData(getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all goods
        Cursor cursor = db.query("goods", null, null, null, null, null, null);

        listGoods.clear();

        while (cursor.moveToNext()) {
            int positionColIndex = cursor.getColumnIndex("name");
            listGoods.add(cursor.getString(positionColIndex));
        }

        cursor.close();
    }

    public void readUnitsFromBaseData() {
        dbHelper = new BaseData(getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all packages
        Cursor cursor = db.query("packages", null, null, null, null, null, null);

        listUnits.clear();

        while (cursor.moveToNext()) {
            int positionColIndex = cursor.getColumnIndex("name");
            listUnits.add(cursor.getString(positionColIndex));
        }

        cursor.close();
    }

    public void takeBasicUnit() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select good
        Cursor cursor = db.rawQuery("select * from goods where name = ?"
                , new String[] { good } );

        if (cursor.moveToFirst()) {
            int basicUnitColIndex = cursor.getColumnIndex("basic_unit");

            basicUnit = cursor.getString(basicUnitColIndex);
        }

        cursor.close();
    }

    public void takeCoefficient() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select package
        Cursor cursor = db.rawQuery("select * from packages where good = ?"
                , new String[] { good } );

        if (cursor.moveToFirst()) {
            int coefficientColIndex = cursor.getColumnIndex("coefficient");

            coefficient = cursor.getString(coefficientColIndex);
        }

        cursor.close();
    }

}