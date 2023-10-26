package kg.printer.kkm.activity.retial;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import kg.printer.kkm.R;
import kg.printer.kkm.activity.retial.SelectionForSaleActivity;
import kg.printer.kkm.app.BaseActivity;
import kg.printer.kkm.activity.money.CashActivity;

import java.util.ArrayList;

public class OperationsActivity extends BaseActivity implements View.OnClickListener {

    private ListView lvData;
    private Button btn_choose_good, btn_cash;

    private ArrayList<String> data = new ArrayList<>();
    private ArrayList<String> itog = new ArrayList<>();
    private String summa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operations);

        initView();
        addListener();
        init();
    }

    @Override
    public void initView() {
        lvData = findViewById(R.id.lv_data);
        btn_choose_good = findViewById(R.id.btn_choose_good);
        btn_cash = findViewById(R.id.btn_cash);
    }

    @Override
    public void addListener() {
        btn_choose_good.setOnClickListener(this);
        btn_cash.setOnClickListener(this);
    }

    @Override
    public void init() {
        Intent intent = getIntent();
        try {
            data = intent.getExtras().getStringArrayList("list");
            itog = intent.getExtras().getStringArrayList("itog");

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, data);
            lvData.setAdapter(adapter);

            summa = intent.getStringExtra("summa");
        } catch (Exception e) {}
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_choose_good:
                Intent intentGoodsList = new Intent(getApplicationContext(), SelectionForSaleActivity.class);
                intentGoodsList.putStringArrayListExtra("list", data);
                intentGoodsList.putStringArrayListExtra("itog", itog);
                startActivity(intentGoodsList);
                finish();
                break;
            case R.id.btn_cash:
                if (data.isEmpty()) {
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