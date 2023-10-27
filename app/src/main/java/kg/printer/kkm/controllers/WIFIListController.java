package kg.printer.kkm.controllers;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import kg.printer.kkm.R;

public class WIFIListController extends BaseAdapter {

    private Context mContext;
    private List<ScanResult> mScanResults;

    public WIFIListController(Context context, List<ScanResult> scanResults) {
        this.mContext = context;
        this.mScanResults = scanResults;
    }

    @Override
    public int getCount() {
        return mScanResults.size();
    }

    @Override
    public ScanResult getItem(int position) {
        return mScanResults.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext.getApplicationContext())
                    .inflate(R.layout.wifi_listview_item, null);
        }
        TextView wifi_set_wifiName = ViewHolderController.get(convertView,
                R.id.wifi_set_wifiName);
        ImageView wifi_set_wifiLevel = ViewHolderController.get(convertView,
                R.id.wifi_set_wifiLevel);
        wifi_set_wifiName.setText(mScanResults.get(position).SSID);
        if (Math.abs(mScanResults.get(position).level) > 100) {
            wifi_set_wifiLevel.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.signal_wifi_1));
        } else if (Math.abs(mScanResults.get(position).level) > 80) {
            wifi_set_wifiLevel.setImageDrawable(mContext.getResources()
                    .getDrawable(R.mipmap.signal_wifi_2));
        } else if (Math.abs(mScanResults.get(position).level) > 70) {
            wifi_set_wifiLevel.setImageDrawable(mContext.getResources()
                    .getDrawable(R.mipmap.signal_wifi_3));
        } else if (Math.abs(mScanResults.get(position).level) > 60) {
            wifi_set_wifiLevel.setImageDrawable(mContext.getResources()
                    .getDrawable(R.mipmap.signal_wifi_4));
        } else if (Math.abs(mScanResults.get(position).level) > 50) {
            wifi_set_wifiLevel.setImageDrawable(mContext.getResources()
                    .getDrawable(R.mipmap.signal_wifi_5));
        } else {
            wifi_set_wifiLevel.setImageDrawable(mContext.getResources()
                    .getDrawable(R.mipmap.signal_wifi_5));
        }
        return convertView;
    }

}