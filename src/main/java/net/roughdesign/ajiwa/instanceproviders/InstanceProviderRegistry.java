package net.roughdesign.ajiwa.instanceproviders;


import net.roughdesign.ajiwa.exceptions.AmbiguousBindingException;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;


public class InstanceProviderRegistry {


    private final Map<Class<?>, InstanceProvider> _instanceProviders;



    public InstanceProviderRegistry() {

        _instanceProviders = new HashMap<>();
    }



    public void register(final Class<?> klass, final InstanceProvider instanceProvider) {

        if (_instanceProviders.containsKey(klass)) throw new AmbiguousBindingException(klass.toString());
        _instanceProviders.put(klass, instanceProvider);
    }



    public boolean contains(final Class<?> klass) {

        return _instanceProviders.containsKey(klass);
    }



    @SuppressWarnings("unchecked")
    public <T> InstanceProvider<T> get(final Class<T> klass) {

        if (!_instanceProviders.containsKey(klass)) throw new NoSuchElementException();
        return _instanceProviders.get(klass);
    }
}
