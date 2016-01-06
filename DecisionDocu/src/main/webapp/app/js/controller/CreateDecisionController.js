app.controller('CreateDecisionController', [
	'$scope',
	'$cookies',
	'CreateDecision',
	'AddAttributeToDecision',
	function ($scope, $cookies, CreateDecision, AddAttributeToDecision) {
		$scope.showGoJsGraph = true;
		$scope.model = new go.Model.fromJson({"class": "go.TreeModel",
	        "nodeDataArray": []});
	    $scope.model.selectedNodeData = null;
	    $scope.selectedDecisionId = "";
	    // method to create decision
	    $scope.createDecision = function() {
	    	if($scope.decisionName.length > 3){
	    		$scope.showGoJsGraph = false;
	    		$scope.model = new go.Model.fromJson({"class": "go.TreeModel",
	    	        "nodeDataArray": [
	    	            {"key": "node", "text": $scope.decisionName, "loc": "0 0", editable: false, showAdd: false},
	    	            {"key": "inf", "parent": "node", "text": "Influence Factors", "brush": "skyblue", "dir": "right", "loc": ($scope.decisionName.length * 7) + " -50", showAdd: true, editable: false},
	    	            {"key": "rat", "parent": "node", "text": "Rationales", "brush": "darkseagreen", "dir": "right", "loc": ($scope.decisionName.length * 7) + " 50", showAdd: true, editable: false},
	    	            {"key": "alt", "parent": "node", "text": "Alternatives", "brush": "palevioletred", "dir": "left", "loc": "-20 -100",showAdd: true,editable: false},
	    	            {"key": "con", "parent": "node", "text": "Consequences", "brush": "coral", "dir": "left", "loc": "-20 0",showAdd: true,editable: false},
	    	            {"key": "qua", "parent": "node", "text": "Quality Attributes", "brush": "grey", "dir": "left", "loc": "-20 100",showAdd: true,editable: false}]});

	    		CreateDecision.save({
	    			decisionName : $scope.decisionName
				}, function(data) {
					alert(data);
					var obj = angular.fromJson(data);
					alert(obj.id);
					$scope.selectedDecisionId = obj.id;
				}, function(error) {
				});
	    	}
	    	else{
	    		alert("Decision name to short!")
	    	}
		}

		// function to add attributes to decision
		$scope.addDecisionAttribute = function() {
			//alert($scope.selectedDecisionId);
			//alert($scope.selectedAttribute);
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
		// add attribute to decision - after panel-save-click
		$scope.addDecisionAttributeInPanelClick = function(){
			//alert($scope.decisionAttributeValueFromPanel);
			//alert("add" + $("#headlineAddAttributePanel").text().trim().split(" ")[1]);
			var att = $("#headlineAddAttributePanel").text().trim().split(" ")[1];
			if($("#headlineAddAttributePanel").text().trim().split(" ").length > 2){
				att = $("#headlineAddAttributePanel").text().trim().split(" ")[1] + $("#headlineAddAttributePanel").text().trim().split(" ")[2];
			}
			if($("#headlineAddAttributePanel").text().trim().split(" ")[0] == "Add"){
				AddAttributeToDecision.save({
					value : $scope.decisionAttributeValueFromPanel,
					id : $scope.selectedDecisionId,
					attribute : "add" + att
				}, function(data) {
					alert(data);
					//$scope.updateGraph();
				}, function(error) {
				});
			}

			if($("#headlineAddAttributePanel").text().trim().split(" ")[0] == "Edit"){
				alert("EDIT - TO-DO - call edit service");
			}
		}
	}]);
