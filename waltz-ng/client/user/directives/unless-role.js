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

import _ from 'lodash';

function controller(scope, UserService) {
    UserService.whoami()
        .then(user => scope.revert = !_.contains(user.roles, scope.role));

    scope.show = false;
}

controller.$inject = ['$scope', 'UserService'];


export default () => ({
    replace: true,
    restrict: 'A',
    transclude: true,
    scope: {
        role: '@waltzUnlessRole'
    },
    template: '<div ng-show="revert"><ng-transclude></ng-transclude></div>',
    controller
});
