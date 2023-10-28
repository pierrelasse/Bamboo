package net.bluept.bamboo.service;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ServiceInfo {
    String name();
}
