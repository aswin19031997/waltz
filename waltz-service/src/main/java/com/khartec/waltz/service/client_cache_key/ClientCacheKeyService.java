/*
 * Waltz - Enterprise Architecture
 * Copyright (C) 2016, 2017  Waltz open source project
 * See README.md for more information
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.khartec.waltz.service.client_cache_key;


import com.khartec.waltz.data.client_cache_key.ClientCacheKeyDao;
import com.khartec.waltz.model.client_cache_key.ClientCacheKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.khartec.waltz.common.Checks.checkNotNull;
import static java.util.UUID.randomUUID;


@Service
public class ClientCacheKeyService {

    private final ClientCacheKeyDao clientCacheKeyDao;


    @Autowired
    public ClientCacheKeyService(ClientCacheKeyDao clientCacheKeyDao) {
        checkNotNull(clientCacheKeyDao, "clientCacheKeyDao cannot be null");
        this.clientCacheKeyDao = clientCacheKeyDao;
    }


    public List<ClientCacheKey> findAll() {
        return clientCacheKeyDao.findAll();
    }


    public ClientCacheKey getByKey(String key) {
        return clientCacheKeyDao.getByKey(key);
    }


    public ClientCacheKey createOrUpdate(String key) {
        UUID guid = randomUUID();
        clientCacheKeyDao.createOrUpdate(key, guid.toString());
        return clientCacheKeyDao.getByKey(key);
    }

}
