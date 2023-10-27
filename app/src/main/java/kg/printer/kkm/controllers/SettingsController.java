package kg.printer.kkm.controllers;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import kg.printer.kkm.R;
import kg.printer.kkm.domains.Organization;

public class SettingsController extends BaseController implements View.OnClickListener {

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
                turnToActivity(Organization.class);
                break;
            case R.id.btn_add_user:
                turnToActivity(ListUsersController.class);
                break;
            case R.id.btn_goods:
                turnToActivity(ListGoodsController.class);
                break;
            case R.id.btn_units:
                turnToActivity(ListUnitsController.class);
                break;
            default:
                break;
        }
    }

}