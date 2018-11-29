package com.example.android.filmfun4me.utils;

import io.reactivex.disposables.Disposable;

/**
 * Created by gobov on 12/28/2017.
 */

public class RxUtils {

    public static void unsubscribe(Disposable disposable){
        if (disposable != null && !disposable.isDisposed()){
            disposable.dispose();
        }
    }
}
