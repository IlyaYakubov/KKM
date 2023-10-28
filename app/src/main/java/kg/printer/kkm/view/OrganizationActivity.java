package kg.printer.kkm.view;

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
import kg.printer.kkm.controllers.UIViewController;
import kg.printer.kkm.repositories.DatabaseDAO;
import kg.printer.kkm.repositories.Database;

public class OrganizationActivity extends UIViewController.BaseAdapter implements View.OnClickListener, Database {

    private Spinner spr_type_of_ownership, spr_taxation;
    private EditText et_org_name, et_inn, et_magazine_name, et_address_magazine, et_telephone_magazine;
    private Button btn_ok;

    private final String[] typesOfOwnership = {"Частный предприниматель", "Юридическое лицо"};
    private final String[] taxation = {"Общая система", "Патент", "Единый налог"};

    private String typeOfOwnership = "Юридическое лицо", tax = "Общая система";

    private DatabaseDAO dbHelper;

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
        ArrayAdapter<String> adapterFormOfSobstvennosti = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, typesOfOwnership);
        adapterFormOfSobstvennosti.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spr_type_of_ownership = findViewById(R.id.spr_form_of_sobstvennosti);
        spr_type_of_ownership.setAdapter(adapterFormOfSobstvennosti);
        spr_type_of_ownership.setPrompt("Форма собственности");
        spr_type_of_ownership.setSelection(1);
        spr_type_of_ownership.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                typeOfOwnership = spr_type_of_ownership.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        ArrayAdapter<String> adapterSystemNalog = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, taxation);
        adapterSystemNalog.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spr_taxation = findViewById(R.id.spr_system_nalog);
        spr_taxation.setAdapter(adapterSystemNalog);
        spr_taxation.setPrompt("Форма собственности");
        spr_taxation.setSelection(0);
        spr_taxation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tax = spr_taxation.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        et_org_name = findViewById(R.id.et_org_name);
        et_inn = findViewById(R.id.et_inn);
        et_magazine_name = findViewById(R.id.et_magazine_name);
        et_address_magazine = findViewById(R.id.et_adress_magazine);
        et_telephone_magazine = findViewById(R.id.et_telephone_magazine);

        btn_ok = findViewById(R.id.btn_ok);
    }

    @Override
    public void addListener() {
        btn_ok.setOnClickListener(this);
    }

    @Override
    public void init() {
        dbHelper = new DatabaseDAO(getApplicationContext());
        readData();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_ok:
                if (et_org_name.getText().toString().isEmpty()) {
                    UIViewController.ToastAdapter.show(this, "Заполните название организации");
                } else if (et_magazine_name.getText().toString().isEmpty()) {
                    UIViewController.ToastAdapter.show(this, "Заполните название торговой точки");
                } else if (et_address_magazine.getText().toString().isEmpty()) {
                    UIViewController.ToastAdapter.show(this, "Заполните адрес торговой точки");
                } else if (et_telephone_magazine.getText().toString().isEmpty()) {
                    UIViewController.ToastAdapter.show(this, "Заполните контактный телефон");
                } else if (et_inn.getText().toString().isEmpty() || et_inn.getText().toString().length() < 14) {
                    UIViewController.ToastAdapter.show(this, "ИНН должен быть 14 знаков");
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
                spr_type_of_ownership.setSelection(0);
            } else spr_type_of_ownership.setSelection(1);

            if (cursor.getString(systemNalogColIndex).equals("Общая система")) {
                spr_taxation.setSelection(0);
            } else if (cursor.getString(systemNalogColIndex).equals("Патент")) {
                spr_taxation.setSelection(1);
            } else if (cursor.getString(systemNalogColIndex).equals("Единый налог")) {
                spr_taxation.setSelection(2);
            }

            et_org_name.setText(cursor.getString(nameColIndex));
            et_magazine_name.setText(cursor.getString(magazineNameColIndex));
            et_inn.setText(cursor.getString(innColIndex));
            et_address_magazine.setText(cursor.getString(adressMagazineColIndex));
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
        String adress_magazine = et_address_magazine.getText().toString();
        String telephone_magazine = et_telephone_magazine.getText().toString();
        String inn = et_inn.getText().toString();

        cv.put("form_of_sobstvennosti", typeOfOwnership);
        cv.put("system_nalog", tax);
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
            InputMethodManager inputMethodManager = (InputMethodManager) OrganizationActivity.this.getSystemService(UIViewController.BaseAdapter.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}
