package com.sample.springbootauth.security.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;


/**
 * The prePostEnabled property enables Spring Security @PreAuthorize and @PostAuthorize annotations
 * The securedEnabled property determines if the @Secured annotation should be enabled
 * The jsr250Enabled property allows us to use the @RoleAllowed annotation
 */
@Configuration
@EnableGlobalMethodSecurity(
  prePostEnabled = true,
  securedEnabled = true,
  jsr250Enabled = true)
public class MethodSecurityConfig
  extends GlobalMethodSecurityConfiguration {
}
