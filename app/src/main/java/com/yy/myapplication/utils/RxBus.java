package com.yy.myapplication.utils;

import io.reactivex.Flowable;
import io.reactivex.processors.FlowableProcessor;
import io.reactivex.processors.PublishProcessor;

public class RxBus {
    private final FlowableProcessor<Object> mBus;

    private RxBus() {
        mBus = PublishProcessor.create().toSerialized();
    }

    private static class Holder {
        private static RxBus instance = new RxBus();
    }

    public static RxBus getInstance() {
        return Holder.instance;
    }

    public void post(Object obj) {
        mBus.onNext(obj);
    }

    public <T> Flowable<T> register(Class<T> clz) {
        return mBus.ofType(clz);
    }

    public void unregisterAll() {
        //解除注册
        mBus.onComplete();
    }

    public boolean hasSubscribers() {
        return mBus.hasSubscribers();
    }
}