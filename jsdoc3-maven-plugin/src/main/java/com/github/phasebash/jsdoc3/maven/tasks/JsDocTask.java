package com.github.phasebash.jsdoc3.maven.tasks;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * A Task which runs the jsdoc3 executable via Rhino.
 */
final class JsDocTask implements Task {

    /** the commonjs module directories to include */
    private static final List<String> MODULES = Collections.unmodifiableList(Arrays.asList(
            "node_modules", "rhino", "lib", null
    ));

    /**
     * Execute the jsdoc3 runner.
     *
     * @param context The context.
     * @throws TaskException If we're unable to run the task.
     */
    @Override
    public void execute(TaskContext context) throws TaskException {
        final List<String> arguments = new LinkedList<String>();

        final String basePath = context.getJsDocDir().getAbsolutePath();
        String basePathForwardSlash = context.getJsDocDir().toURI().normalize().getPath();
        basePathForwardSlash = basePathForwardSlash.substring(0, basePathForwardSlash.length() - 1);
        if (basePathForwardSlash.charAt(2) == ':') {
        	basePathForwardSlash = basePathForwardSlash.substring(1);
        }
        final String javaHome = System.getProperty("java.home");
        final File java = new File(javaHome, "bin" + File.separator + "java");

        arguments.add(java.getAbsolutePath());
        arguments.add("-classpath");
        arguments.add(new File(basePath, "rhino" + File.separator + "js.jar").getAbsolutePath());
        arguments.add("org.mozilla.javascript.tools.shell.Main");

        for (final String module : MODULES) {
            arguments.add("-modules");

            File file = new File(basePath + (module != null ? "/" + module : ""));
            final URI uri = file.toURI().normalize(); 
            String path = uri.getPath();
            if (path.charAt(2) == ':') {
            	path = path.substring(3, path.length() - 1);
            }
            arguments.add(path);
        }

        if (basePathForwardSlash.charAt(1) == ':') {
        	arguments.add(basePathForwardSlash.substring(2) + "/jsdoc.js");        	
        } 
        else {
        	arguments.add(basePathForwardSlash + "/jsdoc.js");
        }
        arguments.add("--dirname=" + basePathForwardSlash);

        if (context.isRecursive()) {
            arguments.add("-r");
        }

        arguments.add("-d");       
        arguments.add(context.getOutputDir().getAbsolutePath());
        
        arguments.add("-t");
        arguments.add(context.getTemplate().getAbsolutePath());

        for (final File sourceFile : context.getSourceDir()) {
            arguments.add(sourceFile.getAbsolutePath());
        }
        
        arguments.add(context.getSourceDirectory().getAbsolutePath());

        Process process;

        System.err.println(arguments);

        if (context.isDebug()) {
            throw new UnsupportedOperationException("Debug mode not currently supported.");
        } else {
            final ProcessBuilder processBuilder = new ProcessBuilder(arguments);

            try {
                process = processBuilder.start();
            } catch (IOException e) {
                throw new TaskException("Unable to execute jsdoc tasks in new JVM.", e);
            }
        }

        try {
            final int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new TaskException("Process died with exit code " + exitCode);
            }
        } catch (InterruptedException e) {
            throw new TaskException("Interrupt while waiting for jsdoc task to complete.", e);
        }
    }
}
