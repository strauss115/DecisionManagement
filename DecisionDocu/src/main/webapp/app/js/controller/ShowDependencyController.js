/**
 * Controller for the 'showDependency.html' - for reporting -> show dependencies -> replaced by the reporting feature!
 */
app.controller('ShowDependencyController', function($scope) {
	$scope.model = new go.GraphLinksModel([ {
		"key" : 1,
		"loc" : "330 120",
		"text" : "Entscheidung 2"
	}, {
		"key" : 2,
		"loc" : "226 376",
		"text" : "Entscheidung 3"
	} ], [ {
		"from" : 0,
		"to" : 1,
		"text" : "#influences",
		"curviness" : 20
	}, {
		"from" : 1,
		"to" : 2,
		"text" : "#hasSubDecision"
	}, {
		"from" : 2,
		"to" : 3,
		"text" : "#changeDecision"
	} ]);
	$scope.model.selectedNodeData = null;
});
