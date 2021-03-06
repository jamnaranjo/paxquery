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
package fr.inria.oak.paxquery.algebra.test.parser;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.inria.oak.paxquery.algebra.operators.BaseLogicalOperator;
import fr.inria.oak.paxquery.algebra.operators.binary.CartesianProduct;
import fr.inria.oak.paxquery.algebra.operators.binary.Join;
import fr.inria.oak.paxquery.algebra.operators.binary.LeftOuterJoin;
import fr.inria.oak.paxquery.algebra.operators.binary.LeftOuterNestedJoin;
import fr.inria.oak.paxquery.algebra.operators.binary.LeftOuterNestedJoinWithAggregation;
import fr.inria.oak.paxquery.algebra.operators.border.XMLConstruct;
import fr.inria.oak.paxquery.algebra.operators.border.XMLScan;
import fr.inria.oak.paxquery.algebra.operators.unary.Aggregation;
import fr.inria.oak.paxquery.algebra.operators.unary.DuplicateElimination;
import fr.inria.oak.paxquery.algebra.operators.unary.Flatten;
import fr.inria.oak.paxquery.algebra.operators.unary.GroupBy;
import fr.inria.oak.paxquery.algebra.operators.unary.GroupByWithAggregation;
import fr.inria.oak.paxquery.algebra.operators.unary.Navigation;
import fr.inria.oak.paxquery.algebra.operators.unary.Projection;
import fr.inria.oak.paxquery.algebra.operators.unary.Selection;
import fr.inria.oak.paxquery.common.aggregation.AggregationType;
import fr.inria.oak.paxquery.common.predicates.ArithmeticOperation;
import fr.inria.oak.paxquery.common.predicates.ArithmeticOperation.Operation;
import fr.inria.oak.paxquery.common.predicates.ConjunctivePredicate;
import fr.inria.oak.paxquery.common.predicates.DisjunctivePredicate;
import fr.inria.oak.paxquery.common.predicates.BasePredicate;
import fr.inria.oak.paxquery.common.predicates.BasePredicate.PredicateType;
import fr.inria.oak.paxquery.common.predicates.SimplePredicate;
import fr.inria.oak.paxquery.common.xml.construction.ApplyConstruct;
import fr.inria.oak.paxquery.common.xml.navigation.NavigationTreePattern;
import fr.inria.oak.paxquery.common.xml.navigation.NavigationTreePatternUtils;



/**
 * A parser for logical plan files (.logp)
 *
 */
public class LogicalPlanParser implements LogicalPlanParserConstants {

        private static final Log logger = LogFactory.getLog(LogicalPlanParser.class);


        private BaseLogicalOperator logOp;

        private LogicalPlanParser(String fileName) throws Exception {
                this(new FileInputStream(fileName));
        }

        public static BaseLogicalOperator parseFile(java.io.InputStream stream) throws Exception {
                LogicalPlanParser parser = new LogicalPlanParser(stream);
                parser.Start();
                return parser.logOp;
        }

        public static BaseLogicalOperator parseFile(String pathToFile) throws Exception {
                LogicalPlanParser parser = new LogicalPlanParser(pathToFile);
                parser.Start();
                return parser.logOp;
        }


        public static void main(String args[]) throws Exception {
                LogicalPlanParser parser = new LogicalPlanParser(args[0]);
                parser.Start();
                System.out.println(parser.logOp.getName());
        }

  final public void Start() throws ParseException {
    LogicalPlan();
    jj_consume_token(0);
  }

  final public void LogicalPlan() throws ParseException {
    logOp = OperatorDEF();
  }

  final public BaseLogicalOperator OperatorDEF() throws ParseException {
        BaseLogicalOperator returnLogOp = null;
    if (jj_2_1(10)) {
      returnLogOp = BinaryOpDEF();
    } else if (jj_2_2(10)) {
      returnLogOp = UnaryOpDEF();
    } else if (jj_2_3(10)) {
      returnLogOp = LeafOpDEF();
    } else {
      jj_consume_token(-1);
      throw new ParseException();
    }
                {if (true) return returnLogOp;}
    throw new Error("Missing return statement in function");
  }

  final public BaseLogicalOperator BinaryOpDEF() throws ParseException {
        BaseLogicalOperator returnLogOp = null;

        Token binaryOp;
        BaseLogicalOperator tempLeftLogOp;
        BaseLogicalOperator tempRightLogOp;
        BasePredicate pred;
        int documentIDColumn = -1;
        int[] nodeIDColumns = null;
        int aggregationColumn = -1;
        Token tempAggregationType = null;
        Token tempExcludeNestedField = null;
    if (jj_2_9(10)) {
      binaryOp = jj_consume_token(CARTESIANPRODUCT);
      jj_consume_token(LPAREN);
      tempLeftLogOp = OperatorDEF();
      jj_consume_token(COMMA);
      tempRightLogOp = OperatorDEF();
      jj_consume_token(RPAREN);
                        try {
                                if (binaryOp.toString().equals("CartesianProduct"))
                                        returnLogOp = new CartesianProduct(tempLeftLogOp, tempRightLogOp);
                        } catch (Exception e) {
                                logger.error("Exception: ",e);
                        }
    } else if (jj_2_10(10)) {
      if (jj_2_4(10)) {
        binaryOp = jj_consume_token(JOIN);
      } else if (jj_2_5(10)) {
        binaryOp = jj_consume_token(LEFTOUTERJOIN);
      } else if (jj_2_6(10)) {
        binaryOp = jj_consume_token(LEFTOUTERNESTEDJOIN);
      } else if (jj_2_7(10)) {
        binaryOp = jj_consume_token(LEFTOUTERNESTEDJOINWITHAGGREGATION);
      } else {
        jj_consume_token(-1);
        throw new ParseException();
      }
      jj_consume_token(LPAREN);
      tempLeftLogOp = OperatorDEF();
      jj_consume_token(COMMA);
      tempRightLogOp = OperatorDEF();
      jj_consume_token(COMMA);
      pred = PredicateDEF();
      jj_consume_token(COMMA);
      documentIDColumn = ColumnDEF();
      jj_consume_token(COMMA);
      nodeIDColumns = ColumnsDEF();
      if (jj_2_8(10)) {
        jj_consume_token(COMMA);
        tempAggregationType = jj_consume_token(AGGREGATIONTYPE);
        jj_consume_token(COMMA);
        aggregationColumn = ColumnDEF();
        jj_consume_token(COMMA);
        tempExcludeNestedField = jj_consume_token(NAME);
      } else {
        ;
      }
      jj_consume_token(RPAREN);
                        try {
                                if (binaryOp.toString().equals("Join"))
                                        returnLogOp = new Join(tempLeftLogOp, tempRightLogOp, pred);
                                else if (binaryOp.toString().equals("LeftOuterJoin"))
                                        returnLogOp = new LeftOuterJoin(tempLeftLogOp, tempRightLogOp, pred,
                                                        documentIDColumn, nodeIDColumns);
                                else if (binaryOp.toString().equals("LeftOuterNestedJoin"))
                                        returnLogOp = new LeftOuterNestedJoin(tempLeftLogOp, tempRightLogOp, pred,
                                                        documentIDColumn, nodeIDColumns);
                                else if (binaryOp.toString().equals("LeftOuterNestedJoinWithAggregation")) {
                                        AggregationType aggregationType = AggregationType.valueOf(tempAggregationType.image);
                                        boolean excludeNestedField = Boolean.parseBoolean(tempExcludeNestedField.image);
                                        returnLogOp = new LeftOuterNestedJoinWithAggregation(tempLeftLogOp, tempRightLogOp, pred,
                                                        documentIDColumn, nodeIDColumns, aggregationColumn, aggregationType,
                                                        excludeNestedField);
                                }
                        } catch (Exception e) {
                                logger.error("Exception: ",e);
                        }
    } else {
      jj_consume_token(-1);
      throw new ParseException();
    }
                {if (true) return returnLogOp;}
    throw new Error("Missing return statement in function");
  }

  final public BaseLogicalOperator UnaryOpDEF() throws ParseException {
        BaseLogicalOperator returnLogOp = null;

        Token unaryOp;
        BaseLogicalOperator tempChildLogOp;
        ApplyConstruct apply;
        int[] columns1 = null;
        int[] columns2;
        BasePredicate pred;
        int column = -1;
        Token string;
        Token tempAggregationType = null;
        Token tempExcludeNestedField = null;
    if (jj_2_18(10)) {
      jj_consume_token(XMLCONSTRUCT);
      jj_consume_token(LPAREN);
      tempChildLogOp = OperatorDEF();
      jj_consume_token(COMMA);
      apply = ApplyConstructDEF();
      jj_consume_token(COMMA);
      string = jj_consume_token(STR);
      jj_consume_token(RPAREN);
                        try {
                                returnLogOp = new XMLConstruct(
                                                tempChildLogOp,
                                                apply,
                                                string.image.substring(1, string.image.length()-1));
                        } catch (Exception e) {
                                logger.error("Exception: ",e);
                        }
    } else if (jj_2_19(10)) {
      if (jj_2_11(10)) {
        unaryOp = jj_consume_token(PROJECTION);
      } else if (jj_2_12(10)) {
        unaryOp = jj_consume_token(DUPLICATEELIMINATION);
      } else {
        jj_consume_token(-1);
        throw new ParseException();
      }
      jj_consume_token(LPAREN);
      tempChildLogOp = OperatorDEF();
      jj_consume_token(COMMA);
      columns1 = ColumnsDEF();
      jj_consume_token(RPAREN);
                        try {
                                if (unaryOp.toString().equals("Projection"))
                                        returnLogOp = new Projection(tempChildLogOp, columns1);
                                else if (unaryOp.toString().equals("DuplicateElimination"))
                                        returnLogOp = new DuplicateElimination(tempChildLogOp, columns1);
                        } catch (Exception e) {
                                logger.error("Exception: ",e);
                        }
    } else if (jj_2_20(10)) {
      jj_consume_token(SELECTION);
      jj_consume_token(LPAREN);
      tempChildLogOp = OperatorDEF();
      jj_consume_token(COMMA);
      pred = PredicateDEF();
      jj_consume_token(RPAREN);
                        returnLogOp = new Selection(tempChildLogOp, pred);
    } else if (jj_2_21(10)) {
      jj_consume_token(NAVIGATION);
      jj_consume_token(LPAREN);
      tempChildLogOp = OperatorDEF();
      jj_consume_token(COMMA);
      column = ColumnDEF();
      jj_consume_token(COMMA);
      string = jj_consume_token(TREEPATTERNSTRING);
      jj_consume_token(RPAREN);
                        try {
                                NavigationTreePattern pat  = NavigationTreePatternUtils.getTreePatternFromString(
                                string.toString().replace("|","").replace("|",""),
                        "ntp");
                                returnLogOp = new Navigation(tempChildLogOp, column, pat);
                        } catch (Exception e) {
                                logger.error("Exception: ",e);
                        }
    } else if (jj_2_22(10)) {
      if (jj_2_13(10)) {
        unaryOp = jj_consume_token(GROUPBY);
      } else if (jj_2_14(10)) {
        unaryOp = jj_consume_token(GROUPBYWITHAGGREGATION);
      } else {
        jj_consume_token(-1);
        throw new ParseException();
      }
      jj_consume_token(LPAREN);
      tempChildLogOp = OperatorDEF();
      jj_consume_token(COMMA);
      columns1 = ColumnsDEF();
      jj_consume_token(COMMA);
      columns2 = ColumnsDEF();
      if (jj_2_15(10)) {
        jj_consume_token(COMMA);
        tempAggregationType = jj_consume_token(AGGREGATIONTYPE);
        jj_consume_token(COMMA);
        column = ColumnDEF();
        jj_consume_token(COMMA);
        tempExcludeNestedField = jj_consume_token(NAME);
      } else {
        ;
      }
      jj_consume_token(RPAREN);
                        try {
                                if (unaryOp.toString().equals("GroupByWithAggregation"))
                                {
                                        AggregationType aggregationType = AggregationType.valueOf(tempAggregationType.image);
                                        boolean excludeNestedField = Boolean.parseBoolean(tempExcludeNestedField.image);
                                        returnLogOp = new GroupByWithAggregation(tempChildLogOp, columns1, columns1, columns2,
                                                        column, aggregationType, excludeNestedField);
                                }
                                else
                                        returnLogOp = new GroupBy(tempChildLogOp, columns1, columns1, columns2);
                        } catch (Exception e) {
                                logger.error("Exception: ",e);
                        }
    } else if (jj_2_23(10)) {
      jj_consume_token(FLATTEN);
      jj_consume_token(LPAREN);
      tempChildLogOp = OperatorDEF();
      if (jj_2_16(10)) {
        jj_consume_token(COMMA);
        columns1 = ColumnsDEF();
      } else {
        ;
      }
      jj_consume_token(RPAREN);
                        try {
                                if(columns1 != null)
                                        returnLogOp = new Flatten(tempChildLogOp, columns1);
                                else
                                        returnLogOp = new Flatten(tempChildLogOp);
                        } catch (Exception e) {
                                logger.error("Exception: ",e);
                        }
    } else if (jj_2_24(10)) {
      jj_consume_token(AGGREGATION);
      jj_consume_token(LPAREN);
      tempChildLogOp = OperatorDEF();
      jj_consume_token(COMMA);
      tempAggregationType = jj_consume_token(AGGREGATIONTYPE);
      columns1 = ColumnsDEF();
      if (jj_2_17(10)) {
        jj_consume_token(COMMA);
        column = ColumnDEF();
        jj_consume_token(COMMA);
        tempExcludeNestedField = jj_consume_token(NAME);
      } else {
        ;
      }
      jj_consume_token(RPAREN);
                        try {
                                AggregationType aggregationType = AggregationType.valueOf(tempAggregationType.image);
                                if(column != -1) {
                                        boolean excludeNestedField = Boolean.parseBoolean(tempExcludeNestedField.image);
                                        returnLogOp = new Aggregation(tempChildLogOp, columns1, aggregationType, column, excludeNestedField);
                                }
                                else
                                        returnLogOp = new Aggregation(tempChildLogOp, columns1, aggregationType);
                        } catch (Exception e) {
                                logger.error("Exception: ",e);
                        }
    } else {
      jj_consume_token(-1);
      throw new ParseException();
    }
                {if (true) return returnLogOp;}
    throw new Error("Missing return statement in function");
  }

  final public BaseLogicalOperator LeafOpDEF() throws ParseException {
        BaseLogicalOperator returnLogOp = null;

        Token name = null;
        Token string = null;
        Token path;
        String[] listDocs = null;
        Token attachDocumentID;
    jj_consume_token(XMLSCAN);
    jj_consume_token(LPAREN);
    path = jj_consume_token(STR);
    if (jj_2_25(10)) {
      jj_consume_token(COMMA);
      listDocs = ListDocsDEF();
    } else {
      ;
    }
    jj_consume_token(COMMA);
    attachDocumentID = jj_consume_token(NAME);
    if (jj_2_26(10)) {
      jj_consume_token(COMMA);
      name = jj_consume_token(NAME);
      jj_consume_token(COMMA);
      string = jj_consume_token(TREEPATTERNSTRING);
    } else {
      ;
    }
    jj_consume_token(RPAREN);
                        try {
                                if(string != null && listDocs != null) {
                                        NavigationTreePattern pat  = NavigationTreePatternUtils.getTreePatternFromString(
                                        string.toString().replace("|","").replace("|",""),
                                name.toString());
                                        returnLogOp = new XMLScan(
                                                        Boolean.parseBoolean(attachDocumentID.image),
                                                        pat,
                                                        path.image.substring(1, path.image.length()-1),
                                                        listDocs);
                                }
                                else if(string != null) {
                                        NavigationTreePattern pat  = NavigationTreePatternUtils.getTreePatternFromString(
                                        string.toString().replace("|","").replace("|",""),
                                name.toString());
                                        returnLogOp = new XMLScan(
                                                        Boolean.parseBoolean(attachDocumentID.image),
                                                        pat,
                                                        path.image.substring(1, path.image.length()-1));
                                }
                                else if(listDocs != null) {
                                        returnLogOp = new XMLScan(
                                                        Boolean.parseBoolean(attachDocumentID.image),
                                                        path.image.substring(1, path.image.length()-1),
                                                        listDocs);
                                }
                                else {
                                        returnLogOp = new XMLScan(
                                                        Boolean.parseBoolean(attachDocumentID.image),
                                                        path.image.substring(1, path.image.length()-1));
                                }
                        } catch (Exception e) {
                                logger.error("Exception: ",e);
                        }
                {if (true) return returnLogOp;}
    throw new Error("Missing return statement in function");
  }

  final public DisjunctivePredicate PredicateDEF() throws ParseException {
        ArrayList<ConjunctivePredicate> list = new ArrayList<ConjunctivePredicate>();
        ConjunctivePredicate tempCPred = null;
    jj_consume_token(LSBRACKET);
    tempCPred = ConjunctivePredicateDEF();
                                                               list.add(tempCPred);
    label_1:
    while (true) {
      if (jj_2_27(10)) {
        ;
      } else {
        break label_1;
      }
      jj_consume_token(OR);
      tempCPred = ConjunctivePredicateDEF();
                                                        list.add(tempCPred);
    }
    jj_consume_token(RSBRACKET);
                {if (true) return new DisjunctivePredicate(list);}
    throw new Error("Missing return statement in function");
  }

  final public ConjunctivePredicate ConjunctivePredicateDEF() throws ParseException {
        ArrayList<SimplePredicate> list = new ArrayList<SimplePredicate>();
        SimplePredicate tempSPred = null;
    jj_consume_token(LPAREN);
    tempSPred = SimplePredicateDEF();
                                                       list.add(tempSPred);
    label_2:
    while (true) {
      if (jj_2_28(10)) {
        ;
      } else {
        break label_2;
      }
      jj_consume_token(AND);
      tempSPred = SimplePredicateDEF();
                                                    list.add(tempSPred);
    }
    jj_consume_token(RPAREN);
                {if (true) return new ConjunctivePredicate(list);}
    throw new Error("Missing return statement in function");
  }

  final public SimplePredicate SimplePredicateDEF() throws ParseException {
        Token tempPredCode;
        ArithmeticOperation arithOperation1 = null;
        ArithmeticOperation arithOperation2 = null;
        int column1 = -1;
        int column2 = -1;
        Token stringConstant = null;
        Token intConstant = null;
    column1 = ColumnDEF();
    if (jj_2_29(10)) {
      arithOperation1 = ArithmeticOperationDEF();
    } else {
      ;
    }
    tempPredCode = jj_consume_token(PREDCODE);
    if (jj_2_31(10)) {
      column2 = ColumnDEF();
      if (jj_2_30(10)) {
        arithOperation2 = ArithmeticOperationDEF();
      } else {
        ;
      }
    } else if (jj_2_32(10)) {
      stringConstant = jj_consume_token(STR);
    } else if (jj_2_33(10)) {
      intConstant = jj_consume_token(NUMBER);
    } else {
      jj_consume_token(-1);
      throw new ParseException();
    }
                PredicateType predCode = null;
                if(tempPredCode.toString().equals("="))
                        predCode = PredicateType.PREDICATE_EQUAL;
                else if(tempPredCode.toString().equals("!="))
                        predCode = PredicateType.PREDICATE_NOTEQUAL;
                else if(tempPredCode.toString().equals(">="))
                        predCode = PredicateType.PREDICATE_GREATEROREQUALTHAN;
                else if(tempPredCode.toString().equals(">"))
                        predCode = PredicateType.PREDICATE_GREATERTHAN;
                else if(tempPredCode.toString().equals("<="))
                        predCode = PredicateType.PREDICATE_SMALLEROREQUALTHAN;
                else if(tempPredCode.toString().equals("<"))
                        predCode = PredicateType.PREDICATE_SMALLERTHAN;

                if(arithOperation1 != null || arithOperation2 != null)
                        {if (true) return new SimplePredicate(column1, arithOperation1, column2, arithOperation2, predCode);}
                else if(stringConstant != null) {
                        String constantColumn2 = StringEscapeUtils.unescapeJava(stringConstant.image.substring(1, stringConstant.image.length()-1));
                        {if (true) return new SimplePredicate(column1, constantColumn2, predCode);}
                }
                else if(intConstant != null) {
                        Double constantColumn2 = Double.parseDouble(intConstant.image);
                        {if (true) return new SimplePredicate(column1, constantColumn2, predCode);}
                } else {
                        {if (true) return new SimplePredicate(column1, column2, predCode);}
                }
    throw new Error("Missing return statement in function");
  }

  final public ArithmeticOperation ArithmeticOperationDEF() throws ParseException {
        Token tempArithCode;
        Token constant;
    tempArithCode = jj_consume_token(ARITHCODE);
    constant = jj_consume_token(NUMBER);
                {if (true) return new ArithmeticOperation(Operation.valueOf(tempArithCode.image), Double.parseDouble(constant.image));}
    throw new Error("Missing return statement in function");
  }

  final public int[] ColumnsDEF() throws ParseException {
        int column;
    ArrayList<Integer> list = new ArrayList<Integer>();
    jj_consume_token(LSBRACKET);
    if (jj_2_34(10)) {
      column = ColumnDEF();
                                            list.add(column);
    } else {
      ;
    }
    label_3:
    while (true) {
      if (jj_2_35(10)) {
        ;
      } else {
        break label_3;
      }
      jj_consume_token(COMMA);
      column = ColumnDEF();
                                       list.add(column);
    }
    jj_consume_token(RSBRACKET);
                int[] result = new int[list.size()];
            for (int i=0; i < result.length; i++) {
                result[i] = list.get(i).intValue();
            }
            {if (true) return result;}
    throw new Error("Missing return statement in function");
  }

  final public int ColumnDEF() throws ParseException {
        Token t;
    jj_consume_token(DOLLAR);
    t = jj_consume_token(NUMBER);
                                {if (true) return Integer.parseInt(t.image);}
    throw new Error("Missing return statement in function");
  }

  final public String[] ListDocsDEF() throws ParseException {
        Token t;
    ArrayList<String> list = new ArrayList<String>();
    jj_consume_token(LPAREN);
    t = jj_consume_token(NAME);
                                list.add(t.toString());
    label_4:
    while (true) {
      jj_consume_token(COMMA);
      t = jj_consume_token(NAME);
                               list.add(t.toString());
      if (jj_2_36(10)) {
        ;
      } else {
        break label_4;
      }
    }
    jj_consume_token(RPAREN);
                String[] result = new String[list.size()];
            for (int i=0; i < result.length; i++) {
                result[i] = list.get(i);
            }
            {if (true) return result;}
    throw new Error("Missing return statement in function");
  }

  final public ApplyConstruct ApplyConstructDEF() throws ParseException {
        ApplyConstruct apply;
        Token before;
        Token after;

        Token tempEach;
        int column;
        ApplyConstruct tempApply;
        List<String> each = new ArrayList<String>();
        List<Integer> fields = new ArrayList<Integer>();
        List<ApplyConstruct> nested = new ArrayList<ApplyConstruct>();
    jj_consume_token(LSBRACKET);
    before = jj_consume_token(STR);
    jj_consume_token(COMMA);
    after = jj_consume_token(STR);
    jj_consume_token(COMMA);
    if (jj_2_37(10)) {
      tempEach = jj_consume_token(STR);
                           each.add(StringEscapeUtils.unescapeJava(tempEach.image.substring(1, tempEach.image.length()-1)));
    } else {
      ;
    }
    label_5:
    while (true) {
      if (jj_2_38(10)) {
        ;
      } else {
        break label_5;
      }
      jj_consume_token(LPAREN);
      column = ColumnDEF();
                                        fields.add(column);
      jj_consume_token(RPAREN);
      if (jj_2_39(10)) {
        tempApply = ApplyConstructDEF();
                                            nested.add(tempApply);
      } else {
        ;
      }
      tempEach = jj_consume_token(STR);
                        each.add(StringEscapeUtils.unescapeJava(tempEach.image.substring(1, tempEach.image.length()-1)));
    }
    jj_consume_token(RSBRACKET);
                {if (true) return new ApplyConstruct(StringEscapeUtils.unescapeJava(before.image.substring(1, before.image.length()-1)),
                        each.toArray(new String[each.size()]),
                        StringEscapeUtils.unescapeJava(after.image.substring(1, after.image.length()-1)),
                        ArrayUtils.toPrimitive(fields.toArray(new Integer[fields.size()])),
                        nested.toArray(new ApplyConstruct[nested.size()]));}
    throw new Error("Missing return statement in function");
  }

  private boolean jj_2_1(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_1(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(0, xla); }
  }

  private boolean jj_2_2(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_2(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(1, xla); }
  }

  private boolean jj_2_3(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_3(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(2, xla); }
  }

  private boolean jj_2_4(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_4(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(3, xla); }
  }

  private boolean jj_2_5(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_5(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(4, xla); }
  }

  private boolean jj_2_6(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_6(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(5, xla); }
  }

  private boolean jj_2_7(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_7(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(6, xla); }
  }

  private boolean jj_2_8(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_8(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(7, xla); }
  }

  private boolean jj_2_9(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_9(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(8, xla); }
  }

  private boolean jj_2_10(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_10(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(9, xla); }
  }

  private boolean jj_2_11(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_11(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(10, xla); }
  }

  private boolean jj_2_12(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_12(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(11, xla); }
  }

  private boolean jj_2_13(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_13(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(12, xla); }
  }

  private boolean jj_2_14(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_14(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(13, xla); }
  }

  private boolean jj_2_15(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_15(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(14, xla); }
  }

  private boolean jj_2_16(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_16(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(15, xla); }
  }

  private boolean jj_2_17(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_17(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(16, xla); }
  }

  private boolean jj_2_18(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_18(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(17, xla); }
  }

  private boolean jj_2_19(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_19(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(18, xla); }
  }

  private boolean jj_2_20(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_20(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(19, xla); }
  }

  private boolean jj_2_21(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_21(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(20, xla); }
  }

  private boolean jj_2_22(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_22(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(21, xla); }
  }

  private boolean jj_2_23(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_23(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(22, xla); }
  }

  private boolean jj_2_24(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_24(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(23, xla); }
  }

  private boolean jj_2_25(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_25(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(24, xla); }
  }

  private boolean jj_2_26(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_26(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(25, xla); }
  }

  private boolean jj_2_27(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_27(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(26, xla); }
  }

  private boolean jj_2_28(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_28(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(27, xla); }
  }

  private boolean jj_2_29(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_29(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(28, xla); }
  }

  private boolean jj_2_30(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_30(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(29, xla); }
  }

  private boolean jj_2_31(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_31(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(30, xla); }
  }

  private boolean jj_2_32(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_32(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(31, xla); }
  }

  private boolean jj_2_33(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_33(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(32, xla); }
  }

  private boolean jj_2_34(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_34(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(33, xla); }
  }

  private boolean jj_2_35(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_35(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(34, xla); }
  }

  private boolean jj_2_36(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_36(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(35, xla); }
  }

  private boolean jj_2_37(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_37(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(36, xla); }
  }

  private boolean jj_2_38(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_38(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(37, xla); }
  }

  private boolean jj_2_39(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_39(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(38, xla); }
  }

  private boolean jj_3_37() {
    if (jj_scan_token(STR)) return true;
    return false;
  }

  private boolean jj_3R_12() {
    if (jj_scan_token(LSBRACKET)) return true;
    if (jj_scan_token(STR)) return true;
    if (jj_scan_token(COMMA)) return true;
    if (jj_scan_token(STR)) return true;
    if (jj_scan_token(COMMA)) return true;
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3_37()) jj_scanpos = xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3_38()) { jj_scanpos = xsp; break; }
    }
    if (jj_scan_token(RSBRACKET)) return true;
    return false;
  }

  private boolean jj_3R_8() {
    if (jj_scan_token(XMLSCAN)) return true;
    if (jj_scan_token(LPAREN)) return true;
    if (jj_scan_token(STR)) return true;
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3_25()) jj_scanpos = xsp;
    if (jj_scan_token(COMMA)) return true;
    if (jj_scan_token(NAME)) return true;
    xsp = jj_scanpos;
    if (jj_3_26()) jj_scanpos = xsp;
    if (jj_scan_token(RPAREN)) return true;
    return false;
  }

  private boolean jj_3_18() {
    if (jj_scan_token(XMLCONSTRUCT)) return true;
    if (jj_scan_token(LPAREN)) return true;
    if (jj_3R_10()) return true;
    if (jj_scan_token(COMMA)) return true;
    if (jj_3R_12()) return true;
    return false;
  }

  private boolean jj_3_1() {
    if (jj_3R_6()) return true;
    return false;
  }

  private boolean jj_3R_10() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3_1()) {
    jj_scanpos = xsp;
    if (jj_3_2()) {
    jj_scanpos = xsp;
    if (jj_3_3()) return true;
    }
    }
    return false;
  }

  private boolean jj_3R_16() {
    if (jj_3R_9()) return true;
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3_29()) jj_scanpos = xsp;
    if (jj_scan_token(PREDCODE)) return true;
    xsp = jj_scanpos;
    if (jj_3_31()) {
    jj_scanpos = xsp;
    if (jj_3_32()) {
    jj_scanpos = xsp;
    if (jj_3_33()) return true;
    }
    }
    return false;
  }

  private boolean jj_3R_7() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3_18()) {
    jj_scanpos = xsp;
    if (jj_3_19()) {
    jj_scanpos = xsp;
    if (jj_3_20()) {
    jj_scanpos = xsp;
    if (jj_3_21()) {
    jj_scanpos = xsp;
    if (jj_3_22()) {
    jj_scanpos = xsp;
    if (jj_3_23()) {
    jj_scanpos = xsp;
    if (jj_3_24()) return true;
    }
    }
    }
    }
    }
    }
    return false;
  }

  private boolean jj_3_31() {
    if (jj_3R_9()) return true;
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3_30()) jj_scanpos = xsp;
    return false;
  }

  private boolean jj_3_33() {
    if (jj_scan_token(NUMBER)) return true;
    return false;
  }

  private boolean jj_3_24() {
    if (jj_scan_token(AGGREGATION)) return true;
    if (jj_scan_token(LPAREN)) return true;
    if (jj_3R_10()) return true;
    if (jj_scan_token(COMMA)) return true;
    if (jj_scan_token(AGGREGATIONTYPE)) return true;
    return false;
  }

  private boolean jj_3_36() {
    if (jj_scan_token(COMMA)) return true;
    if (jj_scan_token(NAME)) return true;
    return false;
  }

  private boolean jj_3R_14() {
    if (jj_scan_token(LPAREN)) return true;
    if (jj_scan_token(NAME)) return true;
    Token xsp;
    if (jj_3_36()) return true;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3_36()) { jj_scanpos = xsp; break; }
    }
    if (jj_scan_token(RPAREN)) return true;
    return false;
  }

  private boolean jj_3_28() {
    if (jj_scan_token(AND)) return true;
    if (jj_3R_16()) return true;
    return false;
  }

  private boolean jj_3R_15() {
    if (jj_scan_token(LPAREN)) return true;
    if (jj_3R_16()) return true;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3_28()) { jj_scanpos = xsp; break; }
    }
    if (jj_scan_token(RPAREN)) return true;
    return false;
  }

  private boolean jj_3R_9() {
    if (jj_scan_token(DOLLAR)) return true;
    if (jj_scan_token(NUMBER)) return true;
    return false;
  }

  private boolean jj_3_14() {
    if (jj_scan_token(GROUPBYWITHAGGREGATION)) return true;
    return false;
  }

  private boolean jj_3_23() {
    if (jj_scan_token(FLATTEN)) return true;
    if (jj_scan_token(LPAREN)) return true;
    if (jj_3R_10()) return true;
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3_16()) jj_scanpos = xsp;
    if (jj_scan_token(RPAREN)) return true;
    return false;
  }

  private boolean jj_3_5() {
    if (jj_scan_token(LEFTOUTERJOIN)) return true;
    return false;
  }

  private boolean jj_3_32() {
    if (jj_scan_token(STR)) return true;
    return false;
  }

  private boolean jj_3_27() {
    if (jj_scan_token(OR)) return true;
    if (jj_3R_15()) return true;
    return false;
  }

  private boolean jj_3_34() {
    if (jj_3R_9()) return true;
    return false;
  }

  private boolean jj_3R_13() {
    if (jj_scan_token(LSBRACKET)) return true;
    return false;
  }

  private boolean jj_3_3() {
    if (jj_3R_8()) return true;
    return false;
  }

  private boolean jj_3_8() {
    if (jj_scan_token(COMMA)) return true;
    if (jj_scan_token(AGGREGATIONTYPE)) return true;
    if (jj_scan_token(COMMA)) return true;
    if (jj_3R_9()) return true;
    if (jj_scan_token(COMMA)) return true;
    if (jj_scan_token(NAME)) return true;
    return false;
  }

  private boolean jj_3_7() {
    if (jj_scan_token(LEFTOUTERNESTEDJOINWITHAGGREGATION)) return true;
    return false;
  }

  private boolean jj_3_6() {
    if (jj_scan_token(LEFTOUTERNESTEDJOIN)) return true;
    return false;
  }

  private boolean jj_3_15() {
    if (jj_scan_token(COMMA)) return true;
    if (jj_scan_token(AGGREGATIONTYPE)) return true;
    if (jj_scan_token(COMMA)) return true;
    if (jj_3R_9()) return true;
    if (jj_scan_token(COMMA)) return true;
    if (jj_scan_token(NAME)) return true;
    return false;
  }

  private boolean jj_3_13() {
    if (jj_scan_token(GROUPBY)) return true;
    return false;
  }

  private boolean jj_3_25() {
    if (jj_scan_token(COMMA)) return true;
    if (jj_3R_14()) return true;
    return false;
  }

  private boolean jj_3_35() {
    if (jj_scan_token(COMMA)) return true;
    if (jj_3R_9()) return true;
    return false;
  }

  private boolean jj_3R_11() {
    if (jj_scan_token(LSBRACKET)) return true;
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3_34()) jj_scanpos = xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3_35()) { jj_scanpos = xsp; break; }
    }
    if (jj_scan_token(RSBRACKET)) return true;
    return false;
  }

  private boolean jj_3_4() {
    if (jj_scan_token(JOIN)) return true;
    return false;
  }

  private boolean jj_3_22() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3_13()) {
    jj_scanpos = xsp;
    if (jj_3_14()) return true;
    }
    if (jj_scan_token(LPAREN)) return true;
    if (jj_3R_10()) return true;
    if (jj_scan_token(COMMA)) return true;
    if (jj_3R_11()) return true;
    return false;
  }

  private boolean jj_3_12() {
    if (jj_scan_token(DUPLICATEELIMINATION)) return true;
    return false;
  }

  private boolean jj_3_10() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3_4()) {
    jj_scanpos = xsp;
    if (jj_3_5()) {
    jj_scanpos = xsp;
    if (jj_3_6()) {
    jj_scanpos = xsp;
    if (jj_3_7()) return true;
    }
    }
    }
    if (jj_scan_token(LPAREN)) return true;
    if (jj_3R_10()) return true;
    if (jj_scan_token(COMMA)) return true;
    if (jj_3R_10()) return true;
    return false;
  }

  private boolean jj_3_21() {
    if (jj_scan_token(NAVIGATION)) return true;
    if (jj_scan_token(LPAREN)) return true;
    if (jj_3R_10()) return true;
    if (jj_scan_token(COMMA)) return true;
    if (jj_3R_9()) return true;
    return false;
  }

  private boolean jj_3R_17() {
    if (jj_scan_token(ARITHCODE)) return true;
    if (jj_scan_token(NUMBER)) return true;
    return false;
  }

  private boolean jj_3_20() {
    if (jj_scan_token(SELECTION)) return true;
    if (jj_scan_token(LPAREN)) return true;
    if (jj_3R_10()) return true;
    if (jj_scan_token(COMMA)) return true;
    if (jj_3R_13()) return true;
    return false;
  }

  private boolean jj_3_2() {
    if (jj_3R_7()) return true;
    return false;
  }

  private boolean jj_3_9() {
    if (jj_scan_token(CARTESIANPRODUCT)) return true;
    if (jj_scan_token(LPAREN)) return true;
    if (jj_3R_10()) return true;
    if (jj_scan_token(COMMA)) return true;
    if (jj_3R_10()) return true;
    return false;
  }

  private boolean jj_3_16() {
    if (jj_scan_token(COMMA)) return true;
    if (jj_3R_11()) return true;
    return false;
  }

  private boolean jj_3_26() {
    if (jj_scan_token(COMMA)) return true;
    if (jj_scan_token(NAME)) return true;
    if (jj_scan_token(COMMA)) return true;
    if (jj_scan_token(TREEPATTERNSTRING)) return true;
    return false;
  }

  private boolean jj_3_11() {
    if (jj_scan_token(PROJECTION)) return true;
    return false;
  }

  private boolean jj_3R_6() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3_9()) {
    jj_scanpos = xsp;
    if (jj_3_10()) return true;
    }
    return false;
  }

  private boolean jj_3_29() {
    if (jj_3R_17()) return true;
    return false;
  }

  private boolean jj_3_30() {
    if (jj_3R_17()) return true;
    return false;
  }

  private boolean jj_3_19() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3_11()) {
    jj_scanpos = xsp;
    if (jj_3_12()) return true;
    }
    if (jj_scan_token(LPAREN)) return true;
    if (jj_3R_10()) return true;
    if (jj_scan_token(COMMA)) return true;
    if (jj_3R_11()) return true;
    return false;
  }

  private boolean jj_3_17() {
    if (jj_scan_token(COMMA)) return true;
    if (jj_3R_9()) return true;
    if (jj_scan_token(COMMA)) return true;
    if (jj_scan_token(NAME)) return true;
    return false;
  }

  private boolean jj_3_39() {
    if (jj_3R_12()) return true;
    return false;
  }

  private boolean jj_3_38() {
    if (jj_scan_token(LPAREN)) return true;
    if (jj_3R_9()) return true;
    if (jj_scan_token(RPAREN)) return true;
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3_39()) jj_scanpos = xsp;
    if (jj_scan_token(STR)) return true;
    return false;
  }

  /** Generated Token Manager. */
  public LogicalPlanParserTokenManager token_source;
  SimpleCharStream jj_input_stream;
  /** Current token. */
  public Token token;
  /** Next token. */
  public Token jj_nt;
  private int jj_ntk;
  private Token jj_scanpos, jj_lastpos;
  private int jj_la;
  private int jj_gen;
  final private int[] jj_la1 = new int[0];
  static private int[] jj_la1_0;
  static private int[] jj_la1_1;
  static {
      jj_la1_init_0();
      jj_la1_init_1();
   }
   private static void jj_la1_init_0() {
      jj_la1_0 = new int[] {};
   }
   private static void jj_la1_init_1() {
      jj_la1_1 = new int[] {};
   }
  final private JJCalls[] jj_2_rtns = new JJCalls[39];
  private boolean jj_rescan = false;
  private int jj_gc = 0;

  /** Constructor with InputStream. */
  public LogicalPlanParser(java.io.InputStream stream) {
     this(stream, null);
  }
  /** Constructor with InputStream and supplied encoding */
  public LogicalPlanParser(java.io.InputStream stream, String encoding) {
    try { jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source = new LogicalPlanParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 0; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Reinitialise. */
  public void ReInit(java.io.InputStream stream) {
     ReInit(stream, null);
  }
  /** Reinitialise. */
  public void ReInit(java.io.InputStream stream, String encoding) {
    try { jj_input_stream.ReInit(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 0; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Constructor. */
  public LogicalPlanParser(java.io.Reader stream) {
    jj_input_stream = new SimpleCharStream(stream, 1, 1);
    token_source = new LogicalPlanParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 0; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Reinitialise. */
  public void ReInit(java.io.Reader stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 0; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Constructor with generated Token Manager. */
  public LogicalPlanParser(LogicalPlanParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 0; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Reinitialise. */
  public void ReInit(LogicalPlanParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 0; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  private Token jj_consume_token(int kind) throws ParseException {
    Token oldToken;
    if ((oldToken = token).next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    if (token.kind == kind) {
      jj_gen++;
      if (++jj_gc > 100) {
        jj_gc = 0;
        for (int i = 0; i < jj_2_rtns.length; i++) {
          JJCalls c = jj_2_rtns[i];
          while (c != null) {
            if (c.gen < jj_gen) c.first = null;
            c = c.next;
          }
        }
      }
      return token;
    }
    token = oldToken;
    jj_kind = kind;
    throw generateParseException();
  }

  static private final class LookaheadSuccess extends java.lang.Error { }
  final private LookaheadSuccess jj_ls = new LookaheadSuccess();
  private boolean jj_scan_token(int kind) {
    if (jj_scanpos == jj_lastpos) {
      jj_la--;
      if (jj_scanpos.next == null) {
        jj_lastpos = jj_scanpos = jj_scanpos.next = token_source.getNextToken();
      } else {
        jj_lastpos = jj_scanpos = jj_scanpos.next;
      }
    } else {
      jj_scanpos = jj_scanpos.next;
    }
    if (jj_rescan) {
      int i = 0; Token tok = token;
      while (tok != null && tok != jj_scanpos) { i++; tok = tok.next; }
      if (tok != null) jj_add_error_token(kind, i);
    }
    if (jj_scanpos.kind != kind) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) throw jj_ls;
    return false;
  }


/** Get the next Token. */
  final public Token getNextToken() {
    if (token.next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    jj_gen++;
    return token;
  }

/** Get the specific Token. */
  final public Token getToken(int index) {
    Token t = token;
    for (int i = 0; i < index; i++) {
      if (t.next != null) t = t.next;
      else t = t.next = token_source.getNextToken();
    }
    return t;
  }

  private int jj_ntk() {
    if ((jj_nt=token.next) == null)
      return (jj_ntk = (token.next=token_source.getNextToken()).kind);
    else
      return (jj_ntk = jj_nt.kind);
  }

  private java.util.List<int[]> jj_expentries = new java.util.ArrayList<int[]>();
  private int[] jj_expentry;
  private int jj_kind = -1;
  private int[] jj_lasttokens = new int[100];
  private int jj_endpos;

  private void jj_add_error_token(int kind, int pos) {
    if (pos >= 100) return;
    if (pos == jj_endpos + 1) {
      jj_lasttokens[jj_endpos++] = kind;
    } else if (jj_endpos != 0) {
      jj_expentry = new int[jj_endpos];
      for (int i = 0; i < jj_endpos; i++) {
        jj_expentry[i] = jj_lasttokens[i];
      }
      jj_entries_loop: for (java.util.Iterator<?> it = jj_expentries.iterator(); it.hasNext();) {
        int[] oldentry = (int[])(it.next());
        if (oldentry.length == jj_expentry.length) {
          for (int i = 0; i < jj_expentry.length; i++) {
            if (oldentry[i] != jj_expentry[i]) {
              continue jj_entries_loop;
            }
          }
          jj_expentries.add(jj_expentry);
          break jj_entries_loop;
        }
      }
      if (pos != 0) jj_lasttokens[(jj_endpos = pos) - 1] = kind;
    }
  }

  /** Generate ParseException. */
  public ParseException generateParseException() {
    jj_expentries.clear();
    boolean[] la1tokens = new boolean[37];
    if (jj_kind >= 0) {
      la1tokens[jj_kind] = true;
      jj_kind = -1;
    }
    for (int i = 0; i < 0; i++) {
      if (jj_la1[i] == jj_gen) {
        for (int j = 0; j < 32; j++) {
          if ((jj_la1_0[i] & (1<<j)) != 0) {
            la1tokens[j] = true;
          }
          if ((jj_la1_1[i] & (1<<j)) != 0) {
            la1tokens[32+j] = true;
          }
        }
      }
    }
    for (int i = 0; i < 37; i++) {
      if (la1tokens[i]) {
        jj_expentry = new int[1];
        jj_expentry[0] = i;
        jj_expentries.add(jj_expentry);
      }
    }
    jj_endpos = 0;
    jj_rescan_token();
    jj_add_error_token(0, 0);
    int[][] exptokseq = new int[jj_expentries.size()][];
    for (int i = 0; i < jj_expentries.size(); i++) {
      exptokseq[i] = jj_expentries.get(i);
    }
    return new ParseException(token, exptokseq, tokenImage);
  }

  /** Enable tracing. */
  final public void enable_tracing() {
  }

  /** Disable tracing. */
  final public void disable_tracing() {
  }

  private void jj_rescan_token() {
    jj_rescan = true;
    for (int i = 0; i < 39; i++) {
    try {
      JJCalls p = jj_2_rtns[i];
      do {
        if (p.gen > jj_gen) {
          jj_la = p.arg; jj_lastpos = jj_scanpos = p.first;
          switch (i) {
            case 0: jj_3_1(); break;
            case 1: jj_3_2(); break;
            case 2: jj_3_3(); break;
            case 3: jj_3_4(); break;
            case 4: jj_3_5(); break;
            case 5: jj_3_6(); break;
            case 6: jj_3_7(); break;
            case 7: jj_3_8(); break;
            case 8: jj_3_9(); break;
            case 9: jj_3_10(); break;
            case 10: jj_3_11(); break;
            case 11: jj_3_12(); break;
            case 12: jj_3_13(); break;
            case 13: jj_3_14(); break;
            case 14: jj_3_15(); break;
            case 15: jj_3_16(); break;
            case 16: jj_3_17(); break;
            case 17: jj_3_18(); break;
            case 18: jj_3_19(); break;
            case 19: jj_3_20(); break;
            case 20: jj_3_21(); break;
            case 21: jj_3_22(); break;
            case 22: jj_3_23(); break;
            case 23: jj_3_24(); break;
            case 24: jj_3_25(); break;
            case 25: jj_3_26(); break;
            case 26: jj_3_27(); break;
            case 27: jj_3_28(); break;
            case 28: jj_3_29(); break;
            case 29: jj_3_30(); break;
            case 30: jj_3_31(); break;
            case 31: jj_3_32(); break;
            case 32: jj_3_33(); break;
            case 33: jj_3_34(); break;
            case 34: jj_3_35(); break;
            case 35: jj_3_36(); break;
            case 36: jj_3_37(); break;
            case 37: jj_3_38(); break;
            case 38: jj_3_39(); break;
          }
        }
        p = p.next;
      } while (p != null);
      } catch(LookaheadSuccess ls) { }
    }
    jj_rescan = false;
  }

  private void jj_save(int index, int xla) {
    JJCalls p = jj_2_rtns[index];
    while (p.gen > jj_gen) {
      if (p.next == null) { p = p.next = new JJCalls(); break; }
      p = p.next;
    }
    p.gen = jj_gen + xla - jj_la; p.first = token; p.arg = xla;
  }

  static final class JJCalls {
    int gen;
    Token first;
    int arg;
    JJCalls next;
  }

}
