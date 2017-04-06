package com.example.administrator.shopcardemo;
/**
 * Created by better_001 on 2017/1/6.
 */

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.administrator.shopcardemo.bean.Demo;
import com.jakewharton.rxbinding.widget.RxTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Notification;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.joins.Pattern2;
import rx.joins.Plan0;
import rx.observables.JoinObservable;
import rx.schedulers.Schedulers;
import rx.schedulers.TimeInterval;
import rx.schedulers.Timestamped;

/**
 * ???????? on 2017/1/6 17:51
 */
public class MainActivity02 extends AppCompatActivity {
    private String mess = "";
    private String  observermess= "";
    private EditText inputtext;
    private TextView mTextView;
    private Boolean flag = false;
    private ArrayList<String> mlist;
    private ArrayList<String> mlist2;
    private Demo mDemo;
    private Integer mI = 0;

    private Observer<String> subscriber = new Observer<String>() {
        @Override
        public void onCompleted() {
            Message message = mHandler.obtainMessage();
            message.obj = observermess+="执行onCompleted了";
            mHandler.sendMessage(message);
        }

        @Override
        public void onError(Throwable e) {
            Message message = mHandler.obtainMessage();
            message.obj = e.toString();
            mHandler.sendMessage(message);
        }

        @Override
        public void onNext(String s) {
            observermess=s;
            Log.e("showme",s);
            Message message = mHandler.obtainMessage();
            message.obj = s;
            mHandler.sendMessage(message);
        }
    };

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String s = (String) msg.obj;
            mTextView.setText(s);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        inputtext = (EditText) findViewById(R.id.input_text);
        mTextView = (TextView) findViewById(R.id.showtext);
        initData();
//        debounce();
    }

    //    Creating Observables
    private void initData() {
        mlist = new ArrayList<>();
        mlist.add("我在仰望");
        mlist.add("月亮之上");
        mlist.add("額");
        mlist.add("好像唱錯東西");
        mlist.add("大家就不要介意了");

        mlist2 = new ArrayList<>();
        mlist2.add("第一个");
        mlist2.add("第二个");
        mlist2.add("第三个");
        mlist2.add("第四个");
        mlist2.add("第五个");

        mDemo = new Demo();
        mDemo.setName("看一看");
        mDemo.setCurson(mlist);
    }
    public void just(View v) {
        relieve();
        mTextView.setText("");
        mess = "";
        Observable.just("我就", "看看", "然後呢", "就沒有了", "我很善心", "我就看看")
                .map(s -> mess += s)
                .subscribe(subscriber);
    }
    public void from(View v) {
        relieve();
        mTextView.setText("");
        mess = "";

        Observable.from(mlist)
                .map(s -> mess += s)
                .subscribe(subscriber);
    }
    public void defer(View v) {
        relieve();
        mTextView.setText("");
        mess = "";
        mI = 10;
        Observable<Integer> justObservable = Observable.just(mI);
        mI = 12;
        Observable<Integer> deferObservable = Observable.defer(() -> Observable.just(mI));
        mI = 15;
        justObservable
                .map(integer -> mess += "just 當前的數字的大小" + integer)
                .subscribe(subscriber);
        deferObservable.map(integer -> mess += "defer 當前的數字的大小" + integer)
                .subscribe(subscriber);
    }
    public void timer(View v) {
        relieve();
        mTextView.setText("");
        mess = "";
        Observable
                .timer(1, TimeUnit.SECONDS, Schedulers.io())
                .map(aLong -> "延遲1秒生成一個數字" + aLong).subscribe(subscriber);
    }
    public void interval(View v) {
        relieve();
        mTextView.setText("");
        mess = "";
        Observable
                .interval(1, 1, TimeUnit.SECONDS, Schedulers.io())
                .subscribe(aLong -> {
                    Log.e("showme", aLong + "");
                    if (aLong > 10l) {
                        mI = 1 / 0;
                    }
                    Message message = mHandler.obtainMessage();
                    message.obj = "每隔一秒生成一個數字" + aLong;
                    mHandler.sendMessage(message);
                }, throwable -> {
                    Log.e("showme", "发生异常 让这个停止");
                });
    }
    public void range(View v) {
        relieve();
        mTextView.setText("");
        mess = "";
        Observable
                .range(0, 100)
                .map(integer -> mess += "range" + integer)
                .subscribe(subscriber);
    }
    public void repeat(View v) {
        relieve();
        mTextView.setText("");
        mess = "";
        Observable
                .just(1, 2)
                .repeat(3)
                .map(integer -> mess += integer + "")
                .subscribe(subscriber);
    }

    //    Transforming Observables
    public void buffer(View v) {
        relieve();
        mTextView.setText("");
        mess = "";
        Observable.just("我", "就", "看", "看", "哈")
                .buffer(2)
                .subscribe(new Subscriber<List<String>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<String> strings) {
                        for (String s : strings) {
                            mess += s;
                        }
                        mess += strings.toString() + "\n";
                        Message message = mHandler.obtainMessage();
                        message.obj = mess;
                        mHandler.sendMessage(message);
                    }
                });
    }
    public void map(View v) {
        relieve();
        mTextView.setText("");
        mess = "";
        Observable<Integer> observable = Observable.create(subscriber1 -> {
            subscriber1.onNext(123);
        });

        observable.map(integer -> integer + "轉換成字符串").subscribe(subscriber);
    }
    public void flatMap(View v) {
        relieve();
        mTextView.setText("");
        mess = "";
        Observable<Demo> demoObservable = Observable.create(new Observable.OnSubscribe<Demo>() {
            @Override
            public void call(Subscriber<? super Demo> subscriber) {
                subscriber.onNext(mDemo);
            }
        });

        demoObservable.flatMap(new Func1<Demo, Observable<String>>() {
            @Override
            public Observable<String> call(Demo demo) {
                return Observable.from(demo.getCurson());
            }
        }).map(s -> mess += s).subscribe(subscriber);
    }
    public void groupby(View v) {
        relieve();
        mTextView.setText("");
        mess = "";
        Observable.from(mlist)
                .groupBy(s -> {
//                        按照字符串長度分組
                    return s.length();
                }).subscribe(integerStringGroupedObservable -> {
            integerStringGroupedObservable
                    .map(s -> mess+=s+"   key="+integerStringGroupedObservable.getKey()+"\n")
                    .subscribe(subscriber);
        });
    }
    public void scan(View v){
        relieve();
        mTextView.setText("");
        mess = "";
        Observable.just(1,2,3,4,5,6,7,8,9)
                .scan((integer, integer2) -> integer+integer2)
                .map(integer -> mess+=integer+" \n")
                .subscribe(subscriber);
    }
    public void window(View v){
        relieve();
        mTextView.setText("");
        mess = "";
        Observable.interval(1,1,TimeUnit.SECONDS)
                .window(3,TimeUnit.SECONDS)
                .subscribe(longObservable -> {
                    Log.e("showme", "調用次數");
                    mess+="調用次數\n";
                    if (flag) {
                        int a = 10 / 0;
                    }
                    longObservable
                            .map(aLong -> {
                                if (aLong > 10L) {
                                    flag = true;
                                }
                                return mess += aLong + "\n";
                            }).subscribe(subscriber);
                }, throwable -> {
                    Log.e("showme",throwable.toString());
                });
    }

    //    Filtering Observables
    public void debounce(View v){
        relieve();
        inputtext = (EditText) findViewById(R.id.input_text);
        RxTextView.textChanges(inputtext)
                .debounce(400,TimeUnit.MILLISECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .map(charSequence -> charSequence.toString()).subscribe(subscriber);
    }
    public void distinct(View v){
        relieve();
        Observable.just(1,2,3,4,5,6,1,2,7,8)
                .distinct()
                .map(new Func1<Integer, String>() {
                    @Override
                    public String call(Integer integer) {
                        return mess+=integer+"";
                    }
                })
                .subscribe(subscriber);
    }
    public void ElementAt(View v){
        relieve();
        Observable.from(mlist)
                .elementAt(2)
                .subscribe(subscriber);
    }
    public void Filter(View v){
        relieve();
        inputtext = (EditText) findViewById(R.id.input_text);
        RxTextView.textChanges(inputtext)
                .filter(charSequence -> {
                    int length = charSequence.length();
                    if(length>0){
                        char a = charSequence.charAt(length-1);
                        return a>'a'&& a<'z';
                    }else {
                        return false;
                    }
                }).map(charSequence -> charSequence.toString())
                .subscribe(subscriber);
    }
    public void First(View v){
        relieve();
        Observable.from(mlist)
                .first()
                .subscribe(subscriber);
    }
    public void IgnoreElements(View v){
        relieve();
        Observable.from(mlist)
                .ignoreElements()
                .subscribe(subscriber);
    }
    public void Last(View v){
        relieve();
        Observable.from(mlist)
                .last()
                .subscribe(subscriber);
    }
    public void Sample(View v){
        relieve();
        Observable
                .interval(1, TimeUnit.SECONDS)
                .sample(2, TimeUnit.SECONDS)
                .map(aLong -> {
                    if(aLong>10L){
                        long a = aLong / 0;
                    }
                    return mess+=aLong+" ";
                }).subscribe(subscriber);
    }
    public void Skip(View v){
        relieve();
        Observable.from(mlist)
                .skip(2)
                .map(s -> mess+=s)
                .subscribe(subscriber);
    }
    public void SkipLast(View v){
        relieve();
        Observable.from(mlist)
                .skipLast(2)
                .map(s -> mess+=s)
                .subscribe(subscriber);
    }
    public void Take(View v){
        relieve();
        Observable.from(mlist)
                .take(2)
                .map(s -> mess+=s)
                .subscribe(subscriber);
    }
    public void TakeLast(View v){
        relieve();
        Observable.from(mlist)
                .takeLast(2)
                .map(s -> mess+=s)
                .subscribe(subscriber);
    }

    //    Combining Observables
    public void AndThenWhen(View v){
        relieve();
        Observable<String> just1 = Observable.just("A", "B");
        Observable<Integer> just2 = Observable.just(1, 2, 3);

        Pattern2<String, Integer> pattern = JoinObservable.from(just1).and(just2);
        Plan0<String> plan = pattern.then((s, integer) -> s + integer);

        JoinObservable.when(plan)
                .toObservable()
                .map(s -> mess+=s+"\n")
                .subscribe(subscriber);
    }
    public void CombineLatest(View v){
        relieve();
        Observable<String> observable1 = Observable.from(mlist);
        Observable<String> observable2 = Observable.from(mlist2);
        Observable.combineLatest(observable1, observable2, (s, s2) -> s+" "+s2)
                .map(s -> mess+=s).subscribe(subscriber);
    }
    public void Join(View v){
        relieve();
        Observable<String> observable1 = Observable.from(mlist);
        Observable<String> observable2 = Observable.just("1","test","complete");
        observable1.join(observable2, new Func1<String, Observable<String>>() {
            @Override
            public Observable<String> call(String s) {
                return Observable.just(s)
                        .map(new Func1<String, String>() {
                            @Override
                            public String call(String s) {
                                return s+"经过了第一个Func1";
                            }
                        });
            }
        }, new Func1<String, Observable<String>>() {
            @Override
            public Observable<String> call(String s) {
                return Observable.just(s)
                        .map(new Func1<String, String>() {
                            @Override
                            public String call(String s) {
                                return s+"经过了第二个Func1";
                            }
                        });
            }
        }, new Func2<String, String, String>() {
            @Override
            public String call(String s, String s2) {
                return s+s2;
            }
        }).map(s -> mess+=s).subscribe(subscriber);
    }
    public void merge(View v) {
        relieve();
        mTextView.setText("");
        mess = "";
        Observable<String> observable1 = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                mess += "observable1";
                subscriber.onNext(mess);
            }
        });

        Observable<String> observable2 = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                mess += "observable2";
                subscriber.onNext(mess);
                mess += "observable3";
                subscriber.onNext(mess);
            }
        });

//        組合多個Observable然後只發出一個Observable
        Observable.merge(observable1, observable2)
                .subscribe(subscriber);

    }
    public void StartWith(View v){
        relieve();
        Observable.from(mlist)
                .startWith("开始了")
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        return mess+=s;
                    }
                }).subscribe(subscriber);
    }
    public void SwitchMap(View v){
        relieve();
        mess+="执行了并发的结果"+"\n";
        Observable.from(mlist)
                .switchMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String s) {
                        Observable<String> ob = Observable.just(s);
                        return ob;
                    }
                }).map(s -> mess+=s).subscribe(subscriber);
        mess+=mess+"\n"+"在同一个线程执行的结果"+"\n";
        Observable.from(mlist)
                .switchMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String s) {
                        Observable<String> ob = Observable.just(s).subscribeOn(AndroidSchedulers.mainThread());
                        return ob;
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .map(s -> mess+=s).subscribe(subscriber);
    }
    public void Zip(View v){
        relieve();
        Observable<String> o1 = Observable.from(mlist);
        Observable<String> o2 = Observable.from(mlist2);
        Observable.zip(o1, o2, (s, s2) -> s+s2).map(s -> mess+=s+" ").subscribe(subscriber);
    }

    //  Error Handling Operators
    public void Catch(View v){
        relieve();
        Observable.just(1,2,3,4,5,6,7,8,9)
                .map(new Func1<Integer, String>() {
                    @Override
                    public String call(Integer integer) {
//                        ???????
                        if(integer==6){
                            int a = integer/0;
                        }
                        return mess+=integer+"";
                    }
                }).onErrorResumeNext(new Func1<Throwable, Observable<? extends String>>() {
            @Override
            public Observable<? extends String> call(Throwable throwable) {
                return Observable.just(mess+="\n到6的时候发生错误\n");
            }
        })
                .subscribe(subscriber);
    }
    boolean flagerror = true;
    public void Retry(View v){
        relieve();
        Observable.just(1,2,3,4,5,6,7,8,9)
                .map(new Func1<Integer, String>() {
                    @Override
                    public String call(Integer integer) {
//                       当等于6的时候出问题
                        if(flagerror&&integer==6){
                            flagerror =!flagerror;
                            int a = integer/0;
                        }
                        return mess+=integer+"";
                    }
                }).retry()
                .subscribe(subscriber);

    }

    //    Observable Utility Operators
    public void Delay(View v){
        relieve();
        Observable
                .just("看看")
                .delay(3,TimeUnit.SECONDS)
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        return mess+="延迟了3秒";
                    }
                }).subscribe(subscriber);
    }
    public void DoOnNext(View v){
        relieve();
        Observable.just(1,2,3,4,5)
                .doOnNext(integer ->
                        ToastUtil.getLongToastByString(MainActivity02.this,integer+""))
                .map(integer -> mess+=integer)
                .subscribe(subscriber);
    }
    public void Materialize(View v){
        relieve();
    }
    public void Dematerialize(View v){
        relieve();
    }
    public void ObserveOn(View v){
        relieve();
        Observable
                .just(1,2,3,4,5,6)
                .delay(10,TimeUnit.SECONDS)
                .observeOn(Schedulers.io())
                .map(new Func1<Integer, String>() {
                    @Override
                    public String call(Integer integer) {
                        return mess+=integer;
                    }
                }).subscribe(subscriber);
    }
//    TODO
    public void Serialize(View v){
        relieve();
    }
    public void Subscribe(View v){
        relieve();
    }
    public void SubscribeOn(View v){
        relieve();
    }
    public void TimeInterval(View v){
        relieve();
        Observable.from(mlist)
                .timeInterval()
                .map(new Func1<TimeInterval<String>, String>() {
                    @Override
                    public String call(TimeInterval<String> stringTimeInterval) {
                        return stringTimeInterval.getValue()+"  "+stringTimeInterval.getIntervalInMilliseconds();
                    }
                })
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        return mess+=s;
                    }
                })
                .subscribe(subscriber);
    }
    public void Timeout(View v){
        relieve();
        Observable.just("haha")
                .delay(5,TimeUnit.SECONDS)
                .timeout(3,TimeUnit.SECONDS)
                .subscribe(subscriber);
    }
    public void Timestamp(View v){
        relieve();
        Observable.from(mlist)
                .timestamp()
                .map(new Func1<Timestamped<String>, String>() {
                    @Override
                    public String call(Timestamped<String> stringTimestamped) {
                        return stringTimestamped.toString();
                    }
                })
                .subscribe(subscriber);
    }
    public void Using (View v){
        relieve();
    }

    //    Conditional and Boolean Operators
    public void All (View v){
        relieve();
        Observable.from(mlist)
                .all(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String s) {
                        return s.length()>4;
                    }
                }).map(new Func1<Boolean, String>() {
            @Override
            public String call(Boolean aBoolean) {
                return mess+=aBoolean.toString()+" ";
            }
        }).subscribe(subscriber);
    }
    public void Amb (View v){
        relieve();
        Observable<String> observable1 = Observable.from(mlist).delay(3,TimeUnit.SECONDS);
        Observable<String> observable2 = Observable.from(mlist2);

        observable1.ambWith(observable2)
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        return mess+=s;
                    }
                }).subscribe(subscriber);
    }
    public void Contains (View v){
        relieve();
        Observable.from(mlist)
                .contains("我在仰望")
                .map(new Func1<Boolean, String>() {
                    @Override
                    public String call(Boolean aBoolean) {
                        return aBoolean.toString();
                    }
                }).subscribe(subscriber);
    }
    public void DefaultIfEmpty (View v){
        relieve();
        Observable.just("")
                .defaultIfEmpty("没有值")
                .subscribe(subscriber);
    }
    public void SequenceEqual (View v){
        relieve();
        Observable<String> observable1 =  Observable.from(mlist).delay(3,TimeUnit.SECONDS);
        Observable<String> observable2 =  Observable.from(mlist);
        Observable.sequenceEqual(observable1,observable2)
                .map(aBoolean -> aBoolean.toString())
                .subscribe(subscriber);
    }
    public void SkipUntil (View v){
        relieve();
        Observable<String> observable1 =  Observable.from(mlist).delay(1,TimeUnit.SECONDS);
        Observable<String> observable2 =  Observable.from(mlist2).delay(3,TimeUnit.SECONDS);
        observable1.skipUntil(observable2)
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        return mess+=s;
                    }
                }).subscribe(subscriber);
    }
    public void SkipWhile (View v){
        relieve();
        Observable.from(mlist)
                .skipWhile(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String s) {
                        return s.length()>4;
                    }
                }).map(new Func1<String, String>() {
            @Override
            public String call(String s) {
                return mess+=s;
            }
        }).subscribe(subscriber);
    }
    public void TakeUntil (View v){
        relieve();
        Observable<String> observable1 =  Observable.from(mlist).delay(1,TimeUnit.SECONDS);
        Observable<String> observable2 =  Observable.from(mlist2).delay(3,TimeUnit.SECONDS);
        observable1.takeUntil(observable2)
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        return mess+=s;
                    }
                }).subscribe(subscriber);
    }
    public void TakeWhile (View v){
        relieve();
        Observable<String> observable1 =  Observable.from(mlist);
        observable1.takeWhile(new Func1<String, Boolean>() {
            @Override
            public Boolean call(String s) {
                return s.length()<5;
            }
        }).map(new Func1<String, String>() {
            @Override
            public String call(String s) {
                return mess+=s;
            }
        }).subscribe(subscriber);
    }

    //  Mathematical and Aggregate Operators
    public void Average (View v){
        relieve();
    }
    public void Concat (View v){
        relieve();
    }
    public void Count (View v){
        relieve();
    }
    public void Max (View v){
        relieve();
    }
    public void Min (View v){
        relieve();
    }
    public void Reduce (View v){
        relieve();
    }
    public void Sum (View v){
        relieve();
    }

//  Backpressure Operators


    //  Connectable Observable Operators
    public void Connect (View v){
        relieve();
    }
    public void Publish (View v){
        relieve();
    }
    public void RefCount (View v){
        relieve();
    }
    public void Replay (View v){
        relieve();
    }

    //  Operators to Convert Observables
    public void To  (View v){
        relieve();
    }

    //  ???
    public void relieve() {
//        if(subscriber.isUnsubscribed()){
//            subscriber.unsubscribe();
//        }
        mTextView.setText("");
        mess = "";
    }
}
