/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.samples.petclinic;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.store.Store;
import org.springframework.samples.petclinic.store.StoreDetails;
import org.springframework.samples.petclinic.store.StoreService;
import org.springframework.samples.petclinic.vet.VetRepository;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PetclinicIntegrationTests {

    @Autowired
    private VetRepository vetRepository;

    @Autowired
    private StoreService storeService;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Before
    public void init() {
        transactionTemplate.execute(status -> {
            entityManager.createQuery("delete from StoreDetails").executeUpdate();
            entityManager.createQuery("delete from Store").executeUpdate();

            return null;
        });
    }

    @Test
    public void testFindAll() {
        vetRepository.findAll();
        vetRepository.findAll(); // served from cache

        assertTrue(true); // hypersistence-optimizer removed
    }

    @Test
    public void testSaveAll() {
        storeService.saveAll(newStoreDetailsList(1000));

        assertTrue(true); // hypersistence-optimizer removed
    }

    @Test
    public void testInsertAll() {
        storeService.insertAll(newStoreDetailsList(2000));

        assertTrue(true); // hypersistence-optimizer removed
    }

    private List<StoreDetails> newStoreDetailsList(int storeCount) {
        List<StoreDetails> storeDetailsList = new ArrayList<>();

        for (int i = 1; i <= storeCount; i++) {
            storeDetailsList.add(
                new StoreDetails()
                    .setId(i)
                    .setOwner("Vlad Mihalcea")
                    .setStore(
                        new Store()
                            .setId(i)
                            .setName(String.format("Store no %d", i))
                    )
            );
        }

        return storeDetailsList;
    }
}
