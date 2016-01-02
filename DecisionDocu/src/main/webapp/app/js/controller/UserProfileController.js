app.controller('UserProfileController', [
		'$scope',
		'$cookies',
		'fileUpload',
		'UserPerMail',
		'UserPicture',
		function($scope, $cookies, fileUpload, UserPerMail, UserPicture) {
			$scope.email = "";
			$scope.firstname = "";
			$scope.lastname = "";
			$scope.profilePicture = "";
			$scope.teams = new Array();

			UserPerMail.query({
				mail : $cookies['Mail']
			}, function(data) {
				$scope.firstname = data['firstName'];
				$scope.lastname = data['lastName'];
				$scope.email = data['eMail'];
				$scope.urlProfilePicture = data['urlProfilePicture'];
				$scope.teams = data['teams'];
			}, function(error) {
			});

			$scope.uploadFile = function() {
				var file = $scope.myFile;
				console.log('file is ');
				console.dir(file);
				var uploadUrl = serverAddress
						+ "/DecisionDocu/api/upload/profilePicture/"
						+ $cookies['UserId'];
				fileUpload.uploadFileToUrl(file, uploadUrl);
			};

			$scope.getUserPicture = function() {
				UserPicture.get({
					id : 6356
				}, function(data) {
					var obj = angular.fromJson(data);
					$scope.profilePicture = ("data:" + obj.type + ";base64," + obj.data);
				}, function(error) {
					alert("error");
				});
			}
			$scope.getUserPicture();
		} ]);
