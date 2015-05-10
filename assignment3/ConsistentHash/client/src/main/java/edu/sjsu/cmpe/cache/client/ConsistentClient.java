package edu.sjsu.cmpe.cache.client;

import edu.sjsu.cmpe.cache.client.consistenthash.ConsistentHashImpl;

import java.util.ArrayList;

public class ConsistentClient {

    public static void main(String[] args) throws Exception {

        System.out.println("Starting Cache Consistent Client...");

        ArrayList<String> cacheList=new ArrayList<String>();
        cacheList.add("http://localhost:3000");
        cacheList.add("http://localhost:3001");
        cacheList.add("http://localhost:3002");

        System.out.println("====================================");
        System.out.println("     LIST OF AVAILABLE CACHES       ");
        System.out.println("====================================");
        for(String cache:cacheList)
            System.out.println(cache);

        System.out.println("====================================");


        ConsistentHashImpl consistentHash=new ConsistentHashImpl(cacheList);

        CacheServiceInterface cache=null;

        for(int i=1;i<=10;i++)
        {
            String selectedCache=consistentHash.get(i).toString();
            cache=new DistributedCacheService(selectedCache);

            char a= (char) (96+i);

            addElementToCache(i,String.valueOf(a),cache);
        }
        System.out.println("\n====================================\n");
        for(int i=1;i<=10;i++)
        {
            String selectedCache=consistentHash.get(i).toString();
            cache=new DistributedCacheService(selectedCache);

            getElementFromCache(i,cache);

        }

        System.out.println("\n====================================\n");

        System.out.println("Existing Cache Consistent Client...");
    }

    public static void addElementToCache(int key,String value,CacheServiceInterface cache)
    {
        cache.put(key, value);
        System.out.println("put( "+key+" => "+value+" ) to "+cache.getCacheServerUrl());
    }

    public static void getElementFromCache(int key,CacheServiceInterface cache)
    {
        String value = cache.get(key);
        System.out.println("get( "+key+" ) => " + value+" from "+cache.getCacheServerUrl());
    }
}
