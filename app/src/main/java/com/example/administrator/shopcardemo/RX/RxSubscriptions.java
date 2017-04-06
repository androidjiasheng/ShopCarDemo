package com.example.administrator.shopcardemo.RX;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by jiasheng on 2016/10/30.
 * email 898478073@qq.com
 * Description: 管理 CompositeSubscription
 */

public class RxSubscriptions {
//  CompositeSubscription  该对象作为subscription的容器，方便统一取消订阅
    private static CompositeSubscription mCompositeSubscription = new CompositeSubscription();

    public static boolean isUnSubscribed(){
        return mCompositeSubscription.hasSubscriptions();
    }

    public static void add(Subscription s){
        if(s != null){
            mCompositeSubscription.add(s);
        }
    }

//    清除指定数据
    public static void remove(Subscription s) {
        if (s != null) {
            mCompositeSubscription.remove(s);
        }
    }

//    清除所有数据
    public static void clear() {
        mCompositeSubscription.clear();
    }
//   解除绑定
    public static void unsubscribe() {
        mCompositeSubscription.unsubscribe();
    }
//    判断是否绑定了
    public static boolean hasSubscriptions() {
        return mCompositeSubscription.hasSubscriptions();
    }
}
