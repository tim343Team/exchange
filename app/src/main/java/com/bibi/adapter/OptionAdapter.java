package com.bibi.adapter;

import android.content.Context;
import android.os.CountDownTimer;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
import java.util.Timer;

import com.bibi.R;
import com.bibi.app.MyApplication;
import com.bibi.entity.OptionEntity;
import com.bibi.entity.Order;

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
    private List<CountDownTimer> timerArray = new ArrayList<>();
    private List<CountDownTimer> timerPriceArray = new ArrayList<>();

    public OptionAdapter(Context context, @LayoutRes int layoutResId, @Nullable List<OptionEntity> data, int type) {
        super(layoutResId, data);
        this.context = context;
        this.type = type;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final OptionEntity item) {
        if (type == 0) {
            helper.setText(R.id.tvCurrentPriceType, context.getResources().getString(R.string.hold_new_price));
            CountDownTimer timer = null;
            CountDownTimer timerPrice = null;
            long time = item.getSettlementTime() - System.currentTimeMillis();
            timer = new CountDownTimer(time, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    long endTime = millisUntilFinished / 1000;
                    helper.setText(R.id.tvEndTime, endTime + "S");
                }

                @Override
                public void onFinish() {
                    helper.setText(R.id.tvEndTime, R.string.complete);
                    callback.onCallback(item, helper.getAdapterPosition());
                }
            };
            timer.start();
            timerArray.add(timer);
            timerPrice = new CountDownTimer(1000 * 60 * 60 * 24 * 360, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    for (String key : priceMap.keySet()) {
                        if (key.equals(item.getSymbol())) {
                            helper.setText(R.id.tvCurrentPrice, priceMap.get(key));
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
                                            helper.setText(R.id.tvProfitLost, context.getResources().getString(R.string.Profit_loss) + " " + new DecimalFormat("#0.0000").format(item.getAmount() * (item.getLeverage() > 0 ? item.getLeverage() : 1)));
                                        } else {
                                            helper.getView(R.id.tvProfitLost).setBackgroundResource(R.drawable.circle_corner_red_back);
                                            helper.setText(R.id.tvProfitLost, context.getResources().getString(R.string.Profit_loss) + " " + "-" + new DecimalFormat("#0.0000").format(item.getAmount() * (item.getLeverage() > 0 ? item.getLeverage() : 1)));
                                        }
                                    } else {
                                        if (Double.parseDouble(priceMap.get(key)) < item.getPrice()) {
                                            helper.getView(R.id.tvProfitLost).setBackgroundResource(R.drawable.circle_corner_green_back);
                                            helper.setText(R.id.tvProfitLost, context.getResources().getString(R.string.Profit_loss) + " " + new DecimalFormat("#0.0000").format(item.getAmount() * (item.getLeverage() > 0 ? item.getLeverage() : 1)));
                                        } else {
                                            helper.getView(R.id.tvProfitLost).setBackgroundResource(R.drawable.circle_corner_red_back);
                                            helper.setText(R.id.tvProfitLost, context.getResources().getString(R.string.Profit_loss) + " " + "-" + new DecimalFormat("#0.0000").format(item.getAmount() * (item.getLeverage() > 0 ? item.getLeverage() : 1)));
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
            timerPrice.start();
            timerPriceArray.add(timerPrice);
        } else {
            helper.setText(R.id.tvEndTime, item.getPeriod());
            helper.setText(R.id.tvCurrentPrice, item.getSettlementPrice() + "");
            helper.setText(R.id.tvCurrentPriceType, context.getResources().getString(R.string.item_cost));
            if (item.getProfitLost() > 0) {
                helper.getView(R.id.tvProfitLost).setBackgroundResource(R.drawable.circle_corner_green_back);
            } else {
                helper.getView(R.id.tvProfitLost).setBackgroundResource(R.drawable.circle_corner_red_back);
            }
            helper.setText(R.id.tvProfitLost, context.getResources().getString(R.string.Profit_loss) + item.getProfitLost());
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
            helper.setText(R.id.tvLeverageTitle, "倍率");
            helper.setText(R.id.tvEarnings, item.getLeverage() + "倍");
        } else {
            helper.setText(R.id.tvLeverageTitle, "盈利率");
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
        for (CountDownTimer cdt : timerArray) {
            if (cdt != null) {
                cdt.cancel();
            }
        }
        for (CountDownTimer cdt : timerPriceArray) {
            if (cdt != null) {
                cdt.cancel();
            }
        }
    }

    public void cancelPostionTimers(int position) {
        if (timerArray == null) {
            return;
        }
        if (timerPriceArray == null) {
            return;
        }
        if (timerArray.size() - 1 > position) {
            CountDownTimer cdt = timerArray.get(position);
            if (cdt != null) {
                cdt.cancel();
                timerArray.remove(cdt);
            }
        }
        if (timerPriceArray.size() - 1 > position) {
            CountDownTimer cdt = timerPriceArray.get(position);
            if (cdt != null) {
                cdt.cancel();
                timerPriceArray.remove(cdt);
            }
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

        void onCallback(OptionEntity item, int position);
    }
}
