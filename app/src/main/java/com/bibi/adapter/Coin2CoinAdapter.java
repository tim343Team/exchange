package com.bibi.adapter;

import android.content.Context;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.bibi.R;
import com.bibi.entity.HoldEntity;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/7/27
 */
public class Coin2CoinAdapter extends RecyclerView.Adapter<Coin2CoinAdapter.MyViewHolder> {
    private Context context;
    private Map<String, String> priceMap = new HashMap<>();
    private List<HoldEntity> items = new ArrayList<>();
//    private List<CountDownTimer> timerArray = new ArrayList<>();

    public Coin2CoinAdapter(Context context, List<HoldEntity> mCurrentData) {
        this.context = context;
        this.items = mCurrentData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.adapter_coin_2_coin, parent, false);
        return new Coin2CoinAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final HoldEntity item = items.get(position);
        holder.tvType.setText(item.getType().equals("LIMIT_PRICE") ? context.getResources().getString(R.string.LIMIT_PRICE) : context.getResources().getString(R.string.MARKET_PRICE));
        holder.tvDirection.setTag("loading");
        if (item.getDirection().equals("BUY")) {
            holder.tvDirection.setTextColor(context.getResources().getColor(R.color.color_00b274));
        } else {
            holder.tvDirection.setTextColor(context.getResources().getColor(R.color.color_f15057));
        }
        holder.tvDirection.setText(item.getDirection().equals("BUY") ? context.getResources().getString(R.string.text_buy) : context.getResources().getString(R.string.text_sale));
        holder.tvSymbol.setText(item.getSymbol());
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String time = simpleDate.format(item.getTime());
        holder.tvTime.setText(time);
        Glide.with(context).load(item.getCoinIcon()).placeholder(R.drawable.icon_bite)
                .into((ImageView) holder.item_home_img);
        holder.tvPrice.setText(item.getPrice() + "");
        holder.tvAmount.setText(new DecimalFormat("#0.00").format(item.getAmount()) + "");
        holder.tvTurnover.setText(new DecimalFormat("#0.0000").format(item.getTurnover()) + "");
        holder.tvFinish.setText(item.getTradedAmount()+"");
        holder.tvCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onclickListenerColse.close(item.getOrderId(),position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvDirection;
        private ImageView item_home_img;
        private TextView tvSymbol;
        private TextView tvTime;
        private TextView tvPrice;
        private TextView tvType;
        private TextView tvAmount;
        private TextView tvFinish;
        private TextView tvTurnover;
        private TextView tvCancle;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvDirection = itemView.findViewById(R.id.tvDirection);
            item_home_img = itemView.findViewById(R.id.item_home_img);
            tvSymbol = itemView.findViewById(R.id.tvSymbol);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvType = itemView.findViewById(R.id.tvType);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvFinish = itemView.findViewById(R.id.tvFinish);
            tvTurnover = itemView.findViewById(R.id.tvTurnover);
            tvCancle = itemView.findViewById(R.id.tvCancle);
        }
    }

    public void setPriceMap(Map<String, String> priceMap) {
        this.priceMap = priceMap;
    }

    OnclickListenerColse onclickListenerColse;

    public void setOnColseListener(OnclickListenerColse colse) {
        this.onclickListenerColse = colse;
    }

    public interface OnclickListenerColse {

        void close(String orderId,int position);
    }
}
