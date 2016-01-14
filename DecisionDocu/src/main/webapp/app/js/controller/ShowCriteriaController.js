/**
 * APA
 * Controller for the 'showCriteria.html'
 */


app.controller('ShowCriteriaController', [
                          				'$scope',
                        				'$cookies',
                        				'$routeParams',
                        				'$window',
                        				'$location',
                        				'LoadEditGraph',
                        				'LoadConnectionsGraph',
                        				'DecisionsByTeam',
                        				'AddAttributeToDecision',
                        				'GetNode',
                        				'GetFile',
                        				'fileUpload1',
                        				'GetMessages',
                        				function($scope, $cookies, $routeParams, $window, $location,
                        						LoadEditGraph, LoadConnectionsGraph, DecisionsByTeam, AddAttributeToDecision,
                        						GetNode, GetFile, fileUpload1, GetMessages) {

			/*
			 * Dummy filter criteria for initial testing
			 */
			$scope.alternatives = [{"id":0, "name": "alt1"}, {"id":1, "name": "alt2"}, {"id":2, "name": "alt3"}]; //1
			$scope.quality = [{"id":0, "name": "qa1"}, {"id":1, "name": "qa2"}, {"id":2, "name": "qa3"}];
			$scope.factors = [{"id":0, "name": "f 1"}, {"id":1, "name": "f 2"}, {"id":2, "name": "f 3"}];
			$scope.consequences = [{"id":0, "name": "c 1"}, {"id":1, "name": "c 2"}, {"id":2, "name": "c 3"}];
			$scope.rationales = [{"id":0, "name": "rat1"}, {"id":1, "name": "group2"}, {"id":2, "name": "group3"}];
		
			$scope.alternativesTmp = [];
			$scope.qualityTmp = [];
			$scope.factorsTmp = [];
			$scope.consequencesTmp = [];
			$scope.rationalesTmp = [];
			
			
			$scope.decisions = []; //contains complete dataset from server - DecisionsByTeam.get
			$scope.decisionsCompact = []; //contains only id and name from $scope.decisions
			$scope.decisionsResult = []; //represents the table contents after filtering
			$scope.decisionsIndexed = []; //contains extended properties
			
			
			
			
			// on "edit" click in the decisions table
			$scope.navigate = function(id){
				$location.path("/editDecision/" + id);
			}
			
			$scope.updateFilter = function(){
				
				$scope.alternatives =  [{"id":1000, "name": "any"}];
				$scope.quality =  [{"id":1000, "name": "any"}];
				$scope.factors =   [{"id":1000, "name": "any"}];
				$scope.consequences =  [{"id":1000, "name": "any"}];
				$scope.rationales =  [{"id":1000, "name": "any"}];
				
				for (var i = 0; i < $scope.alternativesTmp.length; i++) {
					$scope.alternatives.push({"id":i, "name":$scope.alternativesTmp[i]});
				}
				for (var i = 0; i < $scope.factorsTmp.length; i++) {
					$scope.factors.push({"id":i, "name":$scope.factorsTmp[i]});
				}
				for (var i = 0; i < $scope.consequencesTmp.length; i++) {
					$scope.consequences.push({"id":i, "name":$scope.consequencesTmp[i]});
				}
				for (var i = 0; i < $scope.qualityTmp.length; i++) {
					$scope.quality.push({"id":i, "name":$scope.qualityTmp[i]});
				}
				for (var i = 0; i < $scope.rationalesTmp.length; i++) {
					$scope.rationales.push({"id":i, "name":$scope.rationalesTmp[i]});
				}
				
			}
			
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
			$scope.showResult = function(){
				
				$scope.decisionsResult = [];
				
				//console.log("Search button pressed");
				//console.log(angular.toJson($scope.decisionsIndexed));
				
				var searchstring = [];
				
				($scope.selectedAlternative && ($scope.selectedAlternative *1) < 1000) ? searchstring.push($scope.alternatives[(($scope.selectedAlternative *1) +1)].name): searchstring.push("");
				($scope.selectedQuality && ($scope.selectedQuality *1) < 1000) ? searchstring.push($scope.quality[(($scope.selectedQuality *1) +1)].name): searchstring.push("");
				($scope.selectedInfluence && ($scope.selectedInfluence *1) < 1000) ? searchstring.push($scope.factors[(($scope.selectedInfluence *1) +1)].name): searchstring.push("");
				($scope.selectedConsequence && ($scope.selectedConsequence *1) < 1000) ? searchstring.push($scope.consequences[(($scope.selectedConsequence *1) +1)].name): searchstring.push("");
				($scope.selectedRationales && ($scope.selectedRationales *1) < 1000) ? searchstring.push($scope.rationales[(($scope.selectedRationales *1) +1)].name): searchstring.push("");
				
				for (var d = 0; d < $scope.decisionsIndexed.length; d++){
					
					var success = true;
					for(var i=0; i<=4; i++){
						if (searchstring[i].length > 1){
							//console.log(searchstring[i] + " is long enough");
							//console.log("Position: " + $.inArray(searchstring[i], $scope.decisionsIndexed[d].props[i]));
							if ($.inArray(searchstring[i], $scope.decisionsIndexed[d].props[i]) == -1) success = false;
						}
						//console.log("id: " + $scope.decisionsIndexed[d].name + "  --- search for: " +  searchstring[i] + "  --- found:  " + $scope.decisionsIndexed[d].props[i]);
					}
					
					if (success){
						$scope.decisionsResult.push({"id":$scope.decisionsIndexed[d].id, "name":$scope.decisionsIndexed[d].name});
					}
					//console.log (success + " : " + $scope.decisionsIndexed[d].name);
				}
				 

					//console.log("Filter selectedAlternative: " + $scope.alternatives[(($scope.selectedAlternative *1) +1)].name);
					
				//console.log(searchstring);
				
				
				/*if($scope.selectedQuality) console.log("Filter selectedQuality" + $scope.quality[$scope.selectedQuality+1].name);
				if($scope.selectedInfluence) console.log("Filter selectedFactors" + $scope.factors[$scope.selectedInfluence+1].name);
				if($scope.selectedConsequence) console.log("Filter selectedConsequences" + $scope.consequences[$scope.selectedConsequence+1].name);
				if($scope.selectedRationales) console.log("Filter selectedRationales" + $scope.rationales[$scope.selectedRationales+1].name);
*/
			}

			/*
			 *  1. updateAlternativeFilter - fires when dropdown menu with Group filter has been changed
			 */		
			$scope.updateAlternativeFilter = function(){
				//console.log("Alternative filter changed to " + $scope.selectedAlternative);
				$scope.showResult();
			}
			
			/*
			 *  2. updateQualityFilter - fires when dropdown menu with Quality attribute filter has been changed
			 */		
			$scope.updateQualityFilter  = function(){
				//console.log("Quality attribute filter changed to " + $scope.selectedQuality);
				$scope.showResult();
			}	
	
			
			/*
			 *  3. updateInfluenceFilter - fires when dropdown menu with Influence Factors filter has been changed
			 */	
			$scope.updateInfluenceFilter  = function(){
				//console.log("Influence filter changed to " + $scope.selectedInfluence);
				$scope.showResult();
			}
			
			
			/*
			 *  4. updateConsequenceFilter - fires when dropdown menu with Consequence filter has been changed
			 */		
			$scope.updateConsequenceFilter  = function(){
				//console.log("Consequence filter changed to " + $scope.selectedConsequence);
				$scope.showResult();
			}

			/*
			 *  5. updateRationaFilter - fires when dropdown menu with Consequence filter has been changed
			 */		
			$scope.updateRationalesFilter  = function(){
				//console.log("Rationales filter changed to " + $scope.selectedRationales);
				$scope.showResult();
			}		

			
			/*
			 *  initially loads decision filter criteria via LoadConnectionsGraph service
			 */			
			$scope.initiateFilters = function(){
				
				$.each($scope.decisions, function(idx, item){
					if(item.name && item.id){
						$scope.decisionsCompact.push({"id":item.id, "name":item.name});	
						$scope.decisionsResult.push({"id":item.id, "name":item.name});	
					}
				});
				
				$.each($scope.decisionsCompact, function(idx, item){
					
					var props = [[],[],[],[],[]];
					
					LoadEditGraph.get({id : item.id}, 
						function(data) {
						
						
						$.each(data, function(id, node){
							console.log("analyzing node " + item.id);
							if(node.parent == "alt") {
								$scope.alternativesTmp.push(node.text); 
								$scope.alternativesTmp = $.distinct($scope.alternativesTmp); 
								$scope.updateFilter();
								props[0].push(node.text);
							}
							if (node.parent == "con") {
								$scope.consequencesTmp.push(node.text);
								$scope.consequencesTmp = $.distinct($scope.consequencesTmp);
								$scope.updateFilter();
								props[3].push(node.text);
							}
							if (node.parent == "qua") {
								$scope.qualityTmp.push(node.text);
								$scope.qualityTmp = $.distinct($scope.qualityTmp);
								$scope.updateFilter();
								props[1].push(node.text);
							}
							if (node.parent == "rat") {
								$scope.rationalesTmp.push(node.text);
								$scope.rationalesTmp = $.distinct($scope.rationalesTmp);
								$scope.updateFilter();
								props[4].push(node.text);
							}
							if (node.parent == "inf") {
								$scope.factorsTmp.push(node.text);
								$scope.alternativesTmp = $.distinct($scope.alternativesTmp);
								$scope.updateFilter();
								props[2].push(node.text);
							}
						});
						
						console.log("alt: " + angular.toJson($scope.alternativesTmp));
						console.log("con: " + angular.toJson($scope.consequencesTmp));
						console.log("qua: " + angular.toJson($scope.qualityTmp));
						console.log("rat: " + angular.toJson($scope.rationalesTmp));
						console.log("inf: " + angular.toJson($scope.factorsTmp));
			            //console.log("Log " + idx + " for id " + item.id);
						//console.log(angular.toJson(data));
					},
					 function(error) {
						console.log("Error while loading graph data for decision");
					});
					
					$scope.decisionsIndexed.push({"id":item.id, "name": item.name, "props": props});
					
				}); // end for each
				
				
				//console.log(angular.toJson($scope.decisionsCompact));

	
				 
			}
			
			
			/*
			 *  load decision names from server and store in $scope.decisions variable
			 */	
			DecisionsByTeam.get({teamId : $cookies['TeamId']}, function(data) {
				
				$scope.decisions = angular.fromJson(data);
				$scope.initiateFilters();

			}, function(error) {
				console.log("An error has occured. No data loaded for criteria search.");
			});
			
			
		} ]);
