package com.bibi.ui.kline;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.bibi.R;
import com.bibi.adapter.DepthAdapter;
import com.bibi.base.BaseFragment;
import com.bibi.entity.DepthResult;
import com.bibi.app.UrlFactory;
import com.bibi.socket.ISocket;
import com.bibi.serivce.SocketMessage;
import com.bibi.serivce.SocketResponse;
import com.bibi.utils.WonderfulLogUtils;
import com.bibi.utils.okhttp.StringCallback;
import com.bibi.utils.okhttp.WonderfulOkhttpUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Request;

/**
 * kline_深度
 * Created by daiyy on 2018/6/4 0004.
 */

public class DepthFragment extends BaseFragment {
    @BindView(R.id.tvNumber)
    TextView tvNumber;
    @BindView(R.id.tvNumberR)
    TextView tvNumberR;
    @BindView(R.id.tvPrice)
    TextView tvPrice;
    @BindView(R.id.rvDepth)
    RecyclerView recyclerView;
    @BindView(R.id.depthBar)
    ProgressBar depthBar;
    private Unbinder unbinder;
    private String symbol;
    private DepthAdapter adapter;
    private ArrayList<DepthResult.DepthListInfo> objBuyList;
    private ArrayList<DepthResult.DepthListInfo> objSellList;
    private Activity activity;
    private Gson gson;

    public static DepthFragment getInstance(String symbol) {
        DepthFragment depthFragment = new DepthFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("symbol", symbol);
        depthFragment.setArguments(bundle);
        return depthFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        // 取消订阅
        EventBus.getDefault().post(new SocketMessage(0, ISocket.CMD.UNSUBSCRIBE_EXCHANGE_TRADE, symbol.getBytes()));
        EventBus.getDefault().unregister(this);
    }
    private String symbolJson;

    private void startTCP() {
        // 开始订阅深度
        try {
            symbolJson=new JSONObject().put("symbol",symbol).toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        EventBus.getDefault().post(new SocketMessage(0, ISocket.CMD.SUBSCRIBE_EXCHANGE_TRADE, symbolJson.getBytes()));
    }


    /**
     * socket 推送过来的信息
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSocketMessage(SocketResponse response) {
        Log.i("推送过来的信息： DepthFragment", response.getCmd().toString());
        if (response.getCmd() == ISocket.CMD.PUSH_EXCHANGE_DEPTH) {
            try {
                WonderfulLogUtils.logi("tag", "深度图 推送过来的信息==" + response.getResponse());
                if (gson == null)
                    gson = new Gson();
                DepthResult.DepthObject result = gson.fromJson(response.getResponse(), new TypeToken<DepthResult.DepthObject>() {
                }.getType());
                String strSymbol = result.getSymbol();
                if (strSymbol != null && strSymbol.equals(symbol)) {
                    ArrayList<DepthResult.DepthListInfo> depthListInfosBuy =result.getBuyTradePlate().getItems();
                    ArrayList<DepthResult.DepthListInfo> depthListInfosSell =result.getSellTradePlate().getItems();

                    objSellList.removeAll(objSellList);
                    objSellList = initData(depthListInfosSell);
                    adapter.setObjSellList(objSellList);
                    adapter.notifyDataSetChanged();

                    objBuyList.removeAll(objBuyList);
                    objBuyList = initData(depthListInfosBuy);
                    adapter.setObjBuyList(objBuyList);
                    adapter.notifyDataSetChanged();
//                    ArrayList<DepthResult.DepthListInfo> depthListInfos = result.getItems();
//                    String direct = result.getDirection();
//                    if (direct.equals("SELL")) {
//                        objSellList.removeAll(objSellList);
//                        objSellList = initData(depthListInfos);
//                        adapter.setObjSellList(objSellList);
//                        adapter.notifyDataSetChanged();
//                    } else {
//                        objBuyList.removeAll(objBuyList);
//                        objBuyList = initData(depthListInfos);
//                        adapter.setObjBuyList(objBuyList);
//                        adapter.notifyDataSetChanged();
//                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_depth;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        intLv();
        recyclerView.setNestedScrollingEnabled(false);
    }

    /**
     * 初始化列表数据
     */
    private void intLv() {
        activity = getActivity();
        objBuyList = new ArrayList<>();
        objSellList = new ArrayList<>();
        LinearLayoutManager manager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        manager.setStackFromEnd(false);
        recyclerView.setLayoutManager(manager);
    }

    @Override
    protected void obtainData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            symbol = bundle.getString("symbol");
            String[] strings = symbol.split("/");
            tvNumber.setText(getResources().getString(R.string.number) + "(" + strings[0] + ")");
            tvNumberR.setText(getResources().getString(R.string.number) + "(" + strings[0] + ")");
            tvPrice.setText(getResources().getString(R.string.price) + "(" + strings[1] + ")");
            startTCP();
            getDepth();
        }
    }

    @Override
    protected void fillWidget() {
    }

    @Override
    protected void loadData() {
    }

    /**
     * 获取深度图数据
     */
    private void getDepth() {
        if (depthBar != null)
            depthBar.setVisibility(View.VISIBLE);
        WonderfulOkhttpUtils.post().url(UrlFactory.getDepth()).addParams("symbol", symbol).build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        WonderfulLogUtils.logi("tag123", "深度图 返回数据==" + response);
                        doLogicData(response);
                    }
                });
    }

    /**
     * 解析数据
     *
     * @param response
     */
    private void doLogicData(String response) {
        if (depthBar != null)
            depthBar.setVisibility(View.GONE);
        try {
            gson = new Gson();
            DepthResult result = gson.fromJson(response, new TypeToken<DepthResult>() {
            }.getType());
            DepthResult.DepthList ask = result.getAsk();
            ArrayList<DepthResult.DepthListInfo> askItems = ask.getItems();
            objSellList = initData(askItems);
            DepthResult.DepthList bid = result.getBid();
            ArrayList<DepthResult.DepthListInfo> bidItems = bid.getItems();
            objBuyList = initData(bidItems);
            if (adapter == null) {
                DisplayMetrics dm = getResources().getDisplayMetrics();
                int width = dm.widthPixels;
                adapter = new DepthAdapter(activity);
                adapter.setObjBuyList(objBuyList);
                adapter.setObjSellList(objSellList);
                adapter.setWidth(width / 2);
//                WonderfulLogUtils.logi("tag", "屏幕宽度==" + (width / 2));
                recyclerView.setAdapter(adapter);
            }
        } catch (Exception e) {
            e.printStackTrace();
//            if (isAdded())
//                WonderfulToastUtils.showToast(getResources().getString(R.string.parse_error));
        }

    }

    /**
     * 填充数据
     */
    private ArrayList<DepthResult.DepthListInfo> initData(ArrayList<DepthResult.DepthListInfo> objList) {
        ArrayList<DepthResult.DepthListInfo> depthListInfos = new ArrayList<>();
        if (objList != null) {
            for (int i = 0; i < 20; i++) {
                if (i < objList.size()) { // list里有数据
                    depthListInfos.add(objList.get(i));
                } else {
                    DepthResult.DepthListInfo depthListInfo = new DepthResult().new DepthListInfo();
                    depthListInfo.setAmount(-1);
                    depthListInfo.setPrice(-1);
                    depthListInfos.add(depthListInfo);
                }
            }
        } else {
            for (int i = 0; i < 20; i++) {
                DepthResult.DepthListInfo depthListInfo = new DepthResult().new DepthListInfo();
                depthListInfo.setAmount(-1);
                depthListInfo.setPrice(-1);
                depthListInfos.add(depthListInfo);
            }
        }
        return depthListInfos;
    }

}
