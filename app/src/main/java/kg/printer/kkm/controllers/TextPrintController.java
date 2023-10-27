package kg.printer.kkm.controllers;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;

import kg.printer.kkm.R;
import kg.printer.kkm.domains.BaseApplication;
import kg.printer.kkm.repositories.BaseEnumsDAO;
import com.rt.printerlibrary.cmd.Cmd;
import com.rt.printerlibrary.cmd.EscFactory;
import com.rt.printerlibrary.enumerate.CommonEnum;
import com.rt.printerlibrary.enumerate.SettingEnum;
import com.rt.printerlibrary.factory.cmd.CmdFactory;
import com.rt.printerlibrary.printer.RTPrinter;
import com.rt.printerlibrary.setting.TextSetting;

import java.io.UnsupportedEncodingException;

public class TextPrintController extends BaseController implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, RadioGroup.OnCheckedChangeListener {

    private EditText et_text;
    private Button btn_txtprint;
    private CheckBox ck_smallfont, ck_anti_white, ck_double_width, ck_double_height, ck_bold, ck_underline;
    private RadioGroup rg_align_group;

    private RTPrinter rtPrinter;
    private TextSetting textSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_print);

        initView();
        addListener();
        init();
    }

    @Override
    public void initView() {
        et_text = findViewById(R.id.et_text);
        btn_txtprint = findViewById(R.id.btn_txtprint);
        ck_smallfont = findViewById(R.id.ck_smallfont);
        ck_anti_white = findViewById(R.id.ck_anti_white);
        ck_double_width = findViewById(R.id.ck_double_width);
        ck_double_height = findViewById(R.id.ck_double_height);
        ck_bold = findViewById(R.id.ck_bold);
        ck_underline = findViewById(R.id.ck_underline);
        rg_align_group = findViewById(R.id.rg_align_group);
    }

    @Override
    public void addListener() {
        btn_txtprint.setOnClickListener(this);

        ck_smallfont.setOnCheckedChangeListener(this);
        ck_anti_white.setOnCheckedChangeListener(this);
        ck_double_width.setOnCheckedChangeListener(this);
        ck_double_height.setOnCheckedChangeListener(this);
        ck_bold.setOnCheckedChangeListener(this);
        ck_underline.setOnCheckedChangeListener(this);
        rg_align_group.setOnCheckedChangeListener(this);
    }

    @Override
    public void init() {
        rtPrinter = BaseApplication.getInstance().getRtPrinter();
        textSetting = new TextSetting();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_txtprint:
                try {
                    textPrint();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    private void textPrint() throws UnsupportedEncodingException {
        switch (BaseApplication.getInstance().getCurrentCmdType()) {
            case BaseEnumsDAO.CMD_ESC:
                if (rtPrinter != null) {
                    // get text and charset name
                    String text = et_text.getText().toString();

                    // check
                    if (text.isEmpty()) return;

                    CmdFactory escFac = new EscFactory();
                    Cmd escCmd = escFac.create();
                    escCmd.setChartsetName("GBK");

                    textSetting.setAlign(textSetting.getAlign());
                    textSetting.setBold(textSetting.getBold());
                    textSetting.setUnderline(textSetting.getUnderline());
                    textSetting.setIsAntiWhite(textSetting.getIsAntiWhite());
                    textSetting.setDoubleHeight(textSetting.getDoubleHeight());
                    textSetting.setDoubleWidth(textSetting.getDoubleWidth());
                    textSetting.setItalic(textSetting.getItalic());
                    textSetting.setIsEscSmallCharactor(textSetting.getIsEscSmallCharactor());

                    escCmd.append(escCmd.getHeaderCmd());
                    escCmd.append(escCmd.getTextCmd(textSetting, text));
                    escCmd.append(escCmd.getLFCRCmd());

                    rtPrinter.writeMsg(escCmd.getAppendCmds());
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isEnable) {
        if (compoundButton == ck_smallfont) {
            if (isEnable) {
                textSetting.setIsEscSmallCharactor(SettingEnum.Enable);
            } else {
                textSetting.setIsEscSmallCharactor(SettingEnum.Disable);
            }
        }
        if (compoundButton == ck_anti_white) {
            if (isEnable) {
                textSetting.setIsAntiWhite(SettingEnum.Enable);
            } else {
                textSetting.setIsAntiWhite(SettingEnum.Disable);
            }
        }
        if (compoundButton == ck_double_width) {
            if (isEnable) {
                textSetting.setDoubleWidth(SettingEnum.Enable);
            } else {
                textSetting.setDoubleWidth(SettingEnum.Disable);
            }
        }
        if (compoundButton == ck_double_height) {
            if (isEnable) {
                textSetting.setDoubleHeight(SettingEnum.Enable);
            } else {
                textSetting.setDoubleHeight(SettingEnum.Disable);
            }
        }
        if (compoundButton == ck_bold) {
            if (isEnable) {
                textSetting.setBold(SettingEnum.Enable);
            } else {
                textSetting.setBold(SettingEnum.Disable);
            }
        }
        if (compoundButton == ck_underline) {
            if (isEnable) {
                textSetting.setUnderline(SettingEnum.Enable);
            } else {
                textSetting.setUnderline(SettingEnum.Disable);
            }
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (i) {
            case R.id.rb_align_left:
                textSetting.setAlign(CommonEnum.ALIGN_LEFT);
                break;
            case R.id.rb_align_middle:
                textSetting.setAlign(CommonEnum.ALIGN_MIDDLE);
                break;
            case R.id.rb_align_right:
                textSetting.setAlign(CommonEnum.ALIGN_RIGHT);
                break;
            default:
                break;
        }
    }

}