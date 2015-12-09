app.controller('LogoutController', ['$scope', '$rootScope','$cookies', '$location', function ($scope,$rootScope, $cookies, $location) {
        $cookies.Token = null;
        $rootScope.login = false;
        $location.path("/login");
    }]);


