/**
 * Licensed to jclouds, Inc. (jclouds) under one or more
 * contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  jclouds licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jclouds.examples.rackspace.cloudservers;

import static org.jclouds.compute.predicates.NodePredicates.inGroup;

import java.util.Set;

import org.jclouds.ContextBuilder;
import org.jclouds.compute.ComputeService;
import org.jclouds.compute.ComputeServiceContext;
import org.jclouds.compute.domain.NodeMetadata;

/**
 * This example destroys the server created in the CreateServer example. 
 *  
 * @author Everett Toews
 */
public class DeleteServer {
	private static final String GROUP_NAME = "jclouds-example";
	private ComputeService compute;

	/**
	 * To get a username and API key see http://www.jclouds.org/documentation/quickstart/rackspace/
	 * 
	 * The first argument (args[0]) must be your username
	 * The second argument (args[1]) must be your API key
	 */
	public static void main(String[] args) {
		DeleteServer deleteServer = new DeleteServer();
		
		try {
			deleteServer.init(args);
			deleteServer.deleteServer();
		} 
		finally {
			deleteServer.close();
		}
	}

	private void init(String[] args) {	
		// The provider configures jclouds to use the Rackspace open cloud
		String provider = "rackspace-cloudservers-us";
		
		String username = args[0];
		String apiKey = args[1];

		ComputeServiceContext context = ContextBuilder.newBuilder(provider)
			.credentials(username, apiKey)
			.buildView(ComputeServiceContext.class);
		compute = context.getComputeService();
	}
	
	private void deleteServer() {
		System.out.println("Delete Server");
		
		// This method will continue to poll for the server status and won't return until this server is DELETED
		// If you want to know what's happening during the polling, enable logging. See
		// /jclouds-exmaple/rackspace/src/main/java/org/jclouds/examples/rackspace/Logging.java
		Set<? extends NodeMetadata> servers = compute.destroyNodesMatching(inGroup(GROUP_NAME));		
		
		for (NodeMetadata nodeMetadata: servers) {
			System.out.println("  " + nodeMetadata);
		}
	}

	/**
	 * Always close your service when you're done with it.
	 */
	private void close() {
		if (compute != null) {
			compute.getContext().close();
		}
	}
}
