package com.example.test;

import com.example.Joker;
import org.junit.Test;

/**
 * Created by Zang on 2016-09-28.
 */

public class JokerTest {
    @Test
    public void test() {
        Joker joker = new Joker();
        assert joker.getJoke().length() != 0;
    }
}
