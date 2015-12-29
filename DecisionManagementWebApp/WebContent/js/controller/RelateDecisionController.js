app.controller('RelateDecisionController', ['$scope', '$cookies', 'LoadConnectionsGraph', function ($scope, $cookies, LoadConnectionsGraph) {
    $scope.model = new go.GraphLinksModel([], []);
    $scope.model.selectedNodeData = null;

    LoadConnectionsGraph.get({teamId: "6141"}, function (data) {
    	alert(data);
        $scope.model = new go.GraphLinksModel(data['data'],data['relations']);
        $scope.model.selectedNodeData = null;
    }, function (error) {
    });
}]);



