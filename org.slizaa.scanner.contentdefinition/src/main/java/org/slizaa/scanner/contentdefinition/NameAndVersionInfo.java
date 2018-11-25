/*******************************************************************************
 * Copyright (C) 2011-2017 Gerd Wuetherich (gerd@gerd-wuetherich.de).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Gerd Wuetherich (gerd@gerd-wuetherich.de) - initial API and implementation
 ******************************************************************************/
package org.slizaa.scanner.contentdefinition;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.io.IOException;

import org.slizaa.scanner.contentdefinition.internal.DefaultFileBasedContentInfoResolver;

/**
 * <p>
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public final class NameAndVersionInfo {

	/** - */
	private String _name;

	/** - */
	private String _binaryName;

	/** - */
	private String _version;

	/** - */
	private boolean _isSource = false;

	/**
	 * <p>
	 * </p>
	 * 
	 * @param file
	 * @return
	 */
	public static NameAndVersionInfo resolveNameAndVersion(File file) {
		try {
			if (file.isFile()) {
				return extractInfoFromFile(file);
			}

			return extractInfoFromPath(file);

			//
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * <p>
	 * </p>
	 * 
	 * @return
	 */
	public String getName() {
		return _name;
	}

	/**
	 * <p>
	 * </p>
	 * 
	 * @return
	 */
	public String getVersion() {
		return _version;
	}

	/**
	 * <p>
	 * </p>
	 * 
	 * @return
	 */
	public boolean isSource() {
		return _isSource;
	}

	/**
	 * @return the binaryName
	 */
	public String getBinaryName() {
		return _binaryName;
	}

	/**
	 * <p>
	 * Creates a new instance of type {@link NameAndVersionInfo}.
	 * </p>
	 * 
	 * @param name
	 * @param version
	 */
	private NameAndVersionInfo(String name, String binaryName, String version, boolean isSource) {
		checkNotNull(name);
		checkNotNull(version);

		_name = name;
		_binaryName = binaryName;
		_version = version;
		_isSource = isSource;
	}

	/**
	 * <p>
	 * </p>
	 *
	 * @param path
	 * @return
	 */
	private static NameAndVersionInfo extractInfoFromPath(File path) {

		String dirName = path.getName();
		int x = dirName.lastIndexOf('_');
		String version = "0.0.0";
		String name = dirName;

		if (x > 0) {
			name = dirName.substring(0, x);
			version = dirName.substring(x + 1);
		}

		return new NameAndVersionInfo(name, name, version, false);
	}

	/**
	 * <p>
	 * </p>
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	private static NameAndVersionInfo extractInfoFromFile(File file) throws IOException {

		//
		DefaultFileBasedContentInfoResolver resolver = new DefaultFileBasedContentInfoResolver();

		//
		if (resolver.resolve(file)) {

			// return the result
			return new NameAndVersionInfo(resolver.getName(), resolver.getBinaryName(), resolver.getVersion(),
					resolver.isSource());
		}

		// return the default result
		return new NameAndVersionInfo(file.getName(), file.getName(), "0.0.0", false);
	}

}
