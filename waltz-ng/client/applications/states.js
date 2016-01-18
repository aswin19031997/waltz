/*
 *  Waltz
 * Copyright (c) David Watkins. All rights reserved.
 * The use and distribution terms for this software are covered by the
 * Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
 * which can be found in the file epl-v10.html at the root of this distribution.
 * By using this software in any fashion, you are agreeing to be bound by
 * the terms of this license.
 * You must not remove this notice, or any other, from this software.
 *
 */

import AppEdit from './app-edit';
import AppRegistration from './app-registration';
import appTagExplorerView from './app-tag-explorer';



export const appRegistrationState = {
    url: 'application/registration',
    views: {'content@': AppRegistration }
};


export const appEditState = {
    url: 'application/:id/edit',
    resolve: {
        appView: [
            'ApplicationViewDataService',
            '$stateParams',
            (ApplicationViewDataService, $stateParams) =>
                ApplicationViewDataService.getById($stateParams.id)
        ],
        orgUnits: [
            'OrgUnitStore',
            (OrgUnitStore) => OrgUnitStore.findAll()
        ]
    },
    views: {'content@': AppEdit}
};

export const appTagExplorerState = {
    url: 'application-tag-explorer/:tag',
    views: {'content@': appTagExplorerView }
}