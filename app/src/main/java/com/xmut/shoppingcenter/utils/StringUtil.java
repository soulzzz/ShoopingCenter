package com.xmut.shoppingcenter.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class StringUtil {
    public static List<String> splitBySymbol(String input,String symbol) {
        StringTokenizer temp = new StringTokenizer(input, symbol);
        List<String> list = new ArrayList<String>();
        while (temp.hasMoreTokens()) {
            list.add(temp.nextToken());
        }
        return list;
    }
    public static String getFirstSplitBySymbol(String input,String symbol) {
        StringTokenizer temp = new StringTokenizer(input, symbol);
        if (temp.hasMoreTokens()) {
            return temp.nextToken();
        }
        return null;
    }
}
