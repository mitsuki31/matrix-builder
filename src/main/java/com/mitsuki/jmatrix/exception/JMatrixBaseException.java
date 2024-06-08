// :: -------------------------- :: //
/* --    JMatrixBaseException    -- */
// :: -------------------------- :: //

/*
 * Copyright (c) 2023-2024 Ryuu Mitsuki
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mitsuki.jmatrix.exception;

import com.mitsuki.jmatrix.Matrix;
import com.mitsuki.jmatrix.core.MatrixUtils;
import com.mitsuki.jmatrix.enums.JMErrorCode;

import java.lang.RuntimeException;

/**
 * Base exception class for all <b>JMatrix</b> exceptions. This exception and its subclasses are unchecked exceptions,
 * which means they can be thrown during runtime.
 *
 * <p><b>Type:</b> Unchecked exception</p>
 *
 * @since   1.0.0b.1
 * @version 1.3, 06 June 2024
 * @author  <a href="https://github.com/mitsuki31" target="_blank">
 *          Ryuu Mitsuki</a>
 * @license <a href="https://www.apache.org/licenses/LICENSE-2.0" target="_blank">
 *          Apache License 2.0</a>
 */
public class JMatrixBaseException extends RuntimeException {

    /**
     * Stores the serial version number of this class for deserialization to
     * verify that the sender and receiver of a serialized object have loaded classes
     * for that object that are compatible with respect to serialization.
     *
     * @serial
     * @see    java.io.Serializable
     */
    private static final long serialVersionUID = 8_294_400_000L;

    /**
     * A string represents the detail message of this exception.
     *
     * @serial
     * @see    #getMessage()
     */
    private String message = null;

    /**
     * Stores the corresponding {@link JMErrorCode} for later retrieval
     * by {@link #getErrorCode()}.
     *
     * @serial
     * @see    #getErrorCode()
     */
    private JMErrorCode errcode = null;

    /**
     * Stores the string representation of the causing exception.
     *
     * @see   Throwable#toString()
     */
    private String strCause = null;

    /**
     * Stores a string represents the detail message of this exception.
     *
     * @see   #getMessage()
     */
    private String message = null;

    /**
     * Indicates whether this exception is caused by another exception.
     */
    private boolean isCausedException = false;

    /**
     * Constructs a new {@code JMatrixBaseException} with no detail message.
     *
     * @since 1.0.0b.1
     */
    public JMatrixBaseException() {
        super();
    }

    /**
     * Constructs a new {@code JMatrixBaseException} with the specified detail message.
     *
     * @param s  The detail message and will be saved for later retrieval
     *           by the {@link #getMessage()} method.
     *
     * @since    1.0.0b.1
     */
    public JMatrixBaseException(String s) {
        super(s);
        this.message = s;
        this.errcode = JMErrorCode.UNKERR;  // Set to 'Unknown error'
    }

    /**
     * Constructs a new {@code JMatrixBaseException} with the specified errno
     * and the detail message.
     *
     * @param errno  The error number.
     * @param s      The detail message and will be saved for later retrieval
     *               by the {@link #getMessage()} method.
     *
     * @since  1.5.0
     * @see    #getMessage()
     * @see    #getErrorCode()
     */
    public JMatrixBaseException(int errno, String s) {
        super(s);
        this.message = s;
        this.errcode = JMErrorCode.valueOf(errno);
    }

    /**
     * Constructs a new {@code JMatrixBaseException} with the specified cause
     * and a detail message of the cause.
     *
     * @param cause  the cause of this exception.
     *
     * @since        1.0.0b.1
     */
    public JMatrixBaseException(Throwable cause) {
        super(cause);
        this.message = (cause == null) ? null : cause.toString();

        // Get the error code if the `cause` is an instance of this class
        if (cause instanceof JMatrixBaseException) {
            this.errcode = ((JMatrixBaseException) cause).errcode;
        }
    }
        
    public JMatrixBaseException(String s, Throwable cause) {
        super(s, cause);
        this.message = s;

        // Get the error code if the `cause` is an instance of this class
        if (cause instanceof JMatrixBaseException) {
            this.errcode = ((JMatrixBaseException) cause).errcode;
        }
    }

    protected JMatrixBaseException(String message, Throwable cause,
                                   boolean enableSuppression,
                                   boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.message = message;
    }

    /**
     * Prints the stack trace of this exception to the standard error stream.
     *
     * @since 1.0.0b.1
     */
    @Override
    public void printStackTrace() {
        String spc = "        ";
        String arrow = ">>>>>>>>>>>>>";

        if (this.isCausedException && this.stackTraceElements.length > 2) {
            System.err.printf("/!\\ EXCEPTION%s%s%s",
                    System.lineSeparator(),
                    arrow,
                    System.lineSeparator());
            System.err.println(this.toString());

            for (int i = this.stackTraceElements.length - 2; i != this.stackTraceElements.length; i++) {
                System.err.printf("%sat \"%s.%s\" -> \"%s\": line %d" + System.lineSeparator(),
                    spc,
                    this.stackTraceElements[i].getClassName(),
                    this.stackTraceElements[i].getMethodName(),
                    this.stackTraceElements[i].getFileName(),
                    this.stackTraceElements[i].getLineNumber());
            }
        } else {
            System.err.printf("/!\\ EXCEPTION%s%s%s",
                    System.lineSeparator(),
                    arrow,
                    System.lineSeparator());
            System.err.println(this.toString());

            for (StackTraceElement ste : this.stackTraceElements) {
                System.err.printf("%sat \"%s.%s\" -> \"%s\": line %d" + System.lineSeparator(),
                    spc,
                    ste.getClassName(),
                    ste.getMethodName(),
                    ste.getFileName(),
                    ste.getLineNumber());
            }
        }

        if (this.isCausedException) {
            System.err.printf("%s/!\\ CAUSED BY%s%s%s",
                    System.lineSeparator(),
                    System.lineSeparator(),
                    arrow,
                    System.lineSeparator());
            System.err.println(this.strCause);

            for (StackTraceElement cste : this.causedStackTraceElements) {
                System.err.printf("%sat \"%s.%s\" -> \"%s\": line %d" + System.lineSeparator(),
                    spc,
                    cste.getClassName(),
                    cste.getMethodName(),
                    cste.getFileName(),
                    cste.getLineNumber());
            }
        }
        System.err.println(System.lineSeparator() + "[EXCEPTION INFO]");
        System.err.println("Type: " + (this.isCausedException ? this.strCause.split(":\\s")[0] : this.getClass().getName()));
        System.err.println("Message: " + this.message);
    }

    /**
     * Returns the detail message of this exception.
     *
     * @return the detail message.
     *
     * @since  1.0.0b.1
     */
    @Override
    public String getMessage() {
        return (this.message != null ? this.message : this.strCause);
    }

    /**
     * Returns a string representation of this exception, including the class name and the detail message.
     *
     * @return a string representation of this exception.
     *
     * @since  1.0.0b.1
     */
    @Override
    public String toString() {
        return String.format("%s: %s", this.getClass().getName(), (this.isCausedException ? this.strCause : this.message));
    }
}
