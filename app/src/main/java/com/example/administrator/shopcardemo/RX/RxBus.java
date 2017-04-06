package com.example.administrator.shopcardemo.RX;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * Created by jiasheng on 2016/10/12.
 * email 898478073@qq.com
 * Description: MyWeekLyAppDemo
 */

public class RxBus {
//    volatile
//    ?��֤instance�ɼ���
//    ?��ָֹ������
//volatile��һ���������η���type specifier�������Ǳ�����������α���ͬ�̷߳��ʺ��޸ĵı���
//    volatile�������ǣ� ��Ϊָ��ؼ��֣�ȷ������ָ�������������Ż���ʡ�ԣ���Ҫ��ÿ��ֱ�Ӷ�ֵ.
//��volatile���εı������߳���ÿ��ʹ�ñ�����ʱ�򣬶����ȡ�����޸ĺ��ֵ
//    ����һ�����������volatile����ʱ�����ᱣ֤�޸ĵ�ֵ�����������µ����棬���������߳���Ҫ��ȡʱ������ȥ�ڴ��ж�ȡ��ֵ��
//    ָ��������һ����˵��������Ϊ����߳�������Ч�ʣ����ܻ�������������Ż���
// ������֤�����и�������ִ���Ⱥ�˳��ͬ�����е�˳��һ�£�
// �������ᱣ֤��������ִ�н���ʹ���˳��ִ�еĽ����һ�µġ�


//    ��һ��һ�������������ĳ�Ա��������ľ�̬��Ա��������volatile����֮����ô�;߱����������壺

//    ����1����֤�˲�ͬ�̶߳�����������в���ʱ�Ŀɼ��ԣ���һ���߳��޸���ĳ��������ֵ������ֵ�������߳���˵�������ɼ��ġ�

//    ����2����ֹ����ָ��������




    //ʵ��Sticky�¼�
//    Sticky�¼�ָֻ�¼����������¼�����֮���ע���Ҳ�ܽ��յ����¼����������͡�
//   Android�о���������ʵ����Ҳ����Sticky Broadcast��
// ��ճ�Թ㲥�������������������߷�����ĳ���㲥��
// ��������������㲥���ͺ��ע���Լ���Receiver��
// ��ʱ�����߱��޷����յ��ղŵĹ㲥��Ϊ��Android������StickyBroadcast��
// �ڹ㲥���ͽ�����ᱣ��ոշ��͵Ĺ㲥��Intent����
// ������������ע����Receiver��Ϳ��Խ��յ��ղ��Ѿ������Ĺ㲥�����ʹ�����ǿ���Ԥ�ȴ���һЩ�¼���
// ����������ʱ�ٰ���Щ�¼�Ͷ�ݸ������ߡ�

    private static volatile RxBus instance;
    private final Subject<Object, Object> _bus;
    private final Map<Class<?>, Object> mStickyEventMap;


    private RxBus() {
//     SerializedSubject   �����ǹ۲����ݣ����ǹ۲��ߣ�������������ת��������
        _bus = new SerializedSubject<>(PublishSubject.create());
//        ConcurrentHashMap��һ���̰߳�ȫ��HashMap�� ����stripping lock������������Ч�ʱ�HashTable�ߺܶ�
//        ʵ��Sticky�¼�
        mStickyEventMap = new ConcurrentHashMap<>();
    }

    public static RxBus getInstance() {
        if (null == instance) {
            synchronized (RxBus.class) {
                if (null == instance) {
                    instance = new RxBus();
                }
            }
        }
        return instance;
    }




//    ��������
    public void send(Object object) {
        try{
            _bus.onNext(object);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void sendSticky(Object obj){
        synchronized (mStickyEventMap) {
            mStickyEventMap.put(obj.getClass(), obj);
        }
        send(obj);
    }

//    �ж��Ƿ���subject����Onservers
    public boolean hasObservers() {
        return _bus.hasObservers();
    }

    /**
     * ���ݴ��ݵ� eventType ���ͷ����ض�����(eventType)�� ���۲���
     */
    public <T> Observable<T> toObservable(Class<T> eventType) {
        return _bus.ofType(eventType);
        //        �����лС���ӵ�����: ofType = filter + cast
//        return bus.filter(new Func1<Object, Boolean>() {
//            @Override
//            public Boolean call(Object o) {
//                return eventType.isInstance(o);
//            }
//        }) .cast(eventType);
    }

//    ���յ�����
//    ���ص��Ǳ��۲���  ����ճ���¼�
    public <T> Observable<T> toObservableSticky(final Class<T> type) {
        synchronized (mStickyEventMap){
            Observable<T> observable = _bus.ofType(type);
            final Object obj = mStickyEventMap.get(type);
            if(type!=null){
                return observable.mergeWith(Observable.create(new Observable.OnSubscribe<T>(){
                    @Override
                    public void call(Subscriber<? super T> subscriber) {
                        subscriber.onNext(type.cast(obj));
                    }
                }));
//                        subscriber -> {
//                    subscriber.onNext(type.cast(obj));
//                })
            }else {
                return observable;
            }
        }
//        merge�����������Խ����Observables�ϲ����ͺ��������ǵ�����Observableһ����
//       filterֻ�з��Ϲ������������ݲŻᱻ�����䡱
//        cast��һ��Observableת����ָ�����͵�Observable
    }

//    Subject��ReactiveX����Ϊobserver��observerable��һ��bridge����proxy��
//    ��Ϊ����һ���۲��ߣ����������Զ���һ�������ɹ۲����
//    ͬʱ��Ϊ����һ���ɹ۲�������������Դ��ݺ��ͷ����۲⵽�����ݶ��󣬲������ͷ��µĶ���
//    ���⣬RxJava������������
//    ?PublishSubject
//    ?BehaviorSubject
//    ?ReplaySubject
//    ?AsyncSubject
//
//    1.1 AsyncSubject
//
//    AsyncSubject���ͷ�Observable�ͷŵ����һ�����ݣ����ҽ���Observable���֮��Ȼ�������Observable��Ϊ�쳣����ֹ��AsyncSubject�������ͷ��κ����ݣ����ǻ���Observer����һ���쳣֪ͨ��
//
//            1.2 BehaviorSubject
//
//    ��Observer������һ��BehaviorSubject����һ��ʼ�ͻ��ͷ�Observable����ͷŵ�һ�����ݶ��󣬵���û���κ������ͷ�ʱ��������һ��Ĭ��ֵ���������ͻ��ͷ�Observable�ͷŵ��������ݡ����Observable���쳣��ֹ��BehaviorSubject�������������Observer�ͷ����ݣ����ǻ���Observer����һ���쳣֪ͨ��
//
//            1.3 PublishSubject
//
//    PublishSubject������Observer�ͷ��ڶ���֮��Observable�ͷŵ����ݡ�
//
//            1.4 ReplaySubject
//
//    ����Observer��ʱ����ReplaySubject��ReplaySubject��������Observer�ͷ�Observable�ͷŹ������ݡ�
//    �в�ͬ���͵�ReplaySubject�������������޶�Replay�ķ�Χ�������趨Buffer�ľ����С�������趨�����ʱ�䷶Χ��
//    ���ʹ��ReplaySubject��ΪObserver��ע�ⲻҪ�ڶ���߳��е���onNext��onComplete��onError��������Ϊ��ᵼ��˳����ң������Υ����Observer����ġ�


//    ofType������ = filter������ + cast������
//    ?filterֻ�з��Ϲ������������ݲŻᱻ�����䡱
//            ?cast��һ��Observableת����ָ�����͵�Observable

//    ����Subject
//    http://www.mamicode.com/info-detail-987998.html
//    http://blog.csdn.net/sun927/article/details/44818845




//    ����ճ���¼��ķ���

//    ��ȡ�¼�
    public <T> T getStickyEvent(Class<T> type){
        synchronized (mStickyEventMap){
            return type.cast(mStickyEventMap.get(type));
        }
    }

//    �Ƴ�ָ���¼�
    public <T> T removeStickyEvent(Class<T> type){
        synchronized (mStickyEventMap){
            return type.cast(mStickyEventMap.remove(type));
        }
    }


//    �Ƴ����е�sticky�¼�
    public void removeAllStickyEvent(){
        synchronized (mStickyEventMap){
           mStickyEventMap.clear();
        }
    }

//    �������ĵ�
    public <T> Subscription toSubscription(Class<T> type, Observer<T> observer) {
        return toObservable(type).subscribe(observer);
    }

    public <T> Subscription toSubscription(Class<T> type, Action1<T> action1) {
        return toObservable(type).subscribe(action1);
    }

    public <T> Subscription toSubscription(Class<T> type, Action1<T> action1, Action1<Throwable> errorAction1) {
        return toObservable(type).subscribe(action1,errorAction1);
    }

}
