package com.njcwking.box.crop.bus;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

public class RxBus {
    private static volatile RxBus defaultInstance;
    // 主题
    private final Subject bus;

    // PublishSubject只会把在订阅发生的时间点之后来自原始Observable的数据发射给观察者
    public RxBus() {
        bus = PublishSubject.create().toSerialized();
    }

    // 单例RxBus
    public static RxBus getDefault() {
        RxBus rxBus = defaultInstance;
        if (defaultInstance == null) {
            synchronized (RxBus.class) {
                rxBus = defaultInstance;
                if (defaultInstance == null) {
                    rxBus = new RxBus();
                    defaultInstance = rxBus;
                }
            }
        }
        return rxBus;
    }

    /**
     * 提供了一个新的事件,单一类型
     *
     * @param o 事件数据
     */
    public void post(Object o) {
        bus.onNext(o);
    }

    /**
     * 根据传递的 eventType 类型返回特定类型(eventType)的 被观察者
     *
     * @param eventType 事件类型
     * @param <T>
     * @return
     */
    public <T> Observable<T> toObservable(Class<T> eventType) {
        return bus.ofType(eventType);
    }

    /**
     * 提供了一个新的事件,根据code进行分发
     *
     * @param shortMsg 事件shortMsg
     * @param o
     */
    public void post(String shortMsg, Object o) {
        bus.onNext(new Message(shortMsg, o));

    }

    /**
     * 根据传递的code和 eventType 类型返回特定类型(eventType)的 被观察者
     *
     * @param shortMsg  事件shortMsg
     * @param eventType 事件类型
     * @param <T>
     * @return
     */
    public <T> Observable<T> toObservable(final String shortMsg, final Class<T> eventType) {
        return bus.ofType(Message.class)
                .filter(new Predicate() {
                    @Override
                    public boolean test(Object o) throws Exception {
                        return shortMsg.equals(((Message) o).getShortMsg()) && eventType.isInstance(((Message) o).getObject());
                    }
                }).map(new Function() {
                    @Override
                    public Object apply(Object o) throws Exception {
                        return ((Message) o).getObject();
                    }
                }).cast(eventType);
    }
}