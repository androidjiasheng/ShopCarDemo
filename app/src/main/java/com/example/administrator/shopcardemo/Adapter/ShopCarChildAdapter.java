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
//    �жϸ����Ƿ�ȫѡ��
    private Boolean isallclick = true;
//     ʹ�����¼״̬
    private List<ShopCarParentBean> mBeanList;
    private Integer parentposition;
    private int howmuchnoselect = 0;
//    ȫ�����
    private float mTotal;
//    ���ﳵ������
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
//        ��ȡ����
        mess = "x " + childBean.getGoodnum();
        holder.mGoodnum.setText(mess);
        mess = "" + childBean.getGoodnum();
        holder.mShopcarnum.setText(mess);
        //        ��ȡ�۸�
        final float price = childBean.getPrice();
        mess = "�����" + price;
        holder.mGoodmoney.setText(mess);
        //            �����ܽ��
        CountAllPrice();
        mess = "�����:"+mTotal;
        RxBus.getInstance().send(mess);
//        mShopcarGoodPrice.setText(mess);
        mess = "����:"+"("+mTotalnum+")";
        RxBus.getInstance().send(mess);

//        �ж��Ƿ�ȫѡ��
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

//      ɾ����
        holder.mShopcardelete.setOnClickListener(v -> {
            parentBean.getCarChildBeans().remove(position);
            notifyItemRemoved(position);
//                ɾ��֮���ֹ����Խ��
            notifyItemRangeChanged(0, parentBean.getCarChildBeans().size());
            //            �����ܽ��
            CountAllPrice();
            if(parentBean.getCarChildBeans().size()==0){
//                    ɾ���Ϸ���Item
                mBeanList.remove(parentBean);
//                    ��ʾ������Adapter����
//                mAdapter.notifyAdapterData();
                RxBus.getInstance().send(mBeanList.size());
            }else {
//                mAdapter.notifyItemChanged(parentposition);
                RxBus.getInstance().send(parentposition);
            }

        });

//        �ж��Ƿ�򿪱༭
        if(parentBean.getIsOpen()){
            holder.mShopcargooddetail.setVisibility(View.GONE);
            holder.mShopcargoodedit.setVisibility(View.VISIBLE);
        }else {
            holder.mShopcargooddetail.setVisibility(View.VISIBLE);
            holder.mShopcargoodedit.setVisibility(View.GONE);
        }

//        ѡ��
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

//            �жϵ��̵���Ʒ�Ƿ�ȫѡ��
            if(!IsAllSelect()){
                RxBus.getInstance().send(false);
            }else {
                RxBus.getInstance().send(true);
            }

            //            �����ܽ��
            CountAllPrice();
            mess = "�����"+mTotal;
            RxBus.getInstance().send(mess);
            mess = "����:"+"("+mTotalnum+")";
            RxBus.getInstance().send(mess);
        });

        //        ���ٻ���
        holder.mShopcarminus.setOnClickListener(v -> {
            Integer GoodNum = childBean.getGoodnum();
            GoodNum--;
            if (GoodNum <= 1) {
                GoodNum = 1;
            }
            GoodNum(GoodNum,holder,childBean);
        });

//        ������Ʒ
        holder.mShopcaradd.setOnClickListener(v -> {
            Integer GoodNum = childBean.getGoodnum();
            GoodNum++;
            GoodNum(GoodNum,holder,childBean);
        });
    }

//    ������Ʒ������
    private void GoodNum(Integer GoodNum,ShopCarGoodHolder holder,ShopCarChildBean childBean){
        mess = "" + GoodNum;
        holder.mShopcarnum.setText(mess);
        holder.mGoodnum.setText(mess);
        childBean.setGoodnum(GoodNum);
        CountAllPrice();
        mess = "�����:"+mTotal;
        RxBus.getInstance().send(mess);
//        mShopcarGoodPrice.setText(mess);
        mess = "����:"+"("+mTotalnum+")";
        RxBus.getInstance().send(mess);
    }


//    �ж������ĵ�ȫѡ��ť�Ƿ�ѡ��
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
     * �����ܽ��
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

//  �жϵ����ǲ��Ƿ�ȫѡ��
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
