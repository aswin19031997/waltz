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

package com.khartec.waltz.common;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.khartec.waltz.common.Checks.checkNotNull;


public class OptionalUtilities {

    /**
     * Synonym for ofNullable as it reads better.
     * @param value
     * @return
     */
    public static <T> Optional<T> maybe(T value) {
        return Optional.ofNullable(value);
    }


    /**
     * Given a list of optional values will return
     * a list containing only the values that are not
     * empty (unpacked)
     * @param optionals
     * @param <T>
     * @return
     */
    public static <T> List<T> toList(Optional<T>... optionals) {
        if (optionals == null) { return Collections.emptyList(); }

        return Stream
                .of(optionals)
                .filter(opt -> opt.isPresent())
                .map(opt -> opt.get())
                .collect(Collectors.toList());

    }


    /**
     * Returns true iff, `opt` is empty and val `is` null, or, `opt.get` equals `val`
     */
    public static <T> boolean contentsEqual(Optional<T> opt, T val) {
        checkNotNull(opt, "'opt' cannot be null");
        return opt
                .map(v -> v.equals(val))
                .orElse(val == null);
    }


    /**
     * Takes an optional value (that may itself be null) and makes it a non-null optional
     * @param nullable  an Optional value that may, itself, be null
     * @param <T>  type of the optional value
     * @return  an Optional
     */
    public static <T> Optional<T> ofNullableOptional(Optional<T> nullable) {
        return nullable == null
                ? Optional.empty()
                : nullable;
    }

}
