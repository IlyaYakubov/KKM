package kg.printer.kkm.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import kg.printer.kkm.R;
import kg.printer.kkm.controllers.UIViewController;
import kg.printer.kkm.domains.User;
import kg.printer.kkm.services.AuthenticationService;
import kg.printer.kkm.services.SaleService;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class SaleActivity extends UIViewController.BaseAdapter implements View.OnClickListener {

    private TextView tvProduct, tvQuantity, tvPrice, tvSum;
    private EditText etNumData;
    private Button btnClearNum, btnDot;
    private Button btnZero, btnOne, btnTwo, btnThree, btnFour, btnFive, btnSix, btnSeven, btnEight, btnNine;
    private Button btnQuantity, btnPrice;
    private Button btnOk;

    private ArrayList<String> list;
    private ArrayList<String> result;

    private SaleService saleService;
    private AuthenticationService authenticationService;

    private User user;

    private int listIndex;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale);

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
        saleService = new SaleService(this);
        authenticationService = new AuthenticationService(this);

        Intent intent = getIntent();
        listIndex = Integer.parseInt(intent.getStringExtra("position"));
        list = Objects.requireNonNull(intent.getExtras()).getStringArrayList("list");
        result = Objects.requireNonNull(intent.getExtras()).getStringArrayList("result");
        user = Objects.requireNonNull(intent.getExtras()).getParcelable("user");
    }

    @Override
    public void initView() {
        tvProduct = findViewById(R.id.tv_product);
        tvQuantity = findViewById(R.id.tv_quantity);
        tvPrice = findViewById(R.id.tv_price);
        tvSum = findViewById(R.id.tv_sum);
        etNumData = findViewById(R.id.et_num_data);
        btnClearNum = findViewById(R.id.btn_clear_num);
        btnDot = findViewById(R.id.btn_dot);
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
        btnQuantity = findViewById(R.id.btn_quantity);
        btnPrice = findViewById(R.id.btn_price);
        btnOk = findViewById(R.id.btn_ok);
    }

    @Override
    public void addListener() {
        tvQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                tvSum.setText(saleService.toMultiply(tvQuantity.getText().toString(), tvPrice.getText().toString()));
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        tvPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                tvSum.setText(saleService.toMultiply(tvQuantity.getText().toString(), tvPrice.getText().toString()));
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        btnClearNum.setOnClickListener(this);
        btnDot.setOnClickListener(this);
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
        btnQuantity.setOnClickListener(this);
        btnPrice.setOnClickListener(this);
        btnOk.setOnClickListener(this);
    }

    @SuppressLint({"NonConstantResourceId", "SetTextI18n"})
    @Override
    public void onClick(View v) {
        String text = etNumData.getText().toString();

        switch (v.getId()) {
            case R.id.btn_quantity:
                if (!etNumData.getText().toString().isEmpty()) {
                    checkDot(4);
                    tvQuantity.setText(etNumData.getText().toString());
                    etNumData.setText("");
                }
                break;
            case R.id.btn_price:
                if (!etNumData.getText().toString().isEmpty()) {
                    checkDot(3);
                    tvPrice.setText(etNumData.getText().toString());
                    etNumData.setText("");
                }
                break;
            case R.id.btn_ok:
                if (tvQuantity.getText().toString().equals("")
                        || tvPrice.getText().toString().equals("")) {
                    return;
                }

                list.add(tvProduct.getText().toString()
                        + "          кол-во: " + tvQuantity.getText().toString()
                        + " сумма: " + tvSum.getText().toString() + " руб.");
                result.add(tvSum.getText().toString());

                Intent intent = new Intent(getApplicationContext(), CartActivity.class);
                intent.putExtra("list", list);
                intent.putExtra("result", result);
                intent.putExtra("sum", tvSum.getText().toString());
                intent.putExtra("user", user);
                startActivity(intent);
                finish();
                break;
            case R.id.btn_dot:
                int dotIndex = text.indexOf(".");
                if (dotIndex == -1) {
                    etNumData.setText(text + ".");
                    checkDot(4);
                }
                break;
            case R.id.btn_zero:
                etNumData.setText(text + "0");
                checkDot(4);
                break;
            case R.id.btn_one:
                etNumData.setText(text + "1");
                checkDot(4);
                break;
            case R.id.btn_two:
                etNumData.setText(text + "2");
                checkDot(4);
                break;
            case R.id.btn_three:
                etNumData.setText(text + "3");
                checkDot(4);
                break;
            case R.id.btn_four:
                etNumData.setText(text + "4");
                checkDot(4);
                break;
            case R.id.btn_five:
                etNumData.setText(text + "5");
                checkDot(4);
                break;
            case R.id.btn_six:
                etNumData.setText(text + "6");
                checkDot(4);
                break;
            case R.id.btn_seven:
                etNumData.setText(text + "7");
                checkDot(4);
                break;
            case R.id.btn_eight:
                etNumData.setText(text + "8");
                checkDot(4);
                break;
            case R.id.btn_nine:
                etNumData.setText(text + "9");
                checkDot(4);
                break;
            case R.id.btn_clear_num:
                etNumData.setText("");
                break;
            default:
                break;
        }
    }

    @SuppressWarnings("rawtypes")
    private void updateView() {
        user = authenticationService.findUserByListIndex(user.getListIndex());
        if (user.isChangePrice()) {
            btnPrice.setClickable(true);
        } else {
            btnPrice.setBackgroundColor(Color.WHITE);
            btnPrice.setTextColor(Color.GRAY);
            btnPrice.setClickable(false);
        }

        Map map = saleService.findProductByListIndex(listIndex);
        tvProduct.setText(String.valueOf(map.get("name")));
        tvPrice.setText(String.valueOf(map.get("price")));

        tvSum.setText(saleService.toMultiply(tvQuantity.getText().toString(), tvPrice.getText().toString()));
    }

    private void checkDot(int length) {
        String text = etNumData.getText().toString();

        int dotIndex = text.indexOf(".");
        int lastIndex = text.length();

        String textResult = text.length() <= 0 ? null : text.substring(0, text.length() - 1);
        if (dotIndex == 0) {
            text = textResult;
            etNumData.setText(text);
        } else if (dotIndex > 0) {
            if (lastIndex - dotIndex > length) {
                text = textResult;
                etNumData.setText(text);
            }
        }
    }

}
