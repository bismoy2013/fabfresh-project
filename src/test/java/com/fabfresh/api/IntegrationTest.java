package com.fabfresh.api;

import com.fabfresh.api.MainApplication;
import com.fabfresh.api.MainConfiguration;
import com.fabfresh.api.core.Saying;
import com.google.common.base.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import io.dropwizard.testing.junit.DropwizardAppRule;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.junit.ClassRule;
import org.junit.Test;


public class IntegrationTest {
	@ClassRule
	public static final DropwizardAppRule<MainConfiguration> RULE =
		new DropwizardAppRule<MainConfiguration>(MainApplication.class, "example.yml");
	
	@Test
	public void testHelloWorld() throws Exception {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target("http://localhost:" + RULE.getLocalPort() + "/hello-world");
		
		final Optional<String> NAME = Optional.fromNullable("Dr. IntegrationTest");
		
		Saying saying = target.queryParam("name", NAME.get()).request().get(Saying.class);
		
		assertThat(saying.getContent()).isEqualTo(RULE.getConfiguration().buildTemplate().render(NAME));
	}
}
