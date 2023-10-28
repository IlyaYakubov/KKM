package kg.printer.kkm.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import kg.printer.kkm.R;
import kg.printer.kkm.controllers.UIViewController;
import kg.printer.kkm.domains.Administrator;
import kg.printer.kkm.services.AuthenticationService;
import kg.printer.kkm.view.old.BasicPassFragment;

public class AdministratorActivity extends UIViewController.BaseAdapter implements View.OnClickListener {

    private EditText et_position, et_surname, et_name, et_second_name;
    private Button btn_set_pass, btn_del_pass, btn_ok;

    private Administrator administrator;

    private BasicPassFragment settingPasswordDialog;

    private AuthenticationService authenticationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrator);

        initView();
        addListener();
        init();
    }

    @Override
    public void init() {
        authenticationService = new AuthenticationService(this);

        settingPasswordDialog = new BasicPassFragment();

        administrator = authenticationService.findAdministratorInDatabase(settingPasswordDialog);

        et_position.setText(administrator.getPosition());
        et_surname.setText(administrator.getSurname());
        et_name.setText(administrator.getName());
        et_second_name.setText(administrator.getSecondName());
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

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_set_pass:
                settingPasswordDialog.show(getFragmentManager(), null);
                break;
            case R.id.btn_del_pass:
                settingPasswordDialog.setPassword("");
                UIViewController.ToastAdapter.show(this, "Пароль удалён");
                break;
            case R.id.btn_ok:
                if (et_position.getText().toString().isEmpty()) {
                    UIViewController.ToastAdapter.show(this, "Заполните должность");
                } else if (et_name.getText().toString().isEmpty()) {
                    UIViewController.ToastAdapter.show(this, "Заполните имя");
                } else {
                    administrator.setPosition(et_position.getText().toString());
                    administrator.setSurname(et_surname.getText().toString());
                    administrator.setName(et_name.getText().toString());
                    administrator.setSecondName(et_second_name.getText().toString());
                    administrator.setPassword(settingPasswordDialog.getPassword());

                    authenticationService.updateAdministratorInDatabase(administrator);
                    hideKeyboard(view);
                    finish();
                }
                break;
            default:
                break;
        }
    }

    private void hideKeyboard(View view) {
        View viewLayout = this.getCurrentFocus();
        if (viewLayout != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) AdministratorActivity.this.getSystemService(UIViewController.BaseAdapter.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}
