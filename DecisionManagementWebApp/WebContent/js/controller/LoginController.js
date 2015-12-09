app.controller('LoginController', ['$scope', '$rootScope', '$cookies', '$location', 'Login', 'Register', function ($scope, $rootScope, $cookies, $location, Login, Register) {
        $scope.eMail = "";
        $scope.password = "";
        $scope.loginResponse = "";

        $scope.signInEmail = "";
        $scope.password1 = "";
        $scope.password2 = "";
        $scope.firstname = "";
        $scope.lastname = "";

        $scope.login = function () {
            $scope.token = Login.query({eMail: $scope.eMail, password: $scope.password}, function (data) {
                $scope.loginResponse = (data);
                $cookies.Token = $scope.loginResponse.token;
                $location.path("/home");
                $rootScope.login = true;
            }, function (error) {
                $("#loginErrorModal").modal();
                $rootScope.login = false;
            });
        };

        $scope.openRegisterModal = function () {
            $("#registerModal").modal();
        };

        $scope.register = function () {
            $("#emailError").hide();
            $("#passwordError").hide();

            if ($scope.password1 != $scope.password2) {
                $("#passwordError").show();
                return false;
            } else {
                $("#passwordError").hide();
                Register.query({eMail: $scope.signInEmail, password: $scope.password1, firstName: $scope.firstname, lastName: $scope.lastname}, function (data) {
                    $("#registerModal").modal('hide');
                }, function (error) {
                    $("#emailError").show();
                });

            }
        }
    }]);


