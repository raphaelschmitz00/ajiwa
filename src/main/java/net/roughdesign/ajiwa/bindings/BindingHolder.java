package net.roughdesign.ajiwa.bindings;

import net.roughdesign.ajiwa.instanceproviders.InstanceProviderRegistry;
import net.roughdesign.ajiwa.instanceproviders.StoredValueProvider;

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


    public <Tinput extends T> void To(Class<Tinput> klass) {
        throw new UnsupportedOperationException();
    }


}
