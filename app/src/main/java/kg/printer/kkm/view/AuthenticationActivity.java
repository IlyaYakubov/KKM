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

    private Spinner spr_login;
    private EditText et_num_data;
    private ImageButton btn_clear_num;
    private Button btn_login, btn_zero, btn_one, btn_two, btn_three, btn_four, btn_five, btn_six, btn_seven, btn_eight, btn_nine;

    private AuthenticationService authenticationService;

    private ArrayList<User> listUsers;
    private User user = new User();

    public void setListUsers(ArrayList<User> listUsers) {
        this.listUsers = listUsers;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        initView();
        addListener();
        init();
    }

    @Override
    public void init() {
        authenticationService = new AuthenticationService(this);
        authenticationService.getAllUserFromDatabase();

        spinnerOfUsers();
    }

    @Override
    public void initView() {
        et_num_data = findViewById(R.id.et_num_data);
        btn_login = findViewById(R.id.btn_login);
        btn_clear_num = findViewById(R.id.btn_clear_num);
        btn_zero = findViewById(R.id.btn_zero);
        btn_one = findViewById(R.id.btn_one);
        btn_two = findViewById(R.id.btn_two);
        btn_three = findViewById(R.id.btn_three);
        btn_four = findViewById(R.id.btn_four);
        btn_five = findViewById(R.id.btn_five);
        btn_six = findViewById(R.id.btn_six);
        btn_seven = findViewById(R.id.btn_seven);
        btn_eight = findViewById(R.id.btn_eight);
        btn_nine = findViewById(R.id.btn_nine);
    }

    @Override
    public void addListener() {
        btn_login.setOnClickListener(this);
        btn_clear_num.setOnClickListener(this);
        btn_zero.setOnClickListener(this);
        btn_one.setOnClickListener(this);
        btn_two.setOnClickListener(this);
        btn_three.setOnClickListener(this);
        btn_four.setOnClickListener(this);
        btn_five.setOnClickListener(this);
        btn_six.setOnClickListener(this);
        btn_seven.setOnClickListener(this);
        btn_eight.setOnClickListener(this);
        btn_nine.setOnClickListener(this);
    }

    @SuppressLint({"NonConstantResourceId", "SetTextI18n"})
    @Override
    public void onClick(View view) {
        String text = et_num_data.getText().toString();

        switch (view.getId()) {
            case R.id.btn_login:
                // now text is password
                user.setPassword(text);

                if (authenticationService.userExistsInDatabase(user)) {
                    user = authenticationService.findUserInDatabase(user.getName(), user.getPassword());
                    turnToActivityWithUser(MenuActivity.class, user);
                    et_num_data.setText("");
                } else {
                    UIViewController.ToastAdapter.show(this, "Неправльный пароль");
                }
                break;
            case R.id.btn_zero:
                et_num_data.setText(text + "0");
                break;
            case R.id.btn_one:
                et_num_data.setText(text + "1");
                break;
            case R.id.btn_two:
                et_num_data.setText(text + "2");
                break;
            case R.id.btn_three:
                et_num_data.setText(text + "3");
                break;
            case R.id.btn_four:
                et_num_data.setText(text + "4");
                break;
            case R.id.btn_five:
                et_num_data.setText(text + "5");
                break;
            case R.id.btn_six:
                et_num_data.setText(text + "6");
                break;
            case R.id.btn_seven:
                et_num_data.setText(text + "7");
                break;
            case R.id.btn_eight:
                et_num_data.setText(text + "8");
                break;
            case R.id.btn_nine:
                et_num_data.setText(text + "9");
                break;
            case R.id.btn_clear_num:
                text = text.length() <= 0 ? null : text.substring(0, text.length() - 1);
                et_num_data.setText(text);
                break;
            default:
                break;
        }
    }

    private void spinnerOfUsers() {
        List<String> names = new ArrayList<>();
        for (User element : listUsers) {
            names.add(element.getName());
        }
        ArrayAdapter<String> userNames = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, names);
        userNames.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spr_login = findViewById(R.id.spr_login);
        spr_login.setAdapter(userNames);
        spr_login.setSelection(0);

        spr_login.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                user.setName(spr_login.getSelectedItem().toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
    }

}
