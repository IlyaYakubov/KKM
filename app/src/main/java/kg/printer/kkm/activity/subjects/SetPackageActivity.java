package kg.printer.kkm.activity.subjects;

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
import kg.printer.kkm.app.BaseActivity;
import kg.printer.kkm.utils.BaseData;
import kg.printer.kkm.utils.LocalDataBase;
import kg.printer.kkm.utils.ToastUtil;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SetPackageActivity extends BaseActivity implements View.OnClickListener, LocalDataBase {

    private Spinner spr_goods, spr_basic_unit;
    private EditText et_name, et_set_of_packages, et_coefficient;
    private Button btn_ok;

    private int newElement; // 1 true - 0 false
    private int position_on_list;

    private BaseData dbHelper;

    private ArrayAdapter<String> adapterGoods, adapterBasicUnit;
    private ArrayList<String> listGoods = new ArrayList<>();
    private ArrayList<String> listUnits = new ArrayList<>();

    private String good, basicUnit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pa_package);

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
        spr_goods.setSelection(0);
        spr_goods.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                good = spr_goods.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        adapterBasicUnit = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listUnits);
        adapterBasicUnit.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spr_basic_unit = findViewById(R.id.spr_basic_unit);
        spr_basic_unit.setAdapter(adapterBasicUnit);
        spr_basic_unit.setPrompt("Базовая единица измерения");
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
        et_set_of_packages = findViewById(R.id.et_set_of_packages);
        et_coefficient = findViewById(R.id.et_coefficient);
        et_coefficient.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(6)});

        btn_ok = findViewById(R.id.btn_ok);
    }

    @Override
    public void addListener() {
        btn_ok.setOnClickListener(this);
    }

    @Override
    public void init() {
        dbHelper = new BaseData(getApplicationContext());

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
                    ToastUtil.show(this, "Заполните наименование упаковки");
                } else if (et_coefficient.getText().toString().isEmpty()) {
                    ToastUtil.show(this, "Заполните коэффициент");
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

        // select package
        Cursor cursor = db.rawQuery("select * from packages where position_on_list = ?"
                , new String[] { String.valueOf(position_on_list) });

        if (cursor.moveToFirst()) {
            int goodColIndex = cursor.getColumnIndex("good");
            int basicUnitColIndex = cursor.getColumnIndex("unit");

            int nameColIndex = cursor.getColumnIndex("name");
            int setOfPackagesColIndex = cursor.getColumnIndex("set_of_packages");
            int coefficientColIndex = cursor.getColumnIndex("coefficient");

            spr_goods.setSelection(adapterGoods.getPosition(cursor.getString(goodColIndex)));
            spr_basic_unit.setSelection(adapterBasicUnit.getPosition(cursor.getString(basicUnitColIndex)));

            et_name.setText(cursor.getString(nameColIndex));
            et_set_of_packages.setText(cursor.getString(setOfPackagesColIndex));
            et_coefficient.setText(cursor.getString(coefficientColIndex));
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
        String setOfPackages = et_set_of_packages.getText().toString();
        String coefficient = et_coefficient.getText().toString();

        cv.put("good", good);
        cv.put("unit", basicUnit);

        cv.put("name", name);
        cv.put("set_of_packages", setOfPackages);
        cv.put("coefficient", coefficient);

        if (newElement == 1) {
            cv.put("position_on_list", lastPosition() + 1);
            db.insert("packages", null, cv);
        } else {
            cv.put("position_on_list", position_on_list);
            db.update("packages", cv, "position_on_list = ?", new String[] { String.valueOf(position_on_list) });
        }
    }

    @Override
    public void deleteData() {

    }

    @Override
    public int lastPosition() {
        dbHelper = new BaseData(getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all packages
        Cursor cursor = db.query("packages", null, null, null, null, null, null);

        int lastPosition = -1;
        while (cursor.moveToNext()) {
            lastPosition++;
        }

        cursor.close();

        return lastPosition;
    }

    private void readGoodsFromBaseData() {
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

    private void readUnitsFromBaseData() {
        dbHelper = new BaseData(getApplicationContext());
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
            InputMethodManager inputMethodManager = (InputMethodManager) SetPackageActivity.this.getSystemService(BaseActivity.INPUT_METHOD_SERVICE);
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

}