package com.example.rxjava.demo;

/**
 * Created by IntelliJ IDEA.
 * User: pwadawadagi
 * Date: 13/7/17
 * Time: 1:59 PM
 */
public class TodoBuilder
{
    private String title;
    private long id;
    private String descr;

    public TodoBuilder()
    {
    }

    public TodoBuilder setTitle(String title)
    {
        this.title = title;
        return this;
    }

    public TodoBuilder setId(long id)
    {
        this.id = id;
        return this;
    }

    public TodoBuilder setDescr(String descr)
    {
        this.descr = descr;
        return this;
    }

    public Todo build()
    {
        Todo myTodo = new Todo(title,id,descr);
        return myTodo;
    }
}
