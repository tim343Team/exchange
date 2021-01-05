package com.bibi.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.bibi.R;
import com.bibi.app.MyApplication;
import com.bibi.entity.Exchange;
import com.bibi.entity.VolumeInfo;
import com.bibi.utils.WonderfulMathUtils;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/4/2
 */
public class BuyAdapter extends RecyclerView.Adapter<BuyAdapter.MyViewHolder> {
    private Context context;
    private myText text;
    private int type = 0; // 为0就是绿色否则为红色
    private List<Exchange> data;
    private int price = 2;
    private int amount = 2;

    public BuyAdapter(Context context, int type, ArrayList<Exchange> data) {
        this.context = context;
        this.type = type;
        this.data = data;
    }

    public void setText(myText text) {
        this.text = text;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.adapter_sell_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
//        Exchange item = data.get(position);
//        // 对不同的币种做不同的限制
//        if (text != null) {
//            price = text.two();
//            amount = text.one();
//        }
//        if ("--".equals(item.getPrice()) || "--".equals(item.getAmount())) {
//            helper.setText(R.id.item_sell_two, String.valueOf(item.getPrice()));
//            helper.setText(R.id.item_sell_three, String.valueOf(item.getAmount()));
//        } else {
//            BigDecimal bg = new BigDecimal(WonderfulMathUtils.getRundNumber(Double.valueOf(item.getPrice()), price, null));
//            String v = bg.setScale(8, BigDecimal.ROUND_DOWN).stripTrailingZeros().toPlainString();
//            BigDecimal bg1 = new BigDecimal(WonderfulMathUtils.getRundNumber(Double.valueOf(item.getAmount()), amount, null));
//            String v1 = bg1.setScale(8, BigDecimal.ROUND_DOWN).stripTrailingZeros().toPlainString();
//            helper.setText(R.id.item_sell_two, "" + v);
//            helper.setText(R.id.item_sell_three, "" + v1);
//        }
//        int position = helper.getAdapterPosition();
//        float currentTotalAmount = 0;
//        if (type == 1) {
//            for (int i = 0; i <= position; i++) {
//                if (!"--".equals(mData.get(i).getAmount())) {
//                    currentTotalAmount += Float.parseFloat(mData.get(i).getAmount());
//                }
//            }
//        } else {
//            for (int i = mData.size() - 1; i >= position; i--) {
//                if (!"--".equals(mData.get(i).getAmount())) {
//                    currentTotalAmount += Float.parseFloat(mData.get(i).getAmount());
//                }
//            }
//        }
//        float scale = 0;
//        if (totalAmount != 0) {
//            scale = currentTotalAmount / totalAmount;
//        }
//        //Log.i("getAdapterPosition",position+"--"+currentTotalAmount+"--"+totalAmount+"--"+scale);
//        scale = scale > 1 ? 1 : scale;
//        int parentWidth = helper.getView(R.id.ceshi).getMeasuredWidth();
//        int backWidth = (int) (parentWidth * scale);
//        ViewGroup.LayoutParams lp = helper.getView(R.id.tv_back_depth).getLayoutParams();
//        int lastPosition = type == 1 ? mData.size() - 1 : 0;
//        if (!"--".equals(mData.get(position).getAmount())) {
//            if (position == lastPosition) {
//                lp.width = parentWidth - 1;
//                helper.getView(R.id.tv_back_depth).setLayoutParams(lp);
//            } else {
//                lp.width = backWidth >= 1 ? backWidth - 1 : backWidth;
//                helper.getView(R.id.tv_back_depth).setLayoutParams(lp);
//            }
//        } else {
//            lp.width = 0;
//            helper.getView(R.id.tv_back_depth).setLayoutParams(lp);
//        }
//
//
//        if (type == 1) {
//            helper.setTextColor(R.id.item_sell_two, ContextCompat.getColor(MyApplication.getApp(),
//                    R.color.typeGreen));
//            helper.setBackgroundColor(R.id.tv_back_depth, ContextCompat.getColor(MyApplication.getApp(),
//                    R.color.green_back));
//        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public MyViewHolder(View itemView) {
            super(itemView);
//            tvTime = itemView.findViewById(R.id.item_sell_two);
//            tvTime = itemView.findViewById(R.id.item_sell_three);
//            tvTime = itemView.findViewById(R.id.tv_back_depth);
//            tvTime = itemView.findViewById(R.id.item_sell_two);

        }
    }

    public interface myText {
        int one();

        int two();
    }
}
