package kg.printer.kkm.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import kg.printer.kkm.activity.print.PrinterActivity;
import kg.printer.kkm.R;
import kg.printer.kkm.activity.print.TestActivity;
import kg.printer.kkm.app.BaseActivity;
import kg.printer.kkm.activity.subjects.OrganizationActivity;

public class SettingsActivity extends BaseActivity implements View.OnClickListener {

    private Button btn_set_org, btn_add_user, btn_goods, btn_units, btn_packages, btn_tests, btn_device;

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
        btn_packages = findViewById(R.id.btn_packages);
        btn_tests = findViewById(R.id.btn_tests);
        btn_device = findViewById(R.id.btn_device);
    }

    @Override
    public void addListener() {
        btn_set_org.setOnClickListener(this);
        btn_add_user.setOnClickListener(this);
        btn_goods.setOnClickListener(this);
        btn_units.setOnClickListener(this);
        btn_packages.setOnClickListener(this);
        btn_tests.setOnClickListener(this);
        btn_device.setOnClickListener(this);
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
                turnToActivity(ListUsersActivity.class);
                break;
            case R.id.btn_goods:
                turnToActivity(ListGoodsActivity.class);
                break;
            case R.id.btn_units:
                turnToActivity(ListUnitsActivity.class);
                break;
            case R.id.btn_packages:
                turnToActivity(ListPackagesActivity.class);
                break;
            case R.id.btn_tests:
                turnToActivity(TestActivity.class);
                break;
            case R.id.btn_device:
                turnToActivity(PrinterActivity.class);
                break;
            default:
                break;
        }
    }

}