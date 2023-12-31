package kg.printer.kkm.view.old;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleCursorTreeAdapter;

import kg.printer.kkm.R;
import kg.printer.kkm.controllers.UIViewController;
import kg.printer.kkm.domains.BaseApplication;
import kg.printer.kkm.repositories.DatabaseDAO;
import kg.printer.kkm.services.FunctionsService;

import com.rt.printerlibrary.cmd.Cmd;
import com.rt.printerlibrary.cmd.EscFactory;
import com.rt.printerlibrary.enumerate.BmpPrintMode;
import com.rt.printerlibrary.enumerate.CommonEnum;
import com.rt.printerlibrary.exception.SdkException;
import com.rt.printerlibrary.factory.cmd.CmdFactory;
import com.rt.printerlibrary.printer.RTPrinter;
import com.rt.printerlibrary.setting.BitmapSetting;
import com.rt.printerlibrary.setting.CommonSetting;
import com.rt.printerlibrary.utils.BitmapConvertUtil;

import java.io.IOException;
import java.util.ArrayList;

public class ImagePrintActivity extends UIViewController.BaseAdapter implements View.OnClickListener {

    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 0xd0;
    private static final int ALBUM_IMAGE_ACTIVITY_REQUEST_CODE = 0xd1;

    private LinearLayout llUploadImage;
    private Button btn_print;
    private FrameLayout flContent;
    private ImageView ivImage;
    private EditText et_pic_width;

    @SuppressWarnings("rawtypes")
    private RTPrinter rtPrinter;
    private Uri imageUri;
    private Bitmap mBitmap;
    private final static String TAG = "ImagePrint";
    private int bmpPrintWidth = 40;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.old_activity_image_print);
        initView();
        addListener();
        init();
    }

    @Override
    public void initView() {
        llUploadImage = findViewById(R.id.ll_print_image_upload_image);
        btn_print = findViewById(R.id.print);
        flContent = findViewById(R.id.fl_print_image_content);
        ivImage = findViewById(R.id.iv_print_image_image);
        et_pic_width = findViewById(R.id.et_pic_width);
    }

    @Override
    public void init() {
        rtPrinter = BaseApplication.getInstance().getRtPrinter();
    }

    @Override
    public void addListener() {
        llUploadImage.setOnClickListener(this);
        btn_print.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.print:
                try {
                    print();
                    //printForInd(); // India Test
                } catch (SdkException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.ll_print_image_upload_image:
                showPictureModeChooseDialog();
                break;
            case R.id.fl_print_image_content:
                showPictureModeChooseDialog();
                break;
            default:
                break;
        }
    }

    private void print() throws SdkException {

        if (mBitmap == null) {
            showToast(R.string.tip_upload_image);
            return;
        }
        try {
            bmpPrintWidth = Integer.parseInt(et_pic_width.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (BaseApplication.getInstance().getCurrentCmdType() == DatabaseDAO.BaseEnums.CMD_ESC) {
            escPrint();
        }
    }

    private void escPrint() {

        new Thread(new Runnable() {
            @Override
            public void run() {

                showProgressDialog("Загрузка...");

                CmdFactory cmdFactory = new EscFactory();
                Cmd cmd = cmdFactory.create();
                cmd.append(cmd.getHeaderCmd());

                CommonSetting commonSetting = new CommonSetting();
                commonSetting.setAlign(CommonEnum.ALIGN_MIDDLE);
                cmd.append(cmd.getCommonSettingCmd(commonSetting));

                BitmapSetting bitmapSetting = new BitmapSetting();

                //bitmapSetting.setBmpPrintMode(BmpPrintMode.MODE_MULTI_COLOR);
                bitmapSetting.setBmpPrintMode(BmpPrintMode.MODE_SINGLE_COLOR);

                if (bmpPrintWidth > 72) {
                    bmpPrintWidth = 72;
                    runOnUiThread(new Runnable() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void run() {
                            et_pic_width.setText(bmpPrintWidth + "");
                        }
                    });
                }
                bitmapSetting.setBimtapLimitWidth(bmpPrintWidth * 8);
                try {
                    cmd.append(cmd.getBitmapCmd(bitmapSetting, mBitmap));
                } catch (SdkException e) {
                    e.printStackTrace();
                }
                cmd.append(cmd.getLFCRCmd());
                cmd.append(cmd.getLFCRCmd());
                cmd.append(cmd.getLFCRCmd());
                cmd.append(cmd.getLFCRCmd());
                cmd.append(cmd.getLFCRCmd());
                cmd.append(cmd.getLFCRCmd());
                if (rtPrinter != null) {
                    rtPrinter.writeMsg(cmd.getAppendCmds());
                }

                hideProgressDialog();
            }
        }).start();
    }

    private void showPictureModeChooseDialog() {
        final UIViewController.BasicListAdapter pictureModeChooseDialog = new UIViewController.BasicListAdapter();
        ArrayList<String> contentList = new ArrayList<>();
        contentList.add(getString(R.string.picture));
        contentList.add(getString(R.string.album));
        Bundle args = new Bundle();
        args.putString(UIViewController.BasicListAdapter.BUNDLE_KEY_TITLE, getString(R.string.choose_picture_position));
        args.putStringArrayList(UIViewController.BasicListAdapter.BUNDLE_KEY_CONTENT_LIST, contentList);
        pictureModeChooseDialog.setArguments(args);
        pictureModeChooseDialog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getAdapter().getItem(position).toString().equals(getString(R.string.picture))) {
                    takeAPicture();
                } else {
                    openAlbum();
                }
                pictureModeChooseDialog.dismiss();
            }
        });
        pictureModeChooseDialog.show(getFragmentManager(), null);
    }

    private void takeAPicture() {
        if (!FunctionsService.SavingMediaFileService.isExternalStorageWritable()) {
            showToast(R.string.insert_sdcard_tip);
            return;
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        imageUri = FunctionsService.SavingMediaFileService.getOutputMediaFileUri(getApplicationContext(), FunctionsService.SavingMediaFileService.DIR_CAPTURE, FunctionsService.SavingMediaFileService.MEDIA_TYPE_IMAGE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    private void openAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, ALBUM_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    showImage(imageUri);
                }
                SimpleCursorTreeAdapter adapter;
                break;
            case ALBUM_IMAGE_ACTIVITY_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    imageUri = data.getData();
                    showImage(imageUri);
                }
                break;
        }
    }

    private void showImage(Uri uri) {
        llUploadImage.setVisibility(View.GONE);
        flContent.setVisibility(View.VISIBLE);
        if (mBitmap != null) {
            mBitmap.recycle();
            mBitmap = null;
            System.gc();
        }
        try {
            mBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (BaseApplication.getInstance().getCurrentCmdType() == DatabaseDAO.BaseEnums.CMD_ESC) {
            if (mBitmap.getWidth() > 48 * 8) {
                mBitmap = BitmapConvertUtil.decodeSampledBitmapFromUri(ImagePrintActivity.this, uri, 48 * 8, 4000);
            }
        } else if (BaseApplication.getInstance().getCurrentCmdType() == DatabaseDAO.BaseEnums.CMD_PIN) {
            if (mBitmap.getWidth() > 210 * 8) {
                mBitmap = BitmapConvertUtil.decodeSampledBitmapFromUri(ImagePrintActivity.this, uri, 210 * 8, 4000);
            }
        } else {
            if (mBitmap.getWidth() > 72 * 8) {
                mBitmap = BitmapConvertUtil.decodeSampledBitmapFromUri(ImagePrintActivity.this, uri, 72 * 8, 4000);
            }
        }

        Log.d(TAG, "mBitmap getWidth = " + mBitmap.getWidth());
        Log.d(TAG, "mBitmap getHeight = " + mBitmap.getHeight());
        ivImage.setImageBitmap(mBitmap);
    }

}
