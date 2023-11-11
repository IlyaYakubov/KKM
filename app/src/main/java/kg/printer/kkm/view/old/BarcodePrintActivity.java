package kg.printer.kkm.view.old;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import kg.printer.kkm.R;
import kg.printer.kkm.controllers.UIViewController;
import kg.printer.kkm.domains.BaseApplication;
import kg.printer.kkm.repositories.DatabaseDAO;

import com.rt.printerlibrary.cmd.Cmd;
import com.rt.printerlibrary.cmd.EscFactory;
import com.rt.printerlibrary.enumerate.BarcodeStringPosition;
import com.rt.printerlibrary.enumerate.BarcodeType;
import com.rt.printerlibrary.enumerate.EscBarcodePrintOritention;
import com.rt.printerlibrary.exception.SdkException;
import com.rt.printerlibrary.factory.cmd.CmdFactory;
import com.rt.printerlibrary.printer.RTPrinter;
import com.rt.printerlibrary.setting.BarcodeSetting;

import java.util.Objects;

@SuppressWarnings({"rawtypes", "RegExpRedundantEscape"})
public class BarcodePrintActivity extends UIViewController.BaseAdapter implements View.OnClickListener {

    public static final String BUNDLE_KEY_BARCODE_TYPE = "barcodeType";

    private View back;
    private TextView tv_barcodetype, tv_error_tip;
    private EditText et_barcode_content;
    private RadioGroup rg_print_barcode_orientation;
    private Button btn_print;

    private RTPrinter rtPrinter;
    private BarcodeType barcodeType;

    @DatabaseDAO.BaseEnums.CmdType
    private int curCmdType;
    private String barcodeContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.old_activity_barcode_print);

        initView();
        addListener();
        init();
    }

    @Override
    public void initView() {
        back = findViewById(R.id.back);
        tv_barcodetype = findViewById(R.id.tv_barcodetype);
        et_barcode_content = findViewById(R.id.et_barcode_content);
        rg_print_barcode_orientation = findViewById(R.id.rg_print_barcode_orientation);
        btn_print = findViewById(R.id.btn_print);
        tv_error_tip = findViewById(R.id.tv_error_tip);
    }

    @Override
    public void init() {
        rtPrinter = BaseApplication.getInstance().getRtPrinter();
        curCmdType = BaseApplication.getInstance().getCurrentCmdType();
        Bundle mBundle = getIntent().getExtras();
        barcodeType = Enum.valueOf(BarcodeType.class, Objects.requireNonNull(mBundle).getString(BUNDLE_KEY_BARCODE_TYPE));
        tv_barcodetype.setText(barcodeType.name());
        initBarcodeCheck();
    }

    @Override
    public void addListener() {
        back.setOnClickListener(this);
        btn_print.setOnClickListener(this);
        rg_print_barcode_orientation.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switch (i) {
                    case R.id.rb_print_barcode_orientation_left:
                        break;
                    case R.id.rb_print_barcode_orientation_normal:
                        break;
                    case R.id.rb_print_barcode_orientation_right:
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void initBarcodeCheck() {
        String inputTip = null;
        switch (barcodeType) {
            case UPC_A:
                inputTip = getString(R.string.tip_barcode_text_UPC_A);
                et_barcode_content.setRawInputType(InputType.TYPE_CLASS_NUMBER);
                et_barcode_content.setFilters(new InputFilter[]{new InputFilter() {
                    @Override
                    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                        String regex = "[\\D]";
                        return source.toString().replaceAll(regex, "");
                    }
                }, new InputFilter.LengthFilter(11)});
                break;
            case UPC_E:
            /*  inputTip = getString(R.string.tip_barcode_text_UPC_E);
                etInput.setInputType(InputType.TYPE_CLASS_NUMBER);*/
                break;
            case EAN13:
                inputTip = getString(R.string.tip_barcode_text_EAN13);
                et_barcode_content.setRawInputType(InputType.TYPE_CLASS_NUMBER);
                et_barcode_content.setFilters(new InputFilter[]{new InputFilter() {
                    @Override
                    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

                        String regex = "[\\D]";
                        return source.toString().replaceAll(regex, "");
                    }
                }, new InputFilter.LengthFilter(12)});
                break;
            case EAN8:
                inputTip = getString(R.string.tip_barcode_text_EAN8);
                et_barcode_content.setRawInputType(InputType.TYPE_CLASS_NUMBER);
                et_barcode_content.setFilters(new InputFilter[]{new InputFilter() {
                    @Override
                    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

                        String regex = "[\\D]";
                        return source.toString().replaceAll(regex, "");
                    }
                }, new InputFilter.LengthFilter(7)});
                break;
            case CODE39:
                inputTip = getString(R.string.tip_barcode_text_CODE39);
                et_barcode_content.setRawInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
                et_barcode_content.setFilters(new InputFilter[]{new InputFilter() {

                    @Override
                    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

                        String regex = "[^a-zA-Z\\p{Digit} \\$%\\+\\-\\./]";
                        return source.toString().replaceAll(regex, "");
                    }
                }, new InputFilter.AllCaps(), new InputFilter.LengthFilter(30)});
                break;
            case ITF:
                et_barcode_content.setRawInputType(InputType.TYPE_CLASS_NUMBER);
                InputFilter.LengthFilter lengthFilter;
                if (curCmdType == DatabaseDAO.BaseEnums.CMD_ESC) {
                    inputTip = getString(R.string.tip_barcode_text_ITF);
                    lengthFilter = new InputFilter.LengthFilter(30);
                } else {
                    inputTip = getString(R.string.tip_barcode_text_ITF14);
                    lengthFilter = new InputFilter.LengthFilter(14);
                }

                et_barcode_content.setFilters(new InputFilter[]{new InputFilter() {
                    @Override
                    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

                        String regex = "[\\D]";
                        return source.toString().replaceAll(regex, "");
                    }
                }, lengthFilter});
                break;
            case CODABAR:
                inputTip = getString(R.string.tip_barcode_text_CODABAR);
                et_barcode_content.setRawInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
                et_barcode_content.setFilters(new InputFilter[]{new InputFilter() {

                    @Override
                    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

                        String regex = "[^0-9a-dA-D\\$\\+\\-\\./:]";
                        return source.toString().replaceAll(regex, "");
                    }
                }, new InputFilter.AllCaps(), new InputFilter.LengthFilter(30)});
                break;
            case CODE93:
                 /* inputTip = getString(R.string.tip_barcode_text_CODE93);
                    etInput.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
	                etInput.setFilters(new InputFilter[]{new InputFilter() {

	                    @Override
	                    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

	                        String regex = "[^1-9a-dA-D \\$\\+\\-\\./:]";
	                        String s = source.toString().replaceAll(regex,"");
	                        return s;
	                    }
	                }, new InputFilter.AllCaps()});*/
                break;
            case CODE128:
                inputTip = getString(R.string.tip_barcode_text_CODE128);
                et_barcode_content.setRawInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                et_barcode_content.setFilters(new InputFilter[]{new InputFilter() {

                    @Override
                    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

                        String regex = "[^\\p{ASCII}]";
                        return source.toString().replaceAll(regex, "");
                    }
                }, new InputFilter.AllCaps(), new InputFilter.LengthFilter(42)});
                break;
            case QR_CODE:
                inputTip = getString(R.string.tip_barcode_text_QR_CODE);
                et_barcode_content.setRawInputType(InputType.TYPE_CLASS_TEXT);
                et_barcode_content.setFilters(new InputFilter[]{new InputFilter() {

                    @Override
                    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

                        String regex = "[^\\p{ASCII}]";
                        return source.toString().replaceAll(regex, "");
                    }
                }});
                break;
        }

        if (!TextUtils.isEmpty(inputTip)) {
            tv_error_tip.setText(inputTip);
            tv_error_tip.setVisibility(View.VISIBLE);
        } else {
            tv_error_tip.setVisibility(View.GONE);
        }
    }

    private void print() throws SdkException {
        barcodeContent = et_barcode_content.getText().toString();
        if (TextUtils.isEmpty(barcodeContent)) {
            showToast(getString(R.string.error_text));
        }

        if(rtPrinter == null){
            showToast(getString(R.string.tip_pls_connect_device));
            return;
        }

        if (curCmdType == DatabaseDAO.BaseEnums.CMD_ESC) {
            escPrint();
        }
    }

    private void escPrint() {
        CmdFactory cmdFactory = new EscFactory();
        Cmd escCmd = cmdFactory.create();
        escCmd.append(escCmd.getHeaderCmd());

        BarcodeSetting barcodeSetting = new BarcodeSetting();
        barcodeSetting.setBarcodeStringPosition(BarcodeStringPosition.BELOW_BARCODE);
        barcodeSetting.setEscBarcodePrintOritention(EscBarcodePrintOritention.Rotate0);
        barcodeSetting.setHeightInDot(96);//accept value:1~255
        barcodeSetting.setBarcodeWidth(3);//accept value:2~6
        barcodeSetting.setQrcodeDotSize(5);//accept value: Esc(1~15), Tsc(1~10)
        try {
            escCmd.append(escCmd.getBarcodeCmd(barcodeType, barcodeSetting, barcodeContent));
        } catch (SdkException e) {
            e.printStackTrace();
        }
        escCmd.append(escCmd.getLFCRCmd());
        escCmd.append(escCmd.getLFCRCmd());
        escCmd.append(escCmd.getLFCRCmd());
        escCmd.append(escCmd.getLFCRCmd());
        escCmd.append(escCmd.getLFCRCmd());
        escCmd.append(escCmd.getLFCRCmd());

        rtPrinter.writeMsgAsync(escCmd.getAppendCmds());
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.btn_print:
                try {
                    print();
                } catch (SdkException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

}
