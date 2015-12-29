app.controller('UserAdministrationController', ['$scope', 'Users', function($scope, Users) {
  $scope.users = Users.query();
}]);

$('#userAdministrationClear').click(function(){
    alert("clear");
});

$('#userAdministrationSave').click(function(){
    
});