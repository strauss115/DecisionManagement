app.controller('TeamAdministrationController', ['$scope', 'Teams', function($scope, Teams) {
  $scope.teams = Teams.query();
}]);

$('#teamAdministrationClear').click(function(){
    alert("clear");
});

$('#teamAdministrationSave').click(function(){
    
});