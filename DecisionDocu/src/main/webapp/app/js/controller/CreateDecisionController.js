app.controller('CreateDecisionController', function ($scope) {
    $scope.model = new go.Model.fromJson({"class": "go.TreeModel",
        "nodeDataArray": [
            {"key": 0, "text": "Mind Map", "loc": "0 0", editable: false, showAdd: false},
            {"key": 1, "parent": 0, "text": "Influence Factors", "brush": "skyblue", "dir": "right", "loc": "77 -22", editable: false, showAdd: true},
            {"key": 11, "parent": 1, "text": "IF 1", "brush": "skyblue", "dir": "right", "loc": "200 -48", showAdd: false, editable: true},
            {"key": 12, "parent": 1, "text": "IF 2", "brush": "skyblue", "dir": "right", "loc": "200 -22", showAdd: false, editable: true},
            {"key": 13, "parent": 1, "text": "IF 3", "brush": "skyblue", "dir": "right", "loc": "200 4", showAdd: false, editable: true},
            {"key": 2, "parent": 0, "text": "Rationals", "brush": "darkseagreen", "dir": "right", "loc": "77 43", showAdd: true, editable: false},
            {"key": 21, "parent": 2, "text": "R1", "brush": "darkseagreen", "dir": "right", "loc": "203 30", showAdd: false,editable: true},
            {"key": 22, "parent": 2, "text": "R2", "brush": "darkseagreen", "dir": "right", "loc": "203 56", showAdd: false,editable: true},
            {"key": 3, "parent": 0, "text": "Alternatives", "brush": "palevioletred", "dir": "left", "loc": "-20 -31.75",showAdd: true,editable: false},
            {"key": 31, "parent": 3, "text": "A1", "brush": "palevioletred", "dir": "left", "loc": "-117 -64.25", showAdd: false,editable: true},
            {"key": 32, "parent": 3, "text": "A2", "brush": "palevioletred", "dir": "left", "loc": "-117 -25.25", showAdd: false,editable: true},
            {"key": 33, "parent": 3, "text": "A3", "brush": "palevioletred", "dir": "left", "loc": "-117 0.75", showAdd: false,editable: true},
            {"key": 4, "parent": 0, "text": "Quality Attributes", "brush": "coral", "dir": "left", "loc": "-20 52.75",showAdd: true,editable: false},
            {"key": 41, "parent": 4, "text": "QA1", "brush": "coral", "dir": "left", "loc": "-103 26.75", showAdd: false,editable: true},
            {"key": 42, "parent": 4, "text": "QA2", "brush": "coral", "dir": "left", "loc": "-103 52.75", showAdd: false,editable: true},
            {"key": 43, "parent": 4, "text": "QA3", "brush": "coral", "dir": "left", "loc": "-103 78.75", showAdd: false,editable: true}]});
    $scope.model.selectedNodeData = null;
});
