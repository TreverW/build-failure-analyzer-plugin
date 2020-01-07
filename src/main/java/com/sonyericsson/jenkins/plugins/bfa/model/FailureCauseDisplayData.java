package com.sonyericsson.jenkins.plugins.bfa.model;
/*
 * The MIT License
 *
 * Copyright 2013 Sony Mobile Communications AB. All rights reserved.
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

import hudson.model.Run;
import org.kohsuke.stapler.export.ExportedBean;

import java.util.LinkedList;
import java.util.List;

/**
 * A class containing data to be displayed when presenting
 * FailureCausesBuildAction summary.
 *
 * @author Jan-Olof Sivtoft
 */
@ExportedBean
public class FailureCauseDisplayData {

    private List<FoundFailureCause> foundFailureCauses;
    private List<FailureCauseDisplayData> downstreamFailureCauses =
            new LinkedList<FailureCauseDisplayData>();
    private Links links;

    /**
     * Use this constructor when the build is unknown.
     */
    public FailureCauseDisplayData() {
    }

    /**
     * Use this constructor when the build is known.
     *
     * @param build corresponding build.
     *
     */
    @Deprecated
    public FailureCauseDisplayData(final Run build) {
        links = new Links(build.getParent().getUrl(),
                build.getParent().getDisplayName(),
                build.getUrl(),
                build.getDisplayName());
    }

    /**
     * Use this constructor when the build is known.
     *
     * @param parentUrl url of the parent job.
     * @param parentName name of the parent job.
     * @param buildUrl url of the build.
     * @param buildName name of the build.
     *
     */
    public FailureCauseDisplayData(final String parentUrl,
                                   final String parentName,
                                   final String buildUrl,
                                   final String buildName) {
        links = new Links(parentUrl, parentName, buildUrl, buildName);
    }

    /**
     * Set the FoundFailureCauses for corresponding action.
     *
     * @param foundFailureCauses a list of FoundFailureCauses
     */
    public final void setFoundFailureCauses(
            final List<FoundFailureCause> foundFailureCauses) {
        this.foundFailureCauses = foundFailureCauses;
    }

    /**
     * Getter for FoundFailureCauses.
     *
     * @return a list of FoundFailureCauses
     */
    public final List<FoundFailureCause> getFoundFailureCauses() {
        return foundFailureCauses;
    }

    /**
     * Add a downstream builds display data.
     *
     * @param failureCauseDisplayData object containing a downstream
     *                                display data
     */
    public final void addDownstreamFailureCause(
            final FailureCauseDisplayData failureCauseDisplayData) {
        downstreamFailureCauses.add(failureCauseDisplayData);
    }

    /**
     * Getter for getting a list of downstream display data.
     *
     * @return a list of all downstream display data
     */
    public final List<FailureCauseDisplayData> getDownstreamFailureCauses() {
        return downstreamFailureCauses;
    }

    /**
     * Getter for project and build links.
     *
     * @return an object containing link info
     */
    public final Links getLinks() {
        return links;
    }

    /**
     * Indicates whether any failure causes where found (directly or downstream).
     *
     * @return true iff found failures and downstream failures lists are both empty
     */
    public boolean isEmpty() {
        return ((foundFailureCauses == null) || foundFailureCauses.isEmpty())
                && ((downstreamFailureCauses == null) || downstreamFailureCauses.isEmpty());
    }

    /**
     * A class containing links to be displayed for the project and the build.
     */
    public static final class Links {

        private String projectUrl;
        private String buildUrl;
        private String projectDisplayName;
        private String buildDisplayName;

        /**
         * Constructor evaluating links.
         *
         * Url to the project and the build will be displayed in the view.
         *
         * @param parentUrl url of the parent job.
         * @param parentName name of the parent job.
         * @param buildUrl url of the build.
         * @param buildName name of the build.
         */
        private Links(final String parentUrl,
                      final String parentName,
                      final String buildUrl,
                      final String buildName) {
            this.projectUrl = parentUrl;
            this.buildUrl = buildUrl;
            this.projectDisplayName = parentName;
            this.buildDisplayName = buildName;
        }

        /**
         * Getting the project url.
         *
         * @return link to the project.
         */
        public String getProjectUrl() {
            return projectUrl;
        }

        /**
         * Set the project url.
         *
         * @param projectUrl the new url
         */
        public void setProjectUrl(final String projectUrl) {
            this.projectUrl =  projectUrl;
        }

        /**
         * Getting the build url.
         *
         * @return link to build
         */
        public String getBuildUrl() {
            return buildUrl;
        }

        /**
         * Set the build url.
         *
         * @param buildUrl the new url
         */
        public void setBuildUrl(final String buildUrl) {
            this.buildUrl = buildUrl;
        }

        /**
         * Getting the text to be displayed for the project link.
         *
         * @return text to show for the project link
         */
        public String getProjectDisplayName() {
            return projectDisplayName;
        }

        /**
         * Set the text to be displayed for the project link.
         *
         * @param projectDisplayName the text to show
         */
        public void setProjectDisplayName(final String projectDisplayName) {
            this.projectDisplayName = projectDisplayName;
        }

        /**
         * Getting the text to be displayed for the build link.
         *
         * @return text to show for the build link
         */
        public String getBuildDisplayName() {
            return buildDisplayName;
        }

        /**
         * Set the text to be displayed for the build link.
         *
         * @param buildDisplayName the text to show
         */
        public void setBuildDisplayName(final String buildDisplayName) {
            this.buildDisplayName = buildDisplayName;
        }
    }

}
