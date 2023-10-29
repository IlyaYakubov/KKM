package kg.printer.kkm.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import kg.printer.kkm.R;
import kg.printer.kkm.controllers.UIViewController;

public class SettingsActivity extends UIViewController.BaseAdapter implements View.OnClickListener {

    private Button btnSetOrg, btnAddUser, btnProducts, btnUnits;

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
        btnSetOrg = findViewById(R.id.btn_set_org);
        btnAddUser = findViewById(R.id.btn_add_user);
        btnProducts = findViewById(R.id.btn_products);
        btnUnits = findViewById(R.id.btn_units);
    }

    @Override
    public void addListener() {
        btnSetOrg.setOnClickListener(this);
        btnAddUser.setOnClickListener(this);
        btnProducts.setOnClickListener(this);
        btnUnits.setOnClickListener(this);
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
