app.controller('LoginController', ['$scope', 'Login', function ($scope, Login) {
        $scope.eMail = "";
        $scope.password = "test";
        $scope.token1 = Login.query1();
        
        //alert($scope.token1);
        $scope.login = function () {

             $scope.token = (Login.query({eMail: $scope.eMail, password: $scope.password}));
       
            alert($scope.token.toJSON());
            alert($scope.token1);
        }
    }]);


