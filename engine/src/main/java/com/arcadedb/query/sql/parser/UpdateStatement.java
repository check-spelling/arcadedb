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
/* Generated By:JJTree: Do not edit this line. OUpdateStatement.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=O,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_USERTYPE_VISIBILITY_PUBLIC=true */
package com.arcadedb.query.sql.parser;

import com.arcadedb.database.Database;
import com.arcadedb.query.sql.executor.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UpdateStatement extends Statement {
  protected FromClause             target;
  protected List<UpdateOperations> operations   = new ArrayList<UpdateOperations>();
  protected boolean                upsert       = false;
  protected boolean                returnBefore = false;
  protected boolean                returnAfter  = false;
  protected boolean                returnCount  = false;
  protected Projection             returnProjection;
  protected WhereClause            whereClause;

  public UpdateStatement(int id) {
    super(id);
  }

  public UpdateStatement(SqlParser p, int id) {
    super(p, id);
  }

  public void toString(Map<String, Object> params, StringBuilder builder) {
    builder.append(getStatementType());
    if (target != null) {
      target.toString(params, builder);
    }

    for (UpdateOperations ops : this.operations) {
      builder.append(" ");
      ops.toString(params, builder);
    }

    if (upsert) {
      builder.append(" UPSERT");
    }

    if (returnBefore || returnAfter || returnCount) {
      builder.append(" RETURN");
      if (returnBefore) {
        builder.append(" BEFORE");
      } else if (returnAfter) {
        builder.append(" AFTER");
      } else {
        builder.append(" COUNT");
      }
      if (returnProjection != null) {
        builder.append(" ");
        returnProjection.toString(params, builder);
      }
    }
    if (whereClause != null) {
      builder.append(" WHERE ");
      whereClause.toString(params, builder);
    }
    if (limit != null) {
      limit.toString(params, builder);
    }
    if (timeout != null) {
      timeout.toString(params, builder);
    }
  }

  protected String getStatementType() {
    return "UPDATE ";
  }

  @Override
  public UpdateStatement copy() {
    final UpdateStatement result = new UpdateStatement(-1);
    result.target = target == null ? null : target.copy();
    result.operations = operations == null ? null : operations.stream().map(x -> x.copy()).collect(Collectors.toList());
    result.upsert = upsert;
    result.returnBefore = returnBefore;
    result.returnAfter = returnAfter;
    result.returnProjection = returnProjection == null ? null : returnProjection.copy();
    result.whereClause = whereClause == null ? null : whereClause.copy();
    result.limit = limit == null ? null : limit.copy();
    result.timeout = timeout == null ? null : timeout.copy();
    return result;
  }

  @Override
  public ResultSet execute(final Database db, final Object[] args, final CommandContext parentCtx, final boolean usePlanCache) {
    final BasicCommandContext ctx = new BasicCommandContext();
    if (parentCtx != null) {
      ctx.setParentWithoutOverridingChild(parentCtx);
    }
    ctx.setDatabase(db);
    ctx.setInputParameters(args);
    final UpdateExecutionPlan executionPlan = createExecutionPlan(ctx, false);
    executionPlan.executeInternal();
    return new LocalResultSet(executionPlan);
  }

  @Override
  public ResultSet execute(final Database db, final Map params, final CommandContext parentCtx, final boolean usePlanCache) {
    final BasicCommandContext ctx = new BasicCommandContext();
    if (parentCtx != null) {
      ctx.setParentWithoutOverridingChild(parentCtx);
    }
    ctx.setDatabase(db);
    ctx.setInputParameters(params);
    UpdateExecutionPlan executionPlan = createExecutionPlan(ctx, false);
    executionPlan.executeInternal();
    return new LocalResultSet(executionPlan);
  }

  public UpdateExecutionPlan createExecutionPlan(final CommandContext ctx, final boolean enableProfiling) {
    final UpdateExecutionPlanner planner = new UpdateExecutionPlanner(this);
    return planner.createExecutionPlan(ctx, enableProfiling);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;

    final UpdateStatement that = (UpdateStatement) o;

    if (upsert != that.upsert)
      return false;
    if (returnBefore != that.returnBefore)
      return false;
    if (returnAfter != that.returnAfter)
      return false;
    if (target != null ? !target.equals(that.target) : that.target != null)
      return false;
    if (operations != null ? !operations.equals(that.operations) : that.operations != null)
      return false;
    if (returnProjection != null ? !returnProjection.equals(that.returnProjection) : that.returnProjection != null)
      return false;
    if (whereClause != null ? !whereClause.equals(that.whereClause) : that.whereClause != null)
      return false;
    if (limit != null ? !limit.equals(that.limit) : that.limit != null)
      return false;
    return timeout != null ? timeout.equals(that.timeout) : that.timeout == null;
  }

  @Override
  public int hashCode() {
    int result = target != null ? target.hashCode() : 0;
    result = 31 * result + (operations != null ? operations.hashCode() : 0);
    result = 31 * result + (upsert ? 1 : 0);
    result = 31 * result + (returnBefore ? 1 : 0);
    result = 31 * result + (returnAfter ? 1 : 0);
    result = 31 * result + (returnProjection != null ? returnProjection.hashCode() : 0);
    result = 31 * result + (whereClause != null ? whereClause.hashCode() : 0);
    result = 31 * result + (limit != null ? limit.hashCode() : 0);
    result = 31 * result + (timeout != null ? timeout.hashCode() : 0);
    return result;
  }

  public FromClause getTarget() {
    return target;
  }

  public List<UpdateOperations> getOperations() {
    return operations;
  }

  public boolean isUpsert() {
    return upsert;
  }

  public boolean isReturnBefore() {
    return returnBefore;
  }

  public boolean isReturnAfter() {
    return returnAfter;
  }

  public boolean isReturnCount() {
    return returnCount;
  }

  public Projection getReturnProjection() {
    return returnProjection;
  }

  public WhereClause getWhereClause() {
    return whereClause;
  }
}
/* JavaCC - OriginalChecksum=093091d7273f1073ad49f2a2bf709a53 (do not edit this line) */
