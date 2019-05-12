package in.techturtles.application;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.hateoas.hal.Jackson2HalModule;
import org.springframework.session.hazelcast.HazelcastSessionRepository;
import org.springframework.session.hazelcast.PrincipalNameExtractor;
import org.springframework.session.hazelcast.config.annotation.web.http.EnableHazelcastHttpSession;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

import com.fasterxml.jackson.databind.Module;
import com.hazelcast.config.Config;
import com.hazelcast.config.MapAttributeConfig;
import com.hazelcast.config.MapIndexConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;


@SpringBootApplication
@EnableDiscoveryClient
@EnableCircuitBreaker
@EnableFeignClients
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
@EnableHazelcastHttpSession
public class SpringAppUiApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringAppUiApplication.class, args);
	}

	@Bean
    public Module halModule() {
        return new Jackson2HalModule();
    }

   

    

    @Bean
    public HazelcastInstance hazelcastInstance(
            @Value("${hazelcast.max.no.heartbeat.seconds:60}") String hazelcastHeartbeat) {

        MapAttributeConfig attributeConfig =
                new MapAttributeConfig().setName(HazelcastSessionRepository.PRINCIPAL_NAME_ATTRIBUTE)
                        .setExtractor(PrincipalNameExtractor.class.getName());

        Config config = new Config();
        config.setProperty("hazelcast.max.no.heartbeat.seconds", hazelcastHeartbeat)
                .getMapConfig(HazelcastSessionRepository.DEFAULT_SESSION_MAP_NAME)
                .addMapAttributeConfig(attributeConfig)
                .addMapIndexConfig(new MapIndexConfig(HazelcastSessionRepository.PRINCIPAL_NAME_ATTRIBUTE, false));
        config.getGroupConfig().setName("admin");

        return Hazelcast.newHazelcastInstance(config);
    }

    @Bean
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        serializer.setCookieName("APP-UI-SESSION");
        serializer.setCookiePath("/");
        serializer.setDomainNamePattern("^.+?\\.(\\w+\\.[a-z]+)$");
        return serializer;
    }
}
