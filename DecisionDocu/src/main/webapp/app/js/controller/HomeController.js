/**
 * Controller for the 'index.html' - Dashboard
 */
app.controller('HomeController', [
		'$scope',
		'$cookies',
		'$location',
		'DecisionsByTeam',
		function($scope, $cookies, $location, DecisionsByTeam) {
			$scope.decisions = [];
			
			$scope.navigate = function(id){
				$location.path("/editDecision/" + id);
			}
			
			DecisionsByTeam.get({}, function(data) {
				var obj = angular.fromJson(data);
				var firstElementId = "";
				for (var i = 0; i < obj.length; i++) {
					if (i == 0) {
						firstElementId = obj[i].id;
						$scope.selectedDecisionId = obj[i].id;
					}
					
					$scope.decisions.push({
						"id" : obj[i].id,
						"name" : obj[i].name,
						"lastActivity" : obj[i].lastActivity
					});
					
				}
			}, function(error) {
				// TODO error handling
			});
		} ]);