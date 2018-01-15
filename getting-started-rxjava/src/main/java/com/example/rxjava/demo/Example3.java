package com.example.rxjava.demo;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.sse.SseEventSource;

/**
 * Created by IntelliJ IDEA.
 * User: pwadawadagi
 * Date: 13/7/17
 * Time: 1:57 PM
 */
public class Example3
{
    public static void main(String[] args) throws InterruptedException
    {
        System.out.println("Start of program.." + Thread.currentThread().getName());

        Observer<String> observer = new Observer<String>()
        {
            @Override
            public void onSubscribe(Disposable disposable)
            {
                System.out.println("Subscribing..");
            }

            @Override
            public void onNext(String todo)
            {
                System.out.println("Printing from.. " + Thread.currentThread().getName());
                System.out.println(todo);
            }

            @Override
            public void onError(Throwable throwable)
            {
                System.out.println("Error Occured.." + Thread.currentThread().getName());
                throwable.printStackTrace();
            }

            @Override
            public void onComplete()
            {
                System.out.println("Completed");
            }
        };

//        Util.getNetworkDataStream().subscribeOn(Schedulers.newThread()).
//                subscribe(observer);

        Util.getNetworkData().subscribeOn(Schedulers.newThread()).
                subscribe(observer);

        Thread.sleep(100000000);
        System.out.println("End of program");
    }
}
