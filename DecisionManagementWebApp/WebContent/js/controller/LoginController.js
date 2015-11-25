app.controller('LoginController', ['$scope', 'Login', function($scope, Login) {
  $scope.eMail = "";
  $scope.password = "";
  
  $scope.login = function(){
      Login.query();
  };
}]);
