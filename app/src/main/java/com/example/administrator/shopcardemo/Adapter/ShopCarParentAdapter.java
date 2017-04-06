package com.example.administrator.shopcardemo.Adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.shopcardemo.Holder.ShopCarHolder;
import com.example.administrator.shopcardemo.R;
import com.example.administrator.shopcardemo.bean.ShopCarChildBean;
import com.example.administrator.shopcardemo.bean.ShopCarParentBean;

import java.util.List;


public class ShopCarParentAdapter extends RecyclerView.Adapter<ShopCarHolder> {
    private Context mContext;
    private List<ShopCarParentBean> mBeanList;
    private Boolean noData = false;
    private Boolean IsChildAllClick = true;

    public ShopCarParentAdapter(Context context, List<ShopCarParentBean> mBeanList) {
        mContext = context;
        this.mBeanList = mBeanList;
        noData = mBeanList.size()==0;
    }



    @Override
    public ShopCarHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        if (noData){
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shop_car_no_good, parent, false);
        }else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shop_car_head, parent, false);
        }
        return new ShopCarHolder(v,noData);
    }

    @Override
    public void onBindViewHolder(final ShopCarHolder holder, final int position) {
//        判断有没有数据
        if(!noData) {
            final ShopCarParentBean parentBean = mBeanList.get(position);
            if(parentBean.getIsClick()){
                holder.mShopcarallselect.setImageResource(R.drawable.select);
            }else {
                holder.mShopcarallselect.setImageResource(R.drawable.no_select);
            }

            if (parentBean.getIsOpen()) {
                holder.mGoodedit.setText(mContext.getResources().getString(R.string.complete));
            } else {
                holder.mGoodedit.setText(mContext.getResources().getString(R.string.edit));
            }


            if(holder.mShopcarshopgoods.getLayoutManager()!=null&&holder.mShopcarshopgoods.getAdapter()!=null
                    &&parentBean.getChildAdapter()==holder.mShopcarshopgoods.getAdapter()){
                parentBean.getChildAdapter().notifyDataSetChanged();
            }else {
                LinearLayoutManager llm = new LinearLayoutManager(mContext){
                    @Override
                    public boolean canScrollVertically() {
                        return false;
                    }
                };
                holder.mShopcarshopgoods.setHasFixedSize(true);
                holder.mShopcarshopgoods.setLayoutManager(llm);
                holder.mShopcarshopgoods.getItemAnimator().setChangeDuration(0L);
                holder.mShopcarshopgoods.setAdapter(parentBean.getChildAdapter());
            }


            holder.mGoodedit.setOnClickListener(v -> {
                parentBean.setIsOpen(!parentBean.getIsOpen());
                notifyItemChanged(position);
            });

            holder.mShopcarallselect.setOnClickListener(v -> {
                if (parentBean.getIsClick()) {
                    IsChildAllClick = false;
                    parentBean.setIsClick(false);
                } else {
                    IsChildAllClick = true;
                    parentBean.setIsClick(true);
                }
                for (ShopCarChildBean bean:parentBean.getCarChildBeans()) {
                    bean.setIsClick(IsChildAllClick);
                }
                notifyItemChanged(position);
//                notifyDataSetChanged();
            });
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return noData?1:mBeanList.size();
    }
}
