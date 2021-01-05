package com.bibi.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.bibi.R;
import com.bibi.entity.CoinContract;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/7/31
 */
public class AssetAdapter extends RecyclerView.Adapter<AssetAdapter.MyViewHolder> {
    private Context context;
    private List<CoinContract> contents=new ArrayList<>();

    public AssetAdapter(Context context, List<CoinContract> contents) {
        this.context = context;
        this.contents = contents;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.adapter_asset_coin, viewGroup, false);
        return new MyViewHolder(view);
    }

    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
        final CoinContract content=contents.get(i);
        holder.tvCoinName.setText(content.getCoin().getName());
        holder.tvReleaseBalance.setText(new DecimalFormat("#0.00").format(content.getBalance())+"");
        holder.tvFrozenBalance.setText(new DecimalFormat("#0.00").format(content.getFrozenBalance())+"");
        holder.llRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lister.onClick(content);
            }
        });
    }

    @Override
    public int getItemCount() {
        return contents.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout llRoot;
        private TextView tvCoinName;
        private TextView tvReleaseBalance;
        private TextView tvFrozenBalance;

        public MyViewHolder(View itemView) {
            super(itemView);
            llRoot = itemView.findViewById(R.id.llRoot);
            tvCoinName = itemView.findViewById(R.id.tvCoinName);
            tvReleaseBalance = itemView.findViewById(R.id.tvReleaseBalance);
            tvFrozenBalance = itemView.findViewById(R.id.tvFrozenBalance);
        }
    }

    OnclickListener lister;

    public void setOnListener(OnclickListener lister) {
        this.lister = lister;
    }

    public interface OnclickListener {
        void onClick(CoinContract content);
    }
}
