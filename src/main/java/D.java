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

import com.github.olivergondza.dumpling.groovy.Factories;
import com.github.olivergondza.dumpling.groovy.GroovyApiEntryPoint;

import com.github.olivergondza.dumpling.factory.JvmRuntimeFactory;
import com.github.olivergondza.dumpling.groovy.GroovyInterpretterConfig;
import com.github.olivergondza.dumpling.model.jvm.JvmRuntime;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

/**
 * Entry point for D DSL.
 *
 * In default package so it will be available without explicit imports in groovy scripts.
 *
 * @author ogondza
 */
/*package*/ class D {

    static {
        new GroovyInterpretterConfig().setupDecorateMethods();
    }

    protected static final JvmRuntimeFactory factory = new JvmRuntimeFactory();

    private static final GroovyApiEntryPoint d = new GroovyApiEntryPoint(Collections.<String>emptyList(), null, "D");

    public static JvmRuntime getRuntime() {
        return factory.currentRuntime();
    }

    public static @Nonnull GroovyApiEntryPoint.LoadCommand getLoad() {
        return d.getLoad();
    }

    /**
     * @return Class to access static methods on non-instantiable class.
     */
    public static @Nonnull Class<Factories> getQuery() {
        return Factories.class;
    }

    public static @Nonnull List<String> getArgs() {
        return d.getArgs();
    }

    public static String getHelp() {
        return d.toString();
    }
}
