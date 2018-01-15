package com.example.rxjava.demo;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by IntelliJ IDEA.
 * User: pwadawadagi
 * Date: 11/7/17
 * Time: 3:55 PM
 */
public class Example4
{
    public static void main(String[] args)
    {
        //producer
        Observable<String> observable = Observable.just("how", "to", "do", "in", "java");

        //consumer
        Consumer<? super String> consumer = System.out::println;

        //Attaching producer to consumer
        observable.subscribe(consumer);

    }


}
