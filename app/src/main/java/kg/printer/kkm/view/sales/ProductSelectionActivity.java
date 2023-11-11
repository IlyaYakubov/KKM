package kg.printer.kkm.view.sales;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import kg.printer.kkm.R;
import kg.printer.kkm.controllers.UIViewController;
import kg.printer.kkm.domains.User;
import kg.printer.kkm.services.SaleService;

import java.util.ArrayList;

public class ProductSelectionActivity extends UIViewController.BaseAdapter {

    private ListView lvProducts;

    private ArrayList<String> list;
    private ArrayList<String> result;

    private SaleService saleService;

    private User user;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_selection);

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
        saleService = new SaleService(this);

        Intent intent = getIntent();
        list = intent.getExtras().getStringArrayList("list");
        result = intent.getExtras().getStringArrayList("result");
        user = intent.getExtras().getParcelable("user");
    }

    @Override
    public void initView() {
        lvProducts = findViewById(R.id.lv_items);
    }

    @Override
    public void addListener() {
        // do nothing
    }

    private void updateView() {
        UIViewController.BoxAdapter boxAdapter = new UIViewController.BoxAdapter(this, saleService.readProducts());
        lvProducts.setAdapter(boxAdapter);

        lvProducts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                turnToSaleActivity(position, list, result, user);
                finish();
            }
        });
    }

}
