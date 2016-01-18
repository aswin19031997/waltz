import angular from 'angular';

function controller(svgStore, $scope, $state) {

    const vm = this;

    $scope.$watch('ctrl.person', (person) => {
        if (person) {
            const navKey = { empId: person.employeeId };
            $state.go('main.person.view', navKey);
        }
    });


    svgStore.findByKind('ORG_TREE').then(xs => vm.diagrams = xs);
    vm.blockProcessor = block => {
        block.parent.onclick = (e) => {
            $state.go('main.person.view', { empId: block.value });
            e.preventDefault();
        };
        angular.element(block.parent).addClass('clickable');
    };

    vm.person = null;
}


controller.$inject = ['SvgDiagramStore', '$scope', '$state'];

export default {
    template: require('./person-home.html'),
    controllerAs: 'ctrl',
    controller
};
