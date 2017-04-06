package com.example.administrator.shopcardemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.shopcardemo.Adapter.ShopCarChildAdapter;
import com.example.administrator.shopcardemo.Adapter.ShopCarParentAdapter;
import com.example.administrator.shopcardemo.RX.RxBus;
import com.example.administrator.shopcardemo.RX.RxSubscriptions;
import com.example.administrator.shopcardemo.bean.ShopCarChildBean;
import com.example.administrator.shopcardemo.bean.ShopCarParentBean;
import com.jakewharton.rxbinding.view.RxView;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public RecyclerView mShopcarRv;
//   全选的按钮
    public  ImageView mShopcarAllchoose;
//    价格
    public  TextView mShopcarGoodPrice;
//     全部的数量
    public  TextView mCompleteNum;
//    父的Adapter
    public  ShopCarParentAdapter mAdapter;

    private List<ShopCarParentBean> mClickList = new ArrayList<>();
    private Boolean IsAllClick = true;

    private int GoodsNum;
    private float TotalPrice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_car);
        mShopcarRv = (RecyclerView) findViewById(R.id.shopcar_rv);
        mShopcarAllchoose = (ImageView) findViewById(R.id.shopcar_allchoose);
        mShopcarGoodPrice = (TextView) findViewById(R.id.shopcar_good_price);
        mCompleteNum = (TextView) findViewById(R.id.complete_money);
        initData();
        init();
        initRxBus();
    }

    private void initRxBus() {
//        判断是否全选的
        Subscription subscription = RxBus.getInstance().toSubscription(Boolean.class, aBoolean -> {
            Log.e("showme","判断"+aBoolean);
            IsAllClick = !aBoolean;
            if (aBoolean) {
                mShopcarAllchoose.setImageResource(R.drawable.select);
            } else {
                mShopcarAllchoose.setImageResource(R.drawable.no_select);
            }
        }, throwable -> Log.e("showme", throwable.toString() + ""));

//       填写金额的 数量的
        Subscription subscription1 = RxBus.getInstance().toSubscription(String.class, s -> {
            Log.e("showme","判断"+s);
            if(s.contains("结算")){
                mCompleteNum.setText(s);
            }else {
                mShopcarGoodPrice.setText(s);
            }
        });

        Subscription subscription2 = RxBus.getInstance().toSubscription(Integer.class, integer -> {
            Log.e("showme","判断"+integer);
            if(integer==mClickList.size()){
                mAdapter.notifyDataSetChanged();
            }else {
                mAdapter.notifyItemChanged(integer);
            }
        });

//        填写
//        Subscription subscription2 = RxBus.getInstance().toSubscription()
        RxSubscriptions.add(subscription);
        RxSubscriptions.add(subscription1);
        RxSubscriptions.add(subscription2);
    }

    private void initData() {
        //        填充数据
        for (int i = 0; i < 3; i++) {
            ShopCarParentBean bean = new ShopCarParentBean();
            bean.setIsClick(false);
            bean.setIsOpen(false);
            List<ShopCarChildBean> listchild = new ArrayList<>();
            for (int a = 0; a < 3; a++) {
                ShopCarChildBean childBean = new ShopCarChildBean();
                childBean.setIsClick(false);
                childBean.setGoodnum(1);
                childBean.setPrice(59);
                listchild.add(childBean);
            }
            bean.setCarChildBeans(listchild);
            mClickList.add(bean);
        }

        mAdapter = new ShopCarParentAdapter(MainActivity.this, mClickList);
        //        存储子Adapter
        for(ShopCarParentBean parent:mClickList){
            ShopCarChildAdapter adpter = new ShopCarChildAdapter(parent,mClickList,mClickList.indexOf(parent));
            adpter.setHasStableIds(true);
            parent.setChildAdapter(adpter);
        }

        mAdapter.setHasStableIds(true);
        mShopcarRv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        mShopcarRv.setLayoutManager(llm);
        mShopcarRv.getItemAnimator().setChangeDuration(0L);
        mShopcarRv.setAdapter(mAdapter);
    }

    private void init() {
        RxView.clicks(mShopcarAllchoose).subscribe(aVoid -> {
            IsAllClick =!IsAllClick;
            GoodsNum = 0;
            TotalPrice = 0;
            if (IsAllClick) {
//                    全部不选
                mShopcarAllchoose.setImageResource(R.drawable.no_select);
                for (int i = 0; i < mClickList.size(); i++) {
                    mClickList.get(i).setIsClick(false);
                    for (int a = 0; a < mClickList.get(i).getCarChildBeans().size(); a++) {
                        mClickList.get(i).getCarChildBeans().get(a).setIsClick(false);
                    }
                }
            } else {
//                  全选
                mShopcarAllchoose.setImageResource(R.drawable.select);
                for (int i = 0; i < mClickList.size(); i++) {
                    mClickList.get(i).setIsClick(true);
                    for (int a = 0; a < mClickList.get(i).getCarChildBeans().size(); a++) {
                        mClickList.get(i).getCarChildBeans().get(a).setIsClick(true);
                        TotalPrice += mClickList.get(i).getCarChildBeans().get(a).getPrice() * mClickList.get(i).getCarChildBeans().get(a).getGoodnum();
                        GoodsNum += mClickList.get(i).getCarChildBeans().get(a).getGoodnum();
                    }
                }
            }
            mAdapter.notifyDataSetChanged();
            mShopcarGoodPrice.setText("人民币" + TotalPrice + "");
            mCompleteNum.setText("结算" + "(" + GoodsNum + ")");
        });

        mCompleteNum.setOnClickListener(v -> ToastUtil.showMess(MainActivity.this, mShopcarGoodPrice.getText().toString().substring(1)));
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(RxSubscriptions.hasSubscriptions())
            RxSubscriptions.unsubscribe();
    }
}
