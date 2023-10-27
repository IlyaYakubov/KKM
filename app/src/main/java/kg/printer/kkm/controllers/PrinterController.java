package kg.printer.kkm.controllers;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.rt.printerlibrary.bean.SerialPortConfigBean;
import com.rt.printerlibrary.cmd.Cmd;
import com.rt.printerlibrary.cmd.EscFactory;
import com.rt.printerlibrary.connect.PrinterInterface;
import com.rt.printerlibrary.enumerate.CommonEnum;
import com.rt.printerlibrary.factory.cmd.CmdFactory;
import com.rt.printerlibrary.factory.connect.PIFactory;
import com.rt.printerlibrary.factory.connect.SerailPortFactory;
import com.rt.printerlibrary.factory.printer.PrinterFactory;
import com.rt.printerlibrary.factory.printer.ThermalPrinterFactory;
import com.rt.printerlibrary.observer.PrinterObserver;
import com.rt.printerlibrary.observer.PrinterObserverManager;
import com.rt.printerlibrary.printer.RTPrinter;
import com.rt.printerlibrary.utils.PrinterPowerUtil;

import kg.printer.kkm.R;
import kg.printer.kkm.domains.BaseApplication;
import kg.printer.kkm.repositories.BaseEnumsDAO;

public class PrinterController extends BaseController implements View.OnClickListener, PrinterObserver {

    private TextView tv_device_selected;
    private RadioGroup rg_cmdtype;
    private FlowRadioGroupController rg_connect;

    private Button btn_selftest_print, btn_txt_print, btn_disConnect, btn_connect;

    @BaseEnumsDAO.ConnectType
    private int checkedConType = BaseEnumsDAO.CON_COM;
    private RTPrinter rtPrinter = null;
    private PrinterFactory printerFactory;
    private Object configObj;
    private PrinterPowerUtil printerPowerUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        init();
        addListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        doDisConnect();
    }

    @Override
    public void initView() {
        tv_device_selected = findViewById(R.id.tv_device_selected);

        rg_cmdtype = findViewById(R.id.rg_cmdtype);
        rg_connect = findViewById(R.id.rg_connect);

        btn_selftest_print = findViewById(R.id.btn_selftest_print);
        btn_txt_print = findViewById(R.id.btn_txt_print);
        btn_connect = findViewById(R.id.btn_connect);
        btn_disConnect = findViewById(R.id.btn_disConnect);
    }

    @Override
    public void addListener() {
        tv_device_selected.setOnClickListener(this);

        btn_selftest_print.setOnClickListener(this);
        btn_txt_print.setOnClickListener(this);
        btn_connect.setOnClickListener(this);
        btn_disConnect.setOnClickListener(this);

        radioButtonCheckListener();
    }

    @Override
    public void init() {
        BaseApplication.instance.setCurrentCmdType(BaseEnumsDAO.CMD_ESC);
        printerFactory = new ThermalPrinterFactory();
        rtPrinter = printerFactory.create();

        PrinterObserverManager.getInstance().add(this);

        configObj = new SerialPortConfigBean().getDefaultConfig();
        tv_device_selected.setText(configObj.toString());
        printerPowerUtil = new PrinterPowerUtil(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_selftest_print:
                selfTestPrint();
                break;
            case R.id.btn_txt_print:
                textPrint();
                break;
            case R.id.btn_disConnect:
                doDisConnect();
                break;
            case R.id.btn_connect:
                doConnect();
                break;
            case R.id.tv_device_selected:
                showConnectDialog();
                break;
            default:
                break;
        }
    }

    private void radioButtonCheckListener() {
        rg_cmdtype.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switch (i) {
                    case R.id.rb_cmd_esc:
                        BaseApplication.instance.setCurrentCmdType(BaseEnumsDAO.CMD_ESC);
                        printerFactory = new ThermalPrinterFactory();
                        rtPrinter = printerFactory.create();
                        break;
                }
            }
        });

        rg_connect.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                doDisConnect();
                switch (i) {
                    case R.id.rb_connect_com:
                        checkedConType = BaseEnumsDAO.CON_COM;
                        break;
                }
            }
        });
    }

    private void doConnect() {
        switch (checkedConType) {
            case BaseEnumsDAO.CON_COM:
                connectSerialPort((SerialPortConfigBean) configObj);
                printerPowerUtil.setPrinterPower(true); // turn printer power on.
                break;
            default:
                break;
        }
    }

    private void doDisConnect() {
        if (rtPrinter != null && rtPrinter.getPrinterInterface() != null) {
            rtPrinter.disConnect();
        }
        printerPowerUtil.setPrinterPower(false); // turn printer power off.
        setPrintEnable(false);
    }

    private void selfTestPrint() {
        switch (BaseApplication.getInstance().getCurrentCmdType()) {
            case BaseEnumsDAO.CMD_ESC:
                escSelftestPrint();
                break;
            default:
                break;
        }
    }

    private void textPrint() {
        turnToActivity(TextPrintController.class);
    }

    private void escSelftestPrint() {
        CmdFactory cmdFactory = new EscFactory();
        Cmd cmd = cmdFactory.create();
        cmd.append(cmd.getHeaderCmd());
        cmd.append(cmd.getSelfTestCmd());
        rtPrinter.writeMsgAsync(cmd.getAppendCmds());
    }

    private void showConnectDialog() {
        switch (checkedConType) {
            case BaseEnumsDAO.CON_COM:
                ToastController.show(PrinterController.this, "Нажмите на [Подключиться] чтобы установить соединение");
                break;
            default:
                break;
        }
    }

    private void setPrintEnable(boolean isEnable) {
        btn_selftest_print.setEnabled(isEnable);
        btn_txt_print.setEnabled(isEnable);
        btn_connect.setEnabled(!isEnable);
        btn_disConnect.setEnabled(isEnable);
    }

    private void connectSerialPort(SerialPortConfigBean serialPortConfigBean) {
        PIFactory piFactory = new SerailPortFactory();
        PrinterInterface printerInterface = piFactory.create();
        printerInterface.setConfigObject(serialPortConfigBean);
        rtPrinter.setPrinterInterface(printerInterface);
        try {
            rtPrinter.connect(serialPortConfigBean);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    @Override
    public void printerObserverCallback(final PrinterInterface printerInterface, final int state) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (state) {
                    case CommonEnum.CONNECT_STATE_SUCCESS:
                        showToast(printerInterface.getConfigObject().toString() + getString(R.string._main_connected));
                        tv_device_selected.setText(printerInterface.getConfigObject().toString());
                        rtPrinter.setPrinterInterface(printerInterface);
                        BaseApplication.getInstance().setRtPrinter(rtPrinter);
                        setPrintEnable(true);
                        break;
                    case CommonEnum.CONNECT_STATE_INTERRUPTED:
                        if (printerInterface != null && printerInterface.getConfigObject() != null) {
                            showToast(printerInterface.getConfigObject().toString() + getString(R.string._main_disconnect));
                        } else {
                            showToast(getString(R.string._main_disconnect));
                        }
                        tv_device_selected.setText(R.string.please_connect);
                        BaseApplication.getInstance().setRtPrinter(null);
                        setPrintEnable(false);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    public void printerReadMsgCallback(PrinterInterface printerInterface, byte[] bytes) {}

}