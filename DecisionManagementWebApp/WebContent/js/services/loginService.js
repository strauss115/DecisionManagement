/**
 * service for the authentification
 */
var loginServices = angular.module('loginServices', ['ngResource']);


loginServices.factory('Login', ['$resource',
    function ($resource) {
        return $resource('http://192.168.0.102:8181/DecisionManagement/rest/user/login', {}, {
            query: {method: 'GET', isArray: false},
        });
    }]);




