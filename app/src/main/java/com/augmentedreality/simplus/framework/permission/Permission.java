package com.augmentedreality.simplus.framework.permission;

public class Permission {
    private String name;
    private boolean granted;

    public Permission(String name, boolean granted) {
        this.name = name;
        this.granted = granted;
    }

    public String getName() {
        return name;
    }

    public boolean isGranted() {
        return granted;
    }
}
