/*
 * The MIT License
 *
 * Copyright 2012 Sony Mobile Communications AB. All rights reserved.
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

package com.sonyericsson.jenkins.plugins.bfa.db;

import static hudson.Util.fixEmpty;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.sonyericsson.jenkins.plugins.bfa.Messages;
import com.sonyericsson.jenkins.plugins.bfa.PluginImpl;
import com.sonyericsson.jenkins.plugins.bfa.model.FailureCause;
import com.sonyericsson.jenkins.plugins.bfa.statistics.Statistics;
import hudson.Extension;
import hudson.model.Descriptor;
import hudson.model.Run;
import hudson.util.CopyOnWriteList;
import jenkins.model.Jenkins;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 * Handling of the list the traditional way. Local in memory and serialized with the object.
 *
 * @author Robert Sandell &lt;robert.sandell@sonyericsson.com&gt;
 */
public class LocalFileKnowledgeBase extends KnowledgeBase {

    private Map<String, FailureCause> causes;

    /**
     * Standard constructor. Used for legacy conversion.
     *
     * @param legacyCauses the causes.
     */
    public LocalFileKnowledgeBase(CopyOnWriteList<FailureCause> legacyCauses) {
        this(legacyCauses.getView());
    }

    /**
     * Standard constructor. Used for simple testability.
     *
     * @param initialCauses the causes.
     */
    public LocalFileKnowledgeBase(Collection<FailureCause> initialCauses) {
        this.causes = new HashMap<String, FailureCause>();
        for (FailureCause cause : initialCauses) {
            if (fixEmpty(cause.getId()) == null) {
                cause.setId(UUID.randomUUID().toString());
            }
            causes.put(cause.getId(), cause);
        }
    }

    /**
     * Default constructor.
     */
    @DataBoundConstructor
    public LocalFileKnowledgeBase() {
        causes = new HashMap<String, FailureCause>();
    }

    @Override
    public Collection<FailureCause> getCauses() {
        return causes.values();
    }

    @Override
    public Collection<FailureCause> getCauseNames() {
        return getCauses();
    }

    @Override
    public Collection<FailureCause> getShallowCauses() throws Exception {
        return getCauses();
    }

    @Override
    public FailureCause getCause(String id) {
        return causes.get(id);
    }

    @Override
    public FailureCause addCause(FailureCause cause) throws IOException {
        cause.setId(UUID.randomUUID().toString());
        causes.put(cause.getId(), cause);
        PluginImpl.getInstance().save();
        return cause;
    }

    @Override
    public FailureCause removeCause(String id) throws Exception {
        FailureCause remove = causes.remove(id);
        PluginImpl.getInstance().save();
        return remove;
    }

    @Override
    public FailureCause saveCause(FailureCause cause) throws IOException {
        if (fixEmpty(cause.getId()) == null) {
            return addCause(cause);
        } else {
            causes.put(cause.getId(), cause);
            PluginImpl.getInstance().save();
            return cause;
        }
    }

    /**
     * Puts the cause directly into the map. Does not call save.
     * @param cause the cause to put.
     */
    protected void put(FailureCause cause) {
        causes.put(cause.getId(), cause);
    }

    @Override
    public void convertFrom(KnowledgeBase oldKnowledgeBase) throws Exception {
        if (oldKnowledgeBase instanceof LocalFileKnowledgeBase) {
            LocalFileKnowledgeBase lfkb = (LocalFileKnowledgeBase)oldKnowledgeBase;
            causes = lfkb.causes;
        } else {
            convertFromAbstract(oldKnowledgeBase);
        }
    }

    @Override
    public List<String> getCategories() throws Exception {
        if (causes == null) {
            return null;
        }
        List<String> categories = new LinkedList<String>();
        Set myset = new HashSet<String>();
        for (FailureCause cause : causes.values()) {
            List<String> categoriesForCause = cause.getCategories();
            if (categoriesForCause != null) {
                for (String string : categoriesForCause) {
                    if (myset.add(string)) {
                        categories.add(string);
                    }
                }
            }
        }
        return categories;
    }

    @Override
    public boolean equals(KnowledgeBase oldKnowledgeBase) {
        if (getClass().isInstance(oldKnowledgeBase)) {
            return oldKnowledgeBase.getClass().getName().equals(this.getClass().getName());
        } else {
            return false;
        }
    }

    @Override
    public void start() {
        //TODO should something be done here?
    }

    @Override
    public void stop() {
        //TODO should something be done here?
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof KnowledgeBase) {
            return this.equals((KnowledgeBase)other);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        //Making checkstyle happy.
        return getClass().getName().hashCode();
    }

    @Override
    public boolean isEnableStatistics() {
        //Not implemented
        return false;
    }

    @Override
    public boolean isSuccessfulLogging() {
        //not implemented
        return false;
    }

    @Override
    public void saveStatistics(Statistics stat) throws Exception {
        //Not implemented.
    }

    @Override
    public Descriptor<KnowledgeBase> getDescriptor() {
        return Jenkins.getInstance().getDescriptorByType(LocalFileKnowledgeBaseDescriptor.class);
    }

    @Override
    public void removeBuildfailurecause(Run build) throws Exception {
        //Not implemented
    }

    /**
     * Descriptor for {@link LocalFileKnowledgeBase}.
     */
    @Extension
    public static class LocalFileKnowledgeBaseDescriptor extends KnowledgeBaseDescriptor {

        @Override
        public String getDisplayName() {
            return Messages.LocalFileKnowledgeBase_DisplayName();
        }
    }
}
