app.service('fileUpload', ['$http', '$cookies', function ($http, $cookies) {
    this.uploadFileToUrl = function(file, uploadUrl){
        var fd = new FormData();
        fd.append('uploadFile', file);
        $http.post(uploadUrl, fd, {
            transformRequest: angular.identity,
            headers: {'Content-Type': undefined,
            	'token': $cookies['Token']}
        })
        .success(function(){
        	alert("success");
        })
        .error(function(){
        	alert("error");
        });
    }
}]);