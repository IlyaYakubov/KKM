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

    private int listIndex;
    private boolean newItem;

    private UnitService unitService;

    private Unit unit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit);

        init();
        initView();
        addListener();

        updateView();
    }

    @Override
    public void init() {
        unitService = new UnitService(this);

        Intent intent = getIntent();
        listIndex = intent.getIntExtra("list_index", -1);
        newItem = intent.getBooleanExtra("new_item", true);

        // редактирование существующей единицы измерения
        if (newItem) {
            unit = new Unit();
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        } else {
            unit = unitService.findUnitByListIndex(listIndex);
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
    public void onClick(View view) {
        if (view.getId() == R.id.btn_ok) {
            if (etName.getText().toString().isEmpty()) {
                showToast(getString(R.string.fill_in_the_name));
            } else {
                Unit unit = new Unit(etName.getText().toString(), etFullName.getText().toString(), etCodeName.getText().toString());

                if (newItem) {
                    unitService.createUnit(unit);
                } else {
                    unitService.updateUnit(unit, listIndex);
                }

                hideKeyboard(view);
                finish();
            }
        }
    }

    private void updateView() {
        etName.setText(unit.getName());
        etFullName.setText(unit.getFullName());
        etCodeName.setText(unit.getCode());
    }

    private void hideKeyboard(View view) {
        View viewLayout = this.getCurrentFocus();
        if (viewLayout != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) UnitActivity.this.getSystemService(UIViewController.BaseAdapter.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}
