package com.wvkia.tools.kiwi.tools.Test;

import com.google.common.base.Charsets;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

public class TestHashFunction {
    public static void main(String[] args) {
        HashFunction hf = Hashing.md5();
        System.out.println(hf.hashString("1", Charsets.UTF_8).asInt());
        System.out.println(hf.hashString("2", Charsets.UTF_8).asInt());
        System.out.println(hf.hashString("1", Charsets.UTF_8).asInt());



        HashFunction hdf = Hashing.md5();
        System.out.println(hdf.hashString("1", Charsets.UTF_8).asInt());
        System.out.println(hdf.hashString("2", Charsets.UTF_8).asInt());
        System.out.println(hdf.hashString("1", Charsets.UTF_8).asInt());
    }
}
