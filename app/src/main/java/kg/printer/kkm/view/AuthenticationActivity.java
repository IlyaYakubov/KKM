package kg.printer.kkm.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import kg.printer.kkm.R;
import kg.printer.kkm.controllers.UIViewController;
import kg.printer.kkm.domains.User;
import kg.printer.kkm.services.AuthenticationService;

import java.util.ArrayList;
import java.util.List;

public class AuthenticationActivity extends UIViewController.BaseAdapter implements View.OnClickListener {

    private Spinner sprLogin;
    private EditText etNumData;
    private ImageButton btnClearNum;
    private Button btnLogin, btnZero, btnOne, btnTwo, btnThree, btnFour, btnFive, btnSix, btnSeven, btnEight, btnNine;

    private ArrayList<User> users;

    private AuthenticationService authenticationService;

    private User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        init();
        initView();
        addListener();
    }

    @Override
    protected void onResume() {
        super.onResume();

        updateView();
    }

    @Override
    public void init() {
        authenticationService = new AuthenticationService(this);
        authenticationService.checkAllPermissions(this);
    }

    @Override
    public void initView() {
        sprLogin = findViewById(R.id.spr_login);
        etNumData = findViewById(R.id.et_num_data);
        btnLogin = findViewById(R.id.btn_login);
        btnClearNum = findViewById(R.id.btn_clear_num);
        btnZero = findViewById(R.id.btn_zero);
        btnOne = findViewById(R.id.btn_one);
        btnTwo = findViewById(R.id.btn_two);
        btnThree = findViewById(R.id.btn_three);
        btnFour = findViewById(R.id.btn_four);
        btnFive = findViewById(R.id.btn_five);
        btnSix = findViewById(R.id.btn_six);
        btnSeven = findViewById(R.id.btn_seven);
        btnEight = findViewById(R.id.btn_eight);
        btnNine = findViewById(R.id.btn_nine);
    }

    @Override
    public void addListener() {
        btnLogin.setOnClickListener(this);
        btnClearNum.setOnClickListener(this);
        btnZero.setOnClickListener(this);
        btnOne.setOnClickListener(this);
        btnTwo.setOnClickListener(this);
        btnThree.setOnClickListener(this);
        btnFour.setOnClickListener(this);
        btnFive.setOnClickListener(this);
        btnSix.setOnClickListener(this);
        btnSeven.setOnClickListener(this);
        btnEight.setOnClickListener(this);
        btnNine.setOnClickListener(this);
    }

    @SuppressLint({"NonConstantResourceId", "SetTextI18n"})
    @Override
    public void onClick(View view) {
        String text = etNumData.getText().toString();

        switch (view.getId()) {
            case R.id.btn_login:
                // now text is password
                user.setPassword(text);

                if (authenticationService.findUser(user)) {
                    user = authenticationService.findUserByListIndex(user.getListIndex());
                    turnToActivityWithUser(MenuActivity.class, user);
                    etNumData.setText("");
                } else {
                    UIViewController.ToastAdapter.show(this, "Неправльный пароль");
                }
                break;
            case R.id.btn_zero:
                etNumData.setText(text + "0");
                break;
            case R.id.btn_one:
                etNumData.setText(text + "1");
                break;
            case R.id.btn_two:
                etNumData.setText(text + "2");
                break;
            case R.id.btn_three:
                etNumData.setText(text + "3");
                break;
            case R.id.btn_four:
                etNumData.setText(text + "4");
                break;
            case R.id.btn_five:
                etNumData.setText(text + "5");
                break;
            case R.id.btn_six:
                etNumData.setText(text + "6");
                break;
            case R.id.btn_seven:
                etNumData.setText(text + "7");
                break;
            case R.id.btn_eight:
                etNumData.setText(text + "8");
                break;
            case R.id.btn_nine:
                etNumData.setText(text + "9");
                break;
            case R.id.btn_clear_num:
                text = text.length() <= 0 ? null : text.substring(0, text.length() - 1);
                etNumData.setText(text);
                break;
            default:
                break;
        }
    }

    private void updateView() {
        users = authenticationService.readUsers();

        List<String> names = new ArrayList<>();
        for (User element : users) {
            names.add(element.getName());
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, names);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sprLogin.setAdapter(arrayAdapter);
        sprLogin.setSelection(0);

        sprLogin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                user.setListIndex(position);
                user.setName(sprLogin.getSelectedItem().toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
    }

}
