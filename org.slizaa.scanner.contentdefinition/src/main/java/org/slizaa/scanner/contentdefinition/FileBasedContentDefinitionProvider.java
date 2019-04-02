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

import java.io.File;
import java.io.IOException;

import org.slizaa.scanner.spi.contentdefinition.AbstractContentDefinitionProvider;
import org.slizaa.scanner.spi.contentdefinition.AnalyzeMode;
import org.slizaa.scanner.spi.contentdefinition.IContentDefinition;
import org.slizaa.scanner.spi.contentdefinition.IContentDefinitionProvider;

/**
 * <p>
 * Superclass for all implementations of {@link ITempDefinitionProvider}
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
@Deprecated
public class FileBasedContentDefinitionProvider extends AbstractContentDefinitionProvider
		implements IContentDefinitionProvider {


	@Override
	public String exportToJson() {
		return null;
	}

	@Override
	public void importFromJson(String json) {

	}

	@Override
	public IContentDefinition createFileBasedContentDefinition(String contentName, String contentVersion,
			File[] binaryPaths, File[] sourcePaths, AnalyzeMode analyzeMode) {

		//
		return super.createFileBasedContentDefinition(contentName, contentVersion, binaryPaths, sourcePaths,
				analyzeMode);
	}

	@Override
	protected void onInitializeProjectContent() {
		//
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onDisposeProjectContent() {
		//
	}
}
