package com.rightpair.security;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockAppUserSecurityContextFactory.class)
public @interface WithMockAppUser {
    String username() default "test@test.com";
    String password() default "test1234";
    String nickname() default "테스트";

    String role() default "ASSOCIATE_USER";
}
