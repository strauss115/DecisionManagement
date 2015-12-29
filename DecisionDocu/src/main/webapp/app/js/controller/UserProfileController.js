app.controller('UserProfileController', ['$scope', '$cookies', 'UserPerMail', function ($scope, $cookies, UserPerMail) {
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


    }]);

