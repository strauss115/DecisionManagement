app.controller('UserAdministrationController', ['$scope', 'Users', function($scope, Phone) {
  $scope.users = Users.query();
}]);