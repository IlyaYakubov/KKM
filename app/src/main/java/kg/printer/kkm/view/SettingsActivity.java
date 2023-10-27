package kg.printer.kkm.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import kg.printer.kkm.R;
import kg.printer.kkm.controllers.UIViewController;

public class SettingsActivity extends UIViewController.BaseAdapter implements View.OnClickListener {

    private Button btn_set_org, btn_add_user, btn_goods, btn_units;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initView();
        addListener();
        init();
    }

    @Override
    public void initView() {
        btn_set_org = findViewById(R.id.btn_set_org);
        btn_add_user = findViewById(R.id.btn_add_user);
        btn_goods = findViewById(R.id.btn_goods);
        btn_units = findViewById(R.id.btn_units);
    }

    @Override
    public void addListener() {
        btn_set_org.setOnClickListener(this);
        btn_add_user.setOnClickListener(this);
        btn_goods.setOnClickListener(this);
        btn_units.setOnClickListener(this);
    }

    @Override
    public void init() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_set_org:
                turnToActivity(OrganizationActivity.class);
                break;
            case R.id.btn_add_user:
                turnToActivity(UsersActivity.class);
                break;
            case R.id.btn_goods:
                turnToActivity(ProductsActivity.class);
                break;
            case R.id.btn_units:
                turnToActivity(UnitsActivity.class);
                break;
            default:
                break;
        }
    }

}
