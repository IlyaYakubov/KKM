package kg.printer.kkm.view;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import kg.printer.kkm.R;
import kg.printer.kkm.controllers.UIViewController;
import kg.printer.kkm.domains.Product;
import kg.printer.kkm.repositories.DatabaseDAO;

import java.util.ArrayList;

public class ProductSelectionActivity extends UIViewController.BaseAdapter {

    private ListView lvProducts;
    private ArrayList<String> list;
    private ArrayList<String> result;
    private ArrayList<Product> products = new ArrayList<>();
    private UIViewController.BoxAdapter boxAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_selection);

        initView();
        addListener();
        init();
    }

    @Override
    public void initView() {
        lvProducts = findViewById(R.id.lv_data);
    }

    @Override
    public void addListener() {
        // do nothing
    }

    @Override
    public void init() {
        Intent intent = getIntent();
        list = intent.getExtras().getStringArrayList("list");
        result = intent.getExtras().getStringArrayList("itog");

        readDataFromBaseData();

        boxAdapter = new UIViewController.BoxAdapter(this, products);
        //ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, data);
        lvProducts.setAdapter(boxAdapter);

        lvProducts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), SaleActivity.class);
                intent.putExtra("position", String.valueOf(position));
                intent.putStringArrayListExtra("list", list);
                intent.putStringArrayListExtra("itog", result);
                startActivity(intent);
                finish();
            }
        });
    }

    private void readDataFromBaseData() {
        DatabaseDAO dbHelper = new DatabaseDAO(getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all products
        Cursor cursor = db.query("products", null, null, null, null, null, null);

        products.clear();

        while (cursor.moveToNext()) {
            int positionColIndex = cursor.getColumnIndex("name");
            int coastColIndex = cursor.getColumnIndex("coast");
            int unitColIndex = cursor.getColumnIndex("unit");
            products.add(new Product(cursor.getString(positionColIndex), cursor.getString(coastColIndex), cursor.getString(unitColIndex)));
        }

        cursor.close();
    }

}
