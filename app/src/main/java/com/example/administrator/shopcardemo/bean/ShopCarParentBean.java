package com.example.administrator.shopcardemo.bean;

import com.example.administrator.shopcardemo.Adapter.ShopCarChildAdapter;

import java.util.List;


public class ShopCarParentBean {
    public Boolean getIsClick() {
        return isClick;
    }

    public void setIsClick(Boolean isClick) {
        this.isClick = isClick;
    }
//  �Ƿ�����
    private Boolean isClick;
//   ���µ���Item
    private List<ShopCarChildBean> mCarChildBeans;
//    �ж��Ƿ�༭״̬
    private Boolean isOpen;

    private ShopCarChildAdapter mChildAdapter;

    public ShopCarChildAdapter getChildAdapter() {
        return mChildAdapter;
    }

    public void setChildAdapter(ShopCarChildAdapter childAdapter) {
        mChildAdapter = childAdapter;
    }

    public Boolean getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(Boolean isOpen) {
        this.isOpen = isOpen;
    }

    public List<ShopCarChildBean> getCarChildBeans() {
        return mCarChildBeans;
    }

    public void setCarChildBeans(List<ShopCarChildBean> carChildBeans) {
        mCarChildBeans = carChildBeans;
    }
}
