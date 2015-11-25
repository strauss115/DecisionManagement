app.controller('UserAdministrationController', ['$scope', 'Users', function($scope, Users) {
  $scope.users = Users.query();
}]);