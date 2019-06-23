package net.roughdesign.ajiwa.contexts;


import net.roughdesign.ajiwa.bindings.Binding;
import net.roughdesign.ajiwa.bindings.BindingRegistry;
import net.roughdesign.ajiwa.bindings.Scope;
import net.roughdesign.ajiwa.instanceproviders.InstanceProvider;
import net.roughdesign.ajiwa.instanceproviders.InstanceProviderRegistry;
import net.roughdesign.ajiwa.instanceproviders.StoredValueProvider;

import java.util.ArrayList;


public class ContainerImplementation implements Container {

    private final BindingRegistry _bindingRegistry;
    private final InstanceProviderRegistry _instanceProviderRegistry;
    private final ImplicitBindingsFinder _implicitBindingsFinder = new ImplicitBindingsFinder();
    private final AutoResolver _autoResolver;



    public ContainerImplementation() {

        _bindingRegistry = new BindingRegistry();
        _instanceProviderRegistry = new InstanceProviderRegistry();
        _autoResolver = new AutoResolver(_instanceProviderRegistry);
    }



    @Override
    public <T> void bindInstance(final Class<T> klass, final T instance) {

        final StoredValueProvider<T> storedValueProvider = new StoredValueProvider<>(instance);
        _bindingRegistry.addInstance(klass, instance);
        _instanceProviderRegistry.register(klass, storedValueProvider);
    }



    @Override
    public <TDependency, TImplementation extends TDependency> void bindSingleton(
            final Class<TDependency> dependency, final Class<TImplementation> implementation) {

        final Binding binding = Binding.create(implementation, Scope.Singleton);
        _bindingRegistry.addBinding(dependency, binding);
    }



    @Override
    public <TDependency, TImplementation extends TDependency> void bindTransient(
            final Class<TDependency> dependency, final Class<TImplementation> implementation) {

        final Binding binding = Binding.create(implementation, Scope.Transient);
        _bindingRegistry.addBinding(dependency, binding);
    }



    public void sort() {

        _implicitBindingsFinder.addImplicitBindings(_bindingRegistry);
        final DependencySortProcess dependencySortProcess =
                new DependencySortProcess(_bindingRegistry, _instanceProviderRegistry);
        dependencySortProcess.sort();
    }



    @Override
    public <T> T resolve(final Class<T> klass) {

        final ArrayList<Class<?>> requestChain = new ArrayList<>();
        final InstanceProvider<T> instanceProvider =
                _autoResolver.getOrCreateInstanceProvider(klass, requestChain);
        return instanceProvider.getInstance();
    }
}
