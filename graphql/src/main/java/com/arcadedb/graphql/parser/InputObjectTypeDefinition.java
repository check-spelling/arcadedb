/* Generated by: JJTree: Do not edit this line. InputObjectTypeDefinition.java Version 1.1 */
/* ParserGeneratorCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package com.arcadedb.graphql.parser;

import java.util.*;

public class InputObjectTypeDefinition extends TypeDefinition {

  protected Name                       name;
  protected Directives                 directives;
  protected List<InputValueDefinition> inputValueDefinitions = new ArrayList<>();

  public InputObjectTypeDefinition(int id) {
    super(id);
  }

  public InputObjectTypeDefinition(GraphQLParser p, int id) {
    super(p, id);
  }

  public String getName() {
    return name != null ? name.value : null;
  }

  /**
   * Accept the visitor.
   **/
  public Object jjtAccept(GraphQLParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* ParserGeneratorCC - OriginalChecksum=395d43b299254ecb99e16bccb871997c (do not edit this line) */