package kg.printer.kkm.view.sales;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import kg.printer.kkm.R;
import kg.printer.kkm.controllers.UIViewController;
import kg.printer.kkm.domains.User;

import java.util.ArrayList;
import java.util.Objects;

public class CartActivity extends UIViewController.BaseAdapter implements View.OnClickListener {

    private ListView lvData;
    private Button btnSelectProduct, btnCash;

    private ArrayList<String> products = new ArrayList<>();
    private ArrayList<String> result = new ArrayList<>();

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

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
        Intent intent = getIntent();
        products = Objects.requireNonNull(intent.getExtras()).getStringArrayList("list");
        result = intent.getExtras().getStringArrayList("result");
        user = Objects.requireNonNull(intent.getExtras()).getParcelable("user");

        if (products == null || result == null) {
            products = new ArrayList<>();
            result = new ArrayList<>();
        }
    }

    @Override
    public void initView() {
        lvData = findViewById(R.id.lv_items);
        btnSelectProduct = findViewById(R.id.btn_select_product);
        btnCash = findViewById(R.id.btn_cash);
    }

    @Override
    public void addListener() {
        btnSelectProduct.setOnClickListener(this);
        btnCash.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_select_product:
                turnToProductSelectionActivity(products, result, user);
                finish();
                break;
            case R.id.btn_cash:
                if (products.isEmpty()) {
                    return;
                }

                turnToCashActivity(result);

                finish();
                break;
            default:
                break;
        }
    }

    private void updateView() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, products);
        lvData.setAdapter(adapter);
    }

}
