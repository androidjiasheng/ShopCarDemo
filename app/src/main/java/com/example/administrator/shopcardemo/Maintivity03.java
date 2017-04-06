package com.example.administrator.shopcardemo;
/**
 * Created by better_001 on 2017/1/6.
 */

import android.app.Activity;
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
import rx.observables.ConnectableObservable;
import rx.observables.JoinObservable;
import rx.observables.MathObservable;
import rx.schedulers.Schedulers;
import rx.schedulers.TimeInterval;
import rx.schedulers.Timestamped;

/**
 * ???????? on 2017/1/6 17:51
 */
public class Maintivity03 extends AppCompatActivity {
    private String mess = "";
    private String  observermess= "";
    private EditText inputtext;
    private TextView mTextView;
    private Boolean flag = false;
    private ArrayList<String> mlist;
    private ArrayList<String> mlist2;
    private ArrayList<Integer> mIntList;
    private ArrayList<Integer> mIntList2;
    private Demo mDemo;
    private Integer mI = 0;

    private Observer<String> subscriber = new Observer<String>() {
        @Override
        public void onCompleted() {
            Message message = mHandler.obtainMessage();
            message.obj = observermess+="ִ��onCompleted��";
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
        mlist.add("��������");
        mlist.add("����֮��");
        mlist.add("�~");
        mlist.add("�����e�|��");
        mlist.add("��ҾͲ�Ҫ������");

        mlist2 = new ArrayList<>();
        mlist2.add("��һ��");
        mlist2.add("�ڶ���");
        mlist2.add("������");
        mlist2.add("���ĸ�");
        mlist2.add("�����");

        mIntList = new ArrayList<>();
        mIntList.add(1);
        mIntList.add(3);
        mIntList.add(5);
        mIntList.add(7);
        mIntList.add(9);
        mIntList2 = new ArrayList<>();
        mIntList2.add(2);
        mIntList2.add(4);
        mIntList2.add(6);
        mIntList2.add(8);
        mIntList2.add(10);

        mDemo = new Demo();
        mDemo.setName("��һ��");
        mDemo.setCurson(mlist);
    }
    public void just(View v) {
        relieve();
        mTextView.setText("");
        mess = "";
        Observable.just("�Ҿ�", "����", "Ȼ����", "�͛]����", "�Һ�����", "�ҾͿ���")
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
                .map(integer -> mess += "just ��ǰ�Ĕ��ֵĴ�С" + integer)
                .subscribe(subscriber);
        deferObservable.map(integer -> mess += "defer ��ǰ�Ĕ��ֵĴ�С" + integer)
                .subscribe(subscriber);
    }
    public void timer(View v) {
        relieve();
        mTextView.setText("");
        mess = "";
        Observable
                .timer(1, TimeUnit.SECONDS, Schedulers.io())
                .map(aLong -> "���t1������һ������" + aLong).subscribe(subscriber);
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
                    message.obj = "ÿ��һ������һ������" + aLong;
                    mHandler.sendMessage(message);
                }, throwable -> {
                    Log.e("showme", "�����쳣 �����ֹͣ");
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
        Observable.just("��", "��", "��", "��", "��")
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

        observable.map(integer -> integer + "�D�Q���ַ���").subscribe(subscriber);
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
//                        �����ַ����L�ȷֽM
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
                    Log.e("showme", "�{�ôΔ�");
                    mess+="�{�ôΔ�\n";
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
                                return s+"�����˵�һ��Func1";
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
                                return s+"�����˵ڶ���Func1";
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

//        �M�϶���ObservableȻ��ֻ�l��һ��Observable
        Observable.merge(observable1, observable2)
                .subscribe(subscriber);

    }
    public void StartWith(View v){
        relieve();
        Observable.from(mlist)
                .startWith("��ʼ��")
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        return mess+=s;
                    }
                }).subscribe(subscriber);
    }
    public void SwitchMap(View v){
        relieve();
        mess+="ִ���˲����Ľ��"+"\n";
        Observable.from(mlist)
                .switchMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String s) {
                        Observable<String> ob = Observable.just(s);
                        return ob;
                    }
                }).map(s -> mess+=s).subscribe(subscriber);
        mess+=mess+"\n"+"��ͬһ���߳�ִ�еĽ��"+"\n";
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
                return Observable.just(mess+="\n��6��ʱ��������\n");
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
//                       ������6��ʱ�������
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
                .just("����")
                .delay(3,TimeUnit.SECONDS)
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        return mess+="�ӳ���3��";
                    }
                }).subscribe(subscriber);
    }
    public void DoOnNext(View v){
        relieve();
        Observable.just(1,2,3,4,5)
                .doOnNext(integer ->
                        ToastUtil.getLongToastByString(Maintivity03.this,integer+""))
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
                .contains("��������")
                .map(new Func1<Boolean, String>() {
                    @Override
                    public String call(Boolean aBoolean) {
                        return aBoolean.toString();
                    }
                }).subscribe(subscriber);
    }
    public void DefaultIfEmpty (View v){
        relieve();
        Observable.empty()
                .defaultIfEmpty("û��ֵ")
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        ToastUtil.getLongToastByString(Maintivity03.this,o.toString());
                    }
                });
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
        Observable<String> observable2 =  Observable.from(mlist2).delay(2,TimeUnit.SECONDS);
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
                        return s.length()<6;
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
        Observable<String> observable2 =  Observable.from(mlist2).delay(1,TimeUnit.SECONDS);

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
        MathObservable.averageInteger(  Observable.from(mIntList))
                .map(new Func1<Integer, String>() {
                    @Override
                    public String call(Integer integer) {
                        return integer+"";
                    }
                }).subscribe(subscriber);
    }
    public void Concat (View v){
        relieve();
        Observable<Integer> observable1 = Observable.from(mIntList);
        Observable<Integer> observable2 = Observable.from(mIntList2);
        Observable.concat(observable1,observable2)
                .map(new Func1<Integer, String>() {
                    @Override
                    public String call(Integer integer) {
                        return mess+=integer+" ";
                    }
                }).subscribe(subscriber);
    }
    public void Count (View v){
        relieve();
        Observable.from(mIntList)
//        Observable.just(1,2,3)
                .count()
                .map(new Func1<Integer, String>() {
                    @Override
                    public String call(Integer integer) {
                        return mess+=integer.toString()+"???";
                    }
                }).subscribe(subscriber);
    }
    public void Max (View v){
        relieve();
        MathObservable.max(Observable.from(mIntList))
                .map(new Func1<Integer, String>() {
                    @Override
                    public String call(Integer integer) {
                        return integer+"";
                    }
                }).subscribe(subscriber);
    }
    public void Min (View v){
        relieve();
        MathObservable.min(Observable.from(mIntList))
                .map(new Func1<Integer, String>() {
                    @Override
                    public String call(Integer integer) {
                        return integer+"";
                    }
                }).subscribe(subscriber);
    }
    public void Reduce (View v){
        relieve();
        Observable.from(mIntList)
                .reduce(new Func2<Integer, Integer, Integer>() {
                    @Override
                    public Integer call(Integer integer, Integer integer2) {
                        return integer-integer2;
                    }
                }).map(new Func1<Integer, String>() {
            @Override
            public String call(Integer integer) {
                return integer.toString();
            }
        }).subscribe(subscriber);
    }
    public void Sum (View v){
        relieve();
        MathObservable.sumInteger(Observable.from(mIntList))
                .map(new Func1<Integer, String>() {
                    @Override
                    public String call(Integer integer) {
                        return integer+"";
                    }
                }).subscribe(subscriber);
    }

//  Backpressure Operators


    //  Connectable Observable Operators
    public void PublicAndConnect (View v){
        relieve();
        Observable<String> observable = Observable.from(mlist);
        ConnectableObservable<String> stringConnectableObservable = observable.publish();
        stringConnectableObservable.subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                mess+="action one"+s;
                Message message = mHandler.obtainMessage();
                message.obj = mess;
                mHandler.sendMessage(message);
            }
        });
        stringConnectableObservable.subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                mess+="action two"+s;
                Message message = mHandler.obtainMessage();
                message.obj = mess;
                mHandler.sendMessage(message);
            }
        });

        stringConnectableObservable.connect();
    }

    public void RefCount (View v){
        relieve();
        Observable<String> observable = Observable.from(mlist);
        ConnectableObservable<String> stringConnectableObservable = observable.publish();
        Observable<String> observable1 = stringConnectableObservable.refCount();
        final Subscriber<String> subscriberone = new Subscriber<String>() {
            @Override
            public void onNext(String integer) {
                mess+="subscriberone next";
                Message message = mHandler.obtainMessage();
                message.obj = mess;
                mHandler.sendMessage(message);
            }
            @Override
            public void onCompleted() {
                mess+="subscriberone onCompleted";
                Message message = mHandler.obtainMessage();
                message.obj = mess;
                mHandler.sendMessage(message);
            }
            @Override
            public void onError(Throwable e) {
                mess+="subscriberone onError";
                Message message = mHandler.obtainMessage();
                message.obj = mess;
                mHandler.sendMessage(message);
            }
        };

        Subscriber<String> subscriber2 = new Subscriber<String>() {
            @Override
            public void onNext(String integer) {
                mess+="subscriber2 onNext" + " " + integer.length();
                Message message = mHandler.obtainMessage();
                message.obj = mess;
                mHandler.sendMessage(message);
                if(integer.length()==4){
                    subscriberone.unsubscribe();
                }else if(integer.length()==6){
                    observable1.subscribe(subscriberone);
                }
            }
            @Override
            public void onCompleted() {
                mess+="subscriber2 onCompleted";
                Message message = mHandler.obtainMessage();
                message.obj = mess;
                mHandler.sendMessage(message);
            }
            @Override
            public void onError(Throwable e) {
                mess+="subscriber2 onError";
                Message message = mHandler.obtainMessage();
                message.obj = mess;
                mHandler.sendMessage(message);
            }
        };

        observable1.subscribe(subscriber2);
        observable1.subscribe(subscriberone);
    }
    public void Replay (View v){
        relieve();
        Observable<String> observable = Observable.from(mlist).replay();
        ConnectableObservable<String> stringConnectableObservable = observable.publish();
        stringConnectableObservable.subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                mess+="action one"+s;
                Message message = mHandler.obtainMessage();
                message.obj = mess;
                mHandler.sendMessage(message);
                if(s.length()==6){
                    stringConnectableObservable.subscribe(new Action1<String>() {
                        @Override
                        public void call(String s) {
                            mess+="action two"+s;
                            Message message = mHandler.obtainMessage();
                            message.obj = mess;
                            mHandler.sendMessage(message);
                        }
                    });
                }
            }
        });
        stringConnectableObservable.connect();
    }

    //  Operators to Convert Observables
    public void To  (View v){
        relieve();
        Observable.from(mlist)
                .to(new Func1<Observable<String>, Observable<Integer>>() {
                    @Override
                    public Observable<Integer> call(Observable<String> stringObservable) {
                        return stringObservable.count();
                    }
                }).map(new Func1<Integer, String>() {
            @Override
            public String call(Integer integer) {
                return mess+=integer.toString()+"";
            }
        }).subscribe(subscriber);
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
