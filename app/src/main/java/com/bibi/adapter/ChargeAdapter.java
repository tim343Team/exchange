package com.bibi.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import com.bibi.R;
import com.bibi.base.LinAdapter;
import com.bibi.entity.ChargeHisBean;
import com.bibi.entity.TiBiBean;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/6/17
 */
public class ChargeAdapter extends LinAdapter<ChargeHisBean> {
    private List<ChargeHisBean> beanss;

    public ChargeAdapter(Activity context, List<ChargeHisBean> beans) {
        super(context, beans);
        beanss=beans;
    }
    @Override
    protected View LgetView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_charge_his, parent, false);
        }
        ChargeHisBean bean = beanss.get(position);
        TextView tvName=ViewHolders.get(convertView,R.id.tvName);
        switch (bean.name) {
            case "0":
                tvName.setText(context.getResources().getString(R.string.chargeMoney));
                break;
            case "1":
                tvName.setText(context.getResources().getString(R.string.withdrawal));
                break;
            case "2":
                tvName.setText(context.getResources().getString(R.string.transfer));
                break;
            case "3":
                tvName.setText("币币交易");
                break;
            case "4":
                tvName.setText("法币买入");
                break;
            case "5":
                tvName.setText("法币卖出");
                break;
            case "6":
                tvName.setText("活动奖励");
                break;
            case "7":
                tvName.setText("推广奖励");
                break;
            case "8":
                tvName.setText("分红");
                break;
            case "9":
                tvName.setText("投票");
                break;
            case "10":
                tvName.setText(context.getResources().getString(R.string.manual_recharge));
                break;
            case "11":
                tvName.setText("配对");
                break;
            case "12":
                tvName.setText("缴纳商家认证保证金");
                break;
            case "13":
                tvName.setText("退回商家认证保证金");
                break;
            case "14":
                tvName.setText("法币充值");
                break;
            case "15":
                tvName.setText("币币兑换");
                break;
            case "16":
                tvName.setText("渠道推广");
                break;
            case "17":
                tvName.setText("划转入杠杆钱包");
                break;
            case "18":
                tvName.setText("从杠杆钱包划转出");
                break;
            case "19":
                tvName.setText("钱包空投");
                break;
            case "20":
                tvName.setText("锁仓");
                break;
            case "21":
                tvName.setText("解锁");
                break;
            case "22":
                tvName.setText("第三方转入");
                break;
            case "23":
                tvName.setText("第三方转出");
                break;
            case "24":
                tvName.setText(context.getResources().getString(R.string.transfer_the_contract));
                break;
            case "25":
                tvName.setText("资金转入币币账户");
                break;
            case "26":
                tvName.setText("借贷流水");
                break;
            case "27":
                tvName.setText("还款流水");
                break;
            case "28":
                tvName.setText("币币转入币币");
                break;
            case "29":
                tvName.setText("币币转入币币");
                break;
            case "30":
                tvName.setText(context.getResources().getString(R.string.Contract_forced_liquidation));
                break;
            case "31":
                tvName.setText(context.getResources().getString(R.string.Contract_manual_closing));
                break;
            case "32":
                tvName.setText("币币一键平仓");
                break;
            case "33":
                tvName.setText(context.getResources().getString(R.string.contract_opening));
                break;
            case "34":
                tvName.setText("跟单返佣");
                break;
            case "35":
                tvName.setText(context.getResources().getString(R.string.overnight_fee));
                break;
            case "36":
                tvName.setText(context.getResources().getString(R.string.futures_transferred));
                break;
            case "37":
                tvName.setText(context.getResources().getString(R.string.fiat_currency));
                break;
            case "38":
                tvName.setText("期货交易");
                break;
            case "39":
                tvName.setText(context.getResources().getString(R.string.futures_profit));
                break;
            case "40":
                tvName.setText(context.getResources().getString(R.string.futures_loss));
                break;
            case "41":
                tvName.setText("期货人工充值");
                break;
            default:
                break;
        }

        TextView text_time=ViewHolders.get(convertView,R.id.tvNumber);
        text_time.setText(bean.number);

        TextView text_dizhi=ViewHolders.get(convertView,R.id.tvTime);
        text_dizhi.setText(bean.time);

        return convertView;
    }
}
