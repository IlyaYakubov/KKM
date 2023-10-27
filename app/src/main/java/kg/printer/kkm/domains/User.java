package kg.printer.kkm.domains;

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
import android.widget.Switch;

import kg.printer.kkm.R;
import kg.printer.kkm.controllers.BaseController;
import kg.printer.kkm.repositories.BaseDataDAO;
import kg.printer.kkm.repositories.LocalDataBaseDAO;
import kg.printer.kkm.controllers.ToastController;
import kg.printer.kkm.controllers.BasicPassController;

public class User extends BaseController implements View.OnClickListener, LocalDataBaseDAO {

    private EditText et_position, et_surname, et_name, et_second_name, et_inn, et_procent_discount;
    private Switch sw_backings, sw_discounts, sw_change_cost, sw_orders;
    private Button btn_set_pass, btn_del_pass, btn_del_user, btn_ok;

    private int newElement; // 1 true - 0 false
    private int position_on_list;

    public BaseDataDAO dbHelper;

    public BasicPassController settingPasswordDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_u_user);

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
        et_inn = findViewById(R.id.et_inn);
        et_procent_discount = findViewById(R.id.et_procent_discount);

        sw_backings = findViewById(R.id.sw_backings);
        sw_discounts = findViewById(R.id.sw_discounts);
        sw_change_cost = findViewById(R.id.sw_change_cost);
        sw_orders = findViewById(R.id.sw_orders);

        btn_set_pass = findViewById(R.id.btn_set_pass);
        btn_del_pass = findViewById(R.id.btn_del_pass);
        btn_del_user = findViewById(R.id.btn_del_user);
        btn_ok = findViewById(R.id.btn_ok);
    }

    @Override
    public void addListener() {
        btn_set_pass.setOnClickListener(this);
        btn_del_pass.setOnClickListener(this);
        btn_del_user.setOnClickListener(this);
        btn_ok.setOnClickListener(this);
    }

    @Override
    public void init() {
        dbHelper = new BaseDataDAO(getApplicationContext());
        settingPasswordDialog = new BasicPassController();

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
            case R.id.btn_set_pass:
                settingPasswordDialog.show(getFragmentManager(), null);
                break;
            case R.id.btn_del_pass:
                settingPasswordDialog.password = "";
                ToastController.show(this, "Пароль удалён");
                break;
            case R.id.btn_del_user:
                deleteData();
                ToastController.show(this, "Пользователь удалён");
                hideKeyboard(view);
                finish();
                break;
            case R.id.btn_ok:
                if (et_position.getText().toString().isEmpty()) {
                    ToastController.show(this, "Заполните должность");
                } else if (et_surname.getText().toString().isEmpty()) {
                    ToastController.show(this, "Заполните фамилию");
                } else if (et_name.getText().toString().isEmpty()) {
                    ToastController.show(this, "Заполните имя");
                } else if (et_second_name.getText().toString().isEmpty()) {
                    ToastController.show(this, "Заполните отчество");
                } else if (et_inn.getText().toString().isEmpty() || et_inn.getText().toString().length() < 14) {
                    ToastController.show(this, "ИНН должен быть 14 знаков");
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

        // select user
        Cursor cursor = db.rawQuery("select * from users where position_on_list = ?"
                , new String[] { String.valueOf(position_on_list) });

        if (cursor.moveToFirst()) {
            int passColIndex = cursor.getColumnIndex("password");
            int positionColIndex = cursor.getColumnIndex("position");
            int surnameColIndex = cursor.getColumnIndex("surname");
            int nameColIndex = cursor.getColumnIndex("name");
            int secondNameColIndex = cursor.getColumnIndex("second_name");
            int innColIndex = cursor.getColumnIndex("inn");
            int procentOfDiscountColIndex = cursor.getColumnIndex("procent_of_discount");
            int isBackingsColIndex = cursor.getColumnIndex("is_backings");
            int isDiscountsColIndex = cursor.getColumnIndex("is_discounts");
            int isChangeCostColIndex = cursor.getColumnIndex("is_change_cost");
            int isOrdersColIndex = cursor.getColumnIndex("is_orders");

            settingPasswordDialog.password = cursor.getString(passColIndex); // getter / setter
            et_position.setText(cursor.getString(positionColIndex));
            et_surname.setText(cursor.getString(surnameColIndex));
            et_name.setText(cursor.getString(nameColIndex));
            et_second_name.setText(cursor.getString(secondNameColIndex));
            et_inn.setText(cursor.getString(innColIndex));
            et_procent_discount.setText(cursor.getString(procentOfDiscountColIndex));

            if (cursor.getInt(isBackingsColIndex) == 1) sw_backings.setChecked(true);
            if (cursor.getInt(isDiscountsColIndex) == 1) sw_discounts.setChecked(true);
            if (cursor.getInt(isChangeCostColIndex) == 1) sw_change_cost.setChecked(true);
            if (cursor.getInt(isOrdersColIndex) == 1) sw_orders.setChecked(true);
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

        String position = et_position.getText().toString();
        String surname = et_surname.getText().toString();
        String name = et_name.getText().toString();
        String secondName = et_second_name.getText().toString();
        String inn = et_inn.getText().toString();
        String procentOfDiscount = et_procent_discount.getText().toString();

        int isBackings = (sw_backings.isChecked())? 1 : 0;
        int isDiscounts = (sw_discounts.isChecked())? 1 : 0;
        int isChangeCost = (sw_change_cost.isChecked())? 1 : 0;
        int isOrders = (sw_orders.isChecked())? 1 : 0;

        String pass = "";
        if (settingPasswordDialog.etPassword != null) {
            pass = settingPasswordDialog.password;
        }

        cv.put("is_admin", 0);
        cv.put("password", pass);
        cv.put("position", position);
        cv.put("surname", surname);
        cv.put("name", name);
        cv.put("second_name", secondName);
        cv.put("inn", inn);
        cv.put("procent_of_discount", procentOfDiscount);
        cv.put("is_backings", isBackings);
        cv.put("is_discounts", isDiscounts);
        cv.put("is_change_cost", isChangeCost);
        cv.put("is_orders", isOrders);

        if (newElement == 1) {
            cv.put("position_on_list", lastPosition() + 1);
            db.insert("users", null, cv);
        } else {
            cv.put("position_on_list", position_on_list);
            db.update("users", cv, "position_on_list = ?", new String[] { String.valueOf(position_on_list) });
        }
    }

    @Override
    public void deleteData() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("users", "position_on_list = " + position_on_list, null);

        // select users
        Cursor cursor = db.query("users", null, "is_admin = 0", null, null, null, null);

        while (cursor.moveToNext()) {
            //update t1 set id = (select count(*) + 1 from t1 t where t.id < t1.id) where id > (select min(t1.id) from t1 left join t1 next on t1.id+1 = next.id where next.id is null)
            String sql = "update users set position_on_list = (select count(*) + 1 from users t where t.position_on_list < users.position_on_list and users.is_admin = 0) - 1";
            db.execSQL(sql);
        }
    }

    @Override
    public int lastPosition() {
        dbHelper = new BaseDataDAO(getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all users
        Cursor cursor = db.query("users", null, null, null, null, null, null);

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
            InputMethodManager inputMethodManager = (InputMethodManager) User.this.getSystemService(BaseController.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}
