/**
 * Controller for the 'completeDecision.html' - load incompleted decisions
 */
app.controller('CompleteDecisionController', [
		'$scope',
		'$cookies',
'$location',
		'DecisionsByTeam',
		function($scope, $cookies, $location, DecisionsByTeam) {
			// init decision array
			$scope.decisions = [];
			
			
			$scope.navigate = function(id){
				$location.path("/editDecision/" + id);
			}

			// load all graphs from team to show in selection
			DecisionsByTeam.get({}, function(data) {
				var obj = angular.fromJson(data);
				var firstElementId = "";
				for (var i = 0; i < obj.length; i++) {
					if (i == 0) {
						firstElementId = obj[i].id;
						$scope.selectedDecisionId = obj[i].id;
					}

					var alternatives = angular.fromJson(obj[i].alternatives);
					var consequences = angular.fromJson(obj[i].consequences);
					var qualityAttributes = angular
							.fromJson(obj[i].qualityAttributes);
					var influenceFactors = angular
							.fromJson(obj[i].influenceFactors);
					var rationales = angular.fromJson(obj[i].rationales);

					var missing = "";
					if (alternatives.length == 0) {
						missing += "alternatives, ";
					}
					if (consequences.length == 0) {
						missing += "consequences, ";
					}
					if (qualityAttributes.length == 0) {
						missing += "quality attributes, ";
					}
					if (influenceFactors.length == 0) {
						missing += "influence factors, ";
					}
					if (rationales.length == 0) {
						missing += "rationales, ";
					}

					if (missing != "") {
						$scope.decisions.push({
							"id" : obj[i].id,
							"name" : obj[i].name,
							"missing" : missing
						});
					}
				}
			}, function(error) {
			});
		} ]);