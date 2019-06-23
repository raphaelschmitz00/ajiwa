package net.roughdesign.ajiwa.contexts;


import net.roughdesign.ajiwa.bindings.Binding;
import net.roughdesign.ajiwa.bindings.BindingRegistry;
import net.roughdesign.ajiwa.bindings.Scope;

import java.util.Map;


class ImplicitBindingsFinder {



    void addImplicitBindings(final BindingRegistry bindingRegistry) {

        final Map<Class<?>, Binding> bindings = bindingRegistry.getNonInstanceBindings();
        for (final Binding binding : bindings.values()) {
            addImplicitBindings(bindingRegistry, binding);
        }
    }



    private void addImplicitBindings(final BindingRegistry bindingRegistry, final Binding binding) {

        for (final Class<?> parameter : binding.getParameterTypes()) {
            addBindingForParameter(bindingRegistry, parameter);
        }
    }



    private void addBindingForParameter(final BindingRegistry bindingRegistry, final Class<?> parameter) {

        if (bindingRegistry.containsKey(parameter)) return;
        final Binding parameterBinding = Binding.create(parameter, Scope.Singleton);
        bindingRegistry.addBinding(parameter, parameterBinding);
        addImplicitBindings(bindingRegistry, parameterBinding);
    }
}
