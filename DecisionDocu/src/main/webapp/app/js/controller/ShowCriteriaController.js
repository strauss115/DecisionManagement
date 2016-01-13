/**
 * APA
 * Controller for the 'showCriteria.html'
 */

app.controller('ShowCriteriaController', [
		'$scope',
		'$cookies',
		'LoadConnectionsGraph',
		'DecisionsByTeam',
		function($scope, $cookies, LoadConnectionsGraph, DecisionsByTeam) {

			
			$scope.groups = [{"id":0, "name": "group1"}, {"id":1, "name": "group2"}, {"id":2, "name": "group3"}];
			$scope.factors = [{"id":0, "name": "f 1"}, {"id":1, "name": "f 2"}, {"id":2, "name": "f 3"}];
			$scope.consequences = [{"id":0, "name": "c 1"}, {"id":1, "name": "c 2"}, {"id":2, "name": "c 3"}];
			
			
			$scope.serverresult = [];
			
			/*
			 * Helper function: JQuery extension for creating unique arrays -  use: $.distinct([0,1,2,2,3,0,1,1,1]) $.distinct(["a", "b", "b", "c"])
			 */
			$.extend({
			    distinct : function(anArray) {
			       var result = [];
			       $.each(anArray, function(i,v){
			           if ($.inArray(v, result) == -1) result.push(v);
			       });
			       return result;
			    }
			});
			
			/*
			 *  showResult - updates table view of decisions matching criteria
			 */				
			$scope.showResult = function(){}

			/*
			 *  updateGroupFilter - fires when dropdown menu with Group filter has been changed
			 */		
			$scope.updateGroupFilter = function(){
				console.log("Group filter changed to " + $scope.selectedGroup);
			}
			
			/*
			 *  updateConsequenceFilter - fires when dropdown menu with Consequence filter has been changed
			 */		
			$scope.updateConsequenceFilter  = function(){
				console.log("Consequence filter changed to " + $scope.selectedConsequence);
			}

			
			/*
			 *  updateInfluenceFilter - fires when dropdown menu with Influence Factors filter has been changed
			 */	
			$scope.updateInfluenceFilter  = function(){
				console.log("Influence filter changed to " + $scope.selectedInfluence);
			}
			
			
			/*
			 *  initially loads decision filter criteria via LoadConnectionsGraph service
			 */			
			$scope.initiateFilters = function(){
				
			}
			
			
						
			LoadConnectionsGraph.get({teamId : $cookies['TeamId']}, function(data) {
				
				$scope.serverresult = angular.fromJson(data);
				$scope.initiateFilters();

			}, function(error) {
				
			});
			
			
		} ]);
