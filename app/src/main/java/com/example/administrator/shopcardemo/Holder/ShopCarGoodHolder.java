package com.example.administrator.shopcardemo.Holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.shopcardemo.R;


public class ShopCarGoodHolder extends RecyclerView.ViewHolder {

    public ImageView mShopcargoodselect;
    public RelativeLayout mShopcargooddetail;
    public LinearLayout mLoseeffica;
    public RelativeLayout mShopcargoodedit;
    public TextView mGoodnum;
    public TextView mGoodmoney;
    public TextView mGooddetailmess;
    public TextView mGoodtitle;
    public TextView mShopcardelete;
    public ImageView mShopcarchoicestyle;
    public TextView mShopcarsize;
    public TextView mShopcarstyle;
    public ImageView mShopcaradd;
    public TextView mShopcarnum;
    public ImageView mShopcarminus;
    public ImageView mShopcardetailpic;

    public ShopCarGoodHolder(View itemView) {
        super(itemView);
        mShopcargoodselect = (ImageView) itemView.findViewById(R.id.shopcar_goodselect);
        mLoseeffica = (LinearLayout) itemView.findViewById(R.id.lose_effica);
        mShopcargooddetail = (RelativeLayout) itemView.findViewById(R.id.shopcar_good_detail);
        mShopcargoodedit = (RelativeLayout) itemView.findViewById(R.id.shopcar_good_edit);

        mGoodnum = (TextView) itemView.findViewById(R.id.good_num);
        mGoodmoney = (TextView) itemView.findViewById(R.id.good_money);
        mGooddetailmess = (TextView) itemView.findViewById(R.id.good_detail_mess);
        mGoodtitle = (TextView) itemView.findViewById(R.id.good_title);
        mShopcardelete = (TextView) itemView.findViewById(R.id.shopcar_delete);
        mShopcarchoicestyle = (ImageView) itemView.findViewById(R.id.shopcar_choice_style);
        mShopcarsize = (TextView) itemView.findViewById(R.id.shopcar_size);
        mShopcarstyle = (TextView) itemView.findViewById(R.id.shopcar_style);
        mShopcaradd = (ImageView) itemView.findViewById(R.id.shopcar_add);
        mShopcarnum = (TextView) itemView.findViewById(R.id.shopcar_num);
        mShopcarminus = (ImageView) itemView.findViewById(R.id.shopcar_minus);
        mShopcardetailpic = (ImageView) itemView.findViewById(R.id.shopcar_detailpic);

    }
}
