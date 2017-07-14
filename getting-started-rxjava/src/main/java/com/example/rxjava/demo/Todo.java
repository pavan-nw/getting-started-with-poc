package com.example.rxjava.demo;

/**
 * Created by IntelliJ IDEA.
 * User: pwadawadagi
 * Date: 13/7/17
 * Time: 1:58 PM
 */
public class Todo
{
    private String title;
    private long id;
    private String descr;

    public Todo(String title, long id, String descr)
    {
        this.title = title;
        this.id = id;
        this.descr = descr;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public String getDescr()
    {
        return descr;
    }

    public void setDescr(String descr)
    {
        this.descr = descr;
    }

    @Override
    public String toString()
    {
        return "Todo{" +
                "title='" + title + '\'' +
                ", id=" + id +
                ", descr='" + descr + '\'' +
                '}';
    }
}
