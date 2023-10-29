package kg.printer.kkm.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import kg.printer.kkm.R;
import kg.printer.kkm.controllers.UIViewController;
import kg.printer.kkm.domains.Product;
import kg.printer.kkm.services.ProductService;

import java.util.ArrayList;

public class ProductActivity extends UIViewController.BaseAdapter implements View.OnClickListener {

    private Spinner spr_Unit;
    private EditText etName, etPrice;
    private Button btnOk;

    private int newItem; // 1 true - 0 false
    private int listIndex;

    private String unitName;

    private ArrayAdapter<String> adapterBasicUnit;
    private ArrayList<String> listUnits = new ArrayList<>();

    private ProductService productService;

    private Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        init();
        initView();
        addListener();

        updateView(product);
    }

    @Override
    public void init() {
        productService = new ProductService(this);

        Intent intent = getIntent();
        listIndex = intent.getIntExtra("listIndex", -1);
        newItem = intent.getIntExtra("newItem", 1);

        // редактирование существующей номенклатуры
        if (newItem == 0) {
            product = productService.findProductByListIndex(listIndex);
        } else {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        }

        listUnits = productService.findAllUnits();
    }

    @Override
    public void initView() {
        adapterBasicUnit = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listUnits);
        adapterBasicUnit.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spr_Unit = findViewById(R.id.spr_unit);
        spr_Unit.setAdapter(adapterBasicUnit);
        spr_Unit.setPrompt("Единица измерения");
        spr_Unit.setSelection(0);
        spr_Unit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                unitName = spr_Unit.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        etName = findViewById(R.id.et_name);
        etPrice = findViewById(R.id.et_price);
        etPrice.setFilters(new InputFilter[] {new UIViewController.DecimalDigitsInputFilter(2)});

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
                UIViewController.ToastAdapter.show(this, "Заполните наименование");
            } else {
                String name = etName.getText().toString();
                String price = etPrice.getText().toString();

                if (newItem == 1) {
                    productService.createProduct(name, unitName, price);
                } else {
                    productService.updateProduct(name, unitName, price, listIndex);
                }

                hideKeyboard(view);
                finish();
            }
        }
    }

    private void updateView(Product product) {
        if (product != null) {
            etName.setText(product.getName());
            spr_Unit.setSelection(adapterBasicUnit.getPosition(product.getUnit()));
            etPrice.setText(product.getPrice());
        }
    }

    private void hideKeyboard(View view) {
        View viewLayout = this.getCurrentFocus();
        if (viewLayout != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) ProductActivity.this.getSystemService(UIViewController.BaseAdapter.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}
