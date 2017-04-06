package com.example.administrator.shopcardemo.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.shopcardemo.Holder.ShopCarGoodHolder;
import com.example.administrator.shopcardemo.R;
import com.example.administrator.shopcardemo.RX.RxBus;
import com.example.administrator.shopcardemo.bean.ShopCarChildBean;
import com.example.administrator.shopcardemo.bean.ShopCarParentBean;

import java.util.List;

public class ShopCarChildAdapter extends RecyclerView.Adapter<ShopCarGoodHolder> {
    private ShopCarParentBean parentBean;
//    判断父类是否全选的
    private Boolean isallclick = true;
//     使用类记录状态
    private List<ShopCarParentBean> mBeanList;
    private Integer parentposition;
    private int howmuchnoselect = 0;
//    全部金额
    private float mTotal;
//    购物车的数量
    private int mTotalnum;

    private String mess;

    public ShopCarChildAdapter(ShopCarParentBean bean, List<ShopCarParentBean> mBeanList, Integer position) {
        this.parentBean = bean;
        this.mBeanList = mBeanList;
        this.parentposition = position;
    }

    @Override
    public ShopCarGoodHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       return new ShopCarGoodHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shop_car_good, parent, false));
    }

    @Override
    public void onBindViewHolder(final ShopCarGoodHolder holder,int position) {
        final ShopCarChildBean childBean = parentBean.getCarChildBeans().get(position);
//        获取数量
        mess = "x " + childBean.getGoodnum();
        holder.mGoodnum.setText(mess);
        mess = "" + childBean.getGoodnum();
        holder.mShopcarnum.setText(mess);
        //        获取价格
        final float price = childBean.getPrice();
        mess = "人民币" + price;
        holder.mGoodmoney.setText(mess);
        //            计算总金额
        CountAllPrice();
        mess = "人民币:"+mTotal;
        RxBus.getInstance().send(mess);
//        mShopcarGoodPrice.setText(mess);
        mess = "结算:"+"("+mTotalnum+")";
        RxBus.getInstance().send(mess);

//        判断是否全选了
        if(!IsAllSelect()){
            RxBus.getInstance().send(false);
        }else {
            RxBus.getInstance().send(true);
        }

        if(childBean.getIsClick()){
            holder.mShopcargoodselect.setImageResource(R.drawable.select);
        }else {
            holder.mShopcargoodselect.setImageResource(R.drawable.no_select);
        }

//      删除的
        holder.mShopcardelete.setOnClickListener(v -> {
            parentBean.getCarChildBeans().remove(position);
            notifyItemRemoved(position);
//                删除之后防止数组越界
            notifyItemRangeChanged(0, parentBean.getCarChildBeans().size());
            //            计算总金额
            CountAllPrice();
            if(parentBean.getCarChildBeans().size()==0){
//                    删除上方的Item
                mBeanList.remove(parentBean);
//                    提示最外层的Adapter更新
//                mAdapter.notifyAdapterData();
                RxBus.getInstance().send(mBeanList.size());
            }else {
//                mAdapter.notifyItemChanged(parentposition);
                RxBus.getInstance().send(parentposition);
            }

        });

//        判断是否打开编辑
        if(parentBean.getIsOpen()){
            holder.mShopcargooddetail.setVisibility(View.GONE);
            holder.mShopcargoodedit.setVisibility(View.VISIBLE);
        }else {
            holder.mShopcargooddetail.setVisibility(View.VISIBLE);
            holder.mShopcargoodedit.setVisibility(View.GONE);
        }

//        选中
        holder.mShopcargoodselect.setOnClickListener(v -> {
            childBean.setIsClick(!childBean.getIsClick());
            isAllClick();
            if (isallclick) {
                parentBean.setIsClick(true);
            } else {
                parentBean.setIsClick(false);
            }
            if(howmuchnoselect==0||howmuchnoselect==1){
                RxBus.getInstance().send(parentposition);
            }else {
                notifyItemChanged(position);
            }

//            判断店铺的商品是否全选了
            if(!IsAllSelect()){
                RxBus.getInstance().send(false);
            }else {
                RxBus.getInstance().send(true);
            }

            //            计算总金额
            CountAllPrice();
            mess = "人民币"+mTotal;
            RxBus.getInstance().send(mess);
            mess = "结算:"+"("+mTotalnum+")";
            RxBus.getInstance().send(mess);
        });

        //        减少货物
        holder.mShopcarminus.setOnClickListener(v -> {
            Integer GoodNum = childBean.getGoodnum();
            GoodNum--;
            if (GoodNum <= 1) {
                GoodNum = 1;
            }
            GoodNum(GoodNum,holder,childBean);
        });

//        增加商品
        holder.mShopcaradd.setOnClickListener(v -> {
            Integer GoodNum = childBean.getGoodnum();
            GoodNum++;
            GoodNum(GoodNum,holder,childBean);
        });
    }

//    计算商品数量的
    private void GoodNum(Integer GoodNum,ShopCarGoodHolder holder,ShopCarChildBean childBean){
        mess = "" + GoodNum;
        holder.mShopcarnum.setText(mess);
        holder.mGoodnum.setText(mess);
        childBean.setGoodnum(GoodNum);
        CountAllPrice();
        mess = "人民币:"+mTotal;
        RxBus.getInstance().send(mess);
//        mShopcarGoodPrice.setText(mess);
        mess = "结算:"+"("+mTotalnum+")";
        RxBus.getInstance().send(mess);
    }


//    判断最外层的的全选按钮是否选中
    private boolean IsAllSelect() {
        for (int i = 0; i < mBeanList.size(); i++) {
            ShopCarParentBean bean = mBeanList.get(i);
            if(!bean.getIsClick()){
                return bean.getIsClick();
            }
        }
        return true;
    }


    /**
     * 计算总金额
     */
    private void CountAllPrice() {
        mTotal = 0;
        mTotalnum = 0;
        for (ShopCarParentBean parentbean : mBeanList) {
            for (int i = 0; i < parentbean.getCarChildBeans().size(); i++) {
                ShopCarChildBean childbean = parentbean.getCarChildBeans().get(i);
                if (childbean.getIsClick()) {
                    mTotal += childbean.getGoodnum() * childbean.getPrice();
                    mTotalnum += childbean.getGoodnum();
                }
            }
        }
    }

//  判断店铺那层是否全选了
    private void isAllClick() {
        howmuchnoselect = 0;
        for (int i = 0; i < parentBean.getCarChildBeans().size(); i++) {
            ShopCarChildBean child = parentBean.getCarChildBeans().get(i);
            if (!child.getIsClick()) howmuchnoselect++;
        }

        isallclick = howmuchnoselect == 0;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return parentBean.getCarChildBeans()==null?0: parentBean.getCarChildBeans().size();
    }
}
