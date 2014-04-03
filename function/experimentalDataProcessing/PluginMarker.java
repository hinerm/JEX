/*
 * #%L
 * SciJava Common shared library for SciJava software.
 * %%
 * Copyright (C) 2009 - 2014 Board of Regents of the University of
 * Wisconsin-Madison, Broad Institute of MIT and Harvard, and Max Planck
 * Institute of Molecular Cell Biology and Genetics.
 * %%
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */

package function.experimentalDataProcessing;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.scijava.annotations.Indexable;
import org.scijava.plugin.PluginService;
import org.scijava.plugin.SciJavaPlugin;

/**
 * Annotation identifying a plugin, which gets loaded by SciJava's dynamic
 * discovery mechanism.
 * 
 * @author Curtis Rueden
 * @see SciJavaPlugin
 * @see PluginService
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Indexable
public @interface PluginMarker {

	/** The type of plugin; e.g., {@link org.scijava.service.Service}. */
	Class<? extends SciJavaPlugin> type();

	/** The human-readable name of the plugin. */
	String name() default "";

	/** The name of the toolbox to place the plugin within*/
	String toolbox() default "Sandbox";
	
	/** Whether to make this plugin visible within the list of plugins in this toolbox*/
	boolean visibleInToolbox() default true;

	/** A longer description of the plugin (e.g., for use as a tool tip). */
	String description() default "";
	
	/** A link to documentation for the function. */
	String docURL() default "https://github.com/jaywarrick/JEX/wiki";
	
	/** A longer description of the plugin (e.g., for use as a tool tip). */
	boolean allowMultiThreading() default true;

}
