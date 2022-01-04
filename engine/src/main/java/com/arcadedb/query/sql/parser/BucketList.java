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
/* Generated By:JJTree: Do not edit this line. OClusterList.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=O,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_USERTYPE_VISIBILITY_PUBLIC=true */
package com.arcadedb.query.sql.parser;

import com.arcadedb.query.sql.executor.Result;
import com.arcadedb.query.sql.executor.ResultInternal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BucketList extends SimpleNode {

  protected List<Identifier> buckets = new ArrayList<>();

  public BucketList(int id) {
    super(id);
  }

  public BucketList(SqlParser p, int id) {
    super(p, id);
  }

  public void toString(Map<String, Object> params, StringBuilder builder) {

    builder.append("bucket:[");
    boolean first = true;
    for (Identifier id : buckets) {
      if (!first) {
        builder.append(",");
      }
      id.toString(params, builder);
      first = false;
    }
    builder.append("]");
  }

  public List<Bucket> toListOfClusters() {
    List<Bucket> result = new ArrayList<>();
    for (Identifier id : buckets) {
      Bucket bucket = new Bucket(-1);
      bucket.bucketName = id.getStringValue();
      result.add(bucket);
    }
    return result;
  }

  public BucketList copy() {
    BucketList result = new BucketList(-1);
    result.buckets = buckets.stream().map(x -> x.copy()).collect(Collectors.toList());
    return result;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;

    BucketList that = (BucketList) o;

    return buckets != null ? buckets.equals(that.buckets) : that.buckets == null;
  }

  @Override
  public int hashCode() {
    return buckets != null ? buckets.hashCode() : 0;
  }

  public Result serialize() {
    ResultInternal result = new ResultInternal();
    if (buckets != null) {
      result.setProperty("buckets", buckets.stream().map(x -> x.serialize()).collect(Collectors.toList()));
    }
    return result;
  }

  public void deserialize(Result fromResult) {
    if (fromResult.getProperty("buckets") != null) {
      buckets = new ArrayList<>();
      List<Result> ser = fromResult.getProperty("buckets");
      for (Result item : ser) {
        Identifier id = new Identifier(-1);
        Identifier.deserialize(item);
        buckets.add(id);
      }
    }
  }
}
/* JavaCC - OriginalChecksum=bd90ffa0b9d17f204b3cf2d47eedb409 (do not edit this line) */
