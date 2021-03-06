/*
 * Waltz - Enterprise Architecture
 * Copyright (C) 2016, 2017 Waltz open source project
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

package com.khartec.waltz.jobs.harness;

import com.khartec.waltz.common.ListUtilities;
import com.khartec.waltz.common.hierarchy.FlatNode;
import com.khartec.waltz.common.hierarchy.Forest;
import com.khartec.waltz.common.hierarchy.HierarchyUtilities;
import com.khartec.waltz.common.hierarchy.Node;
import com.khartec.waltz.data.orgunit.OrganisationalUnitDao;
import com.khartec.waltz.model.authoritativesource.AuthoritativeSource;
import com.khartec.waltz.model.orgunit.OrganisationalUnit;
import com.khartec.waltz.service.DIConfiguration;
import com.khartec.waltz.service.authoritative_source.AuthoritativeSourceService;
import com.khartec.waltz.service.entity_hierarchy.EntityHierarchyService;
import com.khartec.waltz.service.orgunit.OrganisationalUnitService;
import org.jooq.DSLContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static com.khartec.waltz.schema.tables.OrganisationalUnit.ORGANISATIONAL_UNIT;

public class LogicalFlowDecoratorRatingsServiceHarness {

    public static void main(String[] args) throws SQLException {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(DIConfiguration.class);

        AuthoritativeSourceService authoritativeSourceService = ctx.getBean(AuthoritativeSourceService.class);

        List<AuthoritativeSource> authSources = authoritativeSourceService.findAll();

        OrganisationalUnitService organisationalUnitService = ctx.getBean(OrganisationalUnitService.class);
        OrganisationalUnitDao organisationalUnitDao = ctx.getBean(OrganisationalUnitDao.class);
        DSLContext dsl = ctx.getBean(DSLContext.class);


        dsl.select(ORGANISATIONAL_UNIT.fields())
                .from(ORGANISATIONAL_UNIT)
                .fetch(organisationalUnitDao.TO_DOMAIN_MAPPER);

        EntityHierarchyService hierarchyService = ctx.getBean(EntityHierarchyService.class);

        List<OrganisationalUnit> allOrgUnits = organisationalUnitService.findAll();
        List<FlatNode<OrganisationalUnit, Long>> ouNodes = ListUtilities.map(allOrgUnits, ou -> new FlatNode<>(ou.id().get(), ou.parentId(), ou));
        Forest<OrganisationalUnit, Long> ouForest = HierarchyUtilities.toForest(ouNodes);
        Map<Long, Node<OrganisationalUnit, Long>> nodeMap = ouForest.getAllNodes();





    }

}
