package kg.printer.kkm.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Objects;

import kg.printer.kkm.R;
import kg.printer.kkm.controllers.UIViewController;

public class MenuActivity extends UIViewController.BaseAdapter implements View.OnClickListener {

    private TextView tv_messages;
    private Button btn_sale, btn_x_report, btn_z_report, btn_settings;

    private String position, surname, name, secondName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        init();
        initView();
        addListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Objects.equals(position, "Администратор")) {
            btn_sale.setText("Продажа (не доступна)");
            btn_sale.setBackgroundColor(Color.WHITE);
            btn_sale.setTextColor(Color.GRAY);
            btn_sale.setClickable(false);
        } else {
            btn_sale.setClickable(true);
        }
    }

    @Override
    public void init() {
        Intent intent = getIntent();
        position = intent.getStringExtra("position");
        surname = intent.getStringExtra("surname");
        name = intent.getStringExtra("name");
        secondName = intent.getStringExtra("secondName");

        if (name != null && !name.isEmpty()) {
            UIViewController.ToastAdapter.show(this, "Вы вошли как "
                    + position.trim() + ", " + surname.trim() + " "
                    + name.trim() + " " + secondName.trim());
        }
    }

    @Override
    public void initView() {
        tv_messages = findViewById(R.id.tv_messages);

        btn_sale = findViewById(R.id.btn_reg_sale);

        btn_x_report = findViewById(R.id.btn_x_report);
        btn_x_report.setBackgroundColor(Color.WHITE);
        btn_x_report.setTextColor(Color.GRAY);
        btn_x_report.setClickable(false);

        btn_z_report = findViewById(R.id.btn_z_report);
        btn_z_report.setBackgroundColor(Color.WHITE);
        btn_z_report.setTextColor(Color.GRAY);
        btn_z_report.setClickable(false);

        btn_settings = findViewById(R.id.btn_settings);
    }

    @Override
    public void addListener() {
        btn_sale.setOnClickListener(this);
        btn_x_report.setOnClickListener(this);
        btn_z_report.setOnClickListener(this);
        btn_settings.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_reg_sale:
                turnToActivity(CartActivity.class);
                break;
            case R.id.btn_settings:
                turnToActivity(SettingsActivity.class);
                break;
            default:
                break;
        }
    }

}
