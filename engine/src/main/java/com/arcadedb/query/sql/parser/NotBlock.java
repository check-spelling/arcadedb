/*
 * Copyright © 2021-present Arcade Data Ltd (info@arcadedata.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/* Generated By:JJTree: Do not edit this line. ONotBlock.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=O,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_USERTYPE_VISIBILITY_PUBLIC=true */
package com.arcadedb.query.sql.parser;

import com.arcadedb.database.Database;
import com.arcadedb.database.Identifiable;
import com.arcadedb.query.sql.executor.CommandContext;
import com.arcadedb.query.sql.executor.Result;
import com.arcadedb.schema.DocumentType;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class NotBlock extends BooleanExpression {
  protected BooleanExpression sub;

  protected boolean negate = false;

  public NotBlock(int id) {
    super(id);
  }

  public NotBlock(SqlParser p, int id) {
    super(p, id);
  }

  @Override
  public boolean evaluate(Identifiable currentRecord, CommandContext ctx) {
    if (sub == null) {
      return true;
    }
    boolean result = sub.evaluate(currentRecord, ctx);
    if (negate) {
      return !result;
    }
    return result;
  }

  @Override
  public boolean evaluate(Result currentRecord, CommandContext ctx) {
    if (sub == null) {
      return true;
    }
    boolean result = sub.evaluate(currentRecord, ctx);
    if (negate) {
      return !result;
    }
    return result;
  }

  public BooleanExpression getSub() {
    return sub;
  }

  public void setSub(BooleanExpression sub) {
    this.sub = sub;
  }

  public boolean isNegate() {
    return negate;
  }

  public void setNegate(boolean negate) {
    this.negate = negate;
  }

  public void toString(Map<String, Object> params, StringBuilder builder) {
    if (negate) {
      builder.append("NOT ");
    }
    sub.toString(params, builder);
  }

  @Override
  public boolean supportsBasicCalculation() {
    return true;
  }

  @Override
  protected int getNumberOfExternalCalculations() {
    return sub.getNumberOfExternalCalculations();
  }

  @Override
  protected List<Object> getExternalCalculationConditions() {
    return sub.getExternalCalculationConditions();
  }

  public List<BinaryCondition> getIndexedFunctionConditions(DocumentType iSchemaClass, Database database) {
    if (sub == null) {
      return null;
    }
    if (negate) {
      return null;
    }
    return sub.getIndexedFunctionConditions(iSchemaClass, database);
  }

  @Override
  public List<AndBlock> flatten() {
    if (!negate) {
      return sub.flatten();
    }
    return super.flatten();
  }

  @Override
  public boolean needsAliases(Set<String> aliases) {
    return sub.needsAliases(aliases);
  }

  @Override
  public NotBlock copy() {
    NotBlock result = new NotBlock(-1);
    result.sub = sub.copy();
    result.negate = negate;
    return result;
  }

  @Override
  public void extractSubQueries(SubQueryCollector collector) {
    sub.extractSubQueries(collector);
  }

  @Override
  public boolean refersToParent() {
    return sub.refersToParent();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;

    NotBlock oNotBlock = (NotBlock) o;

    if (negate != oNotBlock.negate)
      return false;
    return sub != null ? sub.equals(oNotBlock.sub) : oNotBlock.sub == null;
  }

  @Override
  public int hashCode() {
    int result = sub != null ? sub.hashCode() : 0;
    result = 31 * result + (negate ? 1 : 0);
    return result;
  }

  @Override
  public List<String> getMatchPatternInvolvedAliases() {
    return sub.getMatchPatternInvolvedAliases();
  }

  @Override
  public boolean isCacheable() {
    return sub.isCacheable();
  }
}
/* JavaCC - OriginalChecksum=1926313b3f854235aaa20811c22d583b (do not edit this line) */
