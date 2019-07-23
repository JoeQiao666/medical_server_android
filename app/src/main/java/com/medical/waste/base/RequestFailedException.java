package com.medical.waste.base;

import com.medical.waste.bean.Result;

class RequestFailedException extends Exception {
    Result result;
    RequestFailedException(Result tResult) {
        this.result = tResult;
    }
}
