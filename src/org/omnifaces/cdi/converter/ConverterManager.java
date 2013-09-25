/*
 * Copyright 2013 OmniFaces.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.omnifaces.cdi.converter;

import static org.omnifaces.util.Beans.getReference;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

import org.omnifaces.application.ConverterProvider;
import org.omnifaces.application.OmniApplication;

/**
 * Provides access to all {@link FacesConverter} annotated {@link Converter} instances which are made eligible for CDI.
 *
 * @author Radu Creanga <rdcrng@gmail.com>
 * @author Bauke Scholtz
 * @see OmniApplication
 * @since 1.6
 */
@ApplicationScoped
public class ConverterManager implements ConverterProvider {

	// Dependencies ---------------------------------------------------------------------------------------------------

	@Inject
	private BeanManager manager;
	private ConverterExtension extension;

	// Init -----------------------------------------------------------------------------------------------------------

	@PostConstruct
	public void init() {
		extension = getReference(manager, ConverterExtension.class);
	}

	// Actions --------------------------------------------------------------------------------------------------------

	@Override
	public Converter createConverter(String converterId) {
		return getConverterReference(extension.getConvertersByID().get(converterId));
	}

	@Override
	public Converter createConverter(Class<?> converterForClass) {
		return getConverterReference(extension.getConvertersByForClass().get(converterForClass));
	}

	// Helpers --------------------------------------------------------------------------------------------------------

	private Converter getConverterReference(Bean<Converter> bean) {
		if (bean == null) {
			return null;
		}

		return getReference(manager, bean);
	}

}