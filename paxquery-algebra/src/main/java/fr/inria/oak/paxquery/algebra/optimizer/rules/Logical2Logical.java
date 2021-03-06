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
package fr.inria.oak.paxquery.algebra.optimizer.rules;

import fr.inria.oak.paxquery.algebra.operators.BaseLogicalOperator;


/**
 * An interface for all the Logical to Logical transformations.
 * 
 */
public interface Logical2Logical {
	
	/**
	 * Method that transforms a LogicalOperator into an equivalent LogicalOperator.
	 * @param operator the LogicalOperator that will be transformed
	 * @return the equivalent LogicalOperator obtained
	 */
	public BaseLogicalOperator transform(BaseLogicalOperator operator);

}
