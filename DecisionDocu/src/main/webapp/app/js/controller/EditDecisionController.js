app.controller('EditDecisionController',
		[
				'$scope',
				'$cookies',
				'$routeParams',
				'$window',
				'LoadEditGraph',
				'DecisionsByTeam',
				'AddAttributeToDecision',
				'GetNode',
				'GetFile',
				'fileUpload1',
				function($scope, $cookies, $routeParams, $window,
						LoadEditGraph, DecisionsByTeam, AddAttributeToDecision,
						GetNode, GetFile, fileUpload1) {

					// init attributes
					$scope.attributes = [ {
						value : 'addAlternative',
						label : '#Alternative'
					}, {
						value : 'addConsequence',
						label : '#Consequence'
					}, {
						value : 'addInfluenceFactor',
						label : '#Influence Factor'
					}, {
						value : 'addRationale',
						label : '#Rationale'
					}, {
						value : 'addQualityAttribute',
						label : '#Quality Attribute'
					} ];
					// init attribute categories values
					$scope.attributeValues = [];

					// init decision array
					$scope.decisions = [];
					// init go-js-model
					$scope.model = new go.GraphLinksModel([], []);
					$scope.model.selectedNodeData = null;

					// function to update selected graph - draw selected graph
					$scope.updateGraph = function() {
						alert($scope.selectedDecision);
						// load chosen graph
						LoadEditGraph.get({
							id : $scope.selectedDecision
						}, function(data) {
							// alert(data);
							$scope.model = new go.Model.fromJson({
								"class" : "go.TreeModel",
								"nodeDataArray" : data
							}), $scope.model.selectedNodeData = null;
						}, function(error) {
						});
					}

					$scope.selectedAttribute = "";
					$scope.selectedAttribute = "";

					$scope.downloadFiles = new Array();

					if ($routeParams.id !== undefined) {
						$scope.selectedDecision = $routeParams.id;
						$scope.updateGraph();
					} else {
						$scope.selectedDecision = "";
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
								$scope.decisions.push({
									"id" : obj[i].id,
									"name" : obj[i].name,
									"alternatives" : obj[i].alternatives
								});
								// alert("alternatives count in decision: " +
								// obj[i].alternatives.length);
								// alert("id of first alternative: " +
								// obj[i].alternatives[0].id);
							}
							/*
							if (firstElementId != "") {
								// load the first graph
								LoadEditGraph.get({
									id : firstElementId
								}, function(data) {
									// alert(data);
									$scope.model = new go.Model.fromJson({
										"class" : "go.TreeModel",
										"nodeDataArray" : data
									}), $scope.model.selectedNodeData = null;
								}, function(error) {
								});
							}*/
						}, function(error) {
						});
					

					// function to update selected graph - draw selected graph
					$scope.updateGraph = function() {
						alert($scope.selectedDecision);
						// load chosen graph
						LoadEditGraph.get({
							id : $scope.selectedDecision
						}, function(data) {
							// alert(data);
							$scope.model = new go.Model.fromJson({
								"class" : "go.TreeModel",
								"nodeDataArray" : data
							}), $scope.model.selectedNodeData = null;
						}, function(error) {
						});
					}
					// load attributes from chosen category
					$scope.changeAttributeCategories = function() {
						alert("Attribute choose changed"
								+ $scope.selectedAttribute);
						/*
						 * $scope.decisions.push({ "id" : obj[i].id, "name" :
						 * obj[i].name, "alternatives" : obj[i].alternatives });
						 */
						$scope.attributeValues.push({
							value : "test",
							label : "test"
						});
					}
					// load attributes category values to edit
					$scope.changeEditAttributeValues = function() {
						alert("Attribute value choose changed: "
								+ $scope.selectedAttributeValue);

					}
					// function to add attributes to decision
					$scope.addDecisionAttribute = function() {
						// alert($scope.selectedDecisionId);
						// alert($scope.selectedAttribute);
						alert($scope.attributeValue);
						AddAttributeToDecision.save({
							value : $scope.attributeValue,
							id : $scope.selectedDecisionId,
							attribute : $scope.selectedAttribute
						}, function(data) {
							alert(data);
							$scope.updateGraph();
						}, function(error) {
						});
					}
					// full screen mode
					$scope.openFullScreenPanel = function() {
						jQuery("#fullScreenPanel").modal();
					}

					$scope.loadNode = function(id) {
						$scope.downloadFiles = new Array();
						$scope.NodeId = id;
						GetNode.get({
							"id" : id
						}, function(data) {
							var arr = data.relationships.hasDocuments;
							for (var i = 0; i < arr.length; i++) {
								var fileId = arr[i].relatedNode.id;
								GetFile.get({
									"id" : fileId
								}, function(data) {

									$scope.downloadFiles.push({
										"name" : data.name,
										"url" : "data:" + data.type
												+ ";base64," + data.data
									});
								}, function(error) {
								});
							}
						}, function(error) {
						});
					}

					$scope.open = function(url) {
						$window.open(url);
					}

					// add attribute to decision - after panel-save-click
					$scope.addDecisionAttributeInPanelClick = function() {
						// alert($scope.decisionAttributeValueFromPanel);
						// alert("add" +
						// $("#headlineAddAttributePanel").text().trim().split("
						// ")[1]);
						var att = $("#headlineAddAttributePanel").text().trim()
								.split(" ")[1];
						if ($("#headlineAddAttributePanel").text().trim()
								.split(" ").length > 2) {
							att = $("#headlineAddAttributePanel").text().trim()
									.split(" ")[1]
									+ $("#headlineAddAttributePanel").text()
											.trim().split(" ")[2];
						}
						if ($("#headlineAddAttributePanel").text().trim()
								.split(" ")[0] == "Add") {
							AddAttributeToDecision.save({
								value : $scope.decisionAttributeValueFromPanel,
								id : $scope.selectedDecisionId,
								attribute : "add" + att
							}, function(data) {
								alert(data);
								// $scope.updateGraph();
							}, function(error) {
							});
						}
						if ($("#headlineAddAttributePanel").text().trim()
								.split(" ")[0] == "Edit") {
							alert("EDIT - TO-DO - call edit service");
						}
					}

					$scope.uploadFile = function(id) {
						var file = $scope.myFile1;
						if (file !== undefined) {
							var uploadUrl = serverAddress
									+ "/DecisionDocu/api/upload/document/"
									+ $scope.NodeId;
							fileUpload1
									.uploadFileToUrl(file, uploadUrl, $scope);

						}
					};
					
					//Hide if no decision is selected
					$scope.isHidden = function(){
						if ($scope.selectedDecision==""){
							return true;
						}else{
							return false;
						}
					}
				} ]);