app.controller('RelateDecisionController', function ($scope) {
    $scope.model = new go.GraphLinksModel(
            [
                {"key": 0, "loc": "120 120", "text": "Entscheidung 1"},
                {"key": 1, "loc": "330 120", "text": "Entscheidung 2"},
                {"key": 2, "loc": "226 376", "text": "Entscheidung 3"},
                {"key": 3, "loc": "-70 180", "text": "Entscheidung 4"},
                {"key": 4, "loc": "-30 350", "text": "Entscheidung 5"}],
            [
                {"from": 0, "to": 1, "text": "#influences", "curviness": 20},
                {"from": 1, "to": 2, "text": "#hasSubDecision"},
                {"from": 2, "to": 3, "text": "#changeDecision"}
            ]);
    $scope.model.selectedNodeData = null;
});