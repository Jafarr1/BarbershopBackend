package org.example.barbershopbackend.model;

public enum ServiceType {
    HAARKLIP("25 min"),
    HAAR_OG_SKAEG("25–45 min"),
    SKAEG("15 min");

    private final String displayDuration;

    ServiceType(String displayDuration) {
        this.displayDuration = displayDuration;
    }

    public String getDisplayDuration() {
        return displayDuration;
    }
}


