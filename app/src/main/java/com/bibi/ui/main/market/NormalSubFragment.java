package com.bibi.ui.main.market;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bibi.R;
import com.bibi.adapter.Homes2Adapter;
import com.bibi.adapter.HomesAdapter;
import com.bibi.app.Injection;
import com.bibi.base.BaseLazyFragment;
import com.bibi.entity.Currency;
import com.bibi.ui.kline.KlineActivity;
import com.bibi.ui.kline_spot.SKlineActivity;
import com.bibi.ui.main.MainContract;
import com.bibi.ui.main.MarketBaseFragment;
import com.bibi.ui.main.drawer.BTCMarket2Fragment;
import com.bibi.utils.SharedPreferenceInstance;
import com.bibi.utils.WonderfulToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/9/25
 */
public class NormalSubFragment extends BaseLazyFragment implements MainContract.TwoRootView {
    @BindView(R.id.rvContent)
    RecyclerView rvContent;
    @BindView(R.id.tab)
    LinearLayout tab;

    private ImageView ivSymbol;
    private ImageView ivClose;
    private ImageView ivChange;
    private HomesAdapter mAdapter;
    private List<Currency> currencies = new ArrayList<>();
    private MainContract.TwoRootPresenter presenter;
    private String sort = "";
    private String direction = "";
    private String directionSymbol = "";
    private String directionClose = "";
    private String directionChange = "";
    private String nav = "";
    private String legalCurrency = "";
    //选择栏
    private List<String> titles = new ArrayList<>();//子tab标题
    private ArrayList<TextView> textViews = new ArrayList<>();
    private TextView selectedTextView; //选择子标题
    private int type = -1; //选择的子标题

    public static NormalSubFragment getInstance(String nav, String legalCurrency) {
        NormalSubFragment fragment = new NormalSubFragment();
        Bundle bundle = new Bundle();
        bundle.putString("nav", nav);
        bundle.putString("legalCurrency", legalCurrency);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_market_normal;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        presenter = new TwoRootPresenter(Injection.provideTasksRepository(getActivity()), this);
        nav = getArguments().getString("nav");
        legalCurrency = getArguments().getString("legalCurrency");
    }

    @Override
    protected void obtainData() {
        presenter.setTickersPage(SharedPreferenceInstance.getInstance().getTOKEN(), sort, direction, nav, legalCurrency);
        presenter.setTickersZone(SharedPreferenceInstance.getInstance().getTOKEN());
    }

    @Override
    protected void fillWidget() {
        initRvContent();
    }

    @Override
    protected void loadData() {
    }

    private void initRvContent() {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvContent.setLayoutManager(manager);
        mAdapter = new HomesAdapter(currencies, 2);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (currencies.get(position).getExchangeable() == 1) {
                    SKlineActivity.actionStart(getActivity(), currencies.get(position).getSymbol());
                } else {
                    WonderfulToastUtils.showToast("暂未开放");
                }
            }
        });
        mAdapter.isFirstOnly(true);
        mAdapter.setLoad(true);
        rvContent.setAdapter(mAdapter);
        View head = View.inflate(getActivity(), R.layout.adapter_two_sub_head, null);
        mAdapter.setHeaderView(head);
        ivSymbol = head.findViewById(R.id.ivSymbol);
        ivClose = head.findViewById(R.id.ivClose);
        ivChange = head.findViewById(R.id.ivChange);
        head.findViewById(R.id.llSymbol).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sort = "symbol";
                if (directionSymbol.equals("DESC")) {
                    directionSymbol = "ASC";
                    ivSymbol.setBackground(getResources().getDrawable(R.drawable.icon_asc));
                } else {
                    directionSymbol = "DESC";
                    ivSymbol.setBackground(getResources().getDrawable(R.drawable.icon_desc));
                }
                directionClose = "";
                directionChange = "";
                ivClose.setBackgroundResource(R.drawable.icon_rank);
                ivChange.setBackgroundResource(R.drawable.icon_rank);
                direction = directionSymbol;
                refresh();
            }
        });
        head.findViewById(R.id.llClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sort = "close";
                if (directionClose.equals("DESC")) {
                    directionClose = "ASC";
                    ivClose.setBackground(getResources().getDrawable(R.drawable.icon_asc));
                } else {
                    directionClose = "DESC";
                    ivClose.setBackground(getResources().getDrawable(R.drawable.icon_desc));
                }
                directionSymbol = "";
                directionChange = "";
                ivSymbol.setBackgroundResource(R.drawable.icon_rank);
                ivChange.setBackgroundResource(R.drawable.icon_rank);
                direction = directionClose;
                refresh();
            }
        });
        head.findViewById(R.id.llChange).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sort = "change";
                if (directionChange.equals("DESC")) {
                    directionChange = "ASC";
                    ivChange.setBackground(getResources().getDrawable(R.drawable.icon_asc));
                } else {
                    directionChange = "DESC";
                    ivChange.setBackground(getResources().getDrawable(R.drawable.icon_desc));
                }
                directionSymbol = "";
                directionClose = "";
                ivClose.setBackgroundResource(R.drawable.icon_rank);
                ivSymbol.setBackgroundResource(R.drawable.icon_rank);
                direction = directionChange;
                refresh();
            }
        });
    }

    void refresh() {
        presenter.setTickersPage(SharedPreferenceInstance.getInstance().getTOKEN(), sort, direction, nav, legalCurrency);
    }

    @Override
    public void getZoneFail(Integer code, String toastMessage) {

    }

    @Override
    public void getZoneSuccess(List<String> obj) {
        textViews.clear();
        titles.clear();
        titles.addAll(obj);
        setTextView();
        //默认选择第一个tab
        selectedTextView = textViews.get(0);
        legalCurrency = selectedTextView.getText().toString();
        //画线
        Drawable home_zhang_no = getResources().getDrawable(
                R.drawable.tag);
        selectedTextView.setCompoundDrawablesWithIntrinsicBounds(null,
                null, null, home_zhang_no);
    }

    @Override
    public void getTickersPageSuccess(List<Currency> obj) {
        currencies.clear();
        currencies.addAll(obj);
        mAdapter.notifyDataSetChanged();
//        invalid.dataInvalid(currencies);
    }


    @Override
    public void setPresenter(MainContract.TwoRootPresenter presenter) {
        this.presenter = presenter;
    }

    /**
     * 设置tab栏显示内容
     */
    private void setTextView() {
        for (int i = 0; i < titles.size(); i++) {
            if (i < 12) {
                TextView textView = (TextView) LayoutInflater.from(getmActivity()).inflate(R.layout.tab_kline_textview, null);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
                layoutParams.setMarginStart(40);
                layoutParams.setMarginEnd(40);
                layoutParams.weight = 1;
                textView.setLayoutParams(layoutParams);
                textView.setText(titles.get(i));
                textView.setTag(i);
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        selectedTextView = (TextView) view;
                        legalCurrency = selectedTextView.getText().toString();
                        int selectedTag = (int) selectedTextView.getTag();
                        type = selectedTag;
                        setPagerView();
                        refresh();
                    }
                });
                textViews.add(textView);
                tab.addView(textView);
            }
        }
    }

    /**
     * textview的点击事件
     */
    private void setPagerView() {
        for (int j = 0; j < textViews.size(); j++) {
            textViews.get(j).setSelected(false);
            textViews.get(j).setTextColor(getResources().getColor(R.color.primaryTextGray));
            textViews.get(j).setCompoundDrawablesWithIntrinsicBounds(null,
                    null, null, null);
            int tag = (int) textViews.get(j).getTag();
            if (tag == type) {
                textViews.get(j).setSelected(true);
                Drawable home_zhang_no1 = getResources().getDrawable(
                        R.drawable.tag);
                textViews.get(j).setCompoundDrawablesWithIntrinsicBounds(null,
                        null, null, home_zhang_no1);
            }
        }
    }

    public void dataLoaded(Currency temp) {
        for (Currency currency:currencies){
            if (temp.getSymbol().equals(currency.getSymbol())) {
                Currency.shallowClone(currency, temp);
                mAdapter.notifyDataSetChanged();
                break;
            }
        }
    }
}
