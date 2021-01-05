package com.bibi.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.bibi.R;
import com.bibi.entity.Currency;
import com.bibi.entity.HoldEntity;

import static com.bibi.ui.order.OrdersFragment.ENTRUST;
import static com.bibi.ui.order.OrdersFragment.FINISH;
import static com.bibi.ui.order.OrdersFragment.HOLD;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/3/25
 */
public class HoldAdapter extends BaseQuickAdapter<HoldEntity, BaseViewHolder> {
    private String type;
    private List<Currency> currencies;

    public HoldAdapter(int layoutResId, @Nullable List<HoldEntity> data, String type, List<Currency> currencies) {
//        super(R.layout.adapter_trust_layout_new, data);
        super(layoutResId, data);
        this.type = type;
        this.currencies = currencies;
    }

    public void setCurrencies(List<Currency> currencies) {
        this.currencies = currencies;
    }

    @Override
    protected void convert(BaseViewHolder helper, final HoldEntity item) {
        if (type.equals(HOLD)) {
            //持仓
            if (item.getDirection().equals("BUY")) {
                ((TextView) helper.getView(R.id.tvDirection)).setTextColor(mContext.getResources().getColor(R.color.color_00b274));
            } else {
                ((TextView) helper.getView(R.id.tvDirection)).setTextColor(mContext.getResources().getColor(R.color.color_f15057));
            }
            helper.setText(R.id.tvDirection, item.getDirection().equals("BUY") ? mContext.getResources().getString(R.string.text_duo) : mContext.getResources().getString(R.string.text_kong));
            helper.setText(R.id.tvSymbol, item.getSymbol());

            SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            String time = simpleDate.format(item.getTime());
            helper.setText(R.id.tvTime, time);
            if (item.getFollowId() == 0) {
                helper.setText(R.id.tvType, mContext.getResources().getString(R.string.nature));
            } else {
                helper.setText(R.id.tvType, mContext.getResources().getString(R.string.auto));
            }
            if (item.getUpdateTime() != null) {
                SimpleDateFormat simpleDate2 = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                String time2 = simpleDate2.format(item.getUpdateTime());
                helper.setText(R.id.tvCreatTime, time2);
            }

            if (item.getCompletedTime() != null) {
                SimpleDateFormat simpleDate3 = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                String time3 = simpleDate3.format(item.getCompletedTime());
                helper.setText(R.id.tvCloseTime, time3);
            }
            Glide.with(mContext).load(item.getCoinIcon()).placeholder(R.drawable.icon_bite)
                    .into((ImageView) helper.getView(R.id.item_home_img));
            helper.setText(R.id.tvPrice, item.getPrice() + "");
            for (Currency currencie : currencies) {
                if (currencie.getSymbol().equals(item.getSymbol())) {
                    String format = new DecimalFormat("#0.0000").format(currencie.getClose());
                    BigDecimal bg = new BigDecimal(format);
                    String v = bg.setScale(8, BigDecimal.ROUND_DOWN).stripTrailingZeros().toPlainString();
                    helper.setText(R.id.tvCurrentPrice, v);
                    break;
                }
            }
//            helper.setText(R.id.tvCurrentPrice, item.getCurrentPrice()+ "");
            helper.setText(R.id.tvAmount, new DecimalFormat("#0.00").format(item.getAmount()) + "");
            helper.setText(R.id.tvMarginMoney, new DecimalFormat("#0.00").format(item.getMarginMoney()) + "");
            helper.setText(R.id.tvOrderFee, new DecimalFormat("#0.00").format(item.getOrderFee()) + "");
            helper.setText(R.id.tvOvernightMoney, new DecimalFormat("#0.00").format(item.getOvernightMoney()) + "");
            helper.setText(R.id.tvStopProfitPrice, item.getStopProfitPrice() + "");
            helper.setText(R.id.tvStopLossPrice, item.getStopLossPrice() + "");
            helper.setText(R.id.tvLeverage, item.getLeverage() + "");
            if (item.getProfitLost() > 0) {
                helper.getView(R.id.tvProfitLost).setBackgroundResource(R.drawable.circle_corner_green_back);
            } else {
                helper.getView(R.id.tvProfitLost).setBackgroundResource(R.drawable.circle_corner_red_back);
            }
            helper.setText(R.id.tvProfitLost, mContext.getResources().getString(R.string.Profit_loss) + new DecimalFormat("#0.00").format(item.getProfitLost()) + "");
            ((TextView) helper.getView(R.id.tvCloseOrder)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onclickListenerColse.close(item.getOrderId());
                }
            });
            ((TextView) helper.getView(R.id.tvModify)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onclickListenerModify.modify(item.getOrderId(), item.getStopProfitPrice() + "", item.getStopLossPrice() + "");
                }
            });
        } else if (type.equals(ENTRUST)) {
            //委托
            if (item.getDirection().equals("BUY")) {
                ((TextView) helper.getView(R.id.tvDirection)).setTextColor(mContext.getResources().getColor(R.color.color_00b274));
            } else {
                ((TextView) helper.getView(R.id.tvDirection)).setTextColor(mContext.getResources().getColor(R.color.color_f15057));
            }
            helper.setText(R.id.tvDirection, item.getDirection().equals("BUY") ? mContext.getResources().getString(R.string.text_duo) : mContext.getResources().getString(R.string.text_kong));
            helper.setText(R.id.tvSymbol, item.getSymbol());

            SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            String time = simpleDate.format(item.getTime());
            helper.setText(R.id.tvTime, time);

            if (item.getUpdateTime() != null) {
                SimpleDateFormat simpleDate2 = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                String time2 = simpleDate2.format(item.getUpdateTime());
                helper.setText(R.id.tvCreatTime, time2);
            }

            if (item.getCompletedTime() != null) {
                SimpleDateFormat simpleDate3 = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                String time3 = simpleDate3.format(item.getCompletedTime());
                helper.setText(R.id.tvCloseTime, time3);
            }
            Glide.with(mContext).load(item.getCoinIcon()).placeholder(R.drawable.icon_bite)
                    .into((ImageView) helper.getView(R.id.item_home_img));
            if (item.getType().equals("MARKET_PRICE")) {
                helper.setText(R.id.tvPrice, "市价");
            } else {
                helper.setText(R.id.tvPrice, item.getPrice() + "");
            }
            for (Currency currencie : currencies) {
                if (currencie.getSymbol().equals(item.getSymbol())) {
                    String format = new DecimalFormat("#0.0000").format(currencie.getClose());
                    BigDecimal bg = new BigDecimal(format);
                    String v = bg.setScale(8, BigDecimal.ROUND_DOWN).stripTrailingZeros().toPlainString();
                    helper.setText(R.id.tvCurrentPrice, v);
                    break;
                }
            }
            helper.setText(R.id.tvAmount, new DecimalFormat("#0.00").format(item.getAmount()) + "");
            helper.setText(R.id.tvMarginMoney, new DecimalFormat("#0.00").format(item.getMarginMoney()) + "");
            helper.setText(R.id.tvOrderFee, new DecimalFormat("#0.00").format(item.getOrderFee()) + "");
            helper.setText(R.id.tvOvernightMoney, new DecimalFormat("#0.00").format(item.getOvernightMoney()) + "");
            helper.setText(R.id.tvStopProfitPrice, item.getStopProfitPrice() + "");
            helper.setText(R.id.tvStopLossPrice, item.getStopLossPrice() + "");
            helper.setText(R.id.tvLeverage, item.getLeverage() + "");
            if (item.getProfitLost() > 0) {
                helper.getView(R.id.tvProfitLost).setBackgroundResource(R.drawable.circle_corner_green_back);
            } else {
                helper.getView(R.id.tvProfitLost).setBackgroundResource(R.drawable.circle_corner_red_back);
            }
            helper.setText(R.id.tvProfitLost, mContext.getResources().getString(R.string.Profit_loss)  + new DecimalFormat("#0.00").format(item.getProfitLost()) + "");
            ((TextView) helper.getView(R.id.tvCloseOrder)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onclickListenerCancle.cancle(item.getOrderId());
                }
            });
            ((TextView) helper.getView(R.id.tvModify)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onclickListenerModify.modify(item.getOrderId(), item.getStopProfitPrice() + "", item.getStopLossPrice() + "");
                }
            });
        } else if (type.equals(FINISH)) {
            //成交
            if (item.getDirection().equals("BUY")) {
                ((TextView) helper.getView(R.id.tvDirection)).setTextColor(mContext.getResources().getColor(R.color.color_00b274));
            } else {
                ((TextView) helper.getView(R.id.tvDirection)).setTextColor(mContext.getResources().getColor(R.color.color_f15057));
            }
            helper.setText(R.id.tvDirection, item.getDirection().equals("BUY") ? mContext.getResources().getString(R.string.text_duo) : mContext.getResources().getString(R.string.text_kong));
            helper.setText(R.id.tvSymbol, item.getSymbol());

            SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            String time = simpleDate.format(item.getUpdateTime());
            helper.setText(R.id.tvTime, time);

            if (item.getUpdateTime() != null) {
                SimpleDateFormat simpleDate2 = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                String time2 = simpleDate2.format(item.getUpdateTime());
                helper.setText(R.id.tvCreatTime, time2);
            }

            if (item.getCompletedTime() != null) {
                SimpleDateFormat simpleDate3 = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                String time3 = simpleDate3.format(item.getCompletedTime());
                helper.setText(R.id.tvCloseTime, time3);
            }
            Glide.with(mContext).load(item.getCoinIcon()).placeholder(R.drawable.icon_bite)
                    .into((ImageView) helper.getView(R.id.item_home_img));
            helper.setText(R.id.tvPrice, item.getPrice() + "");
            helper.setText(R.id.tvCurrentPrice, item.getCurrentPrice() + "");
            helper.setText(R.id.tvAmount, new DecimalFormat("#0.00").format(item.getAmount()) + "");
            helper.setText(R.id.tvMarginMoney, new DecimalFormat("#0.00").format(item.getMarginMoney()) + "");
            helper.setText(R.id.tvOrderFee, new DecimalFormat("#0.00").format(item.getOrderFee()) + "");
            helper.setText(R.id.tvOvernightMoney, new DecimalFormat("#0.00").format(item.getOvernightMoney()) + "");
            helper.setText(R.id.tvStopProfitPrice, item.getStopProfitPrice() + "");
            helper.setText(R.id.tvStopLossPrice, item.getStopLossPrice() + "");
            helper.setText(R.id.tvLeverage, item.getLeverage() + "");
            if (item.getProfitLost() > 0) {
                helper.getView(R.id.tvProfitLost).setBackgroundResource(R.drawable.circle_corner_green_back);
            } else {
                helper.getView(R.id.tvProfitLost).setBackgroundResource(R.drawable.circle_corner_red_back);
            }
            helper.setText(R.id.tvProfitLost, mContext.getResources().getString(R.string.Profit_loss)  + new DecimalFormat("#0.00").format(item.getProfitLost()) + "");
            if (item.getPositionStatus().equals("CLOSED")) {
                helper.setText(R.id.tvType, mContext.getResources().getString(R.string.close_type_1));
            } else if (item.getPositionStatus().equals("FORCE_CLOSED")) {
                helper.setText(R.id.tvType, mContext.getResources().getString(R.string.close_type_2));
            } else if (item.getPositionStatus().equals("FOLLOW_CLOSED")) {
                helper.setText(R.id.tvType, mContext.getResources().getString(R.string.close_type_3));
            }
            ((TextView) helper.getView(R.id.tvShare)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onclickListenerShare.share(item);
                }
            });
        }
    }


    OnclickListenerShare onclickListenerShare;

    OnclickListenerColse onclickListenerColse;

    OnclickListenerModify onclickListenerModify;

    OnclickCancleListener onclickListenerCancle;

    public void setOnShareListener(OnclickListenerShare share) {
        this.onclickListenerShare = share;
    }

    public void setOnColseListener(OnclickListenerColse colse) {
        this.onclickListenerColse = colse;
    }

    public void setOnModifyListener(OnclickListenerModify modify) {
        this.onclickListenerModify = modify;
    }

    public void setOnCancleListener(OnclickCancleListener cancle) {
        this.onclickListenerCancle = cancle;
    }

    public interface OnclickListenerColse {

        void close(String orderId);
    }

    public interface OnclickCancleListener {

        void cancle(String orderId);
    }

    public interface OnclickListenerModify {

        void modify(String orderId, String stopProfitPrice, String stopLossPrice);
    }

    public interface OnclickListenerShare {

        void share(HoldEntity item);
    }


}
