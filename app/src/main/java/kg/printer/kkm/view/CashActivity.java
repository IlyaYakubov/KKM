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

import java.util.ArrayList;
import java.util.Objects;

public class CashActivity extends UIViewController.BaseAdapter implements View.OnClickListener {

    private double sum = 0;

    private TextView tvResult, tvChange;
    private EditText etContributed;
    private Button btnOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash);

        initView();
        addListener();
        init();
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

        btnOk.setOnClickListener(this);

    }

    @Override
    public void init() {
        Intent intent = getIntent();
        ArrayList<String> result = Objects.requireNonNull(intent.getExtras()).getStringArrayList("result");
        assert result != null;
        for (String sum : result) {
            sum = sum.replace(',','.');
            this.sum = this.sum + Double.parseDouble(sum);
        }

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

    private void toCount() {
        if (!etContributed.getText().toString().equals("")) {
            double contributed = Double.parseDouble(etContributed.getText().toString());
            double surrender = contributed - sum;

            tvResult.setText(String.valueOf(contributed));
            tvChange.setText(String.valueOf(surrender));
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
