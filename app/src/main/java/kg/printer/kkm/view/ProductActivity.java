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

    private Spinner sprUnit;
    private EditText etName, etPrice;
    private Button btnOk;

    private boolean newItem;
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

        updateView();
    }

    @Override
    public void init() {
        productService = new ProductService(this);

        Intent intent = getIntent();
        listIndex = intent.getIntExtra("list_index", -1);
        newItem = intent.getBooleanExtra("new_item", true);

        // редактирование существующей номенклатуры
        if (newItem) {
            product = new Product();
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        } else {
            product = productService.findProductByListIndex(listIndex);
        }

        listUnits = productService.findAllUnits();
    }

    @Override
    public void initView() {
        adapterBasicUnit = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listUnits);
        adapterBasicUnit.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sprUnit = findViewById(R.id.spr_unit);
        sprUnit.setAdapter(adapterBasicUnit);
        sprUnit.setPrompt("Единица измерения");
        sprUnit.setSelection(0);
        sprUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                unitName = sprUnit.getSelectedItem().toString();
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
                Product product = new Product(etName.getText().toString(), unitName, etPrice.getText().toString());

                if (newItem) {
                    productService.createProduct(product);
                } else {
                    productService.updateProduct(product, listIndex);
                }

                hideKeyboard(view);
                finish();
            }
        }
    }

    private void updateView() {
        etName.setText(product.getName());
        sprUnit.setSelection(adapterBasicUnit.getPosition(product.getUnit()));
        etPrice.setText(product.getPrice());
    }

    private void hideKeyboard(View view) {
        View viewLayout = this.getCurrentFocus();
        if (viewLayout != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) ProductActivity.this.getSystemService(UIViewController.BaseAdapter.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}
