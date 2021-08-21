package kg.printer.kkm.activity.print;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import kg.printer.kkm.R;
import kg.printer.kkm.app.BaseApplication;
import kg.printer.kkm.utils.FuncUtils;
import com.rt.printerlibrary.cmd.EscCmd;
import com.rt.printerlibrary.connect.PrinterInterface;
import com.rt.printerlibrary.observer.PrinterObserver;
import com.rt.printerlibrary.observer.PrinterObserverManager;
import com.rt.printerlibrary.printer.RTPrinter;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CmdTestActivity extends Activity implements View.OnClickListener, PrinterObserver {

    private TextView tv_rev;
    private Button btn_clear_send, btn_send;
    private RadioGroup rg_sendtype;
    private EditText et_send;

    private RTPrinter rtPrinter = BaseApplication.getInstance().getRtPrinter();
    private byte btSendType = 0; // 0=txt, 1=hex

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cmd_send_read);

        initView();
        addListener();
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PrinterObserverManager.getInstance().remove(this);
    }

    private void initView() {
        tv_rev = findViewById(R.id.tv_rev);
        tv_rev.setText("Arkad Pro\narkadproff@gmail.com\n");

        btn_clear_send = findViewById(R.id.btn_clear_send);
        btn_send = findViewById(R.id.btn_send);
        rg_sendtype = findViewById(R.id.rg_sendtype);
        et_send = findViewById(R.id.et_send);
    }

    private void addListener() {
        btn_send.setOnClickListener(this);
        btn_clear_send.setOnClickListener(this);

        rg_sendtype.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_text:
                        btSendType = 0;
                        break;
                    case R.id.rb_hex:
                        btSendType = 1;
                        break;
                    default:
                        break;
                }
                et_send.setSelection(et_send.getText().toString().length());
            }
        });

        rg_sendtype.check(R.id.rb_hex);
    }

    private void init() {
        PrinterObserverManager.getInstance().add(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_clear_send:
                et_send.setText("");
                break;
            case R.id.btn_send:
                if (rtPrinter != null) {
                    byte[] btContent = null;
                    String str = et_send.getText().toString();
                    if (btSendType == 0) { //text
                        try {
                            btContent = str.getBytes("cp866");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    } else if (btSendType == 1) { //hex
                        str = replaceBlank(str);
                        et_send.setText(str);
                        btContent = FuncUtils.HexToByteArr(str);
                    }
                    if (btContent != null) {
                        rtPrinter.writeMsgAsync(btContent);
                    }
                }
                break;
            default:
                break;
        }
    }

    public byte[] getHeadCmd() {
        byte[] btStart = {
                0x03, (byte) 0xff, 0x2f, 0x00,
                0x00, 0x00, 0x00, 0x00,
                0x00, (byte) 0xd3, 0x00, 0x00};
        rtPrinter.writeMsg(btStart);
        EscCmd cmd = new EscCmd();
        rtPrinter.writeMsg(cmd.getLFCRCmd());
        return btStart;
    }

    @Override
    public void printerObserverCallback(PrinterInterface printerInterface, int i) {

    }

    @Override
    public void printerReadMsgCallback(PrinterInterface printerInterface, final byte[] bytes) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv_rev.append(formatDate(System.currentTimeMillis()) + "_[Rev]: " + FuncUtils.ByteArrToHex(bytes) + "\n");
            }
        });
    }

    public static String formatDate(long timeMillis) {
        String format = "HH:mm:ss.SSS";
        Date date = null;
        if (timeMillis > 0) {
            date = new Date(timeMillis);
        } else {
            date = new Date();
        }

        final SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }

    public static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

}