/**
 * Controller for the 'userProfile.html' - load user data + change user data (personal information and user pic)
 */
app.controller('UserProfileController', [
		'$scope',
		'$cookies',
		'fileUpload',
		'UserPerMail',
		'UserPicture',
		'UpdateLastName',
		'UpdateFirstName',
		function($scope, $cookies, fileUpload, UserPerMail, UserPicture,
				UpdateLastName, UpdateFirstName) {
			$scope.id = "";
			$scope.email = "";
			$scope.firstname = "";
			$scope.lastname = "";
			$scope.profilPictureId = "";
			$scope.teams = new Array();
			// load user which is logged in
			$scope.loadUser = function() {
				UserPerMail.get({
					mail : $cookies['Mail']
				}, function(data) {
					$scope.id = data['id'];
					$scope.firstname = data['firstName'];
					$scope.lastname = data['lastName'];
					$scope.email = data['email'];
					$scope.profilPictureId = data['profilePicture'];
					$scope.teams = data['teams'];
					$scope.getUserPicture();
				}, function(error) {
				});
			}
			$scope.loadUser();
			// method for file upload
			$scope.uploadFile = function() {
				var file = $scope.myFile;
				if (file !== undefined) {
					var uploadUrl = serverAddress
							+ "/DecisionDocu/api/upload/profilePicture/"
							+ $cookies['UserId'];
					fileUpload.uploadFileToUrl(file, uploadUrl, $scope);
					
				}
			};
			// method to load user picture
			$scope.getUserPicture = function() {
				UserPicture.get({
					id : $scope.profilPictureId
				},
						function(data) {
							var obj = angular.fromJson(data);
							$scope.profilePicture = ("data:" + obj.type
									+ ";base64," + obj.data);
						}, function(error) {

						});
			}
			// method to save user information (changed)
			$scope.save = function() {
				UpdateFirstName.save({
					id : $scope.id,
					firstName : $scope.firstname
				}, function(data) {
				}, function(error) {
				});
				UpdateLastName.save({
					id : $scope.id,
					lastName : $scope.lastname
				}, function(data) {
				}, function(error) {
				});	
				$scope.uploadFile();
			}
			// method to get profile picture
			$scope.downloadFile = function() {
				window.open($scope.profilePicture);
			}

		} ]);
