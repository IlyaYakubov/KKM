package kg.printer.kkm.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import kg.printer.kkm.R;
import kg.printer.kkm.controllers.UIViewController;
import kg.printer.kkm.services.SaleService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CashActivity extends UIViewController.BaseAdapter implements View.OnClickListener {

    private TextView tvResult, tvChange;
    private EditText etContributed;
    private Button btnOk;

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
        ArrayList<String> result = Objects.requireNonNull(intent.getExtras()).getStringArrayList("result");
        assert result != null;
        for (String delta : result) {
            delta = delta.replace(',','.');
            sum = sum + Double.parseDouble(delta);
        }
    }

    @Override
    public void initView() {
        tvResult = findViewById(R.id.tv_result);
        tvChange = findViewById(R.id.tv_change);
        etContributed = findViewById(R.id.et_contributed);
        btnOk = findViewById(R.id.btn_ok);
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
    }

    private void updateView() {
        tvResult.setText(String.valueOf(sum));
        etContributed.setText(String.valueOf(sum));
        tvChange.setText("0");
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_ok) {
            //noinspection EqualsBetweenInconvertibleTypes
            if (etContributed.getText().equals("")) {
                return;
            }

            hideKeyboard(v);
            finish();
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
