/*
 * The MIT License
 * 
 * Copyright (c) 2013 IKEDA Yasuyuki
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jenkinsci.plugins.reproducejenkins4409;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.jvnet.hudson.test.Bug;
import org.jvnet.hudson.test.JenkinsRule;

/**
 * Test doing nothing
 * 
 * Any test causes JENKINS-4409 in Windows.
 * 
 * In Jenkins < 1.482, a temporary directory hudsonXXXXXXXtest is leaved.
 * This can be avoided only by upgrading target Jenkins to 1.482 or later.
 * 
 */
@Bug(4409)
@RunWith(Parameterized.class)
public class JENKINS4409JenkinsRuleTest
{
    // To measure the time to run tests.
    private final static int REPEAT = 30;
    
    static {
        TestPluginManagerCleanup.registerCleanup();
    }
    
    @Rule
    public JenkinsRule j = new JenkinsRule(){
        protected void before() throws Throwable {
            // uncommenting this causes JENKINS-4409
            setPluginManager(null);
            super.before();
        }
    };
    
    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        List<Object[]> lst = new ArrayList<Object[]>(REPEAT);
        for(int i = 1; i <= REPEAT; ++i)
        {
            lst.add(new Object[]{ i });
        }
        return lst;
    }
    
    private int repeat = 0;
    
    public JENKINS4409JenkinsRuleTest(int repeat)
    {
        this.repeat = repeat;
    }
    
    @Test
    public void testSomething()
    {
        System.out.println(String.format("= REPEAT %d/%d", repeat, REPEAT));
        System.out.println("Do nothing!");
    }
}
