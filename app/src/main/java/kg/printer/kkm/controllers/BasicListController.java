package kg.printer.kkm.controllers;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import kg.printer.kkm.R;

import java.util.List;

public class BasicListController extends DialogFragment {

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
        View view = inflater.inflate(R.layout.dialog_list_basic, null);
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
