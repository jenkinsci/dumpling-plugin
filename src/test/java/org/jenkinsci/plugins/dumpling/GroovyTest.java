/*
 * The MIT License
 *
 * Copyright (c) 2014 Red Hat, Inc.
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
package org.jenkinsci.plugins.dumpling;

import static org.junit.Assert.assertTrue;
import hudson.slaves.DumbSlave;
import hudson.util.RemotingDiagnostics;
import jenkins.model.Jenkins.MasterComputer;

import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

public class GroovyTest {

    @Rule public JenkinsRule j = new JenkinsRule();

    @Test
    public void master() throws Exception {
        String out = RemotingDiagnostics.executeGroovy("Dumpling.runtime.threads", MasterComputer.localChannel);
        assertTrue(out, out.contains("Executor #0 for master"));

        out = RemotingDiagnostics.executeGroovy("Dumpling.runtime.threads.grep().getClass()", MasterComputer.localChannel);
        assertTrue(out, out.contains("JvmThreadSet"));
    }

    @Test
    public void slave() throws Exception {
        DumbSlave slave = j.createOnlineSlave();
        String out = RemotingDiagnostics.executeGroovy("Dumpling.runtime.threads", slave.getChannel());
        assertTrue(out, out.contains("Pipe writer thread: channel"));

        out = RemotingDiagnostics.executeGroovy("Dumpling.runtime.threads.grep().getClass()", slave.getChannel());
        assertTrue(out, out.contains("JvmThreadSet"));
    }
}
