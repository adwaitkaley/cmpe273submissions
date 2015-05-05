package edu.sjsu.cmpe.cache.client.consistenthash;

/**
 * Created by Adwait on 5/5/2015.
 */

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

import java.util.ArrayList;
import java.util.Collection;

public class ConsistentHashImpl<T> {

    private final HashFunction hashFunction;
    private final ArrayList<T> serverList;

    public ConsistentHashImpl(Collection<T> nodes) {

        this.hashFunction = Hashing.md5();
        this.serverList=new ArrayList<T>();
        for (T node : nodes)
        {
            add(node);
        }
    }

    public void add(T node)
    {
        serverList.add(node);
    }

    public void remove(T node) {
        serverList.remove(node);
    }

    public Object get(int key) {

        if (serverList.isEmpty())
        {
            return null;
        }

        int hash =Hashing.consistentHash(hashFunction.hashInt(key),serverList.size());


            return serverList.get(hash);

    }

}
