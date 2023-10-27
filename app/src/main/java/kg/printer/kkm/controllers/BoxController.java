package kg.printer.kkm.controllers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import kg.printer.kkm.R;
import kg.printer.kkm.domains.Product;

import java.util.ArrayList;

public class BoxController extends BaseAdapter {

    public Context ctx;
    public LayoutInflater lInflater;
    public ArrayList<Product> objects;

    public BoxController(Context context, ArrayList<Product> products) {
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
            view = lInflater.inflate(R.layout.activity_g_item_good, parent, false);
        }

        Product p = getProduct(position);

        ((TextView) view.findViewById(R.id.tv_good)).setText(p.name);
        ((TextView) view.findViewById(R.id.tv_coast)).setText("цена: " + p.price);
        ((TextView) view.findViewById(R.id.tv_unit)).setText("ед.изм: " + p.unit);

        return view;
    }

    Product getProduct(int position) {
        return ((Product) getItem(position));
    }

}