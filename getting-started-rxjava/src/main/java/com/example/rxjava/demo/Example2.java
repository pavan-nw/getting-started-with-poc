package com.example.rxjava.demo;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: pwadawadagi
 * Date: 13/7/17
 * Time: 1:57 PM
 */
public class Example2
{
    public static void main(String[] args) throws InterruptedException
    {

        System.out.println("Start of program.." + Thread.currentThread().getName());
        Observable<Todo> todoObservable = Observable.create(new ObservableOnSubscribe<Todo>()
        {
            @Override
            public void subscribe(ObservableEmitter<Todo> emitter) throws Exception
            {
                try
                {
                    List<Todo> todos = new ArrayList<>();
                    todos.add(new TodoBuilder().setId(1)
                            .setTitle("Test1")
                            .setDescr("Test1 Desr")
                            .build());
                    todos.add(new TodoBuilder().setId(2)
                            .setTitle("Test2")
                            .setDescr("Test2 Desr")
                            .build());
                    for (Todo todo : todos)
                    {
                        System.out.println("Emitting from.. " + Thread.currentThread().getName());
                        emitter.onNext(todo);
                        Thread.sleep(300);
//                        throw new Exception();
                    }
                    emitter.onComplete();
                }
                catch (Exception e)
                {
                    emitter.onError(e);
                }
            }
        }).observeOn(Schedulers.newThread());


        todoObservable.subscribeOn(Schedulers.newThread()).
                subscribe(new Observer<Todo>()
                {
                    @Override
                    public void onSubscribe(Disposable disposable)
                    {
                        System.out.println("Subscribing..");
                    }

                    @Override
                    public void onNext(Todo todo)
                    {
                        System.out.println("Printing from.. " + Thread.currentThread().getName());
                        System.out.println(todo);
                    }

                    @Override
                    public void onError(Throwable throwable)
                    {
                        System.out.println("Error Occured.."+ Thread.currentThread().getName());
                        throwable.printStackTrace();
                    }

                    @Override
                    public void onComplete()
                    {
                        System.out.println("Completed");
                    }
                });

        Thread.sleep(10000);
        System.out.println("End of program");
    }
}
