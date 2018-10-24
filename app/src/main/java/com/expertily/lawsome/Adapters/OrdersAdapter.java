package com.expertily.lawsome.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.expertily.lawsome.R;

import java.util.ArrayList;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder> {

    private Context context;
    private ArrayList<String> ids;
    private ArrayList<String> dates;

    public OrdersAdapter(Context act, ArrayList<String> ids, ArrayList<String> dates) {
        this.context = act;
        this.ids = ids;
        this.dates = dates;
    }


    @Override
    public OrdersAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.order_row, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final OrdersAdapter.ViewHolder viewHolder, int i) {
        viewHolder.orderid.setText("Daily Order " + ids.get(i));
        viewHolder.orderdate.setText("Order on Date: " + dates.get(i));
    }

    @Override
    public int getItemCount() {
        return ids.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView orderid;
        private TextView orderdate;

        public ViewHolder(View view) {
            super(view);
            orderid = view.findViewById(R.id.orderid);
            orderdate = view.findViewById(R.id.orderdate);
        }
    }

}