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

public class CartActivity extends UIViewController.BaseAdapter implements View.OnClickListener {

    private ListView lvData;
    private Button btn_select_product, btn_cash;

    private ArrayList<String> products = new ArrayList<>();
    private ArrayList<String> itog = new ArrayList<>();
    private String sum;

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
        lvData = findViewById(R.id.lv_data);
        btn_select_product = findViewById(R.id.btn_select_product);
        btn_cash = findViewById(R.id.btn_cash);
    }

    @Override
    public void addListener() {
        btn_select_product.setOnClickListener(this);
        btn_cash.setOnClickListener(this);
    }

    @Override
    public void init() {
        Intent intent = getIntent();
        try {
            products = intent.getExtras().getStringArrayList("list");
            itog = intent.getExtras().getStringArrayList("itog");

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, products);
            lvData.setAdapter(adapter);

            sum = intent.getStringExtra("summa");
        } catch (Exception e) {}
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_select_product:
                Intent intentGoodsList = new Intent(getApplicationContext(), ProductSelectionActivity.class);
                intentGoodsList.putStringArrayListExtra("list", products);
                intentGoodsList.putStringArrayListExtra("itog", itog);
                startActivity(intentGoodsList);
                finish();
                break;
            case R.id.btn_cash:
                if (products.isEmpty()) {
                    return;
                }

                Intent intentCash = new Intent(getApplicationContext(), CashActivity.class);
                intentCash.putStringArrayListExtra("itog", itog);
                startActivity(intentCash);
                finish();
                break;
            default:
                break;
        }
    }

}
