package com.bibi.adapter;//package com.bibi.adapter;
//
//import android.support.annotation.Nullable;
//import android.support.v4.content.ContextCompat;
//import android.util.Log;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.bumptech.glide.Glide;
//import com.chad.library.adapter.base.BaseQuickAdapter;
//import com.chad.library.adapter.base.BaseViewHolder;
//
//import com.bibi.R;
//import com.bibi.app.MyApplication;
//import com.bibi.entity.Currency;
//import com.bibi.entity.EntrustHistory;
//import com.bibi.entity.HoldEntity;
//import com.bibi.utils.WonderfulDateUtils;
//import com.bibi.utils.WonderfulMathUtils;
//import com.bibi.utils.WonderfulToastUtils;
//
//import java.math.BigDecimal;
//import java.text.DecimalFormat;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.List;
//import java.util.Locale;
//
//
//public class TrustCurrentAdapter extends BaseQuickAdapter<HoldEntity, BaseViewHolder> {
//    private List<Currency> currencies;
//
//    public TrustCurrentAdapter(@Nullable List<HoldEntity> data,List<Currency> currencies) {
//        super(R.layout.adapter_trust_layout_new, data);
//        this.currencies = currencies;
//    }
//
//    public void setCurrencies(List<Currency> currencies) {
//        this.currencies = currencies;
//    }
//
//    @Override
//    protected void convert(BaseViewHolder helper, final HoldEntity item) {
//        if (item.getDirection().equals("BUY")) {
//            ((TextView) helper.getView(R.id.tvDirection)).setTextColor(mContext.getResources().getColor(R.color.color_00b274));
//        } else {
//            ((TextView) helper.getView(R.id.tvDirection)).setTextColor(mContext.getResources().getColor(R.color.color_f15057));
//        }
//        helper.setText(R.id.tvDirection, item.getDirection().equals("BUY") ? "做多" : "做空");
//        helper.setText(R.id.tvSymbol, item.getSymbol());
//        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
//        String time = simpleDate.format(item.getTime());
//        helper.setText(R.id.tvTime, time);
//        if(item.getFollowId()==0){
//            helper.setText(R.id.tvType, "自然订单");
//        }else {
//            helper.setText(R.id.tvType, "自动跟单");
//        }
//        Glide.with(mContext).load(item.getCoinIcon()).placeholder(R.drawable.icon_bite)
//                .into((ImageView) helper.getView(R.id.item_home_img));
//        helper.setText(R.id.tvPrice, item.getPrice() + "");
//        for(Currency currencie:currencies){
//            if(currencie.getSymbol().equals(item.getSymbol())){
//                String format = new DecimalFormat("#0.0000").format(currencie.getClose());
//                BigDecimal bg = new BigDecimal(format);
//                String v = bg.setScale(8, BigDecimal.ROUND_DOWN).stripTrailingZeros().toPlainString();
//                helper.setText(R.id.tvCurrentPrice, v);
//                break;
//            }
//        }
////        helper.setText(R.id.tvCurrentPrice, item.getCurrentPrice() + "");
//        helper.setText(R.id.tvAmount, new DecimalFormat("#0.00").format(item.getAmount()) + "");
//        helper.setText(R.id.tvMarginMoney, new DecimalFormat("#0.00").format(item.getMarginMoney()) + "");
//        helper.setText(R.id.tvOrderFee, new DecimalFormat("#0.00").format(item.getOrderFee()) + "");
//        helper.setText(R.id.tvOvernightMoney, new DecimalFormat("#0.00").format(item.getOvernightMoney()) + "");
//        helper.setText(R.id.tvStopProfitPrice, item.getStopProfitPrice() + "");
//        helper.setText(R.id.tvStopLossPrice, item.getStopLossPrice() + "");
//        helper.setText(R.id.tvLeverage, item.getLeverage() + "");
//        if (item.getProfitLost() > 0) {
//            helper.getView(R.id.tvProfitLost).setBackgroundResource(R.drawable.circle_corner_green_back);
//        } else {
//            helper.getView(R.id.tvProfitLost).setBackgroundResource(R.drawable.circle_corner_red_back);
//        }
//        helper.setText(R.id.tvProfitLost, "盈亏  " + new DecimalFormat("#0.00").format(item.getProfitLost()) + "");
//
//        helper.getView(R.id.tvCloseOrder).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onclickListenerColse.close(item.getOrderId());
//            }
//        });
//        helper.getView(R.id.tvModify).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onclickListenerModify.modify(item.getOrderId(),item.getStopProfitPrice()+"",item.getStopLossPrice()+"");
//            }
//        });
//    }
//
//    OnclickListenerColse onclickListenerColse;
//
//    OnclickListenerModify onclickListenerModify;
//
//    public void setOnColseListener(OnclickListenerColse colse) {
//        this.onclickListenerColse = colse;
//    }
//
//    public void setOnModifyListener(OnclickListenerModify modify) {
//        this.onclickListenerModify = modify;
//    }
//
//    public interface OnclickListenerColse {
//
//        void close(String orderId);
//    }
//
//    public interface OnclickListenerModify {
//
//        void modify(String orderId,String stopProfitPrice,String stopLossPrice);
//    }
//
////    @Override
////    protected void convert(BaseViewHolder helper, EntrustHistory item) {
////        if ("BUY".equals(item.getDirection())) {
////            helper.setText(R.id.trust_type, mContext.getResources().getString(R.string.text_buy)).setTextColor(R.id.trust_type,
////                    ContextCompat.getColor(MyApplication.getApp(), R.color.typeGreen));
////        } else {
////            helper.setText(R.id.trust_type, mContext.getResources().getString(R.string.text_sale)).setTextColor(R.id.trust_type,
////                    ContextCompat.getColor(MyApplication.getApp(), R.color.typeRed));
////        }
////        String[] times = WonderfulDateUtils.getFormatTime(null, new Date(item.getTime())).split(" ");
////        helper.setText(R.id.trust_time, times[0].substring(5, times[0].length()) + " " + times[1].substring(0, 5));
////        if ("LIMIT_PRICE".equals(item.getType())) { // 限价
////            String format1 = new DecimalFormat("#0.00000000").format(item.getPrice());
////            BigDecimal bg1 = new BigDecimal(format1);
////            String v1 =  bg1.setScale(8,BigDecimal.ROUND_DOWN).stripTrailingZeros().toPlainString();
////            helper.setText(R.id.trust_price, v1);
////            // 数量
////            String format = new DecimalFormat("#0.00000000").format(item.getAmount());
////            BigDecimal bg = new BigDecimal(format);
////            String v = bg.setScale(8, BigDecimal.ROUND_DOWN).stripTrailingZeros().toPlainString();
////            helper.setText(R.id.trust_num, v);
////        } else { // 市价
////            helper.setText(R.id.trust_price, String.valueOf(mContext.getResources().getString(R.string.text_best_prices)));
////            // 数量 如果是市价并买入情况就是--
////            if ("BUY".equals(item.getDirection())) {
////                helper.setText(R.id.trust_num, String.valueOf("--"));
////            } else {
////
////                helper.setText(R.id.trust_num, WonderfulMathUtils.getRundNumber(Double.valueOf(String.valueOf(BigDecimal.valueOf(item.getAmount()))), 2, null));
////            }
////        }
////        String symbol = item.getSymbol();
////        int i = symbol.indexOf("/");
////        helper.setText(R.id.trust_one, mContext.getResources().getString(R.string.text_number) + "(" + symbol.substring(0, i) + ")");
////        helper.setText(R.id.trust_two, mContext.getResources().getString(R.string.text_actual_deal) + "(" + symbol.substring(0, i) + ")");
////        helper.setText(R.id.trust_ones, mContext.getResources().getString(R.string.text_price) + "(" + symbol.substring(i + 1, symbol.length()) + ")");
////        // 已成交
////        String format2 = new DecimalFormat("#0.00000000").format(item.getTradedAmount());
////        BigDecimal bg2 = new BigDecimal(format2);
////        String v2 = bg2.setScale(8, BigDecimal.ROUND_DOWN).stripTrailingZeros().toPlainString();
////        helper.setText(R.id.trust_done, v2);
////        helper.addOnClickListener(R.id.trust_back);
////    }
//}
