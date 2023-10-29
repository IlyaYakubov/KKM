package kg.printer.kkm.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import kg.printer.kkm.R;
import kg.printer.kkm.controllers.UIViewController;

import java.util.ArrayList;
import java.util.Objects;

public class CartActivity extends UIViewController.BaseAdapter implements View.OnClickListener {

    private ListView lvData;
    private Button btnSelectProduct, btnCash;

    private ArrayList<String> products = new ArrayList<>();
    private ArrayList<String> results = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        initView();
        addListener();
        init();
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

    @Override
    public void init() {
        Intent intent = getIntent();
        try {
            products = Objects.requireNonNull(intent.getExtras()).getStringArrayList("list");
            results = intent.getExtras().getStringArrayList("result");

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, products);
            lvData.setAdapter(adapter);

            String sum = intent.getStringExtra("sum");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_select_product:
                Intent intentGoodsList = new Intent(getApplicationContext(), ProductSelectionActivity.class);
                intentGoodsList.putStringArrayListExtra("list", products);
                intentGoodsList.putStringArrayListExtra("result", results);
                startActivity(intentGoodsList);
                finish();
                break;
            case R.id.btn_cash:
                if (products.isEmpty()) {
                    return;
                }

                Intent intentCash = new Intent(getApplicationContext(), CashActivity.class);
                intentCash.putStringArrayListExtra("result", results);
                startActivity(intentCash);
                finish();
                break;
            default:
                break;
        }
    }

}
