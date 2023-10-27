package kg.printer.kkm.services;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import kg.printer.kkm.R;
import kg.printer.kkm.controllers.BaseController;
import kg.printer.kkm.controllers.OperationsController;
import kg.printer.kkm.repositories.BaseDataDAO;

import java.util.ArrayList;

public class GoodService extends BaseController implements View.OnClickListener {

    private int positionOnList;

    private TextView tvGood, tvQuantity, tvCoast, tvSumma;
    private EditText et_num_data;
    private Button btn_clear_num, btn_dot;
    private Button btn_zero, btn_one, btn_two, btn_three, btn_four, btn_five, btn_six, btn_seven, btn_eight, btn_nine;
    private Button btn_quantity, btn_coast;
    private Button btnOk;

    private ArrayList<String> list;
    private ArrayList<String> itog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retial_adding_good);

        initView();
        addListener();
        init();
    }

    @Override
    public void initView() {
        tvGood = findViewById(R.id.tv_good);
        tvQuantity = findViewById(R.id.tv_quantity);
        tvCoast = findViewById(R.id.tv_coast);
        tvSumma = findViewById(R.id.tv_summa);

        et_num_data = findViewById(R.id.et_num_data);

        btn_clear_num = findViewById(R.id.btn_clear_num);
        btn_dot = findViewById(R.id.btn_dot);

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

        btn_quantity = findViewById(R.id.btn_quantity);
        btn_coast = findViewById(R.id.btn_coast);

        btnOk = findViewById(R.id.btn_ok);
    }

    @Override
    public void addListener() {

        tvQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                toCount();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        tvCoast.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                toCount();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        btn_clear_num.setOnClickListener(this);
        btn_dot.setOnClickListener(this);

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

        btn_quantity.setOnClickListener(this);
        btn_coast.setOnClickListener(this);

        btnOk.setOnClickListener(this);

    }

    @Override
    public void init() {
        Intent intent = getIntent();
        positionOnList = Integer.parseInt(intent.getStringExtra("position"));
        list = intent.getExtras().getStringArrayList("list");
        itog = intent.getExtras().getStringArrayList("itog");

        readDataFromBaseData();

        toCount();
    }

    @Override
    public void onClick(View v) {
        String text = et_num_data.getText().toString();

        switch (v.getId()) {
            case R.id.btn_quantity:
                if (!et_num_data.getText().toString().isEmpty()) {
                    checkDot(4);
                    tvQuantity.setText(et_num_data.getText().toString());
                    et_num_data.setText("");
                }
                break;
            case R.id.btn_coast:
                if (!et_num_data.getText().toString().isEmpty()) {
                    checkDot(3);
                    tvCoast.setText(et_num_data.getText().toString());
                    et_num_data.setText("");
                }
                break;
            case R.id.btn_ok:
                if (tvQuantity.getText().equals("")) return;

                list.add(tvGood.getText().toString());
                itog.add(tvSumma.getText().toString());

                Intent intent = new Intent(getApplicationContext(), OperationsController.class);
                intent.putExtra("list", list);
                intent.putExtra("itog", itog);
                intent.putExtra("summa", tvSumma.getText().toString());
                startActivity(intent);
                finish();
                break;
            case R.id.btn_dot:
                int dotIndex = text.indexOf(".");
                if (dotIndex == -1) {
                    et_num_data.setText(text + ".");
                    checkDot(4);
                }
                break;
            case R.id.btn_zero:
                et_num_data.setText(text + "0");
                checkDot(4);
                break;
            case R.id.btn_one:
                et_num_data.setText(text + "1");
                checkDot(4);
                break;
            case R.id.btn_two:
                et_num_data.setText(text + "2");
                checkDot(4);
                break;
            case R.id.btn_three:
                et_num_data.setText(text + "3");
                checkDot(4);
                break;
            case R.id.btn_four:
                et_num_data.setText(text + "4");
                checkDot(4);
                break;
            case R.id.btn_five:
                et_num_data.setText(text + "5");
                checkDot(4);
                break;
            case R.id.btn_six:
                et_num_data.setText(text + "6");
                checkDot(4);
                break;
            case R.id.btn_seven:
                et_num_data.setText(text + "7");
                checkDot(4);
                break;
            case R.id.btn_eight:
                et_num_data.setText(text + "8");
                checkDot(4);
                break;
            case R.id.btn_nine:
                et_num_data.setText(text + "9");
                checkDot(4);
                break;
            case R.id.btn_clear_num:
                et_num_data.setText("");
                break;
            default:
                break;
        }
    }

    private void readDataFromBaseData() {
        BaseDataDAO dbHelper = new BaseDataDAO(getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select good
        Cursor cursor = db.rawQuery("select * from goods where position_on_list = ?"
                , new String[]{String.valueOf(positionOnList)});

        if (cursor.moveToNext()) {
            int nameColIndex = cursor.getColumnIndex("name");
            int coastColIndex = cursor.getColumnIndex("coast");
            tvGood.setText(cursor.getString(nameColIndex));
            tvCoast.setText(cursor.getString(coastColIndex));
        }

        cursor.close();
    }

    private void toCount() {
        if (!tvQuantity.getText().equals("")) {
            double quantity = Double.parseDouble(tvQuantity.getText().toString());
            double coast = Double.parseDouble(tvCoast.getText().toString());
            double sum = quantity * coast;

            String formattedDouble = String.format("%.2f", sum);
            tvSumma.setText(formattedDouble);
        }
    }

    private void checkDot(int lengh) {
        String text = et_num_data.getText().toString();

        int dotIndex = text.indexOf(".");
        int lastIndex = text.length();

        if (dotIndex == 0) {
            text = (text == null || text.length() <= 0) ? null : text.substring(0, text.length() - 1);
            et_num_data.setText(text);
        } else if (dotIndex > 0) {
            if (lastIndex - dotIndex > lengh) {
                text = (text == null || text.length() <= 0) ? null : text.substring(0, text.length() - 1);
                et_num_data.setText(text);
            }
        }
    }

}