package com.example.administrator.shopcardemo.Holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.shopcardemo.R;

/**
 * Created by better_001 on 2016/8/18.
 */
public class ShopCarHolder extends RecyclerView.ViewHolder{

    public RecyclerView mShopcarshopgoods;
    public TextView mGoodedit;
    public ImageView mWhethernearshop;
    public TextView mShopname;
    public ImageView mShopcarallselect;

    public ShopCarHolder(View itemView, Boolean noData) {
        super(itemView);
        if(!noData) {
            mShopcarshopgoods = (RecyclerView) itemView.findViewById(R.id.shopcar_shopgoods);
            mGoodedit = (TextView) itemView.findViewById(R.id.good_edit);
            mWhethernearshop = (ImageView) itemView.findViewById(R.id.whether_nearshop);
            mShopname = (TextView) itemView.findViewById(R.id.shop_name);
            mShopcarallselect = (ImageView) itemView.findViewById(R.id.shop_car_all_select);
        }
    }
}
