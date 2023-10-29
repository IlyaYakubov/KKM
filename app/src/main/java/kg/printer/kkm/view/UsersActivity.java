package kg.printer.kkm.view;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import kg.printer.kkm.R;
import kg.printer.kkm.controllers.UIViewController;
import kg.printer.kkm.domains.User;
import kg.printer.kkm.services.AuthenticationService;

import java.util.ArrayList;

public class UsersActivity extends UIViewController.BaseAdapter implements View.OnClickListener {

    private ListView lvData;
    private Button btnAdd;

    private final ArrayList<User> users = new ArrayList<>();
    private final ArrayList<String> names = new ArrayList<>();

    private AuthenticationService authenticationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        initView();
        addListener();
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();

        names.clear();
        authenticationService.fillUsers(users);
        fillAdapter(users);
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
        authenticationService = new AuthenticationService(this);

        if (users.isEmpty()) {
            authenticationService.createAdministratorInDatabase();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_add) {
            turnToActivityWithPosition(UserActivity.class, -1, 1);
        }
    }

    private void fillAdapter(ArrayList<User> users) {
        for (User user : users) {
            names.add(user.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, names);
        lvData.setAdapter(adapter);

        lvData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    turnToActivity(AdministratorActivity.class);
                } else {
                    turnToActivityWithPosition(UserActivity.class, position, 0);
                }
            }
        });
    }

}
