package net.roughdesign.ajiwa.bindings;

import net.roughdesign.ajiwa.instanceproviders.ConstructorCaller;
import net.roughdesign.ajiwa.instanceproviders.InstanceProviderRegistry;
import net.roughdesign.ajiwa.instanceproviders.StoredValueProvider;

import java.util.ArrayList;

public class BindingHolder<T> {

    private final InstanceProviderRegistry instanceProviderRegistry;
    private final Class<T> klass;


    public BindingHolder(final InstanceProviderRegistry instanceProviderRegistry, final Class<T> klass) {
        this.instanceProviderRegistry = instanceProviderRegistry;
        this.klass = klass;
    }


    public void toInstance(T instance) {

        StoredValueProvider<T> storedValueProvider = new StoredValueProvider<>(instance);
        instanceProviderRegistry.register(klass, storedValueProvider);
    }


    public <TInput extends T> void To(Class<TInput> klass) {

        StoredValueProvider<TInput> storedValueProvider =
                instanceProviderRegistry.createStoredValueProvider(klass, new ArrayList<Class<?>>());
        instanceProviderRegistry.register(this.klass, storedValueProvider);
    }


    public <TInput extends T> void toTransient(Class<TInput> klass) {

        ConstructorCaller<TInput> constructorCaller =
                instanceProviderRegistry.createConstructorCaller(klass, new ArrayList<Class<?>>());
        instanceProviderRegistry.register(this.klass, constructorCaller);
    }
}
