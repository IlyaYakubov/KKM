package kg.printer.kkm.view;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import kg.printer.kkm.R;
import kg.printer.kkm.controllers.UIViewController;
import kg.printer.kkm.repositories.DatabaseDAO;
import kg.printer.kkm.repositories.Database;
import kg.printer.kkm.view.old.BasicPassFragment;

public class AdministratorActivity extends UIViewController.BaseAdapter implements View.OnClickListener, Database {

    private EditText et_position, et_surname, et_name, et_second_name;
    private Button btn_set_pass, btn_del_pass, btn_ok;
    private DatabaseDAO dbHelper;
    private BasicPassFragment settingPasswordDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrator);

        initView();
        addListener();
        init();
    }

    @Override
    public void initView() {
        et_position = findViewById(R.id.et_position);
        et_surname = findViewById(R.id.et_surname);
        et_name = findViewById(R.id.et_name);
        et_second_name = findViewById(R.id.et_second_name);

        btn_set_pass = findViewById(R.id.btn_set_pass);
        btn_del_pass = findViewById(R.id.btn_del_pass);
        btn_ok = findViewById(R.id.btn_ok);
    }

    @Override
    public void addListener() {
        btn_set_pass.setOnClickListener(this);
        btn_del_pass.setOnClickListener(this);
        btn_ok.setOnClickListener(this);
    }

    @Override
    public void init() {
        dbHelper = new DatabaseDAO(getApplicationContext());
        settingPasswordDialog = new BasicPassFragment();

        readData();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_set_pass:
                settingPasswordDialog.show(getFragmentManager(), null);
                break;
            case R.id.btn_del_pass:
                settingPasswordDialog.password = "";
                UIViewController.ToastAdapter.show(this, "Пароль удалён");
                break;
            case R.id.btn_ok:
                if (et_position.getText().toString().isEmpty()) {
                    UIViewController.ToastAdapter.show(this, "Заполните должность");
                } else if (et_surname.getText().toString().isEmpty()) {
                    UIViewController.ToastAdapter.show(this, "Заполните фамилию");
                } else if (et_name.getText().toString().isEmpty()) {
                    UIViewController.ToastAdapter.show(this, "Заполните имя");
                } else if (et_second_name.getText().toString().isEmpty()) {
                    UIViewController.ToastAdapter.show(this, "Заполните отчество");
                } else {
                    updateData();
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

        // select just administrator
        Cursor cursor = db.query("users", null, "is_admin = 1", null, null, null, null);

        if (cursor.moveToFirst()) {
            int passColIndex = cursor.getColumnIndex("password");
            int positionColIndex = cursor.getColumnIndex("position");
            int surnameColIndex = cursor.getColumnIndex("surname");
            int nameColIndex = cursor.getColumnIndex("name");
            int secondNameColIndex = cursor.getColumnIndex("second_name");

            settingPasswordDialog.password = cursor.getString(passColIndex); // getter - setter
            et_position.setText(cursor.getString(positionColIndex));
            et_surname.setText(cursor.getString(surnameColIndex));
            et_name.setText(cursor.getString(nameColIndex));
            et_second_name.setText(cursor.getString(secondNameColIndex));
        }

        cursor.close();
    }

    @Override
    public void updateData() {
        ContentValues cv = new ContentValues();
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String position = et_position.getText().toString();
        String surname = et_surname.getText().toString();
        String name = et_name.getText().toString();
        String secondName = et_second_name.getText().toString();

        String pass = "";
        if (settingPasswordDialog.etPassword != null) {
            pass = settingPasswordDialog.password;
        }

        cv.put("is_admin", 1);
        cv.put("position_on_list", 0);
        cv.put("password", pass);
        cv.put("position", position);
        cv.put("surname", surname);
        cv.put("name", name);
        cv.put("second_name", secondName);

        db.update("users", cv, "position_on_list = ?", new String[] { "0" });

        dbHelper.close();
    }

    @Override
    public void addData() {

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
            InputMethodManager inputMethodManager = (InputMethodManager) AdministratorActivity.this.getSystemService(UIViewController.BaseAdapter.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}
