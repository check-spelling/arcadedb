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
package com.arcadedb.server.ha.message;

import com.arcadedb.log.LogManager;
import com.arcadedb.server.ha.HAServer;

import java.util.logging.Level;

/**
 * Response for a transaction. This is needed to check the quorum by the leader.
 */
public class DatabaseChangeStructureResponse extends HAAbstractCommand {
  @Override
  public HACommand execute(final HAServer server, final String remoteServerName, final long messageNumber) {
    server.receivedResponse(remoteServerName, messageNumber, null);
    LogManager.instance().log(this, Level.FINE, "Database change structure received from server %s (msg=%d)", null, remoteServerName, messageNumber);
    return null;
  }

  @Override
  public String toString() {
    return "dbchangestructure-response";
  }
}
