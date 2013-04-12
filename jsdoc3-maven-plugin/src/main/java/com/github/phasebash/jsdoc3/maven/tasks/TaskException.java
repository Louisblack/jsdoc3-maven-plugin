package com.github.phasebash.jsdoc3.maven.tasks;

/**
 * An exception thrown when a Task cannot complete.
 */
public final class TaskException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 7554260858782178675L;

	/**
     * Exception with a root exception.
     *
     * @param message A message.
     * @param t A throwable.
     */
    TaskException(String message, Throwable t) {
        super(message, t);
    }

    /**
     * An exception without a root exception.
     *
     * @param message The message.
     */
    TaskException(String message) {
        super(message);
    }
}
