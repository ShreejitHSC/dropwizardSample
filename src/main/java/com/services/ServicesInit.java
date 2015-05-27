package com.services;

import com.core.Configuration;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tesco.couchbase.ConcreteCouchbaseResource;
import com.tesco.couchbase.CouchbaseResource;
import com.wordnik.swagger.config.ConfigFactory;
import com.wordnik.swagger.config.ScannerFactory;
import com.wordnik.swagger.config.SwaggerConfig;
import com.wordnik.swagger.jaxrs.config.DefaultJaxrsScanner;
import com.wordnik.swagger.jaxrs.listing.ApiDeclarationProvider;
import com.wordnik.swagger.jaxrs.listing.ApiListingResourceJSON;
import com.wordnik.swagger.jaxrs.listing.ResourceListingProvider;
import com.wordnik.swagger.jaxrs.reader.DefaultJaxrsApiReader;
import com.wordnik.swagger.reader.ClassReaders;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.assets.AssetsBundle;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.picocontainer.DefaultPicoContainer;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.PicoContainer;
import org.picocontainer.injectors.FactoryInjector;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by JC87 on 15/09/2014.
 */

public class ServicesInit extends Service<Configuration> {

    public static void main(String[] args) throws Exception {
        new ServicesInit().run(args);
    }

    @Override
    public void initialize(Bootstrap<Configuration> configurationBootstrap) {
        configurationBootstrap.setName("Services");
        configurationBootstrap.addBundle(new AssetsBundle("/assets/", "/docs", "index.htm"));
    }

    @Override
    public void run(Configuration configuration, Environment environment) throws Exception {
        MutablePicoContainer container = configureDependencies(configuration);
        registerResources(environment, container);
        configureSwagger(environment);
    }

    private void configureSwagger(Environment environment) {
        // Swagger Resource
        environment.addResource(new ApiListingResourceJSON());

        // Swagger providers
        environment.addProvider(new ApiDeclarationProvider());
        environment.addProvider(new ResourceListingProvider());

        // Swagger Scanner, which finds all the resources for @Api Annotations
        ScannerFactory.setScanner(new DefaultJaxrsScanner());

        // Add the reader, which scans the resources and extracts the resource information
        ClassReaders.setReader(new DefaultJaxrsApiReader());

        // Set the swagger config options
        SwaggerConfig config = ConfigFactory.config();
        config.setApiVersion("1.0.1");
        config.setBasePath("../");

        environment.addFilter(CrossOriginFilter.class, "/*");
    }

    private void registerResources(Environment environment, MutablePicoContainer container) {
        HashMap<Class, FactoryInjector> resourceAdaptors = new HashMap<>();
        resourceAdaptors.put(StudentResource.class, studentResourceInjector());

        HashSet<Class> resourceList = new HashSet<>();
        resourceList.add(StudentResource.class);

        for (Class resource : resourceList) {
            container.addComponent(resource);
            environment.addResource(resource);
        }

        environment.addProvider(new PicoProvider(container, resourceList));
    }

    private FactoryInjector<StudentResource> studentResourceInjector() {
        return new FactoryInjector<StudentResource>() {
            @Override
            public StudentResource getComponentInstance(PicoContainer picoContainer, Type type) {
                return new StudentResource(picoContainer.getComponent(StudentRepository.class),picoContainer.getComponent(Configuration.class));
            }
        };
    }

    private MutablePicoContainer configureDependencies(Configuration configuration) {
        MutablePicoContainer container = new DefaultPicoContainer();
        container.addComponent(configuration);
        // CouchBase
        String[] couchbaseNodes = configuration.getCouchbaseNodes();
        String couchbaseBucket = configuration.getCouchbaseBucket();
        String couchbaseUsername = configuration.getCouchbaseUsername();
        String couchbasePassword = configuration.getCouchbasePassword();

        CouchbaseResource couchbaseResource = new ConcreteCouchbaseResource(couchbaseNodes, couchbaseBucket, couchbaseUsername, couchbasePassword);

        container.addComponent(couchbaseResource.getCouchbaseWrapper());
        container.addComponent(couchbaseResource.getAsyncCouchbaseWrapper());

        container.addComponent(StudentRepository.class);
        container.addComponent(ObjectMapper.class);
        return container;
    }
}
