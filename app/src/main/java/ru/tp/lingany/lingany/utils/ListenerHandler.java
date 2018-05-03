package ru.tp.lingany.lingany.utils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.atomic.AtomicReference;

public class ListenerHandler<T> {
    private final T proxy;
    private final AtomicReference<T> listener;

    private ListenerHandler(final AtomicReference<T> reference, final T proxy) {
        this.proxy = proxy;
        this.listener = reference;
    }

    public void unregister() {
        listener.set(null);
    }

    public T asListener() {
        return proxy;
    }

    @SuppressWarnings("unchecked")
    public static <T> ListenerHandler<T> wrap(final Class<T> clazz, final T listener) {
        final AtomicReference<T> reference = new AtomicReference<>(listener);
        T proxy = (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, new InvocationHandler() {
            @Override
            public Object invoke(final Object o, final Method method, final Object[] objects) throws Throwable {
                T listener = reference.get();
                if (listener != null) {
                    method.invoke(listener, objects);
                }
                return null;
            }
        });
        return new ListenerHandler<>(reference, proxy);
    }
}