app.controller('TeamAdministrationController', ['$scope', '$rootScope', '$cookies', '$location', 'Teams', function($scope, $rootScope, $cookies, $location, Teams) {
  $scope.teams = Teams.query();
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
	  $scope.$apply();
  }
}]);

