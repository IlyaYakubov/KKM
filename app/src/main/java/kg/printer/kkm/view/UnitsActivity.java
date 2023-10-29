package kg.printer.kkm.view;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import kg.printer.kkm.R;
import kg.printer.kkm.controllers.UIViewController;
import kg.printer.kkm.services.UnitService;

@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
public class UnitsActivity extends UIViewController.BaseAdapter implements View.OnClickListener {

    private ListView lvData;
    private Button btnAdd;

    private UnitService unitService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_units);

        unitService = new UnitService(this);

        initView();
        addListener();
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();

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

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_add) {
            turnToActivityWithPosition(UnitActivity.class, -1, 1);
        }
    }

    private void fillAdapter() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, unitService.readUnitNames());
        lvData.setAdapter(adapter);

        lvData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                turnToActivityWithPosition(UnitActivity.class, position, 0);
            }
        });
    }

}
