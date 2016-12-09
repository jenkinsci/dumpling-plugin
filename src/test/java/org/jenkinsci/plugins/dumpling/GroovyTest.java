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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.isEmptyString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.github.olivergondza.dumpling.factory.ThreadDumpFactory;
import com.github.olivergondza.dumpling.model.ProcessThread;
import com.github.olivergondza.dumpling.model.dump.ThreadDumpThreadSet;
import hudson.model.Node;
import hudson.slaves.DumbSlave;
import hudson.util.RemotingDiagnostics;
import jenkins.model.Jenkins;
import jenkins.model.Jenkins.MasterComputer;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.theories.DataPoint;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;
import org.jvnet.hudson.test.JenkinsRule;

import java.io.IOException;

@RunWith(Theories.class)
public class GroovyTest {

    @ClassRule
    public static JenkinsRule j = new JenkinsRule();

    @DataPoints
    public static Node[] getNodes() throws Exception {
        return new Node[] {
                j.jenkins, j.createOnlineSlave()
        };
    }

    @DataPoints
    public static String[] getAccessors() throws Exception {
        return new String[] {
                "Dumpling", "D"
        };
    }

    @Theory
    public void getCurrentRuntime(Node node, String accessor) throws Exception {
        String out = run(node, "Thread.start('fixture_thread_" + name(node) + "') { println " + accessor + ".runtime.threads }.join()");
        ThreadDumpThreadSet threads = new ThreadDumpFactory().fromString(out).getThreads().where(ProcessThread.nameIs("fixture_thread_" + name(node)));
        assertEquals(out, 1, threads.size());

        assertThat(
                run(node, "print " + accessor + ".runtime.threads.grep().getClass().name"),
                equalTo("com.github.olivergondza.dumpling.model.jvm.JvmThreadSet")
        );
    }

    @Theory
    public void argsAreEmpty(Node node, String accessor) throws Exception {
        assertEquals("0", run(node, "print " + accessor + ".getArgs().size()"));
        assertEquals("0", run(node, "print " + accessor + ".args.size()"));
    }

    @Theory
    public void loadJvm(Node node, String accessor) throws Exception {
        assertThat(
                run(node, "print " + accessor + ".load.jvm.threads.grep().getClass().name"),
                equalTo("com.github.olivergondza.dumpling.model.jvm.JvmThreadSet")
        );
    }

    @Theory
    public void accessQuery(Node node, String accessor) throws Exception {
        assertThat(
                run(node, "print " + accessor + ".query.blockingTree().getClass().name"),
                equalTo("com.github.olivergondza.dumpling.query.BlockingTree")
        );
    }

    private String run(Node node, String script) throws Exception {
        System.out.println(script);
        return RemotingDiagnostics.executeGroovy(script, node.getChannel());
    }

    private String name(Node node) {
        return node instanceof Jenkins
                ? "master"
                : node.getDisplayName()
        ;
    }
}
