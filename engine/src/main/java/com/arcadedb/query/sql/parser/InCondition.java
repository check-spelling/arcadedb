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
/* Generated By:JJTree: Do not edit this line. OInCondition.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=O,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_USERTYPE_VISIBILITY_PUBLIC=true */
package com.arcadedb.query.sql.parser;

import com.arcadedb.database.Identifiable;
import com.arcadedb.query.sql.executor.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class InCondition extends BooleanExpression {
  protected Expression            left;
  protected BinaryCompareOperator operator;
  protected SelectStatement       rightStatement;
  protected InputParameter        rightParam;
  protected MathExpression        rightMathExpression;
  protected Object                right;

  private static final Object UNSET           = new Object();
  private final        Object inputFinalValue = UNSET;

  public InCondition(int id) {
    super(id);
  }

  public InCondition(SqlParser p, int id) {
    super(p, id);
  }

  @Override
  public boolean evaluate(Identifiable currentRecord, CommandContext ctx) {
    Object leftVal = evaluateLeft(currentRecord, ctx);
    Object rightVal = evaluateRight(currentRecord, ctx);
    if (rightVal == null) {
      return false;
    }
    return evaluateExpression(leftVal, rightVal);
  }

  public Object evaluateRight(Identifiable currentRecord, CommandContext ctx) {
    Object rightVal = null;
    if (rightStatement != null) {
      rightVal = executeQuery(rightStatement, ctx);
    } else if (rightParam != null) {
      rightVal = rightParam.getValue(ctx.getInputParameters());
    } else if (rightMathExpression != null) {
      rightVal = rightMathExpression.execute(currentRecord, ctx);
    }
    return rightVal;
  }

  public Object evaluateLeft(Identifiable currentRecord, CommandContext ctx) {
    return left.execute(currentRecord, ctx);
  }

  @Override
  public boolean evaluate(Result currentRecord, CommandContext ctx) {
    Object leftVal = evaluateLeft(currentRecord, ctx);
    Object rightVal = evaluateRight(currentRecord, ctx);
    if (rightVal == null) {
      return false;
    }
    return evaluateExpression(leftVal, rightVal);
  }

  public Object evaluateRight(Result currentRecord, CommandContext ctx) {
    Object rightVal = null;
    if (rightStatement != null) {
      rightVal = executeQuery(rightStatement, ctx);
    } else if (rightParam != null) {
      rightVal = rightParam.getValue(ctx.getInputParameters());
    } else if (rightMathExpression != null) {
      rightVal = rightMathExpression.execute(currentRecord, ctx);
    }
    return rightVal;
  }

  public Object evaluateLeft(Result currentRecord, CommandContext ctx) {
    return left.execute(currentRecord, ctx);
  }

  protected static Object executeQuery(SelectStatement rightStatement, CommandContext ctx) {
    BasicCommandContext subCtx = new BasicCommandContext();
    subCtx.setParentWithoutOverridingChild(ctx);
    ResultSet result = rightStatement.execute(ctx.getDatabase(), ctx.getInputParameters());
    return result.stream().collect(Collectors.toSet());
  }

  protected static boolean evaluateExpression(final Object iLeft, final Object iRight) {
    if (MultiValue.isMultiValue(iRight)) {
      if (iRight instanceof Set<?>)
        return ((Set) iRight).contains(iLeft);

      for (final Object o : MultiValue.getMultiValueIterable(iRight, false)) {
        if (QueryOperatorEquals.equals(iLeft, o))
          return true;
        if (MultiValue.isMultiValue(iLeft) && MultiValue.getSize(iLeft) == 1) {

          Object item = MultiValue.getFirstValue(iLeft);
          if (item instanceof Result && ((Result) item).getPropertyNames().size() == 1) {
            Object propValue = ((Result) item).getProperty(((Result) item).getPropertyNames().iterator().next());
            if (QueryOperatorEquals.equals(propValue, o))
              return true;
          }
        }

      }
    } else if (iRight.getClass().isArray()) {
      for (final Object o : (Object[]) iRight) {
        if (QueryOperatorEquals.equals(iLeft, o))
          return true;
      }
    } else if (iRight instanceof ResultSet) {
      ResultSet rsRight = (ResultSet) iRight;
      rsRight.reset();
      while (((ResultSet) iRight).hasNext()) {
        if (QueryOperatorEquals.equals(iLeft, rsRight.next())) {
          return true;
        }
      }
    }
    return false;
  }

  public void toString(Map<String, Object> params, StringBuilder builder) {
    left.toString(params, builder);
    builder.append(" IN ");
    if (rightStatement != null) {
      builder.append("(");
      rightStatement.toString(params, builder);
      builder.append(")");
    } else if (right != null) {
      builder.append(convertToString(right));
    } else if (rightParam != null) {
      rightParam.toString(params, builder);
    } else if (rightMathExpression != null) {
      rightMathExpression.toString(params, builder);
    }
  }

  private String convertToString(Object o) {
    if (o instanceof String) {
      return "\"" + ((String) o).replaceAll("\"", "\\\"") + "\"";
    }
    return o.toString();
  }

  @Override
  public boolean supportsBasicCalculation() {
    if (!left.supportsBasicCalculation()) {
      return false;
    }
    if (!rightMathExpression.supportsBasicCalculation()) {
      return false;
    }
    return operator.supportsBasicCalculation();
  }

  @Override
  protected int getNumberOfExternalCalculations() {
    int total = 0;
    if (operator != null && !operator.supportsBasicCalculation()) {
      total++;
    }
    if (!left.supportsBasicCalculation()) {
      total++;
    }
    if (rightMathExpression != null && !rightMathExpression.supportsBasicCalculation()) {
      total++;
    }
    return total;
  }

  @Override
  protected List<Object> getExternalCalculationConditions() {
    List<Object> result = new ArrayList<Object>();

    if (operator != null) {
      result.add(this);
    }
    if (!left.supportsBasicCalculation()) {
      result.add(left);
    }
    if (rightMathExpression != null && !rightMathExpression.supportsBasicCalculation()) {
      result.add(rightMathExpression);
    }
    return result;
  }

  @Override
  public boolean needsAliases(Set<String> aliases) {
    if (left.needsAliases(aliases)) {
      return true;
    }

    return rightMathExpression != null && rightMathExpression.needsAliases(aliases);
  }

  @Override
  public InCondition copy() {
    InCondition result = new InCondition(-1);
    result.operator = operator == null ? null : operator.copy();
    result.left = left == null ? null : left.copy();
    result.rightMathExpression = rightMathExpression == null ? null : rightMathExpression.copy();
    result.rightStatement = rightStatement == null ? null : rightStatement.copy();
    result.rightParam = rightParam == null ? null : rightParam.copy();
    result.right = right;
    return result;
  }

  @Override
  public void extractSubQueries(SubQueryCollector collector) {
    if (left != null) {
      left.extractSubQueries(collector);
    }
    if (rightMathExpression != null) {
      rightMathExpression.extractSubQueries(collector);
    }
    if (rightStatement != null) {
      Identifier alias = collector.addStatement(rightStatement);
      rightMathExpression = new BaseExpression(alias);
      rightStatement = null;
    }
  }

  @Override
  public boolean refersToParent() {
    if (left != null && left.refersToParent()) {
      return true;
    }
    if (rightStatement != null && rightStatement.refersToParent()) {
      return true;
    }
    return rightMathExpression != null && rightMathExpression.refersToParent();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;

    InCondition that = (InCondition) o;

    if (left != null ? !left.equals(that.left) : that.left != null)
      return false;
    if (operator != null ? !operator.equals(that.operator) : that.operator != null)
      return false;
    if (rightStatement != null ? !rightStatement.equals(that.rightStatement) : that.rightStatement != null)
      return false;
    if (rightParam != null ? !rightParam.equals(that.rightParam) : that.rightParam != null)
      return false;
    if (rightMathExpression != null ? !rightMathExpression.equals(that.rightMathExpression) : that.rightMathExpression != null)
      return false;
    if (right != null ? !right.equals(that.right) : that.right != null)
      return false;
    return inputFinalValue != null ? inputFinalValue.equals(that.inputFinalValue) : that.inputFinalValue == null;
  }

  @Override
  public int hashCode() {
    int result = left != null ? left.hashCode() : 0;
    result = 31 * result + (operator != null ? operator.hashCode() : 0);
    result = 31 * result + (rightStatement != null ? rightStatement.hashCode() : 0);
    result = 31 * result + (rightParam != null ? rightParam.hashCode() : 0);
    result = 31 * result + (rightMathExpression != null ? rightMathExpression.hashCode() : 0);
    result = 31 * result + (right != null ? right.hashCode() : 0);
    result = 31 * result + (inputFinalValue != null ? inputFinalValue.hashCode() : 0);
    return result;
  }

  @Override
  public List<String> getMatchPatternInvolvedAliases() {
    List<String> leftX = left == null ? null : left.getMatchPatternInvolvedAliases();

    List<String> conditionX = rightMathExpression == null ? null : rightMathExpression.getMatchPatternInvolvedAliases();

    List<String> result = new ArrayList<String>();
    if (leftX != null) {
      result.addAll(leftX);
    }
    if (conditionX != null) {
      result.addAll(conditionX);
    }

    return result.isEmpty() ? null : result;
  }

  @Override
  public boolean isCacheable() {
    if (left != null && !left.isCacheable()) {
      return false;
    }
    if (rightStatement != null && !rightStatement.executionPlanCanBeCached()) {
      return false;
    }
    return rightMathExpression == null || rightMathExpression.isCacheable();
  }

  public Expression getLeft() {
    return left;
  }

  public void setLeft(Expression left) {
    this.left = left;
  }

  public SelectStatement getRightStatement() {
    return rightStatement;
  }

  public InputParameter getRightParam() {
    return rightParam;
  }

  public MathExpression getRightMathExpression() {
    return rightMathExpression;
  }

  public void setRightParam(InputParameter rightParam) {
    this.rightParam = rightParam;
  }

  public void setRightMathExpression(MathExpression rightMathExpression) {
    this.rightMathExpression = rightMathExpression;
  }
}
/* JavaCC - OriginalChecksum=00df7cb1877c0a12d24205c1700653c7 (do not edit this line) */
