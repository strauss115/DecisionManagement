/**
 * Controller for the 'teamAdministration.html' - load teams + react on
 * selection
 */
app.controller('TeamAdministrationController', [
		'$scope',
		'$cookies',
		'$location',
		'TeamPerId',
		'Teams',
		'UserPerMail',
		'RegisterTeam',
		function($scope, $cookies, $location, TeamPerId, Teams, UserPerMail,
				RegisterTeam) {
			$scope.teams = [];
			$scope.openTeams = [];

			$cookies.TeamId = null;
			// load teams from backend
			$scope.getTeams = function() {
				UserPerMail.get({
					mail : $cookies['Mail']
				}, function(data) {
					var arr = new Array();
					arr = data['teams'];
					$cookies.UserId = data['id'];
					if(arr != null){
						for (var i = 0; i < arr.length; i++) {
	
							TeamPerId.get({
								teamId : arr[i]
							}, function(data1) {
								$scope.teams.push({
									"id" : data1['id'],
									"name" : data1['name']
								});
							}, function(error) {
							});
						}
					}
					$scope.getOpenTeams();
				}, function(error) {
				});

			};
			// load teams where the user is not registered
			$scope.getOpenTeams = function() {
				Teams.get({}, function(data) {
					var arr = new Array();
					arr = data;
					if(arr != null){
						for (var i = 0; i < arr.length; i++) {
							if (arr[i]['users'].indexOf($cookies.UserId) == -1) {
								$scope.openTeams.push({
									"id" : arr[i]['id'],
									"name" : arr[i]['name']
								});
							}
						}
					}
				}, function(error) {
				});
			}

			$scope.getTeams();

			$scope.selectedTeam = "";
			$scope.selectedTeamId = 0;
			// function - open register
			$scope.openRegisterModal = function(id, name) {
				$scope.selectedTeam = name;
				$scope.selectedTeamId = id;
				$("#registerModal").modal();
			};
			// function - register for team
			$scope.register = function(id) {
				$cookies.TeamId = id;
				RegisterTeam.save({
					"teamId" : $cookies['TeamId'],
					"userId" : $cookies['UserId'],
					'password' : $scope.teamPassword
				}, {}, function(data) {
					alert(data.status);
					$("#registerModal").modal('hide');
					$('body').removeClass('modal-open');
					$('.modal-backdrop').remove();
					$location.path("/home");
				}, function(error) {
					alert(error);
					$("#teamPasswordError").modal();
				});
			}
			// select team
			$scope.selectTeam = function(id) {
				$cookies.TeamId = id;
				$location.path("/home");
			}
		} ]);
