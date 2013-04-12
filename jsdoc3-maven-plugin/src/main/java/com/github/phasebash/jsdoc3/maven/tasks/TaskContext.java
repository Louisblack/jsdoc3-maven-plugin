package com.github.phasebash.jsdoc3.maven.tasks;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * A context object representing the necessary values required to run jsdoc3.
 */
public final class TaskContext {

    /** the source directory where sources can be found */
    private final List<File> sourceDir;

    /** where jsdoc should be written */
    private final File outputDir;

    /** the directory where jsdoc source files can be found */
    private final File jsDocDir;

    /** a temp dir for scratch files */
    private final File tempDir;

    /** if jsdoc should run in debug mode */
    private final boolean debug;

    /** if we shall pass the -r flag to jsdoc. */
    private final boolean recursive;

	private File sourceDirectory;

	private File template;

    /**
     * Private constructor.
     *
     * @param sourceDir the source dir.
     * @param outputDir the output dir.
     * @param jsDocDir  the jsdoc dir.
     * @param tempDir   the temp dir.
     * @param debug     if debug mode should be used.
     * @param recursive if the jsdoc task should recursively look for jsfiles.
     */
    TaskContext(List<File> sourceDir, File sourceDirectory, File outputDir, File jsDocDir,
                File tempDir, File template, boolean debug, boolean recursive) {
        this.sourceDir = sourceDir;
        this.sourceDirectory = sourceDirectory;
        this.jsDocDir  = jsDocDir;
        this.outputDir = outputDir;
        this.tempDir   = tempDir;
        this.template = template;
        this.debug     = debug;
        this.recursive = recursive;
    }

    /**
     * Get the JSDoc source directory.  It may not exist.
     *
     * @return The directory.
     */
    public File getJsDocDir() {
        return jsDocDir;
    }

    /**
     * Get the output directory.  It may not exist at the time of this call.
     *
     * @return The directory.
     */
    public File getOutputDir() {
        return outputDir;
    }

    /**
     * Get the temp directory.  It will exist at the time of this call.
     *
     * @return The temp directory.
     */
    public File getTempDir() {
        return tempDir;
    }

    /**
     * Get the source directory.  If will exist at the time of this call.
     *
     * @return The source directory.
     */
    public List<File> getSourceDir() {
        return sourceDir;
    }

    
    
    public File getSourceDirectory() {
		return sourceDirectory;
	}
    
    

	public File getTemplate() {
		return template;
	}

	/**
     * Debug mode?
     *
     * @return true for yes, false for no.
     */
    public boolean isDebug() {
        return debug;
    }

    /**
     * Recursive mode?
     *
     * @return true for yes, false for no.
     */
    public boolean isRecursive() {
        return recursive;
    }

    /**
     * The way in which a TaskContext should be built.
     */
    public static class Builder {

        private Set<File> sourceFiles = new LinkedHashSet<File>();

        private Set<File> directoryRoots = new LinkedHashSet<File>();

        private File outputDirectory;

        private File jsDocDirectory;

        private File tempDirectory;

        private boolean debug = false;

        private boolean recursive = false;

		private File sourceDirectory;

		private File template;

        public Builder withSourceFiles(final Collection<File> sourceFiles) {
            if (sourceFiles != null) {
                this.sourceFiles.addAll(sourceFiles);
            }
            return this;
        }

        public Builder withOutputDirectory(final File outputDirectory) {
            this.outputDirectory = outputDirectory;
            return this;
        }

        public Builder withJsDocDirectory(final File jsDocDirectory) {
            this.jsDocDirectory = jsDocDirectory;
            return this;
        }

        public Builder withTempDirectory(final File tempDirectory) {
            this.tempDirectory = tempDirectory;
            return this;
        }

        public Builder withRecursive(final boolean recursive) {
            this.recursive = recursive;
            return this;
        }
        
        public Builder withTemplate(final File template) {
        	if (template != null) {
        		this.template = template;
        	}
        	return this;
        }

        /**
         * An optional attribute, if jsdoc should run in debug mode.
         *
         * @param debug the value.
         * @return The builder.
         */
        public Builder withDebug(final boolean debug) {
            this.debug = debug;
            return this;
        }

        public void withDirectoryRoots(final Set<File> directoryRoots) {
            if (directoryRoots != null) {
                this.directoryRoots.addAll(directoryRoots);
            }
        }

        public void withSourceDirectory(File sourceDirectory) {
        	if (sourceDirectory != null) {
        		this.sourceDirectory = sourceDirectory;
        	}
        	
    	
        }
        
        public TaskContext build() {
            if (sourceDirectory == null && sourceFiles.size() == 0 && directoryRoots.size() == 0) {
                throw new IllegalArgumentException("sourceDirectory and/or sourceFiles and/or directoryRoots are required.");
            }

            final List<File> sourceRoots = new ArrayList<File>();

            if (sourceFiles != null) {
            	sourceRoots.addAll(sourceFiles);
            }
            sourceRoots.addAll(directoryRoots);

            if (!outputDirectory.exists()) {
                throw new IllegalStateException("Output directory must exist.");
            }

            if (jsDocDirectory == null) {
                throw new IllegalStateException("jsdoc directory must not be null.");
            }

            if (tempDirectory == null) {
                throw new IllegalStateException("Temp directory must not be null.");
            }

            return new TaskContext(sourceRoots, sourceDirectory, outputDirectory, jsDocDirectory, tempDirectory, template, debug, recursive);
        }


    }
}
