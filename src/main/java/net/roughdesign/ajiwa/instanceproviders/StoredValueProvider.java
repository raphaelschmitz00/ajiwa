package net.roughdesign.ajiwa.instanceproviders;

public class StoredValueProvider<T> implements InstanceProvider<T> {
    private T _instance;


    public StoredValueProvider(final T instance) {
        _instance = instance;
    }


    @Override
    public T getInstance() {
        return _instance;
    }
}
