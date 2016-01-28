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

package com.khartec.waltz.jobs.housekeeping;

import com.khartec.waltz.common.ListUtilities;
import com.khartec.waltz.common.hierarchy.FlatNode;
import com.khartec.waltz.common.hierarchy.Forest;
import com.khartec.waltz.common.hierarchy.HierarchyUtilities;
import com.khartec.waltz.common.hierarchy.Node;
import com.khartec.waltz.data.person.PersonDao;
import com.khartec.waltz.model.person.Person;
import com.khartec.waltz.schema.tables.records.PersonHierarchyRecord;
import com.khartec.waltz.service.DIConfiguration;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static com.khartec.waltz.schema.tables.PersonHierarchy.PERSON_HIERARCHY;
import static java.util.stream.Collectors.toList;


@Service
public class PersonHierarchyService {

    private final PersonDao personDao;
    private final DSLContext dsl;


    @Autowired
    public PersonHierarchyService(PersonDao personDao, DSLContext dsl) {
        this.personDao = personDao;
        this.dsl = dsl;
    }


    public static void main(String[] args) {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(DIConfiguration.class);

        PersonHierarchyService svc = ctx.getBean(PersonHierarchyService.class);
        svc.build();
    }

    public void build() {
        List<Person> all = personDao.all();

        Forest<Person, String> forest = toForest(all);

        List<PersonHierarchyRecord> records = toHierarchyRecords(forest);

        System.out.println(records.size());

        dsl.deleteFrom(PERSON_HIERARCHY).execute();
        dsl.batchStore(records).execute();

        System.out.println("Done");
    }


    private List<PersonHierarchyRecord> toHierarchyRecords(Forest<Person, String> forest) {
        List<PersonHierarchyRecord> records = new LinkedList<>();

        for (Node<Person, String> node : forest.getAllNodes().values()) {
            List<Person> ancestors =
                    ListUtilities.reverse(
                        HierarchyUtilities.parents(node)
                            .stream()
                            .map(n -> n.getData())
                            .collect(Collectors.toList()));

            for (int i = 0; i < ancestors.size(); i++) {
                String ancestorId = ancestors.get(i).employeeId();
                String selfId = node.getData().employeeId();
                PersonHierarchyRecord record = new PersonHierarchyRecord(ancestorId, selfId, i + 1);
                records.add(record);
            }
        }
        return records;
    }


    private Forest<Person, String> toForest(List<Person> all) {
        List<FlatNode<Person, String>> allFlatNodes = all.stream()
                .map(p -> new FlatNode<>(p.employeeId(), p.managerEmployeeId(), p))
                .collect(toList());

        return HierarchyUtilities.toForest(allFlatNodes);
    }

}