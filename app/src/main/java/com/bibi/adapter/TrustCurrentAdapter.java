package com.bibi.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.bibi.R;
import com.bibi.entity.Currency;
import com.bibi.entity.HoldEntity;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/4/18
 */
public class TrustCurrentAdapter extends RecyclerView.Adapter<TrustCurrentAdapter.MyViewHolder> {
    public static final int NOTIFY_IV = 10088;
    private Context context;
    private List<Currency> currencies = new ArrayList<>();
    private List<HoldEntity> items = new ArrayList<>();

    public List<Currency> getCurrencies() {
        return currencies;
    }

    public void setCurrencies(List<Currency> currencies) {
        this.currencies = currencies;
    }

    public void setItems(List<HoldEntity> items) {
        this.items = items;
    }

    public TrustCurrentAdapter(Context context, List<Currency> currencies,List<HoldEntity> mCurrentData) {
        this.context = context;
        this.currencies = currencies;
        this.items=mCurrentData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.adapter_trust_layout_new, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final HoldEntity item = items.get(position);
            holder.tvDirection.setTag("loading");
            if (item.getDirection().equals("BUY")) {
                holder.tvDirection.setTextColor(context.getResources().getColor(R.color.color_00b274));
            } else {
                holder.tvDirection.setTextColor(context.getResources().getColor(R.color.color_f15057));
            }
            holder.tvDirection.setText(item.getDirection().equals("BUY") ? context.getResources().getString(R.string.text_duo):context.getResources().getString(R.string.text_kong) );
            holder.tvSymbol.setText(item.getSymbol());
            SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            String time = simpleDate.format(item.getTime());
            holder.tvTime.setText(time);
            if (item.getFollowId() == 0) {
                holder.tvType.setText(context.getResources().getString(R.string.nature));
            } else {
                holder.tvType.setText(context.getResources().getString(R.string.auto));
            }
            Glide.with(context).load(item.getCoinIcon()).placeholder(R.drawable.icon_bite)
                    .into((ImageView) holder.item_home_img);
            holder.tvPrice.setText(item.getPrice() + "");
            for (Currency currencie : currencies) {
                if (currencie.getSymbol().equals(item.getSymbol())) {
                    String format = new DecimalFormat("#0.0000").format(currencie.getClose());
                    BigDecimal bg = new BigDecimal(format);
                    String v = bg.setScale(8, BigDecimal.ROUND_DOWN).stripTrailingZeros().toPlainString();
                    holder.tvCurrentPrice.setText(v);
                    break;
                }
            }
            holder.tvAmount.setText(new DecimalFormat("#0.00").format(item.getAmount()) + "");
            holder.tvMarginMoney.setText(new DecimalFormat("#0.00").format(item.getMarginMoney()) + "");
            holder.tvOrderFee.setText(new DecimalFormat("#0.00").format(item.getOrderFee()) + "");
            holder.tvOvernightMoney.setText(new DecimalFormat("#0.00").format(item.getOvernightMoney()) + "");
            holder.tvStopProfitPrice.setText(item.getStopProfitPrice() + "");
            holder.tvStopLossPrice.setText(item.getStopLossPrice() + "");
            holder.tvLeverage.setText(item.getLeverage() + "");
            if (item.getProfitLost() > 0) {
                holder.tvProfitLost.setBackgroundResource(R.drawable.circle_corner_green_back);
            } else {
                holder.tvProfitLost.setBackgroundResource(R.drawable.circle_corner_red_back);
            }
            holder.tvProfitLost.setText(context.getResources().getString(R.string.Profit_loss) + new DecimalFormat("#0.00").format(item.getProfitLost()) + "");
            holder.tvCloseOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onclickListenerColse.close(item.getOrderId());
                }
            });
            holder.tvModify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onclickListenerModify.modify(item.getOrderId(), item.getStopProfitPrice() + "", item.getStopLossPrice() + "");
                }
            });
    }


    OnclickListenerColse onclickListenerColse;

    OnclickListenerModify onclickListenerModify;

    public void setOnColseListener(OnclickListenerColse colse) {
        this.onclickListenerColse = colse;
    }

    public void setOnModifyListener(OnclickListenerModify modify) {
        this.onclickListenerModify = modify;
    }

    public interface OnclickListenerColse {

        void close(String orderId);
    }

    public interface OnclickListenerModify {

        void modify(String orderId, String stopProfitPrice, String stopLossPrice);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvDirection;
        private TextView tvSymbol;
        private TextView tvTime;
        private TextView tvType;
        private ImageView item_home_img;
        private TextView tvPrice;
        private TextView tvCurrentPrice;
        private TextView tvAmount;
        private TextView tvMarginMoney;
        private TextView tvOrderFee;
        private TextView tvOvernightMoney;
        private TextView tvStopProfitPrice;
        private TextView tvStopLossPrice;
        private TextView tvProfitLost;
        private TextView tvCloseOrder;
        private TextView tvLeverage;
        private TextView tvModify;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvDirection = itemView.findViewById(R.id.tvDirection);
            tvSymbol = itemView.findViewById(R.id.tvSymbol);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvType = itemView.findViewById(R.id.tvType);
            item_home_img = itemView.findViewById(R.id.item_home_img);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvCurrentPrice = itemView.findViewById(R.id.tvCurrentPrice);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvMarginMoney = itemView.findViewById(R.id.tvMarginMoney);
            tvOrderFee = itemView.findViewById(R.id.tvOrderFee);
            tvOvernightMoney = itemView.findViewById(R.id.tvOvernightMoney);
            tvStopProfitPrice = itemView.findViewById(R.id.tvStopProfitPrice);
            tvStopLossPrice = itemView.findViewById(R.id.tvStopLossPrice);
            tvProfitLost = itemView.findViewById(R.id.tvProfitLost);
            tvCloseOrder = itemView.findViewById(R.id.tvCloseOrder);
            tvLeverage = itemView.findViewById(R.id.tvLeverage);
            tvModify = itemView.findViewById(R.id.tvModify);
        }


    }
}
