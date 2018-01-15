package com.example.rxjava.demo;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.schedulers.Schedulers;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.sse.SseEventSource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by IntelliJ IDEA.
 * User: pwadawadagi
 * Date: 6/12/17
 * Time: 3:29 PM
 */
public class Util
{
    public static Observable getNetworkData()
    {
        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>()
        {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception
            {
                try
                {
                    Client client = ClientBuilder.newClient();
                    WebTarget myResource = client.target("http://freegeoip.net/json/");
                    String response = myResource.request(MediaType.TEXT_PLAIN)
                            .get(String.class);

                    emitter.onNext(response);

                }
                catch (Exception e)
                {
//                    e.printStackTrace();
                    emitter.onError(e);
                }

                emitter.onComplete();
            }
        }).observeOn(Schedulers.newThread());

        return observable;

    }

    public static Observable getNetworkDataStream()
    {
        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>()
        {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception
            {
                try
                {
                    Client sseClient = ClientBuilder.newClient();
                    String endpoint = "http://localhost:4545/alerts";
                    WebTarget target = sseClient.target(endpoint);
                    SseEventSource eventSource = SseEventSource.target(target).build();
//                    System.out.println("SSE Event source created........");
//                    System.out.println("SSE Client triggered in thread "+ Thread.currentThread().getName());
                    try
                    {
                        eventSource.register((sseEvent)
                                -> {
//                                  System.out.println("Events received in thread " + Thread.currentThread().getName());
                                    String data = sseEvent.readData();
//                                  System.out.println("SSE event recieved ----- " + data);
                                        if(data.equalsIgnoreCase("End"))
                                        {
                                            emitter.onComplete();
                                        }
                                        else
                                        {
                                            emitter.onNext(data);
                                        }
                                   },
                                (e) -> {
                                    emitter.onError(e);
//                                    e.printStackTrace();
                                });

                        eventSource.open();
                        System.out.println("Source open ????? " + eventSource.isOpen());
                    }
                    catch (Exception e)
                    {
//                        e.printStackTrace();
                        emitter.onError(e);
                    }
                }
                catch (Exception e)
                {
//                    e.printStackTrace();
                    emitter.onError(e);
                }
            }
        }).observeOn(Schedulers.newThread());
        return observable;
    }
}
