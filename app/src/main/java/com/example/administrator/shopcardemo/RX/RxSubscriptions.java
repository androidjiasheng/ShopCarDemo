package com.example.administrator.shopcardemo.RX;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by jiasheng on 2016/10/30.
 * email 898478073@qq.com
 * Description: ���� CompositeSubscription
 */

public class RxSubscriptions {
//  CompositeSubscription  �ö�����Ϊsubscription������������ͳһȡ������
    private static CompositeSubscription mCompositeSubscription = new CompositeSubscription();

    public static boolean isUnSubscribed(){
        return mCompositeSubscription.hasSubscriptions();
    }

    public static void add(Subscription s){
        if(s != null){
            mCompositeSubscription.add(s);
        }
    }

//    ���ָ������
    public static void remove(Subscription s) {
        if (s != null) {
            mCompositeSubscription.remove(s);
        }
    }

//    �����������
    public static void clear() {
        mCompositeSubscription.clear();
    }
//   �����
    public static void unsubscribe() {
        mCompositeSubscription.unsubscribe();
    }
//    �ж��Ƿ����
    public static boolean hasSubscriptions() {
        return mCompositeSubscription.hasSubscriptions();
    }
}
