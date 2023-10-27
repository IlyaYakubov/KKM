package kg.printer.kkm.view;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
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
import kg.printer.kkm.repositories.DatabaseDAO;

import java.util.ArrayList;
import java.util.List;

public class AuthenticationActivity extends UIViewController.BaseAdapter implements View.OnClickListener {

    private static final int REQUEST_CAMERA = 0;

    private String[] NEED_PERMISSION = {
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private Spinner spr_login;
    private EditText et_num_data;
    private Button btn_incommin;
    private ImageButton btn_clear_num;
    private Button btn_zero, btn_one, btn_two, btn_three, btn_four, btn_five, btn_six, btn_seven, btn_eight, btn_nine;

    private List<String> NO_PERMISSION = new ArrayList<>();
    private ArrayAdapter<String> adapterUsers;
    private ArrayList<String> listUsers = new ArrayList<>();
    private DatabaseDAO dbHelper;

    private String position = "", surname = "", name = "", secondName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        initView();
        addListener();
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        position = "";
        surname = "";
        name = "";
        secondName = "";
    }

    @Override
    public void initView() {
        et_num_data = findViewById(R.id.et_num_data);
        btn_incommin = findViewById(R.id.btn_incommin);
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

        spinnerOfUsers();
    }

    private void spinnerOfUsers() {
        adapterUsers = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listUsers);
        adapterUsers.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spr_login = findViewById(R.id.spr_login);
        spr_login.setAdapter(adapterUsers);
        spr_login.setPrompt("Логин");
        spr_login.setSelection(0);
        spr_login.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String user = spr_login.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
    }

    @Override
    public void addListener() {
        btn_incommin.setOnClickListener(this);

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

    @Override
    public void init() {
        dbHelper = new DatabaseDAO(getApplicationContext());
        CheckAllPermission();
    }

    @Override
    public void onClick(View view) {
        String text = et_num_data.getText().toString();

        switch (view.getId()) {
            case R.id.btn_incommin:
                if (!text.isEmpty()) {
                    readDataFromBaseData();
                }

                et_num_data.setText("");
                turnToActivityWithUser(MenuActivity.class, position, surname, name, secondName);
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
                text = (text == null || text.length() <= 0) ? null : text.substring(0, text.length() - 1);
                et_num_data.setText(text);
                break;
            default:
                break;
        }
    }

    private void CheckAllPermission() {
        NO_PERMISSION.clear();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (int i = 0; i < NEED_PERMISSION.length; i++) {
                if (checkSelfPermission(NEED_PERMISSION[i]) != PackageManager.PERMISSION_GRANTED) {
                    NO_PERMISSION.add(NEED_PERMISSION[i]);
                }
            }
            if (NO_PERMISSION.size() != 0) {
                requestPermissions(NO_PERMISSION.toArray(new String[NO_PERMISSION.size()]), REQUEST_CAMERA);
            }
        }
    }

    private void readDataFromBaseData() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all users
        Cursor cursor = db.rawQuery("select * from users where password = ?"
                , new String[] { et_num_data.getText().toString() });

        if (cursor.moveToFirst()) {
            int positionColIndex = cursor.getColumnIndex("position");
            int surnameColIndex = cursor.getColumnIndex("surname");
            int nameColIndex = cursor.getColumnIndex("name");
            int secondNameColIndex = cursor.getColumnIndex("second_name");

            position = cursor.getString(positionColIndex);
            surname = cursor.getString(surnameColIndex);
            name = cursor.getString(nameColIndex);
            secondName = cursor.getString(secondNameColIndex);
        }

        cursor.close();
    }

}
