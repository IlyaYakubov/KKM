package kg.printer.kkm.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
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
import kg.printer.kkm.domains.User;
import kg.printer.kkm.services.AuthenticationService;
import kg.printer.kkm.view.old.BasicPassFragment;

public class UserActivity extends UIViewController.BaseAdapter implements View.OnClickListener {

    private TextView tvNotBigger, tvPercent;
    private EditText etPosition, etSurname, etName, etSecondName, etInn, etPercentDiscount;
    private Switch swBackings, swDiscounts, swChangePrice, swOrders;
    private Button btnSetPass, btnDelPass, btnDelUser, btnOk;

    private int newItem; // 1 true - 0 false
    private int listIndex;

    public BasicPassFragment settingPasswordDialog;

    private AuthenticationService authenticationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        init();
        initView();
        addListener();

        updateView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setVisibleValueOfPercent();
    }

    @Override
    public void init() {
        authenticationService = new AuthenticationService(this);
        settingPasswordDialog = new BasicPassFragment();

        Intent intent = getIntent();
        listIndex = intent.getIntExtra("listIndex", -1);
        newItem = intent.getIntExtra("newItem", 1);
    }

    @Override
    public void initView() {
        etPosition = findViewById(R.id.et_position);
        etSurname = findViewById(R.id.et_surname);
        etName = findViewById(R.id.et_name);
        etSecondName = findViewById(R.id.et_second_name);
        etInn = findViewById(R.id.et_inn);
        tvNotBigger = findViewById(R.id.tv_not_bigger);
        etPercentDiscount = findViewById(R.id.et_percent_discount);
        tvPercent = findViewById(R.id.tv_percent);
        swBackings = findViewById(R.id.sw_backings);
        swDiscounts = findViewById(R.id.sw_discounts);
        swChangePrice = findViewById(R.id.sw_change_price);
        swOrders = findViewById(R.id.sw_orders);
        btnSetPass = findViewById(R.id.btn_set_pass);
        btnDelPass = findViewById(R.id.btn_del_pass);

        btnDelUser = findViewById(R.id.btn_del_user);
        btnDelUser.setBackgroundColor(Color.WHITE);
        btnDelUser.setTextColor(Color.GRAY);
        btnDelUser.setClickable(false);

        btnOk = findViewById(R.id.btn_ok);
    }

    @Override
    public void addListener() {
        swDiscounts.setOnClickListener(this);
        btnSetPass.setOnClickListener(this);
        btnDelPass.setOnClickListener(this);
        btnDelUser.setOnClickListener(this);
        btnOk.setOnClickListener(this);
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
                /*authenticationService.deleteUser(listIndex);
                UIViewController.ToastAdapter.show(this, "Пользователь удалён");
                hideKeyboard(view);
                finish();*/
                break;
            case R.id.btn_ok:
                if (etPosition.getText().toString().isEmpty()) {
                    UIViewController.ToastAdapter.show(this, "Заполните должность");
                } else if (etName.getText().toString().isEmpty()) {
                    UIViewController.ToastAdapter.show(this, "Заполните имя");
                } else if (etInn.getText().toString().isEmpty() || etInn.getText().toString().length() < 14) {
                    UIViewController.ToastAdapter.show(this, "ИНН должен быть 14 знаков");
                } else {
                    User user = new User();

                    user.setPassword(settingPasswordDialog.getPassword());
                    user.setPosition(etPosition.getText().toString());
                    user.setSurname(etSurname.getText().toString());
                    user.setName(etName.getText().toString());
                    user.setSecondName(etSecondName.getText().toString());
                    user.setInn(etInn.getText().toString());
                    user.setPercentOfDiscount(etPercentDiscount.getText().toString());
                    user.setBackings(swBackings.isChecked());
                    user.setDiscounts(swDiscounts.isChecked());
                    user.setChangePrice(swChangePrice.isChecked());
                    user.setOrders(swOrders.isChecked());

                    if (newItem == 1) {
                        authenticationService.createUser(user);
                    } else {
                        authenticationService.updateUser(user, listIndex);
                    }

                    hideKeyboard(view);
                    finish();
                }
                break;
            default:
                break;
        }
    }

    private void updateView() {
        // редактирование существующего пользователя
        if (newItem == 0) {
            User user = authenticationService.findUserByListIndex(listIndex);

            if (user != null) {
                etPosition.setText(user.getPosition());
                etSurname.setText(user.getSurname());
                etName.setText(user.getName());
                etSecondName.setText(user.getSecondName());
                etInn.setText(user.getInn());
                etPercentDiscount.setText(user.getPercentOfDiscount());

                if (user.isBackings()) swBackings.setChecked(true);
                if (user.isDiscounts()) swDiscounts.setChecked(true);
                if (user.isChangePrice()) swChangePrice.setChecked(true);
                if (user.isOrders()) swOrders.setChecked(true);
            }
        } else {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        }
    }

    private void setVisibleValueOfPercent() {
        if (swDiscounts.isChecked()) {
            tvNotBigger.setVisibility(View.VISIBLE);
            etPercentDiscount.setVisibility(View.VISIBLE);
            tvPercent.setVisibility(View.VISIBLE);
        } else {
            tvNotBigger.setVisibility(View.INVISIBLE);
            etPercentDiscount.setVisibility(View.INVISIBLE);
            tvPercent.setVisibility(View.INVISIBLE);
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
