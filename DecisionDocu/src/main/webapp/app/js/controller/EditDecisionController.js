/**
 * Controller for the 'editDecision.html' - load decisions and add attributes
 */
app
		.controller(
				'EditDecisionController',
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
						'GetMessages',
						'DeleteAttribute',
						'DeleteDecision',
						function($scope, $cookies, $routeParams, $window,
								LoadEditGraph, DecisionsByTeam,
								AddAttributeToDecision, GetNode, GetFile,
								fileUpload1, GetMessages, DeleteAttribute,
								DeleteDecision) {

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
							// init messages array
							$scope.messages = [];
							// init go-js-model
							$scope.model = new go.GraphLinksModel([], []);
							$scope.model.selectedNodeData = null;

							// function to update selected graph - draw selected
							// graph
							$scope.updateGraph = function() {
								// load chosen graph
								LoadEditGraph.get({
									id : $scope.selectedDecision
								}, function(data) {
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
										$scope.selectedDecision = obj[i].id;
									}
									$scope.decisions.push({
										"id" : obj[i].id,
										"name" : obj[i].name,
										"alternatives" : obj[i].alternatives
									});
								}
							}, function(error) {
							});

							// function to update selected graph - draw selected
							// graph
							$scope.updateGraph = function() {
								// alert($scope.selectedDecision);
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

								// Messages data
								GetMessages.get({
									id : $scope.selectedDecision
								}, function(data) {
									// alert("Get messages data:" + data);
									var obj = angular.fromJson(data);
									for (var i = 0; i < obj.length; i++) {

										$scope.messages.push({
											"id" : obj[i].id,
											"content" : obj[i].content,
											"date" : obj[i].dateString,
											"author" : obj[i].authorMail
										});

									}
								}, function(error) {
									// TODO error handling
								});

							}
							// load attributes from chosen category
							$scope.changeAttributeCategories = function() {
								// alert("Attribute choose changed" +
								// $scope.selectedAttribute);
								$scope.attributeValues.push({
									value : "test",
									label : "test"
								});
							}
							// load attributes category values to edit
							$scope.changeEditAttributeValues = function() {
								// alert("Attribute value choose changed: " +
								// $scope.selectedAttributeValue);

							}
							// function to add attributes to decision
							$scope.addDecisionAttribute = function() {
								// alert($scope.attributeValue);
								AddAttributeToDecision.save({
									value : $scope.attributeValue,
									id : $scope.selectedDecision,
									attribute : $scope.selectedAttribute
								}, function(data) {
									// alert(data);
									$scope.updateGraph();
								}, function(error) {
								});
							}

							// function to delete attributes from decision
							$scope.deleteDecisionAttribute = function() {
								// alert($scope.attributeValue);
								DeleteAttribute.put({
									id : $scope.selectedDecision,
									attrId : $scope.selectedAttribute
								}, function(data) {
									// alert(data);
									$scope.updateGraph();
								}, function(error) {
								});
							}

							// function to delete decision
							$scope.deleteDecision = function() {
								DeleteDecision.get({
									id : $scope.selectedDecision
								});

								var i = 0, len = $scope.decisions.length, index = 0;
								for (; i < len; i++) {
									if ($scope.decisions[i].id == $scope.selectedDecision) {
										index = i;
									}
								}
								$scope.decisions.splice(index, 1);
								$scope.selectedDecision = $scope.decisions[0];
								$scope.updateGraph();
							}

							// full screen mode
							$scope.openFullScreenPanel = function() {
								jQuery("#fullScreenPanel").modal();
							}
							// load files of node
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
														+ ";base64,"
														+ data.data
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

							// add attribute to decision - after
							// panel-save-click
							$scope.addDecisionAttributeInPanelClick = function() {
								var att = $("#headlineAddAttributePanel")
										.text().trim().split(" ")[1];
								if ($("#headlineAddAttributePanel").text()
										.trim().split(" ").length > 2) {
									att = $("#headlineAddAttributePanel")
											.text().trim().split(" ")[1]
											+ $("#headlineAddAttributePanel")
													.text().trim().split(" ")[2];
								}
								if ($("#headlineAddAttributePanel").text()
										.trim().split(" ")[0] == "Add") {
									//alert($scope.selectedDecision);
									AddAttributeToDecision
											.save(
													{
														value : $scope.decisionAttributeValueFromPanel,
														id : $scope.selectedDecision,
														attribute : "add" + att
													}, function(data) {
														// alert(data);
													}, function(error) {
													});
								}
								if ($("#headlineAddAttributePanel").text()
										.trim().split(" ")[0] == "Edit") {
									//alert("EDIT - TO-DO - call edit service");
								}
							}
							// upload file
							$scope.uploadFile = function(id) {
								var file = $scope.myFile1;
								if (file !== undefined) {
									var uploadUrl = serverAddress
											+ "/DecisionDocu/api/upload/document/"
											+ $scope.NodeId;
									fileUpload1.uploadFileToUrl(file,
											uploadUrl, $scope);

								}
							};

							// Hide if no decision is selected
							$scope.isHidden = function() {
								if ($scope.selectedDecision == "") {
									return true;
								} else {
									return false;
								}
							}

						} ]);