package edu.sjsu.cmpe.cache.client;

import edu.sjsu.cmpe.cache.client.rendezvoushash.RendezvousHashImpl;

import java.util.ArrayList;

/**
 * Created by Adwait on 5/5/2015.
 */
public class RendezvousClient {

    public static void main(String[] args) throws Exception {

        System.out.println("Starting Cache Rendezvous Client...");

        ArrayList<String> cacheList=new ArrayList<String>();
        cacheList.add("http://localhost:3000");
        cacheList.add("http://localhost:3001");
        cacheList.add("http://localhost:3002");


        RendezvousHashImpl rendezvousHash=new RendezvousHashImpl(cacheList);

        CacheServiceInterface cache=null;

        for(int i=1;i<=10;i++)
        {
            String selectedCache=rendezvousHash.get(i).toString();
            cache=new DistributedCacheService(selectedCache);

            char a= (char) (96+i);

            addElementToCache(i,String.valueOf(a),cache);
        }

        for(int i=1;i<=10;i++)
        {
            String selectedCache=rendezvousHash.get(i).toString();
            cache=new DistributedCacheService(selectedCache);

            getElementFromCache(i,cache);

        }

        System.out.println("Existing Cache Rendezvous Client...");
    }

    public static void addElementToCache(int key,String value,CacheServiceInterface cache)
    {
        cache.put(key, value);
        System.out.println("put( "+key+" => "+value+" )");
    }

    public static void getElementFromCache(int key,CacheServiceInterface cache)
    {
        String value = cache.get(key);
        System.out.println("get( "+key+" ) => " + value+" from "+cache.getCacheServerUrl());
    }
}
