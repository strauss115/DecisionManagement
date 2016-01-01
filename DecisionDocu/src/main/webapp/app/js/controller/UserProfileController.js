app.controller('UserProfileController', ['$scope', '$cookies', 'fileUpload', 'UserPerMail', function ($scope, $cookies, fileUpload, UserPerMail) {
        $scope.email = "";
        $scope.firstname = "";
        $scope.lastname = "";
        $scope.urlProfilePicture = "";
        $scope.teams = new Array();
        
        UserPerMail.query({mail: $cookies['Mail']}, function (data) {
            $scope.firstname = data['firstName'];
            $scope.lastname = data['lastName'];
            $scope.email = data['eMail'];
            $scope.urlProfilePicture = data['urlProfilePicture'];
            $scope.teams = data['teams'];
        }, function (error) {
        });
        
        $scope.uploadFile = function(){
            var file = $scope.myFile;
            console.log('file is ' );
            console.dir(file);
            var uploadUrl = serverAddress + "/DecisionDocu/api/upload/profilePicture/" + $cookies['UserId'];
            fileUpload.uploadFileToUrl(file, uploadUrl);
        };
}]);

