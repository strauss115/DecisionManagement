app.controller('EditDecisionController', ['$scope', '$cookies', 'LoadGraph', function ($scope, $cookies, LoadGraph) {

        $scope.json = LoadGraph.query({id: "D1"}, function (data) {
            $scope.model = new go.GraphLinksModel(
                    data['decisionGraph']['decisionNodes'], data['decisionGraph']['decisionAssociation']);
            $scope.model.selectedNodeData = null;
            
           

        }, function (error) {
        });



    }]);