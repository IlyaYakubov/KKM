package kg.printer.kkm.controllers;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import kg.printer.kkm.R;
import kg.printer.kkm.domains.Good;
import kg.printer.kkm.repositories.BaseDataDAO;
import kg.printer.kkm.repositories.LocalDataBaseDAO;
import kg.printer.kkm.domains.Product;

import java.util.ArrayList;

public class ListGoodsController extends BaseController implements View.OnClickListener, LocalDataBaseDAO {

    private ListView lv_data;
    private Button btn_add;
    private ArrayList<Product> data = new ArrayList<>();
    private BoxController boxAdapter;

    private BaseDataDAO dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_g_list_goods);

        initView();
        addListener();
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();

        readData();
    }

    @Override
    public void initView() {
        lv_data = findViewById(R.id.lv_data);
        btn_add = findViewById(R.id.btn_add);
    }

    @Override
    public void addListener() {
        btn_add.setOnClickListener(this);
    }

    @Override
    public void init() {
        dbHelper = new BaseDataDAO(getApplicationContext());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
                turnToActivityWithPosition(Good.class, -1, 1);
                break;
            default:
                break;
        }
    }

    @Override
    public void readData() {
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

        boxAdapter = new BoxController(this, data);
        //ArrayAdapter<Product> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, data);
        lv_data.setAdapter(boxAdapter);

        lv_data.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                turnToActivityWithPosition(Good.class, position, 0);
            }
        });

        cursor.close();
    }

    @Override
    public void updateData() {

    }

    @Override
    public void addData() {

    }

    @Override
    public void deleteData() {

    }

    @Override
    public int lastPosition() {
        return 0;
    }

}