app.controller('LoginController', ['$scope', '$cookies', '$location' ,'Login',  function ($scope, $cookies, $location, Login) {
        $scope.eMail = "";
        $scope.password = "";
        $scope.loginResponse = "";

        $scope.login = function () {
            $scope.token = Login.query({eMail: $scope.eMail, password: $scope.password}, function (data) {
                $scope.loginResponse = (data);
                $cookies.Token = $scope.loginResponse.token;
                
                $location.path("/createDecision");
     
            });        
        }
    }]);


