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
//    ?保证instance可见性
//    ?禁止指令重排
//volatile是一个类型修饰符（type specifier）。它是被设计用来修饰被不同线程访问和修改的变量
//    volatile的作用是： 作为指令关键字，确保本条指令不会因编译器的优化而省略，且要求每次直接读值.
//用volatile修饰的变量，线程在每次使用变量的时候，都会读取变量修改后的值
//    　当一个共享变量被volatile修饰时，它会保证修改的值会立即被更新到主存，当有其他线程需要读取时，它会去内存中读取新值。
//    指令重排序，一般来说，处理器为了提高程序运行效率，可能会对输入代码进行优化，
// 它不保证程序中各个语句的执行先后顺序同代码中的顺序一致，
// 但是它会保证程序最终执行结果和代码顺序执行的结果是一致的。


//    　一旦一个共享变量（类的成员变量、类的静态成员变量）被volatile修饰之后，那么就具备了两层语义：

//    　　1）保证了不同线程对这个变量进行操作时的可见性，即一个线程修改了某个变量的值，这新值对其他线程来说是立即可见的。

//    　　2）禁止进行指令重排序。




    //实现Sticky事件
//    Sticky事件只指事件消费者在事件发布之后才注册的也能接收到该事件的特殊类型。
//   Android中就有这样的实例，也就是Sticky Broadcast，
// 即粘性广播。正常情况下如果发送者发送了某个广播，
// 而接收者在这个广播发送后才注册自己的Receiver，
// 这时接收者便无法接收到刚才的广播，为此Android引入了StickyBroadcast，
// 在广播发送结束后会保存刚刚发送的广播（Intent），
// 这样当接收者注册完Receiver后就可以接收到刚才已经发布的广播。这就使得我们可以预先处理一些事件，
// 让有消费者时再把这些事件投递给消费者。

    private static volatile RxBus instance;
    private final Subject<Object, Object> _bus;
    private final Map<Class<?>, Object> mStickyEventMap;


    private RxBus() {
//     SerializedSubject   ，既是观察内容，又是观察者，起到桥梁／数据转发的作用
        _bus = new SerializedSubject<>(PublishSubject.create());
//        ConcurrentHashMap是一个线程安全的HashMap， 采用stripping lock（分离锁），效率比HashTable高很多
//        实现Sticky事件
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




//    发送请求
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

//    判断是否有subject绑定了Onservers
    public boolean hasObservers() {
        return _bus.hasObservers();
    }

    /**
     * 根据传递的 eventType 类型返回特定类型(eventType)的 被观察者
     */
    public <T> Observable<T> toObservable(Class<T> eventType) {
        return _bus.ofType(eventType);
        //        这里感谢小鄧子的提醒: ofType = filter + cast
//        return bus.filter(new Func1<Object, Boolean>() {
//            @Override
//            public Boolean call(Object o) {
//                return eventType.isInstance(o);
//            }
//        }) .cast(eventType);
    }

//    接收到请求
//    返回的是被观察者  具有粘性事件
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
//        merge操作符：可以将多个Observables合并，就好像它们是单个的Observable一样。
//       filter只有符合过滤条件的数据才会被“发射”
//        cast将一个Observable转换成指定类型的Observable
    }

//    Subject在ReactiveX是作为observer和observerable的一个bridge或者proxy。
//    因为它是一个观察者，所以它可以订阅一个或多个可观察对象，
//    同时因为他是一个可观测对象，所以它可以传递和释放它观测到的数据对象，并且能释放新的对象。
//    主题，RxJava里有四种主题
//    ?PublishSubject
//    ?BehaviorSubject
//    ?ReplaySubject
//    ?AsyncSubject
//
//    1.1 AsyncSubject
//
//    AsyncSubject仅释放Observable释放的最后一个数据，并且仅在Observable完成之后。然而如果当Observable因为异常而终止，AsyncSubject将不会释放任何数据，但是会向Observer传递一个异常通知。
//
//            1.2 BehaviorSubject
//
//    当Observer订阅了一个BehaviorSubject，它一开始就会释放Observable最近释放的一个数据对象，当还没有任何数据释放时，它则是一个默认值。接下来就会释放Observable释放的所有数据。如果Observable因异常终止，BehaviorSubject将不会向后续的Observer释放数据，但是会向Observer传递一个异常通知。
//
//            1.3 PublishSubject
//
//    PublishSubject仅会向Observer释放在订阅之后Observable释放的数据。
//
//            1.4 ReplaySubject
//
//    不管Observer何时订阅ReplaySubject，ReplaySubject会向所有Observer释放Observable释放过的数据。
//    有不同类型的ReplaySubject，它们是用来限定Replay的范围，例如设定Buffer的具体大小，或者设定具体的时间范围。
//    如果使用ReplaySubject作为Observer，注意不要在多个线程中调用onNext、onComplete和onError方法，因为这会导致顺序错乱，这个是违反了Observer规则的。


//    ofType操作符 = filter操作符 + cast操作符
//    ?filter只有符合过滤条件的数据才会被“发射”
//            ?cast将一个Observable转换成指定类型的Observable

//    关于Subject
//    http://www.mamicode.com/info-detail-987998.html
//    http://blog.csdn.net/sun927/article/details/44818845




//    处理粘性事件的方法

//    获取事件
    public <T> T getStickyEvent(Class<T> type){
        synchronized (mStickyEventMap){
            return type.cast(mStickyEventMap.get(type));
        }
    }

//    移除指定事件
    public <T> T removeStickyEvent(Class<T> type){
        synchronized (mStickyEventMap){
            return type.cast(mStickyEventMap.remove(type));
        }
    }


//    移除所有的sticky事件
    public void removeAllStickyEvent(){
        synchronized (mStickyEventMap){
           mStickyEventMap.clear();
        }
    }

//    用来订阅的
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
