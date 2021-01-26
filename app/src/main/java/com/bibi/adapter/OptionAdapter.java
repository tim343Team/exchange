package com.bibi.adapter;

import android.content.Context;
import android.os.CountDownTimer;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.bibi.R;
import com.bibi.app.MyApplication;
import com.bibi.entity.OptionEntity;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/6/11
 */
public class OptionAdapter extends BaseQuickAdapter<OptionEntity, BaseViewHolder> {
    private Context context;
    private int type;
    //    private String price;
    private Map<String, String> priceMap = new HashMap<>();
    //    private List<CountDownTimer> timerArray = new ArrayList<>();
    private Map<String, CountDownTimer> timerArray = new HashMap<String, CountDownTimer>();
    //    private List<CountDownTimer> timerPriceArray = new ArrayList<>();
    private Map<String, CountDownTimer> timerPriceArray = new HashMap<String, CountDownTimer>();

    public OptionAdapter(Context context, @LayoutRes int layoutResId, @Nullable List<OptionEntity> data, int type) {
        super(layoutResId, data);
        this.context = context;
        this.type = type;
    }

    private String timeFormat(int date) {
        if (date < 60) {
            if (date < 10) {
                return "0" + date + "S";
            } else {
                return date + "S";
            }
        } else if (date > 60 && date < 3600) {
            int m = date / 60;
            int s = date % 60;
            String mStr = String.valueOf(m);
            if (m < 10) {
                mStr = "0" + m;
            }
            String sStr = String.valueOf(s);
            if (s < 10) {
                sStr = "0" + s;
            }
            return mStr + ":" + sStr + "";
        } else {
            int h = date / 3600;
            int m = (date % 3600) / 60;
            int s = (date % 3600) % 60;
            String hStr = String.valueOf(h);
            if (h < 10) {
                hStr = "0" + h;
            }
            String mStr = String.valueOf(m);
            if (m < 10) {
                mStr = "0" + m;
            }
            String sStr = String.valueOf(s);
            if (s < 10) {
                sStr = "0" + s;
            }
            return hStr + ":" + mStr + ":" + sStr + "";
        }
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull BaseViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        Log.e("remove_position:", holder.getLayoutPosition() + "");
    }

    @Override
    protected void convert(final BaseViewHolder helper, final OptionEntity item) {
        if (type == 0) {
            helper.setText(R.id.tvCurrentPriceType, context.getResources().getString(R.string.hold_new_price));
            final long time = item.getSettlementTime() - System.currentTimeMillis();
            CountDownTimer timer = timerArray.get(item.getOrderId());
//            if (timer != null) {
//                timer.cancel();
//            }
            if (timer == null) {
                timer = new CountDownTimer(time, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        int endTime = (int) millisUntilFinished / 1000;
                        helper.setText(R.id.tvEndTime, timeFormat(endTime));
                    }

                    @Override
                    public void onFinish() {
                        helper.setText(R.id.tvEndTime, R.string.complete);
                        if (time > 0) {
                            callback.onCallback(item, helper.getLayoutPosition(), item.getOrderId());
                        }
                    }
                };
                timerArray.put(item.getOrderId(), timer);
                timer.start();
            }

            CountDownTimer timerPrice = timerPriceArray.get(item.getOrderId());
//            if (timerPrice != null) {
//                timerPrice.cancel();
//                timerPriceArray.remove(item.getOrderId());
//            }
            if (timerPrice == null) {
                timerPrice = new CountDownTimer(1000 * 60 * 60 * 24 * 360, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        for (String key : priceMap.keySet()) {
                            if (key.equals(item.getSymbol())) {
                                String price = priceMap.get(key);
//                            String[] prices = price.split(",");
//                            if (prices.length > 1) {
//                                price=prices[1].length()>5?prices[1].substring(0,3):;
//                            } else {
//                                price = price + ".0000";
//                            }
                                helper.setText(R.id.tvCurrentPrice, price);
                                try {
                                    if (item.getLeverage() > 0) {
                                        //有倍率的计算方式
                                        double currentPrice = 0;
                                        try {
                                            currentPrice = Double.parseDouble(priceMap.get(key));
                                        } catch (Exception e) {
                                            currentPrice = 0;
                                        }
                                        if (item.getDirection().equals("BUY")) {
                                            if (Double.parseDouble(priceMap.get(key)) > item.getPrice()) {
                                                helper.getView(R.id.tvProfitLost).setBackgroundResource(R.drawable.circle_corner_green_back);
                                                //持仓价/最新价 ：item.getAmount()/priceMap.get(key)
                                                //当前价 ：priceMap.get(key)
                                                //开仓价 ：item.getPrice()
                                                helper.setText(R.id.tvProfitLost, context.getResources().getString(R.string.Profit_loss) + " " + new DecimalFormat("#0.0000").format((item.getAmount() / currentPrice) * (currentPrice - item.getPrice()) * item.getLeverage()));
                                            } else {
                                                helper.getView(R.id.tvProfitLost).setBackgroundResource(R.drawable.circle_corner_red_back);
                                                helper.setText(R.id.tvProfitLost, context.getResources().getString(R.string.Profit_loss) + " " + "-" + new DecimalFormat("#0.0000").format((item.getAmount() / currentPrice) * (currentPrice - item.getPrice()) * item.getLeverage()));
                                            }
                                        } else {
                                            if (Double.parseDouble(priceMap.get(key)) < item.getPrice()) {
                                                helper.getView(R.id.tvProfitLost).setBackgroundResource(R.drawable.circle_corner_green_back);
                                                helper.setText(R.id.tvProfitLost, context.getResources().getString(R.string.Profit_loss) + " " + new DecimalFormat("#0.0000").format((item.getAmount() / currentPrice) * (item.getPrice() - currentPrice) * item.getLeverage()));
                                            } else {
                                                helper.getView(R.id.tvProfitLost).setBackgroundResource(R.drawable.circle_corner_red_back);
                                                helper.setText(R.id.tvProfitLost, context.getResources().getString(R.string.Profit_loss) + " " + "-" + new DecimalFormat("#0.0000").format((item.getAmount() / currentPrice) * (item.getPrice() - currentPrice) * item.getLeverage()));
                                            }
                                        }
                                    } else {
                                        //无倍率的计算方式
                                        if (item.getDirection().equals("BUY")) {
                                            if (Double.parseDouble(priceMap.get(key)) > item.getPrice()) {
                                                helper.getView(R.id.tvProfitLost).setBackgroundResource(R.drawable.circle_corner_green_back);
                                                helper.setText(R.id.tvProfitLost, context.getResources().getString(R.string.Profit_loss) + item.getAmount() * (item.getProfitRate() / 100));
                                            } else {
                                                helper.getView(R.id.tvProfitLost).setBackgroundResource(R.drawable.circle_corner_red_back);
                                                helper.setText(R.id.tvProfitLost, context.getResources().getString(R.string.Profit_loss) + "-" + item.getAmount());
                                            }
                                        } else {
                                            if (Double.parseDouble(priceMap.get(key)) < item.getPrice()) {
                                                helper.getView(R.id.tvProfitLost).setBackgroundResource(R.drawable.circle_corner_green_back);
                                                helper.setText(R.id.tvProfitLost, context.getResources().getString(R.string.Profit_loss) + item.getAmount() * (item.getProfitRate() / 100));
                                            } else {
                                                helper.getView(R.id.tvProfitLost).setBackgroundResource(R.drawable.circle_corner_red_back);
                                                helper.setText(R.id.tvProfitLost, context.getResources().getString(R.string.Profit_loss) + "-" + item.getAmount());
                                            }
                                        }
                                    }

                                } catch (Exception e) {

                                }
                            }
                        }
                    }

                    @Override
                    public void onFinish() {

                    }
                };
                timerPriceArray.put(item.getOrderId(), timerPrice);
                timerPrice.start();
            }

        } else {
            helper.setText(R.id.tvEndTime, item.getPeriod());
            helper.setText(R.id.tvCurrentPrice, item.getSettlementPrice() + "");
            helper.setText(R.id.tvCurrentPriceType, context.getResources().getString(R.string.item_cost));
            if (item.getProfitLost() > 0) {
                helper.getView(R.id.tvProfitLost).setBackgroundResource(R.drawable.circle_corner_green_back);
            } else {
                helper.getView(R.id.tvProfitLost).setBackgroundResource(R.drawable.circle_corner_red_back);
            }
            helper.setText(R.id.tvProfitLost, context.getResources().getString(R.string.Profit_loss) + new DecimalFormat("#0.0000").format(item.getProfitLost()));
        }

        Glide.with(mContext).load(item.getCoinIcon()).placeholder(R.drawable.icon_bite)
                .into((ImageView) helper.getView(R.id.item_home_img));
        helper.setText(R.id.tvSymbol, item.getSymbol());
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String fileName = simpleDate.format(item.getTime());
        if (item.getDirection().equals("BUY")) {
            helper.setTextColor(R.id.tvDirection, ContextCompat.getColor(MyApplication.getApp(),
                    R.color.color_00b274));
        } else {
            helper.setTextColor(R.id.tvDirection, ContextCompat.getColor(MyApplication.getApp(),
                    R.color.typeRed));
        }
        helper.setText(R.id.tvDirection, item.getDirection().equals("BUY") ? context.getResources().getString(R.string.bullish) : context.getResources().getString(R.string.bearish));
        helper.setText(R.id.tvTime, fileName);
        helper.setText(R.id.tvPrice, item.getPrice() + "");
        helper.setText(R.id.tvAmount, item.getAmount() * (item.getLeverage() > 0 ? item.getLeverage() : 1) + "");
        //helper.setText(R.id.tvEarnings, item.getProfitRate() + "%");
        if (item.getLeverage() > 0) {
            helper.setText(R.id.tvLeverageTitle, context.getResources().getString(R.string.earnings));
            helper.setText(R.id.tvEarnings, item.getLeverage() + "倍");
        } else {
            helper.setText(R.id.tvLeverageTitle, context.getResources().getString(R.string.report));
            helper.setText(R.id.tvEarnings, item.getProfitRate() + "%");
        }
    }

    public void cancelAllTimers() {
        if (timerArray == null) {
            return;
        }
        if (timerPriceArray == null) {
            return;
        }
//        for (CountDownTimer cdt : timerArray) {
//            if (cdt != null) {
//                cdt.cancel();
//            }
//        }
        for (Map.Entry<String, CountDownTimer> entry : timerArray.entrySet()) {
            CountDownTimer cdt = entry.getValue();
            if (cdt != null) {
                cdt.cancel();
            }
        }
//        for (CountDownTimer cdt : timerPriceArray) {
//            if (cdt != null) {
//                cdt.cancel();
//            }
//        }
        for (Map.Entry<String, CountDownTimer> entry : timerPriceArray.entrySet()) {
            CountDownTimer cdt = entry.getValue();
            if (cdt != null) {
                cdt.cancel();
            }
        }
        timerArray.clear();
        timerPriceArray.clear();
    }

    public void cancelPostionTimers(String orderId) {
        if (timerArray == null) {
            return;
        }
        if (timerPriceArray == null) {
            return;
        }
        CountDownTimer cdt = timerArray.get(orderId);
        if (cdt != null) {
            cdt.cancel();
            timerArray.remove(orderId);
        }

        CountDownTimer cdt2 = timerPriceArray.get(orderId);
        if (cdt2 != null) {
            cdt2.cancel();
            timerPriceArray.remove(orderId);
        }

    }

    public void setPriceMap(Map<String, String> priceMap) {
        this.priceMap = priceMap;
    }

    CallBackLister callback;

    public void setCallBackLister(CallBackLister callback) {
        this.callback = callback;
    }

    public interface CallBackLister {

        void onCallback(OptionEntity item, int position, String orderId);
    }
}
