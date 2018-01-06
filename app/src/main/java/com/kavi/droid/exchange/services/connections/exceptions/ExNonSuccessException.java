package com.kavi.droid.exchange.services.connections.exceptions;

import com.kavi.droid.exchange.Constants;

/**
 * Created by pl36586 on 1/6/18.
 * @author Kavimal Wijewardana <kavi707@gmail.com>
 */

public class ExNonSuccessException extends Exception{

    public ExNonSuccessException(Throwable throwable) {
        super(Constants.NON_SUCCESS_EXCEPTION_MSG, throwable);
    }
}
