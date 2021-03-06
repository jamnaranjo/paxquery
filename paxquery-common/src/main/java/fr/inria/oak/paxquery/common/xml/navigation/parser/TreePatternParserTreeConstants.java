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
package fr.inria.oak.paxquery.common.xml.navigation.parser;

public interface TreePatternParserTreeConstants
{
  public int JJTSTART = 0;
  public int JJTVOID = 1;
  public int JJTTREEPATTERNSPEC = 2;
  public int JJTROOT = 3;
  public int JJTTREEPATTERNORDERED = 4;
  public int JJTDEFAULTNAMESPACE = 5;
  public int JJTNSPEC = 6;
  public int JJTNE = 7;
  public int JJTNA = 8;
  public int JJTSTRUCTURAL = 9;
  public int JJTORDERED = 10;
  public int JJTINTEGER = 11;
  public int JJTNAVIGATING = 12;
  public int JJTUPDATING = 13;
  public int JJTREQUIRED = 14;
  public int JJTIDSPEC = 15;
  public int JJTTAGRESTRICTION = 16;
  public int JJTTAGFULL = 17;
  public int JJTTAG = 18;
  public int JJTVALRESTRICTION = 19;
  public int JJTVALFULL = 20;
  public int JJTVAL = 21;
  public int JJTCSPEC = 22;
  public int JJTPREDCODE = 23;
  public int JJTCONTENT = 24;
  public int JJTMYID = 25;
  public int JJTDESCENDANT = 26;
  public int JJTCHILD = 27;
  public int JJTNESTED = 28;
  public int JJTOUTER = 29;
  public int JJTJOIN = 30;
  public int JJTSEMI = 31;
  public int JJTEDGESPEC = 32;


  public String[] jjtNodeName = {
    "Start",
    "void",
    "TreePatternSpec",
    "ROOT",
    "TreePatternOrdered",
    "DefaultNamespace",
    "NSPEC",
    "NE",
    "NA",
    "Structural",
    "Ordered",
    "Integer",
    "Navigating",
    "Updating",
    "Required",
    "IDSpec",
    "TagRestriction",
    "TagFull",
    "Tag",
    "ValRestriction",
    "ValFull",
    "Val",
    "CSpec",
    "PredCode",
    "Content",
    "MyID",
    "Descendant",
    "Child",
    "Nested",
    "Outer",
    "Join",
    "Semi",
    "EdgeSpec",
  };
}
/* JavaCC - OriginalChecksum=4d62afbce569e7481c0115d1aa65d815 (do not edit this line) */
