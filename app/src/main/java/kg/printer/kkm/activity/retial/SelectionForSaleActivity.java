package kg.printer.kkm.activity.retial;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import kg.printer.kkm.R;
import kg.printer.kkm.app.BaseActivity;
import kg.printer.kkm.utils.BaseData;
import kg.printer.kkm.view.BoxAdapter;
import kg.printer.kkm.view.Product;

import java.util.ArrayList;

public class SelectionForSaleActivity extends BaseActivity {

    private ListView lvData;
    private ArrayList<String> list;
    private ArrayList<String> itog;
    private ArrayList<Product> data = new ArrayList<>();
    private BoxAdapter boxAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retial_list_goods);

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

        boxAdapter = new BoxAdapter(this, data);
        //ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, data);
        lvData.setAdapter(boxAdapter);

        lvData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), AddingGoodActivity.class);
                intent.putExtra("position", String.valueOf(position));
                intent.putStringArrayListExtra("list", list);
                intent.putStringArrayListExtra("itog", itog);
                startActivity(intent);
                finish();
            }
        });
    }

    private void readDataFromBaseData() {
        BaseData dbHelper = new BaseData(getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all goods
        Cursor cursor = db.query("goods", null, null, null, null, null, null);

        data.clear();

        while (cursor.moveToNext()) {
            int positionColIndex = cursor.getColumnIndex("name");
            int coastColIndex = cursor.getColumnIndex("coast");
            int unitColIndex = cursor.getColumnIndex("basic_unit");
            data.add(new Product(cursor.getString(positionColIndex), cursor.getString(coastColIndex), cursor.getString(unitColIndex)));
        }

        cursor.close();
    }

}