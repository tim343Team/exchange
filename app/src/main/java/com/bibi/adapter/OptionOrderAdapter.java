package com.bibi.adapter;

import android.content.Context;
import android.os.CountDownTimer;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bibi.R;
import com.bibi.app.MyApplication;
import com.bibi.entity.OptionEntity;
import com.bumptech.glide.Glide;
import com.m7.imkfsdk.chat.adapter.FlowAdapter;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class OptionOrderAdapter extends RecyclerView.Adapter<OptionOrderAdapter.ViewHolder> {
    private Context context;
    private int type;//0持仓，1历史
    private List<OptionEntity> data;
    private Map<String, String> priceMap = new HashMap<>();
    private Map<String, CountDownTimer> timerArray = new HashMap<String, CountDownTimer>();
    private Map<String, CountDownTimer> timerPriceArray = new HashMap<String, CountDownTimer>();

    public OptionOrderAdapter(Context context,  @Nullable List<OptionEntity> data, int type) {
        this.data = data;
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

    @NonNull
    @Override
    public OptionOrderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(type==0){
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.item_option, parent, false);
            return new OptionOrderAdapter.ViewHolder(view);
        }else {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.item_option_history, parent, false);
            return new OptionOrderAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final OptionOrderAdapter.ViewHolder holder, final int position) {
        final OptionEntity item = data.get(position);
        if (type == 0) {
            Log.e("remove_position:", "convert" + position + "");
            holder.tvCurrentPriceType.setText(context.getResources().getString(R.string.hold_new_price));
            final long time = item.getSettlementTime() - System.currentTimeMillis();
            if (holder.timer != null) {
                holder.timer.cancel();
                timerArray.remove(item.getOrderId());
                holder.timer = new CountDownTimer(time, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        int endTime = (int) millisUntilFinished / 1000;
                        holder.tvEndTime.setText(timeFormat(endTime));
                    }

                    @Override
                    public void onFinish() {
                        holder.tvEndTime.setText(R.string.complete);
                        if (time > 0) {
                            callback.onCallback(item, position, item.getOrderId());
                        }
                    }
                };
                timerArray.put(item.getOrderId(), holder.timer);
                holder.timer.start();
            }
            if (holder.timer == null) {
                holder.timer = new CountDownTimer(time, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        int endTime = (int) millisUntilFinished / 1000;
                        holder.tvEndTime.setText(timeFormat(endTime));
                    }

                    @Override
                    public void onFinish() {
                        holder.tvEndTime.setText(R.string.complete);
                        if (time > 0) {
                            callback.onCallback(item, holder.getAdapterPosition(), item.getOrderId());
                        }
                    }
                };
                timerArray.put(item.getOrderId(), holder.timer);
                holder.timer.start();
            }

            if (holder.timerPrice != null) {
                holder.timerPrice.cancel();
                timerPriceArray.remove(item.getOrderId());
                holder.timerPrice = new CountDownTimer(1000 * 60 * 60 * 24 * 360, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        for (String key : priceMap.keySet()) {
                            if (key.equals(item.getSymbol())) {
                                String price = priceMap.get(key);
                                holder.tvCurrentPrice.setText(price);
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
                                                holder.tvProfitLost.setBackgroundResource(R.drawable.circle_corner_green_back);
                                                //持仓价/最新价 ：item.getAmount()/priceMap.get(key)
                                                //当前价 ：priceMap.get(key)
                                                //开仓价 ：item.getPrice()
                                                holder.tvProfitLost.setText(context.getResources().getString(R.string.Profit_loss) + " " + new DecimalFormat("#0.0000").format(new BigDecimal ((item.getAmount() / currentPrice) * (currentPrice - item.getPrice()) * item.getLeverage())));
                                            } else {
                                                holder.tvProfitLost.setBackgroundResource(R.drawable.circle_corner_red_back);
                                                holder.tvProfitLost.setText(context.getResources().getString(R.string.Profit_loss) + " " + "-" + new DecimalFormat("#0.0000").format(new BigDecimal((item.getAmount() / currentPrice) * (currentPrice - item.getPrice()) * item.getLeverage())));
                                            }
                                        } else {
                                            if (Double.parseDouble(priceMap.get(key)) < item.getPrice()) {
                                                holder.tvProfitLost.setBackgroundResource(R.drawable.circle_corner_green_back);
                                                holder.tvProfitLost.setText(context.getResources().getString(R.string.Profit_loss) + " " + new DecimalFormat("#0.0000").format(new BigDecimal((item.getAmount() / currentPrice) * (item.getPrice() - currentPrice) * item.getLeverage())));
                                            } else {
                                                holder.tvProfitLost.setBackgroundResource(R.drawable.circle_corner_red_back);
                                                holder.tvProfitLost.setText(context.getResources().getString(R.string.Profit_loss) + " " + "-" + new DecimalFormat("#0.0000").format(new BigDecimal((item.getAmount() / currentPrice) * (item.getPrice() - currentPrice) * item.getLeverage())));
                                            }
                                        }
                                    } else {
                                        //无倍率的计算方式
                                        if (item.getDirection().equals("BUY")) {
                                            if (Double.parseDouble(priceMap.get(key)) > item.getPrice()) {
                                                holder.tvProfitLost.setBackgroundResource(R.drawable.circle_corner_green_back);
                                                holder.tvProfitLost.setText(context.getResources().getString(R.string.Profit_loss) + new BigDecimal(item.getAmount() * (item.getProfitRate() / 100)));
                                            } else {
                                                holder.tvProfitLost.setBackgroundResource(R.drawable.circle_corner_red_back);
                                                holder.tvProfitLost.setText(context.getResources().getString(R.string.Profit_loss) + "-" + item.getAmount());
                                            }
                                        } else {
                                            if (Double.parseDouble(priceMap.get(key)) < item.getPrice()) {
                                                holder.tvProfitLost.setBackgroundResource(R.drawable.circle_corner_green_back);
                                                holder.tvProfitLost.setText(context.getResources().getString(R.string.Profit_loss) + new BigDecimal(item.getAmount() * (item.getProfitRate() / 100)));
                                            } else {
                                                holder.tvProfitLost.setBackgroundResource(R.drawable.circle_corner_red_back);
                                                holder.tvProfitLost.setText(context.getResources().getString(R.string.Profit_loss) + "-" + item.getAmount());
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
                timerPriceArray.put(item.getOrderId(), holder.timerPrice);
                holder.timerPrice.start();
            }
            if (holder.timerPrice == null) {
                holder.timerPrice = new CountDownTimer(1000 * 60 * 60 * 24 * 360, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        for (String key : priceMap.keySet()) {
                            if (key.equals(item.getSymbol())) {
                                String price = priceMap.get(key);
                                holder.tvCurrentPrice.setText(price);
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
                                                holder.tvProfitLost.setBackgroundResource(R.drawable.circle_corner_green_back);
                                                //持仓价/最新价 ：item.getAmount()/priceMap.get(key)
                                                //当前价 ：priceMap.get(key)
                                                //开仓价 ：item.getPrice()
                                                holder.tvProfitLost.setText(context.getResources().getString(R.string.Profit_loss) + " " + new DecimalFormat("#0.0000").format(new BigDecimal ((item.getAmount() / currentPrice) * (currentPrice - item.getPrice()) * item.getLeverage())));
                                            } else {
                                                holder.tvProfitLost.setBackgroundResource(R.drawable.circle_corner_red_back);
                                                holder.tvProfitLost.setText(context.getResources().getString(R.string.Profit_loss) + " " + "-" + new DecimalFormat("#0.0000").format(new BigDecimal((item.getAmount() / currentPrice) * (currentPrice - item.getPrice()) * item.getLeverage())));
                                            }
                                        } else {
                                            if (Double.parseDouble(priceMap.get(key)) < item.getPrice()) {
                                                holder.tvProfitLost.setBackgroundResource(R.drawable.circle_corner_green_back);
                                                holder.tvProfitLost.setText(context.getResources().getString(R.string.Profit_loss) + " " + new DecimalFormat("#0.0000").format(new BigDecimal((item.getAmount() / currentPrice) * (item.getPrice() - currentPrice) * item.getLeverage())));
                                            } else {
                                                holder.tvProfitLost.setBackgroundResource(R.drawable.circle_corner_red_back);
                                                holder.tvProfitLost.setText(context.getResources().getString(R.string.Profit_loss) + " " + "-" + new DecimalFormat("#0.0000").format(new BigDecimal((item.getAmount() / currentPrice) * (item.getPrice() - currentPrice) * item.getLeverage())));
                                            }
                                        }
                                    } else {
                                        //无倍率的计算方式
                                        if (item.getDirection().equals("BUY")) {
                                            if (Double.parseDouble(priceMap.get(key)) > item.getPrice()) {
                                                holder.tvProfitLost.setBackgroundResource(R.drawable.circle_corner_green_back);
                                                holder.tvProfitLost.setText(context.getResources().getString(R.string.Profit_loss) + new BigDecimal(item.getAmount() * (item.getProfitRate() / 100)));
                                            } else {
                                                holder.tvProfitLost.setBackgroundResource(R.drawable.circle_corner_red_back);
                                                holder.tvProfitLost.setText(context.getResources().getString(R.string.Profit_loss) + "-" + item.getAmount());
                                            }
                                        } else {
                                            if (Double.parseDouble(priceMap.get(key)) < item.getPrice()) {
                                                holder.tvProfitLost.setBackgroundResource(R.drawable.circle_corner_green_back);
                                                holder.tvProfitLost.setText(context.getResources().getString(R.string.Profit_loss) + new BigDecimal(item.getAmount() * (item.getProfitRate() / 100)));
                                            } else {
                                                holder.tvProfitLost.setBackgroundResource(R.drawable.circle_corner_red_back);
                                                holder.tvProfitLost.setText(context.getResources().getString(R.string.Profit_loss) + "-" + item.getAmount());
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
                timerPriceArray.put(item.getOrderId(), holder.timerPrice);
                holder.timerPrice.start();
            }

        } else {
            holder.tvEndTime.setText(item.getPeriod());
            holder.tvCurrentPrice.setText(item.getSettlementPrice() + "");
            holder.tvCurrentPriceType.setText(context.getResources().getString(R.string.item_cost));
            if (item.getProfitLost() > 0) {
                holder.tvProfitLost.setBackgroundResource(R.drawable.circle_corner_green_back);
            } else {
                holder.tvProfitLost.setBackgroundResource(R.drawable.circle_corner_red_back);
            }
            holder.tvProfitLost.setText(context.getResources().getString(R.string.Profit_loss) + new DecimalFormat("#0.0000").format(item.getProfitLost()));
        }

        Glide.with(context).load(item.getCoinIcon()).placeholder(R.drawable.icon_bite)
                .into((ImageView) holder.homeImg);
        holder.tvSymbol.setText(item.getSymbol());
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String fileName = simpleDate.format(item.getTime());
        if (item.getDirection().equals("BUY")) {
            holder.tvDirection.setTextColor(ContextCompat.getColor(MyApplication.getApp(),
                    R.color.color_00b274));
        } else {
            holder.tvDirection.setTextColor(ContextCompat.getColor(MyApplication.getApp(),
                    R.color.typeRed));
        }
        holder.tvDirection.setText(item.getDirection().equals("BUY") ? context.getResources().getString(R.string.bullish) : context.getResources().getString(R.string.bearish));
        holder.tvTime.setText(fileName);
        holder.tvPrice.setText(item.getPrice() + "");
        holder.tvAmount.setText(item.getAmount() * (item.getLeverage() > 0 ? item.getLeverage() : 1) + "");
        //helper.setText(R.id.tvEarnings, item.getProfitRate() + "%");
        if (item.getLeverage() > 0) {
            holder.tvLeverageTitle.setText(context.getResources().getString(R.string.earnings));
            holder.tvEarnings.setText(item.getLeverage() + "倍");
        } else {
            holder.tvLeverageTitle.setText( context.getResources().getString(R.string.report));
            holder.tvEarnings.setText(item.getProfitRate() + "%");
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void cancelAllTimers() {
        if (timerArray == null) {
            return;
        }
        if (timerPriceArray == null) {
            return;
        }
        for (Map.Entry<String, CountDownTimer> entry : timerArray.entrySet()) {
            CountDownTimer cdt = entry.getValue();
            if (cdt != null) {
                cdt.cancel();
            }
        }
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

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCurrentPriceType;
        TextView tvEndTime;
        TextView tvCurrentPrice;
        TextView tvProfitLost;
        TextView tvSymbol;
        TextView tvDirection;
        TextView tvTime;
        TextView tvAmount;
        TextView tvLeverageTitle;
        TextView tvEarnings;
        TextView tvPrice;
        ImageView homeImg;
        CountDownTimer timer;
        CountDownTimer timerPrice;

        public ViewHolder(View itemView) {
            super(itemView);
            tvCurrentPriceType = (TextView) itemView.findViewById(R.id.tvCurrentPriceType);
            tvEndTime = (TextView) itemView.findViewById(R.id.tvEndTime);
            tvCurrentPrice = (TextView) itemView.findViewById(R.id.tvCurrentPrice);
            tvProfitLost = (TextView) itemView.findViewById(R.id.tvProfitLost);
            homeImg = (ImageView) itemView.findViewById(R.id.item_home_img);
            tvSymbol = (TextView) itemView.findViewById(R.id.tvSymbol);
            tvDirection = (TextView) itemView.findViewById(R.id.tvDirection);
            tvTime = (TextView) itemView.findViewById(R.id.tvTime);
            tvAmount = (TextView) itemView.findViewById(R.id.tvAmount);
            tvLeverageTitle = (TextView) itemView.findViewById(R.id.tvLeverageTitle);
            tvEarnings = (TextView) itemView.findViewById(R.id.tvEarnings);
            tvPrice = (TextView) itemView.findViewById(R.id.tvPrice);
        }
    }
}
