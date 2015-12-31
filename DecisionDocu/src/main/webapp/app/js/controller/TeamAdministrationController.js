app.controller('TeamAdministrationController', [ '$scope', '$cookies',
		'$location', 'TeamPerId', 'Teams', 'UserPerMail',
		function($scope, $cookies, $location, TeamPerId, Teams, UserPerMail) {
			$scope.teams = [];
			$scope.openTeams = [];
			$scope.userId = 0;

			$scope.getTeams = function() {
				UserPerMail.get({
					mail : $cookies['Mail']
				}, function(data) {
					var arr = new Array();
					arr = data['teams'];
					$scope.userId = data['id'];
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
					$scope.getOpenTeams();
				}, function(error) {
				});

			};

			$scope.getOpenTeams = function() {
				Teams.get({}, function(data) {
					var arr = new Array();
					arr = data;

					for (var i = 0; i < arr.length; i++) {
						if (arr[i]['users'].indexOf($scope.userId) == -1) {
							$scope.openTeams.push({
								"id" : arr[i]['id'],
								"name" : arr[i]['name']
							});
						}
					}
				}, function(error) {
				});
			}

			$scope.getTeams();

			$scope.selectedTeam = "";
			$scope.selectedTeamId = 0;
			$scope.openRegisterModal = function(id, name) {
				$scope.selectedTeam = name;
				$scope.selectedTeamId = id;
				$("#registerModal").modal();
			};

			$scope.register = function(id) {
				$cookies.TeamId = id;
				$("#registerModal").modal('hide');
				$('body').removeClass('modal-open');
				$('.modal-backdrop').remove();
				$location.path("/home");

			}

			$scope.selectTeam = function(id) {
				$cookies.TeamId = id;
				$location.path("/home");
			}
		} ]);
