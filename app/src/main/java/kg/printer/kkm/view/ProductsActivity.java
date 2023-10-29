package kg.printer.kkm.view;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import kg.printer.kkm.R;
import kg.printer.kkm.controllers.UIViewController;
import kg.printer.kkm.domains.Product;
import kg.printer.kkm.services.ProductService;

import java.util.ArrayList;

public class ProductsActivity extends UIViewController.BaseAdapter implements View.OnClickListener {

    private ListView lvData;
    private Button btnAdd;

    private ArrayList<Product> products = new ArrayList<>();

    private ProductService productService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        productService = new ProductService(this);
        products = productService.readProducts();

        initView();
        addListener();
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();

        products = productService.readProducts();
        fillAdapter();
    }

    @Override
    public void initView() {
        lvData = findViewById(R.id.lv_items);
        btnAdd = findViewById(R.id.btn_add);
    }

    @Override
    public void addListener() {
        btnAdd.setOnClickListener(this);
    }

    @Override
    public void init() {
        fillAdapter();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_add) {
            turnToActivityWithPosition(ProductActivity.class, -1, 1);
        }
    }

    private void fillAdapter() {
        UIViewController.BoxAdapter boxAdapter = new UIViewController.BoxAdapter(this, products);
        lvData.setAdapter(boxAdapter);

        lvData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                turnToActivityWithPosition(ProductActivity.class, position, 0);
            }
        });
    }

}
