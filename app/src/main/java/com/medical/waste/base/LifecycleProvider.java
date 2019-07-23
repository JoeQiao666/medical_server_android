package com.medical.waste.base;

import com.trello.rxlifecycle2.LifecycleTransformer;

public interface LifecycleProvider {
    <E> LifecycleTransformer<E> bindLifecycle();
}
