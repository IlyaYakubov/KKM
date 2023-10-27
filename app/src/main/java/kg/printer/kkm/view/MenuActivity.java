package kg.printer.kkm.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import kg.printer.kkm.R;
import kg.printer.kkm.controllers.UIViewController;

public class MenuActivity extends UIViewController.BaseAdapter implements View.OnClickListener {

    private TextView tv_messages;
    private Button btn_reg_operations, btn_set_kassa;

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

        btn_reg_operations = findViewById(R.id.btn_reg_operations);
        btn_set_kassa = findViewById(R.id.btn_set_kassa);
    }

    @Override
    public void addListener() {
        btn_reg_operations.setOnClickListener(this);
        btn_set_kassa.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_reg_operations:
                turnToActivity(CartActivity.class);
                break;
            case R.id.btn_set_kassa:
                turnToActivity(SettingsActivity.class);
                break;
            default:
                break;
        }
    }

}
