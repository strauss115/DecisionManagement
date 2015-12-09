app.controller('EditDecisionController', ['$scope', '$cookies', 'LoadGraph', function ($scope, $cookies, LoadGraph) {

$scope.json = LoadGraph.query({id: "D1"}, 
    function (data) {
        alert(data);
        $scope.model = new go.GraphLinksModel([{"text":"Decision 1 Team 1", "loc":"0 0", "brush":"black", "key":0}, {"text":"Influence Factor", "dir":"right", "parent":0, "loc":"182 -100", "brush":"black", "key":1}, {"text":"Alternatives", "dir":"right", "parent":0, "loc":"182 100", "brush":"black", "key":2}, {"text":"Alternative 1 description", "dir":"right", "parent":2, "loc":"342 90", "brush":"black", "key":21}, {"text":"Consequences", "dir":"left", "parent":0, "loc":"-80 -100", "brush":"black", "key":3}, {"text":"Quality Attributes", "dir":"left", "parent":0, "loc":"-80 100", "brush":"black", "key":4}, {"text":"Rationales", "dir":"left", "parent":0, "loc":"-185 200", "brush":"black", "key":5}, {"text":"rationale 1 description", "dir":"left", "parent":5, "loc":"-335 180", "brush":"black", "key":51}, {"text":"rationale 2 description", "dir":"left", "parent":5, "loc":"-335 210", "brush":"black", "key":52}], [{"to":1, "from":0}, {"to":2, "from":0}, {"to":21, "from":2}, {"to":3, "from":0}, {"to":4, "from":0}, {"to":5, "from":0}, {"to":51, "from":5}, {"to":52, "from":5}]);
        $scope.model.selectedNodeData = null;
    }, function (error) {});
}]);