package net.roughdesign.ajiwa.contexts;

import net.roughdesign.ajiwa.bindings.BindingHolder;
import net.roughdesign.ajiwa.instanceproviders.InstanceProvider;
import net.roughdesign.ajiwa.instanceproviders.InstanceProviderRegistry;

import java.util.ArrayList;

public class InjectContext {

    private InstanceProviderRegistry instanceProviderRegistry;



    public InjectContext() {

        instanceProviderRegistry = new InstanceProviderRegistry();

    }


    public <T> BindingHolder<T> bind(Class<T> klass) {

        //ComposingResolveProvider<T> resolveProvider = new ComposingResolveProvider<T>(klass);
        //BindingHolder<T> bindingHolder = new BindingHolder<>(klass, resolveProvider);

        //bindingHolderDictionary.put(klass, bindingHolder);

        return null;
    }


    public <T> T resolve(Class<T> klass) {

        ArrayList<Class<?>> requestChain = new ArrayList<>();
        InstanceProvider<T> instanceProvider =
                instanceProviderRegistry.getOrCreateInstanceProvider(klass, requestChain);
        return instanceProvider.getInstance();
    }

}
