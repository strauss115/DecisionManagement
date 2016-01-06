app.service('fileUpload', ['$http', '$cookies', function ($http, $cookies) {
    this.uploadFileToUrl = function(file, uploadUrl, $scope){
        var fd = new FormData();
        fd.append('uploadFile', file);
        $http.post(uploadUrl, fd, {
            transformRequest: angular.identity,
            headers: {'Content-Type': undefined,
            	'token': $cookies['Token']}
        })
        .success(function(){
        	$scope.loadUser();
        })
        .error(function(){

        });
    }
}]);

app.service('fileUpload1', ['$http', '$cookies', function ($http, $cookies) {
    this.uploadFileToUrl = function(file, uploadUrl, $scope){
        var fd = new FormData();
        fd.append('uploadFile', file);
        $http.post(uploadUrl, fd, {
            transformRequest: angular.identity,
            headers: {'Content-Type': undefined,
            	'token': $cookies['Token']}
        })
        .success(function(){
        })
        .error(function(){

        });
    }
}]);