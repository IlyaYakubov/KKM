package kg.printer.kkm.controllers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import kg.printer.kkm.R;

import java.util.List;

public class BarcodeAdapter extends ArrayAdapter {

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
            convertView = mInflater.inflate(R.layout.barcode_group_item,null);
            TextView tvIndicator = convertView.findViewById(R.id.tv_barcode_group_item_indicator);
            int colorIndex = mTagList.indexOf(getItem(position));
            tvIndicator.setBackgroundColor(mColors[colorIndex]);
        }else {
            convertView = mInflater.inflate(R.layout.barcode_child_item,null);
        }
        TextView textView = convertView.findViewById(R.id.tv_barcode_text);
        textView.setText(getItem(position).toString());
        return convertView;
    }

}
