package kg.printer.kkm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import kg.printer.kkm.R;
import kg.printer.kkm.activity.retial.OperationsActivity;
import kg.printer.kkm.app.BaseActivity;
import kg.printer.kkm.app.BaseApplication;
import kg.printer.kkm.utils.BaseEnum;
import kg.printer.kkm.view.BasicListDialog;
import com.rt.printerlibrary.bean.SerialPortConfigBean;
import com.rt.printerlibrary.connect.PrinterInterface;
import com.rt.printerlibrary.enumerate.CommonEnum;
import com.rt.printerlibrary.factory.connect.PIFactory;
import com.rt.printerlibrary.factory.connect.SerailPortFactory;
import com.rt.printerlibrary.factory.printer.PrinterFactory;
import com.rt.printerlibrary.observer.PrinterObserver;
import com.rt.printerlibrary.printer.RTPrinter;
import com.rt.printerlibrary.utils.PrinterPowerUtil;

import java.util.ArrayList;

public class MainWindowActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_messages;
    private Button btn_reg_operations, btn_set_kassa;

    private String position = "", surname = "", name = "", secondName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_window);

        initView();
        addListener();
        init();
    }

    @Override
    public void onBackPressed() {
        /*String message = tv_messages.getText().toString();

        if (!message.equals(getString(R.string.non_reg_user))) {
            final BasicListDialog exitDialog = new BasicListDialog();

            ArrayList<String> contentList = new ArrayList<>();
            contentList.add("Выйти из системы");

            Bundle args = new Bundle();
            args.putString(BasicListDialog.BUNDLE_KEY_TITLE, "Желаете выйти из системы?");
            args.putStringArrayList(BasicListDialog.BUNDLE_KEY_CONTENT_LIST, contentList);

            exitDialog.setArguments(args);
            exitDialog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (parent.getAdapter().getItem(position).toString().equals("Выйти из системы")) {
                        tv_messages.setText(getString(R.string.non_reg_user));
                    }
                    exitDialog.dismiss();
                    finish();
                }
            });

            exitDialog.show(getFragmentManager(), null);
        } else {
            *//*if (rtPrinter != null && rtPrinter.getPrinterInterface() != null) {
                rtPrinter.disConnect();
            }
            printerPowerUtil.setPrinterPower(false);*//*
            finish();
        }*/
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
    public void init() {
        Intent intent = getIntent();
        position = intent.getStringExtra("position");
        surname = intent.getStringExtra("surname");
        name = intent.getStringExtra("name");
        secondName = intent.getStringExtra("secondName");

        if (name != null && !name.isEmpty())
            tv_messages.setText("Вы вошли как " + position.trim() + ", " + surname.trim() + " " + name.trim() + " " + secondName.trim());

        /*BaseApplication.instance.setCurrentCmdType(BaseEnum.CMD_ESC);
        printerFactory = new ThermalPrinterFactory();
        rtPrinter = printerFactory.create();
        PrinterObserverManager.getInstance().add(this);
        configObj = new SerialPortConfigBean().getDefaultConfig();
        printerPowerUtil = new PrinterPowerUtil(this);

        connectSerialPort((SerialPortConfigBean) configObj);
        printerPowerUtil.setPrinterPower(true);*/
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_reg_operations:
                turnToActivity(OperationsActivity.class);
                break;
            case R.id.btn_set_kassa:
                turnToActivity(SettingsActivity.class);
                break;
            default:
                break;
        }
    }

}