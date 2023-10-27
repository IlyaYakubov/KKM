package kg.printer.kkm.controllers;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import kg.printer.kkm.R;
import kg.printer.kkm.domains.BaseApplication;
import kg.printer.kkm.repositories.BaseEnumsDAO;
import com.rt.printerlibrary.enumerate.BarcodeType;

import java.util.Arrays;
import java.util.List;

public class BarcodeController extends ListActivity {

    private Context mContext;
    private LinearLayout back;
    private List<String> tagList;
    private List<String> itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode);

        initView();
        setAdapter();
        setListener();

        mContext = this;
    }

    private void initView() {
        back = findViewById(R.id.back);
    }

    public void setAdapter() {
        tagList = Arrays.asList(mContext.getResources().getStringArray(R.array.barcode_tag));
        switch (BaseApplication.getInstance().getCurrentCmdType()) {
            case BaseEnumsDAO.CMD_ESC:
                itemList = Arrays.asList(mContext.getResources().getStringArray(R.array.barcode_item_esc));
                break;
            case BaseEnumsDAO.CMD_TSC:
                itemList = Arrays.asList(mContext.getResources().getStringArray(R.array.barcode_item_label));
                break;
            case BaseEnumsDAO.CMD_CPCL:
                itemList = Arrays.asList(mContext.getResources().getStringArray(R.array.barcode_item_label));
                break;
            case BaseEnumsDAO.CMD_ZPL:
                itemList = Arrays.asList(mContext.getResources().getStringArray(R.array.barcode_item_label_zpl));
                break;
            default:
                itemList = Arrays.asList(mContext.getResources().getStringArray(R.array.barcode_item_label));
                break;
        }
        BarcodeAdapter adapter = new BarcodeAdapter(mContext, itemList, tagList);
        setListAdapter(adapter);
    }

    private void setListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        String barcodeType = l.getAdapter().getItem(position).toString();
        if (getString(R.string.UPC_A).equals(barcodeType)) {
            barcodeType = BarcodeType.UPC_A.name();
        } else if (getString(R.string.UPC_E).equals(barcodeType)) {
            barcodeType = BarcodeType.UPC_E.name();
        } else if (getString(R.string.EAN13).equals(barcodeType)) {
            barcodeType = BarcodeType.EAN13.name();
        } else if (getString(R.string.EAN14).equals(barcodeType)) {
            barcodeType = BarcodeType.EAN14.name();
        } else if (getString(R.string.EAN8).equals(barcodeType)) {
            barcodeType = BarcodeType.EAN8.name();
        } else if (getString(R.string.CODE39).equals(barcodeType)) {
            barcodeType = BarcodeType.CODE39.name();
        } else if (getString(R.string.ITF).equals(barcodeType)) {
            barcodeType = BarcodeType.ITF.name();
        } else if (getString(R.string.CODABAR).equals(barcodeType)) {
            barcodeType = BarcodeType.CODABAR.name();
        } else if (getString(R.string.CODE93).equals(barcodeType)) {
            barcodeType = BarcodeType.CODE93.name();
        } else if (getString(R.string.CODE128).equals(barcodeType)) {
            barcodeType = BarcodeType.CODE128.name();
        } else if (getString(R.string.GS1).equals(barcodeType)) {
            barcodeType = BarcodeType.GS1.name();
        } else if (getString(R.string.QR_CODE).equals(barcodeType)) {
            barcodeType = BarcodeType.QR_CODE.name();
        }
        Intent intent = new Intent(mContext, BarcodePrintController.class);
        Bundle bundle = new Bundle();
        bundle.putString(BarcodePrintController.BUNDLE_KEY_BARCODE_TYPE, barcodeType);
        intent.putExtras(bundle);
        startActivity(intent);
    }

}