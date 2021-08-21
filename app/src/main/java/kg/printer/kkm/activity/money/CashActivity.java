package kg.printer.kkm.activity.money;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import kg.printer.kkm.R;
import kg.printer.kkm.app.BaseActivity;
import kg.printer.kkm.app.BaseApplication;
import kg.printer.kkm.utils.BaseEnum;
import com.rt.printerlibrary.bean.SerialPortConfigBean;
import com.rt.printerlibrary.cmd.Cmd;
import com.rt.printerlibrary.cmd.EscFactory;
import com.rt.printerlibrary.connect.PrinterInterface;
import com.rt.printerlibrary.enumerate.CommonEnum;
import com.rt.printerlibrary.factory.cmd.CmdFactory;
import com.rt.printerlibrary.factory.connect.PIFactory;
import com.rt.printerlibrary.factory.connect.SerailPortFactory;
import com.rt.printerlibrary.factory.printer.PrinterFactory;
import com.rt.printerlibrary.observer.PrinterObserver;
import com.rt.printerlibrary.printer.RTPrinter;
import com.rt.printerlibrary.setting.TextSetting;
import com.rt.printerlibrary.utils.PrinterPowerUtil;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class CashActivity extends BaseActivity implements View.OnClickListener {

    private ArrayList<String> itog = new ArrayList<>();
    private double sumDoub = 0;

    private TextView tvItog, tvSurrender;
    private EditText etContributed;
    private Button btnOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retial_cash);

        initView();
        addListener();
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*if (rtPrinter != null && rtPrinter.getPrinterInterface() != null) {
            rtPrinter.disConnect();
        }
        printerPowerUtil.setPrinterPower(false);*/
    }

    @Override
    public void initView() {
        tvItog = findViewById(R.id.tv_itog);
        tvSurrender = findViewById(R.id.tv_surrender);

        etContributed = findViewById(R.id.et_contributed);

        btnOk = findViewById(R.id.btn_ok);
    }

    @Override
    public void addListener() {

        etContributed.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                toCount();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        btnOk.setOnClickListener(this);

    }

    @Override
    public void init() {
        /*BaseApplication.instance.setCurrentCmdType(BaseEnum.CMD_ESC);
        printerFactory = new ThermalPrinterFactory();
        rtPrinter = printerFactory.create();
        PrinterObserverManager.getInstance().add(this);
        configObj = new SerialPortConfigBean().getDefaultConfig();
        printerPowerUtil = new PrinterPowerUtil(this);

        connectSerialPort((SerialPortConfigBean) configObj);
        printerPowerUtil.setPrinterPower(true);

        textSetting = new TextSetting();*/

        Intent intent = getIntent();
        itog = intent.getExtras().getStringArrayList("itog");
        for (String sum : itog) {
            sum = sum.replace(',','.');
            sumDoub = sumDoub + Double.parseDouble(sum);
        }

        tvItog.setText(String.valueOf(sumDoub));
        etContributed.setText(String.valueOf(sumDoub));
        tvSurrender.setText("0");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok:
                if (etContributed.getText().equals("")) return;

                hideKeyboard(v);
                finish();
                break;
        }
    }

    private void toCount() {
        if (!etContributed.getText().toString().equals("")) {
            double contributed = Double.parseDouble(etContributed.getText().toString());
            double surrender = contributed - sumDoub;

            tvItog.setText(String.valueOf(contributed));
            tvSurrender.setText(String.valueOf(surrender));
        }
    }

    private void hideKeyboard(View view) {
        View viewLayout = this.getCurrentFocus();
        if (viewLayout != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) CashActivity.this.getSystemService(BaseActivity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}