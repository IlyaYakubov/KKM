package kg.printer.kkm.activity.subjects;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
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

public class OrganizationActivity extends BaseActivity implements View.OnClickListener, LocalDataBase {

    private Spinner spr_form_of_sobstvennosti, spr_system_nalog;
    private EditText et_org_name, et_inn, et_magazine_name, et_adress_magazine, et_telephone_magazine;
    private Button btn_ok;

    private String[] formOfSobstvennosti = {"Частный предприниматель", "Юридическое лицо"};
    private String[] systemNalog = {"Общая система", "Патент", "Единый налог"};

    private String sobstvennost = "Юридическое лицо", nalog = "Общая система";

    private BaseData dbHelper;

    private boolean newOrg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organization);

        initView();
        addListener();
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();

        newOrg = true;
        if (et_inn.getText().toString().length() > 0) {
            newOrg = false;
        }
    }

    @Override
    public void initView() {
        ArrayAdapter<String> adapterFormOfSobstvennosti = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, formOfSobstvennosti);
        adapterFormOfSobstvennosti.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spr_form_of_sobstvennosti = findViewById(R.id.spr_form_of_sobstvennosti);
        spr_form_of_sobstvennosti.setAdapter(adapterFormOfSobstvennosti);
        spr_form_of_sobstvennosti.setPrompt("Форма собственности");
        spr_form_of_sobstvennosti.setSelection(1);
        spr_form_of_sobstvennosti.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sobstvennost = spr_form_of_sobstvennosti.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        ArrayAdapter<String> adapterSystemNalog = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, systemNalog);
        adapterSystemNalog.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spr_system_nalog = findViewById(R.id.spr_system_nalog);
        spr_system_nalog.setAdapter(adapterSystemNalog);
        spr_system_nalog.setPrompt("Форма собственности");
        spr_system_nalog.setSelection(0);
        spr_system_nalog.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                nalog = spr_system_nalog.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        et_org_name = findViewById(R.id.et_org_name);
        et_inn = findViewById(R.id.et_inn);
        et_magazine_name = findViewById(R.id.et_magazine_name);
        et_adress_magazine = findViewById(R.id.et_adress_magazine);
        et_telephone_magazine = findViewById(R.id.et_telephone_magazine);

        btn_ok = findViewById(R.id.btn_ok);
    }

    @Override
    public void addListener() {
        btn_ok.setOnClickListener(this);
    }

    @Override
    public void init() {
        dbHelper = new BaseData(getApplicationContext());
        readData();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_ok:
                if (et_org_name.getText().toString().isEmpty()) {
                    ToastUtil.show(this, "Заполните название организации");
                } else if (et_magazine_name.getText().toString().isEmpty()) {
                    ToastUtil.show(this, "Заполните название торговой точки");
                } else if (et_adress_magazine.getText().toString().isEmpty()) {
                    ToastUtil.show(this, "Заполните адрес торговой точки");
                } else if (et_telephone_magazine.getText().toString().isEmpty()) {
                    ToastUtil.show(this, "Заполните контактный телефон");
                } else if (et_inn.getText().toString().isEmpty() || et_inn.getText().toString().length() < 14) {
                    ToastUtil.show(this, "ИНН должен быть 14 знаков");
                } else {
                    addData();
                    hideKeyboard(view);
                    finish();
                }
            default:
                break;
        }
    }

    @Override
    public void readData() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select organization
        Cursor cursor = db.rawQuery("select * from organizations where id = 1", null);

        if (cursor.moveToFirst()) {
            int formOfSobstvennostiColIndex = cursor.getColumnIndex("form_of_sobstvennosti");
            int systemNalogColIndex = cursor.getColumnIndex("system_nalog");
            int nameColIndex = cursor.getColumnIndex("name");
            int innColIndex = cursor.getColumnIndex("inn");
            int magazineNameColIndex = cursor.getColumnIndex("magazine_name");
            int adressMagazineColIndex = cursor.getColumnIndex("adress_magazine");
            int telephoneMagazineColIndex = cursor.getColumnIndex("telephone_magazine");

            if (cursor.getString(formOfSobstvennostiColIndex).equals("Частный предприниматель")) {
                spr_form_of_sobstvennosti.setSelection(0);
            } else spr_form_of_sobstvennosti.setSelection(1);

            if (cursor.getString(systemNalogColIndex).equals("Общая система")) {
                spr_system_nalog.setSelection(0);
            } else if (cursor.getString(systemNalogColIndex).equals("Патент")) {
                spr_system_nalog.setSelection(1);
            } else if (cursor.getString(systemNalogColIndex).equals("Единый налог")) {
                spr_system_nalog.setSelection(2);
            }

            et_org_name.setText(cursor.getString(nameColIndex));
            et_magazine_name.setText(cursor.getString(magazineNameColIndex));
            et_inn.setText(cursor.getString(innColIndex));
            et_adress_magazine.setText(cursor.getString(adressMagazineColIndex));
            et_telephone_magazine.setText(cursor.getString(telephoneMagazineColIndex));
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

        String org_name = et_org_name.getText().toString();
        String magazine_name = et_magazine_name.getText().toString();
        String adress_magazine = et_adress_magazine.getText().toString();
        String telephone_magazine = et_telephone_magazine.getText().toString();
        String inn = et_inn.getText().toString();

        cv.put("form_of_sobstvennosti", sobstvennost);
        cv.put("system_nalog", nalog);
        cv.put("name", org_name);
        cv.put("magazine_name", magazine_name);
        cv.put("adress_magazine", adress_magazine);
        cv.put("telephone_magazine", telephone_magazine);
        cv.put("inn", inn);

        if (newOrg) {
            db.insert("organizations", null, cv);
        } else {
            db.update("organizations", cv, "id = 1", null);
        }
    }

    @Override
    public void deleteData() {

    }

    @Override
    public int lastPosition() {
        return 0;
    }

    private void hideKeyboard(View view) {
        View viewLayout = this.getCurrentFocus();
        if (viewLayout != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) OrganizationActivity.this.getSystemService(BaseActivity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}