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
package org.slizaa.scanner.spi.contentdefinition;

import java.util.List;

/**
 * <p>
 * Represents the definition of the system that should be analyzed.
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface IContentDefinitionProvider {

    /**
     * Exports this {@link IContentDefinitionProvider} to JSON.
     *
     * @return
     */
    String exportToJson();

    /**
     * Imports this {@link IContentDefinitionProvider} from its JSON representation.
     *
     * @param json
     */
    void importFromJson(String json);

    /**
     * <p>
     * Returns a <b>unmodifiable</b> list with all the defined {@link IContentDefinition IContentDefinitions}.
     * </p>
     *
     * @return a <b>unmodifiable</b> list with all the defined {@link IContentDefinition IContentDefinitions}.
     */
    List<? extends IContentDefinition> getContentDefinitions();
}
