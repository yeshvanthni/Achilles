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
package info.archinnov.achilles.test.integration.tests;

import static org.fest.assertions.api.Assertions.assertThat;
import info.archinnov.achilles.entity.manager.CQLPersistenceManager;
import info.archinnov.achilles.junit.AchillesTestResource.Steps;
import info.archinnov.achilles.proxy.CQLEntityInterceptor;
import info.archinnov.achilles.test.integration.AchillesInternalCQLResource;
import info.archinnov.achilles.test.integration.entity.CompleteBean;
import info.archinnov.achilles.test.integration.entity.CompleteBeanTestBuilder;
import net.sf.cglib.proxy.Factory;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class LazyLoadingIT {
	@Rule
	public AchillesInternalCQLResource resource = new AchillesInternalCQLResource(Steps.AFTER_TEST, "CompleteBean");

	private CQLPersistenceManager manager = resource.getPersistenceManager();

	private CompleteBean bean;

	@Before
	public void setUp() {
		bean = CompleteBeanTestBuilder.builder().randomId().name("DuyHai").age(35L).addFriends("foo", "bar")
				.label("label").buid();

		manager.persist(bean);
	}

	@Test
	public void should_not_load_lazy_fields() throws Exception {
		bean = manager.find(CompleteBean.class, bean.getId());

		Factory proxy = (Factory) bean;
		CQLEntityInterceptor<?> interceptor = (CQLEntityInterceptor<?>) proxy.getCallback(0);
		CompleteBean trueBean = (CompleteBean) interceptor.getTarget();

		assertThat(trueBean.getLabel()).isNull();
		assertThat(trueBean.getFriends()).isNull();

		// Trigger loading of lazy fields
		assertThat(bean.getLabel()).isEqualTo("label");
		assertThat(bean.getFriends()).containsExactly("foo", "bar");

		assertThat(trueBean.getLabel()).isEqualTo("label");
		assertThat(trueBean.getFriends()).containsExactly("foo", "bar");
	}

	@Test
	public void should_set_lazy_field() throws Exception {
		bean = manager.find(CompleteBean.class, bean.getId());

		bean.setLabel("newLabel");

		assertThat(bean.getLabel()).isEqualTo("newLabel");
	}
}
