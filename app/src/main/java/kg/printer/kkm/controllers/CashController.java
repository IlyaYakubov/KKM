package kg.printer.kkm.controllers;

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

import java.util.ArrayList;

public class CashController extends BaseController implements View.OnClickListener {

    private ArrayList<String> itog = new ArrayList<>();
    private double sumDoub = 0;

    private TextView tvItog, tvSurrender;
    private EditText etContributed;
    private Button btnOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retial_cash);

        initView();
        addListener();
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void initView() {
        tvItog = findViewById(R.id.tv_itog);
        tvSurrender = findViewById(R.id.tv_surrender);

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
        itog = intent.getExtras().getStringArrayList("itog");
        for (String sum : itog) {
            sum = sum.replace(',','.');
            sumDoub = sumDoub + Double.parseDouble(sum);
        }

        tvItog.setText(String.valueOf(sumDoub));
        etContributed.setText(String.valueOf(sumDoub));
        tvSurrender.setText("0");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok:
                if (etContributed.getText().equals("")) return;

                hideKeyboard(v);
                finish();
                break;
        }
    }

    private void toCount() {
        if (!etContributed.getText().toString().equals("")) {
            double contributed = Double.parseDouble(etContributed.getText().toString());
            double surrender = contributed - sumDoub;

            tvItog.setText(String.valueOf(contributed));
            tvSurrender.setText(String.valueOf(surrender));
        }
    }

    private void hideKeyboard(View view) {
        View viewLayout = this.getCurrentFocus();
        if (viewLayout != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) CashController.this.getSystemService(BaseController.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}
