app.controller('EditDecisionController', ['$scope', '$cookies', 'LoadGraph', function ($scope, $cookies, LoadGraph) {
        $scope.model = new go.GraphLinksModel([], []);
        $scope.model.selectedNodeData = null;

        LoadGraph.get({id: "5884"}, function (data) {
        	alert(data);
            $scope.model = new go.Model.fromJson({"class": "go.TreeModel","nodeDataArray": data}),
            $scope.model.selectedNodeData = null;
        }, function (error) {
        });
    }]);



