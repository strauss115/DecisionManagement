app.controller('EditDecisionController', ['$scope', '$cookies', 'LoadGraph', function ($scope, $cookies, LoadGraph) {
        $scope.model = new go.GraphLinksModel([], []);
        $scope.model.selectedNodeData = null;



        LoadGraph.query({id: "D1"}, function (data) {
            $scope.model = new go.Model.fromJson(
                    {"class": "go.TreeModel",
                        "nodeDataArray": data['decisionNodes']})
        }, function (error) {
        });



    }]);



