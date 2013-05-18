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

import java.io.IOException;

import hudson.Util;

import org.jvnet.hudson.test.TestPluginManager;

/**
 * Cleanup the temporary directory created by org.jvnet.hudson.test.TestPluginManager.
 * Needed for Jenkins < 1.510
 * 
 * Call TestPluginManagerCleanup.registerCleanup() at least once from anywhere.
 */
public class TestPluginManagerCleanup
{
    private static Thread deleteThread = null;
    
    public static synchronized void registerCleanup()
    {
        if(deleteThread != null)
        {
            return;
        }
        deleteThread = new Thread("HOTFIX: cleanup " + TestPluginManager.INSTANCE.rootDir) {
            @Override public void run() {
                if(TestPluginManager.INSTANCE != null
                        && TestPluginManager.INSTANCE.rootDir != null
                        && TestPluginManager.INSTANCE.rootDir.exists())
                {
                    try {
                        Util.deleteRecursive(TestPluginManager.INSTANCE.rootDir);
                    } catch (IOException x) {
                        x.printStackTrace();
                    }
                }
            }
        };
        Runtime.getRuntime().addShutdownHook(deleteThread);
    }
}