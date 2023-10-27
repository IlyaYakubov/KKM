package kg.printer.kkm.view.old;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;

import kg.printer.kkm.R;
import kg.printer.kkm.controllers.UIViewController;
import kg.printer.kkm.domains.BaseApplication;
import kg.printer.kkm.repositories.DatabaseDAO;

import com.rt.printerlibrary.cmd.Cmd;
import com.rt.printerlibrary.cmd.EscFactory;
import com.rt.printerlibrary.enumerate.BarcodeStringPosition;
import com.rt.printerlibrary.enumerate.BarcodeType;
import com.rt.printerlibrary.enumerate.CommonEnum;
import com.rt.printerlibrary.enumerate.EscBarcodePrintOritention;
import com.rt.printerlibrary.enumerate.QrcodeEccLevel;
import com.rt.printerlibrary.enumerate.SettingEnum;
import com.rt.printerlibrary.exception.SdkException;
import com.rt.printerlibrary.factory.cmd.CmdFactory;
import com.rt.printerlibrary.printer.RTPrinter;
import com.rt.printerlibrary.setting.BarcodeSetting;
import com.rt.printerlibrary.setting.BitmapSetting;
import com.rt.printerlibrary.setting.CommonSetting;
import com.rt.printerlibrary.setting.TextSetting;

import java.io.UnsupportedEncodingException;

public class TemplatePrintActivity extends UIViewController.BaseAdapter {

    private RTPrinter rtPrinter;
    private Bitmap bmp;
    private String title, content_email, barcode_str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.old_activity_template_print);

        initView();
        addListener();
        init();
    }

    @Override
    public void initView() {
        title = getString(R.string.temp1_title1_printer_tech);
        content_email = getString(R.string.temp1_content2_email);
        barcode_str = "123456789";
    }

    @Override
    public void addListener() {

    }

    @Override
    public void init() {
        rtPrinter = BaseApplication.getInstance().getRtPrinter();
    }

    public void onBtnClick(View v) {
        switch (v.getId()) {
            case R.id.btn_template1:
                try {
                    printTemplet();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (SdkException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_template2:
                //printTempletIndiaTest();
                escTicketTemplet();
                break;
            default:
                break;
        }
    }

    private void printTempletIndiaTest() {
        switch (BaseApplication.getInstance().getCurrentCmdType()) {
            case DatabaseDAO.BaseEnums.CMD_ESC:
                escPrintIndiaTest();
                break;
            case DatabaseDAO.BaseEnums.CMD_TSC:
                break;
            case DatabaseDAO.BaseEnums.CMD_CPCL:
                break;
            case DatabaseDAO.BaseEnums.CMD_ZPL:
                break;
            case DatabaseDAO.BaseEnums.CMD_PIN:
                break;
            default:
                break;
        }
    }

    private void escPrintIndiaTest() {
        if (rtPrinter == null) {
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    escTempletIndiaTest();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (SdkException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void printTemplet() throws UnsupportedEncodingException, SdkException {
        switch (BaseApplication.getInstance().getCurrentCmdType()) {
            case DatabaseDAO.BaseEnums.CMD_ESC:
                escPrint();
                break;
            default:
                break;
        }
    }

    private void escPrint() throws UnsupportedEncodingException, SdkException {
        if (rtPrinter == null) {
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    escTemplet();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (SdkException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    private void escTemplet() throws UnsupportedEncodingException, SdkException {
        CmdFactory escFac = new EscFactory();
        Cmd escCmd = escFac.create();
        escCmd.setChartsetName("UTF-8");
        TextSetting textSetting = new TextSetting();
        textSetting.setAlign(CommonEnum.ALIGN_MIDDLE);
        textSetting.setBold(SettingEnum.Enable);
        textSetting.setUnderline(SettingEnum.Disable);
        textSetting.setIsAntiWhite(SettingEnum.Disable);
        textSetting.setDoubleHeight(SettingEnum.Enable);
        textSetting.setDoubleWidth(SettingEnum.Enable);
        textSetting.setItalic(SettingEnum.Disable);
        textSetting.setIsEscSmallCharactor(SettingEnum.Disable);
        escCmd.append(escCmd.getHeaderCmd());
        escCmd.append(escCmd.getTextCmd(textSetting, title));
        escCmd.append(escCmd.getLFCRCmd());

        textSetting.setIsEscSmallCharactor(SettingEnum.Enable);
        textSetting.setBold(SettingEnum.Disable);
        textSetting.setDoubleHeight(SettingEnum.Disable);
        textSetting.setDoubleWidth(SettingEnum.Disable);

        escCmd.append(escCmd.getLFCRCmd());
        textSetting.setUnderline(SettingEnum.Enable);
        escCmd.append(escCmd.getTextCmd(textSetting, content_email));

        escCmd.append(escCmd.getLFCRCmd());
        escCmd.append(escCmd.getLFCRCmd());

        BitmapSetting bitmapSetting = new BitmapSetting();
        bitmapSetting.setBimtapLimitWidth(40);
        if (bmp == null) {
            bmp = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        }
        escCmd.append(escCmd.getBitmapCmd(bitmapSetting, Bitmap.createBitmap(bmp)));
        escCmd.append(escCmd.getLFCRCmd());

        BarcodeSetting barcodeSetting = new BarcodeSetting();
        barcodeSetting.setEscBarcodePrintOritention(EscBarcodePrintOritention.Rotate0);
        barcodeSetting.setBarcodeStringPosition(BarcodeStringPosition.BELOW_BARCODE);
        barcodeSetting.setHeightInDot(72);//accept value:1~255
        barcodeSetting.setBarcodeWidth(3);//accept value:2~6
        escCmd.append(escCmd.getBarcodeCmd(BarcodeType.CODE128, barcodeSetting, barcode_str));
        escCmd.append(escCmd.getLFCRCmd());

        barcodeSetting.setQrcodeDotSize(5);//accept value: Esc(1~15), Tsc(1~10)
        barcodeSetting.setQrcodeEccLevel(QrcodeEccLevel.L);
        escCmd.append(escCmd.getBarcodeCmd(BarcodeType.QR_CODE, barcodeSetting, content_email));

        escCmd.append(escCmd.getLFCRCmd());
        escCmd.append(escCmd.getLFCRCmd());
        escCmd.append(escCmd.getLFCRCmd());

        rtPrinter.writeMsg(escCmd.getAppendCmds());
    }

    private void escTempletIndiaTest() throws UnsupportedEncodingException, SdkException {
        CmdFactory escFac = new EscFactory();
        Cmd escCmd = escFac.create();
        escCmd.append(escCmd.getHeaderCmd());
        escCmd.setChartsetName("UTF-8");

        CommonSetting commonSetting = new CommonSetting();
        commonSetting.setAlign(CommonEnum.ALIGN_MIDDLE);
        escCmd.append(escCmd.getCommonSettingCmd(commonSetting));

        BitmapSetting bitmapSetting = new BitmapSetting();
        bitmapSetting.setBimtapLimitWidth(48 * 8);
        if (bmp == null) {
            bmp = BitmapFactory.decodeResource(getResources(), R.drawable.bill_bmptest);
        }
        escCmd.append(escCmd.getBitmapCmd(bitmapSetting, Bitmap.createBitmap(bmp)));
        escCmd.append(escCmd.getLFCRCmd());


        TextSetting textSetting = new TextSetting();
        textSetting.setAlign(CommonEnum.ALIGN_MIDDLE);
        textSetting.setBold(SettingEnum.Enable);
        textSetting.setUnderline(SettingEnum.Disable);
        textSetting.setIsAntiWhite(SettingEnum.Disable);
        textSetting.setDoubleHeight(SettingEnum.Enable);
        textSetting.setDoubleWidth(SettingEnum.Enable);
        textSetting.setItalic(SettingEnum.Disable);
        textSetting.setIsEscSmallCharactor(SettingEnum.Disable);


        escCmd.append(escCmd.getTextCmd(textSetting, "India Test 1"));
        escCmd.append(escCmd.getLFCRCmd());

        textSetting.setIsEscSmallCharactor(SettingEnum.Enable);
        textSetting.setBold(SettingEnum.Disable);
        textSetting.setDoubleHeight(SettingEnum.Disable);
        textSetting.setDoubleWidth(SettingEnum.Disable);
        escCmd.append(escCmd.getTextCmd(textSetting, "India Test 2"));

        escCmd.append(escCmd.getLFCRCmd());
        textSetting.setUnderline(SettingEnum.Enable);
        escCmd.append(escCmd.getTextCmd(textSetting, "India Test 3"));

        escCmd.append(escCmd.getLFCRCmd());
        escCmd.append(escCmd.getLFCRCmd());

        BarcodeSetting barcodeSetting = new BarcodeSetting();
        barcodeSetting.setEscBarcodePrintOritention(EscBarcodePrintOritention.Rotate0);
        barcodeSetting.setBarcodeStringPosition(BarcodeStringPosition.BELOW_BARCODE);
        barcodeSetting.setHeightInDot(72);//accept value:1~255
        barcodeSetting.setBarcodeWidth(3);//accept value:2~6
        escCmd.append(escCmd.getBarcodeCmd(BarcodeType.CODE128, barcodeSetting, barcode_str));
        escCmd.append(escCmd.getLFCRCmd());

        barcodeSetting.setQrcodeDotSize(5);//accept value: Esc(1~15), Tsc(1~10)
        barcodeSetting.setQrcodeEccLevel(QrcodeEccLevel.L);
        escCmd.append(escCmd.getBarcodeCmd(BarcodeType.QR_CODE, barcodeSetting, content_email));

        escCmd.append(escCmd.getLFCRCmd());
        escCmd.append(escCmd.getLFCRCmd());
        escCmd.append(escCmd.getLFCRCmd());

        rtPrinter.writeMsg(escCmd.getAppendCmds());
    }

    private void escTicketTemplet() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    CmdFactory escFac = new EscFactory();
                    Cmd escCmd = escFac.create();
                    escCmd.append(escCmd.getHeaderCmd());
                    escCmd.setChartsetName("UTF-8");

                    CommonSetting commonSetting = new CommonSetting();
                    commonSetting.setAlign(CommonEnum.ALIGN_MIDDLE);
                    escCmd.append(escCmd.getCommonSettingCmd(commonSetting));

                    BitmapSetting bitmapSetting = new BitmapSetting();
                    bitmapSetting.setBimtapLimitWidth(28 * 8);
                    if (bmp == null) {
                        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.camera);
                    }
                    try {
                        escCmd.append(escCmd.getBitmapCmd(bitmapSetting, Bitmap.createBitmap(bmp)));
                    } catch (SdkException e) {
                        e.printStackTrace();
                    }
                    escCmd.append(escCmd.getLFCRCmd());

                    TextSetting textSetting = new TextSetting();
                    textSetting.setAlign(CommonEnum.ALIGN_MIDDLE);
                    textSetting.setBold(SettingEnum.Enable);
                    textSetting.setUnderline(SettingEnum.Disable);
                    textSetting.setIsAntiWhite(SettingEnum.Disable);
                    textSetting.setDoubleHeight(SettingEnum.Enable);
                    textSetting.setDoubleWidth(SettingEnum.Enable);
                    textSetting.setItalic(SettingEnum.Disable);
                    textSetting.setIsEscSmallCharactor(SettingEnum.Disable);

                    escCmd.append(escCmd.getTextCmd(textSetting, "The Red Rose"));
                    escCmd.append(escCmd.getLFCRCmd());
                    textSetting.setBold(SettingEnum.Disable);
                    textSetting.setDoubleHeight(SettingEnum.Disable);
                    textSetting.setDoubleWidth(SettingEnum.Disable);
                    textSetting.setIsEscSmallCharactor(SettingEnum.Enable);
                    escCmd.append(escCmd.getTextCmd(textSetting, "Indian Resturant"));
                    escCmd.append(escCmd.getLFCRCmd());
                    escCmd.append(escCmd.getTextCmd(textSetting, "Noida, Noida"));
                    escCmd.append(escCmd.getLFCRCmd());
                    escCmd.append(escCmd.getTextCmd(textSetting, "Website:http://www.xxx.com"));
                    escCmd.append(escCmd.getLFCRCmd());
                    escCmd.append(escCmd.getTextCmd(textSetting, "GSTIN No.:ROS2345ST"));
                    escCmd.append(escCmd.getLFCRCmd());
                    String line = "—————————————————————————————————————————";
                    textSetting.setBold(SettingEnum.Enable);
                    escCmd.append(escCmd.getTextCmd(textSetting, line));
                    escCmd.append(escCmd.getLFCRCmd());
                    textSetting.setBold(SettingEnum.Disable);
                    String bank = "                          ";
                    escCmd.append(escCmd.getTextCmd(textSetting, "Type:" + bank + "Table:2"));
                    escCmd.append(escCmd.getLFCRCmd());//回车换行
                    escCmd.append(escCmd.getTextCmd(textSetting, "Type:" + bank + "Table:2"));
                    escCmd.append(escCmd.getLFCRCmd());//回车换行
                    escCmd.append(escCmd.getTextCmd(textSetting, "Type:" + bank + "Table:2"));
                    escCmd.append(escCmd.getLFCRCmd());//回车换行
                    textSetting.setBold(SettingEnum.Enable);
                    escCmd.append(escCmd.getTextCmd(textSetting, line));
                    escCmd.append(escCmd.getLFCRCmd());

                    escCmd.append(escCmd.getTextCmd(textSetting, "Item Name      Qty       Rate      Amount\n"));

                    textSetting.setBold(SettingEnum.Enable);
                    escCmd.append(escCmd.getTextCmd(textSetting, line));
                    escCmd.append(escCmd.getLFCRCmd());

                    textSetting.setBold(SettingEnum.Disable);
                    escCmd.append(escCmd.getTextCmd(textSetting, "Item111        2.00      83.33    150.0000\n"));
                    escCmd.append(escCmd.getTextCmd(textSetting, "Item111        2.00      83.33    150.0000\n"));
                    escCmd.append(escCmd.getTextCmd(textSetting, "Item111        2.00      83.33    150.0000\n"));
                    escCmd.append(escCmd.getTextCmd(textSetting, "Item111        2.00      83.33    150.0000\n"));
                    escCmd.append(escCmd.getTextCmd(textSetting, "Item111        2.00      83.33    150.0000\n"));

                    textSetting.setBold(SettingEnum.Enable);
                    escCmd.append(escCmd.getTextCmd(textSetting, line));
                    escCmd.append(escCmd.getLFCRCmd());

                    textSetting.setBold(SettingEnum.Disable);
                    escCmd.append(escCmd.getTextCmd(textSetting, "            Sub Total             750.0000\n"));
                    escCmd.append(escCmd.getTextCmd(textSetting, "            @Oval                        0\n"));

                    textSetting.setBold(SettingEnum.Enable);
                    escCmd.append(escCmd.getTextCmd(textSetting, line));
                    escCmd.append(escCmd.getLFCRCmd());

                    textSetting.setIsEscSmallCharactor(SettingEnum.Disable);
                    textSetting.setBold(SettingEnum.Enable);
                    escCmd.append(escCmd.getTextCmd(textSetting, "     Net Amount         2524.98\n"));

                    textSetting.setBold(SettingEnum.Enable);
                    textSetting.setIsEscSmallCharactor(SettingEnum.Enable);
                    escCmd.append(escCmd.getTextCmd(textSetting, line));
                    escCmd.append(escCmd.getLFCRCmd());

                    textSetting.setBold(SettingEnum.Disable);
                    textSetting.setIsEscSmallCharactor(SettingEnum.Enable);//小字体
                    escCmd.append(escCmd.getTextCmd(textSetting, "KOT(s): KOT_23,KOT_24,KOT_31               \n"));
                    escCmd.append(escCmd.getTextCmd(textSetting, "Guest Signature:              ___________\n"));
                    escCmd.append(escCmd.getTextCmd(textSetting, "Authorised Signatory:         ___________\n"));
                    escCmd.append(escCmd.getTextCmd(textSetting, "Cashier:                                   \n"));

                    textSetting.setItalic(SettingEnum.Enable);
                    textSetting.setAlign(CommonEnum.ALIGN_MIDDLE);
                    textSetting.setIsEscSmallCharactor(SettingEnum.Disable);//小字体
                    escCmd.append(escCmd.getTextCmd(textSetting, "Have a nice day.\nThank you visit again"));

                    escCmd.append(escCmd.getLFCRCmd());
                    escCmd.append(escCmd.getLFCRCmd());
                    escCmd.append(escCmd.getLFCRCmd());
                    escCmd.append(escCmd.getLFCRCmd());
                    escCmd.append(escCmd.getLFCRCmd());

                    rtPrinter.writeMsg(escCmd.getAppendCmds());

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
