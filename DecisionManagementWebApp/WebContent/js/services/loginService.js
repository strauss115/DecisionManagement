/**
 * service for the authentification
 */
var loginServices = angular.module('loginServices', ['ngResource']);


loginServices.factory('Login', ['$resource',
    function ($resource) {
        return $resource(serverAddress + '/DecisionDocu/api/user/login', {}, {});
    }]);

loginServices.factory('Register', ['$resource',
    function ($resource) {
        return $resource(serverAddress + '/DecisionDocu/api/user/register', {}, {});
    }]);




