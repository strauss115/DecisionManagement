app.controller('LoginController', ['$scope', '$cookies', '$location', 'Login', 'Register', function ($scope, $cookies, $location, Login, Register) {
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
            }, function (error) {
                $("#loginErrorModal").modal();
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

                    $location.path("/login");

                }, function (error) {
                    $("#emailError").show();
                });

            }
        }
    }]);


