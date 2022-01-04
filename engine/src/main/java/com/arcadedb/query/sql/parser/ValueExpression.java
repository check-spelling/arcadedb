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
/* Generated By:JJTree: Do not edit this line. OExpression.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=O,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_USERTYPE_VISIBILITY_PUBLIC=true */
package com.arcadedb.query.sql.parser;

import com.arcadedb.database.Identifiable;
import com.arcadedb.database.Record;
import com.arcadedb.exception.CommandExecutionException;
import com.arcadedb.query.sql.executor.AggregationContext;
import com.arcadedb.query.sql.executor.CommandContext;
import com.arcadedb.query.sql.executor.Result;
import com.arcadedb.query.sql.executor.ResultInternal;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * this class is only used by the query executor to store pre-calculated values and store them in a temporary AST. It's not produced
 * by parsing
 */
public class ValueExpression extends Expression {

  public ValueExpression(Object val) {
    super(-1);
    this.value = val;
  }

  public Object execute(Identifiable iCurrentRecord, CommandContext ctx) {
    return value;
  }

  public Object execute(Result iCurrentRecord, CommandContext ctx) {
    return value;
  }

  public boolean isBaseIdentifier() {
    return false;
  }

  public boolean isEarlyCalculated() {
    return true;
  }

  public Identifier getDefaultAlias() {
    return new Identifier(String.valueOf(value));
  }

  public void toString(Map<String, Object> params, StringBuilder builder) {
    builder.append(value);
  }

  public boolean supportsBasicCalculation() {
    return true;
  }

  public boolean isIndexedFunctionCal() {
    return false;
  }

  public boolean canExecuteIndexedFunctionWithoutIndex(FromClause target, CommandContext context, BinaryCompareOperator operator, Object right) {
    return false;
  }

  public boolean allowsIndexedFunctionExecutionOnTarget(FromClause target, CommandContext context, BinaryCompareOperator operator, Object right) {
    return false;
  }

  public boolean executeIndexedFunctionAfterIndexSearch(FromClause target, CommandContext context, BinaryCompareOperator operator, Object right) {
    return false;
  }

  public boolean isExpand() {
    return false;
  }

  public ValueExpression getExpandContent() {
    return null;
  }

  public boolean needsAliases(Set<String> aliases) {
    return false;
  }

  public boolean isAggregate() {
    return false;
  }

  public ValueExpression splitForAggregation(AggregateProjectionSplit aggregateSplit) {
    return this;
  }

  public AggregationContext getAggregationContext(CommandContext ctx) {
    throw new CommandExecutionException("Cannot aggregate on " + this);
  }

  public ValueExpression copy() {
    ValueExpression result = new ValueExpression(-1);
    result.value = value;
    return result;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;

    ValueExpression that = (ValueExpression) o;
    return that.value == this.value;
  }

  @Override
  public int hashCode() {
    return 1;
  }

  public void extractSubQueries(SubQueryCollector collector) {
  }

  public void extractSubQueries(Identifier letAlias, SubQueryCollector collector) {
  }

  public boolean refersToParent() {
    return false;
  }

  List<String> getMatchPatternInvolvedAliases() {
    return null;
  }

  public void applyRemove(ResultInternal result, CommandContext ctx) {
    throw new CommandExecutionException("Cannot apply REMOVE " + this);
  }

  public boolean isCount() {
    return false;
  }

  public Result serialize() {
    throw new UnsupportedOperationException("Cannot serialize value expression (not supported yet)");
  }

  public void deserialize(Result fromResult) {
    throw new UnsupportedOperationException("Cannot deserialize value expression (not supported yet)");
  }

  public boolean isDefinedFor(Result currentRecord) {
    return true;
  }

  public boolean isDefinedFor(Record currentRecord) {
    return true;

  }

  public boolean isCacheable() {
    return true;
  }
}
/* JavaCC - OriginalChecksum=9c860224b121acdc89522ae97010be01 (do not edit this line) */
