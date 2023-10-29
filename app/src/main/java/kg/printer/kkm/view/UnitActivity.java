package kg.printer.kkm.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import kg.printer.kkm.R;
import kg.printer.kkm.controllers.UIViewController;
import kg.printer.kkm.domains.Unit;
import kg.printer.kkm.services.UnitService;

public class UnitActivity extends UIViewController.BaseAdapter implements View.OnClickListener {

    private EditText etName, etFullName, etCodeName;
    private Button btnOk;

    private int newItem; // 1 true - 0 false
    private int listIndex;

    private UnitService unitService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit);

        unitService = new UnitService(this);

        Intent intent = getIntent();
        listIndex = intent.getIntExtra("listIndex", -1);
        newItem = intent.getIntExtra("newItem", 1);

        // редактирование существующей единицы измерения
        Unit unit = null;
        if (newItem == 0) {
            unit = unitService.readUnit(listIndex);
        } else {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        }

        initView();
        addListener();
        init();

        if (unit != null) {
            etName.setText(unit.getName());
            etFullName.setText(unit.getFullName());
            etCodeName.setText(unit.getCode());
        }
    }

    @Override
    public void initView() {
        etName = findViewById(R.id.et_name);
        etFullName = findViewById(R.id.et_full_name);
        etCodeName = findViewById(R.id.et_code_name);

        btnOk = findViewById(R.id.btn_ok);
    }

    @Override
    public void addListener() {
        btnOk.setOnClickListener(this);
    }

    @Override
    public void init() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_ok:
                if (etName.getText().toString().isEmpty()) {
                    UIViewController.ToastAdapter.show(this, "Заполните наименование");
                } else {
                    String name = etName.getText().toString();
                    String fullName = etFullName.getText().toString();
                    String codeName = etCodeName.getText().toString();

                    if (newItem == 1) {
                        unitService.createUnit(name, fullName, codeName);
                    } else {
                        unitService.updateUnit(name, fullName, codeName, listIndex);
                    }

                    hideKeyboard(view);
                    finish();
                }
                break;
            default:
                break;
        }
    }

    private void hideKeyboard(View view) {
        View viewLayout = this.getCurrentFocus();
        if (viewLayout != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) UnitActivity.this.getSystemService(UIViewController.BaseAdapter.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}
