/*
 *  This file is part of Waltz.
 *
 *     Waltz is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Waltz is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Waltz.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.khartec.waltz.web.endpoints.api;


import com.khartec.waltz.model.enduserapp.EndUserApplication;
import com.khartec.waltz.service.end_user_app.EndUserAppService;
import com.khartec.waltz.web.ListRoute;
import com.khartec.waltz.web.WebUtilities;
import com.khartec.waltz.web.endpoints.Endpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.khartec.waltz.web.WebUtilities.mkPath;
import static com.khartec.waltz.web.endpoints.EndpointUtilities.getForList;

@Service
public class EndUserAppEndpoint implements Endpoint {



    private static final String BASE_URL = mkPath("api", "end-user-application");

    private final EndUserAppService endUserAppService;

    @Autowired
    public EndUserAppEndpoint(EndUserAppService endUserAppService) {
        this.endUserAppService = endUserAppService;
    }


    @Override
    public void register() {

        ListRoute<EndUserApplication> findByOrgUnitTree = (request, response) -> {
            long ouId = WebUtilities.getId(request);
            return endUserAppService
                    .findByOrganisationalUnitTree(ouId);
        };

        getForList(
                mkPath(BASE_URL, "org-unit-tree", ":id"),
                findByOrgUnitTree);
    }

}