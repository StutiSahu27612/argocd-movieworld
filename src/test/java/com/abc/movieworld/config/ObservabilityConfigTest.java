package com.abc.movieworld.config;

import io.micrometer.observation.ObservationRegistry;
import io.micrometer.observation.aop.ObservedAspect;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.semconv.resource.attributes.ResourceAttributes;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ObservabilityConfigTest {

    @Nested
    @SpringBootTest
    @ActiveProfiles("test")
    @TestPropertySource(properties = {"otel.sdk.disabled=false"})
    class MainConfigTest {
        @Autowired
        private ObservabilityConfig observabilityConfig;

        @MockBean
        private ObservationRegistry observationRegistry;

        @Test
        void testObservedAspectCreation() {
            ObservedAspect aspect = observabilityConfig.observedAspect(observationRegistry);
            assertNotNull(aspect, "ObservedAspect should not be null");
        }

        @Test
        void testOtelResourceCreation() {
            Resource resource = observabilityConfig.otelResource();
            assertNotNull(resource, "Resource should not be null");
            
            // Verify that the resource has the expected attributes
            assertTrue(resource.getAttributes().asMap().containsKey(ResourceAttributes.SERVICE_NAME),
                    "Resource should contain SERVICE_NAME attribute");
            assertTrue(resource.getAttributes().asMap().containsKey(ResourceAttributes.SERVICE_VERSION),
                    "Resource should contain SERVICE_VERSION attribute");
        }
    }
    
    @Nested
    class FallbackConfigTest {
        @Test
        void testFallbackConfig() {
            // Create an instance of FallbackConfig and call init() to test the method
            ObservabilityConfig.FallbackConfig fallbackConfig = new ObservabilityConfig.FallbackConfig();
            fallbackConfig.init();
            // No assertion needed as we're just testing that the method executes without errors
            // This test is primarily for code coverage
        }
    }
}
