/*
 * Copyright 2021 Arcade Data Ltd
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.arcadedb.integration.backup.format;

import com.arcadedb.database.DatabaseFactory;
import com.arcadedb.database.DatabaseInternal;
import com.arcadedb.database.EmbeddedDatabase;
import com.arcadedb.engine.PaginatedFile;
import com.arcadedb.integration.backup.BackupException;
import com.arcadedb.integration.backup.BackupSettings;
import com.arcadedb.integration.importer.ConsoleLogger;
import com.arcadedb.schema.EmbeddedSchema;
import com.arcadedb.utility.FileUtils;

import java.io.*;
import java.util.*;
import java.util.zip.*;

public class FullBackupFormat extends AbstractBackupFormat {
  public FullBackupFormat(final DatabaseInternal database, final BackupSettings settings, final ConsoleLogger logger) {
    super(database, settings, logger);
  }

  @Override
  public void backupDatabase() throws Exception {
    File file = new File(settings.file);
    if (file.exists() && !settings.overwriteFile)
      throw new BackupException(String.format("The backup file '%s' already exist and '-o' setting is false", settings.file));

    if (database.isTransactionActive())
      throw new BackupException("Transaction in progress found");

    logger.logLine(0, "Executing full backup of database to '%s'...", settings.file);

    final File backupFile = new File(settings.file);

    try (ZipOutputStream zipFile = new ZipOutputStream(new FileOutputStream(backupFile), DatabaseFactory.getDefaultCharset())) {
      zipFile.setLevel(9);

      // ACQUIRE A READ LOCK. TRANSACTION CAN STILL RUN, BUT CREATION OF NEW FILES (BUCKETS, TYPES, INDEXES) WILL BE PUT ON PAUSE UNTIL THIS LOCK IS RELEASED
      database.executeInReadLock(() -> {
        // AVOID FLUSHING OF DATA PAGES TO DISK
        database.getPageManager().suspendPageFlushing(true);
        try {
          final long beginTime = System.currentTimeMillis();

          long databaseOrigSize = 0L;
          databaseOrigSize += compressFile(zipFile, ((EmbeddedDatabase) database).getConfigurationFile());
          databaseOrigSize += compressFile(zipFile, ((EmbeddedSchema) database.getSchema()).getConfigurationFile());

          final Collection<PaginatedFile> files = database.getFileManager().getFiles();

          for (PaginatedFile paginatedFile : files)
            databaseOrigSize += compressFile(zipFile, paginatedFile.getOSFile());

          zipFile.close();

          final long elapsedInSecs = (System.currentTimeMillis() - beginTime) / 1000;

          final long databaseCompressedSize = backupFile.length();

          logger.logLine(0, "Full backup completed in %d seconds %s -> %s (%,d%% compressed)", elapsedInSecs, FileUtils.getSizeAsString(databaseOrigSize),
              FileUtils.getSizeAsString((databaseCompressedSize)),
              databaseOrigSize > 0 ? (databaseOrigSize - databaseCompressedSize) * 100 / databaseOrigSize : 0);

        } finally {
          database.getPageManager().suspendPageFlushing(false);
        }
        return null;
      });
    }
  }

  private long compressFile(final ZipOutputStream zipFile, final File inputFile) throws IOException {
    logger.log(2, "- File '%s'...", inputFile.getName());
    final long origSize = inputFile.length();

    final ZipEntry zipEntry = new ZipEntry(inputFile.getName());
    zipFile.putNextEntry(zipEntry);

    try (final FileInputStream fileIn = new FileInputStream(inputFile)) {
      fileIn.transferTo(zipFile);
    }
    zipFile.closeEntry();

    final long compressedSize = zipEntry.getCompressedSize();

    logger.logLine(2, " %s -> %s (%,d%% compressed)", FileUtils.getSizeAsString(origSize), FileUtils.getSizeAsString(compressedSize),
        origSize > 0 ? (origSize - compressedSize) * 100 / origSize : 0);

    return origSize;
  }
}