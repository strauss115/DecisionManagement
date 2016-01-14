/**
 * APA
 * Controller for the 'showDependency.html' - load decisions and show connections via GoJs-graph
 */

app.controller('ShowDependencyController',[
			'$scope',
			'$cookies',
			'$routeParams',
			'$window',
			'LoadEditGraph',
			'LoadConnectionsGraph',
			'DecisionsByTeam',
			'AddAttributeToDecision',
			'GetNode',
			'GetFile',
			'fileUpload1',
			'GetMessages',
			function($scope, $cookies, $routeParams, $window,
					LoadEditGraph, LoadConnectionsGraph, DecisionsByTeam, AddAttributeToDecision,
					GetNode, GetFile, fileUpload1, GetMessages) {
			
			
			/*
			 * Scope variable 
			 */
			$scope.serverresult = new Object();
			$scope.gojs = new Object();
			$scope.decisions = [{"id":1000, "name":"- select decision -"}];
			$scope.links = [{"id":1000, "name":"select decision first!"}];
			
			 
			
			/*
			 * Helper function: JQuery extension for creating unique arrays -  use: $.distinct([0,1,2,2,3,0,1,1,1])
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
			 * fillLinks creates a unsorted unique values array with connection names for the dropdown menue 
			 */
			$scope.fillLinks = function(decisionID){
				
				var lnk = [];
				$.each($scope.serverresult.relations, function(idx, item){
					if(item.from == decisionID){
						lnk.push(item.text);
					}
					
					//console.log(angular.toJson(item));
				});
				
				//reset connections array
				while($scope.links.length > 0) {$scope.links.pop();}
				
				for (var i = 0; i < lnk.length; i++) {
					$scope.links.push({"id": i, "name": lnk[i]});
				}
				
				// add dummy entry if no connections available
				if (lnk.length == 0) {
					$scope.links.push({"id": 0, "name": "Decision has no connections"})};

			}
			
			//console.log ($.distinct(["eins", "b", "eins", "b", "B", "OBj"]));
			
			
			/*
			 * build GOJS graph based on preselection of decision and type of dependency connection
			 */
			
			$scope.buildGOJS = function (decisionID, dependency){
				
				console.log("Guilding graph for " + $scope.selectedDecision + " - " + $scope.links[dependency].name);
				
				$scope.gojs = [{"data":[]}, {"relations":[]}];
				$scope.nodes = [];
				
				// add connection nodes and calculate the decision nodes candidates
				$.each($scope.serverresult.relations, function(idx, item){
					if(item.from  == decisionID && item.text == $scope.links[dependency].name){
						$scope.nodes.push(item.from);
						$scope.nodes.push(item.to);
						$scope.gojs[1].relations.push(item); // push to gojs.relations
					} 
					
				});
				
				// clean up and remove double values
				$scope.nodes = $.distinct($scope.nodes);
				
				// add nodes from the decision graph
				$.each($scope.serverresult.data, function(idx, item){
					if ($.inArray(item.key, $scope.nodes) >= 0){
						$scope.gojs[0].data.push(item); // push to gojs.relations
					} 
					
				});
				 
				console.log(angular.toJson($scope.nodes));
				console.log(angular.toJson($scope.gojs));
				
				// finally initiate graph
				$scope.model = new go.GraphLinksModel($scope.gojs[0].data,$scope.gojs[1].relations);
				$scope.model.selectedNodeData = null;
				console.log(angular.toJson($scope.model));
				
			}
			
			// fires when drop down menue with decisions is changed. updates the connections dropdown
			$scope.updateConnects = function () {
				console.log("Decision changed to " + $scope.selectedDecision);
				 $scope.selectedLink = {};
				 $scope.fillLinks($scope.selectedDecision);
			}
			
			// fires when drop down menue with connections is changed. updates the GoJS graph
			$scope.updateGraph = function () {
				console.log("Connection changed to " + $scope.selectedLink);
				$scope.buildGOJS ($scope.selectedDecision, $scope.selectedLink);
			}
			
			LoadConnectionsGraph.get({
				teamId : $cookies['TeamId']
			}, function(data) {
				console.log("graph json");
				console.log(data);
				$scope.serverresult = angular.fromJson(data);
				$.each($scope.serverresult.data, function(idx, item){
					if(item.key) {$scope.decisions.push({"id" : item.key, "name" : item.text});}
				});
				
				if ($scope.decisions.length == 0) {
					$scope.decisions.push({"id": 0, "name": "No decisions available"})};
				
			}, function(error) {
				console.log("Error: could not load graph from server!");
			});
			
			
		} ]);
