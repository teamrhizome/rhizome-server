package org.rhizome.server.common.utils;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

@Component
public class SystemLocalDateTimeHolder implements LocalDateTimeHolder {
    @Override
    public LocalDateTime now() {
        return LocalDateTime.now();
    }
}
