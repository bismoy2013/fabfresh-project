package com.fabfresh.api.resources;

import com.codahale.metrics.annotation.Timed;
import com.fabfresh.api.core.Saying;
import com.fabfresh.api.core.Template;
import com.google.common.base.Optional;

import io.dropwizard.jersey.caching.CacheControl;
import io.dropwizard.jersey.params.DateTimeParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

@Path("/hello-world")
@Produces(MediaType.APPLICATION_JSON)
public class MainResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(MainResource.class);

    private final Template template;
    private final AtomicLong counter;

    public MainResource(Template template) {
        this.template = template;
        this.counter = new AtomicLong();
    }

    @GET
    @Timed(name = "get-requests")
    @CacheControl(maxAge = 1, maxAgeUnit = TimeUnit.DAYS)
    public Saying sayHello(@QueryParam("name") Optional<String> name) {
        return new Saying(counter.incrementAndGet(), template.render(name));
    }

    @POST
    public void receiveHello(@Valid Saying saying) {
        LOGGER.info("Received a saying: {}", saying);
    }
    
    @GET
    @Path("/date")
    public void receiveDate(@QueryParam("date") DateTimeParam dateTimeParam) {
        LOGGER.info("Received a saying: {}", dateTimeParam.get());
    }
}
