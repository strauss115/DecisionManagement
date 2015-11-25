/**
 * service for the authentification
 */
var loginServices = angular.module('loginServices', ['ngResource']);

loginServices.factory('Login', ['$resource',
    function ($resource) {
        alert($scope.eMail);
        return $resource('http://192.168.0.102:8181/DecisionManagement/rest/user/login', {}, {
            query: {method: 'GET', params: {eMail: $scope.eMail, password: $scope.password}, isArray: true}
        });
    }]);