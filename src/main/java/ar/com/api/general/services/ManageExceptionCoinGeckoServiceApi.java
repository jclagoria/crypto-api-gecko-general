package ar.com.api.general.services;

import ar.com.api.general.exception.ServiceException;

public class ManageExceptionCoinGeckoServiceApi {
    public static void throwServiceException(Throwable throwable) {
        throw new
                ServiceException(
                    throwable.getMessage(),
                    throwable.fillInStackTrace()
        );
    }



}
