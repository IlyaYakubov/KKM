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
import kg.printer.kkm.domains.User;

public class MenuActivity extends UIViewController.BaseAdapter implements View.OnClickListener {

    private TextView tvMessage;
    private Button btnSale, btnXReport, btnZReport, btnSettings;

    private String position, surname, name, secondName;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        init();
        initView();
        addListener();

        updateView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Objects.equals(position, "Администратор")) {
            btnSale.setText("Продажа (не доступна)");
            btnSale.setBackgroundColor(Color.WHITE);
            btnSale.setTextColor(Color.GRAY);
            btnSale.setClickable(false);

            btnSettings.setClickable(true);
        } else {
            btnSale.setClickable(true);

            btnSettings.setText("Настройки (не доступны)");
            btnSettings.setBackgroundColor(Color.WHITE);
            btnSettings.setTextColor(Color.GRAY);
            btnSettings.setClickable(false);
        }
    }

    @Override
    public void init() {
        Intent intent = getIntent();
        user = intent.getParcelableExtra("user");

        position = intent.getStringExtra("position");
        surname = intent.getStringExtra("surname");
        name = intent.getStringExtra("name");
        secondName = intent.getStringExtra("secondName");
    }

    @Override
    public void initView() {
        tvMessage = findViewById(R.id.tv_message);
        btnSale = findViewById(R.id.btn_reg_sale);

        // Доступность кнопки
        btnXReport = findViewById(R.id.btn_x_report);
        btnXReport.setBackgroundColor(Color.WHITE);
        btnXReport.setTextColor(Color.GRAY);
        btnXReport.setClickable(false);

        // Доступность кнопки
        btnZReport = findViewById(R.id.btn_z_report);
        btnZReport.setBackgroundColor(Color.WHITE);
        btnZReport.setTextColor(Color.GRAY);
        btnZReport.setClickable(false);

        btnSettings = findViewById(R.id.btn_settings);
    }

    @Override
    public void addListener() {
        btnSale.setOnClickListener(this);
        btnXReport.setOnClickListener(this);
        btnZReport.setOnClickListener(this);
        btnSettings.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_reg_sale:
                turnToActivityWithUser(CartActivity.class, user);
                break;
            case R.id.btn_settings:
                turnToActivity(SettingsActivity.class);
                break;
            default:
                break;
        }
    }

    private void updateView() {
        if (name != null && !name.isEmpty()) {
            tvMessage.setText(user.getName().trim());
        }
    }

}
