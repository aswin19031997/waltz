package com.khartec.waltz.web.endpoints.extracts;


import com.khartec.waltz.data.application.ApplicationIdSelectorFactory;
import com.khartec.waltz.model.EntityKind;
import com.khartec.waltz.model.application.ApplicationIdSelectionOptions;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.khartec.waltz.schema.Tables.*;
import static com.khartec.waltz.web.WebUtilities.*;
import static spark.Spark.post;



@Service
public class AllocationsExtractor extends BaseDataExtractor{

    private final ApplicationIdSelectorFactory applicationIdSelectorFactory;

    @Autowired
    public AllocationsExtractor(DSLContext dsl, ApplicationIdSelectorFactory applicationIdSelectorFactory) {
        super(dsl);
        this.applicationIdSelectorFactory = applicationIdSelectorFactory;
    }

    @Override
    public void register() {
        registerExtractForAll( mkPath("data-extract", "allocations", "all"));
        registerExtractForCategory( mkPath("data-extract", "allocations", "measurable-category", ":measurableCategoryId"));
        registerExtractForScheme( mkPath("data-extract", "allocations", "allocation-scheme", ":schemeId"));
    }


    private void registerExtractForAll(String path) {
        post(path, (request, response) -> {

            ApplicationIdSelectionOptions applicationIdSelectionOptions = readAppIdSelectionOptionsFromBody(request);

            String csv = doExtract(
                    DSL.trueCondition(),
                    applicationIdSelectionOptions);

            return writeFile(
                    "allocations.csv",
                    csv,
                    response);
        });
    }


    private void registerExtractForCategory(String path) {
        post(path, (request, response) -> {

            long measurableCategoryId = getLong(request, "measurableCategoryId");

            ApplicationIdSelectionOptions applicationIdSelectionOptions = readAppIdSelectionOptionsFromBody(request);

            Record1<String> fileName = dsl.select(MEASURABLE_CATEGORY.NAME)
                    .from(MEASURABLE_CATEGORY)
                    .where(MEASURABLE_CATEGORY.ID.eq(measurableCategoryId))
                    .fetchOne();

            String suggestedFilename = fileName.value1() + ".csv";

            String csv = doExtract(
                    MEASURABLE.MEASURABLE_CATEGORY_ID.eq(measurableCategoryId),
                    applicationIdSelectionOptions);

            return writeFile(
                    suggestedFilename,
                    csv,
                    response);
        });
    }


    private void registerExtractForScheme(String path) {
        post(path, (request, response) -> {

            long schemeId = getLong(request, "schemeId");

            ApplicationIdSelectionOptions applicationIdSelectionOptions = readAppIdSelectionOptionsFromBody(request);

            Record2<String, String> fileName = dsl.select(MEASURABLE_CATEGORY.NAME, ALLOCATION_SCHEME.NAME)
                    .from(ALLOCATION_SCHEME)
                    .innerJoin(MEASURABLE_CATEGORY).on(ALLOCATION_SCHEME.MEASURABLE_CATEGORY_ID.eq(MEASURABLE_CATEGORY.ID))
                    .where(ALLOCATION_SCHEME.ID.eq(schemeId))
                    .fetchOne();

            String suggestedFilename = fileName.value1() + "-" + fileName.value2() + ".csv";

            String csv = doExtract(
                    ALLOCATION_SCHEME.ID.eq(schemeId),
                    applicationIdSelectionOptions);

            return writeFile(
                    suggestedFilename,
                    csv,
                    response);
        });
    }


    // -- HELPER ----

    private String doExtract(Condition additionalCondition, ApplicationIdSelectionOptions applicationIdSelectionOptions) {
        Select<Record1<Long>> appSelector = applicationIdSelectorFactory.apply(applicationIdSelectionOptions);
        SelectSelectStep<Record> reportColumns = dsl
                .select(APPLICATION.NAME.as("Application"),
                        APPLICATION.ID.as("Waltz Application Id"),
                        APPLICATION.ASSET_CODE.as("Asset Code"))
                .select(ALLOCATION_SCHEME.NAME.as("Allocation Scheme"))
                .select(MEASURABLE.NAME.as("Measurable"))
                .select(ALLOCATION.ALLOCATION_PERCENTAGE.as("Allocation Percentage"),
                        ALLOCATION.LAST_UPDATED_AT.as("Last Updated"),
                        ALLOCATION.LAST_UPDATED_BY.as("Last Updated By"),
                        ALLOCATION.PROVENANCE.as("Provenance"));


        Condition condition = ALLOCATION.ENTITY_ID.in(appSelector)
                .and(ALLOCATION.ENTITY_KIND.eq(EntityKind.APPLICATION.name()))
                .and(additionalCondition);

        SelectConditionStep<Record> qry = reportColumns
                .from(ALLOCATION)
                .innerJoin(MEASURABLE).on(ALLOCATION.MEASURABLE_ID.eq(MEASURABLE.ID))
                .innerJoin(ALLOCATION_SCHEME).on(ALLOCATION.ALLOCATION_SCHEME_ID.eq(ALLOCATION_SCHEME.ID))
                .innerJoin(APPLICATION).on(ALLOCATION.ENTITY_ID.eq(APPLICATION.ID))
                .where(condition);

        return qry.fetch().formatCSV();
    }
}