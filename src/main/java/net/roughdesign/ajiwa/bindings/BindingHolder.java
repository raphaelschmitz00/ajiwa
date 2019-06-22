package net.roughdesign.ajiwa.bindings;

import net.roughdesign.ajiwa.instanceproviders.InstanceProvider;

import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class BindingHolder<T> {

    private final Class<T> _klass;

     private final InstanceProvider<T> _instanceProvider;


    public BindingHolder(final Class<T> klass,                final InstanceProvider<T> instanceProvider) {
        _klass = klass;
        _instanceProvider = instanceProvider;
    }


    public void FromInstance(T instance) {
        throw  new UnsupportedOperationException();
    }


    public void ToSelf() {
        To(_klass);
    }


    public <Tinput extends T> void To(Class<Tinput> klass) {
        throw  new UnsupportedOperationException();
    }


    private Constructor<?> SearchParameterlessConstructor(Constructor<?>[] constructors) {
        for (Constructor<?> constructor : constructors) {
            if (constructor.getParameterCount() == 0) return constructor;
        }
        return null;
    }


    public T resolveInstance() {
        return _instanceProvider.getInstance();
    }


    @SuppressWarnings("unchecked")
    public Class<T> getGenericTypeArgument() {
        ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
        Type[] actualTypeArguments = genericSuperclass.getActualTypeArguments();
        return (Class<T>) actualTypeArguments[0];
    }



    public InstanceProvider<T> getInstanceProvider(){
        return _instanceProvider;
    }
}
