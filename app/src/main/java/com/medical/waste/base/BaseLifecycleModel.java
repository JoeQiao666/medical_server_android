package com.medical.waste.base;


public class BaseLifecycleModel {
    protected LifecycleProvider provider;
    public BaseLifecycleModel(LifecycleProvider provider) {
        this.provider = provider;
    }
}
