/*
 * Copyright 2013 Harald Wellmann
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ops4j.orient.spring.tx;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.orientechnologies.orient.core.db.ODatabaseComplex;
import com.orientechnologies.orient.core.record.impl.ODocument;

/**
 * @author Harald Wellmann
 * 
 */
public class TransactionalService {

    @Autowired
    private OrientDatabaseManager dbm;

    private ODatabaseComplex<ODocument> db;

    @PostConstruct
    public void init() {
        db = dbm.getDatabase();
    }

    @Transactional
    public void commitAutomatically(String className) {
        assertThat(db.getTransaction().isActive(), is(true));

        ODocument doc = new ODocument(className);
        doc.field("test", "test");
        db.save(doc);
    }

    @Transactional
    public void rollbackOnError(String className) {
        assertThat(db.getTransaction().isActive(), is(true));
        ODocument doc = new ODocument(className);
        doc.field("test", "test");
        db.save(doc);

        throw new RuntimeException();
    }
}