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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.inria.oak.paxquery.algebra.operators.BaseLogicalOperator;
import fr.inria.oak.paxquery.common.aggregation.AggregationType;
import fr.inria.oak.paxquery.common.datamodel.metadata.MetadataTypes;
import fr.inria.oak.paxquery.common.datamodel.metadata.NestedMetadata;
import fr.inria.oak.paxquery.common.datamodel.metadata.NestedMetadataUtils;
import fr.inria.oak.paxquery.common.xml.navigation.Variable;


/**
 * Aggregation logical operator.
 *
 */
public class Aggregation extends BaseUnaryOperator {
	
	private static final Log logger = LogFactory.getLog(Aggregation.class);
	
	private int[] aggregationPath;
	
	private AggregationType aggregationType;
	
	private boolean excludeNestedField;
	
	private int documentIDColumn;
	
	private Variable outerVariable;	//variable to which this aggregation is assigned to
	private Variable innerVariable;	//variable being aggregated
	

	public Aggregation(BaseLogicalOperator child, int[] aggregationPath, AggregationType aggregationType) {
		super(child);

		this.aggregationPath = aggregationPath;
		this.aggregationType = aggregationType;
		this.documentIDColumn = -1;
		this.excludeNestedField = false;
		
		this.visible = true;
		this.ownName = "Aggregation";
		
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		for (int i = 0; i < this.aggregationPath.length; i ++){
			sb.append(this.aggregationPath[i]);
			if (i < this.aggregationPath.length - 1){
				sb.append(".");
			}
		}
		sb.append("]");
		this.ownDetails = new String(sb);
	}
	
	/*public Aggregation(BaseLogicalOperator child, int[] aggregationPath, AggregationType aggregationType, Variable outerVariable) {
		this(child, aggregationPath, aggregationType);
		this.outerVariable = outerVariable;
	}*/
	public Aggregation(BaseLogicalOperator child, int[] aggregationPath, AggregationType aggregationType, Variable outerVariable, Variable innerVariable) {
		this(child, aggregationPath, aggregationType);
		this.outerVariable = outerVariable;
		this.innerVariable = innerVariable;
	}
	
	public Aggregation(BaseLogicalOperator child, int[] aggregationPath, AggregationType aggregationType,
			int documentIDColumn, boolean excludeNestedField) {
		this(child, aggregationPath, aggregationType);

		this.documentIDColumn = documentIDColumn;
		this.excludeNestedField = excludeNestedField;
	}
	
	@Override
	public void buildNRSMD() {
		BaseLogicalOperator child = children.get(0);
		
		MetadataTypes[] attScanMeta = new MetadataTypes[1];
		attScanMeta[0] = MetadataTypes.STRING_TYPE;
		NestedMetadata aggregationColumnNRSMD = new NestedMetadata(1,attScanMeta);
		if(aggregationPath.length > 1) {
			final int length = aggregationPath.length - 2;
			if(length != 0) {
				int[] pathExceptLasts = new int[length];
				System.arraycopy(aggregationPath, 0, pathExceptLasts, 0, length);
				NestedMetadata newNestedNRSMD = NestedMetadataUtils.appendNRSMD(child.getNRSMD().getNestedChild(pathExceptLasts), aggregationColumnNRSMD);
				this.nestedMetadata = NestedMetadataUtils.addNestedField(child.getNRSMD(), pathExceptLasts, newNestedNRSMD);
			}
			else
				this.nestedMetadata = NestedMetadataUtils.appendNRSMD(child.getNRSMD(), aggregationColumnNRSMD);
		}
		else {
			NestedMetadata emptyNRSMD = NestedMetadataUtils.emptyNRSMD();
			if(!this.excludeNestedField) {
				NestedMetadata nestedNRSMD = NestedMetadataUtils.addNestedField(emptyNRSMD, child.getNRSMD());
				this.nestedMetadata = NestedMetadataUtils.appendNRSMD(nestedNRSMD, aggregationColumnNRSMD);
			}
			else
				this.nestedMetadata = NestedMetadataUtils.appendNRSMD(emptyNRSMD, aggregationColumnNRSMD);
		}
	}
	
	
	public int[] getAggregationPath() {
		return this.aggregationPath;
	}
	
	public void setAggregationPath(int[] aggregationPath) {
		this.aggregationPath = aggregationPath;
	}
	
	public AggregationType getAggregationType() {
		return this.aggregationType;
	}
	
	public boolean isExcludeNestedField() {
		return this.excludeNestedField;
	}
	
	public int getDocumentIDColumn() {
		return this.documentIDColumn;
	}
	
	public Variable getOuterVariable() {
		return this.outerVariable;
	}
	
	public void setOuterVariable(Variable outerVariable) {
		this.outerVariable = outerVariable;
	}
	
	public Variable getInnerVariable() {
		return this.innerVariable;
	}
	
	public void setInnerVariable(Variable innerVariable) {
		this.innerVariable = innerVariable;
	}
	
}
