app.controller('LogoutController', ['$scope', '$cookies', '$location', function ($scope, $cookies, $location) {



        $cookies.Token = null;

        $location.path("/login");


    }]);


