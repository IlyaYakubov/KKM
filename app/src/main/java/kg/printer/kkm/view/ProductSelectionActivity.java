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
import kg.printer.kkm.repositories.DatabaseDAO;

import java.util.ArrayList;

public class ProductSelectionActivity extends UIViewController.BaseAdapter {

    private ListView lvData;
    private ArrayList<String> list;
    private ArrayList<String> itog;
    private ArrayList<ProductActivity.Product> data = new ArrayList<>();
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
        lvData = findViewById(R.id.lv_data);
    }

    @Override
    public void addListener() {

    }

    @Override
    public void init() {
        Intent intent = getIntent();
        list = intent.getExtras().getStringArrayList("list");
        itog = intent.getExtras().getStringArrayList("itog");

        readDataFromBaseData();

        boxAdapter = new UIViewController.BoxAdapter(this, data);
        //ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, data);
        lvData.setAdapter(boxAdapter);

        lvData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), SaleActivity.class);
                intent.putExtra("position", String.valueOf(position));
                intent.putStringArrayListExtra("list", list);
                intent.putStringArrayListExtra("itog", itog);
                startActivity(intent);
                finish();
            }
        });
    }

    private void readDataFromBaseData() {
        DatabaseDAO dbHelper = new DatabaseDAO(getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all goods
        Cursor cursor = db.query("goods", null, null, null, null, null, null);

        data.clear();

        while (cursor.moveToNext()) {
            int positionColIndex = cursor.getColumnIndex("name");
            int coastColIndex = cursor.getColumnIndex("coast");
            int unitColIndex = cursor.getColumnIndex("basic_unit");
            data.add(new ProductActivity.Product(cursor.getString(positionColIndex), cursor.getString(coastColIndex), cursor.getString(unitColIndex)));
        }

        cursor.close();
    }

}
