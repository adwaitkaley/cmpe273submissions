package edu.sjsu.cmpe.cache.client.rendezvoushash;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

import java.nio.charset.Charset;
import java.util.Collection;
import java.util.HashMap;

/**
 * Created by Adwait on 5/5/2015.
 */
public class RendezvousHashImpl<T> {
    private final HashFunction hashFunction;
    private final HashMap<Integer,T> serverList;

    public RendezvousHashImpl(Collection<T> nodes) {

        this.hashFunction = Hashing.md5();
        this.serverList=new HashMap<Integer, T>();
        for (T node : nodes)
        {
            add(node);
        }
    }

    public void add(T node)
    {
        int hash=hashFunction.newHasher()
                .putString(node.toString(), Charset.defaultCharset())
                .hash()
                .asInt();

        serverList.put(hash,node);
    }

    public void remove(T node) {

        int hash=hashFunction.newHasher()
                .putString(node.toString(), Charset.defaultCharset())
                .hash()
                .asInt();

        serverList.remove(hash);
    }

    public Object get(int key) {

        if (serverList.isEmpty())
        {
            return null;
        }

        Integer maxWeight = Integer.MIN_VALUE;

        T maxNode=null;

        for(HashMap.Entry<Integer, T> node : serverList.entrySet())
        {
            int weight=hashFunction.newHasher()
                    .putInt(key)
                    .putString(node.getValue().toString(), Charset.defaultCharset())
                    .hash()
                    .asInt();

          //  System.out.println("Weight from key "+key+" to Node "+node.getValue().toString()+" is : "+weight);

            if(weight>maxWeight)
            {
                maxWeight=weight;
                maxNode=node.getValue();
            }
        }

        //System.out.println("MaxWeight from key "+key+" to Node  is : "+maxNode.toString());
        return maxNode;
    }

}
