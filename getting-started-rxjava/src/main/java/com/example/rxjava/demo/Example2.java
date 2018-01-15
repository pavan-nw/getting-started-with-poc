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

                    Client client = ClientBuilder.newClient();
                    WebTarget myResource = client.target("http://freegeoip.net/json/amazon.com");
                    String response = myResource.request(MediaType.TEXT_PLAIN)
                            .get(String.class);
//                    System.out.println(response);
                    Todo todo1 = new TodoBuilder().setId(1)
                            .setTitle(response)
                            .setDescr("")
                            .build();
                    emitter.onNext(todo1);


                    Client sseClient = ClientBuilder.newClient();
                    String endpoint = "http://localhost:4545/alerts";
                    WebTarget target = sseClient.target(endpoint);

                    System.out.println("SSE client timer created");

                    SseEventSource eventSource = SseEventSource.target(target).build();
                    System.out.println("SSE Event source created........");

                    System.out.println("SSE Client triggered in thread "+ Thread.currentThread().getName());


                    try {
                        eventSource.register((sseEvent)
                                -> {

//                            System.out.println("Events received in thread " + Thread.currentThread().getName());
                            String data = sseEvent.readData();
//                            System.out.println("SSE event recieved ----- " + data);

                            Todo todo = new TodoBuilder().setId(1)
                                    .setTitle(data)
                                    .setDescr(data+" desc")
                                    .build();
                            emitter.onNext(todo);

                        },
                                (e) -> e.printStackTrace());

                        eventSource.open();
                        System.out.println("Source open ????? " + eventSource.isOpen());
                    } catch (Exception e) {

                        e.printStackTrace();
                        emitter.onError(e);
                    }

//                    List<Todo> todos = new ArrayList<>();
//                    todos.add(new TodoBuilder().setId(1)
//                            .setTitle("Test1")
//                            .setDescr("Test1 Desr")
//                            .build());
//                    todos.add(new TodoBuilder().setId(2)
//                            .setTitle("Test2")
//                            .setDescr("Test2 Desr")
//                            .build());
//                    for (Todo todo : todos)
//                    {
//                        System.out.println("Emitting from.. " + Thread.currentThread().getName());
//                        emitter.onNext(todo);
//                        Thread.sleep(300);
////                        throw new Exception();
//                    }
//                    emitter.onComplete();
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
                        System.out.println("Error Occured.." + Thread.currentThread().getName());
                        throwable.printStackTrace();
                    }

                    @Override
                    public void onComplete()
                    {
                        System.out.println("Completed");
                    }
                });

        Thread.sleep(100000000);
        System.out.println("End of program");
    }
}
