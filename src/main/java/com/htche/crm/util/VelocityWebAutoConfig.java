package com.htche.crm.util;

import org.apache.velocity.tools.config.*;
import org.springframework.boot.autoconfigure.velocity.VelocityProperties;
import org.springframework.context.annotation.Bean;
import org.apache.velocity.context.Context;
import org.apache.velocity.tools.Scope;
import org.apache.velocity.tools.ToolboxFactory;
import org.apache.velocity.tools.view.ViewToolContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.view.velocity.VelocityLayoutView;
import org.springframework.web.servlet.view.velocity.VelocityLayoutViewResolver;
import org.springframework.web.servlet.view.velocity.VelocityViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Set;

@Configuration
public class VelocityWebAutoConfig {

    @Bean
    @Order(PriorityOrdered.HIGHEST_PRECEDENCE)
    @ConditionalOnMissingBean(name = {"velocityViewResolver"})
    @ConditionalOnProperty(name = {"spring.velocity.enabled"}, matchIfMissing = true)
    public VelocityViewResolver velocityViewResolver(VelocityProperties properties) {
        EmbeddedToolSupportedVelocityLayoutViewResolver resolver = new EmbeddedToolSupportedVelocityLayoutViewResolver();
//        String layoutUrl = properties.getProperties().getOrDefault("layout-url", "layout/default.vm");
        resolver.setLayoutUrl("layout/default.vm");
        properties.applyToViewResolver(resolver);
        return resolver;
    }

    public static class EmbeddedToolSupportedVelocityLayoutViewResolver extends VelocityLayoutViewResolver {
        @Override
        protected Class<?> requiredViewClass() {
            return EmbeddedToolSupportedVelocityLayoutView.class;
        }
    }

    public static class EmbeddedToolSupportedVelocityLayoutView extends VelocityLayoutView {

        @Override
        protected Context createVelocityContext(Map<String, Object> model, HttpServletRequest request,
                                                HttpServletResponse response) throws Exception {
            ViewToolContext context = new ViewToolContext(getVelocityEngine(),
                    request, response, getServletContext());

            ToolboxFactory toolFac = new ToolboxFactory();
            toolFac.configure(ConfigurationUtils.find(this.getToolboxConfigLocation()));
            //
            for (String scope : Scope.values()) {
                context.addToolbox(toolFac.createToolbox(scope));
            }

            if (model != null) {
                for (Map.Entry<String, Object> entry : (Set<Map.Entry<String, Object>>) model
                        .entrySet()) {
                    context.put(entry.getKey(), entry.getValue());
                }
            }
            return context;
        }
    }
}