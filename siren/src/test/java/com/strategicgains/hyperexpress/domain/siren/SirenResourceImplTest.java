/*
    Copyright 2014, Strategic Gains, Inc.

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

		http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
*/
package com.strategicgains.hyperexpress.domain.siren;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;

import com.strategicgains.hyperexpress.builder.TokenResolver;
import com.strategicgains.hyperexpress.domain.Namespace;
import com.strategicgains.hyperexpress.domain.siren.SirenLinkBuilder;
import com.strategicgains.hyperexpress.domain.siren.SirenResource;
import com.strategicgains.hyperexpress.exception.ResourceException;

/**
 * @author toddf
 * @since Mar 18, 2014
 */
public class SirenResourceImplTest
{
	@Test(expected=ResourceException.class)
	public void shouldThrowOnMissingRel()
	{
		SirenResource r = new SirenResource();
		r.addLink(new SirenLinkBuilder("/something").build(new TokenResolver()));
	}

	@Test(expected=ResourceException.class)
	public void shouldThrowOnNullLink()
	{
		SirenResource r = new SirenResource();
		r.addLink(null);
	}

	@Test(expected=ResourceException.class)
	public void shouldThrowOnNullEmbed()
	{
		SirenResource r = new SirenResource();
		r.addResource("something", null);
	}

	@Test(expected=ResourceException.class)
	public void shouldThrowOnNullEmbedRel()
	{
		SirenResource r = new SirenResource();
		r.addResource(null, new SirenResource());
	}

	@Test(expected=ResourceException.class)
	public void shouldThrowOnNullCurie()
	{
		SirenResource r = new SirenResource();
		r.addNamespace(null);
	}

	@Test(expected=ResourceException.class)
	public void shouldThrowOnNamelessCurie()
	{
		SirenResource r = new SirenResource();
		r.addNamespace(new Namespace(null, "/sample/{rel}"));
	}

	@Test
	public void shouldAddCurieRel()
	{
		SirenResource r = new SirenResource();
		r.addNamespace(new Namespace("some-name", "/sample/{rel}"));
		r.addNamespace(new Namespace("another-name", "/sample/{rel}"));

		List<Namespace> curies = r.getNamespaces();
		assertNotNull(curies);
		assertEquals(2, curies.size());
	}

	@Test
	public void shouldAddNamespace()
	{
		SirenResource r = new SirenResource();
		r.addNamespace("ea:blah", "/sample/{rel}");

		List<Namespace> curies = r.getNamespaces();
		assertNotNull(curies);
		assertEquals(1, curies.size());
		assertEquals("/sample/{rel}", curies.get(0).href());
		assertEquals("ea:blah", curies.get(0).name());
	}
}
