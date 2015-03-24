package com.fabfresh.api;

import com.fabfresh.api.auth.MainAuthenticator;
import com.fabfresh.api.cli.RenderCommand;
import com.fabfresh.api.core.Person;
import com.fabfresh.api.core.Template;
import com.fabfresh.api.core.User;
import com.fabfresh.api.db.PersonDAO;
import com.fabfresh.api.filter.DateRequiredFeature;
import com.fabfresh.api.health.TemplateHealthCheck;
import com.fabfresh.api.resources.FilteredResource;
import com.fabfresh.api.resources.MainResource;
import com.fabfresh.api.resources.PeopleResource;
import com.fabfresh.api.resources.PersonResource;
import com.fabfresh.api.resources.ProtectedResource;
import com.fabfresh.api.resources.ViewResource;


import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.auth.AuthFactory;
import io.dropwizard.auth.basic.BasicAuthFactory;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;

import java.util.Map;

public class MainApplication extends Application<MainConfiguration> {
    public static void main(String[] args) throws Exception {
        new MainApplication().run(args);
    }

    private final HibernateBundle<MainConfiguration> hibernateBundle =
            new HibernateBundle<MainConfiguration>(Person.class) {
                @Override
                public DataSourceFactory getDataSourceFactory(MainConfiguration configuration) {
                    return configuration.getDataSourceFactory();
                }
            };

    @Override
    public String getName() {
        return "Sample String";
    }

    @Override
    public void initialize(Bootstrap<MainConfiguration> bootstrap) {
        // Enable variable substitution with environment variables
        bootstrap.setConfigurationSourceProvider(
                new SubstitutingSourceProvider(
                        bootstrap.getConfigurationSourceProvider(),
                        new EnvironmentVariableSubstitutor(false)
                )
        );

        bootstrap.addCommand(new RenderCommand());
        bootstrap.addBundle(new AssetsBundle());
        bootstrap.addBundle(new MigrationsBundle<MainConfiguration>() {
            @Override
            public DataSourceFactory getDataSourceFactory(MainConfiguration configuration) {
                return configuration.getDataSourceFactory();
            }
        });
        bootstrap.addBundle(hibernateBundle);
        bootstrap.addBundle(new ViewBundle<MainConfiguration>() {
            @Override
            public Map<String, Map<String, String>> getViewConfiguration(MainConfiguration configuration) {
                return configuration.getViewRendererConfiguration();
            }
        });
    }

    @Override
    public void run(MainConfiguration configuration, Environment environment) {
        final PersonDAO dao = new PersonDAO(hibernateBundle.getSessionFactory());
        final Template template = configuration.buildTemplate();

        environment.healthChecks().register("template", new TemplateHealthCheck(template));
        environment.jersey().register(DateRequiredFeature.class);

        environment.jersey().register(AuthFactory.binder(new BasicAuthFactory<>(new MainAuthenticator(),
                                                                 "SUPER SECRET STUFF",
                                                                 User.class)));
        environment.jersey().register(new MainResource(template));
        environment.jersey().register(new ViewResource());
        environment.jersey().register(new ProtectedResource());
        environment.jersey().register(new PeopleResource(dao));
        environment.jersey().register(new PersonResource(dao));
        environment.jersey().register(new FilteredResource());
    }
}
