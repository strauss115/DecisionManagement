/**
 * service for the authentification
 */
var loginServices = angular.module('loginServices', ['ngResource']);


loginServices.factory('Login', ['$resource',
    function ($resource) {
        return $resource(serverAddress + '/DecisionManagement/rest/user/login', {}, {
            query: {method: 'GET', isArray: false}
        });
    }]);

loginServices.factory('Register', ['$resource',
    function ($resource) {
        return $resource(serverAddress + '/DecisionManagement/rest/user/register', {}, {
            query: {method: 'GET', isArray: false}
        });
    }]);




