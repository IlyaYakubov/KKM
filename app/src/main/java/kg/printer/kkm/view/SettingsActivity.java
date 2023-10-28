package kg.printer.kkm.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import kg.printer.kkm.R;
import kg.printer.kkm.controllers.UIViewController;

public class SettingsActivity extends UIViewController.BaseAdapter implements View.OnClickListener {

    private Button btn_set_org, btn_add_user, btn_products, btn_units;

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
        btn_products = findViewById(R.id.btn_products);
        btn_units = findViewById(R.id.btn_units);
    }

    @Override
    public void addListener() {
        btn_set_org.setOnClickListener(this);
        btn_add_user.setOnClickListener(this);
        btn_products.setOnClickListener(this);
        btn_units.setOnClickListener(this);
    }

    @Override
    public void init() {
        // do nothing
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_set_org:
                turnToActivity(OrganizationActivity.class);
                break;
            case R.id.btn_add_user:
                turnToActivity(UsersActivity.class);
                break;
            case R.id.btn_products:
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
