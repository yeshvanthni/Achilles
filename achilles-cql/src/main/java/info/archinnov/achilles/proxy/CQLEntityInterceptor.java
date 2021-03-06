/**
 *
 * Copyright (C) 2012-2013 DuyHai DOAN
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package info.archinnov.achilles.proxy;

import info.archinnov.achilles.context.CQLPersistenceContext;
import info.archinnov.achilles.entity.metadata.PropertyMeta;
import info.archinnov.achilles.entity.operations.CQLEntityLoader;
import info.archinnov.achilles.entity.operations.CQLEntityPersister;
import info.archinnov.achilles.entity.operations.CQLEntityProxifier;
import info.archinnov.achilles.proxy.wrapper.CQLCounterWrapper;
import info.archinnov.achilles.type.Counter;

public class CQLEntityInterceptor<T> extends EntityInterceptor<CQLPersistenceContext, T> {

	public CQLEntityInterceptor() {
		super.loader = new CQLEntityLoader();
		super.persister = new CQLEntityPersister();
		super.proxifier = new CQLEntityProxifier();
	}

	@Override
	protected Counter buildCounterWrapper(PropertyMeta propertyMeta) {
		return new CQLCounterWrapper(context, propertyMeta);
	}

}
