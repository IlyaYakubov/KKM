package kg.printer.kkm.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import kg.printer.kkm.R;
import kg.printer.kkm.controllers.UIViewController;
import kg.printer.kkm.services.AuthenticationService;
import kg.printer.kkm.view.old.BasicPassFragment;

public class UserActivity extends UIViewController.BaseAdapter implements View.OnClickListener {

    private TextView tv_not_bigger, tv_percent;
    private EditText et_position, et_surname, et_name, et_second_name, et_inn, et_percent_discount;
    private Switch sw_backings, sw_discounts, sw_change_cost, sw_orders;
    private Button btn_set_pass, btn_del_pass, btn_del_user, btn_ok;

    private int newElement; // 1 true - 0 false
    private int position_on_list;

    public BasicPassFragment settingPasswordDialog;

    private AuthenticationService authenticationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        initView();
        addListener();
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setVisibleValueOfPercent();
    }

    @Override
    public void initView() {
        et_position = findViewById(R.id.et_position);
        et_surname = findViewById(R.id.et_surname);
        et_name = findViewById(R.id.et_name);
        et_second_name = findViewById(R.id.et_second_name);
        et_inn = findViewById(R.id.et_inn);

        tv_not_bigger = findViewById(R.id.tv_not_bigger);
        et_percent_discount = findViewById(R.id.et_percent_discount);
        tv_percent = findViewById(R.id.tv_percent);

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
        sw_discounts.setOnClickListener(this);
        btn_set_pass.setOnClickListener(this);
        btn_del_pass.setOnClickListener(this);
        btn_del_user.setOnClickListener(this);
        btn_ok.setOnClickListener(this);
    }

    @Override
    public void init() {
        authenticationService = new AuthenticationService(this);
        settingPasswordDialog = new BasicPassFragment();

        Intent intent = getIntent();
        position_on_list = intent.getIntExtra("position_on_list", -1);
        newElement = intent.getIntExtra("new_element", 1);

        // редактирование существующего пользователя
        if (newElement == 0) {
            authenticationService.readUserFromDatabase(settingPasswordDialog,
                    position_on_list,
                    et_position,
                    et_surname,
                    et_name,
                    et_second_name,
                    et_inn,
                    et_percent_discount,
                    sw_backings,
                    sw_discounts,
                    sw_change_cost,
                    sw_orders);
        } else {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sw_discounts:
                setVisibleValueOfPercent();
                break;
            case R.id.btn_set_pass:
                settingPasswordDialog.show(getFragmentManager(), null);
                break;
            case R.id.btn_del_pass:
                settingPasswordDialog.setPassword("");
                UIViewController.ToastAdapter.show(this, "Пароль удалён");
                break;
            case R.id.btn_del_user:
                authenticationService.deleteUserFromDatabase(position_on_list);
                UIViewController.ToastAdapter.show(this, "Пользователь удалён");
                hideKeyboard(view);
                finish();
                break;
            case R.id.btn_ok:
                if (et_position.getText().toString().isEmpty()) {
                    UIViewController.ToastAdapter.show(this, "Заполните должность");
                } else if (et_name.getText().toString().isEmpty()) {
                    UIViewController.ToastAdapter.show(this, "Заполните имя");
                } else if (et_inn.getText().toString().isEmpty() || et_inn.getText().toString().length() < 14) {
                    UIViewController.ToastAdapter.show(this, "ИНН должен быть 14 знаков");
                } else {
                    authenticationService.createOrUpdateUserData(settingPasswordDialog,
                            authenticationService.lastUserIdInDatabase(),
                            et_position,
                            et_surname,
                            et_name,
                            et_second_name,
                            et_inn,
                            et_percent_discount,
                            sw_backings,
                            sw_discounts,
                            sw_change_cost,
                            sw_orders,
                            newElement);
                    hideKeyboard(view);
                    finish();
                }
                break;
            default:
                break;
        }
    }

    private void setVisibleValueOfPercent() {
        if (sw_discounts.isChecked()) {
            tv_not_bigger.setVisibility(View.VISIBLE);
            et_percent_discount.setVisibility(View.VISIBLE);
            tv_percent.setVisibility(View.VISIBLE);
        } else {
            tv_not_bigger.setVisibility(View.INVISIBLE);
            et_percent_discount.setVisibility(View.INVISIBLE);
            tv_percent.setVisibility(View.INVISIBLE);
        }
    }

    private void hideKeyboard(View view) {
        View viewLayout = this.getCurrentFocus();
        if (viewLayout != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) UserActivity.this.getSystemService(UIViewController.BaseAdapter.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}
