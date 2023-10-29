package kg.printer.kkm.view;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import kg.printer.kkm.R;
import kg.printer.kkm.controllers.UIViewController;
import kg.printer.kkm.domains.Unit;
import kg.printer.kkm.repositories.DatabaseDAO;

import java.util.ArrayList;

@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
public class UnitsActivity extends UIViewController.BaseAdapter implements View.OnClickListener {

    private ListView lvData;
    private Button btnAdd;

    private final ArrayList<Unit> units = new ArrayList<>();

    private DatabaseDAO dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_units);

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
        lvData = findViewById(R.id.lv_items);
        btnAdd = findViewById(R.id.btn_add);
    }

    @Override
    public void addListener() {
        btnAdd.setOnClickListener(this);
    }

    @Override
    public void init() {
        dbHelper = new DatabaseDAO(getApplicationContext());
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_add) {
            turnToActivityWithPosition(UnitActivity.class, -1, 1);
        }
    }

    public void readData() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all units
        Cursor cursor = db.query("units", null, null, null, null, null, null);

        units.clear();

        ArrayList<String> unitNames = new ArrayList<>();

        while (cursor.moveToNext()) {
            int positionColIndex = cursor.getColumnIndex("name");
            Unit unit = new Unit(cursor.getString(positionColIndex), "", "");
            units.add(unit);
            unitNames.add(unit.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, unitNames);
        lvData.setAdapter(adapter);

        lvData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                turnToActivityWithPosition(UnitActivity.class, position, 0);
            }
        });

        cursor.close();
    }

}
