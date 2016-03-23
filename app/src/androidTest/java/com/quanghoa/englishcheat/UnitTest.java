package com.quanghoa.englishcheat;

import org.junit.Test;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
/**
 * Created by voqua on 3/23/2016.
 */
public class UnitTest extends TestCase{
    @Test
    public void testAdd() {
        String str= "Junit is working fine";
        assertEquals("Junit is working fine",str);
    }
}
