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
package com.strategicgains.hyperexpress.domain;

import java.util.Collection;


/**
 * Resource defines an interface for a RESTful resource that contains one or more links to a URL.
 * 
 * @author toddf
 * @since Jan 10, 2014
 */
public interface Resource
{
	/**
	 * Define a link relationship from the resource to a URL.
	 * 
	 * @param linkDefinition an abstraction of a link as a LinkDefinition instance.
	 */
	public void addLink(LinkDefinition linkDefinition);

	/**
	 * Define a link relationship from the resource to a URL.
	 * 
	 * @param linkDefinition an abstraction of a link as a LinkDefinition instance.
	 */
	public void addLinks(Collection<LinkDefinition> links);
}