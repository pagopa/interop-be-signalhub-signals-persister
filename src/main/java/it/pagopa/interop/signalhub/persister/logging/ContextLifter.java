package it.pagopa.interop.signalhub.persister.logging;

import org.reactivestreams.Subscription;
import reactor.core.CoreSubscriber;
import reactor.util.context.Context;

public class ContextLifter<T> implements CoreSubscriber<T> {
    private final CoreSubscriber<T> actualSubscriber;
    private final Context context;

    public ContextLifter(CoreSubscriber<T> actualSubscriber) {
        this.actualSubscriber = actualSubscriber;
        this.context = actualSubscriber.currentContext();
    }

    @Override
    public Context currentContext() {
        return context;
    }

    @Override
    public void onSubscribe(Subscription subscription) {
        actualSubscriber.onSubscribe(subscription);
    }

    @Override
    public void onNext(T t) {
        MdcContextLifter.setContextToMdc(context);
        try {
            actualSubscriber.onNext(t);
        } finally {
            MdcContextLifter.clearMdc();
        }
    }

    @Override
    public void onError(Throwable throwable) {
        MdcContextLifter.setContextToMdc(context);
        try {
            actualSubscriber.onError(throwable);
        } finally {
            MdcContextLifter.clearMdc();
        }
    }

    @Override
    public void onComplete() {
        MdcContextLifter.setContextToMdc(context);
        try {
            actualSubscriber.onComplete();
        } finally {
            MdcContextLifter.clearMdc();
        }
    }
}
