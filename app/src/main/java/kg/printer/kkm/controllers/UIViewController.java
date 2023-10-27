package kg.printer.kkm.controllers;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import kg.printer.kkm.R;
import kg.printer.kkm.view.ProductActivity;

public class UIViewController {

    public abstract static class BaseAdapter extends AppCompatActivity {

        private ProgressDialog progressDialog;

        public abstract void initView();

        public abstract void addListener();

        public abstract void init();

        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        public void turnToActivity(Class<?> cls) {
            Intent i = new Intent(this, cls);
            startActivity(i);
        }

        public void turnToActivityWithPosition(Class<?> cls, int position_on_list, int new_element) {
            Intent intent = new Intent(this, cls);
            intent.putExtra("position_on_list", position_on_list);
            intent.putExtra("new_element", new_element);
            startActivity(intent);
        }

        public void turnToActivityWithUser(Class<?> cls, String position, String surname, String name, String secondName) {
            Intent intent = new Intent(this, cls);
            intent.putExtra("position", position);
            intent.putExtra("surname", surname);
            intent.putExtra("name", name);
            intent.putExtra("secondName", secondName);
            startActivity(intent);
        }

        public void showToast(String msg){
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        }

        public void showProgressDialog(final String str){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(progressDialog == null){
                        progressDialog = new ProgressDialog(BaseAdapter.this);
                    }
                    if(!TextUtils.isEmpty(str)){
                        progressDialog.setMessage(str);
                    }else{
                        progressDialog.setMessage("Загрузка...");
                    }
                    progressDialog.show();
                }
            });

        }

        public void hideProgressDialog(){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(progressDialog != null && progressDialog.isShowing()){
                        progressDialog.hide();
                    }
                }
            });
        }

    }

    public static class BarcodeAdapter extends ArrayAdapter {

        private Context mContext;
        private List<String> mItemList;
        private List<String> mTagList;
        private LayoutInflater mInflater;
        private int[] mColors;

        public BarcodeAdapter(Context context, List<String> itemList, List<String> tagList) {
            super(context,0,itemList);
            mContext = context;
            mItemList = itemList;
            mTagList = tagList;
            mInflater = LayoutInflater.from(mContext);
            mColors = mContext.getResources().getIntArray(R.array.indicator_color);
        }

        @Override
        public boolean isEnabled(int position) {
            if(mTagList.contains(getItem(position))){
                return false;
            }
            return super.isEnabled(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(mTagList.contains(getItem(position))){
                convertView = mInflater.inflate(R.layout.old_barcode_group_item,null);
                TextView tvIndicator = convertView.findViewById(R.id.tv_barcode_group_item_indicator);
                int colorIndex = mTagList.indexOf(getItem(position));
                tvIndicator.setBackgroundColor(mColors[colorIndex]);
            }else {
                convertView = mInflater.inflate(R.layout.old_barcode_child_item,null);
            }
            TextView textView = convertView.findViewById(R.id.tv_barcode_text);
            textView.setText(getItem(position).toString());
            return convertView;
        }

    }

    public static class BasicDialogAdapter extends android.widget.BaseAdapter {

        private Context mContext;
        private List<String> mList;
        private LayoutInflater mInflater;

        public BasicDialogAdapter(Context context, List<String> list) {
            this.mContext = context;
            this.mList = list;
            mInflater = LayoutInflater.from(mContext);
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        private class ViewHolder{
            TextView tvText;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView == null){
                convertView = mInflater.inflate(R.layout.old_basic_dialog_item,null);

                holder = new ViewHolder();
                holder.tvText = convertView.findViewById(R.id.tv_basic_dialog_item_text);
                convertView.setTag(holder);
            }else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tvText.setText(mList.get(position));
            return convertView;
        }

    }

    public static class BasicListAdapter extends DialogFragment {

        public static final String BUNDLE_KEY_TITLE = "title";
        public static final String BUNDLE_KEY_CONTENT_LIST = "list";

        private Context mContext;
        private String mTitle;
        private List<String> mList;
        private TextView tvTitle;
        private ListView lvContent;
        private AdapterView.OnItemClickListener mListener;
        private BasicDialogAdapter mAdapter;

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            mContext = activity;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            Bundle args = getArguments();
            mTitle = args.getString(BUNDLE_KEY_TITLE);
            mList = args.getStringArrayList(BUNDLE_KEY_CONTENT_LIST);
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View view = inflater.inflate(R.layout.old_dialog_list_basic, null);
            initView(view);
            setListener();
            setAdapter();
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setView(view).setCancelable(true).setNegativeButton("Отмена", null);
            return builder.create();
        }

        private void initView(View view) {
            tvTitle = view.findViewById(R.id.tv_dialog_list_basic_title);
            lvContent = view.findViewById(R.id.lv_dialog_list_basic_content);
            tvTitle.setText(mTitle);
        }

        private void setListener() {
            lvContent.setOnItemClickListener(mListener);
        }

        private void setAdapter() {
            mAdapter = new BasicDialogAdapter(mContext,mList);
            lvContent.setAdapter(mAdapter);
        }

        public void setOnItemClickListener(AdapterView.OnItemClickListener listener){
            mListener = listener;
        }

    }

    public static class BoxAdapter extends android.widget.BaseAdapter {

        public Context ctx;
        public LayoutInflater lInflater;
        public ArrayList<ProductActivity.Product> objects;

        public BoxAdapter(Context context, ArrayList<ProductActivity.Product> products) {
            ctx = context;
            objects = products;
            lInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return objects.size();
        }

        @Override
        public Object getItem(int position) {
            return objects.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                view = lInflater.inflate(R.layout.activity_product_box, parent, false);
            }

            ProductActivity.Product p = getProduct(position);

            ((TextView) view.findViewById(R.id.tv_good)).setText(p.name);
            ((TextView) view.findViewById(R.id.tv_coast)).setText("цена: " + p.price);
            ((TextView) view.findViewById(R.id.tv_unit)).setText("ед.изм: " + p.unit);

            return view;
        }

        ProductActivity.Product getProduct(int position) {
            return ((ProductActivity.Product) getItem(position));
        }

    }

    public static class FlowRadioGroupAdapter extends RadioGroup {

        public FlowRadioGroupAdapter(Context context) {
            super(context);
        }

        public FlowRadioGroupAdapter(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            int maxWidth = MeasureSpec.getSize(widthMeasureSpec);
            int childCount = getChildCount();
            int x = 0;
            int y = 0;
            int row = 0;

            for (int index = 0; index < childCount; index++) {
                final View child = getChildAt(index);
                if (child.getVisibility() != View.GONE) {
                    child.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);

                    int width = child.getMeasuredWidth();
                    int height = child.getMeasuredHeight();
                    x += width;
                    y = row * height + height;
                    if (x > maxWidth) {
                        x = width;
                        row++;
                        y = row * height + height;
                    }
                }
            }

            setMeasuredDimension(maxWidth, y);
        }

        @Override
        protected void onLayout(boolean changed, int l, int t, int r, int b) {
            final int childCount = getChildCount();
            int maxWidth = r - l;
            int x = 0;
            int y = 0;
            int row = 0;
            for (int i = 0; i < childCount; i++) {
                final View child = this.getChildAt(i);
                if (child.getVisibility() != View.GONE) {
                    int width = child.getMeasuredWidth();
                    int height = child.getMeasuredHeight();
                    x += width;
                    y = row * height + height;
                    if (x > maxWidth) {
                        x = width;
                        row++;
                        y = row * height + height;
                    }
                    child.layout(x - width, y - height, x, y);
                }
            }
        }

    }

    public static class ScrollEditTextAdapter extends android.support.v7.widget.AppCompatEditText {

        private GestureDetector detector;

        public ScrollEditTextAdapter(Context context, AttributeSet attrs) {
            super(context, attrs);
            detector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {

                @Override
                public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                    super.onScroll(e1, e2, distanceX, distanceY);
                    boolean clampedY = false;
                    if (computeVerticalScrollOffset() == 0
                            && distanceY < 0) {
                        clampedY = true;
                    }

                    int deltaY = computeVerticalScrollRange() - computeVerticalScrollExtent();
                    if ((computeVerticalScrollOffset() == deltaY || deltaY < 0)
                            && distanceY > 0) {
                        clampedY = true;
                    }
                    if (clampedY) {
                        getParent().requestDisallowInterceptTouchEvent(false);
                    }
                    return true;
                }
            });
        }

        @Override
        public boolean dispatchTouchEvent(MotionEvent event) {
            getParent().requestDisallowInterceptTouchEvent(true);
            return super.dispatchTouchEvent(event);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            return super.onTouchEvent(event) | detector.onTouchEvent(event);
        }

    }

    public static class ToastAdapter {

        private ToastAdapter() {
            throw new UnsupportedOperationException("cannot be instantiated");
        }

        public static void show(Context context, String msg) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        }

        public static void show(Context context, int msgResId) {
            Toast.makeText(context, msgResId, Toast.LENGTH_SHORT).show();
        }

        public static void showLong(Context context, String msg) {
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
        }

    }

    public static class ViewHolderAdapter {

        @SuppressWarnings("unchecked")
        public static <T extends View> T get(View view, int id) {
            SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();
            if (viewHolder == null) {
                viewHolder = new SparseArray<>();
                view.setTag(viewHolder);
            }
            View childView = viewHolder.get(id);
            if (childView == null) {
                childView = view.findViewById(id);
                viewHolder.put(id, childView);
            }
            return (T) childView;
        }

    }

    public static class WIFIListAdapter extends android.widget.BaseAdapter {

        private Context mContext;
        private List<ScanResult> mScanResults;

        public WIFIListAdapter(Context context, List<ScanResult> scanResults) {
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
                        .inflate(R.layout.old_wifi_listview_item, null);
            }
            TextView wifi_set_wifiName = ViewHolderAdapter.get(convertView,
                    R.id.wifi_set_wifiName);
            ImageView wifi_set_wifiLevel = ViewHolderAdapter.get(convertView,
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

}
