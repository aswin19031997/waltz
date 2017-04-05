/*
 * Waltz - Enterprise Architecture
 * Copyright (C) 2016  Khartec Ltd.
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

package com.khartec.waltz.model.physical_flow;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.khartec.waltz.model.DescriptionProvider;
import com.khartec.waltz.model.IdProvider;
import com.khartec.waltz.model.LastUpdatedProvider;
import com.khartec.waltz.model.ProvenanceProvider;
import org.immutables.value.Value;

import java.util.Optional;


/**
 * Binds a data specification (e.g. a file specification) to a logical data flow.
 * As such it can be thought of a realisation of the logical into the
 * physical. A Logical may have many such realisations whereas a a physical
 * data flow may only associated to a single logical flow.
 */
@Value.Immutable
@JsonSerialize(as = ImmutablePhysicalFlow.class)
@JsonDeserialize(as = ImmutablePhysicalFlow.class)
public abstract class PhysicalFlow implements
        IdProvider,
        DescriptionProvider,
        ProvenanceProvider,
        LastUpdatedProvider {

    public abstract long logicalFlowId();

    public abstract long specificationId();

    public abstract FrequencyKind frequency();

    // e.g. for representing T+0, T+1, T+7, T-1
    public abstract int basisOffset();

    public abstract TransportKind transport();

    public abstract Optional<Long> specificationDefinitionId();
}
