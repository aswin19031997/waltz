package com.khartec.waltz.data.source_data_rating;

import com.khartec.waltz.common.Checks;
import com.khartec.waltz.model.EntityKind;
import com.khartec.waltz.model.capabilityrating.RagRating;
import com.khartec.waltz.model.source_data_rating.ImmutableSourceDataRating;
import com.khartec.waltz.model.source_data_rating.SourceDataRating;
import com.khartec.waltz.schema.tables.records.SourceDataRatingRecord;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collection;

import static com.khartec.waltz.schema.tables.SourceDataRating.SOURCE_DATA_RATING;

@Repository
public class SourceDataRatingDao {

    private static final RecordMapper<? super Record, SourceDataRating> MAPPER = r -> {
        SourceDataRatingRecord record = r.into(SOURCE_DATA_RATING);
        return ImmutableSourceDataRating.builder()
                .sourceName(record.getSourceName())
                .entityKind(EntityKind.valueOf(record.getEntityKind()))
                .authoritativeness(RagRating.valueOf(record.getAuthoritativeness()))
                .accuracy(RagRating.valueOf(record.getAccuracy()))
                .completeness(RagRating.valueOf(record.getCompleteness()))
                .build();
    };

    private final DSLContext dsl;


    @Autowired
    public SourceDataRatingDao(DSLContext dsl) {
        Checks.checkNotNull(dsl, "dsl cannot be null");
        this.dsl = dsl;
    }


    public Collection<SourceDataRating> findAll() {
        return dsl.select(SOURCE_DATA_RATING.fields())
                .from(SOURCE_DATA_RATING)
                .fetch(MAPPER);

    }

}