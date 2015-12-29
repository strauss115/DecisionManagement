app.controller('EditDecisionController', ['$scope', '$cookies', 'LoadEditGraph', function ($scope, $cookies, LoadEditGraph) {
        $scope.model = new go.GraphLinksModel([], []);
        $scope.model.selectedNodeData = null;

        LoadEditGraph.get({id: "5884"}, function (data) {
         alert(data);
            $scope.model = new go.Model.fromJson({"class": "go.TreeModel","nodeDataArray": data}),
            $scope.model.selectedNodeData = null;
        }, function (error) {
        });
    }]);



