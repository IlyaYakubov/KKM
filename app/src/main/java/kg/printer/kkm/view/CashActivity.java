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

    private TextView tv_result, tv_change;
    private EditText et_contributed;
    private Button btn_ok;

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
        tv_result = findViewById(R.id.tv_itog);
        tv_change = findViewById(R.id.tv_surrender);

        et_contributed = findViewById(R.id.et_contributed);

        btn_ok = findViewById(R.id.btn_ok);
    }

    @Override
    public void addListener() {

        et_contributed.addTextChangedListener(new TextWatcher() {
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

        btn_ok.setOnClickListener(this);

    }

    @Override
    public void init() {
        Intent intent = getIntent();
        ArrayList<String> result = Objects.requireNonNull(intent.getExtras()).getStringArrayList("itog");
        assert result != null;
        for (String sum : result) {
            sum = sum.replace(',','.');
            this.sum = this.sum + Double.parseDouble(sum);
        }

        tv_result.setText(String.valueOf(sum));
        et_contributed.setText(String.valueOf(sum));
        tv_change.setText("0");
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_ok) {
            //noinspection EqualsBetweenInconvertibleTypes
            if (et_contributed.getText().equals("")) {
                return;
            }

            hideKeyboard(v);
            finish();
        }
    }

    private void toCount() {
        if (!et_contributed.getText().toString().equals("")) {
            double contributed = Double.parseDouble(et_contributed.getText().toString());
            double surrender = contributed - sum;

            tv_result.setText(String.valueOf(contributed));
            tv_change.setText(String.valueOf(surrender));
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
