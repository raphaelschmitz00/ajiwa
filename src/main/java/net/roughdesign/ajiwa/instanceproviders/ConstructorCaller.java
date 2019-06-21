package net.roughdesign.ajiwa.instanceproviders;

import net.roughdesign.ajiwa.exceptions.ConstructorCallException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class ConstructorCaller<T> implements InstanceProvider<T> {


    private final Constructor<T> _constructor;
    private final ArrayList<InstanceProvider> _parameterProviders;


    public ConstructorCaller(final Constructor<T> constructor,
                             final ArrayList<InstanceProvider> parameterInstanceProviders) {
        _constructor = constructor;
        _parameterProviders = parameterInstanceProviders;
    }


    @Override
    public T getInstance() {

        Object[] objects = getParameterObjects();

        try {
            return _constructor.newInstance(objects);
        } catch (InstantiationException e) {
            throw new ConstructorCallException("InstantiationException for " + _constructor);
        } catch (IllegalAccessException e) {
            throw new ConstructorCallException("IllegalAccessException for " + _constructor);
        } catch (InvocationTargetException e) {
            throw new ConstructorCallException("InvocationTargetException for " + _constructor);
        }
    }


    private Object[] getParameterObjects() {
        ArrayList<Object> objects = new ArrayList<>();
        for (InstanceProvider instanceProvider : _parameterProviders) {
            Object instance = instanceProvider.getInstance();
            objects.add(instance);
        }
        return objects.toArray();
    }
}
