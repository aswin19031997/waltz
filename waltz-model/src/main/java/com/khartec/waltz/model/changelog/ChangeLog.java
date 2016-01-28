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

package com.khartec.waltz.model.changelog;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.khartec.waltz.model.EntityReference;
import com.khartec.waltz.model.Severity;
import org.immutables.value.Value;

import java.time.LocalDateTime;
import java.time.ZoneId;


@Value.Immutable
@JsonSerialize(as = ImmutableChangeLog.class)
@JsonDeserialize(as = ImmutableChangeLog.class)
public abstract class ChangeLog {

    public abstract EntityReference parentReference();
    public abstract Severity severity();
    public abstract String message();
    public abstract String userId();

    @Value.Default
    public LocalDateTime createdAt() {
        return LocalDateTime.now(ZoneId.of("UTC"));
    }

}