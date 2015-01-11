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

import hudson.Extension;
import hudson.model.Action;
import hudson.model.RootAction;
import hudson.model.TransientComputerActionFactory;
import hudson.model.Computer;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import javax.annotation.Nonnull;

import org.kohsuke.accmod.Restricted;
import org.kohsuke.accmod.restrictions.DoNotUse;
import org.kohsuke.accmod.restrictions.NoExternalUse;
import org.kohsuke.stapler.Stapler;
import org.kohsuke.stapler.StaplerRequest;

@Extension
@Restricted(NoExternalUse.class)
public class ReferenceLink implements RootAction {

    public String getIconFileName() {
        return !isApplicable() ? null : "help.png";
    }

    public String getDisplayName() {
        return !isApplicable() ? null : "Dumpling reference";
    }

    public String getUrlName() {
        return !isApplicable() ? null : "https://olivergondza.github.io/dumpling/refdoc/";
    }

    private boolean isApplicable() {
        final StaplerRequest req = Stapler.getCurrentRequest();
        if (req == null) return false;

        return req.getRequestURI().endsWith("/script");
    }

    @Extension
    @Restricted(DoNotUse.class)
    public static final class ComputerHelpFactory extends TransientComputerActionFactory {

        private static final @Nonnull Set<ReferenceLink> SINGLETON = Collections.singleton(new ReferenceLink());

        @Override
        public Collection<? extends Action> createFor(Computer target) {
            return SINGLETON;
        }
    }
}
