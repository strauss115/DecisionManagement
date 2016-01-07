/**
 * Controller for the login feature - set cookies and authenticate user 
 */
app.controller('LoginController', [ '$scope', '$rootScope', '$cookies',
		'$location', 'Login', 'Register',
		function($scope, $rootScope, $cookies, $location, Login, Register) {
			$scope.eMail = "";
			$scope.password = "";
			$scope.loginResponse = "";

			$scope.signInEmail = "";
			$scope.password1 = "";
			$scope.password2 = "";
			$scope.firstname = "";
			$scope.lastname = "";
			// login method - authenticate against backend
			$scope.login = function() {

				Login.get({
					"eMail" : $scope.eMail,
					"password" : $scope.password
				}, {}, function(data) {
					$scope.loginResponse = (data);
					$cookies.Token = $scope.loginResponse.access_token;
					$cookies.Mail = $scope.eMail;
					$location.path("/selectTeam");
					$rootScope.login = true;
				}, function(error) {
					$("#loginErrorModal").modal();
					$rootScope.login = false;
				});
			};
			// method to open register modal
			$scope.openRegisterModal = function() {
				$("#registerModal").modal();
			};
			// method to register user
			$scope.register = function() {
				$("#emailError").hide();
				$("#passwordError").hide();

				if ($scope.password1 != $scope.password2) {
					$("#passwordError").show();
					return false;
				} else {
					$("#passwordError").hide();

					Register.save({
						"eMail" : $scope.signInEmail,
						"password" : $scope.password1,
						"firstName" : $scope.firstname,
						"lastName" : $scope.lastname
					}, {}, function(data) {
						$("#registerModal").modal('hide');
					}, function(error) {
						$("#emailError").show();
					});

				}
			}
		} ]);
