package kg.printer.kkm.activity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import kg.printer.kkm.R;
import kg.printer.kkm.app.BaseActivity;
import kg.printer.kkm.activity.subjects.UnitActivity;
import kg.printer.kkm.utils.BaseData;
import kg.printer.kkm.utils.LocalDataBase;

import java.util.ArrayList;

public class ListUnitsActivity extends BaseActivity implements View.OnClickListener, LocalDataBase {

    private ListView lv_data;
    private Button btn_add;
    private ArrayList<String> data = new ArrayList<>();

    private BaseData dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_un_list_units);

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
        dbHelper = new BaseData(getApplicationContext());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
                turnToActivityWithPosition(UnitActivity.class, -1, 1);
                break;
            default:
                break;
        }
    }

    @Override
    public void readData() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all units
        Cursor cursor = db.query("units", null, null, null, null, null, null);

        data.clear();

        while (cursor.moveToNext()) {
            int positionColIndex = cursor.getColumnIndex("name");
            data.add(cursor.getString(positionColIndex));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, data);
        lv_data.setAdapter(adapter);

        lv_data.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                turnToActivityWithPosition(UnitActivity.class, position, 0);
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