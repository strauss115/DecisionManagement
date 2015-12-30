app.controller('EditDecisionController', ['$scope', '$cookies', 'LoadEditGraph', 'DecisionsByTeam', function ($scope, $cookies, LoadEditGraph, DecisionsByTeam) {
        $scope.model = new go.GraphLinksModel([], []);

        $scope.updateGraph = function() {
        	   alert($scope.selectedDecision);
               // load chosen graph
               LoadEditGraph.get({id: $scope.selectedDecision}, function (data) {
                //alert(data);
                   $scope.model = new go.Model.fromJson({"class": "go.TreeModel","nodeDataArray": data}),
                   $scope.model.selectedNodeData = null;
               }, function (error) {
               });
        	}
        $scope.model.selectedNodeData = null;
        $scope.decisions = [];
        // load all graphs from team
        DecisionsByTeam.get({}, function (data) {
        	var obj = angular.fromJson(data);
        	var firstElementId = "";
        	for(var i = 0; i< obj.length;i++){
        		if(i == 0){
        			firstElementId = obj[i].id;
        		}
				$scope.decisions.push({
					"id" : obj[i].id,
					"name" : obj[i].name
				});
        		//alert(obj[i].id);
        	}
        	if(firstElementId != ""){
                // load the first graph
                LoadEditGraph.get({id: firstElementId}, function (data) {
                 //alert(data);
                    $scope.model = new go.Model.fromJson({"class": "go.TreeModel","nodeDataArray": data}),
                    $scope.model.selectedNodeData = null;
                }, function (error) {
                });
        	}
           }, function (error) {
           });

    }]);


