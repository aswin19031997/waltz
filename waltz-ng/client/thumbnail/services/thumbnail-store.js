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


import { checkIsEntityRef } from "../../common/checks";

function store($http, BaseApiURL) {
    const BASE = `${BaseApiURL}/thumbnail`;

    const getByReference = (ref) => {
        checkIsEntityRef(ref);
        return $http
            .get(`${BASE}/${ref.kind}/${ref.id}`)
            .then(d => d.data);
    };


    const deleteByReference = (ref) => {
        checkIsEntityRef(ref);
        return $http
            .delete(`${BASE}/${ref.kind}/${ref.id}`)
            .then(d => d.data);
    };


    const save = (cmd) => {
        return $http
            .post(`${BASE}/save`, cmd)
            .then(d => d.data);
    };

    return {
        getByReference,
        deleteByReference,
        save
    }
}


store.$inject = [
    "$http",
    "BaseApiUrl"
];


const serviceName = "ThumbnailStore";


export const ThumbnailStore_API = {
    getByReference: {
        serviceName,
        serviceFnName: "getByReference",
        description: "get a thumbnail by entity ref"
    },
    deleteByReference: {
        serviceName,
        serviceFnName: "deleteByReference",
        description: "delete a thumbnail by entity ref"
    },
    save: {
        serviceName,
        serviceFnName: "save",
        description: "save a thumbnail"
    }
};


export default {
    serviceName,
    store
};