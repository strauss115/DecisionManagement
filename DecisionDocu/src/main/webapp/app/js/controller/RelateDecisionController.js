/**
 * Controller for the 'relateDecision.html' - load decisions and show connections via GoJs-graph
 */
app.controller('RelateDecisionController', [
		'$scope',
		'$cookies',
		'LoadConnectionsGraph',
		'DecisionsByTeam',
		function($scope, $cookies, LoadConnectionsGraph, DecisionsByTeam) {
			$scope.model = new go.GraphLinksModel([], []);
			$scope.model.selectedNodeData = null;
			// init decision array
			$scope.decisions = [];
			LoadConnectionsGraph.get({
				teamId : $cookies['TeamId']
			}, function(data) {
				//alert(data);
				$scope.model = new go.GraphLinksModel(data['data'],
						data['relations']);
				$scope.model.selectedNodeData = null;
			}, function(error) {
			});
			// load all graphs from team to show in selection
			DecisionsByTeam.get({}, function(data) {
				var obj = angular.fromJson(data);
				var firstElementId = "";
				for (var i = 0; i < obj.length; i++) {
					$scope.decisions.push({
						"id" : obj[i].id,
						"name" : obj[i].name,
						"alternatives" : obj[i].alternatives
					});
				}
			}, function(error) {
			});
		} ]);
