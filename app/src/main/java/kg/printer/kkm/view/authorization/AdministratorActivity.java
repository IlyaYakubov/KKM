package kg.printer.kkm.view.authorization;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import kg.printer.kkm.R;
import kg.printer.kkm.controllers.UIViewController;
import kg.printer.kkm.domains.Administrator;
import kg.printer.kkm.services.AuthenticationService;
import kg.printer.kkm.view.old.BasicPassFragment;

public class AdministratorActivity extends UIViewController.BaseAdapter implements View.OnClickListener {

    private EditText etPosition, etSurname, etName, etSecondName;
    private Button btnSetPass, btnDelPass, btnOk;

    private BasicPassFragment settingPasswordDialog;

    private AuthenticationService authenticationService;

    private Administrator administrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrator);

        init();
        initView();
        addListener();
    }

    @Override
    protected void onResume() {
        super.onResume();

        updateView();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    @Override
    public void init() {
        authenticationService = new AuthenticationService(this);

        settingPasswordDialog = new BasicPassFragment();

        administrator = authenticationService.readAdministrator();
        settingPasswordDialog.setPassword(administrator.getPassword());
    }

    @Override
    public void initView() {
        etPosition = findViewById(R.id.et_position);
        etSurname = findViewById(R.id.et_surname);
        etName = findViewById(R.id.et_name);
        etSecondName = findViewById(R.id.et_second_name);
        btnSetPass = findViewById(R.id.btn_set_pass);
        btnDelPass = findViewById(R.id.btn_del_pass);
        btnOk = findViewById(R.id.btn_ok);
    }

    @Override
    public void addListener() {
        btnSetPass.setOnClickListener(this);
        btnDelPass.setOnClickListener(this);
        btnOk.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_set_pass:
                settingPasswordDialog.show(getFragmentManager(), null);
                break;
            case R.id.btn_del_pass:
                settingPasswordDialog.setPassword("");
                administrator.setPassword("");
                UIViewController.ToastAdapter.show(this, "Пароль удалён");
                break;
            case R.id.btn_ok:
                if (etPosition.getText().toString().isEmpty()) {
                    UIViewController.ToastAdapter.show(this, "Заполните должность");
                } else if (etName.getText().toString().isEmpty()) {
                    UIViewController.ToastAdapter.show(this, "Заполните имя");
                } else {
                    administrator.setPosition(etPosition.getText().toString());
                    administrator.setSurname(etSurname.getText().toString());
                    administrator.setName(etName.getText().toString());
                    administrator.setSecondName(etSecondName.getText().toString());
                    administrator.setPassword(settingPasswordDialog.getPassword());

                    authenticationService.updateAdministrator(administrator);
                    hideKeyboard(view);
                    finish();
                }
                break;
            default:
                break;
        }
    }

    private void updateView() {
        etPosition.setText(administrator.getPosition());
        etSurname.setText(administrator.getSurname());
        etName.setText(administrator.getName());
        etSecondName.setText(administrator.getSecondName());
    }

    private void hideKeyboard(View view) {
        View viewLayout = this.getCurrentFocus();
        if (viewLayout != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) AdministratorActivity.this.getSystemService(UIViewController.BaseAdapter.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}
