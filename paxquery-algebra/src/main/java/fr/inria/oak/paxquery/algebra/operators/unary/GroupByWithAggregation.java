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
package fr.inria.oak.paxquery.algebra.operators.unary;

import fr.inria.oak.paxquery.algebra.operators.BaseLogicalOperator;
import fr.inria.oak.paxquery.common.aggregation.AggregationType;
import fr.inria.oak.paxquery.common.datamodel.metadata.MetadataTypes;
import fr.inria.oak.paxquery.common.datamodel.metadata.NestedMetadata;
import fr.inria.oak.paxquery.common.datamodel.metadata.NestedMetadataUtils;
import fr.inria.oak.paxquery.common.exception.PAXQueryExecutionException;


/**
 * GroupBy combined with aggregation logical operator.
 *
 */
public class GroupByWithAggregation extends GroupBy {

	private int aggregationColumn;

	private final AggregationType aggregationType;
	
	private boolean excludeNestedField;
	

	public GroupByWithAggregation(BaseLogicalOperator child, int[] reduceByColumns,
			int[] groupByColumns, int[] nestColumns,
			int aggregationColumn, AggregationType aggregationType, 
			boolean excludeNestedField) throws PAXQueryExecutionException {
		super(child, reduceByColumns, groupByColumns, nestColumns);
    this.ownName = "GroupByWithAggregation";
		this.aggregationColumn = aggregationColumn;
		this.aggregationType = aggregationType;
		this.excludeNestedField = excludeNestedField;
	}
	
	@Override
	public void buildNRSMD() {
		super.buildNRSMD();
		if(this.excludeNestedField) {
			int[] keepColumns = new int[this.nestedMetadata.colNo-1];
			for(int i=0; i<this.nestedMetadata.colNo-1; i++) 
				keepColumns[i] = i;
			this.nestedMetadata = NestedMetadataUtils.makeProjectRSMD(this.nestedMetadata, keepColumns);
		}
		
		MetadataTypes[] attScanMeta = new MetadataTypes[1];
		attScanMeta[0] = MetadataTypes.STRING_TYPE;
		NestedMetadata aggregationColumnNRSMD = new NestedMetadata(1,attScanMeta);
		this.nestedMetadata = NestedMetadataUtils.appendNRSMD(this.nestedMetadata, aggregationColumnNRSMD);
	}
	

	public int getAggregationColumn() {
		return this.aggregationColumn;
	}

  public void setAggregationColumn(int aggregationColumn) {
	  this.aggregationColumn = aggregationColumn;
  }

	public AggregationType getAggregationType() {
		return this.aggregationType;
	}
	
	public boolean isExcludeNestedField() {
		return this.excludeNestedField;
	}

}
