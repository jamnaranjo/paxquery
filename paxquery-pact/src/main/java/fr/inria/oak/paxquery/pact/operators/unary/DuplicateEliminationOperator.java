/*******************************************************************************
 * Copyright (C) 2013, 2014 by Inria and Paris-Sud University
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
package fr.inria.oak.paxquery.pact.operators.unary;

import java.util.Iterator;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.lang.SerializationUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import eu.stratosphere.configuration.Configuration;
import eu.stratosphere.types.Record;
import eu.stratosphere.util.Collector;
import fr.inria.oak.paxquery.pact.configuration.PACTOperatorsConfiguration;
import fr.inria.oak.paxquery.pact.operators.BaseReduceOperator;


/**
 * Duplicate elimination operator in PACT.
 * 
 */
public class DuplicateEliminationOperator extends BaseReduceOperator {
	
	private static final Log logger = LogFactory.getLog(DuplicateEliminationOperator.class);


	private int[] dupElimColumns;

	
	@Override
	public void open(Configuration parameters) throws Exception {
		super.open(parameters);
		
		String dupElimColumnsEncoded = parameters.getString(PACTOperatorsConfiguration.DUP_ELIM_COLUMNS_BINARY.toString(), null);
		byte[] dupElimColumnsBytes = DatatypeConverter.parseBase64Binary(dupElimColumnsEncoded);
		final int[] dupElimColumns = (int[]) SerializationUtils.deserialize(dupElimColumnsBytes);
		this.dupElimColumns = dupElimColumns;
	}

	@Override
	public void reduce(Iterator<Record> records, Collector<Record> collector) throws InstantiationException, IllegalAccessException {
		collector.collect(records.next());
	}

}