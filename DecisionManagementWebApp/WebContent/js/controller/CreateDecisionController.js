app.controller('CreateDecisionController', function ($scope) {
            $scope.model = new go.GraphLinksModel(
                    [
                        {"key": 0, "parent":"","text": "Entscheidung 1", "brush":"", "dir":"", "loc": "0 0"},
                        {"key": 1, "parent": 0, "text": "Gruppe", "brush": "skyblue", "dir": "right", "loc": "107 -22"},
                        {"key": 11, "parent": 1, "text": "Architekturentscheidung", "brush": "skyblue", "dir": "right", "loc": "200 -48"},
                        {"key": 2, "parent": 0, "text": "Lösungsalternative", "brush": "darkseagreen", "dir": "right", "loc": "107 43"},
                        {"key": 21, "parent": 2, "text": "Fat-Client", "brush": "darkseagreen", "dir": "right", "loc": "253 30"},
                        {"key": 3, "parent": 0, "text": "Einflussfaktor", "brush": "palevioletred", "dir": "left", "loc": "-20 -31.75"},
                        {"key": 31, "parent": 3, "text": "Know How", "brush": "palevioletred", "dir": "left", "loc": "-117 -64.25"},
                        {"key": 32, "parent": 3, "text": "Kosten", "brush": "palevioletred", "dir": "left", "loc": "-117 -25.25"}
                    ],
                    [
                        {from: 0, to: 1},
                        {from: 0, to: 2},
                        {from: 0, to: 3},
                        {from: 1, to: 11},
                        {from: 2, to: 21},
                        {from: 3, to: 31},
                        {from: 3, to: 32},
                    ]);
            $scope.model.selectedNodeData = null;
        });