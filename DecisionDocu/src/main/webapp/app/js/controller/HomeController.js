app.controller('HomeController', ['$scope', '$cookies', '$location' ,  function ($scope, $cookies, $location) {
alert("Team: " + $cookies['TeamId'] + " Token: " + $cookies['Token']);
    }]);


