package kg.printer.kkm.view.old;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import kg.printer.kkm.R;
import kg.printer.kkm.controllers.UIViewController;
import kg.printer.kkm.domains.BaseApplication;

import com.rt.printerlibrary.enumerate.ConnectStateEnum;
import com.rt.printerlibrary.enumerate.WiFiModeEnum;
import com.rt.printerlibrary.printer.RTPrinter;
import com.rt.printerlibrary.utils.WiFiSettingUtil;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class WIFISettingActivity extends UIViewController.BaseAdapter {

    private static final String[] WIFI_MMODE = {"STA", "AP"};
    private WiFiModeEnum wifi_mode = WiFiModeEnum.STA;

    private LinearLayout back;
    private WifiManager wifiManager;
    private List<ScanResult> scanResults;
    private ListView lv_set_wifi;
    private UIViewController.WIFIListAdapter wifi_adapter;
    private final ComparatorLevel comparatorLevel = new ComparatorLevel();
    @SuppressWarnings("rawtypes")
    private RTPrinter rtPrinter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.old_activity_set_wifi);
        init();
        initData();
        initView();
        addListener();
    }

    public void init() {
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
        wifiManager.startScan();
        scanResults = sortScanWifi();
        rtPrinter = BaseApplication.getInstance().getRtPrinter();
    }

    private void initData() {
    }

    public void initView() {
        back = findViewById(R.id.back);
        Spinner sp_wifi_set = findViewById(R.id.sp_wifi_set);
        lv_set_wifi = findViewById(R.id.lv_set_wifi);
        wifi_adapter = new UIViewController.WIFIListAdapter(this, scanResults);
        lv_set_wifi.setAdapter(wifi_adapter);
        ArrayAdapter<String> spinner_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, WIFI_MMODE);
        sp_wifi_set.setAdapter(spinner_adapter);
        sp_wifi_set.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        wifi_mode = WiFiModeEnum.STA;
                        break;
                    case 1:
                        wifi_mode = WiFiModeEnum.AP;
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        setListener();
    }

    @Override
    public void addListener() {

    }

    private void setListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        lv_set_wifi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ScanResult scanResult = scanResults.get(position);
                initSetWiFi(scanResult);
            }
        });
    }

    private void initSetWiFi(ScanResult scanResult) {
        showConfirmDialog(scanResult);
    }

    private void showConfirmDialog(final ScanResult scanResult) {
        int WIFIType;
        if (scanResult.capabilities.contains("WPA2-PSK")) {
            WIFIType = 1;
        } else if (scanResult.capabilities.contains("WPA-PSK")) {
            WIFIType = 1;
        } else if (scanResult.capabilities.contains("WEP")) {
            WIFIType = 2;
        } else {
            WIFIType = 0;
        }

        LayoutInflater inflater = getLayoutInflater();
        View inflate = inflater.inflate(R.layout.old_wifi_confirm_dialog, null);
        TextView tx_wifi_name = inflate.findViewById(R.id.tx_wifi_name);
        final LinearLayout ll_wifi_pd = inflate.findViewById(R.id.ll_wifi_pd);
        final EditText et_wifi_pd = inflate.findViewById(R.id.et_wifi_pd);
        tx_wifi_name.setText(scanResult.SSID);
        if (WIFIType == 0) {
            ll_wifi_pd.setVisibility(View.GONE);
        } else {
            ll_wifi_pd.setVisibility(View.VISIBLE);
        }
        new AlertDialog.Builder(this).setTitle(R.string.confirm_set_this_wifi).setView(inflate).setCancelable(false).setPositiveButton(getResources().getText(R.string.dialog_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (rtPrinter == null || rtPrinter.getConnectState() != ConnectStateEnum.Connected) {
                    showToast(R.string.pls_connect_printer_first);
                    return;
                }
                //write the wifi infos to the printer.
                rtPrinter.writeMsgAsync(WiFiSettingUtil.getInstance().setWiFiParam(scanResult, et_wifi_pd.getText().toString(), wifi_mode));
            }
        }).setNegativeButton(getResources().getText(R.string.dialog_cancel), null).show();

    }

    public void onClick(View view) {
        if (view.getId() == R.id.btn_wifi_refresh) {
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.show();
            scanResults.clear();
            wifiManager.startScan();
            scanResults.addAll(sortScanWifi());
            wifi_adapter.notifyDataSetChanged();
            progressDialog.cancel();
        }
    }

    private List<ScanResult> sortScanWifi() {
        List<ScanResult> scanR = wifiManager.getScanResults();
        int size = scanR.size();
        for (int i = 0; i < size; i++) {
            if (TextUtils.isEmpty(scanR.get(i).SSID)) {
                //noinspection SuspiciousListRemoveInLoop
                scanR.remove(i);
                size--;
            }
        }
        //noinspection unchecked
        Collections.sort(scanR, comparatorLevel);
        return scanR;
    }

    @SuppressWarnings("rawtypes")
    private static class ComparatorLevel implements Comparator {
        public int compare(Object arg0, Object arg1) {
            ScanResult user0 = (ScanResult) arg0;
            ScanResult user1 = (ScanResult) arg1;
            int flag = user1.level - (user0.level);
            if (flag == 0) {
                return user1.SSID.compareTo(user0.SSID);
            } else {
                return flag;
            }
        }
    }

}
