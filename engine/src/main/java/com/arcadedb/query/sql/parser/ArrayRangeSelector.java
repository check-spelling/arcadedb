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
/* Generated By:JJTree: Do not edit this line. OArrayRangeSelector.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=O,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_USERTYPE_VISIBILITY_PUBLIC=true */
package com.arcadedb.query.sql.parser;

import com.arcadedb.database.Identifiable;
import com.arcadedb.exception.CommandExecutionException;
import com.arcadedb.query.sql.executor.CommandContext;
import com.arcadedb.query.sql.executor.MultiValue;
import com.arcadedb.query.sql.executor.Result;
import com.arcadedb.query.sql.executor.ResultInternal;

import java.lang.reflect.Array;
import java.util.*;

public class ArrayRangeSelector extends SimpleNode {
  protected Integer from;
  protected Integer to;
  protected boolean newRange = false;
  protected boolean included = false;

  protected ArrayNumberSelector fromSelector;
  protected ArrayNumberSelector toSelector;

  public ArrayRangeSelector(int id) {
    super(id);
  }

  public ArrayRangeSelector(SqlParser p, int id) {
    super(p, id);
  }

  public void toString(Map<String, Object> params, StringBuilder builder) {
    if (from != null) {
      builder.append(from);
    } else {
      fromSelector.toString(params, builder);
    }
    if (newRange) {
      builder.append("..");
      if (included) {
        builder.append('.');
      }
    } else {
      builder.append("-");
    }
    if (to != null) {
      builder.append(to);
    } else {
      toSelector.toString(params, builder);
    }
  }

  public Object execute(Identifiable iCurrentRecord, Object result, CommandContext ctx) {
    if (result == null) {
      return null;
    }
    if (!MultiValue.isMultiValue(result)) {
      return null;
    }
    Integer lFrom = from;
    if (fromSelector != null) {
      lFrom = fromSelector.getValue(iCurrentRecord, result, ctx);
    }
    if (lFrom == null) {
      lFrom = 0;
    }
    Integer lTo = to;
    if (toSelector != null) {
      lTo = toSelector.getValue(iCurrentRecord, result, ctx);
    }
    if (included) {
      lTo++;
    }
    if (lFrom > lTo) {
      return null;
    }
    Object[] arrayResult = MultiValue.array(result);

    if (arrayResult == null || arrayResult.length == 0) {
      return arrayResult;
    }
    lFrom = Math.max(lFrom, 0);
    if (arrayResult.length < lFrom) {
      return null;
    }
    lFrom = Math.min(lFrom, arrayResult.length - 1);

    lTo = Math.min(lTo, arrayResult.length);

    return Arrays.asList(Arrays.copyOfRange(arrayResult, lFrom, lTo));
  }

  public Object execute(Result iCurrentRecord, Object result, CommandContext ctx) {
    if (result == null) {
      return null;
    }
    if (!MultiValue.isMultiValue(result)) {
      return null;
    }
    Integer lFrom = from;
    if (fromSelector != null) {
      lFrom = fromSelector.getValue(iCurrentRecord, result, ctx);
    }
    if (lFrom == null) {
      lFrom = 0;
    }
    Integer lTo = to;
    if (toSelector != null) {
      lTo = toSelector.getValue(iCurrentRecord, result, ctx);
    }
    if (included) {
      lTo++;
    }
    if (lFrom > lTo) {
      return null;
    }
    Object[] arrayResult = MultiValue.array(result);

    if (arrayResult == null || arrayResult.length == 0) {
      return arrayResult;
    }
    lFrom = Math.max(lFrom, 0);
    if (arrayResult.length < lFrom) {
      return null;
    }
    lFrom = Math.min(lFrom, arrayResult.length - 1);

    lTo = Math.min(lTo, arrayResult.length);

    return Arrays.asList(Arrays.copyOfRange(arrayResult, lFrom, lTo));
  }

  public boolean needsAliases(Set<String> aliases) {
    if (fromSelector != null && fromSelector.needsAliases(aliases)) {
      return true;
    }
    return toSelector != null && toSelector.needsAliases(aliases);
  }

  public ArrayRangeSelector copy() {
    ArrayRangeSelector result = new ArrayRangeSelector(-1);
    result.from = from;
    result.to = to;
    result.newRange = newRange;
    result.included = included;

    result.fromSelector = fromSelector == null ? null : fromSelector.copy();
    result.toSelector = toSelector == null ? null : toSelector.copy();

    return result;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;

    ArrayRangeSelector that = (ArrayRangeSelector) o;

    if (newRange != that.newRange)
      return false;
    if (included != that.included)
      return false;
    if (from != null ? !from.equals(that.from) : that.from != null)
      return false;
    if (to != null ? !to.equals(that.to) : that.to != null)
      return false;
    if (fromSelector != null ? !fromSelector.equals(that.fromSelector) : that.fromSelector != null)
      return false;
    return toSelector != null ? toSelector.equals(that.toSelector) : that.toSelector == null;
  }

  @Override
  public int hashCode() {
    int result = from != null ? from.hashCode() : 0;
    result = 31 * result + (to != null ? to.hashCode() : 0);
    result = 31 * result + (newRange ? 1 : 0);
    result = 31 * result + (included ? 1 : 0);
    result = 31 * result + (fromSelector != null ? fromSelector.hashCode() : 0);
    result = 31 * result + (toSelector != null ? toSelector.hashCode() : 0);
    return result;
  }

  public void extractSubQueries(SubQueryCollector collector) {
    if (fromSelector != null) {
      fromSelector.extractSubQueries(collector);
    }
    if (toSelector != null) {
      toSelector.extractSubQueries(collector);
    }
  }

  public boolean refersToParent() {
    if (fromSelector != null && fromSelector.refersToParent()) {
      return true;
    }
    return toSelector != null && toSelector.refersToParent();
  }

  public void setValue(Object target, Object value, CommandContext ctx) {
    if (target == null) {
      return;
    }
    if (target.getClass().isArray()) {
      setArrayValue(target, value, ctx);
    } else if (target instanceof List) {
      setValue((List) target, value, ctx);
    } else if (MultiValue.isMultiValue(value)) {
      //TODO
    }
    //TODO

  }

  public void setValue(List target, Object value, CommandContext ctx) {
    int from = this.from == null ? 0 : this.from;
    int to = target.size() - 1;
    if (this.to != null) {
      to = this.to;
      if (!included) {
        to--;
      }
    }
    if (from > to) {
      target.clear();
      return;
    }
    for (int i = 0; i <= to; i++) {
      if (i < from && target.size() - 1 < i) {
        target.set(i, null);
      } else if (i >= from) {
        target.set(i, value);
      }
      //else leave untouched the existing element
    }
  }

  public void setValue(Set target, Object value, CommandContext ctx) {
    Set result = new LinkedHashSet<>();
    int from = this.from == null ? 0 : this.from;
    int to = target.size() - 1;
    if (this.to != null) {
      to = this.to;
      if (!included) {
        to--;
      }
    }
    if (from > to) {
      target.clear();
      return;
    }
    Iterator targetIterator = target.iterator();
    for (int i = 0; i <= to; i++) {
      Object next = null;
      if (targetIterator.hasNext()) {
        next = targetIterator.next();
      }
      if (i < from && target.size() - 1 < i) {
        result.add(null);
      } else if (i >= from) {
        result.add(value);
      } else {
        result.add(next);
      }
      target.clear();
      target.addAll(result);
    }
  }

  public void setValue(Map target, Object value, CommandContext ctx) {
    int from = this.from == null ? 0 : this.from;
    int to = this.to;
    if (!included) {
      to--;
    }
    if (from > to) {
      target.clear();
      return;
    }
    for (int i = from; i <= to; i++) {
      target.put(i, value);
    }
  }

  private void setArrayValue(Object target, Object value, CommandContext ctx) {

    int from = this.from == null ? 0 : this.from;
    int to = Array.getLength(target) - 1;
    if (this.to != null) {
      to = this.to;
      if (!included) {
        to--;
      }
    }
    if (from > to || from >= Array.getLength(target)) {
      return;
    }
    to = Math.min(to, Array.getLength(target) - 1);
    for (int i = from; i <= to; i++) {
      Array.set(target, i, value);//TODO type conversion?
    }
  }

  public void applyRemove(Object currentValue, ResultInternal originalRecord, CommandContext ctx) {
    if (currentValue == null) {
      return;
    }
    Integer from = this.from;
    if (fromSelector != null) {
      from = fromSelector.getValue(originalRecord, null, ctx);
    }
    Integer to = this.to;
    if (toSelector != null) {
      to = toSelector.getValue(originalRecord, null, ctx);
    }
    if (from == null || to == null) {
      throw new CommandExecutionException("Invalid range expression: " + this + " one of the elements is null");
    }
    if (included) {
      to++;
    }
    if (from < 0) {
      from = 0;
    }
    if (from >= to) {
      return;
    }
    int range = to - from;
    if (currentValue instanceof List) {
      List list = (List) currentValue;
      for (int i = 0; i < range; i++) {
        if (list.size() > from) {
          list.remove(from);
        } else {
          break;
        }
      }
    } else if (currentValue instanceof Set) {
      Iterator iter = ((Set) currentValue).iterator();
      int count = 0;
      while (iter.hasNext()) {
        iter.next();
        if (count >= from) {
          if (count < to) {
            iter.remove();
          } else {
            break;
          }
        }
        count++;
      }
    } else {
      throw new CommandExecutionException("Trying to remove elements from " + currentValue + " (" + currentValue.getClass().getSimpleName() + ")");
    }
  }

  public Result serialize() {
    ResultInternal result = new ResultInternal();
    result.setProperty("from", from);
    result.setProperty("to", to);
    result.setProperty("newRange", newRange);
    result.setProperty("included", included);

    if (fromSelector != null) {
      result.setProperty("fromSelector", fromSelector.serialize());
    }
    if (toSelector != null) {
      result.setProperty("toSelector", toSelector.serialize());
    }
    return result;
  }

  public void deserialize(Result fromResult) {
    from = fromResult.getProperty("from");
    to = fromResult.getProperty("to");
    newRange = fromResult.getProperty("newRange");
    included = fromResult.getProperty("included");

    if (fromResult.getProperty("fromSelector") != null) {
      fromSelector = new ArrayNumberSelector(-1);
      fromSelector.deserialize(fromResult.getProperty("fromSelector"));
    }
    if (fromResult.getProperty("toSelector") != null) {
      toSelector = new ArrayNumberSelector(-1);
      toSelector.deserialize(fromResult.getProperty("toSelector"));
    }
  }
}
/* JavaCC - OriginalChecksum=594a372e31fcbcd3ed962c2260e76468 (do not edit this line) */
