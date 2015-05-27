package com.services;

import com.sun.jersey.core.spi.component.ioc.IoCInstantiatedComponentProvider;
import org.picocontainer.PicoContainer;

/**
 * Created by JC87 on 17/09/2014.
 */
public class PicoComponentProvider implements IoCInstantiatedComponentProvider {
    private final Class<?> resourceClass;
    private final PicoContainer container;

    public PicoComponentProvider(Class<?> resourceClass, PicoContainer container) {
        this.resourceClass = resourceClass;
        this.container = container;
    }

    public Object getInstance() {
        Object component = container.getComponent(resourceClass);
        if(component == null) {
            throw new RuntimeException("Failed to get instance of class " + resourceClass.getName() + " from dependency injector");
        }
        return component;
    }

    public Object getInjectableInstance(Object o) {
        return o;
    }
}
