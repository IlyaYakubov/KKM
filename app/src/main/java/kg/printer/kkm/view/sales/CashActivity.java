package kg.printer.kkm.view.sales;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import kg.printer.kkm.R;
import kg.printer.kkm.controllers.UIViewController;
import kg.printer.kkm.services.SaleService;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class CashActivity extends UIViewController.BaseAdapter implements View.OnClickListener {

    private TextView tvResult, tvChange;
    private EditText etContributed, etNumData;
    private ImageButton btnClearNum;
    private Button btnOk, btnEnter, btnZero, btnOne, btnTwo, btnThree, btnFour, btnFive, btnSix, btnSeven, btnEight, btnNine;

    private double sum = 0;

    private SaleService saleService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash);

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

        Intent intent = getIntent();
        ArrayList<String> amountList = Objects.requireNonNull(intent.getExtras()).getStringArrayList("amount_list");
        assert amountList != null;
        for (String delta : amountList) {
            delta = delta.replace(',','.');
            sum = sum + Double.parseDouble(delta);
        }
    }

    @Override
    public void initView() {
        tvResult = findViewById(R.id.tv_result);
        tvChange = findViewById(R.id.tv_change);
        etContributed = findViewById(R.id.et_contributed);
        etNumData = findViewById(R.id.et_num_data);
        btnOk = findViewById(R.id.btn_ok);
        btnEnter = findViewById(R.id.btn_enter);
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
        etContributed.addTextChangedListener(new TextWatcher() {
            @SuppressWarnings("rawtypes")
            @Override
            public void afterTextChanged(Editable s) {
                if (!etContributed.getText().toString().equals("")) {
                    Map result = saleService.toCount(etContributed.getText().toString(), sum);

                    tvResult.setText(String.valueOf(result.get("contributed")));
                    tvChange.setText(String.valueOf(result.get("change")));
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        btnOk.setOnClickListener(this);
        btnEnter.setOnClickListener(this);
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

    private void updateView() {
        tvResult.setText(String.valueOf(sum));
        etContributed.setText(String.valueOf(sum));
        tvChange.setText("0");
    }

    @SuppressLint({"NonConstantResourceId", "SetTextI18n"})
    @Override
    public void onClick(View view) {
        String text = etNumData.getText().toString();

        switch (view.getId()) {
            case R.id.btn_ok:
                //noinspection EqualsBetweenInconvertibleTypes
                if (etContributed.getText().equals("")) {
                    return;
                }

                hideKeyboard(view);
                finish();
                break;
            case R.id.btn_enter:
                if (text.isEmpty()) {
                    return;
                }

                etContributed.setText(text);
                etNumData.setText("");
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

    private void hideKeyboard(View view) {
        View viewLayout = this.getCurrentFocus();
        if (viewLayout != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) CashActivity.this.getSystemService(UIViewController.BaseAdapter.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}
