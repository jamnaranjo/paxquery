/*******************************************************************************
 * Copyright (C) 2013, 2014, 2015 by Inria and Paris-Sud University
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package fr.inria.oak.paxquery.common.xml.construction;

import java.io.Serializable;

/**
 * Represents the edges that will connect two nodes in a {@link ConstructionTreePattern}.
 * 
 */
public final class ConstructionTreePatternEdge implements Serializable {
		
	private ConstructionTreePatternNode parent;
	private ConstructionTreePatternNode child;
		
	
	public ConstructionTreePatternEdge(ConstructionTreePatternNode parent, ConstructionTreePatternNode child) {
		this.parent = parent;
		this.child = child;
	}
	

	public ConstructionTreePatternNode getParent() {
		return this.parent;
	}

	public ConstructionTreePatternNode getChild() {
		return this.child;
	}
	
	
}
