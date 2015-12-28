app.controller('TeamAdministrationController', ['$scope',  '$cookies', '$location', 'Teams', function($scope, $cookies, $location, Teams) {
  //$scope.teams = Teams.query();
	$scope.teams = {};
  $scope.selectedTeam = 0;
  $scope.openRegisterModal = function (id) {
	  $scope.selectedTeam = id;
      $("#registerModal").modal();
  };
  
  $scope.register = function (id){
	  $cookies.TeamId = id;
	  $("#registerModal").modal('hide');
	  $('body').removeClass('modal-open');
	  $('.modal-backdrop').remove();
	  $location.path("/home");
	  
  }
  
  $scope.selectTeam = function (id){
	  $cookies.TeamId = id;
	  $location.path("/home");
  }
}]);

